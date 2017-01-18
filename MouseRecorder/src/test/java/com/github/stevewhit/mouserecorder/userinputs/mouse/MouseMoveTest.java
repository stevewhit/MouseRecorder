package com.github.stevewhit.mouserecorder.userinputs.mouse;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.stevewhit.mouserecorder.monitor.Pixel;
import com.github.stevewhit.mouserecorder.monitor.PixelColor;
import com.github.stevewhit.mouserecorder.monitor.PixelCoordinate2D;

public class MouseMoveTest
{
	MouseMove move;
	Pixel startPixel;
	Pixel endPixel;
	
	@Before
	public void setUp() throws Exception
	{
		startPixel = new Pixel(new PixelColor(11691775), new PixelCoordinate2D(100, 150));
		endPixel = new Pixel(new PixelColor(11691775), new PixelCoordinate2D(150, 250));
		
		move = new MouseMove(startPixel, endPixel);
	}
	
	@After
	public void tearDown() throws Exception
	{
		move = null;
		startPixel = null;
		endPixel = null;
	}
	
	//========================================================
	
	@Test
	public void testToString()
	{
		assertEquals("Move: (100, 150)-->(150, 250)", move.toString());
	}

	//========================================================
	
	@Test(expected=IllegalArgumentException.class)
	public void testMouseMove_nullStartPixel()
	{
		move = new MouseMove(null, endPixel);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testMouseMove_nullEndPixel()
	{
		move = new MouseMove(startPixel, null);
	}
	
	@Test
	public void testMouseMove_Valid()
	{
		assertTrue(move.getStartPixel().equals(startPixel));
		assertTrue(move.getEndPixel().equals(endPixel));
		assertTrue(move.isValidAction());
		assertNotNull(move.getActionId());
		assertNotNull(move.getActionDateTime());
	}
	
	//========================================================
	
	@Test
	public void testGetStartPixel()
	{
		assertTrue(move.getStartPixel().equals(startPixel));
		assertFalse(move.getStartPixel().equals(endPixel));
	}

	//========================================================
	
	@Test
	public void testGetEndPixel()
	{
		assertTrue(move.getEndPixel().equals(endPixel));
		assertFalse(move.getEndPixel().equals(startPixel));
	}

	//========================================================
	
	@Test
	public void testGetMoveDistance()
	{
		double distance = Math.sqrt(12500);

		assertTrue(move.getMoveDistance() == distance);
	}

	//========================================================

	@Test
	public void testIsValidMove()
	{
		assertTrue(move.isValidAction());
	}

	//========================================================
	
}
