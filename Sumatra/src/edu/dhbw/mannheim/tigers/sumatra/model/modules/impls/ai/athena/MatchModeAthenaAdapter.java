/*
 * *********************************************************
 * Copyright (c) 2009 - 2014, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: Feb 8, 2014
 * Author(s): Nicolai Ommer <nicolai.ommer@gmail.com>
 * *********************************************************
 */
package edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.ai.athena;

import java.util.ArrayList;
import java.util.List;

import edu.dhbw.mannheim.tigers.sumatra.Sumatra;
import edu.dhbw.mannheim.tigers.sumatra.model.data.frames.MetisAiFrame;
import edu.dhbw.mannheim.tigers.sumatra.model.data.modules.ai.PlayStrategy;
import edu.dhbw.mannheim.tigers.sumatra.model.data.modules.ai.PlayStrategy.Builder;
import edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.ai.pandora.plays.APlay;
import edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.ai.pandora.plays.DefensePlay;
import edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.ai.pandora.plays.EPlay;
import edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.ai.pandora.plays.KeeperPlay;
import edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.ai.pandora.plays.OffensivePlay;
import edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.ai.pandora.plays.SupportPlay;
import edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.ai.pandora.plays.others.CheeringPlay;
import edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.ai.pandora.plays.others.MaintenancePlay;


/**
 * Match mode...
 * 
 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
 */
public class MatchModeAthenaAdapter extends AAthenaAdapter
{
	
	@Override
	public void doProcess(final MetisAiFrame metisAiFrame, final Builder playStrategyBuilder, final AIControl aiControl)
	{
		
		if (metisAiFrame.getNewRefereeMsg() != null)
		{
			switch (metisAiFrame.getNewRefereeMsg().getTeamSpecRefCmd())
			{
				case Halt:
					playStrategyBuilder.getActivePlays().clear();
					// GAMBIARRA//
					try
					{
						
						
						byte pwmM1 = 0, pwmM2 = 0, pwmM3 = 0, dirM1 = 0, dirM2 = 0, dirM3 = 0, chute = 0, drible = 0, bateria = 0;
						byte ultimo = (byte) (chute + (4 * drible) + (8 * bateria));
						byte M1 = (byte) ((dirM1 * 128) + pwmM1);
						byte M2 = (byte) ((dirM2 * 128) + pwmM2);
						byte M3 = (byte) ((dirM3 * 128) + pwmM3);
						byte[] arrayBytes = new byte[5];
						byte idComand = 0;
						arrayBytes[0] = idComand;
						arrayBytes[1] = M1;
						arrayBytes[2] = M2;
						arrayBytes[3] = M3;
						arrayBytes[4] = ultimo;
						Sumatra.serialPort.writeBytes(arrayBytes);
						Thread.sleep(100);
					} catch (Exception e)
					{
						e.printStackTrace();
					}
					System.out.println("PAROU AS POTARIA");
					
					break;
				case TimeoutEnemies:
				case TimeoutTigers:
					playStrategyBuilder.getActivePlays().clear();
					playStrategyBuilder.getActivePlays().add(new MaintenancePlay());
					break;
				case DirectFreeKickEnemies:
				case DirectFreeKickTigers:
				case IndirectFreeKickEnemies:
				case IndirectFreeKickTigers:
				case KickOffEnemies:
				case KickOffTigers:
				case PenaltyEnemies:
				case PenaltyTigers:
				case ForceStart:
				case GoalEnemies:
				case GoalTigers:
				case NormalStart:
				case Stop:
				case NoCommand:
				default:
					addDefaultPlays(metisAiFrame, playStrategyBuilder);
					break;
			}
			switch (metisAiFrame.getNewRefereeMsg().getStage())
			{
				case NORMAL_FIRST_HALF:
				case NORMAL_SECOND_HALF:
				case EXTRA_FIRST_HALF:
				case EXTRA_SECOND_HALF:
					break;
				case NORMAL_FIRST_HALF_PRE:
				case NORMAL_SECOND_HALF_PRE:
				case EXTRA_FIRST_HALF_PRE:
				case EXTRA_SECOND_HALF_PRE:
					break;
				case NORMAL_HALF_TIME:
				case EXTRA_HALF_TIME:
				case EXTRA_TIME_BREAK:
				case PENALTY_SHOOTOUT_BREAK:
					playStrategyBuilder.getActivePlays().clear();
					playStrategyBuilder.getActivePlays().add(new MaintenancePlay());
					break;
				case PENALTY_SHOOTOUT:
					break;
				case POST_GAME:
					playStrategyBuilder.getActivePlays().clear();
					playStrategyBuilder.getActivePlays().add(new CheeringPlay());
					break;
			}
		}
	}
	
	
	private void addDefaultPlays(final MetisAiFrame frame, final PlayStrategy.Builder playStrategyBuilder)
	{
		
		clearNotDefaultPlays(playStrategyBuilder.getActivePlays());
		
		if (!collectionContainsPlay(playStrategyBuilder.getActivePlays(), EPlay.OFFENSIVE))
		{
			playStrategyBuilder.getActivePlays().add(new OffensivePlay());
		}
		if (!collectionContainsPlay(playStrategyBuilder.getActivePlays(), EPlay.SUPPORT))
		{
			playStrategyBuilder.getActivePlays().add(new SupportPlay());
		}
		if (!collectionContainsPlay(playStrategyBuilder.getActivePlays(), EPlay.DEFENSIVE))
		{
			playStrategyBuilder.getActivePlays().add(new DefensePlay());
		}
		if (!collectionContainsPlay(playStrategyBuilder.getActivePlays(), EPlay.KEEPER))
		{
			playStrategyBuilder.getActivePlays().add(new KeeperPlay());
		}
		
	}
	
	
	private boolean collectionContainsPlay(final List<APlay> plays, final EPlay type)
	{
		for (final APlay play : plays)
		{
			if (play.getType() == type)
			{
				return true;
			}
		}
		return false;
	}
	
	
	private void clearNotDefaultPlays(final List<APlay> plays)
	{
		List<APlay> toBeRemoved = new ArrayList<APlay>();
		for (APlay play : plays)
		{
			if ((play.getType() != EPlay.OFFENSIVE)
					&& (play.getType() != EPlay.SUPPORT)
					&& (play.getType() != EPlay.DEFENSIVE)
					&& (play.getType() != EPlay.KEEPER))
			{
				toBeRemoved.remove(play);
			}
		}
		plays.removeAll(toBeRemoved);
	}
}
