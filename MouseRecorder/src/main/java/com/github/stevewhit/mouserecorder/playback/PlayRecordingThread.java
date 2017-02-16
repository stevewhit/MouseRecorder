package com.github.stevewhit.mouserecorder.playback;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.rmi.AccessException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.zip.DataFormatException;
import javax.activation.UnsupportedDataTypeException;
import javax.activity.InvalidActivityException;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import com.github.stevewhit.mouserecorder.monitor.PixelColor;
import com.github.stevewhit.mouserecorder.monitor.PixelCoordinate2D;
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

class PlayRecordingThread implements Runnable
{
	/**
	 * Playback states enum describing the current state of the playback thread.
	 * @author Steve Whitmire (swhit114@gmail.com)
	 *
	 */
	public enum PlayThreadStates
	{
		New,
		Running,
		Paused,
		Stopped,
		FinishedSuccessfully,
		FinishedWithErrors
	};
	
	/**
	 * The thread that the recording runs on.
	 */
	private Thread workerThread;
	
	/**
	 * Enables or disables pausing of the thread.
	 */
	private volatile boolean pauseThread = false;
	
	/**
	 * The current state of the thread.
	 */
	private volatile PlayThreadStates threadState = PlayThreadStates.New;
	
	/**
	 * A list of user input actions with mouse wait statements between each action.
	 */
	private Queue<AbstractInputAction> loadedRecordingWithWaits;
		
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
	 * Enable or disable whether the playback should check for pixel color before any clicking action and force stop if invalid colors are detected.
	 */
	private Boolean checkPixelColorBeforeClick = null;
	
	/**
	 * The number of times to repeat the same recording.
	 */
	private Integer numTimesToRepeat = null;
	
	/**
	 * The number value representing the number of 'timequantifiers' when playing for a certain duration.
	 */
	private Integer durationNumericalValue = null;
	
	/**
	 * Indicates 'seconds, minutes, hours'.. etc. to play the recording.
	 */
	private TimeQuantifier durationTimeQuantifier = null;
	
	/**
	 * The system time in milliseconds that the playback thread was paused.
	 */
	private Long pauseStartMilliseconds;
	
	/**
	 * The system time in milliseconds that the playback thread was resumed.
	 */
	private Long pauseEndMilliseconds;
	
	/**
	 * The keyboard key sequence that if pressed, notifies the main GUI that playback is completed.
	 * Note: There is another listener thread listening for these keys
	 */
	private Integer[] FINISHED_PLAYBACK_ITEM_SHORTCUT_KEYS;
	
	/**
	 * The keyboard key sequence that if pressed, notifies the playback player that there was an issue during playback.
	 * Note: There is another listener thread listening for these keys so by pressing them,
	 * it forces the playback to perform a given action.
	 */
	private Integer[] ERROR_DURING_PLAYBACK_SHORTCUT_KEYS;
	
	/**
	 * Constructor that takes pre-verified click zones and input actions and loads them for playing.
	 * @param loadedRecordingClickZones The click zones that are used during playback.. if any.
	 * @param loadedRecordingWithWaits The input actions to playback with wait statements.
	 */
	public PlayRecordingThread(ArrayList<ClickZoneDetails> loadedRecordingClickZones, Queue<AbstractInputAction> loadedRecordingWithWaits)
	{
		// These items were already verified during the loading process of the playback engine.
		this.loadedRecordingClickZones = loadedRecordingClickZones;
		this.loadedRecordingWithWaits = loadedRecordingWithWaits;
	}

	@Override
	public void run()
	{
		// Reset timers for pausing options.
		pauseStartMilliseconds = null;
		pauseEndMilliseconds = null;
		
		try
		{
			threadState = PlayThreadStates.Running;
			
			// Determine which recording algorithm to run based of what is initialized.
			if (durationNumericalValue == null && durationTimeQuantifier == null)
				playRecordingNumberTimes();
			else if (numTimesToRepeat == null)
				playRecordingDuration();
			else 
			{
				throw new IllegalStateException("Thread was initialized with invalid values.");
			}
			
			// Only perform the key sequence if the thread wasn't stopped already.
			if (threadState != PlayThreadStates.Stopped)
			{
				pressCertainKeySequence(FINISHED_PLAYBACK_ITEM_SHORTCUT_KEYS);
				
				// Finished successfully
				threadState = PlayThreadStates.FinishedSuccessfully;
			}
		}
		catch(AccessException | DataFormatException | InvalidActivityException ex)
		{
			threadState = PlayThreadStates.FinishedWithErrors;
			System.out.println("There was an error during playback ==> " + ex.getMessage());
			
			// Notify the GUI there was an error during playback.
			pressCertainKeySequence(ERROR_DURING_PLAYBACK_SHORTCUT_KEYS);
		}
	}
	
