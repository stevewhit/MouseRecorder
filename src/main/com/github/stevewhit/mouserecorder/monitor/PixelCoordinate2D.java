package com.github.stevewhit.mouserecorder.monitor;

import java.util.Objects;

public class PixelCoordinate2D
{
	private int x;
	private int y;
	
	/**
	 * Default constructor that accepts a (X,Y) location.
	 * @param x - the X coordinate of this coordinate2D
	 * @param y - the Y coordinate of this coordinate2D
	 */
	public PixelCoordinate2D(int x, int y)
	{
		setLocation(x,y);
	}
	
	/**
	 * Returns the X point of the coordinate
	 * @return Returns the X point of the coordinate.
	 */
	public int getX()
	{
		return this.x;
	}

	/**
	 * Returns the Y point of the coordinate 
	 * @return Returns the Y point of the coordinate.
	 */
	public int getY()
	{
		return this.y;
	}

	/**
	 * Sets the location of this Coordinate2D to the specified coordinates.
	 * @param x - the new X coordinate of this coordinate2D
	 * @param y - the new Y coordinate of this coordinate2D
	 */
	private void setLocation(int x, int y)
	{
		this.x = x;
		this.y = y;
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
		
		PixelCoordinate2D pixelCoord = (PixelCoordinate2D) obj;
		
		return Objects.equals(x, pixelCoord.x) && Objects.equals(y, pixelCoord.y);
	}
	
	@Override
	public String toString()
	{
		return String.format("(%1d,%2d)", x, y);
	}
}
