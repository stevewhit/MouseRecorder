package com.github.stevewhit.mouserecorder.playback;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.IOException;
import java.rmi.AccessException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.zip.DataFormatException;
import javax.activation.UnsupportedDataTypeException;
import javax.activity.InvalidActivityException;
import com.github.stevewhit.mouserecorder.datahandling.ActionDataHandlerUtils;
import com.github.stevewhit.mouserecorder.monitor.PixelColor;
import com.github.stevewhit.mouserecorder.monitor.ScreenUtils;
import com.github.stevewhit.mouserecorder.ui.ClickZoneDetails;
import com.github.stevewhit.mouserecorder.ui.ClickZoneWindow;
import com.github.stevewhit.mouserecorder.ui.PlaybackOptions.TimeQuantifier;
import com.github.stevewhit.mouserecorder.userinputs.AbstractInputAction;
import com.github.stevewhit.mouserecorder.userinputs.InputWaitAction;
import com.github.stevewhit.mouserecorder.userinputs.keyboard.KeyboardKeyPress;
import com.github.stevewhit.mouserecorder.userinputs.keyboard.KeyboardKeyRelease;
import com.github.stevewhit.mouserecorder.userinputs.mouse.MouseButtonPress;
import com.github.stevewhit.mouserecorder.userinputs.mouse.MouseButtonRelease;
import com.github.stevewhit.mouserecorder.userinputs.mouse.MouseMove;

/**
 * Represents a mouse and keyboard player that emulates recorded user input actions.
 * @author Steve Whitmire (swhit114@gmail.com)
 *
 */
public class PlaybackEngine
{
	/**
	 * A list of user input actions with mouse wait statements between each action.
	 */
	private Queue<AbstractInputAction> loadedRecordingWithWaits;
	
	/**
	 * A list of user input actions without mouse wait statements between each action.
	 */
	private Queue<AbstractInputAction> loadedRecordingWithoutWaits; 
	
	/**
	 * A list of the click zones used by this recording.
	 */
	private ArrayList<ClickZoneDetails> loadedRecordingClickZones;
	
	/**
	 * A list of the click zone windows that are currently visible / not disposed.
	 */
	private ArrayList<ClickZoneWindow> visibleClickZoneWindows;
	
	/**
	 * Represents a pixel color that was expected before the mousePressed action but couldn't find it.
	 * This value will be null if the mousePressed color was found before the action.
	 */
	private PixelColor pressedColorToCheckReleasedFor;
	
	/**
	 * Default constructor.
	 */
	public PlaybackEngine(){}
	
	/**
	 * Loads a recording from a file location and serializes and stores the user input actions with wait statements between each action.
	 * @param filePath The file path location of the recording file.
	 * @param useClickZones Enable or disable the use of click zone windows.
	 * @throws AccessException Throws if there is an issue loading the recording properly.
	 */
	public void loadNewRecording(String filePath, boolean useClickZones) throws AccessException
	{
		if (filePath == null)
		{
			throw new IllegalArgumentException("Cannot load recording from a null filepath.");
		}
		
		try
		{
			if (useClickZones)
			{
				// Load recording and click zones.
				loadNewRecording(new LinkedList<>(ActionDataHandlerUtils.importActionDataFromFile(filePath)), ActionDataHandlerUtils.importClickZoneDataFromFile(filePath));
			}
			else
			{
				// Load recording only.
				loadNewRecording(new LinkedList<>(ActionDataHandlerUtils.importActionDataFromFile(filePath)));
			}
		}
		catch(IllegalArgumentException | IOException | DataFormatException ex)
		{
			throw new AccessException("Could not properly load the recording file because ==> " + ex.getMessage());
		}
	}
	
	/**
	 * Serializes and stores the user input actions with wait statements between each action. Click zones are not used.
	 * @param recordedUserInputActions A list of the recorded user input actions without wait statements.
	 * @throws IllegalArgumentException Throws if the recording contains invalid/unsupported input actions.
	 */
	public void loadNewRecording(LinkedList<AbstractInputAction> recordedUserInputActions) throws IllegalArgumentException
	{
		loadNewRecording(recordedUserInputActions, null);
	}
	
