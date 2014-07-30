/*
 * *********************************************************
 * Copyright (c) 2009 - 2010, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: 12.09.2010
 * Author(s): Oliver Steinbrecher <OST1988@aol.com>
 * *********************************************************
 */
package edu.dhbw.mannheim.tigers.sumatra.view.aicenter.internals.moduleoverview;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.DefaultButtonModel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;
import edu.dhbw.mannheim.tigers.sumatra.presenter.aicenter.EAIControlState;


/**
 * This panel controls the ai sub-modules.
 * 
 * @author Oliver, Malte
 */
public class ModuleControlPanel extends JPanel implements IAIModeChanged
{
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final long						serialVersionUID	= -2509991904665753934L;
	
	private final JRadioButton						matchControl;
	private final JRadioButton						mixedTeamControl;
	private final JRadioButton						athenaControl;
	private final JRadioButton						emergencyControl;
	
	private final JTabbedPane						tabbedPane;
	private final PlayControlPanel				playPanel;
	private final RoleControlPanel				rolePanel;
	private final TacticalFieldControlPanel	tacticalFieldPanel;
	private final MetisCalculatorsPanel			metisCalculatorsPanel;
	private final ButtonGroup						modeGroup;
	
	private final List<IAIModeChanged>			observers			= new CopyOnWriteArrayList<IAIModeChanged>();
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 */
	public ModuleControlPanel()
	{
		setLayout(new MigLayout("insets 0 0 0 0"));
		
		final JPanel controlPanel = new JPanel(new MigLayout("insets 0 0 0 0", "", ""));
		matchControl = new JRadioButton("Match");
		matchControl.addActionListener(new MatchModeControlListener());
		controlPanel.add(matchControl);
		
		mixedTeamControl = new JRadioButton("Mixed Team");
		mixedTeamControl.addActionListener(new MixedTeamControlListener());
		controlPanel.add(mixedTeamControl);
		
		athenaControl = new JRadioButton("Test");
		athenaControl.addActionListener(new AthenaControlListener());
		controlPanel.add(athenaControl);
		
		emergencyControl = new JRadioButton("Emergency");
		emergencyControl.addActionListener(new EmergencyControlListener());
		controlPanel.add(emergencyControl);
		
		modeGroup = new ButtonGroup();
		modeGroup.add(matchControl);
		modeGroup.add(mixedTeamControl);
		modeGroup.add(athenaControl);
		modeGroup.add(emergencyControl);
		final ButtonModel btnModel = new DefaultButtonModel();
		btnModel.setGroup(modeGroup);
		matchControl.setSelected(true);
		
		
		final JPanel moduleStates = new JPanel(new MigLayout(""));
		moduleStates.add(controlPanel);
		
		playPanel = new PlayControlPanel();
		rolePanel = new RoleControlPanel();
		tacticalFieldPanel = new TacticalFieldControlPanel();
		metisCalculatorsPanel = new MetisCalculatorsPanel();
		
		final JPanel lachesisPanel = new JPanel(new MigLayout("fill"));
		lachesisPanel.add(rolePanel);
		
		
		tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Plays", playPanel);
		tabbedPane.addTab("Roles", rolePanel);
		tabbedPane.addTab("Metis", tacticalFieldPanel);
		tabbedPane.addTab("Metis Calcs", metisCalculatorsPanel);
		
		
		add(moduleStates, "wrap");
		add(tabbedPane);
		
		
		// Initialize
		onStop();
	}
	
	
	/**
	 * @param observer
	 */
	public void addObserver(final IAIModeChanged observer)
	{
		synchronized (observers)
		{
			observers.add(observer);
		}
	}
	
	
	/**
	 * @param observer
	 */
	public void removeObserver(final IAIModeChanged observer)
	{
		synchronized (observers)
		{
			observers.remove(observer);
		}
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	/**
	 * @return
	 */
	public PlayControlPanel getPlayPanel()
	{
		return playPanel;
	}
	
	
	/**
	 * @return
	 */
	public RoleControlPanel getRolePanel()
	{
		return rolePanel;
	}
	
	
	/**
	 * @return
	 */
	public TacticalFieldControlPanel getTacticalFieldControlPanel()
	{
		return tacticalFieldPanel;
	}
	
	
	@Override
	public void onAiModeChanged(final EAIControlState mode)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				switch (mode)
				{
					case EMERGENCY_MODE:
						emergencyControl.setSelected(true);
						break;
					case MATCH_MODE:
						tabbedPane.setSelectedComponent(playPanel);
						matchControl.setSelected(true);
						break;
					case MIXED_TEAM_MODE:
						tabbedPane.setSelectedComponent(playPanel);
						mixedTeamControl.setSelected(true);
						break;
					case TEST_MODE:
						athenaControl.setSelected(true);
						break;
					default:
						break;
				
				}
				playPanel.onAiModeChanged(mode);
				rolePanel.onAiModeChanged(mode);
			}
		});
	}
	
	
	/**
	 */
	public void onStart()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				setEnabled(true);
				
				matchControl.setEnabled(true);
				mixedTeamControl.setEnabled(true);
				athenaControl.setEnabled(true);
				emergencyControl.setEnabled(true);
				// onAiModeChanged(EAIControlState.TEST_MODE);
			}
		});
	}
	
	
	/**
	 */
	public void onStop()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				setEnabled(false);
				matchControl.setEnabled(false);
				mixedTeamControl.setEnabled(false);
				athenaControl.setEnabled(false);
				emergencyControl.setEnabled(false);
				// onAiModeChanged(EAIControlState.EMERGENCY_MODE);
			}
		});
	}
	
	
	private class AthenaControlListener implements ActionListener
	{
		@Override
		public void actionPerformed(final ActionEvent e)
		{
			for (IAIModeChanged o : observers)
			{
				o.onAiModeChanged(EAIControlState.TEST_MODE);
			}
		}
	}
	
	private class MatchModeControlListener implements ActionListener
	{
		@Override
		public void actionPerformed(final ActionEvent e)
		{
			for (IAIModeChanged o : observers)
			{
				o.onAiModeChanged(EAIControlState.MATCH_MODE);
			}
		}
	}
	
	private class MixedTeamControlListener implements ActionListener
	{
		@Override
		public void actionPerformed(final ActionEvent e)
		{
			for (IAIModeChanged o : observers)
			{
				o.onAiModeChanged(EAIControlState.MIXED_TEAM_MODE);
			}
		}
	}
	
	
	private class EmergencyControlListener implements ActionListener
	{
		@Override
		public void actionPerformed(final ActionEvent arg0)
		{
			for (IAIModeChanged o : observers)
			{
				o.onAiModeChanged(EAIControlState.EMERGENCY_MODE);
			}
		}
	}
	
	
	/**
	 * @return the metisCalculatorsPanel
	 */
	public final MetisCalculatorsPanel getMetisCalculatorsPanel()
	{
		return metisCalculatorsPanel;
	}
	
}
