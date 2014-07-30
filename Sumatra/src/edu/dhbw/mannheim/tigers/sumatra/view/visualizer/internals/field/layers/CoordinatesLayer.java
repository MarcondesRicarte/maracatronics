/*
 * *********************************************************
 * Copyright (c) 2009 - 2013, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: Jan 19, 2013
 * Author(s): Nicolai Ommer <nicolai.ommer@gmail.com>
 * *********************************************************
 */
package edu.dhbw.mannheim.tigers.sumatra.view.visualizer.internals.field.layers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import edu.dhbw.mannheim.tigers.sumatra.model.data.airecord.IRecordFrame;
import edu.dhbw.mannheim.tigers.sumatra.model.data.modules.ai.ETeamColor;
import edu.dhbw.mannheim.tigers.sumatra.model.data.shapes.vector.IVector2;
import edu.dhbw.mannheim.tigers.sumatra.model.data.shapes.vector.Vector2;
import edu.dhbw.mannheim.tigers.sumatra.model.data.shapes.vector.line.DrawableLine;
import edu.dhbw.mannheim.tigers.sumatra.model.data.shapes.vector.line.DrawableLine.ETextLocation;
import edu.dhbw.mannheim.tigers.sumatra.model.data.shapes.vector.line.Line;
import edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.ai.config.AIConfig;
import edu.dhbw.mannheim.tigers.sumatra.view.visualizer.internals.field.FieldPanel;


/**
 * This layer shows coordinate axis
 * 
 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
 */
public class CoordinatesLayer extends AFieldLayer
{
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final int	STEP_1				= 1000;
	private static final int	STEP_2				= 2000;
	private static final int	STEP_MARKER_LEN	= 100;
	
	private IVector2				lastMousePoint		= new Vector2();
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	 */
	public CoordinatesLayer()
	{
		super(EFieldLayer.COORDINATES, false);
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	@Override
	protected void paintLayerAif(final Graphics2D g, final IRecordFrame frame)
	{
		g.setStroke(new BasicStroke());
		g.setFont(new Font("", Font.PLAIN, 10));
		Color color = frame.getTeamColor() == ETeamColor.YELLOW ? Color.yellow : Color.blue;
		
		float maxY = AIConfig.getGeometry().getFieldWidth() / 2;
		float maxX = AIConfig.getGeometry().getFieldLength() / 2;
		
		DrawableLine xAxis = new DrawableLine(new Line(new Vector2(0, 0), new Vector2(maxX, 0)), color);
		xAxis.setText("x");
		xAxis.setTextLocation(ETextLocation.HEAD);
		xAxis.paintShape(g, getFieldPanel(), frame.getWorldFrame().isInverted());
		
		DrawableLine yAxis = new DrawableLine(new Line(new Vector2(0, 0), new Vector2(0, maxY)), color);
		yAxis.setText("y");
		yAxis.setTextLocation(ETextLocation.HEAD);
		yAxis.paintShape(g, getFieldPanel(), frame.getWorldFrame().isInverted());
		
		DrawableLine xStep1 = new DrawableLine(new Line(new Vector2(STEP_1, (STEP_MARKER_LEN) / 2), new Vector2(0,
				-STEP_MARKER_LEN)), color);
		xStep1.setText(String.valueOf(STEP_1));
		xStep1.setTextLocation(ETextLocation.HEAD);
		xStep1.setDrawArrowHead(false);
		xStep1.paintShape(g, getFieldPanel(), frame.getWorldFrame().isInverted());
		
		DrawableLine xStep2 = new DrawableLine(new Line(new Vector2(STEP_2, (STEP_MARKER_LEN) / 2), new Vector2(0,
				-STEP_MARKER_LEN)), color);
		xStep2.setText(String.valueOf(STEP_2));
		xStep2.setTextLocation(ETextLocation.HEAD);
		xStep2.setDrawArrowHead(false);
		xStep2.paintShape(g, getFieldPanel(), frame.getWorldFrame().isInverted());
		
		DrawableLine yStep1 = new DrawableLine(new Line(new Vector2((STEP_MARKER_LEN) / 2, STEP_1), new Vector2(
				-STEP_MARKER_LEN, 0)), color);
		yStep1.setText(String.valueOf(STEP_1));
		yStep1.setTextLocation(ETextLocation.HEAD);
		yStep1.setDrawArrowHead(false);
		yStep1.paintShape(g, getFieldPanel(), frame.getWorldFrame().isInverted());
		
		IVector2 center;
		switch (getFieldPanel().getFieldTurn())
		{
			case NORMAL:
			case T180:
				center = new Vector2((getFieldPanel().getFieldTotalWidth() / 2.0f), FieldPanel.FIELD_MARGIN - 19);
				break;
			case T270:
			case T90:
				center = new Vector2((getFieldPanel().getFieldTotalHeight() / 2.0f), FieldPanel.FIELD_MARGIN - 19);
				break;
			default:
				throw new IllegalStateException();
		}
		int inv2;
		switch (getFieldPanel().getFieldTurn())
		{
			case NORMAL:
			case T90:
				inv2 = 1;
				break;
			case T180:
			case T270:
				inv2 = -1;
				break;
			default:
				throw new IllegalStateException();
		}
		
		int inv = (frame.getWorldFrame().isInverted() ? -1 : 1);
		IVector2 offset;
		if ((frame.getWorldFrame().isInverted() && (inv2 == -1)) || (!frame.getWorldFrame().isInverted() && (inv2 == 1)))
		{
			offset = new Vector2(-110, 0);
		} else
		{
			offset = new Vector2(60, 0);
		}
		
		IVector2 posGui = center.addNew(offset);
		g.setColor(Color.white);
		char tColor = frame.getTeamColor() == ETeamColor.YELLOW ? 'Y' : 'B';
		g.drawString(
				String.format("%c x:%5d", tColor, inv * (int) lastMousePoint.x()),
				posGui.x(), posGui.y());
		g.drawString(
				String.format("   y:%5d", inv * (int) lastMousePoint.y()),
				posGui.x(), posGui.y() + 11);
	}
	
	
	/**
	 * Set the mouse position for coordinates text
	 * 
	 * @param location
	 */
	public void updateMouseLocation(final IVector2 location)
	{
		lastMousePoint = location;
	}
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
