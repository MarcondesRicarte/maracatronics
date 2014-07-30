/*
 * *********************************************************
 * Copyright (c) 2009 - 2011, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: 09.11.2011
 * Author(s): Sven Frank
 * 
 * *********************************************************
 */
package edu.dhbw.mannheim.tigers.sumatra.view.rcm;

import java.util.List;

import javax.swing.JMenu;
import javax.swing.JPanel;

import edu.dhbw.mannheim.tigers.sumatra.views.ISumatraView;


/**
 * RCM (Robot Control Manager) view
 * 
 * @author Sven Frank
 * 
 */

public class RobotControlManagerPanel extends JPanel implements ISumatraView
{
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final long					serialVersionUID	= 3125761918996725026L;
	
	private static final ShowRCMMainPanel	RCM_MAIN_PANEL		= ShowRCMMainPanel.getInstance();
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 */
	public RobotControlManagerPanel()
	{
		this.add(RCM_MAIN_PANEL);
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 */
	public void start()
	{
		RCM_MAIN_PANEL.start();
	}
	
	
	/**
	 */
	public void stop()
	{
		RCM_MAIN_PANEL.stop();
	}
	
	
	@Override
	public List<JMenu> getCustomMenus()
	{
		return null;
	}
	
	
	@Override
	public void onShown()
	{
	}
	
	
	@Override
	public void onHidden()
	{
	}
	
	
	@Override
	public void onFocused()
	{
	}
	
	
	@Override
	public void onFocusLost()
	{
	}
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
