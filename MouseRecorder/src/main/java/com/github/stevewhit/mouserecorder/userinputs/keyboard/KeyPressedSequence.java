package com.github.stevewhit.mouserecorder.userinputs.keyboard;

import java.time.LocalDateTime;
import java.util.List;
import com.github.stevewhit.mouserecorder.userinputs.AbstractInputAction;
import com.github.stevewhit.mouserecorder.userinputs.ActionSequence;


/**
 * Represents a sequence of user actions which starts with a keyboard key press and hold, followed by an unbounded number of user inputs.
 * @author Steve Whitmire (swhit114@gmail.com)
 *
 */
public class KeyPressedSequence extends AbstractInputAction implements ActionSequence
{
	/**
	 * a list of user inputs actions starting with a keyboard key press and hold followed by an unbounded number of user inputs.
	 */
	private List<AbstractInputAction> actionSequence;
	
	/**
	 * Constructor that accepts list of input actions that represent a key sequence.
	 * @param actionSequence A list of user input actions starting with a keyboard key press and hold followed an unbounded number of user inputs.
	 * @throws IllegalArgumentException Throws if the action sequence is null, only contains one action, or doesn't start with a keyboard key press.
	 */
	public KeyPressedSequence(List<AbstractInputAction> actionSequence) throws IllegalArgumentException
	{
		super (LocalDateTime.now());
		
		try
		{
			setInputSequence(actionSequence);
			
			if(!isValidAction())
			{
				throw new IllegalArgumentException("Input sequence needs to start with a keyboard key press.");
			}
		}
		
		catch (IllegalArgumentException ex)
		{
			this.actionSequence = null;
			
			throw new IllegalArgumentException("Cannot create KeyPressedSequence action because ==> " + ex.getMessage());
		}
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * Checks this action for invalid actions that occur in the action sequence.
	 * </p>
	 */
	public boolean isValidAction()
	{
		// Verify the action sequence isn't null and contains atleast two actions.
		if (actionSequence == null || actionSequence.size() <= 1)
		{
			return false;
		}
		
		AbstractInputAction firstAction = actionSequence.get(0);
		
		// Verify the first action is a keyboard key press and that it's valid.
		if (!KeyboardKeyPress.class.isAssignableFrom(firstAction.getClass()) || !((KeyboardKeyPress)firstAction).isValidAction())
		{
			return false;
		}
		
		// Check each action for validity but skip the first action 
		// because we already know it's the keyboard key press.
		for (AbstractInputAction action : actionSequence.subList(1, actionSequence.size()))
		{
			// Check for a valid action
			if (action == null || !action.isValidAction())
			{
				return false;
			}
			
			// Check that this key sequence doesn't contain another key pressed sequence
			if (KeyPressedSequence.class.isAssignableFrom(action.getClass()))
			{
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<AbstractInputAction> getActionSequence()
	{
		return this.actionSequence;
	}
	
	/**
	 * {@inheritDoc}
	 * <pre>
	 * Example output: 'Key-Pressed Sequence: KEY:CONTROL
	 * </pre>
	 */
	public String toString()
	{
		if (! isValidAction() )
		{
			return null;
		}
		
		return String.format("Key-Pressed Sequence: %1$s", getStartKeyPressed().toString());
	}
	
	/**
	 * Returns the starting key that is held throughout this action sequence.
	 * @return Returns the starting key that is held throughout this action sequence as a KeyboardKeyPress object.
	 * @throws IllegalStateException Throws if this sequence hasn't been properly initialized before calling this method.
	 */
	public KeyboardKeyPress getStartKeyPressed() throws IllegalStateException
	{
		if (!isValidAction())
		{
			throw new IllegalStateException("Sequence must be properly initialized before accessing starting key pressed.");
		}
		
		// By checking if the action is valid, we've already identified tht the first
		// Item in the sequence is a KeyboardKeyPress action.
		
		return ((KeyboardKeyPress)actionSequence.get(0));
	}
	
	/**
	 * Updates the action sequence for this object.
	 * @param actionSequence A list of user input actions starting with a keyboard key press and hold followed an unbounded number of user inputs.
	 * @throws IllegalArgumentException Throws if the action sequence is null or contains less than 2 actions.
	 */
	private void setInputSequence(List<AbstractInputAction> actionSequence) throws IllegalArgumentException
	{
		if (actionSequence == null || actionSequence.size() <= 1)
		{
			throw new IllegalArgumentException("Input action sequence must not be null and must contain atleast two actions.");
		}
		
		this.actionSequence = actionSequence;
	}
}

















