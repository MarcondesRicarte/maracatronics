/*
 * *********************************************************
 * Copyright (c) 2009 - 2011, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: 10.01.2011
 * Author(s): Oliver Steinbrecher <OST1988@aol.com>
 * *********************************************************
 */
package edu.dhbw.mannheim.tigers.sumatra.model.data.modules.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.oval.constraint.NotNull;
import net.sf.oval.constraint.Size;

import com.sleepycat.persist.model.Persistent;

import edu.dhbw.mannheim.tigers.sumatra.model.data.shapes.IDrawableShape;
import edu.dhbw.mannheim.tigers.sumatra.model.data.trackedobjects.ids.BotIDMap;
import edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.ai.pandora.plays.APlay;
import edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.ai.pandora.roles.ARole;
import edu.dhbw.mannheim.tigers.sumatra.presenter.aicenter.EAIControlState;


/**
 * This stores all tactical information.
 * 
 * @author Oliver Steinbrecher <OST1988@aol.com>
 */
@Persistent(version = 3)
public class PlayStrategy implements IPlayStrategy
{
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	
	private transient List<APlay>	activePlays;
	
	/** Contains all finished plays of the last cycle */
	private transient List<APlay>	finishedPlays;
	
	@NotNull
	private BotConnection			botConnection;
	
	@Size(min = 4, profiles = { "compatibility" })
	private List<IDrawableShape>	debugShapes;
	
	private EAIControlState			aiControlState;
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	
	@SuppressWarnings("unused")
	private PlayStrategy()
	{
	}
	
	
	/**
	 * @param builder
	 */
	public PlayStrategy(final Builder builder)
	{
		activePlays = Collections.unmodifiableList(builder.activePlays);
		finishedPlays = Collections.unmodifiableList(builder.finishedPlays);
		botConnection = builder.botConnection;
		debugShapes = new ArrayList<IDrawableShape>();
		aiControlState = builder.controlState;
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	/**
	 * Get the number of roles (calculates the sum of roles of all plays)
	 * 
	 * @return
	 */
	@Override
	public int getNumRoles()
	{
		int sum = 0;
		for (APlay play : activePlays)
		{
			sum += play.getRoles().size();
		}
		return sum;
	}
	
	
	/**
	 * @return
	 */
	@Override
	public BotIDMap<ARole> getActiveRoles()
	{
		BotIDMap<ARole> roles = new BotIDMap<ARole>(6);
		for (APlay play : activePlays)
		{
			for (ARole role : play.getRoles())
			{
				roles.put(role.getBotID(), role);
			}
		}
		return roles;
	}
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	 * @return
	 */
	@Override
	public List<APlay> getActivePlays()
	{
		return activePlays;
	}
	
	
	/**
	 * @return
	 */
	@Override
	public List<APlay> getFinishedPlays()
	{
		return finishedPlays;
	}
	
	
	/**
	 * @return
	 */
	@Override
	public BotConnection getBotConnection()
	{
		return botConnection;
	}
	
	
	/**
	 * @return the debugShapes
	 */
	@Override
	public List<IDrawableShape> getDebugShapes()
	{
		return debugShapes;
	}
	
	
	/**
	 * @return the controlState
	 */
	@Override
	public EAIControlState getAIControlState()
	{
		return aiControlState;
	}
	
	
	/**
	 * Use this builder to create a {@link PlayStrategy}
	 * 
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
	public static class Builder
	{
		private List<APlay>		activePlays;
		private List<APlay>		finishedPlays;
		private BotConnection	botConnection;
		private EAIControlState	controlState;
		
		
		/**
		  * 
		  */
		public Builder()
		{
			activePlays = new ArrayList<APlay>();
			finishedPlays = new ArrayList<APlay>();
			botConnection = new BotConnection(false, false, false, false, false, false);
			controlState = EAIControlState.TEST_MODE;
		}
		
		
		/**
		 * Create a new {@link PlayStrategy} with the data in this Builder
		 * 
		 * @return
		 */
		public IPlayStrategy build()
		{
			return new PlayStrategy(this);
		}
		
		
		/**
		 * @return the activePlays
		 */
		public final List<APlay> getActivePlays()
		{
			return activePlays;
		}
		
		
		/**
		 * @param activePlays the activePlays to set
		 */
		public final void setActivePlays(final List<APlay> activePlays)
		{
			this.activePlays = activePlays;
		}
		
		
		/**
		 * @return the finishedPlays
		 */
		public final List<APlay> getFinishedPlays()
		{
			return finishedPlays;
		}
		
		
		/**
		 * @param finishedPlays the finishedPlays to set
		 */
		public final void setFinishedPlays(final List<APlay> finishedPlays)
		{
			this.finishedPlays = finishedPlays;
		}
		
		
		/**
		 * @return the botConnection
		 */
		public final BotConnection getBotConnection()
		{
			return botConnection;
		}
		
		
		/**
		 * @param botConnection the botConnection to set
		 */
		public final void setBotConnection(final BotConnection botConnection)
		{
			this.botConnection = botConnection;
		}
		
		
		/**
		 * @return Athena's controlState
		 */
		public final EAIControlState getAIControlState()
		{
			return controlState;
		}
		
		
		/**
		 * @param controlState the controlState to set
		 */
		public final void setAIControlState(final EAIControlState controlState)
		{
			this.controlState = controlState;
		}
		
	}
}
