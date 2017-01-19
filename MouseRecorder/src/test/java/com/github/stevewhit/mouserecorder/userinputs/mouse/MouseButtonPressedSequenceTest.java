package com.github.stevewhit.mouserecorder.userinputs.mouse;

import static org.junit.Assert.*;

import java.net.StandardProtocolFamily;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.stevewhit.mouserecorder.monitor.Pixel;
import com.github.stevewhit.mouserecorder.monitor.PixelColor;
import com.github.stevewhit.mouserecorder.monitor.PixelCoordinate2D;
import com.github.stevewhit.mouserecorder.userinputs.AbstractInputAction;
import com.github.stevewhit.mouserecorder.userinputs.mouse.MouseClick.MouseButton;

public class MouseButtonPressedSequenceTest
{
	
	MouseButtonPressedSequence mouseSequence;
	List<AbstractInputAction> actionSequence;
	
	@Before
	public void setUp() throws Exception
	{
		actionSequence = new ArrayList<AbstractInputAction>();
		
		actionSequence.add(new MouseClick(MouseButton.Left, new Pixel(new PixelColor(11691775), new PixelCoordinate2D(150, 100)), 123321));
		actionSequence.add(new MouseWait(101));
		actionSequence.add(new MouseMove(new Pixel(new PixelColor(11691775), new PixelCoordinate2D(150, 100)), new Pixel(new PixelColor(11691776), new PixelCoordinate2D(150, 250))));
		actionSequence.add(new MouseWait(202));
		actionSequence.add(new MouseMove(new Pixel(new PixelColor(11691776), new PixelCoordinate2D(150, 250)), new Pixel(new PixelColor(11691777), new PixelCoordinate2D(200, 250))));
		actionSequence.add(new MouseWait(303));
		
		mouseSequence = new MouseButtonPressedSequence(actionSequence);
	}
	
	@After
	public void tearDown() throws Exception
	{
		actionSequence = null;
		mouseSequence = null;
	}
	
	//=========================================================================================
	
	@Test
	public void testIsValidAction_DoesntStartWithClick()
	{
		actionSequence.remove(0);
		
		assertFalse(mouseSequence.isValidAction());
	}
		
	@Test
	public void testIsValidAction_EmptySequence()
	{
		actionSequence.removeAll(actionSequence);
		
		assertTrue(actionSequence.size() == 0);
		assertFalse(mouseSequence.isValidAction());
	}
	
	@Test
	public void testIsValidAction_OneActionSequence()
	{
		actionSequence.clear();
		actionSequence.add(new MouseClick(MouseButton.Left, new Pixel(new PixelColor(11691775), new PixelCoordinate2D(150, 100)), 123321));
		
		assertTrue(actionSequence.size() == 1);
		assertFalse(mouseSequence.isValidAction());
	}
	
	@Test
	public void testIsValidAction_NullActionInsideSequence()
	{
		actionSequence.add(2, null);
		
		assertFalse(mouseSequence.isValidAction());
	}
	
	@Test
	public void testIsValidAction_ClickAtEndOfSequence()
	{
		actionSequence.add(new MouseClick(MouseButton.Left, new Pixel(new PixelColor(11691775), new PixelCoordinate2D(150, 100)), 123321));
		
		assertFalse(mouseSequence.isValidAction());
	}
	
	@Test
	public void testIsValidAction_valid()
	{
		assertTrue(mouseSequence.isValidAction());
	}

	//=========================================================================================
	
	@Test
	public void testToString()
	{
		assertEquals("Mouse-Click Sequence: (150, 100)-->(200, 250)", mouseSequence.toString());
	}

	//=========================================================================================
	
	@Test(expected=IllegalArgumentException.class)
	public void testMouseButtonPressedSequence_NullSequence()
	{
		mouseSequence = new MouseButtonPressedSequence(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testMouseButtonPressedSequence_NoItemSequence()
	{
		mouseSequence = new MouseButtonPressedSequence(new ArrayList<AbstractInputAction>());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testMouseButtonPressedSequence_OneItemSequence()
	{
		mouseSequence = new MouseButtonPressedSequence(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testMouseButtonPressedSequence_SequenceDoesntStartWithClick()
	{
		actionSequence.remove(0);
		
		mouseSequence = new MouseButtonPressedSequence(actionSequence);
	}
	
	@Test
	public void testMouseButtonPressedSequence_Valid()
	{
		assertTrue(mouseSequence.isValidAction());
		assertEquals(mouseSequence.getActionSequence(), actionSequence);
		assertNotNull(mouseSequence.getStartingLocation());
		assertNotNull(mouseSequence.getEndingLocation());
	}

	//=========================================================================================
	
	@Test
	public void testGetActionSequence()
	{
		assertEquals(mouseSequence.getActionSequence(), actionSequence);
	}

	//=========================================================================================
	
	@Test
	public void testGetStartingLocation()
	{
		PixelCoordinate2D startLoc = new PixelCoordinate2D(150, 100);
		
		assertTrue(mouseSequence.getStartingLocation().equals(startLoc));
	}

	//=========================================================================================
	
	@Test
	public void testGetEndingLocation()
	{
		PixelCoordinate2D endLoc = new PixelCoordinate2D(200, 250);
		
		assertTrue(mouseSequence.getEndingLocation().equals(endLoc));
	}

	//=========================================================================================
	
}
