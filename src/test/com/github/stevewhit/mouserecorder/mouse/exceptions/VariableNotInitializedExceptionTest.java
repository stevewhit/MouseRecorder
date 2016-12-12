package com.github.stevewhit.mouserecorder.mouse.exceptions;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class VariableNotInitializedExceptionTest
{
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Test
	public void testVariableNotInitializedException() throws VariableNotInitializedException
	{
		String message = "Throw this message!";
		
		expectedException.expect(MouseActionException.class);
		expectedException.expectMessage(message);
		throw new VariableNotInitializedException(message);
	}
}
