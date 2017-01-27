package com.github.stevewhit.mouserecorder.userinputs.mouse;

import com.github.stevewhit.mouserecorder.monitor.PixelCoordinate2D;

/**
 * Represents a mouse pointer moving on the computer screen to a desired pixel coordinate.
 * @author Steve Whitmire (swhit114@gmail.com)
 *
 */
public class MouseMove extends AbstractMouseInputAction
{
	/**
	 * Constructor that initializes location to move to.
	 * @param location The location of where to move the mouse to.
	 * @throws IllegalArgumentException Throws if the location is invalid or the timeStamp is negative.
	 */
	public MouseMove(PixelCoordinate2D location, long timeStamp) throws IllegalArgumentException
	{
		super(location, timeStamp);
	}
	
	/**
	 * {@inheritDoc}
	 * <pre>
	 * Example output: MOUSEMOVE: (123, 456)
	 * </pre>
	 */
	@Override
	public String toString()
	{
		if (!super.isValidAction())
		{
			return null;
		}
		
		return String.format("MOUSEMOVE: %1$s", getLocation().toString());
	}
}