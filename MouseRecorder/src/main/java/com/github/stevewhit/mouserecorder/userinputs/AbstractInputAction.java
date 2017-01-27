package com.github.stevewhit.mouserecorder.userinputs;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a user input action that occurred. This object contains a timestamp and a actionId for identification purposes.
 * @author Steve Whitmire (swhit114@gmail.com)
 *
 */
public abstract class AbstractInputAction
{
	/**
	 * The random ID for this action to help with identification purposes.
	 */
	private BigInteger actionId;
	
	/**
	 * The time (in nanoseconds) that the action took place.
	 */
	private long timeStamp;
	
	/**
	 * Constructor that accepts a time to initialize this object.
	 * @param timeCreated The time (in nanoseconds) that the action took place.
	 * @throws IllegalArgumentException Throws if the time stamp is negative.
	 */
	protected AbstractInputAction(long timeStamp) throws IllegalArgumentException
	{
		try
		{
			setTimeStamp(timeStamp);
			setActionId();
		}
		catch (IllegalArgumentException ex) 
		{
			timeStamp = -1;
			actionId = null;
			
			throw new IllegalArgumentException("Could not create input action because ==> " + ex.getMessage());
		}
	}
	
	/**
	 * Verifies the action is initialized properly and meets all action requirements.
	 * @return Returns true if the action is valid; otherwise false.
	 */
	public boolean isValidAction()
	{
		return timeStamp >= 0 && actionId != null;
	}
	
	/**
	 * Returns the time that this action took place
	 * @return Returns the time that this action took place in nanoseconds.
	 */
	public long getTimeStamp()
	{
		return this.timeStamp;
	}
	
	/**
	 * Returns the actionId of the action.
	 * @return The actionId of the action as an Integer in the format yyyyMMddHHmmssSSSSSSSSS
	 */
	public BigInteger getActionId()
	{
		return this.actionId;
	}
	
	/**
	 * Returns a formatted string that represents the action.
	 * <pre>
	 * Example output: Timestamp: 123456789876541ns
	 * </pre>
	 */
	@Override
	public String toString()
	{
		return String.format("Timestamp: %1$sns", String.valueOf(getTimeStamp()));
	}
	
	/**
	 * Sets the time stamp for this action.
	 * @param timeStamp The time stamp (in nanoseconds) this action occurred.
	 * @throws IllegalArgumentException Throws if the time stamp is negative.
	 */
	private void setTimeStamp(long timeStamp) throws IllegalArgumentException
	{
		if (timeStamp < 0)
		{
			throw new IllegalArgumentException("Action cannot have a negative timestamp.");
		}
		
		this.timeStamp = timeStamp;
	}
	
	/**
	 * Generates a new action id for this action by combining various parts of the date time.
	 */
	private void setActionId()
	{
		this.actionId = new BigInteger(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSSSSSSS")));
	}
}
