package com.github.stevewhit.mouserecorder.userinputs;

/**
 * Represents a waiting action where the user doesn't supply any input for a period of time.
 * @author Steve Whitmire (swhit114@gmail.com)
 *
 */
public class InputWaitAction extends AbstractInputAction
{
	/**
	 * The time (in nanoseconds) that the program should wait before performing any other actions.
	 */
	private long waitTime;
	
	/**
	 * Constructor that accepts a wait time and time stamp.
	 * @param waitTime The time (in nanoseconds) that the program should wait before performing any other actions. 
	 * @param timeStamp The time that this action occured (in nanoseconds)
	 * @throws IllegalArgumentException Throws if the wait time or time stamp are negative.
	 */
	public InputWaitAction(long waitTime, long timeStamp) throws IllegalArgumentException
	{
		super(timeStamp);
		
		try
		{
			setWaitTime(waitTime);
		}
		catch (IllegalArgumentException ex) 
		{
			throw new IllegalArgumentException("Could not create wait action because ==> " + ex.getMessage());
		}
	}
	
	/**
	 * {@inheritDoc}
	 * <pre>
	 * Example output: 'WAIT: 12341324ns'
	 * </pre>
	 */
	@Override
	public String toString()
	{
		if (!isValidAction())
		{
			return null;
		}
		
		return String.format("WAIT: %1$sns", String.valueOf(waitTime));
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Also checks that the wait time for this action is valid.
	 * </p>
	 */
	@Override
	public boolean isValidAction()
	{
		return super.isValidAction() && waitTime >= 0;
	}
	
	/**
	 * Returns the time that the program needs to wait (in nanoseconds)
	 * @return Returns the time that the program needs to wait (in nanoseconds)
	 */
	public long getWaitTime()
	{
		return waitTime;
	}
	
	/**
	 * Updates the wait time for this mouse wait action.
	 * @param waitTime The wait time (in nanoseconds) for this mouse wait action.
	 * @throws IllegalArgumentException Throws if the wait time is negative.
	 */
	private void setWaitTime(long waitTime) throws IllegalArgumentException
	{
		if (waitTime < 0)
		{
			throw new IllegalArgumentException("Mouse wait time cannot be negative.");
		}
		
		this.waitTime = waitTime;
	}
}
