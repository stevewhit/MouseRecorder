package com.github.stevewhit.mouserecorder.Mouse;

import static org.junit.Assert.*;

import java.time.*;
import org.junit.*;

public class MouseActionTest
{
	public class MouseActionInstanceTest extends MouseAction
	{
		public MouseActionInstanceTest()
		{
			// Used for testing purposes only.
		}
	}
	
	MouseAction actionWithValidDateTime;
	
	LocalDate date;
	LocalTime time;
	LocalDateTime dateTime;

	@Before
	public void setup()
	{
		date = LocalDate.of(2016, 12, 9);
		time = LocalTime.of(5, 27, 51, 500830);
		dateTime = LocalDateTime.of(date, time);
		
		actionWithValidDateTime = new MouseActionInstanceTest();
		actionWithValidDateTime.setDateTime(dateTime);
	}

	@Test
	public void testGetDateTime()
	{
		assertEquals(actionWithValidDateTime.getDateTime(), dateTime);
	}
	
	@Test
	public void testSetDateTime()
	{
		MouseAction action = new MouseActionInstanceTest();		
		action.setDateTime(dateTime);
	
		assertEquals(action.getDateTime(), dateTime);
	}
	
	@Test
	public void testGetTime()
	{
		assertEquals(actionWithValidDateTime.getTime(), time);
	}
	
	@Test
	public void testGetTimeToString()
	{
		assertEquals(actionWithValidDateTime.getTimeToString(), "05:27:51:000500830");
	}
	
	@Test
	public void testGetDate()
	{
		assertEquals(actionWithValidDateTime.getDate(), date);
	}
	
	@Test
	public void testGetDateToString()
	{
		String dateString = actionWithValidDateTime.getDateToString();
		
		assertEquals(dateString, "12/09/2016");
	}
	
	@Test
	public void testToString()
	{
		assertEquals(actionWithValidDateTime.goString(), "12/09/2016_05:27:51:000500830");
	}
}
