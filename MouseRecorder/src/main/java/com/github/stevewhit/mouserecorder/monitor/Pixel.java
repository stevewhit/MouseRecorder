package com.github.stevewhit.mouserecorder.monitor;

/**
 * Represents an individual pixel on any given resolution. Contains a pixel location and color.
 * @author Steve Whitmire (swhit114@gmail.com)
 *
 */
public class Pixel
{
	/**
	 * The color of this pixel.
	 */
	private PixelColor color;
	
	/**
	 * The 2D location of this pixel on the monitor. (0,0) is the top left corner.
	 */
	private PixelCoordinate2D location;
	
	/**
	 * Constructor that accepts a color and location. 
	 * @param color The color of the pixel.
	 * @param location The location of the pixel.
	 * @throws IllegalArgumentException Throws if either the color or location are invalid.
	 */
	public Pixel(PixelColor color, PixelCoordinate2D location) throws IllegalArgumentException
	{
		try
		{
			setColor(color);
			setLocation(location);
		}
		catch (IllegalArgumentException ex)
		{
			this.color = null;
			this.location = null;
			
			throw new IllegalArgumentException("Could not create pixel because ==> " + ex.getMessage());
		}
	}
	
	/**
	 * Returns the pixel color.
	 * @return Returns the pixel color as a PixelColor object.
	 */
	public PixelColor getColor()
	{
		return this.color;
	}
	
	/**
	 * Returns the location of the pixel.
	 * @return Returns the location of the pixel as a PixelCoordinate2D object.
	 */
	public PixelCoordinate2D getLocation()
	{
		return this.location;
	}
	
	/**
	 * Tests the location and color of the pixel to determine if it is valid or not. 
	 * @return Returns true if it is a valid pixel, otherwise false.
	 */
	public boolean isValidPixel()
	{
		// Pixel color is un-restrictive so only test the location for validity.
		return location.isValidCoord();
	}
	
	/**
	 * Returns a formatted string that represents the pixel color and location.
	 * <pre>
	 * Example output: '(12, 234)-->(R123/G152/B235)'
	 * </pre>
	 */
	@Override
	public String toString()
	{
		return String.format("%1$s-->%2$s", location.toString(), color.toString());
	}
	
	/**
	 * Checks for equality between two pixels by comparing color and locations.
	 */
	@Override
	public boolean equals(Object obj)
	{
		// Make sure it isn't null.
		if (obj == null)
		{
			return false;
		}
		
		// Make sure they're relatable by class structure.
		if (!Pixel.class.isAssignableFrom(obj.getClass()))
		{
			return false;
		}
		
		final Pixel castObj = (Pixel) obj;
		
		// Compare colors
		if (!castObj.getColor().equals(this.color))
		{
			return false;
		}
		
		// Compare locations
		if (!castObj.getLocation().equals(this.location))
		{
			return false;
		}
				
		return true;
	}
	
	/**
	 * Updates the color of this pixel.
	 * @param color The color to update this pixel to.
	 * @throws IllegalArgumentException Throws if the color is null.
	 */
	private void setColor(PixelColor color) throws IllegalArgumentException
	{
		if (color == null)
		{
			throw new IllegalArgumentException("Color cannot be null.");
		}
		
		this.color = color;
	}
	
	/**
	 * Updates the location of this pixel.
	 * @param location The location to update this pixel to.
	 * @throws IllegalArgumentException Throws if the location is null.
	 */
	private void setLocation(PixelCoordinate2D location) throws IllegalArgumentException
	{
		if (location == null)
		{
			throw new IllegalArgumentException("Location cannot be null.");
		}
		
		this.location = location;
	}
}
