package com.github.stevewhit.mouserecorder.monitor;

import static org.junit.Assert.*;

import java.awt.geom.Point2D;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
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
	public void testPixelCoordinate2D()
	{
		PixelCoordinate2D tempPixelCoord = new PixelCoordinate2D(x, y);
		assertTrue(tempPixelCoord.getX() == x);
		assertTrue(tempPixelCoord.getY() == y);
	}
	
	@Test
	public void testToString()
	{
		assertEquals(pixelCoord.toString(), "(-50,70)");
	}
	
	@Test
	public void testEqualsSelf()
	{
		PixelCoordinate2D pixelCoordMatch = pixelCoord;
		
		assertTrue(pixelCoord.equals(pixelCoordMatch));
	}
	
	@Test
	public void testEqualsNullObj()
	{
		assertFalse(pixelCoord.equals(null));
	}
	
	@Test
	public void testEqualsDifClass()
	{
		assertFalse(pixelCoord.equals(new String("NotEqual")));
	}
	
	@Test
	public void testEqualsDifVariables()
	{
		PixelCoordinate2D pixelCoordDif = new PixelCoordinate2D(-50, 71);
		assertFalse(pixelCoord.equals(pixelCoordDif));
	}
	
	@Test
	public void testEqualsSameVariables()
	{
		PixelCoordinate2D pixelCoordDif = new PixelCoordinate2D(-50, 70);
		assertTrue(pixelCoord.equals(pixelCoordDif));
	}
}
































