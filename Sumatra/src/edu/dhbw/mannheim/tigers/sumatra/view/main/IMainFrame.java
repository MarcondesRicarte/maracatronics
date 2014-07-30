/*
 * *********************************************************
 * Copyright (c) 2009 - 2010, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: 04.08.2010
 * Author(s): rYan
 * 
 * *********************************************************
 */
package edu.dhbw.mannheim.tigers.sumatra.view.main;

import java.util.List;

import edu.dhbw.mannheim.tigers.sumatra.views.ASumatraView;


/**
 * Interface for the main frame. Necessary for the nogui feature.
 * 
 * @author AndreR
 * 
 */
public interface IMainFrame
{
	/**
	 * 
	 * @param o
	 */
	void addObserver(IMainFrameObserver o);
	
	
	/**
	 * 
	 * @param o
	 */
	void removeObserver(IMainFrameObserver o);
	
	
	/**
	 * 
	 * @param filename
	 */
	void loadLayout(final String filename);
	
	
	/**
	 * 
	 * @param filename
	 */
	void saveLayout(String filename);
	
	
	/**
	 * 
	 * @param names
	 */
	void setMenuLayoutItems(List<String> names);
	
	
	/**
	 * 
	 * @param names
	 */
	void setMenuModuliItems(List<String> names);
	
	
	/**
	 * 
	 * @param view
	 */
	void addView(ASumatraView view);
	
	
	/**
	 * 
	 * @param name
	 */
	void selectModuliItem(String name);
	
	
	/**
	 * 
	 * @param name
	 */
	void selectLayoutItem(String name);
	
	
	/**
	 * 
	 * @param lafName
	 */
	void setLookAndFeel(String lafName);
}
