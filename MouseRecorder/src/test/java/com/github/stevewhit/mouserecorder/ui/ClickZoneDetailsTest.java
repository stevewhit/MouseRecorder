package com.github.stevewhit.mouserecorder.ui;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.Point;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ClickZoneDetailsTest
{
	private ClickZoneDetails details;
	
	@Before
	public void setUp() throws Exception
	{
		details = new ClickZoneDetails(100, 150, 300, 200);
	}
	
	@After
	public void tearDown() throws Exception
	{
		this.details = null;
	}
	
	//===========================================================
	
	@Test(expected = IllegalArgumentException.class)
	public void testClickZoneDetails_InvalidX()
	{
		details = new ClickZoneDetails(-1, 10, 100, 150);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testClickZoneDetails_InvalidY()
	{
		details = new ClickZoneDetails(10, -1, 100, 150);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testClickZoneDetails_InvalidWidth()
	{
		details = new ClickZoneDetails(123, 10, 0, 150);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testClickZoneDetails_InvalidHeight()
	{
		details = new ClickZoneDetails(123, 10, 123, 0);
	}
	
	//===========================================================
	
	@Test
	public void testGetWindowLocation()
	{
		assertTrue(details.getWindowLocation().equals(new Point(100, 150)));
	}

	//===========================================================
	
	@Test
	public void testGetWindowDimensions()
	{
		assertTrue(details.getWindowDimensions().equals(new Dimension(300, 200)));
	}

	//===========================================================
	
}
