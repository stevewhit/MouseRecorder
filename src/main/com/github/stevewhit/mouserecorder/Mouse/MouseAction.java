package com.github.stevewhit.mouserecorder.Mouse;

import java.time.*;
import java.time.format.*;

public abstract class MouseAction
{
	private LocalDateTime _actionDateTime;
	
	/**
	 * Sets the {@link LocalDateTime} for the action.
	 * @param actionDateTime the {@link LocalDateTime} stored by this action.
	 */
	public void setDateTime(LocalDateTime actionDateTime)
	{
		this._actionDateTime = actionDateTime;
	}
	
	/**
	 * Returns the {@link LocalDateTime} of the action.
	 * @return Returns the {@link LocalDateTime} of the action.
	 */
	public LocalDateTime getDateTime()
	{
		return this._actionDateTime;
	}
	
	/**
	 * Returns the {@link LocalTime} portion of the {@link LocalDateTime} for the action.
	 * @return Returns the {@link LocalTime} of the action.
	 */
	public LocalTime getTime()
	{
		return _actionDateTime.toLocalTime();
	}
	
	/**
	 * Returns the {@link LocalTime} of the action as a formatted string.
	 * @return Returns the {@link LocalTime} of the action in the following string format: HH:mm:ss:SSSSSSSSS
	 */
	public String getTimeToString()
	{
		return _actionDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss:SSSSSSSSS"));
	}
	
	/**
	 * Returns the {@link LocalDate} portion of the {@link LocalDateTime} stored by the action.
	 * @return {@link LocalDate} stored by the action.
	 */
	public LocalDate getDate()
	{
		return _actionDateTime.toLocalDate();
	}
	
	/**
	 * Returns the {@link LocalDate} of the action as a formatted string.
	 * @return Returns the {@link LocalDate} of the action in the following string format: MM/dd/yyyy
	 */
	public String getDateToString()
	{
		return _actionDateTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
	}
	
	/**
	 * Returns the {@link LocalDateTime} of the action as a formatted string.
	 * @return Returns the {@link LocalDateTime} of the action in the following string format: HH:mm:ss:SSSSSSSSS_MM/dd/yyyy
	 */
	public String goString()
	{
		return String.join("_", getDateToString(), getTimeToString());
	}
}
