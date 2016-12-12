package com.github.stevewhit.mouserecorder.mouse;

import static org.junit.Assert.*;

import java.time.*;
import org.junit.*;
import org.junit.rules.ExpectedException;

import com.github.stevewhit.mouserecorder.mouse.MouseAction;
import com.github.stevewhit.mouserecorder.mouse.exceptions.MouseActionException;
import com.github.stevewhit.mouserecorder.mouse.exceptions.VariableNotInitializedException;

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

	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Test
	public void testGetDateTime() throws VariableNotInitializedException
	{
		assertEquals(actionWithValidDateTime.getDateTime(), dateTime);
		
		expectedException.expect(VariableNotInitializedException.class);
		MouseAction tempAction = new MouseActionInstanceTest();
		tempAction.getDateTime();
	}
	
	@Test
	public void testSetDateTime() throws IllegalArgumentException, VariableNotInitializedException
	{
		MouseAction action = new MouseActionInstanceTest();		
		action.setDateTime(dateTime);
	
		assertEquals(action.getDateTime(), dateTime);
		
		expectedException.expect(IllegalArgumentException.class);
		MouseAction tempAction = new MouseActionInstanceTest();
		tempAction.setDateTime(null);
	}
	
	@Test
	public void testGetTime() throws VariableNotInitializedException
	{
		assertEquals(actionWithValidDateTime.getTime(), time);
		
		expectedException.expect(VariableNotInitializedException.class);
		MouseAction tempAction = new MouseActionInstanceTest();
		tempAction.getTime();
	}
	
	@Test
	public void testGetTimeFormattedString() throws VariableNotInitializedException
	{
		assertEquals(actionWithValidDateTime.getTimeFormattedString(), "05:27:51:000500830");
	
		expectedException.expect(VariableNotInitializedException.class);
		MouseAction tempAction = new MouseActionInstanceTest();
		tempAction.getTimeFormattedString();
	}
	
	@Test
	public void testGetDate() throws VariableNotInitializedException
	{
		assertEquals(actionWithValidDateTime.getDate(), date);
		
		expectedException.expect(VariableNotInitializedException.class);
		MouseAction tempAction = new MouseActionInstanceTest();
		tempAction.getDate();
	}
	
	@Test
	public void testGetDateFormattedString() throws VariableNotInitializedException
	{
		String expectedDateString = actionWithValidDateTime.getDateFormattedString();
		assertEquals(expectedDateString, "12/09/2016");
		
		expectedException.expect(VariableNotInitializedException.class);
		MouseAction tempAction = new MouseActionInstanceTest();
		tempAction.getDateFormattedString();
	}
	
	@Test
	public void testGetDateTimeString() throws VariableNotInitializedException
	{
		assertEquals(actionWithValidDateTime.getDateTimeString(), "12/09/2016_05:27:51:000500830");
		
		expectedException.expect(VariableNotInitializedException.class);
		MouseAction tempAction = new MouseActionInstanceTest();
		tempAction.getDateTimeString();
	}
}
