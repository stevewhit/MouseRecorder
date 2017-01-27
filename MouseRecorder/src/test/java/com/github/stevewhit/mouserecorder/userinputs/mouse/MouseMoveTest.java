package com.github.stevewhit.mouserecorder.userinputs.mouse;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.github.stevewhit.mouserecorder.monitor.PixelCoordinate2D;

public class MouseMoveTest
{
	MouseMove move;
	PixelCoordinate2D location;
	
	@Before
	public void setUp() throws Exception
	{
		location = new PixelCoordinate2D(100, 135);
		
		move = new MouseMove(location, 12341242134132l);
	}
	
	@After
	public void tearDown() throws Exception
	{
		move = null;
		location = null;
	}
	
	//========================================================
	
	@Test
	public void testToString()
	{
		assertEquals("MOUSEMOVE: (100, 135)", move.toString());
	}

	//========================================================
	
	@Test(expected=IllegalArgumentException.class)
	public void testMouseMove_nullLocation()
	{
		move = new MouseMove(null, 13241241l);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testMouseMove_NegTimeStamp()
	{
		move = new MouseMove(location, -1);
	}
	
	@Test
	public void testMouseMove_Valid()
	{
		assertTrue(move.getLocation().equals(new PixelCoordinate2D(100, 135)));
		assertTrue(move.isValidAction());
		assertTrue(move.getActionId() != null);
		
		move = new MouseMove(location, 0);
		move = new MouseMove(location, 1);
		move = new MouseMove(location, 12341234123l);
		
	}	
	
	//========================================================
	
}
