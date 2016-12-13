package com.github.stevewhit.mouserecorder.mouse;

import static org.junit.Assert.*;

import java.time.*;
import org.junit.*;
import org.junit.rules.ExpectedException;

public class MouseActionTest
{
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
		
		actionWithValidDateTime = new MouseAction(dateTime);
	}

	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Test 
	public void testMouseActionValidDateTime()
	{
		assertEquals(actionWithValidDateTime.getDateTime(), dateTime);
		assertEquals(actionWithValidDateTime.getActionId().toString(), "20161209052751000500830");
	}
	
	@Test 
	public void testMouseActionInvalidDateTime()
	{
		expectedException.expect(IllegalArgumentException.class);
		MouseAction invalidMouseAction = new MouseAction(null);
	}
	
	@Test 
	public void testGetActionId()
	{
		assertEquals(actionWithValidDateTime.getActionId().toString(), "20161209052751000500830");
	}
	
	@Test
	public void testGetDateTime()
	{
		assertEquals(actionWithValidDateTime.getDateTime(), dateTime);
	}
	
	@Test
	public void testGetTime()
	{
		assertEquals(actionWithValidDateTime.getTime(), time);
	}
	
	@Test
	public void testGetTimeFormattedString()
	{
		assertEquals(actionWithValidDateTime.getTimeFormattedString(), "05:27:51:000500830");
	}
	
	@Test
	public void testGetDate()
	{
		assertEquals(actionWithValidDateTime.getDate(), date);
	}
	
	@Test
	public void testGetDateFormattedString()
	{
		String expectedDateString = actionWithValidDateTime.getDateFormattedString();
		assertEquals(expectedDateString, "12/09/2016");
	}
	
	@Test
	public void testToString()
	{
		assertEquals(actionWithValidDateTime.toString(), "Created: 12/09/2016_05:27:51:000500830");
	}
}
