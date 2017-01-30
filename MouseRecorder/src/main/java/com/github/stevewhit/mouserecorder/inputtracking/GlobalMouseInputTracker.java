package com.github.stevewhit.mouserecorder.inputtracking;

import java.awt.Robot;
import java.util.Queue;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

public class GlobalMouseInputTracker implements NativeMouseInputListener
{	
	/**
	 * Typical format for click press: ButtonNumber, xCoordinate, yCoordinate, rgbInt, time(in nanoseconds)
	 */
	private final static String MOUSE_PRESS_FORMAT = "MPRESS:%1$d:%2$d:%3$d:%4$s:%5$s";
	
	/**
	 * Typical format for click release: ButtonNumber, xCoordinate, yCoordinate, rgbInt, time(in nanoseconds)
	 */
	private final static String MOUSE_RELEASE_FORMAT = "MRELEA:%1$d:%2$d:%3$d:%4$s:%5$s";
	
	/**
	 * Typical format for click move/drag: xCoordinate, yCoordinate, time(in nanoseconds)
	 */
	private final static String MOUSE_MOVE_FORMAT = "MMOVED:%1$d:%2$d:%3$s";
	
	/**
	 * A reference to the queue that contains the input actions./
	 */
	private Queue<String> actionsQueue;
	
	/**
	 * Constructor that accepts a reference to the actions queue that the generated mouse clicks are added to.
	 * @param actionsQueueRef The queue of actions that clicks are added to.
	 * @throws IllegalArgumentException Throws if the queue is null.
	 */
	protected GlobalMouseInputTracker(Queue<String> actionsQueue) throws IllegalArgumentException
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
	}

	/**
	 * Captures and stores relevant mouse position information into the actionsqueue when a mouse button is pressed.
	 */
	@Override
	public void nativeMousePressed(NativeMouseEvent e)
	{
		final long timeCaptured = System.nanoTime();
		final int xCoord = e.getX();
		final int yCoord = e.getY();
		String rgbColor = null;
		final int buttonNum = e.getButton();
		
		try	
		{
			Robot screenRobot = new Robot();
			rgbColor = String.valueOf(screenRobot.getPixelColor(xCoord, yCoord).getRGB() & 0x00FFFFFF);
		}
		catch (Exception ex) 
		{	
			rgbColor = "INVALIDCOLOR";
		}
		
		final String actionToFormattedString = String.format(MOUSE_PRESS_FORMAT, buttonNum, xCoord, yCoord, rgbColor, timeCaptured);
		
		// Add the formatted string to the queue.
		actionsQueue.add(actionToFormattedString);
	}

	/**
	 * Captures and stores relevant mouse position information into the actionsqueue when a mouse button is released.
	 */
	@Override
	public void nativeMouseReleased(NativeMouseEvent e)
	{
		final long timeCaptured = System.nanoTime();
		final int xCoord = e.getX();
		final int yCoord = e.getY();
		String rgbColor = null;
		final int buttonNum = e.getButton();
		
		try	
		{
			Robot screenRobot = new Robot();
			rgbColor = String.valueOf(screenRobot.getPixelColor(xCoord, yCoord).getRGB() & 0x00FFFFFF);
		}
		catch (Exception ex) 
		{	
			rgbColor = "INVALIDCOLOR";
		}
		
		final String actionToFormattedString = String.format(MOUSE_RELEASE_FORMAT, buttonNum, xCoord, yCoord, rgbColor, timeCaptured);
		
		// Add the formatted string to the queue.
		actionsQueue.add(actionToFormattedString);
	}

	/**
	 * Captures and stores relevant mouse position information into the actionsqueue when a mouse button is pressed and moved *dragged*
	 */
	@Override
	public void nativeMouseDragged(NativeMouseEvent e)
	{
		long timeCaptured = System.nanoTime();
		int xCoord = e.getX();
		int yCoord = e.getY();
		
		String actionToFormattedString = String.format(MOUSE_MOVE_FORMAT, xCoord, yCoord, timeCaptured);
		
		// Add the formatted string to the queue.
		actionsQueue.add(actionToFormattedString);
	}

	/**
	 * Captures and stores relevant mouse position information into the actionsqueue when a mouse is moved.
	 */
	@Override
	public void nativeMouseMoved(NativeMouseEvent e)
	{
		long timeCaptured = System.nanoTime();
		int xCoord = e.getX();
		int yCoord = e.getY();
		
		String actionToFormattedString = String.format(MOUSE_MOVE_FORMAT, xCoord, yCoord, timeCaptured);
		
		// Add the formatted string to the queue.
		actionsQueue.add(actionToFormattedString);
	}
	
	@Override
	public void nativeMouseClicked(NativeMouseEvent e)
	{
		/*
		 * do nothing here..
		 */
	}

}
