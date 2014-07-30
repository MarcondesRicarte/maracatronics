/*
 * *********************************************************
 * Copyright (c) 2009 - 2010, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: 30.08.2010
 * Author(s): AndreR
 * *********************************************************
 */
package edu.dhbw.mannheim.tigers.sumatra.view.botcenter.internals.bots.tiger;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;
import edu.dhbw.mannheim.tigers.sumatra.model.data.modules.ai.ETeamColor;
import edu.dhbw.mannheim.tigers.sumatra.model.data.trackedobjects.ids.BotID;
import edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.botmanager.bots.communication.ENetworkState;


/**
 * Main panel for the tiger bot.
 *
 * @author AndreR
 */
public class TigerBotMainPanel extends JPanel
{
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final long								serialVersionUID	= 8270294413292980459L;
	
	private final JTextField								id						= new JTextField();
	private final JTextField								name					= new JTextField();
	private final JTextField								ip						= new JTextField();
	private final JTextField								port					= new JTextField();
	private final JTextField								status				= new JTextField();
	private final JTextField								mac					= new JTextField();
	private final JTextField								serverPort			= new JTextField();
	private final JTextField								cpuId					= new JTextField();
	private final JCheckBox									useUpdateAll		= new JCheckBox();
	private final JCheckBox									logMovement			= new JCheckBox();
	private final JCheckBox									logKicker			= new JCheckBox();
	private final JCheckBox									logPower				= new JCheckBox();
	private final JCheckBox									logIr					= new JCheckBox();
	private final JButton									connect				= new JButton("Connect");
	private final JComboBox<ETeamColor>					color					= new JComboBox<ETeamColor>(new ETeamColor[] {
			ETeamColor.YELLOW, ETeamColor.BLUE });
	
