/*
 * *********************************************************
 * Copyright (c) 2009 - 2013, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: 22.04.2013
 * Author(s): AndreR
 * 
 * *********************************************************
 */
package edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.botmanager.commands.basestation;

import edu.dhbw.mannheim.tigers.sumatra.model.data.modules.ai.ETeamColor;
import edu.dhbw.mannheim.tigers.sumatra.model.data.trackedobjects.ids.AObjectID;
import edu.dhbw.mannheim.tigers.sumatra.model.data.trackedobjects.ids.BotID;
import edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.botmanager.commands.ACommand;
import edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.botmanager.commands.CommandFactory;
import edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.botmanager.commands.ECommand;
import edu.dhbw.mannheim.tigers.sumatra.util.serial.SerialData;
import edu.dhbw.mannheim.tigers.sumatra.util.serial.SerialData.ESerialDataType;


/**
 * Packs a ACommand in an ACommand and prepends an ID.
 * This is a varying length command!
 * 
 * @author AndreR
 * 
 */
public class BaseStationACommand extends ACommand
{
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	// Cached variables
	private ACommand	child	= null;
	private BotID		id		= null;
	
	@SerialData(type = ESerialDataType.UINT8)
	private int			idData;
	@SerialData(type = ESerialDataType.TAIL)
	private byte[]		childData;
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	/** */
	public BaseStationACommand()
	{
		super(ECommand.CMD_BASE_ACOMMAND);
	}
	
	
	/**
	 * 
	 * @param id
	 * @param command
	 */
	public BaseStationACommand(BotID id, ACommand command)
	{
		super(ECommand.CMD_BASE_ACOMMAND);
		
		setId(id);
		setChild(command);
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * @return the child
	 */
	public ACommand getChild()
	{
		if (child == null)
		{
			child = CommandFactory.getInstance().decode(childData, false);
		}
		
		return child;
	}
	
	
	/**
	 * @param child the child to set
	 */
	public void setChild(ACommand child)
	{
		childData = CommandFactory.getInstance().encode(child, false);
		this.child = child;
	}
	
	
	/**
	 * @return the id
	 */
	public BotID getId()
	{
		if (id == null)
		{
			id = getBotIdFromBaseStationId(idData);
		}
		
		return id;
	}
	
	
	/**
	 * @param id the id to set
	 */
	public void setId(BotID id)
	{
		idData = getBaseStationIdFromBotId(id);
		this.id = id;
	}
	
	
	/**
	 * Transform a botID in base station format to Sumatra BotID.
	 * Base station used id 0-11 for yellow and 12-23 for blue.
	 * ID 255 is an unused slot in base station.
	 * 
	 * @param id
	 * @return
	 */
	public static BotID getBotIdFromBaseStationId(int id)
	{
		if (id == AObjectID.UNINITIALIZED_ID)
		{
			return BotID.createBotId();
		}
		
		if (id > AObjectID.BOT_ID_MAX)
		{
			return BotID.createBotId(id - (AObjectID.BOT_ID_MAX + 1), ETeamColor.BLUE);
		}
		
		return BotID.createBotId(id, ETeamColor.YELLOW);
	}
	
	
	/**
	 * Transform a Sumatra BotID to base station format.
	 * 
	 * @param id
	 * @return
	 */
	public static int getBaseStationIdFromBotId(BotID id)
	{
		if (id.getNumber() == AObjectID.UNINITIALIZED_ID)
		{
			return 255;
		}
		
		if (id.getTeamColor() == ETeamColor.BLUE)
		{
			return id.getNumber() + AObjectID.BOT_ID_MAX + 1;
		}
		
		return id.getNumber();
	}
}