	/**
	 * Stop the thread from playing.
	 */
	public void stop()
	{
		threadState = PlayThreadStates.Stopped;
		//workerThread.interrupt();
	}
	
	/**
	 * Have the thread wait in the current location until this is changed.
	 */
	public void pause()
	{
		pauseStartMilliseconds = System.currentTimeMillis();
		
		threadState = PlayThreadStates.Paused;
		
		this.pauseThread = true;
	}
	
	/**
	 * Resume thread from the paused location.
	 */
	public void resume()
	{
		pauseEndMilliseconds = System.currentTimeMillis();
		
		threadState = PlayThreadStates.Running;
		this.pauseThread = false;
		
		if (workerThread != null)
			workerThread.interrupt();
	}
	
	/**
	 * Returns the current state of the thread.
	 * @return
	 */
	public PlayThreadStates getState()
	{
		return this.threadState;
	}
	
	/**
	 * Starts the thread and repeats the loaded user input actions a certain number of times.
	 * @param startImmediately Enable or diable starting immediately. If not, thread will be in the paused position.
	 * @param checkPixelColorBeforeClick Enable or disable using click zones.
	 * @param numTimesToRepeat The integer value of the number of times to repeat the same recording.
	 * @param FINISHED_PLAYBACK_ITEM_SHORTCUT_KEYS The shortcut keys that should be pressed when playback is completed.
	 * @param ERROR_DURING_PLAYBACK_SHORTCUT_KEYS The shortcut keys used to notify the GUI and error occured during playback.
	 */
	public void start(boolean startImmediately, boolean checkPixelColorBeforeClick, int numTimesToRepeat, Integer[] FINISHED_PLAYBACK_ITEM_SHORTCUT_KEYS, Integer[] ERROR_DURING_PLAYBACK_SHORTCUT_KEYS)
	{
		this.FINISHED_PLAYBACK_ITEM_SHORTCUT_KEYS = FINISHED_PLAYBACK_ITEM_SHORTCUT_KEYS;
		this.ERROR_DURING_PLAYBACK_SHORTCUT_KEYS = ERROR_DURING_PLAYBACK_SHORTCUT_KEYS;
		
		this.checkPixelColorBeforeClick = checkPixelColorBeforeClick;
		this.numTimesToRepeat = numTimesToRepeat;
		this.durationNumericalValue = null;
		this.durationTimeQuantifier = null;
		
		this.pauseThread = !startImmediately;
		workerThread = new Thread(this);
		workerThread.start();	
	}
	
	/**
	 * Starts the thread and repeats the loaded user input actions for a certain amount of time.
	 * @param startImmediately Enable or diable starting immediately. If not, thread will be in the paused position.
	 * @param checkPixelColorBeforeClick Enable or disable using click zones.
	 * @param durationNumericalValue The number value representing the number of 'timequalifiers' 
	 * @param timeQuantifier Indicates Seconds, Minutes, Hours.. etc
	 * @param FINISHED_PLAYBACK_ITEM_SHORTCUT_KEYS The shortcut keys that should be pressed when playback is completed.
	 * @param ERROR_DURING_PLAYBACK_SHORTCUT_KEYS The shortcut keys that should be pressed if there is an issue during playback.
	 */
	public void start(boolean startImmediately, boolean checkPixelColorBeforeClick, int durationNumericalValue, TimeQuantifier durationTimeQuantifier, Integer[] FINISHED_PLAYBACK_ITEM_SHORTCUT_KEYS, Integer[] ERROR_DURING_PLAYBACK_SHORTCUT_KEYS)
	{
		this.FINISHED_PLAYBACK_ITEM_SHORTCUT_KEYS = FINISHED_PLAYBACK_ITEM_SHORTCUT_KEYS;
		this.ERROR_DURING_PLAYBACK_SHORTCUT_KEYS = ERROR_DURING_PLAYBACK_SHORTCUT_KEYS;
		
		this.checkPixelColorBeforeClick = checkPixelColorBeforeClick;
		this.numTimesToRepeat = null;
		this.durationNumericalValue = durationNumericalValue;
		this.durationTimeQuantifier = durationTimeQuantifier;
		
		this.pauseThread = !startImmediately;
		workerThread = new Thread(this);
		workerThread.start();	
	}
	
