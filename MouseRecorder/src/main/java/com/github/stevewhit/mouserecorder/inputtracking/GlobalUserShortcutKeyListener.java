package com.github.stevewhit.mouserecorder.inputtracking;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Iterator;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class GlobalUserShortcutKeyListener implements NativeKeyListener
{
	/** 
	 * Used to fire property change events to be handled by an application 
	 * when a shortcut combination is pressed
	 */
	protected PropertyChangeSupport propertyChangeSupport;
	
	/**
	 * The key sequences that will fire update events if they are pressed at the same time.
	 */
	private ArrayList<Integer[]> keySequencesThatFireEvents;
	
	/**
	 * The keys that are currently pressed and held.
	 */
	private ArrayList<Integer> pressedKeys;
	
	/**
	 * Constructor that sets up a property change listener to watch for specific key sequences that are pressed.
	 * @param propertyListener The listener where to sent update values.
	 * @param keySequencesThatFireEvents A list of key integer value sequences to watch out for.
	 * @throws IllegalArgumentException Throws if one of the arguments supplied is invalid.
	 */
	public GlobalUserShortcutKeyListener(PropertyChangeListener propertyListener, ArrayList<Integer[]> keySequencesThatFireEvents) throws IllegalArgumentException
	{
		try
		{
			if (propertyListener == null)
				throw new IllegalArgumentException("Property listener cannot be null.");

			// Setup the property change support so we can fire property change events if needed.
			propertyChangeSupport = new PropertyChangeSupport(this);
			propertyChangeSupport.addPropertyChangeListener(propertyListener);
			
			setKeySequencesThatFireEvents(keySequencesThatFireEvents);
			
			pressedKeys = new ArrayList<>();
		}
		catch(IllegalArgumentException ex)
		{
			propertyChangeSupport = null;
			keySequencesThatFireEvents = null;
			pressedKeys = null;
			
			throw new IllegalArgumentException("Cannot create key listener because ==> " + ex.getMessage());
		}
	}
	
	/**
	 * Verifies key sequences are valid and then updates the key sequences variable.
	 * @param keySequencesThatFireEvents Key integer sequences that fire update events.
	 * @throws IllegalArgumentException Throws if an invalid key combination or key is supplied.
	 */
	public void setKeySequencesThatFireEvents(ArrayList<Integer[]> keySequencesThatFireEvents) throws IllegalArgumentException
	{
		if (keySequencesThatFireEvents == null)
			throw new IllegalArgumentException("Key sequences list cannot be null.");
		
		for (Integer[] keyCombo : keySequencesThatFireEvents)
		{
			if (keyCombo == null)
				throw new IllegalArgumentException("Invalid key combination supplied.");
			
			for(Integer key : keyCombo)
			{
				if (key == null)
					throw new IllegalArgumentException("Combo contains invalid key.");
			}
		}
		
		this.keySequencesThatFireEvents = keySequencesThatFireEvents;
	}
	
	/**
	 * Captures and stores relevant key information when a key is pressed.
	 */
	@Override
	public void nativeKeyPressed(NativeKeyEvent e)
	{
		// Check that this listener is properly initialized.
		checkForProperInitialization();
		
		// Convert the native key keycode into KeyEvent keycode for later use.
		final int key = KeyboardKeyConverterUtils.nativeKeyToEventKey(e);
		
		if (key == -1)
		{
			System.out.println("Skipping unsupported key action: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
			return;
		}
		
		// Don't add duplicate keys to the pressed keys list.
		if (!pressedKeys.contains(key))
		{
			pressedKeys.add(key);
			checkForPressedKeySequences();
		}
	}
	
	/**
	 * Captures and stores relevant key information when a key is released.
	 */
	@Override
	public void nativeKeyReleased(NativeKeyEvent e)
	{
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
	}
	
	/**
	 * Checks the pressed keys to see if any of the key sequences are pressed. If they are, it fires a property change.
	 */
	private void checkForPressedKeySequences()
	{
		int comboCounter = 0;
		
		// Check each key sequence to see if they are pressed.
		for (Integer[] keyCombo : keySequencesThatFireEvents)
		{
			boolean isKeyComboPressed = true;
			
			// Check each key combo to see if it is pressed.
			for(Integer key : keyCombo)
			{
				isKeyComboPressed &= pressedKeys.contains(key);
			}
			
			if (isKeyComboPressed)
			{
				propertyChangeSupport.firePropertyChange(null, null, comboCounter);
				return;
			}
			
			comboCounter++;
		}
	}
	
	/**
	 * Checks that each variable is properly initialized.
	 */
	private void checkForProperInitialization()
	{
		if (propertyChangeSupport == null || keySequencesThatFireEvents == null)
			throw new IllegalStateException("Key listener is not properly initialized.");

		if (pressedKeys == null)
			pressedKeys = new ArrayList<>();
		
		// Check the key sequences for initialization.
		for (Integer[] keyCombo : keySequencesThatFireEvents)
		{
			if (keyCombo == null)
				throw new IllegalStateException("Key sequences that fire events is not properly initialized.");
			
			for(Integer key : keyCombo)
			{
				if (key == null)
					throw new IllegalStateException("Atleast one key sequence contains null key.");
			}
		}
	}
	
	@Override
	public void nativeKeyTyped(NativeKeyEvent e)
	{
		/**
		 * NOT USED!
		 */
	}
}






























