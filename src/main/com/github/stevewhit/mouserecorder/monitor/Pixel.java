package com.github.stevewhit.mouserecorder.monitor;

import java.util.Objects;

/**
 * Represents a resolution's computer pixel. 
 * @author Steve
 *
 */
public class Pixel
{
	/// TODO: private PixelColor pixelColor.
	
	/**
	 * The 2d coordinate location of the pixel on the resolution.
	 */
	private PixelCoordinate2D coordinate2D;
	
	/**
	 * Constructor that accepts a 2-D coordinate.
	 * @param coordinate2D - The 2-D coordinate of the pixel in the resolution. {@link PixelCoordinate2D}
	 */
	public Pixel(PixelCoordinate2D coordinate2D)
	{
		setCoordinate2D(coordinate2D);
	}
		
	/**
	 * Updates the 2d coordinate for this pixel.
	 * @param coordinate2D - The 2D coordinate for this pixel. {@link PixelCoordinate2D}
	 * @throws IllegalArgumentException if the 2d coordinate is null.
	 */
	private void setCoordinate2D(PixelCoordinate2D coordinate2D) throws IllegalArgumentException
	{
		if (coordinate2D == null)
			throw new IllegalArgumentException("Cannot set a null coordinate value.");
		
		this.coordinate2D = coordinate2D;
	}
	
	/**
	 * Returns the 2d coordinate for this pixel.
	 * @return Returns the 2d coordinate for this pixel.
	 */
	public PixelCoordinate2D getPixelCoordinate2D()
	{
		return this.coordinate2D;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		Pixel  pixel = (Pixel) obj;
		
		return Objects.equals(pixel.getPixelCoordinate2D(), coordinate2D);
	}
	
	@Override
	public String toString()
	{
		return String.format("%1s", coordinate2D.toString());
	}
}
