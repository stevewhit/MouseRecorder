package com.github.stevewhit.mouserecorder.userinputs.mouse;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MouseWaitTest
{
	MouseWait wait;
	
	@Before
	public void setUp() throws Exception
	{
		wait = new MouseWait(12344321);
	}
	
	@After
	public void tearDown() throws Exception
	{
		wait = null;
	}
	
	//================================================================
	
	@Test
	public void testToString()
	{
		assertEquals("Wait: 12344321ms", wait.toString());
	}

	//================================================================
	
	@Test(expected=IllegalArgumentException.class)
	public void testMouseWait_NegativeWait()
	{
		wait = new MouseWait(-1);
	}

	@Test
	public void testMouseWait_valid()
	{
		assertTrue(wait.isValidAction());
		assertTrue(wait.getWaitTime() == 12344321);
		
		wait = new MouseWait(0);
		wait = new MouseWait(1);
	}
	
	//================================================================
	
	@Test
	public void testGetWaitTime()
	{
		assertTrue(wait.getWaitTime() == 12344321);
	}

	//================================================================
	
	@Test
	public void testIsValidWait()
	{
		assertTrue(wait.isValidAction());
	}

	//================================================================
	
}
