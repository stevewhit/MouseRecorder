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
import com.github.stevewhit.mouserecorder.mouse.MouseClick.MouseButton;

public class MouseDragTest
{
	MouseDrag dragValid;
	MouseMove moveValid;
	MouseClick clickValid;
	
	@Before
	public void setUp() throws Exception
	{
		List<Pixel> mouseMovements = new ArrayList<Pixel>();
		mouseMovements.add(new Pixel(new PixelCoordinate2D(10,15)));
		mouseMovements.add(new Pixel(new PixelCoordinate2D(11,15)));
		mouseMovements.add(new Pixel(new PixelCoordinate2D(12,15)));
		mouseMovements.add(new Pixel(new PixelCoordinate2D(13,16)));
		
		moveValid = new MouseMove(mouseMovements);
		clickValid = new MouseClick(MouseButton.Left, new Pixel(new PixelCoordinate2D(10, 15)), 15);

		dragValid = new MouseDrag(clickValid, moveValid);
	}
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Test
	public void testToString()
	{
		assertEquals(dragValid.toString(), "Drag: (10,15) --> (13,16)");
	}
	
	@Test
	public void testMouseDragActionId()
	{
		assertTrue(dragValid.getActionId() != null);
	}
	
	@Test
	public void testMouseDragNotValidClick()
	{
		expectedException.expect(IllegalArgumentException.class);
		
		MouseDrag dragInvalid = new MouseDrag(null, moveValid);
	}
	
	@Test
	public void testMouseDragNotValidMove()
	{
		expectedException.expect(IllegalArgumentException.class);
		
		MouseDrag dragInvalid = new MouseDrag(clickValid, null);
	}
	
	@Test
	public void testMouseDragNotValidDrag()
	{
		expectedException.expect(IllegalArgumentException.class);
		
		MouseClick clickDifStart = new MouseClick(MouseButton.Left, new Pixel(new PixelCoordinate2D(12, 15)), 15);
		MouseDrag dragInvalid = new MouseDrag(clickDifStart, moveValid);
	}
	
	@Test
	public void testMouseDragValid()
	{
		assertEquals(dragValid.getMouseClick(), clickValid);
		assertEquals(dragValid.getMouseMove(), moveValid);
	}
	
	@Test
	public void testGetMouseClick()
	{
		assertEquals(dragValid.getMouseClick(), clickValid);
	}
	
	@Test
	public void testGetMouseMove()
	{
		assertEquals(dragValid.getMouseMove(), moveValid);
	}
}
