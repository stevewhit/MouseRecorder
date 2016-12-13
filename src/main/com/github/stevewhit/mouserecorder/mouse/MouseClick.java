package com.github.stevewhit.mouserecorder.mouse;

import java.time.LocalDateTime;

public class MouseClick extends MouseAction
{
	/**
	 * Enumeration that indicates which mouse button is pressed.
	 * @author Steve
	 */
	public enum MouseButton
	{
		Right, 
		Left,
		ScrollWheel
	};
	
	/**
	 * The mouse button that is pressed while performing the *click* action.
	 */
	private MouseButton mouseButton;
	
	/**
	 * How long the mouse button is held before being released in nanoseconds.
	 */
	private long clickHoldTime;

	/**
	 * Default constructor that sets up the {@link MouseClick} with a button and hold time.
	 * @param button The mouse button that is pressed while performing the *click* action.
	 * @param clickHoldTime The time in milliseconds that the button is pressed before releasing.
	 */
	public MouseClick(MouseButton button, long clickHoldTime) throws IllegalArgumentException
	{
		super (LocalDateTime.now());
		
		setMouseButton(button);
		setClickHoldTime(clickHoldTime);
	}
	
	/**
	 * Updates the button that is pressed during the *click* action.
	 * @param button The mouse button that is pressed while performing the *click* action.
	 */
	public void setMouseButton(MouseButton button) throws IllegalArgumentException
	{
		if (button == null)
			throw new IllegalArgumentException("Mouse button must not be null.");
		
		this.mouseButton = button;
	}
	
	/**
	 * Returns the button that is pressed during the *click* action.
	 * @return Returns the button that is pressed during the *click* action.
	 */
	public MouseButton getMouseButton()
	{
		return this.mouseButton;
	}
	
	/**
	 * Updates the time the mouse button is pressed before releasing.
	 * @param clickHoldTime The time in nanoseconds that the button is pressed before releasing.
	 */
	public void setClickHoldTime(long clickHoldTime) throws IllegalArgumentException
	{
		if (clickHoldTime < 0)
			throw new IllegalArgumentException("Hold time must be at least 0 milliseconds.");
		
		this.clickHoldTime = clickHoldTime;
	}
	
	/**
	 * Returns the amount of time the button is pressed before releasing.
	 * @return Returns the amount of time (in nanoseconds) the button is pressed before releasing.
	 */
	public long getClickHoldTime()
	{
		return this.clickHoldTime;
	}
	
	@Override
	public String toString()
	{
		return String.format("%1s click and hold %1dns", mouseButton, clickHoldTime);
	}
}
