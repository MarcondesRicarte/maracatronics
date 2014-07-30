/*
 * *********************************************************
 * Copyright (c) 2009 - 2010, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: 01.11.2010
 * Author(s): Yakisoba
 * 
 * *********************************************************
 */
package edu.dhbw.mannheim.tigers.sumatra.model.data.modules.cam;

/**
 *
 */
public abstract class ACamObject
{
	/** often 1.0, unknown */
	public final float	confidence;
	
	/** px, left = 0 */
	public final float	pixelX;
	
	/** px, top = 0 */
	public final float	pixelY;
	
	
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * 
	 * @param confidence
	 * @param pixelX
	 * @param pixelY
	 */
	public ACamObject(float confidence, float pixelX, float pixelY)
	{
		this.confidence = confidence;
		this.pixelX = pixelX;
		this.pixelY = pixelY;
	}
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
