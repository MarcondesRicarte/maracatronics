/*
 * *********************************************************
 * Copyright (c) 2009 - 2011, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: 29.04.2011
 * Author(s): DanielW
 * 
 * *********************************************************
 */
package edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.ai.sisyphus.data;

import com.sleepycat.persist.model.Persistent;

import edu.dhbw.mannheim.tigers.sumatra.model.data.shapes.vector.IVector2;


/**
 * collects values associated with a path that the GUI can display
 * 
 * This class is deprecated but can not be deleted, because BerkeleyDB needs its existence for old record data
 * 
 * @author DanielW
 * 
 */
@Persistent
@Deprecated
public class PathGuiFeatures
{
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private Float		virtualVehicle	= null;
	private IVector2	currentMove		= null;
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * @return the virtualVehicle
	 */
	public Float getVirtualVehicle()
	{
		return virtualVehicle;
	}
	
	
	/**
	 * @param virtualVehicle the virtualVehicle to set
	 */
	public void setVirtualVehicle(Float virtualVehicle)
	{
		this.virtualVehicle = virtualVehicle;
	}
	
	
	/**
	 * @return the currentMove
	 */
	public IVector2 getCurrentMove()
	{
		return currentMove;
	}
	
	
	/**
	 * @param currentMove the currentMove to set
	 */
	public void setCurrentMove(IVector2 currentMove)
	{
		this.currentMove = currentMove;
	}
}
