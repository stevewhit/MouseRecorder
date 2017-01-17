package com.github.stevewhit.mouserecorder.userinputs.mouse;

import java.time.LocalDateTime;
import com.github.stevewhit.mouserecorder.monitor.Pixel;
import com.github.stevewhit.mouserecorder.userinputs.AbstractInputAction;

/**
 * Represents a mouse pointer moving on the computer screen from one pixel to the next.
 * @author Steve Whitmire (swhit114@gmail.com)
 *
 */
public class MouseMove extends AbstractInputAction
{
	/**
	 * The starting pixel in the mouse move.
	 */
	private Pixel startPixel;
	
	/**
	 * The ending pixel in the mouse move.
	 */
	private Pixel endPixel;
		
	/**
	 * Constructor that initializes the start and end pixel for this move.
	 * @param startPixel The starting pixel for the move.
	 * @param endPixel The ending pixel for the move.
	 * @throws IllegalArgumentException Throws if either the start or end pixel are null or invalid.
	 */
	public MouseMove(Pixel startPixel, Pixel endPixel) throws IllegalArgumentException
	{
		super(LocalDateTime.now());
		
		try
		{
			setStartPixel(startPixel);
			setEndPixel(endPixel);
		}
		catch (IllegalArgumentException ex)
		{
			this.startPixel = null;
			this.endPixel = null;
			
			throw new IllegalArgumentException("Could not create mouse move action because ==> " + ex.getMessage());
		}
	}
	
	/**
	 * Returns the starting pixel for this move.
	 * @return Returns the starting pixel for this move as a Pixel object.
	 */
	public Pixel getStartPixel()
	{
		return this.startPixel;
	}
	
	/**
	 * Returns the ending pixel for this move.
	 * @return Returns the ending pixel for this move as a Pixel object.
	 */
	public Pixel getEndPixel()
	{
		return this.endPixel;
	}
	
	/**
	 * Calculates the distance from the starting pixel to the ending pixel.
	 * @return Returns the distance from the starting pixel to the ending pixel as a double.
	 * @throws IllegalStateException Throws if the starting and ending pixels aren't initialized or they're invalid.
	 */
	public double getMoveDistance() throws IllegalStateException
	{
		if (!isValidMove())
		{
			throw new IllegalStateException("Starting and ending pixels need to be initialized and valid.");
		}
		
		final int xDist = endPixel.getLocation().getX() - startPixel.getLocation().getX();
		final int yDist = endPixel.getLocation().getY() - startPixel.getLocation().getY();
		
		// Pythagorean theorem
		return Math.sqrt( Math.pow(xDist, 2) + Math.pow(yDist, 2));
	}
	
	/**
	 * Checks for proper initialization of the starting and ending pixels and that each pixel is valid.
	 * @return Returns true if both pixels are valid.
	 */
	public boolean isValidMove()
	{
		return (startPixel != null) && startPixel.isValidPixel() && (endPixel != null) && endPixel.isValidPixel();
	}
	
	/**
	 * Returns a formatted string representation for this mouse move.
	 * <pre>
	 * Example output: 'Move: (12, 345)-->(13, 345)'
	 * </pre>
	 */
	@Override
	public String toString()
	{
		if (!isValidMove())
		{
			return null;
		}
		
		return String.format("Move: %1$s-->%2$s", startPixel.getLocation().toString(), endPixel.getLocation().toString());
	}
	
	
	/**
	 * Updates the mouse move start pixel.
	 * @param startPixel The start pixel to update the move to.
	 */
	private void setStartPixel(Pixel startPixel)
	{
		if (startPixel == null || !startPixel.isValidPixel())
		{
			throw new IllegalArgumentException("Cannot have a null or invalid start pixel.");
		}
		
		this.startPixel = startPixel;
	}
	
	/**
	 * Updates the mouse move end pixel.
	 * @param endPixel The end pixel to update the move to.
	 */
	private void setEndPixel(Pixel endPixel)
	{
		if (endPixel == null || !endPixel.isValidPixel())
		{
			throw new IllegalArgumentException("Cannot have a null or invalid end pixel");
		}
		
		this.endPixel = endPixel;
	}
}