	/**
	 * Serializes and stores the user input actions with wait statements between each action. Also, stores the click zones for playback.
	 * @param recordedUserInputActions A list of the recorded user input actions without wait statements.
	 * @param clickZonesForRecording A list of the click zones used by this recording.
	 * @throws IllegalArgumentException Throws if the recording contains invalid/unsupported input actions.
	 */
	public void loadNewRecording(LinkedList<AbstractInputAction> recordedUserInputActions, ArrayList<ClickZoneDetails> clickZonesForRecording) throws IllegalArgumentException
	{
		try
		{
			setRecordingClickZones(clickZonesForRecording);
			setSerializedRecording(recordedUserInputActions);
		}
		catch (IllegalArgumentException | UnsupportedDataTypeException ex)
		{
			loadedRecordingWithWaits = null;
			loadedRecordingWithoutWaits = null;
			loadedRecordingClickZones = null;
			
			throw new IllegalArgumentException("Could not load recording because: ==> " + ex.getMessage());
		}
	}
	
	/**
	 * Plays the loaded recording for a given number of times.
	 * @param checkPixelColorBeforeClick Enable or diable whether the playback should check for pixel colors before any clicking action and force stop if invalid colors are detected.
	 * @param numTimesToRepeat The number of times to repeat the same recording.
	 * @param isCancellationSequencePressed A reference to check whether the cancellation sequence is pressed during playback to stop the playback.
	 * @throws DataFormatException Throws if an invalid action is found during playback.
	 * @throws InvalidActivityException Throws if we're validating pixel colors and an incorrect pixel color is found.
	 * @throws AccessException Throws if there's an issue reading the screen pixels during playback.
	 */
	public void playRecording(boolean checkPixelColorBeforeClick, int numTimesToRepeat, Boolean isCancellationSequencePressed) throws DataFormatException, InvalidActivityException, AccessException
	{
		try
		{
			// If they want to check the pixel color before click, make sure to load the click zone windows.
			if (checkPixelColorBeforeClick)
				showLoadedClickZoneWindows();
			else
				disposeLoadedClickZoneWindows();
			
			// Robot used for controlling the mouse and keyboard.
			Robot robot = new Robot();
			
			// Repeat a certain number of iterations.
			for (int iterationNum = 0; iterationNum < numTimesToRepeat; iterationNum++)
			{
				// Perform each of the loaded actions.
				for (AbstractInputAction action : loadedRecordingWithWaits)
				{
					if(!isCancellationSequencePressed)
					{
						performAction(robot, action, checkPixelColorBeforeClick);
					}
					else
					{
						// Close the loaded click zone windows if any and return.
						disposeLoadedClickZoneWindows();
						return;
					}
				}
			}
		}
		catch(AWTException | IllegalArgumentException ex)
		{
			throw new AccessException("Encountered an issue accessing the screens pixel colors during playback.");
		}
		catch(UnsupportedDataTypeException | DataFormatException ex)
		{
			throw new DataFormatException("Found an invalid action during playback: " + ex.getMessage());
		}
		catch(InvalidActivityException ex)
		{
			throw new InvalidActivityException("Playback stopped because ==> " + ex.getMessage());
		}
		finally
		{
			disposeLoadedClickZoneWindows();
		}
		
		disposeLoadedClickZoneWindows();
	}
	
