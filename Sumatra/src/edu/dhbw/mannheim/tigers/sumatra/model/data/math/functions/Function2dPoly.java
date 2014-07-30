/*
 * *********************************************************
 * Copyright (c) 2009 - 2013, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: 24.03.2013
 * Author(s): AndreR
 * *********************************************************
 */
package edu.dhbw.mannheim.tigers.sumatra.model.data.math.functions;

import java.util.ArrayList;
import java.util.List;


/**
 * Two dimensional function
 * y=a[0]+a[1]*x+a[2]*y+a[3]*x*y+a[4]*x*x+a[5]*y*y
 * 
 * @author AndreR
 */
public class Function2dPoly implements IFunction1D
{
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	/** */
	private final float[]	a;
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * Create polynomial function.
	 * param a must be in the form: y=a[0]+a[1]*x+a[2]*y+a[3]*x*y+a[4]*x*x+a[5]*y*y
	 * 
	 * @param a
	 */
	public Function2dPoly(final float[] a)
	{
		this.a = a;
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	@Override
	public float eval(final float... x)
	{
		float m[] = new float[] { 1, x[0], x[1], x[0] * x[1], x[0] * x[0], x[1] * x[1] };
		float result = 0;
		for (int i = 0; i < Math.min(a.length, m.length); i++)
		{
			result += a[i] * m[i];
		}
		return result;
	}
	
	
	@Override
	public List<Float> getParameters()
	{
		List<Float> params = new ArrayList<Float>();
		
		for (float f : a)
		{
			params.add(f);
		}
		return params;
	}
	
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Function2DPoly[");
		builder.append(a[0]);
		for (int i = 1; i < a.length; i++)
		{
			builder.append(',').append(a[i]);
		}
		builder.append("]");
		return builder.toString();
	}
	
	
	@Override
	public EFunction getIdentifier()
	{
		return EFunction.POLY_2D;
	}
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
