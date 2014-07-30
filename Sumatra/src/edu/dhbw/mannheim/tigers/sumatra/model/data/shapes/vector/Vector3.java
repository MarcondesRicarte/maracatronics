/*
 * *********************************************************
 * Copyright (c) 2009 - 2010, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: 09.08.2012
 * Author(s): Gero, AndreR
 * *********************************************************
 */
package edu.dhbw.mannheim.tigers.sumatra.model.data.shapes.vector;

import java.util.ArrayList;
import java.util.List;

import com.sleepycat.persist.model.Persistent;

import edu.dhbw.mannheim.tigers.sumatra.model.data.math.AngleMath;
import edu.dhbw.mannheim.tigers.sumatra.model.data.math.SumatraMath;


/**
 * Simple data holder for position data
 * <p>
 * <i>(Being aware of EJ-SE Items 13, 14 and 55: members are public to reduce noise)</i>
 * </p>
 * 
 * @see Vector2
 * @author Gero, AndreR
 */
@Persistent
public class Vector3 extends AVector3
{
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	
	/**  */
	/** */
	private float	x;
	/** */
	private float	y;
	/** */
	private float	z;
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	/** */
	public Vector3()
	{
		x = 0;
		y = 0;
		z = 0;
	}
	
	
	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vector3(final float x, final float y, final float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	
	/**
	 * Providing a <strong>hard, deep</strong> copy of original
	 * 
	 * @param xy
	 * @param z
	 */
	public Vector3(final IVector2 xy, final float z)
	{
		set(xy, z);
	}
	
	
	/**
	 * Providing a <strong>hard, deep</strong> copy of original
	 * 
	 * @param original
	 */
	public Vector3(final IVector3 original)
	{
		set(original);
	}
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	/**
	 * @param original
	 * @param z
	 */
	public void set(final IVector2 original, final float z)
	{
		x = original.x();
		y = original.y();
		this.z = z;
	}
	
	
	/**
	 * @param original
	 */
	public void set(final IVector3 original)
	{
		x = original.x();
		y = original.y();
		z = original.z();
	}
	
	
	/**
	 * @return
	 */
	@Override
	public float getLength3()
	{
		return SumatraMath.sqrt((x * x) + (y * y) + (z * z));
	}
	
	
	/**
	 * Adds the given 'vector' to 'this'
	 * 
	 * @param vector
	 * @return Added vector.
	 */
	public Vector3 add(final IVector3 vector)
	{
		x += vector.x();
		y += vector.y();
		z += vector.z();
		return this;
	}
	
	
	/**
	 * Subtracts the given 'vector' from 'this'
	 * 
	 * @param vector
	 * @return Subtracted vector.
	 */
	public Vector3 subtract(final IVector3 vector)
	{
		x -= vector.x();
		y -= vector.y();
		z -= vector.z();
		return this;
	}
	
	
	/**
	 * Multiply this vector with f
	 * 
	 * @param f factor
	 * @return this
	 */
	public Vector3 mutiply(final float f)
	{
		x *= f;
		y *= f;
		z *= f;
		
		return this;
	}
	
	
	/**
	 * Turns 'this' <b>with</b> the given 'angle' around the Z axis. The length of the vector remains the same.
	 * The angle is added anti-clockwise.
	 * 
	 * @param angle angle in [rad]
	 * @return Turned vector.
	 * @author AndreR
	 */
	public Vector3 turnAroundZ(final float angle)
	{
		final float cosA = AngleMath.cos(angle);
		final float sinA = AngleMath.sin(angle);
		
		final float x2 = (x * cosA) - (y * sinA);
		final float y2 = (y * cosA) + (x * sinA);
		
		x = x2;
		y = y2;
		
		return this;
	}
	
	
	@Override
	public float x()
	{
		return x;
	}
	
	
	@Override
	public float y()
	{
		return y;
	}
	
	
	@Override
	public float z()
	{
		return z;
	}
	
	
	@Override
	public String toString()
	{
		return "Vector3 (" + x + "," + y + "," + z + ")";
	}
	
	
	/**
	 * @param value
	 * @return
	 */
	public static Object valueOf(final String value)
	{
		String[] values = value.replaceAll("[,;]", " ").split("[ ]");
		List<String> finalValues = new ArrayList<String>(3);
		for (String val : values)
		{
			if (!val.trim().isEmpty() && !val.contains(","))
			{
				finalValues.add(val.trim());
			}
		}
		if (finalValues.size() != 3)
		{
			throw new NumberFormatException(
					"The String must contain exactly one character of [ ,] between x and y coordinate. Values: "
							+ finalValues);
		}
		return new Vector3(Float.valueOf(finalValues.get(0)), Float.valueOf(finalValues.get(1)),
				Float.valueOf(finalValues.get(2)));
	}
}