	/**
	 * Plays the loaded recording for a specific duration.
	 * @param checkPixelColorBeforeClick Enable or diable whether the playback should check for pixel colors before any clicking action and force stop if invalid colors are detected.
	 * @param durationNumericalValue The number value representing the number of 'timequalifiers' 
	 * @param timeQuantifier Indicates Seconds, Minutes, Hours.. etc
	 * @param isCancellationSequencePressed A reference to check whether the cancellation sequence is pressed during playback to stop the playback.
	 * @throws DataFormatException Throws if an invalid action is found during playback.
	 * @throws InvalidActivityException Throws if we're validating pixel colors and an incorrect pixel color is found.
	 * @throws AccessException Throws if there's an issue reading the screen pixels during playback.
	 */
	public void playRecording(boolean checkPixelColorBeforeClick, int durationNumericalValue, TimeQuantifier timeQuantifier, Boolean isCancellationSequencePressed) throws DataFormatException, InvalidActivityException, AccessException
	{
		try
		{
			// If they want to check the pixel color before click, make sure to load the click zone windows.
			if (checkPixelColorBeforeClick)
				showLoadedClickZoneWindows();
			else
				disposeLoadedClickZoneWindows();
			
			// Robot used for controlling the mouse and keyboard.
			Robot robot = new Robot();
			
			// Variable to stop playback.
			boolean continuePlaying = true;
			
			// The time (in milliseconds) that the playback should stop playing.
			long endTimeMilliseconds = getEndTimeInMilliseconds(System.currentTimeMillis(), durationNumericalValue, timeQuantifier);
			
			// Keep repeating and cancel from within.
			while(continuePlaying)
			{
				// Perform each of the loaded actions.
				for (AbstractInputAction action : loadedRecordingWithWaits)
				{
					if (System.currentTimeMillis() < endTimeMilliseconds && !isCancellationSequencePressed)
					{
						performAction(robot, action, checkPixelColorBeforeClick);
					}
					else
					{
						continuePlaying = false;
						break;
					}
				}
			}
		}
		catch(AWTException | IllegalArgumentException ex)
		{
			throw new AccessException("Encountered an issue accessing the screens pixel colors during playback.");
		}
		catch(UnsupportedDataTypeException | DataFormatException ex)
		{
			throw new DataFormatException("Found an invalid action during playback: " + ex.getMessage());
		}
		catch(InvalidActivityException ex)
		{
			throw new InvalidActivityException("Playback stopped because ==> " + ex.getMessage());
		}
		finally
		{
			// Dispose any visible click zone windows.
			disposeLoadedClickZoneWindows();
		}
		
		// Dispose any visible click zone windows.
		disposeLoadedClickZoneWindows();
	}
	
