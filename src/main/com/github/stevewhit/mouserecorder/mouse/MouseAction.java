package com.github.stevewhit.mouserecorder.mouse;

import java.math.BigInteger;
import java.time.*;
import java.time.format.*;

public class MouseAction
{
	private LocalDateTime actionDateTime;
	private BigInteger actionId;
	
	/**
	 * Main constructor for this class.
	 * @param actionDateTime The {@link LocalDateTime} stored by this action.
	 */
	public MouseAction(LocalDateTime actionDateTime) throws IllegalArgumentException
	{
		if (actionDateTime == null)
			throw new IllegalArgumentException("Cannot set actionDateTime to a null value.");
		
		setDateTime(actionDateTime);
	}

	/**
	 * Sets the {@link LocalDateTime} for the action.
	 * @param actionDateTime the {@link LocalDateTime} stored by this action.
	 */
	private void setDateTime(LocalDateTime actionDateTime) throws IllegalArgumentException
	{
		if (actionDateTime == null)
			throw new IllegalArgumentException("Cannot set actionDateTime to a null value.");
		
		this.actionDateTime = actionDateTime;
		
		setActionId();
	}

	/**
	 * Returns the {@link LocalDateTime} of the action.
	 * @return Returns the {@link LocalDateTime} of the action.
	 */
	public LocalDateTime getDateTime()
	{
		return this.actionDateTime;
	}
	
	/**
	 * Generates a new action id of the action by combining various parts of the dateTime.
	 */
	private void setActionId()
	{
		actionId = new BigInteger(actionDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSSSSSSS")));
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
	 * Returns the {@link LocalTime} portion of the {@link LocalDateTime} for the action.
	 * @return Returns the {@link LocalTime} of the action.
	 */
	public LocalTime getTime()
	{
		return actionDateTime.toLocalTime();
	}
	
	/**
	 * Returns the {@link LocalTime} of the action as a formatted string.
	 * @return Returns the {@link LocalTime} of the action in the following string format: HH:mm:ss:SSSSSSSSS
	 */
	public String getTimeFormattedString()
	{
		return actionDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss:SSSSSSSSS"));
	}
	
	/**
	 * Returns the {@link LocalDate} portion of the {@link LocalDateTime} stored by the action.
	 * @return {@link LocalDate} stored by the action.
	 */
	public LocalDate getDate()
	{
		return actionDateTime.toLocalDate();
	}
	
	/**
	 * Returns the {@link LocalDate} of the action as a formatted string.
	 * @return Returns the {@link LocalDate} of the action in the following string format: MM/dd/yyyy
	 */
	public String getDateFormattedString()
	{
		return actionDateTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
	}
	
	@Override
	public String toString()
	{
		return String.format("Created: %1s_%2s", getDateFormattedString(), getTimeFormattedString());
	}
}
