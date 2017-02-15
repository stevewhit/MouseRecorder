package com.github.stevewhit.mouserecorder.inputtracking;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.SwingDispatchService;

public class GlobalUserShortcutListener
{
	/**
	 * The listener used to track user key input values.
	 */
	private GlobalUserShortcutKeyListener globalUserShortcutKeyListener;
	
	public GlobalUserShortcutListener() {}
	
	/**
	 * Starts listening to the user inputs and fires an event if one of the sequences is pressed.
	 * @param propertyListener The listener where to sent update values.
	 * @param keySequencesThatFireEvents A list of key integer value sequences to watch out for.
	 * @throws IllegalStateException Throws if there is an issue registering the native hook.
	 * @throws IllegalArgumentException Throws if one of the arguments supplied is invalid.
	 */
	public void StartListening(PropertyChangeListener propertyListener, ArrayList<Integer[]> keySequencesThatFireEvents) throws IllegalStateException, IllegalArgumentException
	{
		// Create logger for the GlobalScreen.
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		
		// Disable the parent handlers.
		logger.setUseParentHandlers(false);
		logger.setLevel(Level.ALL);
		
		try
		{
			// Register the native hook so we can begin listening
			GlobalScreen.registerNativeHook();
		}
		catch (NativeHookException ex)
		{
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());

			throw new IllegalStateException("There was a problem registering the native hook. Cannot proceed to record.");
		}
		
		// Remove previous listeners so we don't have multiple recorders.
		GlobalScreen.removeNativeKeyListener(globalUserShortcutKeyListener);
		
		// Create and add the key listener.
		globalUserShortcutKeyListener = new GlobalUserShortcutKeyListener(propertyListener, keySequencesThatFireEvents);
		GlobalScreen.addNativeKeyListener(globalUserShortcutKeyListener);
		
		/* Note: JNativeHook does *NOT* operate on the event dispatching thread.
		 * Because Swing components must be accessed on the event dispatching
		 * thread, you *MUST* wrap access to Swing components using the
		 * SwingUtilities.invokeLater() or EventQueue.invokeLater() methods.
		 */
		GlobalScreen.setEventDispatcher(new SwingDispatchService());
	}
	
	/**
	 * Stops the listeners from tracking user inputs.
	 */
	public void StopListening()
	{
		// Remove previous listeners so we don't have multiple recorders.
		GlobalScreen.removeNativeKeyListener(globalUserShortcutKeyListener);
	}
}















