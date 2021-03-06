package com.github.stevewhit.mouserecorder.monitor;

import java.awt.Color;

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
	 * Constructor that accepts a color.
	 * @param color The color for this object.
	 */
	public PixelColor(Color color)
	{
		if (color == null)
		{
			throw new IllegalArgumentException("Color cannot be null.");
		}
		
		setRGBValue(color.getRGB());
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
	 * Checks for equality of two pixel colors with an allowed error. 
	 * @param colorToCompare The color that is being compared to this.
	 * @return Returns true if the compared color falls within the allowed range to be considered similar. otherwise false.
	 */
	public boolean isSimilarColor(PixelColor colorToCompare)
	{
		// If the values are relatively similar, return true.
		if (Math.abs(colorToCompare.getRedValue() - this.getRedValue()) < 25 &&
			Math.abs(colorToCompare.getGreenValue() - this.getGreenValue()) < 25 &&
			Math.abs(colorToCompare.getBlueValue() - this.getBlueValue()) < 25)
		{
			return true;
		}
		
		return false;
	}

	/**
	 * Updates the RGB Value for this color. 
	 * @param argbValue The Red, Green, and Blue values for this color.
	 */
	private void setRGBValue(int rgbValue)
	{
		// Remove the alpha value.
		this.rgbValue = rgbValue & 0x00FFFFFF;
	}
	
	/**
	 * Returns a formatted string indicating the red, green, and blue components of this color.
	 * <pre>
	 * Example output ==> (R214/G123/B34)
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