	private final List<ITigerBotMainPanelObserver>	observers			= new ArrayList<ITigerBotMainPanelObserver>();
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 *
	 */
	public TigerBotMainPanel()
	{
		setLayout(new MigLayout("fill"));
		
		status.setEditable(false);
		
		final JButton saveGeneral = new JButton("Save");
		final JButton saveLogs = new JButton("Apply");
		
		saveGeneral.addActionListener(new SaveGeneral());
		connect.addActionListener(new ConnectionChange());
		saveLogs.addActionListener(new SaveLogs());
		
		final JPanel general = new JPanel(new MigLayout("fill", "[50]10[200,fill]"));
		final JPanel network = new JPanel(new MigLayout("fill", "[50][100,fill]10[100,fill]"));
		final JPanel logs = new JPanel(new MigLayout("fill", "[100]10[150,fill]"));
		
		general.setBorder(BorderFactory.createTitledBorder("General"));
		network.setBorder(BorderFactory.createTitledBorder("Network"));
		logs.setBorder(BorderFactory.createTitledBorder("Logs"));
		
		general.add(new JLabel("ID: "));
		general.add(id, "wrap");
		general.add(new JLabel("Name:"));
		general.add(name, "wrap");
		general.add(new JLabel("CPU ID:"));
		general.add(cpuId, "wrap");
		general.add(new JLabel("MAC:"));
		general.add(mac, "wrap");
		general.add(new JLabel("IP:"));
		general.add(ip, "wrap");
		general.add(new JLabel("Port:"));
		general.add(port, "wrap");
		general.add(new JLabel("Server Port:"));
		general.add(serverPort, "wrap");
		general.add(new JLabel("Color:"));
		general.add(color, "wrap");
		general.add(new JLabel("Use UpdateAll:"));
		general.add(useUpdateAll, "wrap");
		general.add(saveGeneral);
		
		network.add(new JLabel("Status:"));
		network.add(status);
		network.add(connect);
		
		logs.add(new JLabel("Movement:"));
		logs.add(logMovement, "wrap");
		logs.add(new JLabel("Kicker:"));
		logs.add(logKicker, "wrap");
		logs.add(new JLabel("Power:"));
		logs.add(logPower, "wrap");
		logs.add(new JLabel("IR:"));
		logs.add(logIr, "wrap");
		logs.add(saveLogs, "span 2");
		
		add(general, "wrap");
		add(network, "wrap");
		add(logs, "wrap");
		add(Box.createGlue(), "push");
	}
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * @param observer
	 */
	public void addObserver(final ITigerBotMainPanelObserver observer)
	{
		synchronized (observers)
		{
			observers.add(observer);
		}
	}
	
	
	/**
	 * @param observer
	 */
	public void removeObserver(final ITigerBotMainPanelObserver observer)
	{
		synchronized (observers)
		{
			observers.remove(observer);
		}
	}
	
	
	/**
	 * @param idVal
	 */
	public void setId(final BotID idVal)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				id.setText(String.valueOf(idVal.getNumber()));
			}
		});
	}
	
	
	/**
	 * @return
	 * @throws NumberFormatException
	 */
	public int getId()
	{
		return Integer.parseInt(id.getText());
	}
	
	
	/**
	 * @param nameVal
	 */
	public void setBotName(final String nameVal)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				name.setText(nameVal);
			}
		});
	}
	
	
	/**
	 * @return
	 */
	public String getBotName()
	{
		return name.getText();
	}
	
	
	/**
	 * @param ipVal
	 */
	public void setIp(final String ipVal)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				ip.setText(ipVal);
			}
		});
	}
	
	
	/**
	 * @return
	 */
	public String getIp()
	{
		return ip.getText();
	}
	
	
	/**
	 * @param portVal
	 */
	public void setPort(final int portVal)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				port.setText(String.valueOf(portVal));
			}
		});
	}
	
	
	/**
	 * @return
	 * @throws NumberFormatException
	 */
	public int getPort()
	{
		return Integer.parseInt(port.getText());
	}
	
	
	/**
	 * @param state
	 */
	public void setConnectionState(final ENetworkState state)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				switch (state)
				{
					case OFFLINE:
						status.setText("Offline");
						connect.setText("Connect");
						status.setBackground(new Color(255, 128, 128));
						break;
					case CONNECTING:
						status.setText("Connecting");
						connect.setText("Disconnect");
						status.setBackground(Color.CYAN);
						break;
					case ONLINE:
						status.setText("Online");
						connect.setText("Disconnect");
						status.setBackground(Color.GREEN);
						break;
				}
			}
		});
	}
	
	
	/**
	 * @param id
	 */
	public void setCpuId(final String id)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				cpuId.setText(id);
			}
		});
	}
	
	
	/**
	 * @return
	 */
	public String getCpuId()
	{
		return cpuId.getText();
	}
	
	
	/**
	 * @param newMac
	 */
	public void setMac(final String newMac)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				mac.setText(newMac);
			}
		});
	}
	
	
	/**
	 * @return
	 */
	public String getMac()
	{
		return mac.getText();
	}
	
	
	/**
	 * @param portVal
	 */
	public void setServerPort(final int portVal)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				serverPort.setText(String.valueOf(portVal));
			}
		});
	}
	
	
	/**
	 * @return
	 * @throws NumberFormatException
	 */
	public int getServerPort()
	{
		return Integer.parseInt(serverPort.getText());
	}
	
	
	/**
	 * @param enable
	 */
	public void setUseUpdateAll(final boolean enable)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				useUpdateAll.setSelected(enable);
			}
		});
	}
	
	
	/**
	 * @return
	 */
	public boolean getUseUpdateAll()
	{
		return useUpdateAll.isSelected();
	}
	
	
	/**
	 * @return
	 */
	public boolean getLogMovement()
	{
		return logMovement.isSelected();
	}
	
	
	/**
	 * @param enable
	 */
	public void setLogMovement(final boolean enable)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				logMovement.setSelected(enable);
			}
		});
	}
	
	
	/**
	 * @return
	 */
	public boolean getLogKicker()
	{
		return logKicker.isSelected();
	}
	
	
	/**
	 * @param enable
	 */
	public void setLogKicker(final boolean enable)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				logKicker.setSelected(enable);
			}
		});
	}
	
	
	/**
	 * @return
	 */
	public boolean getLogPower()
	{
		return logPower.isSelected();
	}
	
	
	/**
	 * @param enable
	 */
	public void setLogPower(final boolean enable)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				logPower.setSelected(enable);
			}
		});
	}
	
	
	/**
	 * @return
	 */
	public boolean getLogIr()
	{
		return logIr.isSelected();
	}
	
	
	/**
	 * @param enable
	 */
	public void setLogIr(final boolean enable)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				logIr.setSelected(enable);
			}
		});
	}
	
	
	/**
	 * @return
	 */
	public ETeamColor getColor()
	{
		return (ETeamColor) color.getSelectedItem();
	}
	
	
	/**
	 * @param c
	 */
	public void setColor(final ETeamColor c)
	{
		color.setSelectedItem(c);
	}
	
	
	protected void notifySaveGeneral()
	{
		synchronized (observers)
		{
			for (final ITigerBotMainPanelObserver o : observers)
			{
				o.onSaveGeneral();
			}
		}
	}
	
	
	protected void notifyConnectionChange()
	{
		synchronized (observers)
		{
			for (final ITigerBotMainPanelObserver o : observers)
			{
				o.onConnectionChange();
			}
		}
	}
	
	
	private void notifySaveLogs()
	{
		synchronized (observers)
		{
			for (final ITigerBotMainPanelObserver observer : observers)
			{
				observer.onSaveLogs();
			}
		}
	}
	
	// --------------------------------------------------------------------------
	// --- Actions --------------------------------------------------------------
	// --------------------------------------------------------------------------
	protected class SaveGeneral implements ActionListener
	{
		@Override
		public void actionPerformed(final ActionEvent arg0)
		{
			notifySaveGeneral();
		}
	}
	
	protected class ConnectionChange implements ActionListener
	{
		@Override
		public void actionPerformed(final ActionEvent e)
		{
			notifyConnectionChange();
		}
	}
	
	protected class SaveLogs implements ActionListener
	{
		@Override
		public void actionPerformed(final ActionEvent e)
		{
			notifySaveLogs();
		}
	}
}
