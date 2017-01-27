package com.github.stevewhit.mouserecorder.userinputs.keyboard;

/**
 * Represent a keyboard key being released. This object contains a keyvalue and a timestamp for when it occurred.
 * @author Steve Whitmire (swhit114@gmail.com)
 *
 */
public class KeyboardKeyRelease extends AbstractKeyboardInputAction
{
	/**
	 * Constructor that sets up the key release with a key value and a timestamp.
	 * @param keyValue The keyboard key value of the key that is released.
	 * @param timeStamp The time (in nanoseconds) which this action occurred.
	 * @throws IllegalArgumentException Throws if the keyvalue or timestamp are negative.
	 */
	public KeyboardKeyRelease(int keyValue, long timeStamp) throws IllegalArgumentException
	{
		super(keyValue, timeStamp);
	}
	
	/**
	 * {@inheritDoc}
	 * <pre>
	 * Example output: KEYRELEASE:CONTROL
	 * </pre>
	 */
	@Override
	public String toString()
	{
		if (!isValidAction())
		{
			return null;
		}
		
		return String.format("KEYRELEASE:%1$s", getKeyValueText(getKeyValueInt()));
	}
}
