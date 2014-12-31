/*
 * *********************************************************
 * Copyright (c) 2009 - 2012, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: Nov 10, 2012
 * Author(s): Nicolai Ommer <nicolai.ommer@gmail.com>
 * *********************************************************
 */
package edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.ai.pandora.conditions;

import edu.dhbw.mannheim.tigers.sumatra.model.data.frames.SimpleWorldFrame;
import edu.dhbw.mannheim.tigers.sumatra.model.data.trackedobjects.ids.BotID;
import edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.ai.pandora.ACondition;


/**
 * This condition will do nothing. It is always false and will never be equal to any other condition
 * It is used for quite a dirty workaround in a JTable :P
 * 
 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
 */
public class DummyCondition extends ACondition
{
	// DAFUQ GAMBIARRA
	
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	/**
	 * @param type
	 */
	public DummyCondition(final ECondition type)
	{
		super(type);
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	@Override
	protected EConditionState doCheckCondition(final SimpleWorldFrame worldFrame, final BotID botId)
	{
		return EConditionState.PENDING;
	}
	
	
	@Override
	protected boolean compareContent(final ACondition condition)
	{
		return false;
	}
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
