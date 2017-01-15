package com.github.stevewhit.mouserecorder.monitor;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PixelColorTest
{
	PixelColor color;
	
	@Before
	public void setUp() throws Exception
	{
		color = new PixelColor(11691775); // HEX: #B266FF
	}
	
	@After
	public void tearDown() throws Exception
	{
		color = null;
	}
	
	//====================================================================
	
	@Test
	public void testPixelColor()
	{
		assertTrue(color.getRGBValue() == 11691775);
	}
	
	@Test
	public void testGetRGBValue()
	{
		assertTrue(color.getRGBValue() == 11691775);
	}
	
	@Test
	public void testGetRedValue()
	{
		assertTrue(color.getRedValue() == 178);
	}
	
	@Test
	public void testGetGreenValue()
	{
		assertTrue(color.getGreenValue() == 102);
	}
	
	@Test
	public void testGetBlueValue()
	{
		assertTrue(color.getBlueValue() == 255);
	}
	
	@Test
	public void testToString()
	{
		assertEquals(color.toString(), "Red: 178, Green: 102, Blue: 255");
	}
	
	@Test
	public void testEqualsObject()
	{
		assertFalse(color.equals(null));
		assertFalse(color.equals(new String("shouldn't equal")));
		assertFalse(color.equals(new PixelColor(11691774)));
		assertTrue(color.equals(new PixelColor(11691775)));
	}
	
}
