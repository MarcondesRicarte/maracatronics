/*
 * *********************************************************
 * Copyright (c) 2009 - 2010, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: 21.07.2010
 * Author(s): Gero
 * *********************************************************
 */
package edu.dhbw.mannheim.tigers.sumatra.model.data.shapes.vector;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.sleepycat.persist.model.Persistent;

import edu.dhbw.mannheim.tigers.sumatra.model.data.math.AngleMath;


/**
 * Simple data holder for position data. Containing a X- and a Y-Coordinate.
 * <p>
 * <i>(Being aware of EJ-SE Items 13, 14 and 55: members are public to reduce noise)</i>
 * </p>
 * 
 * @see Vector3
 * @author Gero
 */
@Persistent
public class Vector2 extends AVector2 implements Serializable
{
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final long	serialVersionUID	= -8795210822775094406L;
	
	
	/** */
	public float					x;
	/** */
	public float					y;
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * Creates a new vector with (0, 0)
	 * 
	 * @see Vector2
	 */
	public Vector2()
	{
		x = 0;
		y = 0;
	}
	
	
	/**
	 * Creates a new vector with given (x, y)
	 * 
	 * @param x
	 * @param y
	 * @see Vector2
	 */
	public Vector2(final float x, final float y)
	{
		this.x = x;
		this.y = y;
	}
	
	
	/**
	 * Creates a new vector with given (x, y) <br>
	 * <b>Attention!</b> Parameters will be casted to float!
	 * 
	 * @param x
	 * @param y
	 * @see Vector2
	 */
	public Vector2(final double x, final double y)
	{
		this.x = (float) x;
		this.y = (float) y;
	}
	
	
	/**
	 * Creates a normalized vector that has the given angle, referring to the x-axis.
	 * 
	 * @param angle
	 */
	public Vector2(final float angle)
	{
		x = 1;
		y = 0;
		turnTo(angle);
	}
	
	
	/**
	 * Providing a <strong>hard, deep</strong> copy of original
	 * 
	 * @param original
	 */
	public Vector2(final IVector2 original)
	{
		set(original);
	}
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * s * Set x and y of this vector to the values of original
	 * 
	 * @param original
	 * @return
	 */
	public Vector2 set(final IVector2 original)
	{
		x = original.x();
		y = original.y();
		return this;
	}
	
	
	/**
	 * @param x
	 * @return
	 */
	public Vector2 setX(final float x)
	{
		this.x = x;
		return this;
	}
	
	
	/**
	 * @param y
	 * @return
	 */
	public Vector2 setY(final float y)
	{
		this.y = y;
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
	
	
	/**
	 * Adds a given value to the x-value of the current vector.
	 * Returns the manipulated vector!
	 * 
	 * @param x
	 * @return
	 */
	public Vector2 addX(final float x)
	{
		this.x += x;
		return this;
	}
	
	
	/**
	 * Adds a given value to the y-value of the current vector.
	 * Returns the manipulated vector!
	 * 
	 * @param y
	 * @return
	 */
	public Vector2 addY(final float y)
	{
		this.y += y;
		return this;
	}
	
	
	/**
	 * Subtracts the given value from the x-value of the current vector.
	 * Returns the manipulated vector!
	 * 
	 * @param x
	 * @return
	 */
	public Vector2 subX(final float x)
	{
		this.x -= x;
		return this;
	}
	
	
	/**
	 * Subtracts the given value from the y-value of the current vector.
	 * Returns the manipulated vector!
	 * 
	 * @param y
	 * @return
	 */
	public Vector2 subY(final float y)
	{
		this.y -= y;
		return this;
	}
	
	
	/**
	 * Multiplys the given value with the x-value of the current vector.
	 * Returns the manipulated vector!
	 * 
	 * @param xFactor
	 * @return
	 */
	public Vector2 multX(final float xFactor)
	{
		x *= xFactor;
		return this;
	}
	
	
	/**
	 * Multiplys the given value with the y-value of the current vector.
	 * Returns the manipulated vector!
	 * 
	 * @param yFactor
	 * @return
	 */
	public Vector2 multY(final float yFactor)
	{
		y *= yFactor;
		return this;
	}
	
	
	@Override
	public String toString()
	{
		return String.format("(%.1f, %.1f)", x, y);
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * Adds the given 'vector' to 'this'
	 * 
	 * @param vector
	 * @return
	 */
	public Vector2 add(final IVector2 vector)
	{
		x += vector.x();
		y += vector.y();
		return this;
	}
	
	
	/**
	 * Subtracts the given 'vector' from 'this'
	 * 
	 * @param vector
	 * @return
	 */
	public Vector2 subtract(final IVector2 vector)
	{
		x -= vector.x();
		y -= vector.y();
		return this;
	}
	
	
	/**
	 * Multiplies 'this' by the given 'factor'
	 * 
	 * @param factor
	 * @return
	 */
	public Vector2 multiply(final float factor)
	{
		x *= factor;
		y *= factor;
		return this;
	}
	
	
	/**
	 * Sets the length of 'this' to the given 'length' <br>
	 * If 'this' is a zero vector, it stays always unaffected.
	 * 
	 * <pre>
	 *   l
	 * ----  *   v
	 * | v |
	 * 
	 * <pre>
	 * 
	 * @param newLength
	 * @author Malte
	 * @return
	 */
	public Vector2 scaleTo(final float newLength)
	{
		final float oldLength = getLength2();
		if (oldLength != 0)
		{
			return multiply(newLength / oldLength);
		}
		return this;
		// log.warn("You tried to scale a null-vector to a non-zero length! Vector stays unaffected.");
	}
	
	
	/**
	 * Turns 'this' <b>with</b> the given 'angle'. The length of the vector remains the same.
	 * The angle is added anti-clockwise.
	 * 
	 * @param angle
	 * @author Malte
	 * @return
	 */
	public Vector2 turn(final float angle)
	{
		final float cosA = AngleMath.cos(angle);
		final float sinA = AngleMath.sin(angle);
		
		final float x2 = (x * cosA) - (y * sinA);
		final float y2 = (y * cosA) + (x * sinA);
		
		x = x2;
		y = y2;
		
		return this;
	}
	
	
	/**
	 * Turns 'this' <b>to</b> the given 'angle'. Angle is calculated between 'this' and the x-Axis.
	 * The length of the vector remains the same.
	 * 
	 * @param angle
	 * @author Malte
	 * @return
	 */
	public Vector2 turnTo(final float angle)
	{
		final float len = getLength2();
		y = AngleMath.sin(angle) * len;
		x = AngleMath.cos(angle) * len;
		return this;
	}
	
	
	/**
	 * Normalizes this and returns it as well.
	 * 
	 * @author Malte
	 * @return
	 */
	public Vector2 normalize()
	{
		set(normalizeNew());
		return this;
	}
	
	
	/**
	 * @param digits Number of digits to round at (negative values will be ignored!)
	 * @return This (rounded by the number of digits)
	 * @see IVector2#roundNew(int)
	 * @author Gero
	 */
	public Vector2 round(final int digits)
	{
		if (digits == 0)
		{
			x = Math.round(x());
			y = Math.round(y());
		} else if (digits > 0)
		{
			final float f = 10 * digits;
			x = Math.round(x() * f) / f;
			y = Math.round(y() * f) / f;
		}
		
		return this;
	}
	
	
	/**
	 * @param savedString
	 */
	public void setSavedString(final String savedString)
	{
		String splitChar = ";";
		
		if (savedString.contains(","))
		{
			splitChar = ",";
		}
		
		final String floats[] = savedString.split(splitChar);
		if (floats.length < 2)
		{
			throw new IllegalArgumentException("Not enough floats in: " + savedString);
		}
		
		x = Float.parseFloat(floats[0]);
		y = Float.parseFloat(floats[1]);
	}
	
	
	@Override
	public boolean similar(final IVector2 vec, final float treshold)
	{
		final IVector2 newVec = subtractNew(vec);
		return (Math.abs(newVec.x()) < treshold) && (Math.abs(newVec.y()) < treshold);
	}
	
	
	/**
	 * The String must be a pair of comma or space separated float values.
	 * Additional spaces are considered
	 * 
	 * @param value
	 * @return
	 * @throws NumberFormatException
	 */
	public static Vector2 valueOf(final String value)
	{
		String[] values = value.replaceAll("[,;]", " ").split("[ ]");
		List<String> finalValues = new ArrayList<String>(2);
		for (String val : values)
		{
			if (!val.trim().isEmpty() && !val.contains(","))
			{
				finalValues.add(val.trim());
			}
		}
		if (finalValues.size() != 2)
		{
			throw new NumberFormatException(
					"The String must contain exactly one character of [ ,;] between x and y coordinate. Values: "
							+ finalValues);
		}
		return new Vector2(Float.valueOf(finalValues.get(0)), Float.valueOf(finalValues.get(1)));
	}
}