	/**
	 * Performs a pre-recorded user input action.
	 * @param robot The Robot object that is tied into the screen to move the mouse and/or perform keyboard actions.
	 * @param action The AbstractInputAction action that is to be performed.
	 * @param checkPixelColorBeforeClick If true, it validates the pixel color before an item is clicked.
	 * @throws DataFormatException Throws if the action item is invalid.
	 * @throws IllegalArgumentException Throws if the screen utility item can't correctly get the screen pixel color.
	 * @throws AWTException Throws if the screen utility item can't correctly get the screen pixel color.
	 * @throws InvalidActivityException Throws if we're validating pixel colors and an incorrect pixel color is found.
	 * @throws UnsupportedDataTypeException Throws if the action is not supported by the playback engine.
	 */
	private void performAction(Robot robot, AbstractInputAction action, boolean checkPixelColorBeforeClick) throws DataFormatException, IllegalArgumentException, AWTException, InvalidActivityException, UnsupportedDataTypeException
	{
		if (!action.isValidAction())
		{
			throw new DataFormatException("Found invalid action during playback: " + action.toString());
		}
		
		if (action instanceof InputWaitAction)
		{
			// The most accurate wait sleep method without about a 10% error @ 1 nanosecond. 
			// The error decreases tremendously as the wait time increases.
			long waitUntil = System.nanoTime() + ((InputWaitAction)action).getWaitTime();
			while(waitUntil > System.nanoTime())
			{
				;
			}
		}
		else if (action instanceof MouseButtonPress)
		{
			MouseButtonPress castMBP = ((MouseButtonPress)action);
			
			if (!checkPixelColorBeforeClick)
			{
				robot.mousePress(castMBP.getMouseButton().getButtonNum());
				pressedColorToCheckReleasedFor = null;
			}
			else
			{
				// The current screen color BEFORE the mouse press action.
				PixelColor screenRGBColor = ScreenUtils.getScreenPixelColor(castMBP.getLocation().getX(), castMBP.getLocation().getY());
				
				robot.mousePress(castMBP.getMouseButton().getButtonNum());
				
				// Test the pixel color from before the mouse press.
				if (castMBP.getPixelColor().isSimilarColor(screenRGBColor))
				{
					pressedColorToCheckReleasedFor = null;
				}
				else
				{
					// The current screen color AFTER the mouse press action.
					screenRGBColor = ScreenUtils.getScreenPixelColor(castMBP.getLocation().getX(), castMBP.getLocation().getY());
					
					// If the current pixel color is different after the mouse click
					if (!castMBP.getPixelColor().isSimilarColor(screenRGBColor))
					{
						pressedColorToCheckReleasedFor = castMBP.getPixelColor();
					}
					else
					{
						pressedColorToCheckReleasedFor = null;
					}
				}
			}								
		}
		else if (action instanceof MouseButtonRelease)
		{
			if (!checkPixelColorBeforeClick || pressedColorToCheckReleasedFor == null)
			{
				robot.mouseRelease(((MouseButtonRelease)action).getMouseButton().getButtonNum());
			}
			else
			{
				// Mouse Button release action.
				MouseButtonRelease castMBR = (MouseButtonRelease)action;

				// The current screen color before the release action.
				PixelColor screenRGBColor = ScreenUtils.getScreenPixelColor(castMBR.getLocation().getX(), castMBR.getLocation().getY());
				
				// Either way we need to release the mouse button so do it here.
				robot.mouseRelease(castMBR.getMouseButton().getButtonNum());
				
				// If the pixel color value BEFORE the release is still not a similar color to the pressed-mouse color
				if (!screenRGBColor.isSimilarColor(pressedColorToCheckReleasedFor))
				{	
					// Now holds the screen color after the release action.
					screenRGBColor = ScreenUtils.getScreenPixelColor(castMBR.getLocation().getX(), castMBR.getLocation().getY());
					
					// If the pixel color value AFTER the release is still not a similar color to the pressed-mouse color
					if (!screenRGBColor.isSimilarColor(pressedColorToCheckReleasedFor))
					{
						throw new InvalidActivityException("Matching pixel color was not found before mouse press and checked mouse release also: Expected-" + pressedColorToCheckReleasedFor);
					}
				}
				
				pressedColorToCheckReleasedFor = null;
			}
		}
		else if (action instanceof MouseMove)
		{
			robot.mouseMove(((MouseMove)action).getLocation().getX(), ((MouseMove)action).getLocation().getY());
		}
		else if (action instanceof KeyboardKeyPress)
		{
			robot.keyPress(((KeyboardKeyPress)action).getKeyValueInt());
		}
		else if (action instanceof KeyboardKeyRelease)
		{
			robot.keyRelease(((KeyboardKeyRelease)action).getKeyValueInt());
		}
		else 
		{
			throw new UnsupportedDataTypeException("Found unsupported action during playback.");
		}
	}
	
	/**
	 * Opens and shows the click zone windows that were loaded with the recording.
	 */
	private void showLoadedClickZoneWindows()
	{
		// Dispose of any remaining/visible click zone windows.
		disposeLoadedClickZoneWindows();

		// Make sure there are details to create windows out of.
		if (loadedRecordingClickZones != null && !loadedRecordingClickZones.isEmpty())
		{
			visibleClickZoneWindows = new ArrayList<ClickZoneWindow>();
			
			// Foreach of the loaded window details, create a new window for it.
			for (final ClickZoneDetails windowDetails : loadedRecordingClickZones)
			{
				// Create click zone window with the same location and size but with updated transparency.
				visibleClickZoneWindows.add(new ClickZoneWindow(true, true, windowDetails));
			}
		}
	}
	
