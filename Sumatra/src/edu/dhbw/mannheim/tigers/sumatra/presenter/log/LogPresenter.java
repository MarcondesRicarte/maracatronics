/*
 * *********************************************************
 * Copyright (c) 2009 - 2010, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: 06.08.2010
 * Author(s): AndreR
 * *********************************************************
 */
package edu.dhbw.mannheim.tigers.sumatra.presenter.log;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Priority;
import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;

import edu.dhbw.mannheim.tigers.moduli.listenerVariables.ModulesState;
import edu.dhbw.mannheim.tigers.sumatra.model.SumatraModel;
import edu.dhbw.mannheim.tigers.sumatra.view.log.LogPanel;
import edu.dhbw.mannheim.tigers.sumatra.view.log.internals.IFilterPanelObserver;
import edu.dhbw.mannheim.tigers.sumatra.view.log.internals.ISlidePanelObserver;
import edu.dhbw.mannheim.tigers.sumatra.view.log.internals.ITreePanelObserver;
import edu.dhbw.mannheim.tigers.sumatra.views.ISumatraView;
import edu.dhbw.mannheim.tigers.sumatra.views.ISumatraViewPresenter;


/**
 * The log presenter handles catching LoggingEvents from log4j and displays them.
 * Furthermore it can filter the output by custom user strings or via class selection.
 * One more word on the filtering capability:
 * - Enter a user filter -> text is filtered
 * - Reset user filter -> all messages reappear
 * - Select a class filter -> text is filtered
 * - Select another class filter -> original text is filtered
 * - Select a new log level -> nothing filtered, but only events with a level equal or
 * higher to the log level will appear, all others are lost. They will not even appear
 * if you drop the log level to a lower value.
 * The number of events that can reappear is limited by the {@link #STORAGE_CAPACITY} ({@value #STORAGE_CAPACITY})
 * constant.
 * 
 * @author AndreR
 */
