package com.github.stevewhit.mouserecorder.userinputs;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AbstractInputActionTest
{
	protected class ConcreteInputAction extends AbstractInputAction
	{
		public ConcreteInputAction(long timeStamp)
		{
			super(timeStamp);
		}
		
		public boolean isValidAction()
		{
			// Implementation specific.
			
			return true;
		}
	}
	
	AbstractInputAction inputAction;
	long timeStamp;
	
	@Before
	public void setUp() throws Exception
	{
		timeStamp = 123456789876543l;
		
		inputAction = new ConcreteInputAction(timeStamp);
	}
	
	@After
	public void tearDown() throws Exception
	{
		inputAction = null;
	}
	
	//=============================================================
	
	@Test(expected=IllegalArgumentException.class)
	public void testAbstractInputAction_nullDateTime()
	{
		inputAction = new ConcreteInputAction(-1);
	}
	
	@Test
	public void testAbstractInputAction_valid()
	{
		assertTrue(inputAction.getTimeStamp() == 123456789876543l);
		assertEquals("Timestamp: 123456789876543ns", inputAction.toString());
		assertNotNull(inputAction.getActionId());
	}
	
	//=============================================================
	
	@Test
	public void testGetTimeStamp()
	{
		assertTrue(inputAction.getTimeStamp() == 123456789876543l);
	}

	//=============================================================
	
	@Test
	public void testGetActionId()
	{
		assertNotNull(inputAction.getActionId());
	}

	//=============================================================
	
	@Test
	public void testToString()
	{
		assertEquals(inputAction.toString(), "Timestamp: 123456789876543ns");
	}

	//=============================================================
	
}
