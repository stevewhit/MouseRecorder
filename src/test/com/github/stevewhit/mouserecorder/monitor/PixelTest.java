package com.github.stevewhit.mouserecorder.monitor;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PixelTest
{
	PixelCoordinate2D coordinate;
	Pixel pixelValid;

	@Before
	public void setup()
	{
		coordinate = new PixelCoordinate2D(-50, 35);
		pixelValid = new Pixel(coordinate);
	}
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Test
	public void testPixelSetupCoordinate2D()
	{
		assertEquals(pixelValid.getPixelCoordinate2D(), coordinate);
	}
	
	@Test
	public void testSetCoordinate2DValid()
	{
		Pixel tempPixel = new Pixel(new PixelCoordinate2D(10, 20));
		tempPixel.setCoordinate2D(coordinate);
		
		assertEquals(tempPixel.getPixelCoordinate2D(), coordinate);
	}
	
	@Test
	public void testSetCoordinate2DNull()
	{
		expectedException.expect(IllegalArgumentException.class);
		Pixel tempPixel = new Pixel(new PixelCoordinate2D(10, 20));
		
		tempPixel.setCoordinate2D(null);
	}
	
	@Test
	public void testGetPixelCoordinate2D()
	{
		assertEquals(pixelValid.getPixelCoordinate2D(), coordinate);
	}
	
	@Test
	public void testToString()
	{
		assertEquals(pixelValid.toString(), "(X=-50, Y=35)");
	}
}
