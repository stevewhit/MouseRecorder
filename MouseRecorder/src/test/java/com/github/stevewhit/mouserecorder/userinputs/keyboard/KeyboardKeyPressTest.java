package com.github.stevewhit.mouserecorder.userinputs.keyboard;

import static org.junit.Assert.*;

import java.awt.event.KeyEvent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class KeyboardKeyPressTest
{
	KeyboardKeyPress pressedKey;
	
	@Before
	public void setUp() throws Exception
	{
		pressedKey = new KeyboardKeyPress('A', 1651616);
	}
	
	@After
	public void tearDown() throws Exception
	{
		pressedKey = null;
	}
	
	//===========================================================
	
	@Test
	public void testIsValidAction()
	{
		assertTrue(pressedKey.isValidAction());
	}

	//===========================================================
	
	@Test
	public void testToString()
	{
		assertEquals("KEYPRESS:A", pressedKey.toString());
		
		pressedKey = new KeyboardKeyPress(KeyEvent.VK_SEMICOLON, 109);
		assertEquals("KEYPRESS:SEMICOLON", pressedKey.toString());
		
		// Can't test for an invalid keyboard key because we can't make one.
	}

	//===========================================================
	
	@Test(expected=IllegalArgumentException.class)
	public void testKeyboardKeyPress_NegKeyVal()
	{
		pressedKey = new KeyboardKeyPress(-1, 123);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testKeyboardKeyPress_NegtimeStamp()
	{
		pressedKey = new KeyboardKeyPress(67, -1);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testKeyboardKeyPress_NotSupportedKeyValue()
	{
		pressedKey = new KeyboardKeyPress(264, 1234);
	}
	
	@Test
	public void testKeyboardKeyPress_valid()
	{
		assertTrue(pressedKey.isValidAction());
		assertTrue(pressedKey.getKeyValueInt() == KeyEvent.VK_A);
		assertTrue(pressedKey.getTimeStamp() == 1651616);
		
		pressedKey = new KeyboardKeyPress(67, 0);
		pressedKey = new KeyboardKeyPress(67, 12341234);
	}

	//===========================================================
	
	@Test
	public void testGetKeyValueInt()
	{
		assertTrue(pressedKey.getKeyValueInt() == 'A');
	}

	//===========================================================
	
	@Test
	public void testGetKeyValueText()
	{
		assertEquals(AbstractKeyboardInputAction.getKeyValueText('A'), "A");
	}

	//===========================================================
	
	@Test
	public void testGetTimeStamp()
	{
		assertTrue(pressedKey.getTimeStamp() == 1651616);
	}

	//===========================================================
	
}
