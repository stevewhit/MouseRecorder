package com.github.stevewhit.mouserecorder.monitor;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ScreenUtils
{
	/**
	 * Private constructor to utilize ScreenUtils as a static class.
	 */
	private ScreenUtils(){}
	
	/**
	 * Returns the active screen dimensions.
	 * @return Returns the screen dimensions as a Dimension.
	 */
	public static Dimension getScreenDimensions()
	{
		return Toolkit.getDefaultToolkit().getScreenSize();
	}
	
	/**
	 * Returns the active screen dimensions as a Rectangle.
	 * @return Returns the active screen dimensions as a rectangle.
	 */
	public static Rectangle getScreenRectangle()
	{
		return new Rectangle(getScreenDimensions());
	}
	
	/**
	 * Returns a buffered image of the entire screen.
	 * @return Returns an image of the entire screen as a buffered image.
	 * @throws AWTException Throws if an invalid geometry is used for the screen capture.
	 */
	public static BufferedImage getScreenCapture() throws AWTException
	{
		return getScreenCapture(getScreenRectangle());
	}
	
	/**
	 * Returns a buffered image of the screen with the given rectangle dimensions.
	 * @param captureRectangle The rectangle dimensions of the screen capture.
	 * @return Returns an image of the screen rectangle as a buffered image.
	 * @throws AWTException Throws if an invalid geometry is used for the screen capture.
	 */
	public static BufferedImage getScreenCapture(Rectangle captureRectangle) throws AWTException
	{
		// Verify capture rectangle is inside the screen's dimensions.
		if (captureRectangle == null || !getScreenRectangle().contains(captureRectangle))
		{
			throw new IllegalArgumentException("CaptureRectangle cannot be null and it must be inside the screen's dimensions.");
		}
		
		return (new Robot()).createScreenCapture(captureRectangle);
	}
	
	/**
	 * Saves an image of the screen capture as a PNG to the desired location. PNG format is used to preserve absolute pixel information for a higher-quality image details.
	 * @param fileLocation The location of where to save the image. Must contain the '.png' extension.
	 * @Throws IllegalArgumentException Throws if the filelocation that was supplied was null/empty or if the filepath doesn't end in .png
	 * @throws IOException Throws if unable to save to desired file location.
	 * @throws AWTException Throws if an invalid geometry is used for the screen capture.
	 */
	public static void saveScreenCapturePNG(File fileLocation) throws IllegalArgumentException, IOException, AWTException
	{
		saveScreenCapturePNG(fileLocation, getScreenRectangle());
	}
	
	/**
	 * Saves an image of the screen capture with the supplied dimensions as a PNG to the desired file location. 
	 * PNG format is used to preserve absolute pixel information for a higher-quality image details.
	 * @param fileLocation The location of where to save the image. Must contain the '.png' extension.
	 * @param captureRectangle The rectangle dimensions of the screen capture.
	 * @Throws IllegalArgumentException Throws if the filelocation that was supplied was null/empty or if the filepath doesn't end in .png
	 * @throws IOException Throws if unable to save to desired file location.
	 * @throws AWTException Throws if an invalid geometry is used for the screen capture.
	 */
	public static void saveScreenCapturePNG(File fileLocation, Rectangle captureRectangle) throws IllegalArgumentException, IOException, AWTException
	{
		verifyPNGFilePath(fileLocation);
		
		if (captureRectangle == null)
		{
			throw new IllegalArgumentException("CaptureRectangle cannot be null.");
		}
		
		try
		{
			ImageIO.write(getScreenCapture(captureRectangle), "PNG", fileLocation);
		}
		catch(AWTException ex)
		{
			throw new AWTException("Invalid rectangle geometry used for screen capture: " + ex.getMessage());
		}
		catch(IOException ex)
		{
			throw new IOException("Could not locate/write to desired file location because: " + ex.getMessage());
		}
	}

	/**
	 * Saves the desired image as a PNG to the desired file location.
	 * PNG format is used to preserve absolute pixel information for a higher-quality image details.
	 * @param fileLocation The location of where to save the image. Must contain the '.png' extension.
	 * @param imageToSave The bufferedImage that is being saved.
	 * @throws IllegalArgumentException Throws if the filelocation that was supplied was null/empty or if the filepath doesn't end in .png
	 * @throws IOException Throws if unable to save to desired file location.
	 */
	public static void saveScreenCapturePNG(File fileLocation, BufferedImage imageToSave) throws IllegalArgumentException, IOException
	{
		verifyPNGFilePath(fileLocation);
		
		if (imageToSave == null)
		{
			throw new IllegalArgumentException("ImageToSave cannot be null.");
		}
		
		try
		{
			ImageIO.write(imageToSave, "PNG", fileLocation);
		}
		catch(IOException ex)
		{
			throw new IOException("Could not locate/write to desired file location because: " + ex.getMessage());
		}
	}
	
	/**
	 * Verifies the file location contains the proper naming convensions for a typical .png file.
	 * @param fileLocation The location that is being tested for validity.
	 * @throws IllegalArgumentException Throws if the filelocation that was supplied was null/empty or if the filepath doesn't end in .png
	 */
	private static void verifyPNGFilePath(File fileLocation) throws IllegalArgumentException
	{
		if (fileLocation == null || fileLocation.getPath() == null)
		{
			throw new IllegalArgumentException("FileLocation and path cannot be null.");
		}
		
		String filePath = fileLocation.getPath();
		
		//Verify the file has a proper name before extension
		if (filePath.length() <= 4)
		{
			throw new IllegalArgumentException("File path location must contain a name + .png extension");
		}
		
		// Verify filepath ends in '.png'
		if (!filePath.substring(filePath.length() - 4, filePath.length()).toLowerCase().equals(".png"))
		{
			throw new IllegalArgumentException("File path location must end in .png");
		}
	}
}
