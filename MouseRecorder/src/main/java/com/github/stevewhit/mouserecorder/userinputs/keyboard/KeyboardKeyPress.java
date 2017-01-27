package com.github.stevewhit.mouserecorder.userinputs.keyboard;

/**
 * Represents a keyboard key being pressed. This object contains a keyvalue and a timestamp for when it occurred.
 * @author Steve Whitmire (swhit114@gmail.com)
 *
 */
public class KeyboardKeyPress extends AbstractKeyboardInputAction
{
	/**
	 * Constructor that sets up the key press with a key value and timestamp.
	 * @param keyValue The keyboard key value of the key that is pressed.
	 * @param timeStamp The time (in nanoseconds) which this action occurred.
	 * @throws IllegalArgumentException Throws if the keyvalue or timestamp are negative.
	 */
	public KeyboardKeyPress(int keyValue, long timeStamp) throws IllegalArgumentException
	{
		super(keyValue, timeStamp);
	}
	
	/**
	 * {@inheritDoc}
	 * <pre>
	 * Example output: KEYPRESS:CONTROL
	 * </pre>
	 */
	@Override
	public String toString()
	{
		if (!isValidAction())
		{
			return null;
		}
		
		return String.format("KEYPRESS:%1$s", getKeyValueText(getKeyValueInt()));
	}
}
