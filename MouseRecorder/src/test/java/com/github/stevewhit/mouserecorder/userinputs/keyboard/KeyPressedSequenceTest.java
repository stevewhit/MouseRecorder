package com.github.stevewhit.mouserecorder.userinputs.keyboard;

import static org.junit.Assert.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.github.stevewhit.mouserecorder.monitor.Pixel;
import com.github.stevewhit.mouserecorder.monitor.PixelColor;
import com.github.stevewhit.mouserecorder.monitor.PixelCoordinate2D;
import com.github.stevewhit.mouserecorder.userinputs.AbstractInputAction;
import com.github.stevewhit.mouserecorder.userinputs.mouse.MouseClick;
import com.github.stevewhit.mouserecorder.userinputs.mouse.MouseMove;
import com.github.stevewhit.mouserecorder.userinputs.mouse.MouseWait;
import com.github.stevewhit.mouserecorder.userinputs.mouse.MouseClick.MouseButton;

public class KeyPressedSequenceTest
{
	KeyPressedSequence keySequence;
	List<AbstractInputAction> actionSequence;
	
	@Before
	public void setUp() throws Exception
	{
		actionSequence = new ArrayList<AbstractInputAction>();
		
		actionSequence.add(new KeyboardKeyPress(KeyEvent.VK_CONTROL, 1002));
		actionSequence.add(new MouseClick(MouseButton.Left, new Pixel(new PixelColor(11691775), new PixelCoordinate2D(150, 100)), 123321));
		actionSequence.add(new MouseWait(101));
		actionSequence.add(new MouseMove(new Pixel(new PixelColor(11691775), new PixelCoordinate2D(150, 100)), new Pixel(new PixelColor(11691776), new PixelCoordinate2D(150, 250))));
		actionSequence.add(new MouseWait(202));
		actionSequence.add(new MouseMove(new Pixel(new PixelColor(11691776), new PixelCoordinate2D(150, 250)), new Pixel(new PixelColor(11691777), new PixelCoordinate2D(200, 250))));
		actionSequence.add(new MouseWait(303));
		actionSequence.add(new MouseClick(MouseButton.Left, new Pixel(new PixelColor(11691775), new PixelCoordinate2D(150, 100)), 123321));
		actionSequence.add(new MouseWait(101));
		actionSequence.add(new MouseClick(MouseButton.Left, new Pixel(new PixelColor(11691775), new PixelCoordinate2D(150, 100)), 123321));
		actionSequence.add(new MouseWait(101));
		actionSequence.add(new MouseClick(MouseButton.Left, new Pixel(new PixelColor(11691775), new PixelCoordinate2D(150, 100)), 123321));
		actionSequence.add(new MouseWait(101));
		actionSequence.add(new MouseClick(MouseButton.Left, new Pixel(new PixelColor(11691775), new PixelCoordinate2D(150, 100)), 123321));
		actionSequence.add(new MouseWait(101));
		actionSequence.add(new KeyboardKeyPress(KeyEvent.VK_0, 103));
		
		keySequence = new KeyPressedSequence(actionSequence);
	}
	
	@After
	public void tearDown() throws Exception
	{
		keySequence = null;
		actionSequence = null;
	}
	
	//========================================================
	
	@Test
	public void testIsValidAction_valid()
	{
		assertTrue(keySequence.isValidAction());
	}

	@Test
	public void testIsValidAction_NoActions()
	{
		actionSequence.removeAll(actionSequence);
		
		assertTrue(actionSequence.size() == 0);
		assertTrue(keySequence.getActionSequence().size() == 0);
		assertFalse(keySequence.isValidAction());
	}
	
	@Test
	public void testIsValidAction_OneAction()
	{
		actionSequence.removeAll(actionSequence);
		actionSequence.add(new KeyboardKeyPress(KeyEvent.VK_CONTROL, 1002));
		
		assertTrue(actionSequence.size() == 1);
		assertTrue(keySequence.getActionSequence().size() == 1);
		assertFalse(keySequence.isValidAction());
	}
	
	@Test
	public void testIsValidAction_DoesntStartWithKeyPress()
	{
		actionSequence.remove(0);
		assertFalse(keySequence.isValidAction());
	}
	
	@Test
	public void testIsValidAction_NullActionInSequence()
	{
		actionSequence.add(2, null);
		
		assertFalse(keySequence.isValidAction());
	}
	
	//========================================================
	
	@Test
	public void testToString()
	{
		assertEquals("Key-Pressed Sequence: KEY:CONTROL", keySequence.toString());
	}

	//========================================================
	
	@Test(expected=IllegalArgumentException.class)
	public void testKeyPressedSequence_NullSequence()
	{
		keySequence = new KeyPressedSequence(null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testKeyPressedSequence_NoItemSequence()
	{
		keySequence = new KeyPressedSequence(new ArrayList<AbstractInputAction>());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testKeyPressedSequence_OneItemSequence()
	{
		actionSequence.removeAll(actionSequence);
		actionSequence.add(new KeyboardKeyPress(KeyEvent.VK_CONTROL, 1002));
		
		assertTrue(actionSequence.size() == 1);
		keySequence = new KeyPressedSequence(actionSequence);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testKeyPressedSequence_SequenceDoesntStartWithKey()
	{
		actionSequence.remove(0);
		keySequence = new KeyPressedSequence(actionSequence);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testKeyPressedSequence_SequenceContainsSequence()
	{
		List<AbstractInputAction> anotherseq = new ArrayList<AbstractInputAction>();
		anotherseq.add(new KeyboardKeyPress(KeyEvent.VK_CONTROL, 1002));
		anotherseq.add(new MouseClick(MouseButton.Left, new Pixel(new PixelColor(11691775), new PixelCoordinate2D(150, 100)), 123321));
		anotherseq.add(new MouseWait(101));
		
		actionSequence.add(new KeyPressedSequence(anotherseq));
		
		keySequence = new KeyPressedSequence(actionSequence);
	}
	
	@Test
	public void testKeyPressedSequence_valid()
	{
		assertTrue(keySequence.isValidAction());
		assertEquals(keySequence.getActionSequence(), actionSequence);
		assertNotNull(keySequence.getStartKeyPressed());
	}
	//========================================================
	
	@Test
	public void testGetActionSequence()
	{
		assertEquals(keySequence.getActionSequence(), actionSequence);
	}

	//========================================================
	
	@Test
	public void testGetStartKeyPressed()
	{
		assertEquals(keySequence.getStartKeyPressed().getKeyValueInt(), KeyEvent.VK_CONTROL);
	}

	//========================================================
}
