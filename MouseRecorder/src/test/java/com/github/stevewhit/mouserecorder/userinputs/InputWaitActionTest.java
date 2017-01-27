package com.github.stevewhit.mouserecorder.userinputs;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class InputWaitActionTest
{
	InputWaitAction waitAction;
	
	@Before
	public void setUp() throws Exception
	{
		waitAction = new InputWaitAction(1324123l, 1324123412l);
	}
	
	@After
	public void tearDown() throws Exception
	{
		waitAction = null;
	}
	
	@Test
	public void testIsValidAction()
	{
		assertTrue(waitAction.isValidAction());
	}
	
	@Test
	public void testToString()
	{
		assertEquals("WAIT: 1324123ns", waitAction.toString());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInputWaitAction_NegWaitTime()
	{
		waitAction = new InputWaitAction(-1, 1234l);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInputWaitAction_NegTimeStamp()
	{
		waitAction = new InputWaitAction(13241, -1);
	}
	
	@Test
	public void testInputWaitAction_Valid()
	{
		assertTrue(waitAction.isValidAction());
		assertTrue(waitAction.getActionId() != null);
		assertTrue(waitAction.getTimeStamp() == 1324123412l);
		
		waitAction = new InputWaitAction(0, 0);
		waitAction = new InputWaitAction(1, 0);
		waitAction = new InputWaitAction(0, 1);
		waitAction = new InputWaitAction(1, 1);		
	}
	
	@Test
	public void testGetWaitTime()
	{
		assertTrue(waitAction.getWaitTime() == 1324123l);
	}
	
}
