package com.github.stevewhit.mouserecorder.mouse;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class MouseWaitTest
{
	MouseWait validMouseWait;
	LocalDateTime dateTime;
	long waitTime;
	
	@Before
	public void setup()
	{
		waitTime = 123456789;
		dateTime = LocalDateTime.now();
		
		validMouseWait = new MouseWait(waitTime);
	}
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Test
	public void testMouseWaitValidTimeCreatesAction()
	{
		MouseWait tempMouseWait = new MouseWait(waitTime);
		assertTrue(tempMouseWait.getActionId() != null);
		assertEquals(tempMouseWait.getWaitTime(), waitTime);
	}
	
	@Test
	public void testMouseWaitValidTimeSetsWaitTime()
	{
		MouseWait tempMouseWait = new MouseWait(waitTime);
		assertEquals(tempMouseWait.getWaitTime(), waitTime);
	}	
	
	@Test
	public void testMouseWaitInvalidTime()
	{
		expectedException.expect(IllegalArgumentException.class);
		MouseWait tempMouseWait = new MouseWait(-1);
	}
	
	@Test
	public void testGetWaitTime()
	{
		assertEquals(validMouseWait.getWaitTime(), waitTime);
	}
	
	@Test
	public void testSetWaitTime()
	{
		MouseWait tempMouseWait = new MouseWait(waitTime);
		tempMouseWait.setWaitTime(987654321);
		
		assertEquals(tempMouseWait.getWaitTime(), 987654321);
	}
	
}
