package com.github.stevewhit.mouserecorder.monitor;

import java.awt.Dimension;

/**
 * Used to represent the 2D coordinate of any given pixel in a monitor's screen. Note, (0,0) starts in the top left corner.
 * @author Steve Whitmire (swhit114@gmail.com)
 *
 */
public final class PixelCoordinate2D
{
	/**
	 * The 2-D X coordinate of the pixel.
	 * Note, (0,0) starts in the top-left corner.
	 */
	private int x;
	
	/**
	 * The 2-D Y coordinate of the pixel.
	 * Note, (0,0) starts in the top-left corner.
	 */
	private int y;
			
	/**
	 * Constructor used to set the X and Y values of the Pixel.
	 * @param x The X value of the pixel.
	 * @param y The Y value of the pixel.
	 * @throws IllegalArgumentException Throws if either the X or Y values are less than 0.
	 */
	protected PixelCoordinate2D(int x, int y) throws IllegalArgumentException
	{
		try
		{
			setX(x);
			setY(y);
		}
		catch(IllegalArgumentException ex)
		{
			this.x = 0;
			this.y = 0;
			
			throw new IllegalArgumentException("PixelCoordinate2D could not be created ==> " + ex.getMessage());
		}
	}
	
	/**
	 * Returns the X value of the pixel coordinate.
	 * @return Returns the X value of the pixel coordinate as an int.
	 */
	public int getX()
	{
		return this.x;
	}
	
	/**
	 * Returns the Y value of the pixel coordinate.
	 * @return Returns the Y value of the pixel coordinate as an int.
	 */
    public int getY()
	{
		return this.y;
	}
    
    /**
     * Verifies the coordinate by identifying that both X and Y values are >= 0.
     * @return Returns true of the coordinate is valid. Otherwise, false.
     */
    public boolean isValidCoord()
    {
    	// Check X, Y values are >= 0
    	return  (y >= 0) && (x >= 0);
    }
    
    /**
     * Checks to see if this coordinate is inside the given screen dimensions.
     * @return Returns true if the pixel coordinate is inside the dimensions, otherwise false;
     */
    public boolean isInsideScreenDimensions(Dimension screenDimensions)
    {
    	if (screenDimensions == null)
    	{
    		throw new IllegalArgumentException("Screen dimensions cannot be null.");
    	}
    	
    	return (y >= 0 && y <= screenDimensions.getHeight()) &&
    		   (x >= 0 && x <= screenDimensions.getWidth());
    }
        
    /**
     * Checks for equality between two pixel coordinates by comparing X and Y values. 
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
		if (!PixelCoordinate2D.class.isAssignableFrom(obj.getClass()))
		{
			return false;
		}
		
		final PixelCoordinate2D castObj = (PixelCoordinate2D) obj;
		
		// Compare the X values
		if (castObj.getX() != this.getX())
		{
			return false;
		}
		
		// Compare the Y values
		if (castObj.getY() != this.getY())
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * Returns the pixel coordinate as a formatted string.
	 * <pre>
	 * Example output for coordinate X=5 and Y=129 ==> (5, 129)
	 * </pre>
	 */
	@Override
	public String toString()
	{
		return String.format("(%1$d, %2$d)", x, y);
	}

	/**
	 * Updates the X coordinate to whatever value is passed.
	 * @param x The value of the X coordinate to store. Must be >= 0.
	 * @throws IllegalArgumentException Throws if X < 0.
	 */
	private void setX(int x) throws IllegalArgumentException
	{
		if (x < 0)
		{
			throw new IllegalArgumentException("'X' coordinates must be >= 0.");
		}
		
		this.x = x;
	}
	
	/**
	 * Updates the Y coordinate to whatever value is passed.
	 * @param y The value of the Y coordinate to store. Must be >= 0.
	 * @throws IllegalArgumentException Throws if Y < 0.
	 */
	private void setY(int y) throws IllegalArgumentException
	{
		if (y < 0)
		{
			throw new IllegalArgumentException("'Y' Coordinates must be >= 0.");
		}
		
		this.y = y;
	}
}
