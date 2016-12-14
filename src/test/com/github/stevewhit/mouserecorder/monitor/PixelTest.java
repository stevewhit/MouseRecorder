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
	public void testGetPixelCoordinate2D()
	{
		assertEquals(pixelValid.getPixelCoordinate2D(), coordinate);
	}
	
	@Test
	public void testToString()
	{
		assertEquals(pixelValid.toString(), "(-50,35)");
	}
	
	@Test
	public void testEqualsSelf()
	{
		Pixel pixelMatch = pixelValid;
		
		assertTrue(pixelValid.equals(pixelMatch));
	}
	
	@Test
	public void testEqualsNullObj()
	{
		assertFalse(pixelValid.equals(null));
	}
	
	@Test
	public void testEqualsDifClass()
	{
		assertFalse(pixelValid.equals(new String("NotEqual")));
	}
	
	@Test
	public void testEqualsDifVariables()
	{
		Pixel pixelDif = new Pixel(new PixelCoordinate2D(-50, 36));
		
		assertFalse(pixelValid.equals(pixelDif));
	}
	
	@Test
	public void testEqualsSameVariables()
	{
		Pixel pixelSame =new Pixel( new PixelCoordinate2D(-50, 35));
		assertTrue(pixelValid.equals(pixelSame));
	}
}
