package com.github.stevewhit.mouserecorder.inputtracking;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class GlobalKeyTracker implements NativeKeyListener
{
	/**
	 * Typical format for key press: keyNumber, xCoordinate, yCoordinate, rgbInt, time(in nanoseconds)
	 */
	private final static String KEY_PRESS_FORMAT = "KPRESS:%1$d:%2$s";
	
	/**
	 * Typical format for key release: ButtonNumber, xCoordinate, yCoordinate, rgbInt, time(in nanoseconds)
	 */
	private final static String KEY_RELEASE_FORMAT = "KRELEA:%1$d:%2$s";
	
	/**
	 * A reference to the queue that contains the input actions./
	 */
	private Queue<String> actionsQueue;
	
	/**
	 * The cancellation keys that if pressed at the same time, stop the recording.
	 */
	private Map<Integer, Boolean> cancellationKeysMap; 
	
	/**
	 * Constructor that accepts a reference to the actions queue that the generated mouse clicks are added to and an array of cancellation keys. 
	 * The default cancellation keys are set to ALT+R
	 * @param actionsQueueRef The queue of actions that clicks are added to.
	 * @param cancellationKeys An array of integer values which represent KeyEvent integers, used to cancel the recorder.
	 * @throws IllegalArgumentException Throws if the queue is null.
	 */
	protected GlobalKeyTracker(Queue<String> actionsQueue, int[] cancellationKeys) throws IllegalArgumentException
	{
		if (actionsQueue == null)
		{
			try
			{
				GlobalScreen.unregisterNativeHook();
			}
			catch (NativeHookException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			throw new IllegalArgumentException("Actions queue cannot be null.");
		}
		
		this.actionsQueue = actionsQueue;
		
		setCancellationKeys(cancellationKeys);
	}
	
	/**
	 * Captures and stores relevant key information when a key is pressed.
	 */
	@Override
	public void nativeKeyPressed(NativeKeyEvent e)
	{
		final long timeCaptured = System.nanoTime();
		
		// Convert the native key keycode into KeyEvent keycode for later use.
		final int key = KeyboardKeyConverterUtils.nativeKeyToEventKey(e);

		if (key == -1)
		{
			System.out.println("Skipping unsupported key action: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
			return;
		}
		
		// If the key is one of the cancellation keys
		if (cancellationKeysMap.containsKey(key))
		{
			// Update the map value and check for the required cancellation sequence.
			cancellationKeysMap.put(key, true);

			if (areCancellationKeysPressed())
			{
				try
				{
					GlobalScreen.unregisterNativeHook();
				}
				catch (NativeHookException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				return;
			}
		}
		
		final String actionToFormattedString = String.format(KEY_PRESS_FORMAT, key, timeCaptured);
		
		// Add the formatted string to the queue.
		actionsQueue.add(actionToFormattedString);
		
		System.out.println(actionToFormattedString);
	}

	/**
	 * Captures and stores relevant key information when a key is released.
	 */
	@Override
	public void nativeKeyReleased(NativeKeyEvent e)
	{
		final long timeCaptured = System.nanoTime();
		final int key = KeyboardKeyConverterUtils.nativeKeyToEventKey(e);
		
		// If the key is one of the cancellation keys
		if (cancellationKeysMap.containsKey(key))
		{
			// Update the map value
			cancellationKeysMap.put(key, false);
		}
		
		final String actionToFormattedString = String.format(KEY_RELEASE_FORMAT, key, timeCaptured);
		
		// Add the formatted string to the queue.
		actionsQueue.add(actionToFormattedString);
		
		System.out.println(actionToFormattedString);
	}

	/**
	 * Allows the user to set their own hotkeys to stop the tracker from recording.
	 * If the cancellation keys are null or empty, use the default ALT+R keys.
	 */
	public void setCancellationKeys(int[] cancellationKeys)
	{
		// If null or empty, use the default ALT+R 
		if (cancellationKeys == null || cancellationKeys.length == 0)
		{
			cancellationKeys = new int[]{KeyEvent.VK_ALT, KeyEvent.VK_R};
		}
		
		cancellationKeysMap = new HashMap<Integer, Boolean>();
		
		// Add each of the keys to the cancellation key map.
		for(int key : cancellationKeys)
		{
			cancellationKeysMap.put(key, false);
		}	
	}
	
	/**
	 * Checks each of the cancellation keys and determines if they are all pressed at the same time. 
	 * @return Returns true if they are all pressed at the same time; otherwise false.
	 */
	private boolean areCancellationKeysPressed()
	{
		boolean stopRecording = true;
		
		// Iterate the cancellation keys and determine if they are all pressed at the same time.
		for(Iterator<Boolean> cancellationKeysIterator = cancellationKeysMap.values().iterator(); cancellationKeysIterator.hasNext();)
		{
			boolean cancelValue = cancellationKeysIterator.next();
			stopRecording = stopRecording && cancelValue;
		}
		
		return stopRecording;
	}
	
	@Override
	public void nativeKeyTyped(NativeKeyEvent e)
	{
		/**
		 * NOT USED!
		 */
	}
}

































