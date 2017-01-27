package com.github.stevewhit.mouserecorder.userinputs.mouse;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.github.stevewhit.mouserecorder.monitor.Pixel;
import com.github.stevewhit.mouserecorder.monitor.PixelColor;
import com.github.stevewhit.mouserecorder.monitor.PixelCoordinate2D;

public class MouseButtonPressTest
{
	MouseButtonPress buttonPressedAction;
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
		
		buttonPressedAction = new MouseButtonPress(buttonPressed, pixelInfo, 65161235l);
	}
	
	@After
	public void tearDown() throws Exception
	{
		buttonPressedAction = null;
		pixelInfo = null;
		color = null;
		location = null;
	}
	
	@Test
	public void testToString()
	{
		assertEquals("MOUSEPRESS: 'Left' : (150, 1000) : (R35/G201/B17)", buttonPressedAction.toString());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testMouseButtonPress_nullPixelInfo()
	{
		buttonPressedAction = new MouseButtonPress(MouseButton.Left, null, 123412);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testMouseButtonPress_NegTimeStamp()
	{
		buttonPressedAction = new MouseButtonPress(MouseButton.Left, pixelInfo, -1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testMouseButtonPress_nullButtonPressed()
	{
		buttonPressedAction = new MouseButtonPress(null, pixelInfo, 123412);
	}
	
	@Test
	public void testMouseButtonPress_valid()
	{
		assertTrue(buttonPressedAction.isValidAction());
		assertTrue(buttonPressedAction.getMouseButton() == MouseButton.Left);
		assertTrue(buttonPressedAction.getPixelColor().equals(new PixelColor(2345233)));
		
		buttonPressedAction = new MouseButtonPress(MouseButton.Left, pixelInfo, 0);
		buttonPressedAction = new MouseButtonPress(MouseButton.Left, pixelInfo, 1);
		buttonPressedAction = new MouseButtonPress(MouseButton.Left, pixelInfo, 123412342314l);
	}
	
}
