/*
 * *********************************************************
 * Copyright (c) 2009 - 2013, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: 16.04.2013
 * Author(s): AndreR
 * 
 * *********************************************************
 */
package edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.botmanager.commands.tigerv2;

import java.io.UnsupportedEncodingException;

import edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.botmanager.commands.ACommand;
import edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.botmanager.commands.ECommand;
import edu.dhbw.mannheim.tigers.sumatra.util.serial.SerialData;
import edu.dhbw.mannheim.tigers.sumatra.util.serial.SerialData.ESerialDataType;


/**
 * Simple text messages from a tigerv2 bot.
 * 
 * @author AndreR
 * 
 */
public class TigerSystemConsolePrint extends ACommand
{
	/** */
	public static enum ConsolePrintSource
	{
		/** */
		UNKNOWN(0),
		/** */
		MAIN(1),
		/** */
		MEDIA(2);
		
		private final int	id;
		
		
		private ConsolePrintSource(int id)
		{
			this.id = id;
		}
		
		
		/**
		 * Get id of source.
		 * 
		 * @return
		 */
		public int getId()
		{
			return id;
		}
		
		
		/**
		 * Convert an id to an enum.
		 * 
		 * @param id 1 (MAIN) or 2 (MEDIA)
		 * @return enum
		 */
		public static ConsolePrintSource getSourceConstant(int id)
		{
			for (ConsolePrintSource s : values())
			{
				if (s.getId() == id)
				{
					return s;
				}
			}
			
			return UNKNOWN;
		}
	}
	
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	@SerialData(type = ESerialDataType.UINT8)
	private int		source;
	@SerialData(type = ESerialDataType.TAIL)
	private byte[]	textData;
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	/** */
	public TigerSystemConsolePrint()
	{
		super(ECommand.CMD_SYSTEM_CONSOLE_PRINT);
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * @return the source
	 */
	public ConsolePrintSource getSource()
	{
		return ConsolePrintSource.getSourceConstant(source);
	}
	
	
	/**
	 * @param source the source to set
	 */
	public void setSource(ConsolePrintSource source)
	{
		this.source = source.getId();
	}
	
	
	/**
	 * @return the text
	 */
	public String getText()
	{
		String text;
		
		try
		{
			text = new String(textData, 0, textData.length, "US-ASCII");
		} catch (UnsupportedEncodingException err)
		{
			text = "Unsupported Encoding";
		}
		
		return text;
	}
	
	
	/**
	 * @param text the text to set
	 */
	public void setText(String text)
	{
		try
		{
			textData = text.getBytes("US-ASCII");
		} catch (UnsupportedEncodingException err)
		{
			textData = new byte[1];
			textData[0] = 0;
		}
	}
}
