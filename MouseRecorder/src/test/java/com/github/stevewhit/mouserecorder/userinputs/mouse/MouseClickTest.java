package com.github.stevewhit.mouserecorder.userinputs.mouse;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.stevewhit.mouserecorder.monitor.Pixel;
import com.github.stevewhit.mouserecorder.monitor.PixelColor;
import com.github.stevewhit.mouserecorder.monitor.PixelCoordinate2D;
import com.github.stevewhit.mouserecorder.userinputs.mouse.MouseClick.MouseButton;

public class MouseClickTest
{
	MouseClick click;
	long holdTime;
	MouseButton buttonPressed;
	Pixel pixelClicked;
	
	@Before
	public void setUp() throws Exception
	{
		holdTime = 123;
		buttonPressed = MouseButton.Left;
		pixelClicked = new Pixel(new PixelColor(11691775), new PixelCoordinate2D(150, 100));
		
		click = new MouseClick(buttonPressed, pixelClicked, holdTime);
	}
	
	@After
	public void tearDown() throws Exception
	{
		click = null;
		buttonPressed = null;
		pixelClicked = null;
	}
	
	//=========================================================================
	
	@Test
	public void testToString()
	{
		assertEquals(click.toString(), "Left click (150, 100), hold 123ms");
	}

	//=========================================================================
	
	@Test(expected=IllegalArgumentException.class)
	public void testMouseClick_nullButton()
	{
		click = new MouseClick(null, pixelClicked, holdTime);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testMouseClick_nullPixelClicked()
	{
		click = new MouseClick(buttonPressed, null, holdTime);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testMouseClick_NegHoldTime()
	{
		click = new MouseClick(buttonPressed, pixelClicked, -1L);
	}
	
	@Test
	public void testMouseClick_valid()
	{
		assertTrue(click.getActionDateTime() != null);
		assertTrue(click.getMouseButton() == buttonPressed);
		assertTrue(click.getPixelClicked() == pixelClicked);
		assertTrue(click.getClickHoldTime() == holdTime);
		
		click = new MouseClick(buttonPressed, pixelClicked, 0);
		click = new MouseClick(buttonPressed, pixelClicked, 1);
		click = new MouseClick(buttonPressed, pixelClicked, 165165);
	}
	//=========================================================================
	
	@Test
	public void testGetMouseButton()
	{
		assertTrue(click.getMouseButton() == buttonPressed);
	}

	//=========================================================================
	
	@Test
	public void testGetPixelClicked()
	{
		assertTrue(click.getPixelClicked() == pixelClicked);
	}

	//=========================================================================
	
	@Test
	public void testGetClickHoldTime()
	{
		assertTrue(click.getClickHoldTime() == holdTime);
	}

	//=========================================================================
	
}
