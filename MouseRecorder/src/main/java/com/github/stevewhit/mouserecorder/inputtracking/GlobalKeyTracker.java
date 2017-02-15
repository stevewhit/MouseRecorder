package com.github.stevewhit.mouserecorder.inputtracking;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;
import javax.swing.JTextArea;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class GlobalKeyTracker implements NativeKeyListener
{
	/**
	 * Typical format for key press: keyNumber, time(in nanoseconds)
	 */
	private final static String KEY_PRESS_FORMAT_COMPACT = "KPRESS:%1$d:%2$s";
	private final static String KEY_PRESS_FORMAT_READABLE = "\nKey Pressed: '%1$s'";
	
	/**
	 * Typical format for key release: keyNumber, time(in nanoseconds)
	 */
	private final static String KEY_RELEASE_FORMAT_COMPACT = "KRELEA:%1$d:%2$s";
	private final static String KEY_RELEASE_FORMAT_READABLE = "\nKey Released: '%1$s'";
	
	/**
	 * A reference to the queue that contains the input actions./
	 */
	private Queue<String> actionsQueue;
	
	/**
	 * The cancellation keys that if pressed at the same time, stop the recording.
	 */
	private ArrayList<Integer> cancellationKeys;
	
	/**
	 * The keys that are currently pressed and held. Used so that multiple of the same "key pressed" actions aren't created for a single keypress.
	 */
	private ArrayList<Integer> pressedKeys;
	
	/**
	 * A reference to the text area that the recorded actions should be written to.
	 * This is an optional field and will only write to it if it isn't null.
	 */
	private JTextArea optionalOutputTextArea;
	
	/**
	 * The recorder that owns this key tracker.
	 */
	private GlobalInputRecorder parentRecorder;
	
	/**
	 * Constructor that accepts a reference to the actions queue that the generated mouse clicks are added to and an array of cancellation keys. 
	 * The default cancellation keys are set to ALT+R
	 * @param actionsQueueRef The queue of actions that clicks are added to.
	 * @param cancellationKeys An array of integer values which represent KeyEvent integers, used to cancel the recorder.
	 * @throws IllegalArgumentException Throws if the queue is null.
	 */
	protected GlobalKeyTracker(GlobalInputRecorder parentRecorder, Queue<String> actionsQueue, int[] cancellationKeys) throws IllegalArgumentException
	{
		this(parentRecorder, actionsQueue, null, cancellationKeys);
	}
	
	/**
	 * Constructor that accepts a reference to the actions queue that the generated mouse clicks are added to and an array of cancellation keys. 
	 * The default cancellation keys are set to ALT+R
	 * @param actionsQueueRef The queue of actions that clicks are added to.
	 * @param optionalOutputTextArea An optional textArea that will only be written to if it isn't null.
	 * @param cancellationKeys An array of integer values which represent KeyEvent integers, used to cancel the recorder.
	 * @throws IllegalArgumentException Throws if the queue is null.
	 */
	protected GlobalKeyTracker(GlobalInputRecorder parentRecorder, Queue<String> actionsQueue, JTextArea optionalOutputTextArea, int[] cancellationKeys) throws IllegalArgumentException
	{
		if (parentRecorder == null)
		{
			throw new IllegalArgumentException("Parent recorder cannot be null.");
		}
		
		if (actionsQueue == null)
		{
			parentRecorder.StopRecording();
			throw new IllegalArgumentException("Actions queue cannot be null.");
		}
		
		this.parentRecorder = parentRecorder;
		this.actionsQueue = actionsQueue;
		this.optionalOutputTextArea = optionalOutputTextArea;
		
		pressedKeys = new ArrayList<Integer>();
		
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
		
		// Add key to the list of pressed keys if not.
		pressedKeys.add(key);
		
		// If the key is one of the cancellation keys.
		if (cancellationKeys.contains(key))
		{
			// Check for the required cancellation sequence
			if (areCancellationKeysPressed())
			{
				parentRecorder.StopRecording();
				return;
			}
		}
		
		final String actionToFormattedString = String.format(KEY_PRESS_FORMAT_COMPACT, key, timeCaptured);
		
		// Add the formatted string to the queue.
		actionsQueue.add(actionToFormattedString);
		
		if (optionalOutputTextArea != null)
		{
			optionalOutputTextArea.append(String.format(KEY_PRESS_FORMAT_READABLE, NativeKeyEvent.getKeyText(e.getKeyCode())));
		}
	}

	/**
	 * Captures and stores relevant key information when a key is released.
	 */
	@Override
	public void nativeKeyReleased(NativeKeyEvent e)
	{
		final long timeCaptured = System.nanoTime();
		final int key = KeyboardKeyConverterUtils.nativeKeyToEventKey(e);
		
		if (key == -1)
		{
			System.out.println("Skipping unsupported key action: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
			return;
		}
		
		// Remove the key from the pressed-keys list
		for (Iterator<Integer> iter = pressedKeys.listIterator(); iter.hasNext();)
		{
			Integer pressedKeyId = iter.next();
			
			if (pressedKeyId == key)
			{
				iter.remove();
			}
		}
		
		final String actionToFormattedString = String.format(KEY_RELEASE_FORMAT_COMPACT, key, timeCaptured);
		
		// Add the formatted string to the queue.
		actionsQueue.add(actionToFormattedString);

		if (optionalOutputTextArea != null)
		{
			optionalOutputTextArea.append(String.format(KEY_RELEASE_FORMAT_READABLE, NativeKeyEvent.getKeyText(e.getKeyCode())));
		}
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
		
		this.cancellationKeys = new ArrayList<Integer>();
		
		// Add each of the keys to the cancellation key map.
		for(int key : cancellationKeys)
		{
			this.cancellationKeys.add(key);
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
		for(Iterator<Integer> cancellationKeysIterator = cancellationKeys.listIterator(); cancellationKeysIterator.hasNext();)
		{
			Integer cancelKey = cancellationKeysIterator.next();
			
			if (pressedKeys.contains(cancelKey))
			{
				stopRecording = stopRecording && true;
			}
			else
			{
				stopRecording = stopRecording && false;
			}
		}
		
		// If the stop recording keys are pressed, release them 
		// so they aren't still pressed when the user starts playback.
		if (stopRecording)
		{
			for(Iterator<Integer> cancellationKeysIterator = cancellationKeys.listIterator(); cancellationKeysIterator.hasNext();)
			{
				Integer cancelKey = cancellationKeysIterator.next();
				
				final String actionToFormattedString = String.format(KEY_RELEASE_FORMAT_COMPACT, cancelKey, System.nanoTime());
				
				// Add the formatted string to the queue.
				actionsQueue.add(actionToFormattedString);
			}
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


































