package com.github.stevewhit.mouserecorder.monitor;

/**
 * The RGB Color of any given pixel.
 * @author Steve Whitmire (swhit114@gmail.com)
 *
 */
public class PixelColor
{
	/**
	 * The Red, Green, and Blue value of the color represented as a single integer.
	 */
	private int rgbValue;
	
	/**
	 * Constructor that accepts an Red, Green, Blue integer
	 * @param rgbValue The RBG Value of this color.
	 */
	public PixelColor(int rgbValue)
	{
		setRGBValue(rgbValue);
	}
	
	/**
	 * Returns the Red, Green, and Blue value for this color.
	 * @return Returns the RGB Value for this color as an integer.
	 */
	public int getRGBValue()
	{
		return this.rgbValue;
	}
	
	/**
	 * Returns the Red component of this color.
	 * @return Returns the Red component of this color as an integer.
	 */
	public int getRedValue()
	{
		return (this.rgbValue >> 16) & 0xFF;
	}
	
	/**
	 * Returns the Green component of this color.
	 * @return Returns the Green component of this color as an integer.
	 */
	public int getGreenValue()
	{
		return (this.rgbValue >> 8) & 0xFF;
	}
	
	/**
	 * Returns the Blue component of this color.
	 * @return Returns the Blue component of this color as an integer.
	 */
	public int getBlueValue()
	{
		return this.rgbValue & 0xFF;
	}

	/**
	 * Updates the RGB Value for this color. 
	 * @param argbValue The Red, Green, and Blue values for this color.
	 */
	private void setRGBValue(int rgbValue)
	{
		this.rgbValue = rgbValue;
	}
	
	/**
	 * Returns a formatted string indicating the red, green, and blue components of this color.
	 * <pre>
	 * Example output ==> Red: 0, Green: 233, Blue: 213
	 * </pre>
	 */
	@Override
	public String toString()
	{
		return String.format("(R%1$d/G%2$d/B%3$d)", getRedValue(), getGreenValue(), getBlueValue());
	}
	
	/**
	 * Checks for comparison of two PixelColors. Returns true if they're equal, otherwise false.
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
		if (!PixelColor.class.isAssignableFrom(obj.getClass()))
		{
			return false;
		}
		
		final PixelColor castObj = (PixelColor) obj;
		
		// Compare the RGB value. This includes comparison of the individual Red, Green, and Blue values.
		if (castObj.getRGBValue() != this.getRGBValue())
		{
			return false;
		}
		
		return true;
	}
}
