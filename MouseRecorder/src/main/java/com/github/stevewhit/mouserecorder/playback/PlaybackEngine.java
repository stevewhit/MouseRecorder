package com.github.stevewhit.mouserecorder.playback;

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
import com.github.stevewhit.mouserecorder.playback.PlayRecordingThread.PlayThreadStates;
import com.github.stevewhit.mouserecorder.ui.ClickZoneDetails;
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
	 * The most recent playback thread that is used to play a recording.
	 */
	private PlayRecordingThread currentPlaybackThread;
	
	/**
	 * Default constructor.
	 */
	public PlaybackEngine() {}
	
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
	 * @param playbackStopShortcutKeys The shortcut keys that are being listened for to stop the playback player.
	 * @param ERROR_DURING_PLAYBACK_SHORTCUT_KEYS The shortcut keys that should be pressed if there is an issue during playback.
	 * @throws DataFormatException Throws if an invalid action is found during playback.
	 * @throws InvalidActivityException Throws if we're validating pixel colors and an incorrect pixel color is found.
	 * @throws AccessException Throws if there's an issue reading the screen pixels during playback.
	 */
	public void playLoadedRecording(boolean checkPixelColorBeforeClick, int numTimesToRepeat, Integer[] playbackStopShortcutKeys, Integer[] ERROR_DURING_PLAYBACK_SHORTCUT_KEYS) throws DataFormatException, InvalidActivityException, AccessException
	{
		if (currentPlaybackThread != null)
			currentPlaybackThread.stop();
		
		// Create a new thread and start it.
		currentPlaybackThread = new PlayRecordingThread(loadedRecordingClickZones, loadedRecordingWithWaits);
		currentPlaybackThread.start(true, checkPixelColorBeforeClick, numTimesToRepeat, playbackStopShortcutKeys, ERROR_DURING_PLAYBACK_SHORTCUT_KEYS);
	}
	
	/**
	 * Plays the loaded recording for a specific duration.
	 * @param checkPixelColorBeforeClick Enable or diable whether the playback should check for pixel colors before any clicking action and force stop if invalid colors are detected.
	 * @param durationNumericalValue The number value representing the number of 'timequalifiers' 
	 * @param timeQuantifier Indicates Seconds, Minutes, Hours.. etc
	 * @param playbackStopShortcutKeys The shortcut keys that are being listened for to stop the playback player.
	 * @param ERROR_DURING_PLAYBACK_SHORTCUT_KEYS The shortcut keys that should be pressed if there is an issue during playback.
	 * @return Returns true if the player is finished playing the recording.
	 * @throws DataFormatException Throws if an invalid action is found during playback.
	 * @throws InvalidActivityException Throws if we're validating pixel colors and an incorrect pixel color is found.
	 * @throws AccessException Throws if there's an issue reading the screen pixels during playback.
	 */
	public void playLoadedRecording(boolean checkPixelColorBeforeClick, int durationNumericalValue, TimeQuantifier timeQuantifier, Integer[] playbackStopShortcutKeys, Integer[] ERROR_DURING_PLAYBACK_SHORTCUT_KEYS) throws DataFormatException, InvalidActivityException, AccessException
	{
		if (currentPlaybackThread != null)
			currentPlaybackThread.stop();
		
		currentPlaybackThread = null;
		
		// Create a new thread and start it.
		currentPlaybackThread = new PlayRecordingThread(loadedRecordingClickZones, loadedRecordingWithWaits);
		currentPlaybackThread.start(true, checkPixelColorBeforeClick, durationNumericalValue, timeQuantifier, playbackStopShortcutKeys, ERROR_DURING_PLAYBACK_SHORTCUT_KEYS);
	}
	
	/**
	 * Pauses the current recording playback. It can be resumed later.
	 * @throws IllegalStateException Throws if the recording hasn't even been started.
	 */
	public void pausePlayback() throws IllegalStateException
	{
		if (currentPlaybackThread != null && currentPlaybackThread.getState() == PlayThreadStates.Running)
			currentPlaybackThread.pause();
		else
			throw new IllegalStateException("Cannot pause a recording that isn't even playing or is null.");
	}
	
	/**
	 * Resumes the playback from a paused state.
	 * @throws IllegalStateException Throws if the recording hasn't been paused.
	 */
	public void resumePlayback()
	{
		if (currentPlaybackThread != null && currentPlaybackThread.getState() == PlayThreadStates.Paused)
			currentPlaybackThread.resume();
		else
			throw new IllegalStateException("Cannot resume a recording that isn't even paused or is null.");
	}
	
	/**
	 * Stops the current recording playback and unloads the recording.
	 */
	public void stopPlaybackAndUnload()
	{
		if (currentPlaybackThread != null)
		{
			currentPlaybackThread.stop();
			currentPlaybackThread = null;
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




































