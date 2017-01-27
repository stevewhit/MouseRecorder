package com.github.stevewhit.mouserecorder.userinputs.mouse;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.stevewhit.mouserecorder.monitor.PixelCoordinate2D;

public class AbstractMouseInputActionTest
{
	protected class ConcreteMouseInputAction extends AbstractMouseInputAction
	{
		public ConcreteMouseInputAction(PixelCoordinate2D location, long timeStamp)
		{
			super(location, timeStamp);
		}

		@Override
		public boolean isValidAction()
		{
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public String toString()
		{
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	ConcreteMouseInputAction inputAction;
	PixelCoordinate2D location;
	
	@Before
	public void setUp() throws Exception
	{
		location = new PixelCoordinate2D(100, 150);
		inputAction = new ConcreteMouseInputAction(location, 12341234124l);
	}
	
	@After
	public void tearDown() throws Exception
	{
		location = null;
		inputAction = null;
	}
	
	//==============================================================
	
	@Test(expected = IllegalArgumentException.class)
	public void testAbstractMouseInputAction_nullLocation()
	{
		inputAction = new ConcreteMouseInputAction(null, 11);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAbstractMouseInputAction_NegTimestamp()
	{
		inputAction = new ConcreteMouseInputAction(location, -1);
	}
	
	@Test
	public void testAbstractMouseInputAction_Valid()
	{
		assertTrue(inputAction.getActionId() != null);
		assertTrue(inputAction.getLocation().isValidCoord());
		assertTrue(inputAction.getTimeStamp() == 12341234124l);
		
		inputAction = new ConcreteMouseInputAction(location, 0);
		inputAction = new ConcreteMouseInputAction(location, 1);
		inputAction = new ConcreteMouseInputAction(location, 12341234);
		
	}
	
	//==============================================================
	
	@Test
	public void testGetLocation()
	{
		assertNotNull(inputAction.getLocation());
		assertTrue(inputAction.getLocation().isValidCoord());
	}

	//==============================================================
	
}
