package com.github.stevewhit.mouserecorder.userinputs.mouse;

import java.awt.event.InputEvent;

public enum MouseButton
{
	Left(InputEvent.BUTTON1_MASK),
	Right(InputEvent.BUTTON3_MASK),
	ScrollWheel(InputEvent.BUTTON2_MASK);
	
	private int buttonNum;
	
	private MouseButton(int buttonNum)
	{
		this.buttonNum = buttonNum;
	}
	
	public int getButtonNum()
	{
		return buttonNum;
	}	
}
