package com.github.stevewhit.mouserecorder.inputtracking;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.SwingDispatchService;

public class GlobalInputRecorder
{
	/**
	 * A queue of strings that represents the last action values that were captured during a recording session.
	 */
	private Queue<String> lastRecordingValues;
	
	/**
	 * The listeners used during the recording process. Remember to remove them as listeners from the global screen once completed.
	 */
	private GlobalMouseInputTracker globalMouseTracker;
	private GlobalKeyTracker globalKeyTracker;
	
	public GlobalInputRecorder(){}
	
	/**
	 * Returns the last action values that were captured during a recording session.
	 * @return Returns a queue of strings representing mouse and keyboard actions that occurred.
	 */
	public Queue<String> getLastRecordingValues()
	{
		return this.lastRecordingValues;
	}
	
	/**
	 * Re-initializes the last recording values so they aren't re-added to the recording list.
	 */
	public void resetLastRecordingValues()
	{
		lastRecordingValues = new LinkedList<String>();
	}
	
	/**
	 * Records user mouse and keyboard actions and stores each of them in a queue via a formatted string representation.
	 * The default cancellation keys for this recording are ALT+R.
	 * @return Returns a queue of formatted strings that represent the actions that took place during the recording.
	 * @throws throws IllegalStateException Throws if we are unable to register the native hook to the global screen.
	 */
	public Queue<String> Record() throws IllegalStateException
	{
		return Record(new int[]{});
	}
	
	/**
	 * Records user mouse and keyboard actions and stores each of them in a queue via a formatted string representation. Also, the actions
	 * are also added to the optional text area if it isn't null. The default cancellation keys for this recording are ALT+R.
	 * @param optionalOutputTextArea An optional textArea that will only be written to if it isn't null.
	 * @return Returns a queue of formatted strings that represent the actions that took place during the recording.
	 * @throws throws IllegalStateException Throws if we are unable to register the native hook to the global screen.
	 */
	public Queue<String> Record(JTextArea optionalOutputTextArea) throws IllegalStateException
	{
		return Record(optionalOutputTextArea, null);
	}
		
	/**
	 * Records user mouse and keyboard actions and stores each of them in a queue via a formatted string representation.
	 * @param cancellationKeys The KeyEvent keyvalues of the keys (if pressed together) that force the recorder to stop recording user actions.
	 * @return Returns a queue of formatted strings that represent the actions that took place during the recording.
	 * @throws throws IllegalStateException Throws if we are unable to register the native hook to the global screen.
	 */
	public Queue<String> Record(int[] cancellationKeys) throws IllegalStateException
	{
		return Record(null, cancellationKeys);
	}
	
	/**
	 * Records user mouse and keyboard actions and stores each of them in a queue via a formatted string representation. Also, the actions
	 * are also added to the optional text area if it isn't null.
	 * @param optionalOutputTextArea An optional textArea that will only be written to if it isn't null.
	 * @param cancellationKeys The KeyEvent keyvalues of the keys (if pressed together) that force the recorder to stop recording user actions.
	 * @return Returns a queue of formatted strings that represent the actions that took place during the recording.
	 * @throws throws IllegalStateException Throws if we are unable to register the native hook to the global screen.
	 */
	public Queue<String> Record(JTextArea optionalOutputTextArea, int[] cancellationKeys) throws IllegalStateException
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
		
		// Remove previous listeners so we don't have multiple recorders
		GlobalScreen.removeNativeMouseListener(globalMouseTracker);
		GlobalScreen.removeNativeMouseMotionListener(globalMouseTracker);
		GlobalScreen.removeNativeKeyListener(globalKeyTracker);
		
		// Create and add the mouse input trackers
		globalMouseTracker = new GlobalMouseInputTracker(recordedActionsQueue, optionalOutputTextArea);
		GlobalScreen.addNativeMouseListener(globalMouseTracker);
		GlobalScreen.addNativeMouseMotionListener(globalMouseTracker);
		
		// Create and add the key trackers.
		globalKeyTracker = new GlobalKeyTracker(this, recordedActionsQueue, optionalOutputTextArea, cancellationKeys);
		GlobalScreen.addNativeKeyListener(globalKeyTracker);
		
		/* Note: JNativeHook does *NOT* operate on the event dispatching thread.
		 * Because Swing components must be accessed on the event dispatching
		 * thread, you *MUST* wrap access to Swing components using the
		 * SwingUtilities.invokeLater() or EventQueue.invokeLater() methods.
		 */
		GlobalScreen.setEventDispatcher(new SwingDispatchService());
		
		// Save the recorded values for access later.
		this.lastRecordingValues = recordedActionsQueue;
		
		return recordedActionsQueue;
	}
	
	/**
	 * Stops the input recorder from recording any more actions.
	 */
	public void StopRecording()
	{
		// Remove previous listeners so we don't have multiple recorders
		GlobalScreen.removeNativeMouseListener(globalMouseTracker);
		GlobalScreen.removeNativeMouseMotionListener(globalMouseTracker);
		GlobalScreen.removeNativeKeyListener(globalKeyTracker);
	}
}
