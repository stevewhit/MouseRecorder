package com.github.stevewhit.mouserecorder.userinputs.mouse;

import com.github.stevewhit.mouserecorder.monitor.PixelCoordinate2D;
import com.github.stevewhit.mouserecorder.userinputs.AbstractInputAction;

/**
 * An abstract class that represents a mouse click/release/move action
 * @author Steve Whitmire (swhit114@gmail.com)
 *
 */
public abstract class AbstractMouseInputAction extends AbstractInputAction
{
	/**
	 * The 2d coordinate location of this mouse input action.
	 */
	private PixelCoordinate2D location;
	
	/**
	 * Constructor that accepts a location and a timestamp.
	 * @param location The 2d coordinate location of this mouse input action.
	 * @param timeStamp The timestamp which this action occurred.
	 * @throws IllegalArgumentException Throws if the location is invalid or the timestamp is negative.
	 */
	protected AbstractMouseInputAction(PixelCoordinate2D location, long timeStamp) throws IllegalArgumentException
	{
		super(timeStamp);
		
		try
		{
			setLocation(location);
		}
		catch (IllegalArgumentException ex) 
		{
			this.location = null;
			
			throw new IllegalArgumentException("Cannot create mouse input action because ==> " + ex.getMessage());
		}
	}
	
	@Override
	public abstract String toString();
	
	/**
	 * {@inheritDoc}
	 * Also checks the input action for a valid pixel coordinate.
	 */
	@Override
	public boolean isValidAction()
	{
		return super.isValidAction() && location != null && location.isValidCoord();
	}
	
	/**
	 * Returns the 2d coordinate location of this mouse input action.
	 * @return Returns the PixelCoordinate2D location of this mouse input action.
	 */
	public PixelCoordinate2D getLocation()
	{
		return this.location;
	}
	
	/**
	 * Updates the 2d coordinate location of this mouse input action.
	 * @param location The 2d coordinate location to store for this action.
	 * @throws IllegalArgumentException Throws if the location is null or invalid.
	 */
	private void setLocation(PixelCoordinate2D location) throws IllegalArgumentException
	{
		if (location == null || !location.isValidCoord())
		{
			throw new IllegalArgumentException("Click action location must not be null or invalid.");
		}
		
		this.location = location;
	}
}
