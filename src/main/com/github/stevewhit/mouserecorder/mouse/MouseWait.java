package com.github.stevewhit.mouserecorder.mouse;

import java.time.LocalDateTime;

public class MouseWait extends MouseAction
{
	/**
	 * Wait time in nanoseconds
	 */
	private long waitTime;
	
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
	 * Returns the wait time for this action.
	 * @return Returns the wait time in nanoseconds
	 */
	public long getWaitTime()
	{
		return this.waitTime;
	}
	
	/**
	 * Updates/stores the waitTime for this action.
	 * @param waitTime Wait time in nanoseconds. Must be greater or equal to 0.
	 */
	public void setWaitTime(long waitTime) throws IllegalArgumentException
	{
		if (waitTime < 0)
			throw new IllegalArgumentException("Wait time must be greater than or equal to 0.");
		
		this.waitTime = waitTime;
	}

	/**
	 * Returns the wait details of this instance as well as the creation information.
	 * @return Returns a formatted string detailing the waitTime and creation time details.
	 */
	public String getDetails()
	{
		return String.format("%1s --> %2s", this.toString(), super.toString());
	}
	
	@Override
	public String toString()
	{
		return String.format("Waiting: %1dns", waitTime);
	}
}
