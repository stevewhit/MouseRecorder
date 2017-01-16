package com.github.stevewhit.mouserecorder.monitor;

import static org.junit.Assert.*;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ScreenUtilsTest
{
	BufferedImage capturedImage;
	Rectangle captureRect;
	File fileLoc;
	
	@Before
	public void setUp() throws Exception
	{
		capturedImage = ScreenUtils.getScreenCapture();
		captureRect = new Rectangle(100, 150, 150, 250);
		fileLoc = new File("TestFileLocation.png");
	}
	
	@After
	public void tearDown() throws Exception
	{
		capturedImage = null;
		captureRect = null;
		
		if (fileLoc != null && fileLoc.exists())
			fileLoc.delete();
		
		fileLoc = null;
	}
	
	@Test
	public void testGetScreenDimensions()
	{
		assertEquals(ScreenUtils.getScreenDimensions(), Toolkit.getDefaultToolkit().getScreenSize());
	}
	
	@Test
	public void testGetScreenRectangle()
	{
		assertEquals(ScreenUtils.getScreenRectangle(), new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
	}
	
	@Test
	public void testGetScreenCapture() throws AWTException
	{
		assertTrue(capturedImage.getWidth() == Toolkit.getDefaultToolkit().getScreenSize().getWidth());
		assertTrue(capturedImage.getHeight() == Toolkit.getDefaultToolkit().getScreenSize().getHeight());
	}

	//========================================================================================

	@Test
	public void testGetScreenCaptureRectangle_TestProperDimensions() throws AWTException
	{
		capturedImage = ScreenUtils.getScreenCapture(captureRect);
		
		assertTrue(capturedImage.getWidth() == 150);
		assertTrue(capturedImage.getHeight() == 250);
	}
	
	@Test
	public void testGetScreenCaptureRectangle_TestBoundsOfRectangle() throws AWTException
	{
		BufferedImage entireCapture = ScreenUtils.getScreenCapture();
		capturedImage = ScreenUtils.getScreenCapture(captureRect);
		
		// Test that the bounds of the rectangle image are properly caught.
		assertTrue(entireCapture.getRGB(100, 150) == capturedImage.getRGB(0, 0));
		assertTrue(entireCapture.getRGB(250, 400) == capturedImage.getRGB(149, 249));
		
		entireCapture = null;
	}
	
	//========================================================================================
	
	@Test(expected=IllegalArgumentException.class)
	public void testSaveScreenCapturePNGFile_NullFile() throws IllegalArgumentException, IOException, AWTException
	{
		ScreenUtils.saveScreenCapturePNG(null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSaveScreenCapturePNGFile_NotProperExtension() throws IllegalArgumentException, IOException, AWTException
	{
		fileLoc = new File("TestImageFile.txt");
		
		ScreenUtils.saveScreenCapturePNG(fileLoc);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSaveScreenCapturePNGFile_NotProperFileName() throws IllegalArgumentException, IOException, AWTException
	{
		fileLoc = new File(".png");
		
		ScreenUtils.saveScreenCapturePNG(fileLoc);
	}
	
	@Test
	public void testSaveScreenCapturePNGFile_valid() throws IllegalArgumentException, IOException, AWTException
	{
		fileLoc = new File("TestImageFile.PnG");
		
		ScreenUtils.saveScreenCapturePNG(fileLoc);
		
		assertTrue(fileLoc.exists());
	}
	
	//========================================================================================
	
	@Test(expected=IllegalArgumentException.class)
	public void testSaveScreenCapturePNGFileRectangle_nullFile() throws IllegalArgumentException, IOException, AWTException
	{
		ScreenUtils.saveScreenCapturePNG(null, captureRect);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSaveScreenCapturePNGFileRectangle_NotProperExtension() throws IllegalArgumentException, IOException, AWTException
	{
		fileLoc = new File("TestImageFile.txt");
		
		ScreenUtils.saveScreenCapturePNG(fileLoc, captureRect);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSaveScreenCapturePNGFileRectangle_InvalidFileName() throws IllegalArgumentException, IOException, AWTException
	{
		fileLoc = new File(".png");
		
		ScreenUtils.saveScreenCapturePNG(fileLoc, captureRect);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSaveScreenCapturePNGFileRectangle_NullRect() throws IllegalArgumentException, IOException, AWTException
	{
		ScreenUtils.saveScreenCapturePNG(fileLoc, new Rectangle(0, 0, -1, -1));
	}
	
	@Test
	public void testSaveScreenCapturePNGFileRectangle_Valid() throws IllegalArgumentException, IOException, AWTException
	{
		ScreenUtils.saveScreenCapturePNG(fileLoc, captureRect);
		assertTrue(fileLoc.exists());
	}
	
	//========================================================================================
	
	@Test(expected=IllegalArgumentException.class)
	public void testSaveScreenCapturePNGFileDimension_NullFile() throws IllegalArgumentException, IOException
	{
		ScreenUtils.saveScreenCapturePNG(null, capturedImage);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSaveScreenCapturePNGFileDimension_NotProperExtension() throws IllegalArgumentException, IOException
	{
		fileLoc = new File("TestImageFile.txt");
		
		ScreenUtils.saveScreenCapturePNG(fileLoc, capturedImage);
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public void testSaveScreenCapturePNGFileDimension_InvalidFileName() throws IllegalArgumentException, IOException
	{
		fileLoc = new File(".png");
		
		ScreenUtils.saveScreenCapturePNG(fileLoc, capturedImage);
	}
	
	@Test
	public void testSaveScreenCapturePNGFileDimension_Valid() throws IllegalArgumentException, IOException
	{
		ScreenUtils.saveScreenCapturePNG(fileLoc, capturedImage);
		assertTrue(fileLoc.exists());
	}
	
	//========================================================================================
}
