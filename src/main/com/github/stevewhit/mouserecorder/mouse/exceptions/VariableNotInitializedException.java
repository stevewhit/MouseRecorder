package com.github.stevewhit.mouserecorder.mouse.exceptions;

public class VariableNotInitializedException extends MouseActionException
{
	/**
	 * Throws an exception that contains a message. 
	 * @param message Message to be displayed with the exception.
	 */
	public VariableNotInitializedException(String message)
	{
		super(message);
	}
}