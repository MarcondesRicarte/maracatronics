/*
 * *********************************************************
 * Copyright (c) 2009 - 2014, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: Feb 9, 2014
 * Author(s): Nicolai Ommer <nicolai.ommer@gmail.com>
 * 
 * *********************************************************
 */
package edu.dhbw.mannheim.tigers.sumatra.util;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.log4j.Logger;


/**
 * Utility class for loading jar files
 * 
 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
 * 
 */
public class JarLoader
{
	private static final Logger	log	= Logger.getLogger(JarLoader.class.getName());
	
	
	private JarLoader()
	{
	}
	
	
	/**
	 * @param path
	 */
	public static void loadJar(String path)
	{
		URL jarURL;
		try
		{
			jarURL = new URL("file://" + path);
		} catch (MalformedURLException err)
		{
			log.error("Could not create URL with path " + path);
			return;
		}
		Class<URLClassLoader> sysclass = URLClassLoader.class;
		URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		try
		{
			Method method = sysclass.getDeclaredMethod("addURL", new Class[] { URL.class });
			method.setAccessible(true);
			method.invoke(urlClassLoader, new Object[] { jarURL });
		} catch (Throwable t)
		{
			log.error("Could not load jar with path: " + path);
			return;
		}
		log.debug("Loaded " + path);
	}
}
