/*
 * *********************************************************
 * Copyright (c) 2009 - 2010, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: 30.08.2010
 * Author(s): AndreR
 * 
 * *********************************************************
 */
package edu.dhbw.mannheim.tigers.sumatra.view.botcenter.internals.bots.tiger;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;
import edu.dhbw.mannheim.tigers.sumatra.model.data.trackedobjects.ids.BotID;
import edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.botmanager.bots.communication.ENetworkState;


/**
 * Tiger bot summary for the overview panel.
 * 
 * @author AndreR
 * 
 */
public class TigerBotSummary extends JPanel
{
	/**
	 *
	 */
	public interface ITigerBotSummaryObserver
	{
		/** */
		void onConnectionChange();
		
		
		/**
		 * @param multicast
		 */
		void onConnectionTypeChange(boolean multicast);
		
		
		/**
		 * @param oofCheck
		 */
		void onOOFCheckChange(boolean oofCheck);
	}
	
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final long							serialVersionUID	= 5485796598824650963L;
	private final JTextField							status;
	private final JRadioButton							unicast;
	private final JRadioButton							multicast;
	private final JCheckBox								oofCheck;
	private final JProgressBar							battery;
	private final JButton								connect;
	
	private String											name;
	private BotID									id;
	private ENetworkState								netState;
	
	private final List<ITigerBotSummaryObserver>	observers			= new ArrayList<ITigerBotSummaryObserver>();
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * 
	 */
	public TigerBotSummary()
	{
		setLayout(new MigLayout("fill", "[100,fill]10[100,fill]30[60]10[60]10[60]30[40]5[60]", "0[]0"));
		
		name = "Bob";
		id = BotID.createBotId();
		netState = ENetworkState.CONNECTING;
		
		status = new JTextField();
		status.setEditable(false);
		unicast = new JRadioButton("Unicast");
		multicast = new JRadioButton("Multicast");
		oofCheck = new JCheckBox("OOF Check");
		battery = new JProgressBar(12800, 16800);
		battery.setStringPainted(true);
		
		final ButtonGroup netGroup = new ButtonGroup();
		netGroup.add(unicast);
		netGroup.add(multicast);
		
		unicast.setSelected(true);
		
		connect = new JButton("Disconnect");
		
		connect.addActionListener(new Connect());
		unicast.addActionListener(new NetworkChanged());
		multicast.addActionListener(new NetworkChanged());
		oofCheck.addActionListener(new OOFChange());
		
		add(status);
		add(connect);
		add(unicast);
		add(multicast);
		add(oofCheck);
		add(new JLabel("Battery:"));
		add(battery, "growy");
		
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Trish (1)"));
		
		setNetworkState(ENetworkState.OFFLINE);
		setBatteryLevel(14.2f);
	}
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * @param observer
	 */
	public void addObserver(ITigerBotSummaryObserver observer)
	{
		synchronized (observers)
		{
			observers.add(observer);
		}
	}
	
	
	/**
	 * 
	 * @param observer
	 */
	public void removeObserver(ITigerBotSummaryObserver observer)
	{
		synchronized (observers)
		{
			observers.remove(observer);
		}
	}
	
	
	/**
	 * 
	 * @param id
	 */
	public void setId(BotID id)
	{
		this.id = id;
		updateTitle();
	}
	
	
	/**
	 * 
	 * @param name
	 */
	public void setBotName(String name)
	{
		this.name = name;
		updateTitle();
	}
	
	
	/**
	 * 
	 * @param voltage
	 */
	public void setBatteryLevel(final float voltage)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				battery.setValue((int) (voltage * 1000));
				battery.setString(String.format(Locale.ENGLISH, "%1.2f V", voltage));
			}
		});
	}
	
	
	/**
	 * 
	 * @param enable
	 */
	public void setMulticast(final boolean enable)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				if (enable)
				{
					multicast.setSelected(true);
				} else
				{
					unicast.setSelected(true);
				}
			}
		});
	}
	
	
	/**
	 * @param enable
	 */
	public void setOofCheck(final boolean enable)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				oofCheck.setSelected(enable);
			}
		});
	}
	
	
	/**
	 * @param state
	 */
	public void setNetworkState(ENetworkState state)
	{
		netState = state;
		updateTitle();
	}
	
	
	private void notifyConnectionChange()
	{
		synchronized (observers)
		{
			for (final ITigerBotSummaryObserver observer : observers)
			{
				observer.onConnectionChange();
			}
		}
	}
	
	
	private void notifyConnectionTypeChange(boolean multicast)
	{
		synchronized (observers)
		{
			for (final ITigerBotSummaryObserver observer : observers)
			{
				observer.onConnectionTypeChange(multicast);
			}
		}
	}
	
	
	private void notifyOOFChange(boolean oofCheck)
	{
		synchronized (observers)
		{
			for (final ITigerBotSummaryObserver observer : observers)
			{
				observer.onOOFCheckChange(oofCheck);
			}
		}
	}
	
	
	private void updateTitle()
	{
		final String title = String.format("[ %d ] %s", id.getNumber(), name);
		Color borderColor = null;
		String stateText = "";
		String buttonText = "Disconnect";
		
		switch (netState)
		{
			case OFFLINE:
				stateText = "Offline";
				borderColor = Color.RED;
				buttonText = "Connect";
				break;
			case CONNECTING:
				stateText = "Connecting";
				borderColor = Color.BLUE;
				break;
			case ONLINE:
				stateText = "Online";
				borderColor = Color.GREEN;
				break;
		}
		
		final Color col = borderColor;
		final String text = stateText;
		final String bText = buttonText;
		
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(col), title));
				status.setText(text);
				connect.setText(bText);
			}
		});
	}
	
	private class NetworkChanged implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			notifyConnectionTypeChange(multicast.isSelected());
		}
	}
	
	private class Connect implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			notifyConnectionChange();
		}
	}
	
	private class OOFChange implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			notifyOOFChange(oofCheck.isSelected());
		}
	}
}
