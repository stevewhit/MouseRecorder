package com.github.stevewhit.mouserecorder.mouse;

import java.time.LocalDateTime;

public class MouseWait extends MouseAction
{
	private long waitTime;				// Wait time in nano-seconds
	
	/**
	 * Main constructor for the class that initializes the wait time.
	 * @param waitTime Time in nanoseconds. 
	 */
	public MouseWait(long waitTime)
	{
		super(LocalDateTime.now());
		
		setWaitTime(waitTime);
	}
	
	/**
	 * Returns the wait time in nano-seconds.
	 * @return Returns the wait time in nano-seconds.
	 */
	public long getWaitTime()
	{
		return this.waitTime;
	}
	
	/**
	 * Updates/stores the waitTime for this action.
	 * @param waitTime Wait time in nano-seconds. Must be greater or equal to 0.
	 */
	public void setWaitTime(long waitTime) throws IllegalArgumentException
	{
		if (waitTime < 0)
			throw new IllegalArgumentException("Wait time must be greater than or equal to 0.");
		
		this.waitTime = waitTime;
	}
}
