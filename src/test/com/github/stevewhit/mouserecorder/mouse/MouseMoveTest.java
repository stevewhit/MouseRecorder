package com.github.stevewhit.mouserecorder.mouse;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.github.stevewhit.mouserecorder.monitor.Pixel;
import com.github.stevewhit.mouserecorder.monitor.PixelCoordinate2D;

public class MouseMoveTest
{
	MouseMove moveValid;
	List<Pixel> mouseMovements;
	
	@Before
	public void setUp() throws Exception
	{
		mouseMovements = new ArrayList<Pixel>();
		mouseMovements.add(new Pixel(new PixelCoordinate2D(10,15)));
		mouseMovements.add(new Pixel(new PixelCoordinate2D(11,15)));
		mouseMovements.add(new Pixel(new PixelCoordinate2D(12,15)));
		mouseMovements.add(new Pixel(new PixelCoordinate2D(13,16)));
		
		moveValid = new MouseMove(mouseMovements);
	}
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Test
	public void testToString()
	{
		assertEquals(moveValid.toString(), "Moving: (X=10, Y=15) --> (X=13, Y=16)");
	}
	
	@Test
	public void testMouseMoveListOfPixelNull()
	{
		expectedException.expect(IllegalArgumentException.class);
		MouseMove invalidMM = new MouseMove(null);
	}
	
	@Test
	public void testMouseMoveListOfPixelOne()
	{
		expectedException.expect(IllegalArgumentException.class);
		MouseMove invalidMM = new MouseMove(mouseMovements.subList(0, 0));
	}
	
	@Test
	public void testMouseMoveListOfPixelValid()
	{
		MouseMove validMM = new MouseMove(mouseMovements);
		
		assertTrue(validMM.getActionId() != null);
		assertEquals(validMM.getMousePath(), mouseMovements);
	}
	
	@Test
	public void testMouseMovePixelPixelNull()
	{
		expectedException.expect(IllegalArgumentException.class);
		MouseMove invalidMM = new MouseMove(new Pixel(new PixelCoordinate2D(10,15)), null);
	}
	
	@Test
	public void testMouseMovePixelPixelTooFar()
	{
		expectedException.expect(IllegalArgumentException.class);
		MouseMove invalidMM = new MouseMove(new Pixel(new PixelCoordinate2D(10,15)), new Pixel(new PixelCoordinate2D(10,17)));
	}
	
	@Test
	public void testGetMousePath()
	{
		assertEquals(moveValid.getMousePath(), mouseMovements);
	}
	
	@Test
	public void testGetStartingPoint()
	{
		assertEquals(moveValid.getStartingPoint(), mouseMovements.get(0));
	}
	
	@Test
	public void testGetEndingPoint()
	{
		int lastIndex = mouseMovements.size() - 1;
		assertEquals(moveValid.getEndingPoint(), mouseMovements.get(lastIndex));
	}
	
}
