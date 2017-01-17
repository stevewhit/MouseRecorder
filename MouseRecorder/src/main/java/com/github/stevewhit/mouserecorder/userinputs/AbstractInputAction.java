package com.github.stevewhit.mouserecorder.userinputs;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public abstract class AbstractInputAction
{
	private LocalDateTime actionDateTime;
	
	private BigInteger actionId;
	
	public AbstractInputAction(LocalDateTime actionDateTime) throws IllegalArgumentException, IllegalStateException
	{
		try
		{
			setActionDateTime(actionDateTime);
			setActionId();
		}
		catch (IllegalArgumentException ex) 
		{
			actionDateTime = null;
			actionId = null;
			
			throw new IllegalArgumentException("Could not create input action because ==> " + ex.getMessage());
		}
	}
	
	/**
	 * Returns the date time of the action.
	 * @return Returns the date time of the action as a LocalDateTime.
	 */
	public LocalDateTime getActionDateTime()
	{
		return this.actionDateTime;
	}
	
	/**
	 * Returns the date portion of the datetime stored by the action.
	 * @return Returns the date of the action as a LocalDate.
	 */
	public LocalDate getActionDate()
	{
		return actionDateTime.toLocalDate();
	}
	
	/**
	 * Returns the time portion of the date time for the action.
	 * @return Returns the time portion of the action as a LocalTime.
	 */
	public LocalTime getActionTime()
	{
		return actionDateTime.toLocalTime();
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
	 * Returns the time of the action as a formatted string.
	 * @return Returns the time of the action in the following string format: HH:mm:ss:SSSSSSSSS
	 */
	public String getActionTimeString()
	{
		return actionDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss:SSSSSSSSS"));
	}
	
	/**
	 * Returns the date of the action as a formatted string.
	 * @return Returns the date of the action in the following string format: MM/dd/yyyy
	 */
	public String getActionDateString()
	{
		return actionDateTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
	}
	
	/**
	 * Returns a formatted string that represents the action.
	 * <pre>
	 * Example output: '06/09/2016 @ 05:27:51:000500830'
	 * </pre>
	 */
	@Override
	public String toString()
	{
		return String.format("%1s @ %2s", getActionDateString(), getActionTimeString());
	}
	
	/**
	 * Sets the action date time for this action.
	 * @param actionDateTime The local date time associated with this action.
	 * @throws IllegalArgumentException
	 */
	private void setActionDateTime(LocalDateTime actionDateTime) throws IllegalArgumentException
	{
		if (actionDateTime == null)
		{
			throw new IllegalArgumentException("Cannot set a null actionDateTime.");
		}
		
		this.actionDateTime = actionDateTime;
	}
	
	/**
	 * Generates a new action id for this action by combining various parts of the date time.
	 * @throws IllegalStateException Throws if the action date time is not set before this method is called.
	 */
	private void setActionId() throws IllegalStateException
	{
		if (actionDateTime == null)
		{
			throw new IllegalStateException("Action date time must be set before creating a new action id.");
		}
		
		this.actionId = new BigInteger(actionDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSSSSSSS")));
	}
}
