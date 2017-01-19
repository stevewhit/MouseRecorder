package com.github.stevewhit.mouserecorder.userinputs.mouse;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import com.github.stevewhit.mouserecorder.monitor.PixelCoordinate2D;
import com.github.stevewhit.mouserecorder.userinputs.AbstractInputAction;
import com.github.stevewhit.mouserecorder.userinputs.ActionSequence;

/**
 * Represents a sequence of user actions which starts with a mouse click and hold, followed by an unbounded number of user inputs.
 * @author Steve Whitmire (swhit114@gmail.com)
 *
 */
public class MouseButtonPressedSequence extends AbstractInputAction implements ActionSequence
{
	/**
	 * A list of user input actions starting with a mouse click and hold followed an unbounded number of user inputs.
	 */
	private List<AbstractInputAction> actionSequence;
	
	/**
	 * Constructor that accepts list of input actions that represent a mouse sequence.
	 * @param actionSequence A list of user input actions starting with a mouse click and hold followed an unbounded number of user inputs.
	 * @throws IllegalArgumentException Throws if the action sequence is null, only contains one action, or doesn't start with a mouse click.
	 */
	public MouseButtonPressedSequence(List<AbstractInputAction> actionSequence) throws IllegalArgumentException
	{
		super (LocalDateTime.now());
	
		try
		{
			setInputSequence(actionSequence);
			
			if(!isValidAction())
			{
				throw new IllegalArgumentException("Input sequence needs to start with a mouse click.");
			}
		}
		
		catch (IllegalArgumentException ex)
		{
			this.actionSequence = null;
			
			throw new IllegalArgumentException("Cannot create MouseButtonPressedSequence action because ==> " + ex.getMessage());
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
		
		// Verify the first action is a mouse click and that it's valid.
		if (!MouseClick.class.isAssignableFrom(firstAction.getClass()) || !((MouseClick)firstAction).isValidAction())
		{
			return false;
		}
		
		// Check each action for validity but skip the first action 
		// because we already know it's the click action.
		for (AbstractInputAction action : actionSequence.subList(1, actionSequence.size()))
		{
			// Check for a valid action
			if (action == null || !action.isValidAction())
			{
				return false;
			}
			
			// Check that this drag sequence doesn't contain another drag sequence.
			if (MouseButtonPressedSequence.class.isAssignableFrom(action.getClass()))
			{
				return false;
			}
			
			// Check that this drag sequence doesn't contain a click
			if (MouseClick.class.isAssignableFrom(action.getClass()))
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
	 * Example output: 'Mouse-Click Sequence: (12, 454)-->(112, 123)'
	 * </pre>
	 */
	public String toString()
	{
		if (! isValidAction() )
		{
			return null;
		}
		
		return String.format("Mouse-Click Sequence: %1$s-->%2$s", getStartingLocation().toString(), getEndingLocation().toString());
	}
	
	/**
	 * Returns the starting pixel location of this sequence.
	 * @return Returns the starting pixel location of this sequence as a PixelCoordinate2D.
	 * @throws IllegalStateException Throws if this sequence hasn't been properly initialized before calling this method.
	 */
	public PixelCoordinate2D getStartingLocation() throws IllegalStateException
	{
		if (!isValidAction())
		{
			throw new IllegalStateException("Sequence must be properly initialized before accessing start location.");
		}
		
		// By checking if the action is valid, we've already identified that the first
		// Item in the sequence is a MouseClick action.
		
		return ((MouseClick)actionSequence.get(0)).getPixelClicked().getLocation();
	}
	
	/**
	 * Returns the ending pixel location of this sequence.
	 * @return Returns the ending pixel location of this sequence as a PixelCoordinate2D.
	 * @throws IllegalStateException Throws if this sequence hasn't been properly initialized before calling this method.
	 */
	public PixelCoordinate2D getEndingLocation() throws IllegalStateException
	{
		if (!isValidAction())
		{
			throw new IllegalStateException("Sequence must be properly initialized before accessing end location.");
		}
			
		PixelCoordinate2D endLocation = null;
		
		// Iterate the action sequence looking for that last action that could possibly move the mouse cursor.
		for (Iterator<AbstractInputAction> actionIterator = actionSequence.listIterator(); actionIterator.hasNext();)
		{
			AbstractInputAction action = actionIterator.next();
			
			// If the action is a mouse click or a subclass
			if (MouseClick.class.isAssignableFrom(action.getClass()))
			{
				endLocation = ((MouseClick)action).getPixelClicked().getLocation();
			}
			
			// If the action is a mouse move or a subclass
			else if (MouseMove.class.isAssignableFrom(action.getClass()))
			{
				endLocation = ((MouseMove)action).getEndPixel().getLocation();
			}
		}
		
		return endLocation;
	}
	
	/**
	 * Updates the action sequence for this object.
	 * @param actionSequence A list of user input actions starting with a mouse click and hold followed an unbounded number of user inputs.
	 * @throws IllegalArgumentException Throws if the action sequence is null or contains less than 2 actions.
	 */
	private void setInputSequence(List<AbstractInputAction> actionSequence) throws IllegalArgumentException
	{
		if (actionSequence == null || actionSequence.size() <= 1)
		{
			throw new IllegalArgumentException("Input action sequence must not be null and must contain atleast two items.");
		}
		
		this.actionSequence = actionSequence;
	}
}





























