/*
 * *********************************************************
 * Copyright (c) 2009 - 2014, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: Jan 23, 2014
 * Author(s): Nicolai Ommer <nicolai.ommer@gmail.com>
 * *********************************************************
 */
package edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.ai.metis.calculators.general;

import edu.dhbw.mannheim.tigers.sumatra.model.data.frames.BaseAiFrame;
import edu.dhbw.mannheim.tigers.sumatra.model.data.math.GeoMath;
import edu.dhbw.mannheim.tigers.sumatra.model.data.modules.ai.EGameState;
import edu.dhbw.mannheim.tigers.sumatra.model.data.modules.ai.TacticalField;
import edu.dhbw.mannheim.tigers.sumatra.model.data.modules.referee.ETeamSpecRefCmd;
import edu.dhbw.mannheim.tigers.sumatra.model.data.modules.referee.RefereeMsg;
import edu.dhbw.mannheim.tigers.sumatra.model.data.shapes.vector.IVector2;
import edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.ai.config.AIConfig;
import edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.ai.metis.calculators.ACalculator;


/**
 * Check in which game state we are by consulting new referee messages
 * 
 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
 */
public class GameStateCalc extends ACalculator
{
	
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	
	private static final float	BALL_MOVED_DISTANCE_TOL	= 50;
	private IVector2				ballPosOnPrepare			= null;
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	@Override
	public void doCalc(final TacticalField newTacticalField, final BaseAiFrame baseAiFrame)
	{
		final EGameState lastGameState = baseAiFrame.getPrevFrame().getTacticalField().getGameState();
		final RefereeMsg refereeMsg = baseAiFrame.getNewRefereeMsg();
		EGameState newGameState = lastGameState;
		
		if (refereeMsg != null)
		{
			switch (refereeMsg.getTeamSpecRefCmd())
			{
				case DirectFreeKickEnemies:
					if (AIConfig.getGeometry().getField().isPointInShape(baseAiFrame.getWorldFrame().getBall().getPos()))
					{
						newGameState = EGameState.DIRECT_KICK_THEY;
					} else if (baseAiFrame.getWorldFrame().getBall().getPos().x() < 0)
					{
						// ball is in our half, freekick is awarded to enemies => corner kick
						newGameState = EGameState.CORNER_KICK_THEY;
					} else
					{
						// ball is in their half, freekick is awarded to enemies => goal kick
						newGameState = EGameState.GOAL_KICK_THEY;
					}
					break;
				case DirectFreeKickTigers:
					if (AIConfig.getGeometry().getField().isPointInShape(baseAiFrame.getWorldFrame().getBall().getPos()))
					{
						newGameState = EGameState.DIRECT_KICK_WE;
					} else if (baseAiFrame.getWorldFrame().getBall().getPos().x() < 0)
					{
						// ball is in our half, freekick is awarded to us => goal kick
						newGameState = EGameState.GOAL_KICK_WE;
					} else
					{
						// ball is in their half, freekick is awarded to us => corner kick
						newGameState = EGameState.CORNER_KICK_WE;
					}
					break;
				case ForceStart:
					newGameState = EGameState.RUNNING;
					break;
				case GoalEnemies:
					break;
				case GoalTigers:
					break;
				case Halt:
					newGameState = EGameState.HALTED;
					break;
				case IndirectFreeKickEnemies:
					newGameState = EGameState.THROW_IN_THEY;
					break;
				case IndirectFreeKickTigers:
					newGameState = EGameState.THROW_IN_WE;
					break;
				case KickOffEnemies:
					newGameState = EGameState.PREPARE_KICKOFF_THEY;
					break;
				case KickOffTigers:
					newGameState = EGameState.PREPARE_KICKOFF_WE;
					break;
				case NoCommand:
					break;
				case NormalStart:
					if (lastGameState == EGameState.STOPPED)
					{
						// NORMAL_START is not allowed after STOP, but assistant ref may press wrong button...
						newGameState = EGameState.RUNNING;
					}
					break;
				case PenaltyEnemies:
					newGameState = EGameState.PREPARE_PENALTY_THEY;
					break;
				case PenaltyTigers:
					newGameState = EGameState.PREPARE_PENALTY_WE;
					break;
				case Stop:
					newGameState = EGameState.STOPPED;
					break;
				case TimeoutEnemies:
					newGameState = EGameState.TIMEOUT_THEY;
					break;
				case TimeoutTigers:
					newGameState = EGameState.TIMEOUT_WE;
					break;
				default:
					throw new IllegalStateException();
			}
			
			// remember ball position on certain situations
			switch (refereeMsg.getTeamSpecRefCmd())
			{
				case DirectFreeKickEnemies:
				case DirectFreeKickTigers:
				case IndirectFreeKickEnemies:
				case IndirectFreeKickTigers:
				case NormalStart:
					ballPosOnPrepare = baseAiFrame.getWorldFrame().getBall().getPos();
					break;
				default:
					ballPosOnPrepare = null;
					
			}
		} else
		{
			switch (lastGameState)
			{
				case PREPARE_KICKOFF_THEY:
				case PREPARE_KICKOFF_WE:
				case PREPARE_PENALTY_THEY:
				case PREPARE_PENALTY_WE:
					if ((baseAiFrame.getLatestRefereeMsg() != null)
							&& (baseAiFrame.getLatestRefereeMsg().getTeamSpecRefCmd() != ETeamSpecRefCmd.NormalStart))
					{
						break;
					}
				case CORNER_KICK_THEY:
				case CORNER_KICK_WE:
				case GOAL_KICK_THEY:
				case GOAL_KICK_WE:
				case THROW_IN_THEY:
				case THROW_IN_WE:
				case DIRECT_KICK_THEY:
				case DIRECT_KICK_WE:
					if ((ballPosOnPrepare != null)
							&& (GeoMath.distancePP(baseAiFrame.getWorldFrame().getBall().getPos(), ballPosOnPrepare) > BALL_MOVED_DISTANCE_TOL))
					{
						ballPosOnPrepare = null;
						newGameState = EGameState.RUNNING;
						break;
					}
				case HALTED:
					break;
				case RUNNING:
					break;
				case STOPPED:
					break;
				case TIMEOUT_THEY:
					break;
				case TIMEOUT_WE:
					break;
				case UNKNOWN:
					break;
				default:
					throw new IllegalStateException();
			}
		}
		
		newTacticalField.setGameState(newGameState);
	}
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
