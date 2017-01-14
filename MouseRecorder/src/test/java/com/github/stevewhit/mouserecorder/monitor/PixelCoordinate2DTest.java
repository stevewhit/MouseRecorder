package com.github.stevewhit.mouserecorder.monitor;

import static org.junit.Assert.*;

import java.awt.Dimension;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PixelCoordinate2DTest
{
	PixelCoordinate2D pixelCoord;
	
	@Before
	public void setUp() throws Exception
	{
		pixelCoord = new PixelCoordinate2D(10, 129, new Dimension(100, 150));
	}
	
	@After
	public void tearDown() throws Exception
	{
		pixelCoord = null;
	}
	
	//================================================================================
	
	@Test (expected=IllegalArgumentException.class)
	public void testPixelCoordinate2DIntInt_NegX()
	{
		pixelCoord = new PixelCoordinate2D(-1, 1);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testPixelCoordinate2DIntInt_NegY()
	{
		pixelCoord = new PixelCoordinate2D(1, -1);
	}
	
	@Test
	public void testPixelCoordinate2DIntInt_ValidTests()
	{
		pixelCoord = new PixelCoordinate2D(0, 0);
		assertTrue(pixelCoord.isValidCoord());
		assertFalse(pixelCoord.isInsideResolution());
		assertNull(pixelCoord.getScreenResolution());
		
		pixelCoord = new PixelCoordinate2D(0, 1);
		assertTrue(pixelCoord.isValidCoord());
		assertFalse(pixelCoord.isInsideResolution());
		assertNull(pixelCoord.getScreenResolution());
		
		pixelCoord = new PixelCoordinate2D(1, 0);
		assertTrue(pixelCoord.isValidCoord());
		assertFalse(pixelCoord.isInsideResolution());
		assertNull(pixelCoord.getScreenResolution());
		
		pixelCoord = new PixelCoordinate2D(1, 1);
		assertTrue(pixelCoord.isValidCoord());
		assertFalse(pixelCoord.isInsideResolution());
		assertNull(pixelCoord.getScreenResolution());
		
		pixelCoord = new PixelCoordinate2D(13241, 123412);
		assertTrue(pixelCoord.isValidCoord());
		assertFalse(pixelCoord.isInsideResolution());
		assertNull(pixelCoord.getScreenResolution());
	}

	//================================================================================
	
	@Test(expected = IllegalArgumentException.class)
	public void testPixelCoordinate2DIntIntDimension_InvalidX()
	{
		pixelCoord = new PixelCoordinate2D(-1, 1, new Dimension(10, 12));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testPixelCoordinate2DIntIntDimension_InvalidY()
	{
		pixelCoord = new PixelCoordinate2D(1, -1, new Dimension(10, 12));
	}
	
	@Test
	public void testPixelCoordinate2DIntIntDimension_ValidTests()
	{
		pixelCoord = new PixelCoordinate2D(0, 0, new Dimension(10, 12));
		assertTrue(pixelCoord.isValidCoord());
		assertTrue(pixelCoord.isInsideResolution());
		assertEquals(new Dimension(10, 12), pixelCoord.getScreenResolution());		
		
		pixelCoord = new PixelCoordinate2D(1, 0, new Dimension(10, 12));
		assertTrue(pixelCoord.isValidCoord());
		assertTrue(pixelCoord.isInsideResolution());
		assertEquals(new Dimension(10, 12), pixelCoord.getScreenResolution());	
		
		pixelCoord = new PixelCoordinate2D(0, 1, new Dimension(10, 12));
		assertTrue(pixelCoord.isValidCoord());
		assertTrue(pixelCoord.isInsideResolution());
		assertEquals(new Dimension(10, 12), pixelCoord.getScreenResolution());	
		
		pixelCoord = new PixelCoordinate2D(1033, 923, new Dimension(1500, 1000));
		assertTrue(pixelCoord.isValidCoord());
		assertTrue(pixelCoord.isInsideResolution());
		assertEquals(new Dimension(1500, 1000), pixelCoord.getScreenResolution());	
	}

	//================================================================================

	@Test
	public void testGetX()
	{
		assertTrue(pixelCoord.getX() == 10);
	}
	
	//================================================================================
	
	@Test
	public void testGetY()
	{
		assertTrue(pixelCoord.getY() == 129);
	}
	
	//================================================================================
	
	@Test
	public void testIsValidCoord()
	{
		assertTrue(pixelCoord.isValidCoord());
	}
	
	//================================================================================
	
	@Test
	public void testIsInsideResolution()
	{
		assertTrue(pixelCoord.isInsideResolution());
		
		pixelCoord = new PixelCoordinate2D(10, 12);
		assertFalse(pixelCoord.isInsideResolution());
	}
	
	//================================================================================
	
	@Test
	public void testEqualsObject()
	{
		PixelCoordinate2D diffCoord = new PixelCoordinate2D(10, 129);
		assertTrue(pixelCoord.equals(diffCoord));
		
		diffCoord = new PixelCoordinate2D(10, 128);
		assertFalse(pixelCoord.equals(diffCoord));
	}
	
	//================================================================================
	
	@Test
	public void testToString()
	{
		assertEquals(pixelCoord.toString(), "(10, 129)");
	}
	
	//================================================================================
	
	@Test
	public void testGetScreenResolution()
	{
		Dimension newDimension = new Dimension(100, 150);
		assertEquals(pixelCoord.getScreenResolution(), newDimension);
		
		newDimension = new Dimension(100, 151);
		assertNotEquals(pixelCoord.getScreenResolution(), newDimension);
	}
	
	//================================================================================
}
