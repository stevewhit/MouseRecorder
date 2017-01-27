package com.github.stevewhit.mouserecorder.inputtracking;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

public class GlobalInputRecorder
{
	public GlobalInputRecorder(){}
	
	/**
	 * Records user mouse and keyboard actions and stores each of them in a queue via a formatted string representation.
	 * The default cancellation keys for this recording are ALT+R.
	 * @return Returns a queue of formatted strings that represent the actions that took place during the recording.
	 * @throws throws IllegalStateException Throws if we are unable to register the native hook to the global screen.
	 */
	public Queue<String> Record() throws IllegalStateException
	{
		return Record(null);
	}
		
	/**
	 * Records user mouse and keyboard actions and stores each of them in a queue via a formatted string representation.
	 * @param cancellationKeys The KeyEvent keyvalues of the keys (if pressed together) that force the recorder to stop recording user actions.
	 * @return Returns a queue of formatted strings that represent the actions that took place during the recording.
	 * @throws throws IllegalStateException Throws if we are unable to register the native hook to the global screen.
	 */
	public Queue<String> Record(int[] cancellationKeys) throws IllegalStateException
	{
		// Create logger for the GlobalScreen.
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		
		// Disable the parent handlers.
		logger.setUseParentHandlers(false);
		logger.setLevel(Level.ALL);
		
		try
		{
			// Register the native hook so we can begin recording
			GlobalScreen.registerNativeHook();
		}
		catch (NativeHookException ex)
		{
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());

			throw new IllegalStateException("There was a problem registering the native hook. Cannot proceed to record.");
		}
		
		Queue<String> recordedActionsQueue = new LinkedList<String>(); 
		
		// Create and add the mouse input trackers
		GlobalMouseInputTracker globalMouseTracker = new GlobalMouseInputTracker(recordedActionsQueue);
		GlobalScreen.addNativeMouseListener(globalMouseTracker);
		GlobalScreen.addNativeMouseMotionListener(globalMouseTracker);
		
		// Create and add the key trackers.
		GlobalScreen.addNativeKeyListener(new GlobalKeyTracker(recordedActionsQueue, cancellationKeys));
		
		while (GlobalScreen.isNativeHookRegistered())
		{
			// Loop to keep the program synchronized until the user is finished recording.
		}
		
		return recordedActionsQueue;
	}
}