	/**
	 * Forces a robot to press the supplied key sequence
	 */
	private void pressCertainKeySequence(Integer[] keySequenceToPress)
	{
		try
		{
			Robot robot = new Robot();
			for(Integer key : keySequenceToPress)
			{
				robot.keyPress(key);
			}
			for(Integer key : keySequenceToPress)
			{
				robot.keyRelease(key);
			}
		}
		catch (AWTException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Plays the loaded recording for a given number of times.
	 * @throws AccessException Throws if there's an issue reading the screen pixels during playback.
	 * @throws DataFormatException Throws if an invalid action is found during playback.
	 * @throws InvalidActivityException Throws if we're validating pixel colors and an incorrect pixel color is found.
	 */
	private void playRecordingNumberTimes() throws AccessException, DataFormatException, InvalidActivityException
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
				System.out.println("Running (" + iterationNum + "/" + numTimesToRepeat + ")");
				
				// Perform each of the loaded actions.
				for (AbstractInputAction action : loadedRecordingWithWaits)
				{
					// If the thread is paused, do nothing.
					while(pauseThread)
					{
						if (threadState == PlayThreadStates.Stopped)
							return;
						
						threadState = PlayThreadStates.Paused;
						
						try
						{
							Thread.sleep(100);
						}
						catch (InterruptedException e)
						{
							// Eat the exception.
						}
					}
					
					if (threadState == PlayThreadStates.Stopped)
						return;
					
					// If the thread is running, perform the action.
					performAction(robot, action, checkPixelColorBeforeClick);
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
	 * @throws AccessException Throws if there's an issue reading the screen pixels during playback.
	 * @throws DataFormatException Throws if an invalid action is found during playback.
	 * @throws InvalidActivityException Throws if we're validating pixel colors and an incorrect pixel color is found.
	 */
	private void playRecordingDuration() throws AccessException, DataFormatException, InvalidActivityException
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
			long endTimeMilliseconds = getEndTimeInMilliseconds(System.currentTimeMillis(), durationNumericalValue, durationTimeQuantifier);
			
			// Keep repeating and cancel from within.
			while(continuePlaying)
			{
				// Perform each of the loaded actions.
				for (AbstractInputAction action : loadedRecordingWithWaits)
				{
					// If the thread is paused, sit here.
					while(pauseThread)
					{
						if (threadState == PlayThreadStates.Stopped)
							return;
						
						threadState = PlayThreadStates.Paused;
						
						try
						{
							Thread.sleep(100);
						}
						catch (InterruptedException e)
						{
							// Eat the exception.
						}
					}
					
					if (threadState == PlayThreadStates.Stopped)
						return;
					
					// If the user paused the playback update the end time 
					if (pauseStartMilliseconds != null && pauseEndMilliseconds != null)
					{
						endTimeMilliseconds += pauseEndMilliseconds - pauseStartMilliseconds;
						pauseEndMilliseconds = null;
						pauseStartMilliseconds = null;
					}
					
					if (System.currentTimeMillis() < endTimeMilliseconds)
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
		
			// Check if the mousepress occurs within one of the given geometries.			
			if (!checkPixelColorBeforeClick || !pointIsInsideLoadedClickZones(castMBP.getLocation()))
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
	 * Checks to see if a given point falls inside atleast one of the click zone windows.
	 * @param locationToCheck The coordinate point location that is tested.
	 * @return Returns true if the point location falls inside atleast one of the click zone windows; otherwise false.
	 */
	private boolean pointIsInsideLoadedClickZones(PixelCoordinate2D locationToCheck)
	{
		if (loadedRecordingClickZones == null || loadedRecordingClickZones.isEmpty())
		{
			return false;
		}
		else
		{
			// Check each click zone window to see if the point lies within atleast one of them.
			for(ClickZoneDetails loadedZoneDetail : loadedRecordingClickZones)
			{
				final Rectangle clickZoneRect = new Rectangle();
				clickZoneRect.setLocation(loadedZoneDetail.getWindowLocation());
				clickZoneRect.setSize(loadedZoneDetail.getWindowDimensions());
				
				// If the point location falls inside the click zone rectangle, return true.
				if (clickZoneRect.contains(new Point(locationToCheck.getX(), locationToCheck.getY())))
				{
					return true;
				}
			}
		}
		
		return false;
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
				try
				{
					// Set the look and feel so the border is invisible
		        	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		        	
		        	// Create click zone window with the same location and size but with updated transparency.
					visibleClickZoneWindows.add(new ClickZoneWindow(true, true, windowDetails));
					
					// Reset the look and feel to the original value.
					UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
				}
				catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException | ClassCastException ex)
				{
					ex.printStackTrace();
				}
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
}
























