package com.github.stevewhit.mouserecorder.userinputs;

import java.util.List;

/**
 * Represents the holding action of a mouse button or keyboard key followed by a series of user input actions.
 * @author Steve Whitmire (swhit114@gmail.com)
 *
 */
public interface ActionSequence
{
	/**
	 * Returns the action sequence for this action.
	 * @return Returns the action sequence as a list of input actions.
	 */
	public List<AbstractInputAction> getActionSequence();
	
	/**
	 * Returns a representation of this action sequence as a formatted string.
	 * @return Returns a representation of this action sequence as a formatted string.
	 */
	@Override 
	public String toString();
}
