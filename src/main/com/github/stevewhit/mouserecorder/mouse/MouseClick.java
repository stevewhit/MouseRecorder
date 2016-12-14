package com.github.stevewhit.mouserecorder.mouse;

import java.time.LocalDateTime;

import com.github.stevewhit.mouserecorder.monitor.Pixel;

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
	 * The pixel information where the click action is performed.
	 */
	private Pixel pixelClicked;
	
	/**
	 * How long the mouse button is held before being released in nanoseconds.
	 */
	private long clickHoldTime;

	/**
	 * Default constructor that sets up the {@link MouseClick} with a button and hold time.
	 * @param button The mouse button that is pressed while performing the *click* action.
	 * @param pixelClicked The pixel information that is clicked.
	 * @param clickHoldTime The time in milliseconds that the button is pressed before releasing.
	 */
	public MouseClick(MouseButton button, Pixel pixelClicked, long clickHoldTime) throws IllegalArgumentException
	{
		super (LocalDateTime.now());
		
		setMouseButton(button);
		setPixelClicked(pixelClicked);
		setClickHoldTime(clickHoldTime);
	}
	
	/**
	 * Updates the button that is pressed during the *click* action.
	 * @param button The mouse button that is pressed while performing the *click* action.
	 */
	private void setMouseButton(MouseButton button) throws IllegalArgumentException
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
	 * Updates the pixel that is clicked during the click action.
	 * @param pixelClicked - The pixel information regarding the pixel that is clicked.
	 */
	private void setPixelClicked(Pixel pixelClicked)
	{
		if(pixelClicked == null)
			throw new IllegalArgumentException("Pixel clicked must not be null");
		
		this.pixelClicked = pixelClicked;
	}
	
	/**
	 * Returns the pixel that is clicked.
	 * @return Returns the pixel information that is clicked.
	 */
	public Pixel getPixelClicked()
	{
		return this.pixelClicked;
	}
	
	/**
	 * Updates the time the mouse button is pressed before releasing.
	 * @param clickHoldTime The time in nanoseconds that the button is pressed before releasing.
	 */
	private void setClickHoldTime(long clickHoldTime) throws IllegalArgumentException
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
		return String.format("%1s click %2s, hold %3dns", mouseButton, pixelClicked.toString(), clickHoldTime);
	}
}
