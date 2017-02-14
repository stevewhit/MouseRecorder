package com.github.stevewhit.mouserecorder.ui;

import java.awt.Dimension;
import java.awt.Point;

/**
 * The location and dimension details of this window.
 * @author Steve Whitmire (swhit114@gmail.com)
 *
 */
public class ClickZoneDetails
{
	/**
	 * The (x, y) location of the top left corner of the window.
	 */
	private Point windowLocation;
	
	/**
	 * The Width x height dimensions of the window.
	 */
	private Dimension windowDimensions;
	
	/**
	 * Constructor that generates a window with the given details.
	 * @param xLoc The X coordinate location of the window.
	 * @param yLoc The Y coordinate location of the window.
	 * @param width The width of the window.
	 * @param height The height of the window.
	 * @throws IllegalArgumentException Throws if a negative value is supplied as a parameter.
	 */
	public ClickZoneDetails(int xLoc, int yLoc, int width, int height) throws IllegalArgumentException
	{
		try
		{
			setLocation(xLoc, yLoc);
			setDimensions(width, height);
		}
		catch(IllegalArgumentException ex)
		{
			this.windowDimensions = null;
			this.windowLocation = null;
			
			throw new IllegalArgumentException("Cannot create the click zone because ==> " + ex.getMessage());
		}
	}
	
	/**
	 * Returns the window (x, y) location.
	 * @return Returns the location of the window as a Point object.
	 */
	public Point getWindowLocation()
	{
		return this.windowLocation;
	}
	
	/**
	 * Returns the window dimensions.
	 * @return Returns the window dimensions as a Dimension object.
	 */
	public Dimension getWindowDimensions()
	{
		return this.windowDimensions;
	}
	
	/**
	 * Updates the window location of this element using a x and y coordinate.
	 * @param xLoc The x coordinate location of this window.
	 * @param yLoc The y coordinate location of this window.
	 * @throws IllegalArgumentException Throws if either the x or y coordinate are negative.
	 */
	private void setLocation(int xLoc, int yLoc) throws IllegalArgumentException
	{
		if (xLoc < 0)
		{
			throw new IllegalArgumentException("Invalid x coordinate location was passed.");
		}
		
		if (yLoc < 0)
		{
			throw new IllegalArgumentException("Invalid y coordinate location was passed.");
		}
		
		this.windowLocation = new Point(xLoc, yLoc);
	}
	
	/**
	 * Updates the window dimensions of this element using a width and height.
	 * @param width The width of the window.
	 * @param height The height of the window.
	 * @throws IllegalArgumentException Throws if either the width or height are <= 0
	 */
	private void setDimensions(int width, int height) throws IllegalArgumentException
	{
		if (width <= 0)
		{
			throw new IllegalArgumentException("Invalid width supplied.");
		}
		
		if (height <= 0)
		{
			throw new IllegalArgumentException("Invalid height supplied.");
		}
		
		this.windowDimensions = new Dimension(width, height);
	}
}
