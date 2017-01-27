package com.github.stevewhit.mouserecorder.userinputs.keyboard;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AbstractKeyboardInputActionTest
{
	protected class ConcreteKeyboardInputAction extends AbstractKeyboardInputAction
	{

		protected ConcreteKeyboardInputAction(int keyValue, long timeStamp) throws IllegalArgumentException
		{
			super(keyValue, timeStamp);
		}

		@Override
		public String toString()
		{
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	ConcreteKeyboardInputAction keyboardIA;
	int keyValue;
	long timeStamp;
	
	@Before
	public void setUp() throws Exception
	{
		keyValue = 67;
		timeStamp = 123456789876543l;
		
		keyboardIA = new ConcreteKeyboardInputAction(keyValue, timeStamp);
	}
	
	@After
	public void tearDown() throws Exception
	{
		keyboardIA = null;
	}
	
	//=====================================================
	
	@Test
	public void testIsValidAction()
	{
		assertTrue(keyboardIA.isValidAction());
	}

	//=====================================================
	
	@Test
	public void testToString()
	{
		// Implementation specific. 
		assertNull(keyboardIA.toString());
	}

	//=====================================================
	
	@Test(expected=IllegalArgumentException.class)
	public void testAbstractKeyboardInputAction_NegativeKeyValue()
	{
		keyboardIA = new ConcreteKeyboardInputAction(-1, 1234123412);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAbstractKeyboardInputAction_NotSupportedKeyValue()
	{
		keyboardIA = new ConcreteKeyboardInputAction(0, 1234123412);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAbstractKeyboardInputAction_NegativeTimeStamp()
	{
		keyboardIA = new ConcreteKeyboardInputAction(67, -1);
	}
	
	@Test
	public void testAbstractKeyboardInputAction_Valid()
	{
		keyboardIA = new ConcreteKeyboardInputAction(55, 0);
		keyboardIA = new ConcreteKeyboardInputAction(55, 1);
		keyboardIA = new ConcreteKeyboardInputAction('A', 1234123412);
	}
	
	//=====================================================
	
	@Test
	public void testGetKeyValueInt()
	{
		assertTrue(keyboardIA.getKeyValueInt() == 67);
	}

	//=====================================================
	
	@Test
	public void testGetKeyValueText()
	{
		assertEquals(keyboardIA.getKeyValueText('A'), "A");
		assertEquals(keyboardIA.getKeyValueText(0), null);
	}

	//=====================================================
	
}
