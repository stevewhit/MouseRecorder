package com.github.stevewhit.mouserecorder.monitor;

import static org.junit.Assert.*;

import java.awt.geom.Point2D;

import org.junit.Before;
import org.junit.Test;

public class PixelCoordinate2DTest
{
	PixelCoordinate2D pixelCoord;
	int x;
	int y;
	
	@Before
	public void setup()
	{
		x = -50;
		y = 70;
		
		pixelCoord = new PixelCoordinate2D(x, y);
	}
	
	@Test
	public void testGetX()
	{
		assertTrue(pixelCoord.getX() == x);
	}
	
	@Test
	public void testGetY()
	{
		assertTrue(pixelCoord.getY() == y);
	}
	
	@Test
	public void testSetLocationDoubleDouble()
	{
		PixelCoordinate2D tempPixelCoord = new PixelCoordinate2D(x, y);
		tempPixelCoord.setLocation(y, x);
		
		assertTrue(tempPixelCoord.getX() == y);
		assertTrue(tempPixelCoord.getY() == x);
	}
	
	@Test
	public void testPixelCoordinate2D()
	{
		PixelCoordinate2D tempPixelCoord = new PixelCoordinate2D(x, y);
		assertTrue(tempPixelCoord.getX() == x);
		assertTrue(tempPixelCoord.getY() == y);
	}
	
	@Test
	public void testToString()
	{
		assertEquals(pixelCoord.toString(), "(X=-50, Y=70)");
	}
}
