package com.github.stevewhit.mouserecorder.userinputs.mouse;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.stevewhit.mouserecorder.monitor.Pixel;
import com.github.stevewhit.mouserecorder.monitor.PixelColor;
import com.github.stevewhit.mouserecorder.monitor.PixelCoordinate2D;

public class AbstractMouseButtonActionTest
{
	protected class ConcreteMouseButtonAction extends AbstractMouseButtonAction
	{
		protected ConcreteMouseButtonAction(MouseButton buttonPressed, Pixel pixelInfo, long timeStamp)	throws IllegalArgumentException
		{
			super(buttonPressed, pixelInfo, timeStamp);
		}

		@Override
		public String toString()
		{
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	ConcreteMouseButtonAction buttonAction;
	MouseButton buttonPressed;
	Pixel pixelInfo;
	PixelColor color;
	PixelCoordinate2D location;
	
	@Before
	public void setUp() throws Exception
	{
		buttonPressed = MouseButton.Left;
		color = new PixelColor(2345233);
		location = new PixelCoordinate2D(150, 1000);
		pixelInfo = new Pixel(color, location);
		
		buttonAction = new ConcreteMouseButtonAction(buttonPressed, pixelInfo, 65161235l);
	}
	
	@After
	public void tearDown() throws Exception
	{
		buttonAction = null;
		pixelInfo = null;
		color = null;
		location = null;
	}
	
	//===================================================================
	
	@Test
	public void testIsValidAction()
	{
		assertTrue(buttonAction.isValidAction());
	}

	//===================================================================
	
	@Test(expected = IllegalArgumentException.class)
	public void testAbstractMouseButtonAction_nullPixelInfo()
	{
		buttonAction = new ConcreteMouseButtonAction(MouseButton.Left, null, 123412);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAbstractMouseButtonAction_NegTimeStamp()
	{
		buttonAction = new ConcreteMouseButtonAction(MouseButton.Left, pixelInfo, -1);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAbstractMouseButtonAction_NullButtonPressed()
	{
		buttonAction = new ConcreteMouseButtonAction(null, pixelInfo, 123412);
	}
	
	@Test
	public void testAbstractMouseButtonAction_valid()
	{
		assertTrue(buttonAction.isValidAction());
		assertTrue(buttonAction.getMouseButton() == MouseButton.Left);
		assertTrue(buttonAction.getPixelColor().equals(new PixelColor(2345233)));
		
		buttonAction = new ConcreteMouseButtonAction(MouseButton.Left, pixelInfo, 0);
		buttonAction = new ConcreteMouseButtonAction(MouseButton.Left, pixelInfo, 1);
		buttonAction = new ConcreteMouseButtonAction(MouseButton.Left, pixelInfo, 123412342314l);
	}
	//===================================================================
	
	@Test
	public void testGetMouseButton()
	{
		assertTrue(buttonAction.getMouseButton() == MouseButton.Left);
	}

	//===================================================================
	
	@Test
	public void testGetPixelColor()
	{
		assertTrue(buttonAction.getPixelColor().equals(new PixelColor(2345233)));
	}

	//===================================================================
	
}
