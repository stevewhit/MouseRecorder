package com.github.stevewhit.mouserecorder.userinputs.mouse;

import com.github.stevewhit.mouserecorder.monitor.Pixel;

/**
 * Represents a mouse button being released and contains relevant button information, pixel information, and the timestamp from what it occurred.
 * @author Steve Whitmire (swhit114@gmail.com)
 *
 */
public class MouseButtonRelease extends AbstractMouseButtonAction
{
	/**
	 * Constructor that accepts a button released, pixel information, and a time stamp.
	 * @param buttonPressed The mouse button that was used for this releasing action.
	 * @param pixelInfo Pixel coordinate and color information collected while recording.
	 * @param timeStamp The time (in nanoseconds) that this action occurred.
	 * @throws IllegalArgumentException Throws if the passed pixelinfo is invalid or null, or if the timestamp is negative.
	 */
	public MouseButtonRelease(MouseButton buttonPressed, Pixel pixelInfo, long timeStamp) throws IllegalArgumentException
	{
		super(buttonPressed, pixelInfo, timeStamp);
	}

	/**
	 * {@inheritDoc}
	 * <pre>
	 * Example output: MOUSERELEASE: 'LEFT' : (12, 435) : (R132/G345/B321)
	 * </pre>
	 */
	@Override
	public String toString()
	{
		if (!super.isValidAction())
		{
			return null;
		}
		
		return String.format("MOUSERELEASE: '%1$s' : %2$s : %3$s", getMouseButton(), getLocation(), getPixelColor());
	}
}
