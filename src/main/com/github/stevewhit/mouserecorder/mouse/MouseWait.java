package com.github.stevewhit.mouserecorder.mouse;

public class MouseWait extends MouseAction
{
	// Wait time (ms)
	private Integer time;
	
	/**
	 * Main constructor for the class that initializes the wait time.
	 * @param time Time in nanoseconds. 
	 */
	public MouseWait(Integer time)
	{
		this.time = time;
	}
}
