package com.github.stevewhit.mouserecorder.userinputs.mouse;

import com.github.stevewhit.mouserecorder.monitor.Pixel;
import com.github.stevewhit.mouserecorder.monitor.PixelColor;

/**
 * Represents a user input action using one of the mouse buttons.
 * @author Steve Whitmire (swhit114@gmail.com)
 *
 */
public abstract class AbstractMouseButtonAction extends AbstractMouseInputAction
{
	/**
	 * The mouse button that is used for this action.
	 */
	private MouseButton buttonPressed;
	
	/**
	 * The color of the pixel for this action.
	 */
	private PixelColor pixelColor;
	
	/**
	 * Constructor that accepts a button pressed, pixel information, and a time stamp.
	 * @param buttonPressed The mouse button that was used for this action.
	 * @param pixelInfo Pixel coordinate and color information collected while recording.
	 * @param timeStamp The time (in nanoseconds) that this action occurred.
	 * @throws IllegalArgumentException Throws if the passed pixelinfo is invalid or null, or if the timestamp is negative.
	 */
	protected AbstractMouseButtonAction(MouseButton buttonPressed, Pixel pixelInfo, long timeStamp) throws IllegalArgumentException
	{
		// Since java limits you to use super() as your first call in the constructor, 
		// if the pixel is null, send a null location to force an error; 
		// otherwise send the location.
		super(pixelInfo == null ? null : pixelInfo.getLocation(), timeStamp);
		
		try
		{
			setButtonPressed(buttonPressed);
			setPixelColor(pixelInfo);
		}
		catch (IllegalArgumentException ex)
		{
			throw new IllegalArgumentException("Could not create mouse button action because ==> " + ex.getMessage());
		}
	}
	
	@Override
	public abstract String toString();
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * Also, checks for appropriate pixel color initialization.
	 * </p>
	 */
	@Override 
	public boolean isValidAction()
	{
		return super.isValidAction() && pixelColor != null;
	}
	
	/**
	 * Returns the button that is used for this action.
	 * @return Returns the button that is used for this action as a MouseButton.
	 */
	public MouseButton getMouseButton()
	{
		return buttonPressed;
	}
	
	/**
	 * Returns the pixel color that was identified for this action.
	 * @return Returns the pixel color for this action as a PixelColor.
	 */
	public PixelColor getPixelColor()
	{
		return pixelColor;
	}

	/**
	 * Updates the button that is pressed for this action.
	 * @param buttonPressed The mouse button that is pressed during this action.
	 * @throws IllegalArgumentException Throws if button pressed is null.
	 */
	private void setButtonPressed(MouseButton buttonPressed) throws IllegalArgumentException
	{
		if (buttonPressed == null)
		{
			throw new IllegalArgumentException("Cannot have a null button pressed.");
		}
		
		this.buttonPressed = buttonPressed;
	}
	
	/**
	 * Updates the pixel color for this object by using the color object inside the pixel.
	 * @param pixelInfo The relevant pixel information for this action.
	 * @throws IllegalArgumentException Throws if the pixel or the pixel color are null.
	 */
	private void setPixelColor(Pixel pixelInfo) throws IllegalArgumentException
	{
		if (pixelInfo == null || pixelInfo.getColor() == null)
		{
			throw new IllegalArgumentException("Invalid pixel details supplied.");
		}
		
		this.pixelColor = pixelInfo.getColor();
	}
}