public class LogPresenter extends WriterAppender implements ISumatraViewPresenter, IFilterPanelObserver,
		ISlidePanelObserver, ITreePanelObserver
{
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	
	// colors
	/** */
	public static final Color						DEFAULT_COLOR_ALL		= new Color(0, 0, 0);
	/** */
	public static final Color						DEFAULT_COLOR_FATAL	= new Color(128, 0, 128);
	/** */
	public static final Color						DEFAULT_COLOR_ERROR	= new Color(255, 0, 0);
	/** */
	public static final Color						DEFAULT_COLOR_WARN	= new Color(0, 0, 255);
	/** */
	public static final Color						DEFAULT_COLOR_INFO	= new Color(0, 128, 0);
	/** */
	public static final Color						DEFAULT_COLOR_DEBUG	= new Color(96, 96, 96);
	/** */
	public static final Color						DEFAULT_COLOR_TRACE	= new Color(0, 0, 0);
	/** */
	public static final Color						DEFAULT_COLOR			= new Color(0, 0, 0);
	
	private LogPanel									logPanel					= null;
	
	/** */
	private static final int						STORAGE_CAPACITY		= 5000;
	/** */
	private static final int						DISPLAY_CAPACITY		= 1000;
	
	private final List<LoggingEvent>				eventStorage			= new ArrayList<LoggingEvent>(STORAGE_CAPACITY + 1);
	
	private List<String>								allowedClasses			= new ArrayList<String>();
	private List<String>								allowedStrings			= new ArrayList<String>();
	
	private static final String					LOG_LEVEL_KEY			= LogPresenter.class.getName() + ".loglevel";
	private Level										logLevel;
	private int											numFatals				= 0;
	private int											numErrors				= 0;
	private int											numWarnings				= 0;
	
	private final Map<Integer, AttributeSet>	attributeSets			= new HashMap<Integer, AttributeSet>();
	
	private final Object								eventSync				= new Object();
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 */
	public LogPresenter()
	{
		// set initial log level to info to avoid performance issues
		String strLevel = SumatraModel.getInstance().getUserProperty(LOG_LEVEL_KEY);
		// NicolaiO: Only use level from log4j.properties, use own filter here
		// setThreshold(Level.toLevel(strLevel));
		logLevel = Level.toLevel(strLevel);
		
		logPanel = new LogPanel(DISPLAY_CAPACITY, logLevel);
		
		logPanel.getFilterPanel().addObserver(this);
		logPanel.getSlidePanel().addObserver(this);
		logPanel.getTreePanel().addObserver(this);
		
		// set internal output layout -> see log4j.properties
		setLayout(new PatternLayout("%d{ABSOLUTE} %-5p [%t|%c{1}] %m%n"));
		Logger.getRootLogger().addAppender(this);
		
		final StyleContext sc = StyleContext.getDefaultStyleContext();
		attributeSets.put(Priority.ALL_INT,
				sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, DEFAULT_COLOR_ALL));
		attributeSets.put(Priority.FATAL_INT,
				sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, DEFAULT_COLOR_FATAL));
		attributeSets.put(Priority.ERROR_INT,
				sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, DEFAULT_COLOR_ERROR));
		attributeSets.put(Priority.WARN_INT,
				sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, DEFAULT_COLOR_WARN));
		attributeSets.put(Priority.INFO_INT,
				sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, DEFAULT_COLOR_INFO));
		attributeSets.put(Priority.DEBUG_INT,
				sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, DEFAULT_COLOR_DEBUG));
		attributeSets.put(Level.TRACE_INT,
				sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, DEFAULT_COLOR_TRACE));
	}
	
	
	/**
	 * @param active
	 */
	public void setAutoScrolling(final boolean active)
	{
		logPanel.setAutoScrolling(active);
	}
	
	
	/**
	 * Remove all events
	 */
	public void clearEventStorage()
	{
		logPanel.getTextPane().clear();
		synchronized (eventSync)
		{
			eventStorage.clear();
		}
		onLevelChanged(logLevel);
	}
	
	
	@Override
	public void append(final LoggingEvent logEvent)
	{
		synchronized (eventSync)
		{
			eventStorage.add(logEvent);
			if (eventStorage.size() > STORAGE_CAPACITY)
			{
				eventStorage.remove(0);
			}
		}
		
		appendLoggingEvent(logEvent);
	}
	
	
	@Override
	public void onNewClassList(final List<String> classes)
	{
		logPanel.getTextPane().clear();
		numFatals = 0;
		numErrors = 0;
		numWarnings = 0;
		
		allowedClasses = classes;
		
		synchronized (eventSync)
		{
			for (final LoggingEvent event : eventStorage)
			{
				appendLoggingEvent(event);
			}
		}
	}
	
	
	@Override
	public void onLevelChanged(final Level level)
	{
		if (level.equals(logLevel))
		{
			return;
		}
		
		logPanel.getTextPane().clear();
		numFatals = 0;
		numErrors = 0;
		numWarnings = 0;
		
		// do not alter log level, so old low prio log message can be displayed later
		// setThreshold(level);
		logLevel = level;
		SumatraModel.getInstance().setUserProperty(LOG_LEVEL_KEY, level.toString());
		logPanel.getTextPane().clear();
		
		synchronized (eventSync)
		{
			for (final LoggingEvent event : eventStorage)
			{
				appendLoggingEvent(event);
			}
		}
	}
	
	
	@Override
	public void onNewFilter(final List<String> allowed)
	{
		logPanel.getTextPane().clear();
		numFatals = 0;
		numErrors = 0;
		numWarnings = 0;
		
		allowedStrings = allowed;
		
		synchronized (eventSync)
		{
			for (final LoggingEvent event : eventStorage)
			{
				appendLoggingEvent(event);
			}
		}
	}
	
	
	/**
	 * Append an empty line
	 */
	public void appendLine()
	{
		final StyleContext sc = StyleContext.getDefaultStyleContext();
		final AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.black);
		logPanel.getTextPane().append("--------------------------------------------------------------\n", aset);
	}
	
	
	private void appendLoggingEvent(final LoggingEvent event)
	{
		if (!checkStringFilter(event) || !checkClassFilter(event) || !checkForLogLevel(event))
		{
			return;
		}
		
		switch (event.getLevel().toInt())
		{
			case Priority.FATAL_INT:
				numFatals++;
				logPanel.getFilterPanel().setNumFatals(numFatals);
				break;
			case Priority.ERROR_INT:
				numErrors++;
				logPanel.getFilterPanel().setNumErrors(numErrors);
				break;
			case Priority.WARN_INT:
				numWarnings++;
				logPanel.getFilterPanel().setNumWarnings(numWarnings);
				break;
			default:
		}
		
		logPanel.getTextPane().append(layout.format(event), attributeSets.get(event.getLevel().toInt()));
	}
	
	
	/**
	 * Checks if the event is from a component which is on the white-list
	 * 
	 * @param event Event to check.
	 * @return true if the event is in the white-list or the white-list is empty.
	 */
	private boolean checkClassFilter(final LoggingEvent event)
	{
		if (allowedClasses.isEmpty())
		{
			return true;
		}
		
		// get classname of loggingEvent
		final String from = new PatternLayout("%C{1}").format(event);
		
		for (final String allowed : allowedClasses)
		{
			if (from.startsWith(allowed))
			{
				return true;
			}
		}
		
		return false;
	}
	
	
	private boolean checkForLogLevel(final LoggingEvent event)
	{
		if (event.getLevel().isGreaterOrEqual(logLevel))
		{
			return true;
		}
		return false;
	}
	
	
	/**
	 * Checks if the event contains one of the user filter strings.
	 * 
	 * @param event Event to check.
	 * @return true if the event contains a user filter string or if there are no filter strings.
	 */
	private boolean checkStringFilter(final LoggingEvent event)
	{
		if (allowedStrings.isEmpty())
		{
			return true;
		}
		
		for (final String allowed : allowedStrings)
		{
			if (layout.format(event).contains(allowed))
			{
				return true;
			}
		}
		
		return false;
	}
	
	
	@Override
	public Component getComponent()
	{
		return logPanel;
	}
	
	
	@Override
	public ISumatraView getSumatraView()
	{
		return logPanel;
	}
	
	
	@Override
	public void onEmergencyStop()
	{
	}
	
	
	@Override
	public void onModuliStateChanged(final ModulesState state)
	{
	}
}
