package com.github.stevewhit.mouserecorder.mouse.exceptions;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.github.stevewhit.mouserecorder.mouse.MouseAction;
import com.github.stevewhit.mouserecorder.mouse.MouseActionTest.MouseActionInstanceTest;

public class MouseActionExceptionTest
{
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Test
	public void testMouseActionException() throws MouseActionException
	{
		String message = "Throw this message!";
		
		expectedException.expect(MouseActionException.class);
		expectedException.expectMessage(message);
		throw new MouseActionException(message);
	}
}
