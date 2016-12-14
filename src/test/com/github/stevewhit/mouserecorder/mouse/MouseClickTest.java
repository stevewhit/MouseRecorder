package com.github.stevewhit.mouserecorder.mouse;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.github.stevewhit.mouserecorder.monitor.Pixel;
import com.github.stevewhit.mouserecorder.monitor.PixelCoordinate2D;
import com.github.stevewhit.mouserecorder.mouse.MouseClick.MouseButton;

public class MouseClickTest
{
	MouseClick click;
	long holdTime;
	MouseButton button;
	Pixel pixelClicked;
	
	@Before
	public void setup()
	{
		holdTime = 123456789453643333L;
		button = MouseButton.Left;
		pixelClicked = new Pixel(new PixelCoordinate2D(15, 20));
		
		click = new MouseClick(button, pixelClicked, holdTime);
	}
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Test
	public void testMouseClickCheckActionId()
	{
		assertTrue(click.getActionId() != null);
	}
	
	@Test
	public void testMouseClickSetupMouseButton()
	{
		assertEquals(click.getMouseButton(), button);
	}
	
	@Test
	public void testMouseClickSetupMouseButtonWithNullButton()
	{
		expectedException.expect(IllegalArgumentException.class);
		MouseClick mouseClick = new MouseClick(null, pixelClicked, holdTime);
	}
	
	@Test
	public void testMouseClickSetupMouseButtonWithInvalidHoldTime()
	{
		expectedException.expect(IllegalArgumentException.class);
		MouseClick mouseClick = new MouseClick(button, pixelClicked, -1);
	}
	
	@Test
	public void testMouseClickSetupMouseButtonWithInvalidPixelClicked()
	{
		expectedException.expect(IllegalArgumentException.class);
		MouseClick mouseClick = new MouseClick(button, null, holdTime);
	}
	
	@Test
	public void testGetMouseButton()
	{
		assertEquals(click.getMouseButton(), button);
	}
	
	@Test
	public void testGetClickHoldTime()
	{
		assertEquals(click.getClickHoldTime(), holdTime);
	}
	
	@Test
	public void testGetPixelClicked()
	{
		assertEquals(click.getPixelClicked().toString(), pixelClicked.toString());
	}
	
	@Test
	public void testToString()
	{
		assertEquals(click.toString(), "Left click (15,20), hold 123456789453643333ns");
	}
}
