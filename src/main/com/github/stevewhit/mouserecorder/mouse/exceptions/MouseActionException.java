package com.github.stevewhit.mouserecorder.mouse.exceptions;

public class MouseActionException extends Exception
{
	/**
	 * Throws an exception that contains a message. 
	 * @param message Message to be displayed with the exception.
	 */
	public MouseActionException(String message) 
	{ 
		super(message); 
	}
}

