package com.github.stevewhit.mouserecorder.userinputs.mouse;

import java.time.LocalDateTime;

import com.github.stevewhit.mouserecorder.userinputs.AbstractInputAction;

public class MouseWait extends AbstractInputAction
{
	/**
	 * Wait time in milliseconds
	 */
	private long waitTime;
	
	/**
	 * Main constructor for the class that initializes the wait time.
	 * @param waitTime Time in milliseconds. 
	 */
	public MouseWait(long waitTime) throws IllegalArgumentException
	{
		super(LocalDateTime.now());
		
		try
		{
			setWaitTime(waitTime);
		}
		catch (IllegalArgumentException ex) 
		{
			waitTime = -1;
			
			throw new IllegalArgumentException("Could not create mouse wait action because ==> " + ex.getMessage());
		}
	}
	
	/**
	 * Returns the wait time associated with this mouse wait action.
	 * @return Returns the wait time in milliseconds.
	 */
	public long getWaitTime()
	{
		return this.waitTime;
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * Checks this action to make sure wait time is >= 0.
	 * </p>
	 */
	public boolean isValidAction()
	{
		return waitTime >= 0;
	}
	
	/**
	 * Returns a formatted string that represents this mouse wait action.
	 * <pre>
	 * Example output: 'Wait: 12343ms'
	 * </pre>
	 */
	@Override
	public String toString()
	{
		if (!isValidAction())
		{
			return null;
		}
		
		return String.format("Wait: %1$dms", waitTime);
	}
	
	/**
	 * Updates the wait time for this Mousewait action.
	 * @param waitTime The amount of time to wait in milliseconds.
	 */
	private void setWaitTime(long waitTime)
	{
		if (waitTime < 0)
		{
			throw new IllegalArgumentException("Wait time cannot be less than 0.");
		}
		
		this.waitTime = waitTime;
	}
}
