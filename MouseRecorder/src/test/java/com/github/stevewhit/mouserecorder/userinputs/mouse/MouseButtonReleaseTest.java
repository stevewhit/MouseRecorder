package com.github.stevewhit.mouserecorder.userinputs.mouse;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.stevewhit.mouserecorder.monitor.Pixel;
import com.github.stevewhit.mouserecorder.monitor.PixelColor;
import com.github.stevewhit.mouserecorder.monitor.PixelCoordinate2D;

public class MouseButtonReleaseTest
{
	MouseButtonRelease buttonReleasedAction;
	MouseButton buttonPressed;
	Pixel pixelInfo;
	PixelColor color;
	PixelCoordinate2D location;
	
	@Before
	public void setUp() throws Exception
	{
		buttonPressed = MouseButton.Left;
		color = new PixelColor(2345233);
		location = new PixelCoordinate2D(150, 1000);
		pixelInfo = new Pixel(color, location);
		
		buttonReleasedAction = new MouseButtonRelease(buttonPressed, pixelInfo, 65161235l);
	}
	
	@After
	public void tearDown() throws Exception
	{
		buttonReleasedAction = null;
		pixelInfo = null;
		color = null;
		location = null;
	}
	
	@Test
	public void testToString()
	{
		assertEquals("MOUSERELEASE: 'Left' : (150, 1000) : (R35/G201/B17)", buttonReleasedAction.toString());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testMouseButtonRelease_nullPixelInfo()
	{
		buttonReleasedAction = new MouseButtonRelease(MouseButton.Left, null, 123412);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testMouseButtonRelease_NegTimeStamp()
	{
		buttonReleasedAction = new MouseButtonRelease(MouseButton.Left, pixelInfo, -1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testMouseButtonRelease_nullButtonPressed()
	{
		buttonReleasedAction = new MouseButtonRelease(null, pixelInfo, 123412);
	}
	
	@Test
	public void testMouseButtonRelease_valid()
	{
		assertTrue(buttonReleasedAction.isValidAction());
		assertTrue(buttonReleasedAction.getMouseButton() == MouseButton.Left);
		assertTrue(buttonReleasedAction.getPixelColor().equals(new PixelColor(2345233)));
		
		buttonReleasedAction = new MouseButtonRelease(MouseButton.Left, pixelInfo, 0);
		buttonReleasedAction = new MouseButtonRelease(MouseButton.Left, pixelInfo, 1);
		buttonReleasedAction = new MouseButtonRelease(MouseButton.Left, pixelInfo, 123412342314l);
	}
	
}
