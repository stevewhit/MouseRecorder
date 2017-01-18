package com.github.stevewhit.mouserecorder.userinputs;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AbstractInputActionTest
{
	protected class ConcreteInputAction extends AbstractInputAction
	{
		public ConcreteInputAction(LocalDateTime actionDateTime)
		{
			super(actionDateTime);
		}
		
		public boolean isValidAction()
		{
			// Implementation specific.
			
			return true;
		}
	}
	
	AbstractInputAction inputAction;
	LocalDate date;
	LocalTime time;
	LocalDateTime dateTime;
	
	@Before
	public void setUp() throws Exception
	{
		date = LocalDate.of(2016, 12, 9);
		time = LocalTime.of(5, 27, 51, 500830);
		dateTime = LocalDateTime.of(date, time);
		
		inputAction = new ConcreteInputAction(dateTime);
	}
	
	@After
	public void tearDown() throws Exception
	{
		inputAction = null;
		
		date = null;
		time = null;
		dateTime = null;
	}
	
	//=============================================================
	
	@Test(expected=IllegalArgumentException.class)
	public void testAbstractInputAction_nullDateTime()
	{
		inputAction = new ConcreteInputAction(null);
	}
	
	@Test
	public void testAbstractInputAction_valid()
	{
		assertEquals(inputAction.getActionDateTime(), dateTime);
		assertEquals(inputAction.getActionId().toString(), "20161209052751000500830");
	}
	
	//=============================================================
	
	@Test
	public void testGetActionDateTime()
	{
		assertEquals(inputAction.getActionDateTime(), dateTime);
	}
	
	//=============================================================
	
	@Test
	public void testGetActionDate()
	{
		assertEquals(inputAction.getActionDate(), date);
	}

	//=============================================================
	
	@Test
	public void testGetActionTime()
	{
		assertEquals(inputAction.getActionTime(), time);
	}

	//=============================================================
	
	@Test
	public void testGetActionId()
	{
		assertEquals(inputAction.getActionId().toString(), "20161209052751000500830");
	}

	//=============================================================
	
	@Test
	public void testGetActionTimeString()
	{
		assertEquals(inputAction.getActionTimeString(), "05:27:51:000500830");
	}

	//=============================================================
	
	@Test
	public void testGetActionDateString()
	{
		assertEquals(inputAction.getActionDateString(), "12/09/2016");
	}

	//=============================================================
	
	@Test
	public void testToString()
	{
		assertEquals(inputAction.toString(), "12/09/2016 @ 05:27:51:000500830");
	}

	//=============================================================
	
}
