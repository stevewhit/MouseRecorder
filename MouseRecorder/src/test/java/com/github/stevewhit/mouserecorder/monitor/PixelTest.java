package com.github.stevewhit.mouserecorder.monitor;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PixelTest
{
	Pixel pixel;
	
	@Before
	public void setUp() throws Exception
	{
		pixel = new Pixel(new PixelColor(11691775), new PixelCoordinate2D(150, 100));
	}
	
	@After
	public void tearDown() throws Exception
	{
		pixel = null;
	}
	
	//===================================================================================
	
	@Test(expected=IllegalArgumentException.class)
	public void testPixel_nullColor()
	{
		pixel = new Pixel(null, new PixelCoordinate2D(150, 100));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testPixel_nullLocation()
	{
		pixel = new Pixel(new PixelColor(11691775), null);
	}
	
	@Test
	public void testPixel_valid()
	{
		assertTrue(pixel.getColor().equals(new PixelColor(11691775)));
		assertTrue(pixel.getLocation().equals(new PixelCoordinate2D(150, 100)));
	}
	
	//===================================================================================
	
	@Test
	public void testGetColor()
	{
		assertTrue(pixel.getColor().equals(new PixelColor(11691775)));
	}
	
	@Test
	public void testGetLocation()
	{
		assertTrue(pixel.getLocation().equals(new PixelCoordinate2D(150, 100)));
	}
	
	@Test
	public void testIsValidPixel()
	{
		assertTrue(pixel.isValidPixel());
		
		// Cannot test an invalid pixel because PixelCoord2D is limited to only creating valid coordinates.
	}
	
	@Test
	public void testToString()
	{
		assertTrue(pixel.toString().equals("(150, 100)-->(R178/G102/B255)"));
		assertFalse(pixel.toString().equals("(150, 101)-->(R178/G102/B255)"));
	}
	
	@Test
	public void testEqualsObject()
	{
		assertTrue(pixel.equals(new Pixel(new PixelColor(11691775), new PixelCoordinate2D(150, 100))));
		assertFalse(pixel.equals(null));
		assertFalse(pixel.equals(new Pixel(new PixelColor(11691775), new PixelCoordinate2D(150, 101))));
		assertFalse(pixel.equals(new Pixel(new PixelColor(11691775), new PixelCoordinate2D(151, 100))));
		assertFalse(pixel.equals(new Pixel(new PixelColor(11691774), new PixelCoordinate2D(150, 100))));
		assertFalse(pixel.equals(new PixelCoordinate2D(150, 100)));
	}
	
}
