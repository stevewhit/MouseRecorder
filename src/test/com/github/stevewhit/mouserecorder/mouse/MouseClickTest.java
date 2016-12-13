package com.github.stevewhit.mouserecorder.mouse;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.github.stevewhit.mouserecorder.mouse.MouseClick.MouseButton;

public class MouseClickTest
{
	MouseClick click;
	long holdTime;
	MouseButton button;
	
	@Before
	public void setup()
	{
		holdTime = 123456789453643333L;
		button = MouseButton.Left;
		
		click = new MouseClick(button, holdTime);
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
		MouseClick mouseClick = new MouseClick(null, holdTime);
	}
	
	@Test
	public void testMouseClickSetupMouseButtonWithInvalidHoldTime()
	{
		expectedException.expect(IllegalArgumentException.class);
		MouseClick mouseClick = new MouseClick(button, -1);
	}
	
	@Test
	public void testSetMouseButtonNull()
	{
		expectedException.expect(IllegalArgumentException.class);
		MouseClick mouseClick = new MouseClick(button, holdTime);
		mouseClick.setMouseButton(null);
	}
	
	@Test
	public void testSetMouseButtonValid()
	{
		MouseClick mouseClick = new MouseClick(button, holdTime);
		mouseClick.setMouseButton(MouseButton.Right);
		
		assertEquals(mouseClick.getMouseButton(), MouseButton.Right);
	}
	
	@Test
	public void testGetMouseButton()
	{
		assertEquals(click.getMouseButton(), button);
	}
	
	@Test
	public void testSetClickHoldTimeNegative()
	{
		expectedException.expect(IllegalArgumentException.class);
		MouseClick mouseClick = new MouseClick(button, holdTime);
		mouseClick.setClickHoldTime(-1);
	}
	
	@Test
	public void testSetClickHoldTimeValid()
	{
		MouseClick mouseClick = new MouseClick(button, holdTime);
		mouseClick.setClickHoldTime(123485719273412748L);
		assertEquals(mouseClick.getClickHoldTime(), 123485719273412748L);		
	}
	
	@Test
	public void testGetClickHoldTime()
	{
		assertEquals(click.getClickHoldTime(), holdTime);
	}
	
	@Test
	public void testToString()
	{
		assertEquals(click.toString(), "Left click and hold 123456789453643333ns");
	}
}
