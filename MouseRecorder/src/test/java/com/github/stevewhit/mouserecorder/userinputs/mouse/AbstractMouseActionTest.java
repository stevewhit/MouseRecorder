package com.github.stevewhit.mouserecorder.userinputs.mouse;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AbstractMouseActionTest
{
	protected class ConcreteMouseAction extends AbstractMouseAction
	{
		protected ConcreteMouseAction(long timeStamp) throws IllegalArgumentException
		{
			super(timeStamp);
		}

		@Override
		public String toString()
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isValidAction()
		{
			// TODO Auto-generated method stub
			return false;
		}
	}
	
	ConcreteMouseAction mouseAction;
	
	@Before
	public void setUp() throws Exception
	{
		mouseAction = new ConcreteMouseAction(123456789876543l);
	}
	
	@After
	public void tearDown() throws Exception
	{
		mouseAction = null;
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAbstractMouseAction_NegTimeStamp()
	{
		mouseAction = new ConcreteMouseAction(-1l);
	}
	
	@Test
	public void testAbstractMouseAction_Valid()
	{
		mouseAction = new ConcreteMouseAction(0);
		mouseAction = new ConcreteMouseAction(1);
		mouseAction = new ConcreteMouseAction(12341234);
		
		assertTrue(mouseAction.isValidAction());
	}
}
