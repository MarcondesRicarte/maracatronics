/*
 * *********************************************************
 * Copyright (c) 2009 - 2010, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: 17.10.2010
 * Author(s): Malte
 * *********************************************************
 */
package edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.ai.pandora.plays.others;

import edu.dhbw.mannheim.tigers.sumatra.model.data.area.Goal;
import edu.dhbw.mannheim.tigers.sumatra.model.data.frames.AthenaAiFrame;
import edu.dhbw.mannheim.tigers.sumatra.model.data.math.AngleMath;
import edu.dhbw.mannheim.tigers.sumatra.model.data.modules.ai.EGameState;
import edu.dhbw.mannheim.tigers.sumatra.model.data.shapes.vector.IVector2;
import edu.dhbw.mannheim.tigers.sumatra.model.data.shapes.vector.Vector2;
import edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.ai.config.AIConfig;
import edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.ai.pandora.plays.APlay;
import edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.ai.pandora.plays.EPlay;
import edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.ai.pandora.roles.ARole;
import edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.ai.pandora.roles.move.MoveRole;
import edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.ai.pandora.roles.move.MoveRole.EMoveBehavior;


/**
 * All available Robots shall move on a circle around the ball-position.
 * 
 * @author Malte, OliverS
 */
public class AroundTheBallPlay extends APlay
{
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	
	private final Goal	goal		= AIConfig.getGeometry().getGoalOur();
	
	private final float	radius	= AIConfig.getGeometry().getBotToBallDistanceStop();
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 */
	public AroundTheBallPlay()
	{
		super(EPlay.AROUND_THE_BALL);
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	@Override
	protected void doUpdate(final AthenaAiFrame currentFrame)
	{
		IVector2 ballPos = new Vector2(currentFrame.getWorldFrame().ball.getPos());
		
		// direction: vector from ball to the middle of the goal!
		Vector2 direction = goal.getGoalCenter().subtractNew(ballPos);
		// sets the length of the vector to 'radius'
		direction.scaleTo(radius);
		
		float turn = AngleMath.PI / 2;
		
		for (final ARole role : getRoles())
		{
			final MoveRole moveRole = (MoveRole) role;
			
			final IVector2 destination = ballPos.addNew(direction.turnNew(turn));
			
			moveRole.getMoveCon().updateDestination(destination);
			turn -= AngleMath.PI / 5;
		}
	}
	
	
	@Override
	protected ARole onRemoveRole()
	{
		return getLastRole();
	}
	
	
	@Override
	protected ARole onAddRole()
	{
		return (new MoveRole(EMoveBehavior.LOOK_AT_BALL));
	}
	
	
	@Override
	protected void onGameStateChanged(final EGameState gameState)
	{
	}
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
}
