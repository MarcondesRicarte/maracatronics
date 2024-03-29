/*
 * *********************************************************
 * Copyright (c) 2009 - 2011, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: 19.02.2011
 * Author(s): DanielW
 * 
 * *********************************************************
 */
package edu.dhbw.mannheim.tigers.sumatra.util.csvexporter.exceptions;

/**
 * simple wrapper for exceptions that might occur while exporting to csv
 * 
 * @author DanielW
 * 
 */
public class CSVExporterException extends RuntimeException
{
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	
	
	/**  */
	private static final long	serialVersionUID	= 1L;
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * @param message
	 * @param throwable
	 */
	public CSVExporterException(String message, Throwable throwable)
	{
		super(message, throwable);
	}
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
