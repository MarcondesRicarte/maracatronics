/*
 * *********************************************************
 * Copyright (c) 2009 - 2011, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: 16.07.2011
 * Author(s): Malte
 * 
 * *********************************************************
 */
package edu.dhbw.mannheim.tigers.sumatra.test.junit.model.data.shapes;

import edu.dhbw.mannheim.tigers.sumatra.model.data.shapes.I2DShape;


/**
 * Interface for all JUnit-Test classes, testing a {@link I2DShape}.
 * 
 * @author Malte
 */
public interface I2DShapeTest
{
	/**
	 *
	 */
	void testConstructor();
	
	
	/**
	 *
	 */
	void testGetArea();
	
	
	/**
	 *
	 */
	void testIsPointInShape();
	
	
	/**
	 *
	 */
	void testIsLineIntersectingShape();
	
	
	/**
	 *
	 */
	void testNearestPointOutside();
}
