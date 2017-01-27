package com.github.stevewhit.mouserecorder.userinputs.keyboard;

import static org.junit.Assert.*;

import java.awt.event.KeyEvent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class KeyboardKeyReleaseTest
{
	KeyboardKeyRelease releasedKey;
	
	@Before
	public void setUp() throws Exception
	{
		releasedKey = new KeyboardKeyRelease('A', 1700000);
	}
	
	@After
	public void tearDown() throws Exception
	{
		releasedKey = null;
	}
	
	//==========================================================
	
	@Test
	public void testToString()
	{
		assertEquals("KEYRELEASE:A", releasedKey.toString());
	}

	//==========================================================
		
	@Test(expected=IllegalArgumentException.class)
	public void testKeyboardKeyRelease_NegativeKeyVal()
	{
		releasedKey = new KeyboardKeyRelease(-1, 123);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testKeyboardKeyRelease_NegativeTimeStamp()
	{
		releasedKey = new KeyboardKeyRelease(67, -1);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testKeyboardKeyRelease_NotSupportedKeyValue()
	{
		releasedKey = new KeyboardKeyRelease(264, 1234);
	}
	
	@Test
	public void testKeyboardKeyRelease_valid()
	{
		assertTrue(releasedKey.isValidAction());
		assertTrue(releasedKey.getKeyValueInt() == KeyEvent.VK_A);
		assertTrue(releasedKey.getTimeStamp() == 1700000);
		
		releasedKey = new KeyboardKeyRelease(67, 0);
		releasedKey = new KeyboardKeyRelease(67, 12341234);
	}

	//==========================================================
	
}
