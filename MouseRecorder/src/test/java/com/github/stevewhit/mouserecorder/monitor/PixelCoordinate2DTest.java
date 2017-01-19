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
		pixelCoord = new PixelCoordinate2D(10, 129);
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
		assertTrue(pixelCoord.isInsideScreenDimensions(new Dimension(100, 150)));
		
		pixelCoord = new PixelCoordinate2D(0, 1);
		assertTrue(pixelCoord.isValidCoord());
		assertTrue(pixelCoord.isInsideScreenDimensions(new Dimension(100, 150)));
		
		pixelCoord = new PixelCoordinate2D(1, 0);
		assertTrue(pixelCoord.isValidCoord());
		assertTrue(pixelCoord.isInsideScreenDimensions(new Dimension(100, 150)));
		
		pixelCoord = new PixelCoordinate2D(1, 1);
		assertTrue(pixelCoord.isValidCoord());
		assertTrue(pixelCoord.isInsideScreenDimensions(new Dimension(100, 150)));
		
		pixelCoord = new PixelCoordinate2D(13241, 123412);
		assertTrue(pixelCoord.isValidCoord());
		assertFalse(pixelCoord.isInsideScreenDimensions(new Dimension(100, 150)));
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
	public void testIsInsideResolution_AllValid()
	{
		// 10, 129
		assertTrue(pixelCoord.isInsideScreenDimensions(new Dimension(10, 1000)));
		assertTrue(pixelCoord.isInsideScreenDimensions(new Dimension(100, 129)));
		assertTrue(pixelCoord.isInsideScreenDimensions(new Dimension(10, 129)));
		assertTrue(pixelCoord.isInsideScreenDimensions(new Dimension(1000, 1000)));
		
		assertFalse(pixelCoord.isInsideScreenDimensions(new Dimension(9, 1000)));
		assertFalse(pixelCoord.isInsideScreenDimensions(new Dimension(100, 128)));
		assertFalse(pixelCoord.isInsideScreenDimensions(new Dimension(9, 128)));
		
		assertFalse(pixelCoord.isInsideScreenDimensions(new Dimension(2, 2)));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testIsInsideResolution_nullDimension()
	{
		pixelCoord.isInsideScreenDimensions(null);
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
}
