/*
 * *********************************************************
 * Copyright (c) 2009 - 2011, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: 28.11.2011
 * Author(s): Manuel
 * 
 * *********************************************************
 */
package edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.rcm.inputDevice;

import java.util.Map;

import edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.botmanager.bots.ABot;
import edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.rcm.ActionCommand;
import edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.rcm.inputDevice.sendAction.ARCCommandInterpreter;
import edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.rcm.inputDevice.sendAction.ActionSender;
import edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.rcm.inputDevice.sendAction.TigerV2Interpreter;


/**
 * Abstract Input Device implementation for RCM.
 * 
 * @author Manuel
 * 
 */
public abstract class AInputDevice
{
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private final ARCCommandInterpreter	cmdInterpreter;
	
	private final ActionSender				actionSender;
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * @param bot
	 */
	public AInputDevice(ABot bot)
	{
		switch (bot.getType())
		{
			case TIGER:
			case GRSIM:
			case TIGER_V2:
				cmdInterpreter = new TigerV2Interpreter(bot);
				break;
			default:
				throw new IllegalArgumentException("Invalid bot type: " + bot.getType());
		}
		
		actionSender = new ActionSender();
		actionSender.startSending(cmdInterpreter);
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * @param command
	 */
	public void execute(ActionCommand command)
	{
		actionSender.setNewCmd(command);
	}
	
	
	/**
	 * @param currentConfig
	 */
	public abstract void setCurrentConfig(Map<String, String> currentConfig);
	
	
	protected void stopSending()
	{
		actionSender.stopSending();
	}
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
}
