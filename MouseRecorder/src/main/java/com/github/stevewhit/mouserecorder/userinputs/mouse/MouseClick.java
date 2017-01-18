package com.github.stevewhit.mouserecorder.userinputs.mouse;

import java.time.LocalDateTime;

import com.github.stevewhit.mouserecorder.monitor.Pixel;
import com.github.stevewhit.mouserecorder.userinputs.AbstractInputAction;

/**
 * Represents a Mouse click action which contains a button pressed, pixel, and hold time.
 * @author Steve Whitmire (swhit114@gmail.com)
 *
 */
public class MouseClick extends AbstractInputAction
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
	 * How long the mouse button is held before being released in milliseconds.
	 */
	private long clickHoldTime;
	
	/**
	 * Constructor that sets up the mouse click with a button and hold time.
	 * @param button The mouse button that is pressed while performing the *click* action.
	 * @param pixelClicked The pixel information that is clicked.
	 * @param clickHoldTime The time in milliseconds that the button is pressed before releasing.
	 */
	public MouseClick(MouseButton button, Pixel pixelClicked, long clickHoldTime) throws IllegalArgumentException
	{
		super (LocalDateTime.now());
		
		try
		{
			setMouseButton(button);
			setPixelClicked(pixelClicked);
			setClickHoldTime(clickHoldTime);
		}
		catch (IllegalArgumentException ex)
		{
			button = null;
			pixelClicked = null;
			clickHoldTime = -1;
			
			throw new IllegalArgumentException("Cannot create MouseClick action because ==> " + ex.getMessage());
		}
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
	 * Returns the information from the pixel that is clicked.
	 * @return Returns the information from the pixel that is clicked.
	 */
	public Pixel getPixelClicked()
	{
		return this.pixelClicked;
	}
	
	/**
	 * Returns the amount of time the button is pressed before releasing.
	 * @return Returns the amount of time (in milliseconds) the button is pressed before releasing.
	 */
	public long getClickHoldTime()
	{
		return this.clickHoldTime;
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * Checks this action for an invalid pixel and hold times less than 0.
	 * </p>
	 */
	public boolean isValidAction()
	{
		if (pixelClicked == null || mouseButton == null)
		{
			return false;
		}
			
		return (pixelClicked.isValidPixel()) && (clickHoldTime >= 0 );
	}
	
	/**
	 * Returns a formatted string that represents the click action.
	 * <pre>
	 * Example output: 'Right click (12, 234), hold 123ms'
	 * </pre>
	 */
	@Override
	public String toString()
	{
		if (!isValidAction())
		{
			return null;
		}
		
		return String.format("%1s click %2s, hold %3dms", mouseButton, pixelClicked.getLocation().toString(), clickHoldTime);
	}
	
	/**
	 * Updates the button that is pressed during the *click* action.
	 * @param button The mouse button that is pressed while performing the *click* action.
	 */
	private void setMouseButton(MouseButton button) throws IllegalArgumentException
	{
		if (button == null)
		{
			throw new IllegalArgumentException("Mouse button must not be null.");
		}
		
		this.mouseButton = button;
	}
	
	/**
	 * Updates the pixel that is clicked during the click action.
	 * @param pixelClicked - The pixel information regarding the pixel that is clicked.
	 */
	private void setPixelClicked(Pixel pixelClicked) throws IllegalArgumentException
	{
		if(pixelClicked == null || !pixelClicked.isValidPixel())
		{
			throw new IllegalArgumentException("Pixel clicked is invalid or null.");
		}
		
		this.pixelClicked = pixelClicked;
	}
	
	/**
	 * Updates the time the mouse button is pressed before releasing.
	 * @param clickHoldTime The time in milliseconds that the button is pressed before releasing.
	 * @throws Throws if click hold time is negative.
	 */
	private void setClickHoldTime(long clickHoldTime) throws IllegalArgumentException
	{
		if (clickHoldTime < 0)
		{
			throw new IllegalArgumentException("Hold time must be at least 0 milliseconds.");
		}
		
		this.clickHoldTime = clickHoldTime;
	}	
}
