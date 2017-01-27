package com.github.stevewhit.mouserecorder.userinputs.mouse;

import com.github.stevewhit.mouserecorder.userinputs.AbstractInputAction;

/**
 * An abstract class that represents a keyboard input action.
 * @author Steve Whitmire (swhit114@gmail.com)
 *
 */
public abstract class AbstractMouseAction extends AbstractInputAction
{
	/**
	 * Constructor that accepts a timestamp to initialize this action.
	 * @param timeStamp The time stamp for which this action occured (in nanoseconds)
	 * @throws IllegalArgumentException Throws if the timestamp is negative.
	 */
	protected AbstractMouseAction(long timeStamp) throws IllegalArgumentException
	{
		super(timeStamp);		
	}
	
	@Override
	public abstract String toString();
	
	@Override
	public abstract boolean isValidAction();
}
