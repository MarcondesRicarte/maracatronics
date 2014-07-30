/*
 * *********************************************************
 * Copyright (c) 2009 - 2010, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: 29.08.2010
 * Author(s): Gero
 * 
 * *********************************************************
 */
package edu.dhbw.mannheim.tigers.sumatra.model.modules.observer;

import edu.dhbw.mannheim.tigers.sumatra.model.data.frames.SimpleWorldFrame;


/**
 * Counterpart to {@link IWorldPredictorObserver}
 * 
 * @author Gero
 * 
 */
public interface IWorldPredictorObservable
{
	
	
	/**
	 * Notifies observers with new Wf
	 * @param wFrame
	 */
	void notifyNewWorldFrame(SimpleWorldFrame wFrame);
	
	
	/**
	 * Notifies about Vision signal Lost
	 * @param emptyWf emptyWorldFrame
	 */
	void notifyVisionSignalLost(SimpleWorldFrame emptyWf);
	
}
