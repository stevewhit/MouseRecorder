package com.github.stevewhit.mouserecorder.mouse;

import java.time.LocalDateTime;

public class MouseDrag extends MouseAction
{
	private MouseClick mouseClick;
	private MouseMove mouseMove;
	
	/**
	 * Constructor that accepts a click and move action. 
	 * @param mouseClick - Click action information of the drag.
	 * @param mouseMove - Move action information of the drag.
	 * @throws IllegalArgumentException	- Throws if either the click or move are invalid.
	 */
	public MouseDrag(MouseClick mouseClick, MouseMove mouseMove) throws IllegalArgumentException
	{
		super(LocalDateTime.now());
		
		if (!isValidDrag(mouseClick, mouseMove))
		{
			throw new IllegalArgumentException("Click and Move actions produce an invalid drag.");
		}
		
		setMouseClick(mouseClick);
		setMouseMove(mouseMove);
	}
	
	/**
	 * Updates the mouseClick action for the drag.
	 * @param mouseClick - Click action to set.
	 * @throws IllegalArgumentException - Throws if click is null.
	 */
	private void setMouseClick(MouseClick mouseClick) throws IllegalArgumentException
	{
		if (mouseClick == null)
			throw new IllegalArgumentException("Cannot set a null MouseClick.");
		
		this.mouseClick = mouseClick;
	}
	
	/**
	 * Returns the click action of the drag.
	 * @return Returns the click action of the drag.
	 */
	public MouseClick getMouseClick()
	{
		return this.mouseClick;
	}
	
	/**
	 * Updates the mouseMove action for the drag.
	 * @param mouseMove - Move action to set.
	 * @throws IllegalArgumentException - Throws if move is null.
	 */
	private void setMouseMove(MouseMove mouseMove) throws IllegalArgumentException
	{
		if (mouseMove == null)
			throw new IllegalArgumentException("Cannot set a null MouseMove.");
		
		this.mouseMove = mouseMove;
	}
	
	/**
	 * Returns the move action of the drag.
	 * @return Returns the move action of the drag.
	 */
	public MouseMove getMouseMove()
	{
		return this.mouseMove;
	}
	
	/**
	 * Returns true or false depending if the {@link mouseClick} and {@link mouseMove} start at the same position.
	 * @param mouseClick - The click details where the drag begins.
	 * @param mouseMove - The mouse movement details for the drag action.
	 * @return Returns true if the click and move actions start at the same spot.
	 */
	private boolean isValidDrag(MouseClick mouseClick, MouseMove mouseMove)
	{
		if (mouseClick == null || mouseMove == null)
			return false;
			
		return mouseClick.getPixelClicked().equals(mouseMove.getStartPixel());
	}
	
	@Override 
	public String toString()
	{
		return String.format("Drag: %1s --> %2s", mouseMove.getStartPixel().toString(), mouseMove.getEndPixel().toString());
	}
}
