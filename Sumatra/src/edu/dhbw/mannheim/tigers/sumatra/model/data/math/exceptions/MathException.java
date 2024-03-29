/*
 * *********************************************************
 * Copyright (c) 2009 - 2011, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: 19.06.2011
 * Author(s): Gero
 * 
 * *********************************************************
 */
package edu.dhbw.mannheim.tigers.sumatra.model.data.math.exceptions;


/**
 * This exception is thrown if a mathematical/logical error occurs during usage of
 * {@link edu.dhbw.mannheim.tigers.sumatra.model.data.math.SumatraMath} or this package
 * 
 * @author Gero
 */
public class MathException extends Exception
{
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final long	serialVersionUID	= -321058382938703716L;
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 */
	public MathException()
	{
		super();
	}
	
	
	/**
	 * @param str
	 */
	public MathException(String str)
	{
		super(str);
	}
}