	/**
	 * Closes and disposes of any visible click zone windows. If none are visible, it will simply return.
	 */
	private void disposeLoadedClickZoneWindows()
	{
		if (visibleClickZoneWindows != null && !visibleClickZoneWindows.isEmpty())
		{
			// Foreach visible window, dispose of it.
			for(ClickZoneWindow visibleWindow : visibleClickZoneWindows)
			{
				visibleWindow.dispose();
			}
		}
		
		visibleClickZoneWindows = null;
	}
	
	/**
	 * Returns an end time in milliseconds by adding the start time with the duration values provided.
	 * @param startTime The start time in milliseconds.
	 * @param durationNumericalValue The number value representing the number of 'timequalifiers' 
	 * @param timeQuantifier Indicates Seconds, Minutes, Hours.. etc
	 * @return Returns the end time in milliseconds by adding the duration values with the start time.
	 * @throws UnsupportedDataTypeException Throws if a time qualifier value is not supported.
	 */
	private long getEndTimeInMilliseconds(long startTime, int durationNumericalValue, TimeQuantifier timeQuantifier) throws UnsupportedDataTypeException
	{
		switch(timeQuantifier)
		{
			case Seconds:
				return startTime + durationNumericalValue * 1000;
			case Minutes:
				return startTime + durationNumericalValue * 1000 * 60;
			case Hours:
				return startTime + durationNumericalValue * 1000 * 60 * 60;
			default:
				throw new UnsupportedDataTypeException("Time Quantifier is not supported.");
		}
	}
	
	/**
	 * Saves the click zone details for playback.
	 * @param recordingClickZones A list of the click zone window details.
	 */
	private void setRecordingClickZones(ArrayList<ClickZoneDetails> recordingClickZones)
	{
		if (recordingClickZones == null)
			this.loadedRecordingClickZones = new ArrayList<ClickZoneDetails>();
		else
			this.loadedRecordingClickZones = recordingClickZones;		
	}
	
	/**
	 * Saves the user input actions with wait statements between each action.
	 * @param recordedUserInputActions A list of the user input actions performed during this recording.
	 * @throws IllegalArgumentException Throws if the list is null or empty.
	 * @throws UnsupportedDataTypeException Throws if the list contains unsuporrted input action types.
	 */
	private void setSerializedRecording(LinkedList<AbstractInputAction> recordedUserInputActions) throws IllegalArgumentException, UnsupportedDataTypeException
	{
		if(recordedUserInputActions == null || recordedUserInputActions.isEmpty())
		{
			throw new IllegalArgumentException("Cannot have a null recorded user input actions for playback.");
		}
		
		// Verify the actions are valid.
		for(AbstractInputAction action : recordedUserInputActions)
		{
			if (!action.isValidAction())
			{
				throw new IllegalArgumentException("Recorded input actions contains invalid actions.");
			}
		}
		
		LinkedList<AbstractInputAction> serializedInputActions = new LinkedList<AbstractInputAction>();
		AbstractInputAction lastAction = null;
		
		// Add InputWaitActions in between each action.
		for(Iterator<AbstractInputAction> actionsIter = recordedUserInputActions.listIterator(); actionsIter.hasNext();)
		{
			AbstractInputAction action = actionsIter.next();
			
			if (action instanceof MouseButtonPress ||
				action instanceof MouseButtonRelease ||
				action instanceof MouseMove ||
				action instanceof KeyboardKeyPress ||
				action instanceof KeyboardKeyRelease)
				{
					if (lastAction != null)
					{
						// Add a wait action that spans from the last action to this action
						serializedInputActions.add(new InputWaitAction(action.getTimeStamp() - lastAction.getTimeStamp(), 0));
					}
					
					serializedInputActions.add(action);
					lastAction = action;
				}
			else
			{
				throw new UnsupportedDataTypeException("Found action type that is not supported.");
			}
		}
		
		this.loadedRecordingWithoutWaits = recordedUserInputActions;
		this.loadedRecordingWithWaits = serializedInputActions;
	}
}




































