/*
 * *********************************************************
 * Copyright (c) 2009 - 2010, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: 23.08.2010
 * Author(s): AndreR
 * 
 * *********************************************************
 */
package edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.botmanager.bots.communication;

/**
 * Communication statistics packet.
 * 
 * @author AndreR
 * 
 */
public class Statistics
{
	/** */
	public int		payload	= 0;
	/** */
	public int		raw		= 0;
	/** */
	public int		packets	= 0;
	private long	lastReset;
	
	
	/**
	 */
	public Statistics()
	{
		lastReset = System.nanoTime();
	}
	
	
	/**
	 * @param org
	 */
	public Statistics(Statistics org)
	{
		payload = org.payload;
		raw = org.raw;
		packets = org.packets;
		lastReset = org.lastReset;
	}
	
	
	/**
 */
	public void reset()
	{
		payload = 0;
		raw = 0;
		packets = 0;
		
		lastReset = System.nanoTime();
	}
	
	
	/**
	 * @return
	 */
	public long getLastResetTimestamp()
	{
		return lastReset;
	}
	
	
	/**
	 * @param stat
	 * @return
	 */
	public Statistics substract(Statistics stat)
	{
		final Statistics ret = new Statistics();
		ret.payload = payload - stat.payload;
		ret.raw = raw - stat.raw;
		ret.packets = packets - stat.packets;
		
		return ret;
	}
	
	
	/**
	 * @param stat
	 * @return
	 */
	public Statistics add(Statistics stat)
	{
		final Statistics ret = new Statistics();
		ret.payload = payload + stat.payload;
		ret.raw = raw + stat.raw;
		ret.packets = packets + stat.packets;
		
		return ret;
	}
	
	
	/**
	 * @return
	 */
	public float getOverheadPercentage()
	{
		if (raw == 0)
		{
			return 0;
		}
		
		return (1.0f - (((float) payload) / ((float) raw)));
	}
	
	
	/**
	 * @param passedTime
	 * @return
	 */
	public float getLoadPercentage(float passedTime)
	{
		if (passedTime == 0)
		{
			return 0;
		}
		
		return ((raw) / (28800.0f * passedTime));
	}
	
	
	/**
	 * @return
	 */
	public float getLoadPercentageWithLastReset()
	{
		return getLoadPercentage((System.nanoTime() - lastReset) / 1000000000.0f);
	}
}
