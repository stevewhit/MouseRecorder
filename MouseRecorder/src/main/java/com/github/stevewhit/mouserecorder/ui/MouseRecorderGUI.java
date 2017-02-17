package com.github.stevewhit.mouserecorder.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.rmi.AccessException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import javax.activity.InvalidActivityException;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.NumberFormatter;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.SwingDispatchService;
import com.github.stevewhit.mouserecorder.datahandling.DataIOHandlerUtils;
import com.github.stevewhit.mouserecorder.inputtracking.GlobalInputRecorder;
import com.github.stevewhit.mouserecorder.inputtracking.GlobalUserShortcutListener;
import com.github.stevewhit.mouserecorder.playback.PlaybackEngine;
import com.github.stevewhit.mouserecorder.ui.PlaybackOptions.TimeQuantifier;
import com.github.stevewhit.mouserecorder.userinputs.AbstractInputAction;

public class MouseRecorderGUI extends JFrame implements WindowListener, PropertyChangeListener
{
	/**
	 * The constant serial versionUID
	 */
	private static final long serialVersionUID = 1541183202160543102L;
	
	/** Logging */
	private static final Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
	
	/** The input recorder that captures mouse and keyboard inputs. **/
	GlobalInputRecorder inputRecorder = new GlobalInputRecorder();
	LinkedList<String> recordedActions;
	
	/** The listener that fires events if any of the user shortcuts are pressed **/
	GlobalUserShortcutListener userShortcutListener = new GlobalUserShortcutListener();
	
	/** The playback engine that plays recordings after they're loaded **/
	PlaybackEngine playbackPlayer = new PlaybackEngine();

	/* Shortcut key combinations */
	Integer[] recorderStartShortcutKeys = new Integer[]{KeyEvent.VK_ALT, KeyEvent.VK_R};
	Integer[] recorderStopShortcutKeys = new Integer[]{KeyEvent.VK_ALT, KeyEvent.VK_R};
	Integer[] recorderPauseShortcutKeys = new Integer[]{KeyEvent.VK_ALT, KeyEvent.VK_P};
	
	Integer[] playbackStartShortcutKeys = new Integer[]{KeyEvent.VK_ALT, KeyEvent.VK_S};
	Integer[] playbackPauseShortcutKeys = new Integer[]{KeyEvent.VK_ALT, KeyEvent.VK_P};
	Integer[] playbackStopShortcutKeys = new Integer[]{KeyEvent.VK_ALT, KeyEvent.VK_S};
	
	private final static Integer[] ERROR_DURING_PLAYBACK_SHORTCUT_KEYS = new Integer[]{KeyEvent.VK_CONTROL, KeyEvent.VK_ALT, KeyEvent.VK_SHIFT, KeyEvent.VK_E};
	private final static Integer[] FINISHED_PLAYBACK_ITEM_SHORTCUT_KEYS = new Integer[]{KeyEvent.VK_CONTROL, KeyEvent.VK_ALT, KeyEvent.VK_SHIFT, KeyEvent.VK_F};
	
	/** Panels used */
	private JPanel displayPanel;
	private JPanel iconSelectionPanel;
	private JPanel modeViewPanel;
	private JPanel playbackPanel;
	private JPanel recordingPanel;	
	
	/* Global buttons used */
	private JButton iconSelectionChangeViewButton;
	private JButton iconSelectionNewButton;
	
	/* The recording buttons used */
	private JButton iconSelectionRecordPauseResumeButton = new JButton();
	private JButton iconSelectionStopRecordingButton = new JButton();
	private JButton iconSelectionClickZoneButton = new JButton();

	/* The playback buttons used */
	private JButton iconSelectionPlayPauseResumeButton = new JButton();
	private JButton iconSelectionStopPlayButton = new JButton();
	
	private JTextArea recordingPanelRecordedActionsTextArea = new JTextArea();
	
	/* Playback option components used */
	private JPanel playbackOptionsPanel = new JPanel(new GridBagLayout());
	
	private JCheckBox playbackOptionsRepeatNumTimesCheckBox = new JCheckBox("Repeat script cetain number of times (0 for infinite):");
	private JFormattedTextField playbackOptionsRepeatNumTimesTextField;
	
	private JCheckBox playbackOptionsRepeatLengthOfTimeCheckBox = new JCheckBox("Repeat script for length of time:");
	private JFormattedTextField playbackOptionsRepeatLengthOfTimeTextField;
	private JComboBox<String> playbackOptionsRepeatLengthOfTimeComboBox = new JComboBox<String>();
	
	private JCheckBox playbackOptionsIgnoreClickZonesDuringPlaybackCheckBox = new JCheckBox("Ignore click-zones (pixel color checks) during playback.");

	private JCheckBox playbackOptionsStopPlaybackQueueIfFailsCheckBox = new JCheckBox("Stop playback queue if this script fails.");
	
	private JCheckBox playbackOptionsRunAdditionalScriptIfFailsCheckBox = new JCheckBox("Run another script if this script fails.");
	private JTextField playbackOptionsAdditionalScriptToRunIfFailsTextField = new JTextField();
	private JButton playbackOptionsAddAdditionalScriptIfFailsButton = new JButton();
	
	private JButton playbackQueueAddButton = new JButton();
	private JButton playbackQueueRemoveButton = new JButton();
	
	private JPanel queueButtonPanel = new JPanel(new GridBagLayout());
	
	private DefaultListModel<String> playbackQueueRecordsListModel = new DefaultListModel<String>();
	private JList<String> playbackQueueRecordsList;
	
	/** A list of items in the queue **/
	private final static String CURRENT_RECORDING_NAME = "Current Recording";
	private Map<PlaybackOptions, LoadedRecording> loadedPlaybackMap = new LinkedHashMap<PlaybackOptions, LoadedRecording>();
	private Queue<PlaybackQueueOptionsAndRecording> loadedPlaybackOptionsAndRecordings = null;
	private PlaybackQueueOptionsAndRecording currentlyPlayingOptionsAndRecording = null;
	
	private PlaybackOptions currentPlaybackOptions;

	/** Click Zones that were added to the recording **/
	private ArrayList<ClickZoneWindow> addedClickZones = new ArrayList<ClickZoneWindow>();
	
	/** The Recording state of the program */
	private RecordingStates currentRecordingState = RecordingStates.Disable;
	
	private class PlaybackQueueOptionsAndRecording
	{
		public PlaybackOptions options;
		public LoadedRecording recording;
		
		public PlaybackQueueOptionsAndRecording(PlaybackOptions options, LoadedRecording recording)
		{
			this.options = options;
			this.recording = recording;
		}
	}
	
	private enum RecordingStates
	{
		Record,
		Pause,
		Resume,
		Stop,
		Disable,
		Enable
	};
	
	/** The Playback state of the program */
	private PlaybackStates currentPlaybackState = PlaybackStates.Enable;
	
	private enum PlaybackStates
	{
		Play,
		Resume,
		Pause,
		Stop,
		Disable,
		Enable
	};

	public MouseRecorderGUI()
	{
		// Set the title bar and layout.
		setTitle("Mouse Recorder By Steve Whitmire");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		addMenuToFrame();
		
		GridBagConstraints gbc = new GridBagConstraints();
		displayPanel = new JPanel(new GridBagLayout());
		
		/** ADD IconSelectionPanel to displayPanel**/
		iconSelectionPanel = new JPanel(new GridLayout(1, 1));
		iconSelectionPanel.setPreferredSize(new Dimension(1, 80));
		iconSelectionPanel.setMinimumSize(new Dimension(10, 80));
		addIconsToIconsPanel(iconSelectionPanel);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.weighty = 0;
		
		// Add the icon panel to the main display panel and to the frame.
		displayPanel.add(iconSelectionPanel, gbc);
		
		/** ADD ModeViewPanel to displayPanel**/
		modeViewPanel = new JPanel(new GridBagLayout());

		/** Add RecordingPanel to ModeViewPanel **/
		recordingPanel = new JPanel(new GridBagLayout());

		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		addComponentsToRecordingPanel();
		modeViewPanel.add(recordingPanel, gbc);
		
		/** Add playbackPanel to ModeViewPanel  **/
		playbackPanel = new JPanel(new GridBagLayout());

		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		addComponentsToPlaybackPanel();
		modeViewPanel.add(playbackPanel, gbc);
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		displayPanel.add(modeViewPanel, gbc);

		add(displayPanel);
		
		/* Initialize the GUI with the default values. */
		setRecordingState(RecordingStates.Disable);
		setPlaybackState(PlaybackStates.Enable);
		
		// Load the default playback controls.
		showPlaybackControls(new PlaybackOptions(null));
		
		// Adds support for window actions such as: open, close, minimize, maximize.. etc.
		addWindowListener(this);

		// Add a keyboard event listener for the entire frame and all its components
		// To listen for shortcut combinations.
		//KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventPostProcessor(this);
		startUserShortcutListener();
		
		// Disable parent logger and set the desired level.
		logger.setUseParentHandlers(false);
		logger.setLevel(Level.ALL);
		
		// Add our custom formatter to a console handler.
		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.WARNING);
		logger.addHandler(handler);

		/* Swing components must be accessed on the event dispatching
		 * thread; you *MUST* wrap access to Swing components using the
		 * SwingUtilities.invokeLater() or EventQueue.invokeLater() methods.
		 */
		GlobalScreen.setEventDispatcher(new SwingDispatchService());

		setResizable(false);
		setPreferredSize(new Dimension(490, 600));
		pack();
		setVisible(true);
		
	}
	
	private void startUserShortcutListener()
	{
		final PropertyChangeListener frameListener = this;

		// Start the user shortcut listener.
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				userShortcutListener.StartListening(frameListener, getAllShortcutSequences());
			}
		});
	}
	
	private void stopUserShortcutListener()
	{
		// Stop the user shortcut listener.
		userShortcutListener.StopListening();
	}
	
	public int[] toPrimitive(Integer[] integerArray)
	{
		int[] intArray = new int[integerArray.length];
		
		for (int i = 0; i < integerArray.length; i++)
		{
			intArray[i] = integerArray[i].intValue();
		}
		
		return intArray;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		// get the index of the shortcut that was pressed.
		int shortcutIndex = (int) evt.getNewValue();

		Integer[] shortCutPressed = getAllShortcutSequences().get(shortcutIndex);
		
		// Based on which recording/playback state we're in, see if that shortcut is anything we should be worried about.
		if(currentRecordingState != RecordingStates.Disable && currentPlaybackState == PlaybackStates.Disable)
		{
			// If the record shortcut keys are pressed
			// and we're not recording (so either paused or stopped). RECORD
			if (arraysEqual(shortCutPressed, recorderStartShortcutKeys) && currentRecordingState != RecordingStates.Record)
			{
				if (currentRecordingState == RecordingStates.Pause)
					setRecordingState(RecordingStates.Resume);
				else
					setRecordingState(RecordingStates.Record);
			}
			// If the stop shortcut keys are pressed 
			// and we're either recording or paused.. STOP
			else if (arraysEqual(shortCutPressed, recorderStopShortcutKeys) && currentRecordingState != RecordingStates.Stop)
			{
				// Stop recording
				setRecordingState(RecordingStates.Stop);
			}
			// If the pause shortcut keys are pressed
			// and we're recording.. PAUSE
			else if (arraysEqual(shortCutPressed, recorderPauseShortcutKeys) && currentRecordingState == RecordingStates.Record)
			{
				// Pause recording
				setRecordingState(RecordingStates.Pause);
			}
		}
		else if (currentRecordingState == RecordingStates.Disable && currentPlaybackState != PlaybackStates.Disable)
		{
			// If the play shortcut keys are pressed
			// and we're not already playing (So either paused or stopped). PLAY
			if (arraysEqual(shortCutPressed, playbackStartShortcutKeys) && currentPlaybackState != PlaybackStates.Play)
			{
				if (currentPlaybackState == PlaybackStates.Pause)
					setPlaybackState(PlaybackStates.Resume);
				else
					setPlaybackState(PlaybackStates.Play);
			}
			//If the stop shortcut keys are pressed
			// and we're either playing or paused.. STOP
			else if (arraysEqual(shortCutPressed, playbackStopShortcutKeys) && currentPlaybackState != PlaybackStates.Stop)
			{
				// Stop playing
				setPlaybackState(PlaybackStates.Stop);			
			}
			// If the pause shortcut keys are pressed
			// and we're playing.. PAUSE
			else if (arraysEqual(shortCutPressed, playbackPauseShortcutKeys) && currentPlaybackState == PlaybackStates.Play)
			{
				// Pause playing
				setPlaybackState(PlaybackStates.Pause);
			}
			else if (arraysEqual(shortCutPressed, ERROR_DURING_PLAYBACK_SHORTCUT_KEYS) && currentPlaybackState == PlaybackStates.Play)
			{
				performPlaybackFailAction();
			}
			else if (arraysEqual(shortCutPressed, FINISHED_PLAYBACK_ITEM_SHORTCUT_KEYS) && currentPlaybackState == PlaybackStates.Play)
			{
				playNextRecording();
			}
		}
	}
	
	private boolean arraysEqual(Integer[] firstArray, Integer[] secondArray)
	{
		if (firstArray == null && secondArray != null)
			return false;
		
		if (secondArray == null && firstArray != null)
			return false;
		
		if (firstArray.length != secondArray.length)
			return false;
		
		for (int i = 0; i < firstArray.length; i++)
		{
			if (firstArray[i].intValue() != secondArray[i].intValue())
				return false;
		}
		
		return true;
	}
	
	/**
	 * Goes through each of the shortcuts and adds only unique key combinations.
	 * @return
	 */
	private ArrayList<Integer[]> getAllShortcutSequences()
	{
		final ArrayList<Integer[]> shortcuts = new ArrayList<>();
		
		shortcuts.add(recorderStartShortcutKeys);
		shortcuts.add(recorderStopShortcutKeys);
		shortcuts.add(recorderPauseShortcutKeys);

		shortcuts.add(playbackStartShortcutKeys);
		shortcuts.add(playbackStopShortcutKeys);
		shortcuts.add(playbackPauseShortcutKeys);
		
		shortcuts.add(ERROR_DURING_PLAYBACK_SHORTCUT_KEYS);
		shortcuts.add(FINISHED_PLAYBACK_ITEM_SHORTCUT_KEYS);
		
		return shortcuts;
	}
	
	private void addAdditionalScriptIfFailsButtonPressed()
	{
		// Initialize the file chooser
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter("*.txt", "txt"));
		
		// Open dialog and wait for user to save.
		int returnValue = fileChooser.showOpenDialog(this);
		
		// Make sure the user actually saved.
		if (returnValue == JFileChooser.APPROVE_OPTION)
		{
			// Verify the filename ends in .txt
			String fileOpenLocation = fileChooser.getSelectedFile().getPath();
		
			if (!fileOpenLocation.endsWith(".txt"))
			{
				JOptionPane.showMessageDialog(this, "This file is not a valid recording text file.");
				return;
			}
			
			// Add the default options to the queue.
			try
			{
				currentPlaybackOptions.loadedAdditionalScriptIfFails = new LoadedRecording(fileOpenLocation);
				playbackOptionsAdditionalScriptToRunIfFailsTextField.setText(fileOpenLocation);
				playbackOptionsAddAdditionalScriptIfFailsButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/removeAdditionalScriptButton.png")));
			}
			catch (IllegalArgumentException | IOException e)
			{
				JOptionPane.showMessageDialog(this, "There was an error trying to read from the file.");
				return;
			}
			catch(DataFormatException e)
			{
				JOptionPane.showMessageDialog(this, "This file contains invalid or unsupported data types.");
				return;
			}
		}
		// Cancel / X button ==> Don't reset text area.
		else
			return;
	}
	
	private void removeAdditionalScriptIfFailsButtonPressed()
	{
		currentPlaybackOptions.loadedAdditionalScriptIfFails = null;
		playbackOptionsAdditionalScriptToRunIfFailsTextField.setText("");
		playbackOptionsAddAdditionalScriptIfFailsButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/addButton.png")));
	}
	
	private void playbackQueueAddButtonPressed()
	{
		// Initialize the file chooser
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter("*.txt", "txt"));
		
		// Open dialog and wait for user to save.
		int returnValue = fileChooser.showOpenDialog(this);
		
		// Make sure the user actually saved.
		if (returnValue == JFileChooser.APPROVE_OPTION)
		{
			// Verify the filename ends in .txt, if not, add it for them.
			String fileOpenLocation = fileChooser.getSelectedFile().getPath();
		
			if (!fileOpenLocation.endsWith(".txt"))
			{
				JOptionPane.showMessageDialog(this, "This file is not a valid recording text file.");
				return;
			}
			
			// Create default playback options with the file location
			PlaybackOptions defaultOptions = new PlaybackOptions(fileOpenLocation);

			// Add the default options to the queue.
			try
			{
				addPlaybackItemToQueue(defaultOptions);
			}
			catch (IllegalArgumentException | IOException e)
			{
				JOptionPane.showMessageDialog(this, "There was an error trying to read from the file.");
				return;
			}
			catch(DataFormatException e)
			{
				JOptionPane.showMessageDialog(this, "This file contains invalid or unsupported data types.");
				return;
			}
		}
		// Cancel / X button ==> Don't reset text area.
		else
			return;
	}
	
	private void playbackQueueRemoveButtonPressed()
	{
		// Prompts user for for confimation they want to remove,
		// and then removes the item from the queue and the playbackoptions list.
		removeItemFromPlaybackQueue(playbackQueueRecordsList.getSelectedValue(), true);
	}
	
	private void removeItemFromPlaybackQueue(String itemID, boolean showRemovalPrompt)
	{
		// Verify the playback queue isnt null and contains this item id.
		if (playbackQueueRecordsListModel != null && playbackQueueRecordsListModel.contains(itemID))
		{
			if (showRemovalPrompt)
			{
				// If the itemID is "Current Recording" show a different prompt.
				if (itemID != null && itemID.contains(CURRENT_RECORDING_NAME))
				{
					Object[] dialogOptions = {"Save and Continue",
											  "Continue",
										      "Cancel"};
					
					// Prompt user to make sure they want to remove.
					int userAnswer = JOptionPane.showOptionDialog(
							this, 
							"You are about to remove and delete the current recording.\n" +
							"Would you like to save the recorded actions before continuing?",
							"Save Recording",
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.WARNING_MESSAGE,
							null,
							dialogOptions,
							dialogOptions[0]);
		
					// Save and Continue
					if (userAnswer == 0)
					{
						resetRecordingActionsTextArea(false, true, false);
					}
					// Don't Save and continue
					else if (userAnswer == 1)
					{
						resetRecordingActionsTextArea(false, false, false);
					}
					// Cancel options
					else 
					{
						return;
					}
				}
				else
				{
					// Prompt user to make sure they want to remove.
					int userAnswer = JOptionPane.showConfirmDialog(
							this, 
							"Are you sure you want to remove this item from the queue?",
							"Remove",
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE);
		
					// No / Cancel -- don't remove the item.
					if (userAnswer != 0)
					{
						return;
					}
				}
			}
			
			// Remove the item from the viewer queue.
			playbackQueueRecordsListModel.removeElement(itemID);
			
			// Remove the item from the playback map.
			for (Iterator<Map.Entry<PlaybackOptions, LoadedRecording>> mapIter = loadedPlaybackMap.entrySet().iterator(); mapIter.hasNext(); )
			{
				PlaybackOptions nextOptionsItem = mapIter.next().getKey();
				
				if (nextOptionsItem.getID() != null && nextOptionsItem.getID().equals(itemID))
				{
					// Found a match, remove it.
					mapIter.remove();
					break;
				}
			}
			
			// Make sure there are items in the list model, if not, disable playback options and 
			// show the default settings.
			if (playbackQueueRecordsListModel.isEmpty())
			{
				iconSelectionNewButton.setEnabled(false);
				playbackQueueRemoveButton.setEnabled(false);
				
				// Disable the playback options if there are no items. && set the options to default.
				showPlaybackControls(new PlaybackOptions(null));
			}
			else
			{
				// Make sure there is an item selected. If not, default to the first item.
				if (playbackQueueRecordsList.isSelectionEmpty())
				{
					playbackQueueRecordsList.setSelectedIndex(0);
				}
			}
		}
	}
	
	private void addPlaybackItemToQueue(PlaybackOptions playbackOptions) throws IllegalArgumentException, IOException, DataFormatException
	{	
		LoadedRecording loadedRecording = new LoadedRecording(playbackOptions.recordingFileLocation);
		
		// Enable or disable certain options based on if there are even click zones available.
		if (loadedRecording.getClickZoneDetails().size() <= 0)
		{
			playbackOptions.ignoreClickZonesDuringPlaybackChecked = true;
		}

		// Add the item to the playbackQueueList
		loadedPlaybackMap.put(playbackOptions, loadedRecording);

		// Add the item to the viewer
		playbackQueueRecordsListModel.addElement(playbackOptions.getID());
		
		// Select the item in the viewer and update our currently selected playback option.
		playbackQueueRecordsList.setSelectedValue(playbackOptions.getID(), true);
		
		iconSelectionNewButton.setEnabled(true);
		playbackQueueRemoveButton.setEnabled(true);
	}
	
	private void savePlaybackSettings()
	{
		// Save existing settings of our playback options
		if (currentPlaybackOptions != null)
		{
			currentPlaybackOptions.repeatNumTimesChecked = playbackOptionsRepeatNumTimesCheckBox.isSelected();
			currentPlaybackOptions.repeatNumTimesNumericalValue = Integer.valueOf(playbackOptionsRepeatNumTimesTextField.getText());
			
			currentPlaybackOptions.repeatLengthOfTimeChecked = playbackOptionsRepeatLengthOfTimeCheckBox.isSelected();
			currentPlaybackOptions.repeatLengthOfTimeNumericalValue = Integer.valueOf(playbackOptionsRepeatLengthOfTimeTextField.getText());
			currentPlaybackOptions.repeatLengthOfTimeTimeQuantifier = TimeQuantifier.values()[playbackOptionsRepeatLengthOfTimeComboBox.getSelectedIndex()];
		
			currentPlaybackOptions.ignoreClickZonesDuringPlaybackChecked = playbackOptionsIgnoreClickZonesDuringPlaybackCheckBox.isSelected();
			
			currentPlaybackOptions.stopPlaybackQueueIfFailsChecked = playbackOptionsStopPlaybackQueueIfFailsCheckBox.isSelected();
			
			currentPlaybackOptions.runAdditionalScriptIfFailsChecked = playbackOptionsRunAdditionalScriptIfFailsCheckBox.isSelected();
			currentPlaybackOptions.runAdditionalScriptIfFailsFileLocation = playbackOptionsAdditionalScriptToRunIfFailsTextField.getText();		
		}
		else
			currentPlaybackOptions = new PlaybackOptions(null);
	}
	
	private void refreshPlaybackControls()
	{
		showPlaybackControls(currentPlaybackOptions);
	}
	
	private void showPlaybackControls(PlaybackOptions playbackOptions)
	{	
		// Re-enable the playback options panel if nothing is in the queue.
		if (loadedPlaybackMap != null && !loadedPlaybackMap.isEmpty())
		{
			for (Component childComponent : playbackOptionsPanel.getComponents())
			{
				childComponent.setEnabled(true);
			}
			
			savePlaybackSettings();
		}

		currentPlaybackOptions = playbackOptions;
		
		/********************************* Load repeat number of times controls************************************************/
		playbackOptionsRepeatNumTimesCheckBox.setSelected(currentPlaybackOptions.repeatNumTimesChecked);
		playbackOptionsRepeatNumTimesTextField.setText(String.valueOf(currentPlaybackOptions.repeatNumTimesNumericalValue));
		playbackOptionsRepeatNumTimesTextField.setEnabled(playbackOptionsRepeatNumTimesCheckBox.isSelected());
		
		try
		{
			// For some reason i need to commit the edit so the text field formatter will work
			// Correctly..
			playbackOptionsRepeatNumTimesTextField.commitEdit();
		}
		catch (ParseException e1)
		{
			e1.printStackTrace();
		}
	
		/********************************* Load repeat length of time controls************************************************/
		playbackOptionsRepeatLengthOfTimeCheckBox.setSelected(currentPlaybackOptions.repeatLengthOfTimeChecked);
		playbackOptionsRepeatLengthOfTimeTextField.setText(String.valueOf(currentPlaybackOptions.repeatLengthOfTimeNumericalValue));
		playbackOptionsRepeatLengthOfTimeTextField.setEnabled(playbackOptionsRepeatLengthOfTimeCheckBox.isSelected());
		playbackOptionsRepeatLengthOfTimeComboBox.setSelectedIndex(currentPlaybackOptions.repeatLengthOfTimeTimeQuantifier.getComboBoxPosition());
		playbackOptionsRepeatLengthOfTimeComboBox.setEnabled(playbackOptionsRepeatLengthOfTimeCheckBox.isSelected());
		
		try
		{
			// For some reason I need to commit the edit so the textField formatter will work
			// Correctly..
			playbackOptionsRepeatLengthOfTimeTextField.commitEdit();
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		
		/********************************* Load stop playback if fails controls************************************************/
		playbackOptionsStopPlaybackQueueIfFailsCheckBox.setSelected(currentPlaybackOptions.stopPlaybackQueueIfFailsChecked);
		
		/********************************* Load Run other script if fails controls************************************************/
		playbackOptionsRunAdditionalScriptIfFailsCheckBox.setSelected(currentPlaybackOptions.runAdditionalScriptIfFailsChecked);
		playbackOptionsAdditionalScriptToRunIfFailsTextField.setText(currentPlaybackOptions.runAdditionalScriptIfFailsFileLocation);
		playbackOptionsAdditionalScriptToRunIfFailsTextField.setEnabled(playbackOptionsRunAdditionalScriptIfFailsCheckBox.isSelected());
		playbackOptionsAddAdditionalScriptIfFailsButton.setEnabled(playbackOptionsRunAdditionalScriptIfFailsCheckBox.isSelected());
		
		if (playbackOptionsAdditionalScriptToRunIfFailsTextField.getText().isEmpty())
			playbackOptionsAddAdditionalScriptIfFailsButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/addButton.png")));
		else
			playbackOptionsAddAdditionalScriptIfFailsButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/removeAdditionalScriptButton.png")));
		
		/********************************* Ignore click-zones control************************************************/
		playbackOptionsIgnoreClickZonesDuringPlaybackCheckBox.setSelected(currentPlaybackOptions.ignoreClickZonesDuringPlaybackChecked);

		// Now turn on and off certain controls based on what is selected.
		if (playbackOptionsIgnoreClickZonesDuringPlaybackCheckBox.isSelected())
		{
			// disable the fails options.
			playbackOptionsStopPlaybackQueueIfFailsCheckBox.setEnabled(false);
			playbackOptionsStopPlaybackQueueIfFailsCheckBox.setSelected(false);
			
			playbackOptionsAddAdditionalScriptIfFailsButton.setEnabled(false);
			
			playbackOptionsAdditionalScriptToRunIfFailsTextField.setEnabled(false);
			playbackOptionsRunAdditionalScriptIfFailsCheckBox.setEnabled(false);
			playbackOptionsRunAdditionalScriptIfFailsCheckBox.setSelected(false);
		}
		
		
		// Disable the playback options panel if nothing is in the queue.
		if (loadedPlaybackMap == null || loadedPlaybackMap.isEmpty())
		{
			for (Component childComponent : playbackOptionsPanel.getComponents())
			{
				childComponent.setEnabled(false);
			}
		}
	}

	private void refreshPlaybackQueue()
	{
		if (playbackQueueRecordsListModel.isEmpty())
			iconSelectionNewButton.setEnabled(false);
		
		// Clear the queue and re-add the entire playback options list
		playbackQueueRecordsList.clearSelection();
		playbackQueueRecordsListModel.clear();

		// Foreach item in the playback queue, display it in the queue control
		for(PlaybackOptions queueItem : loadedPlaybackMap.keySet())
		{
			playbackQueueRecordsListModel.addElement(queueItem.getID());
		}
		
		// Load the first item in the playback queue if it isn't empty.
		if (!loadedPlaybackMap.isEmpty())
		{
			// By setting the selection it will force an auto-save of the last playback settings
			// and it will populate playback options field.
			playbackQueueRecordsList.setSelectedIndex(0);
		}
		
		// Enable the remove button if items are in the list; otherwise disable.
		playbackQueueRemoveButton.setEnabled(!playbackQueueRecordsListModel.isEmpty());
	}
	
	private void setSelectedQueuedRecord(String nameID)
	{	
		if (nameID == null)
			return;
		
		// Find the matching script file location and select it.
		for (PlaybackOptions queuedOptions : loadedPlaybackMap.keySet())
		{
			if (queuedOptions.getID().equals(nameID))
			{
				playbackQueueRecordsList.setSelectedValue(nameID, true);
				showPlaybackControls(queuedOptions);
			}
		}
	}
	
	private void addMenuToFrame()
	{
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		JMenuItem menuBarFileSaveRecording = new JMenuItem("Save Recording..");
		JMenuItem menuBarFileSaveConfiguration = new JMenuItem("Save configuration..");
		JMenuItem menuBarFileExit = new JMenuItem("Exit");
		
		fileMenu.addMenuListener(new MenuListener()
		{
			
			@Override
			public void menuSelected(MenuEvent e)
			{
				System.out.println("File menu clicked.");
				if (currentRecordingState != RecordingStates.Disable)
				{
					menuBarFileSaveConfiguration.setEnabled(false);
					menuBarFileSaveRecording.setEnabled(iconSelectionNewButton.isEnabled());
				}
				
				else if (currentPlaybackState != PlaybackStates.Disable)
				{
					menuBarFileSaveRecording.setEnabled(false);
					menuBarFileSaveConfiguration.setEnabled(iconSelectionNewButton.isEnabled());
				}
			}
			
			@Override
			public void menuDeselected(MenuEvent e)
			{
			}
			
			@Override
			public void menuCanceled(MenuEvent e)
			{
			}
		});
		
		menuBarFileSaveRecording.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				resetRecordingActionsTextArea(false, true, true);
				refreshPlaybackQueue();
			}
		});
		
		fileMenu.add(menuBarFileSaveRecording);
		
		menuBarFileSaveConfiguration.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				resetPlaybackOptionsAndQueue(false);
			}
		});
		
		fileMenu.add(menuBarFileSaveConfiguration);
		
		menuBarFileExit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (iconSelectionNewButton.isEnabled())
				{
					resetPlaybackOptionsAndQueue(true);
				}
				
				try
				{
					GlobalScreen.unregisterNativeHook();
				}
				catch (NativeHookException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				finalizeAndCloseWindow();
			}
		});
		
		fileMenu.add(menuBarFileExit);

		menuBar.add(fileMenu);
		
		setJMenuBar(menuBar);
	}
	
	private void finalizeAndCloseWindow()
	{
		this.setVisible(false);
		dispose();
	}
	
	private void addComponentsToRecordingPanel()
	{
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		
		recordingPanelRecordedActionsTextArea.setEditable(false);
		recordingPanelRecordedActionsTextArea.setBorder(new TitledBorder("Recorded Actions"));

		recordingPanel.add(new JScrollPane(recordingPanelRecordedActionsTextArea), gbc);	
	}
	
	private void addComponentsToPlaybackPanel()
	{
		GridBagConstraints gbc = new GridBagConstraints();

		playbackOptionsPanel.setBorder(new TitledBorder("Playback Options For: "));

		/********************************************Number of times Option***************************************************************/
		playbackOptionsRepeatNumTimesCheckBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (playbackOptionsRepeatNumTimesCheckBox.isSelected())
				{
					playbackOptionsRepeatLengthOfTimeCheckBox.setSelected(false);
					playbackOptionsRepeatLengthOfTimeTextField.setEnabled(false);
					playbackOptionsRepeatLengthOfTimeComboBox.setEnabled(false);
					playbackOptionsRepeatNumTimesTextField.setEnabled(true);
				}
				else
				{
					playbackOptionsRepeatLengthOfTimeCheckBox.setSelected(true);
					playbackOptionsRepeatLengthOfTimeTextField.setEnabled(true);
					playbackOptionsRepeatLengthOfTimeComboBox.setEnabled(true);
					playbackOptionsRepeatNumTimesTextField.setEnabled(false);
				}
				
			}
		});
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 0;
		playbackOptionsPanel.add(playbackOptionsRepeatNumTimesCheckBox, gbc);
		
		// Create a JFormattedText field but limit the entry to only numbers.
		NumberFormat numFormat= NumberFormat.getNumberInstance();
		numFormat.setGroupingUsed(false);
		NumberFormatter formatter = new NumberFormatter(numFormat);
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(0);
		formatter.setMaximum(9999);
		formatter.setAllowsInvalid(false);
		
		playbackOptionsRepeatNumTimesTextField = new JFormattedTextField(formatter);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 0;
		playbackOptionsPanel.add(playbackOptionsRepeatNumTimesTextField, gbc);

		/********************************************Length of time Option***************************************************************/
		playbackOptionsRepeatLengthOfTimeCheckBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (playbackOptionsRepeatLengthOfTimeCheckBox.isSelected())
				{
					playbackOptionsRepeatNumTimesCheckBox.setSelected(false);
					playbackOptionsRepeatNumTimesTextField.setEnabled(false);
					playbackOptionsRepeatLengthOfTimeTextField.setEnabled(true);
					playbackOptionsRepeatLengthOfTimeComboBox.setEnabled(true);
					
				}
				else
				{
					playbackOptionsRepeatNumTimesCheckBox.setSelected(true);
					playbackOptionsRepeatNumTimesTextField.setEnabled(true);
					playbackOptionsRepeatLengthOfTimeTextField.setEnabled(false);
					playbackOptionsRepeatLengthOfTimeComboBox.setEnabled(false);
				}
			}
		});
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		playbackOptionsPanel.add(playbackOptionsRepeatLengthOfTimeCheckBox, gbc);

		playbackOptionsRepeatLengthOfTimeTextField = new JFormattedTextField(formatter);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		playbackOptionsPanel.add(playbackOptionsRepeatLengthOfTimeTextField, gbc);

		/********************************************Add repeat length of time combo box option***************************************************************/
		// Add all time quantifier values to the combobox.
		for (PlaybackOptions.TimeQuantifier timeValue : PlaybackOptions.TimeQuantifier.values())
			playbackOptionsRepeatLengthOfTimeComboBox.addItem(timeValue.toString());
		
		playbackOptionsRepeatLengthOfTimeComboBox.setSelectedIndex(0);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTHEAST;
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		playbackOptionsPanel.add(playbackOptionsRepeatLengthOfTimeComboBox, gbc);
		
		/********************************************Ignore click zone / pixel color checks during playback**************************************************************/
		playbackOptionsIgnoreClickZonesDuringPlaybackCheckBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				refreshPlaybackControls();
			}
		});

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 1;
		gbc.weighty = 0;
		playbackOptionsPanel.add(playbackOptionsIgnoreClickZonesDuringPlaybackCheckBox, gbc);
		

		/********************************************Stop playback queue option***************************************************************/
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weightx = 1;
		gbc.weighty = 0;
		playbackOptionsPanel.add(playbackOptionsStopPlaybackQueueIfFailsCheckBox, gbc);
		
		/********************************************Run additional scripts if fails option***************************************************************/
		
		playbackOptionsRunAdditionalScriptIfFailsCheckBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (!playbackOptionsRunAdditionalScriptIfFailsCheckBox.isSelected())
				{
					playbackOptionsAddAdditionalScriptIfFailsButton.setEnabled(false);
					playbackOptionsAdditionalScriptToRunIfFailsTextField.setEnabled(false);
				}
				else
				{
					playbackOptionsAddAdditionalScriptIfFailsButton.setEnabled(true);
					playbackOptionsAdditionalScriptToRunIfFailsTextField.setEnabled(true);
				}
			}
		});
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.weightx = 1;
		gbc.weighty = 0;
		playbackOptionsPanel.add(playbackOptionsRunAdditionalScriptIfFailsCheckBox, gbc);
		
		
		playbackOptionsAddAdditionalScriptIfFailsButton.setPreferredSize(new Dimension(45, 25));
		playbackOptionsAddAdditionalScriptIfFailsButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/addButton.png")));
		
		playbackOptionsAddAdditionalScriptIfFailsButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Add script and change icon.
				if (playbackOptionsAdditionalScriptToRunIfFailsTextField.getText().isEmpty())
				{
					addAdditionalScriptIfFailsButtonPressed();
					
				}
				// Remove script and change icon.
				else
				{
					removeAdditionalScriptIfFailsButtonPressed();
				}
				
			}
		});

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTHEAST;
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.weightx = 1;
		gbc.weighty = 0;
		playbackOptionsPanel.add(playbackOptionsAddAdditionalScriptIfFailsButton, gbc);
		
		
		/********************************************Add script if this one fails textbox***************************************************************/
		playbackOptionsAdditionalScriptToRunIfFailsTextField.setEditable(false);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.gridx = 0;
		gbc.insets = new Insets(0, 23, 0, 0);
		gbc.gridy = 5;
		gbc.weightx = 1;
		gbc.weighty = 0;
		playbackOptionsPanel.add(playbackOptionsAdditionalScriptToRunIfFailsTextField, gbc);
		
		// Add the entire playback options panel to the playback panel.
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		playbackPanel.add(playbackOptionsPanel, gbc);
		
		/********************************************Add playback queue to bottom of playback panel***************************************************************/
		
		JPanel playbackQueuePanel = new JPanel(new GridBagLayout());
		playbackQueuePanel.setBorder(new TitledBorder("Playback Queue"));

		/********************************************Queue button panels***************************************************************/
		playbackQueueAddButton.setPreferredSize(new Dimension(56, 27));
		playbackQueueAddButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/addQueueButton.png")));
		
		playbackQueueAddButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				playbackQueueAddButtonPressed();
			}
		});

		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.weighty = 0;
		queueButtonPanel.add(playbackQueueAddButton, gbc);
		
		// Adds a space between the add and remove buttons for formatting.
		JLabel spaceFiller = new JLabel("");
		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 0;
		queueButtonPanel.add(spaceFiller, gbc);

		/***********************************************Queue Remove Button ******************************************/
		playbackQueueRemoveButton.setPreferredSize(new Dimension(59, 28));
		playbackQueueRemoveButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/removeButton.png")));
		
		playbackQueueRemoveButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				playbackQueueRemoveButtonPressed();
			}
		});

		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.weighty = 0;
		queueButtonPanel.add(playbackQueueRemoveButton, gbc);

		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.weighty = 0;
		playbackQueuePanel.add(queueButtonPanel, gbc);
		
		/********************************************Add queue jlist area***************************************************************/
		playbackQueueRecordsList = new JList<String>(playbackQueueRecordsListModel);
		playbackQueueRecordsList.setAutoscrolls(true);
		playbackQueueRecordsList.addListSelectionListener(new  ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				// Check for value adjusting and include necessary check for when clearing the selection.
				if (!e.getValueIsAdjusting() && playbackQueueRecordsList.getSelectedValue() != null)
				{
					System.out.println("New queue value selected: " + playbackQueueRecordsList.getSelectedValue());
					
					setSelectedQueuedRecord(playbackQueueRecordsList.getSelectedValue());
				}
			}
		});
		
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		playbackQueuePanel.add(new JScrollPane(playbackQueueRecordsList), gbc);
		
		// Add the playback queue panel to the playbackPanel
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 1000;
		playbackPanel.add(playbackQueuePanel, gbc);
	}
	
	private void addIconsToIconsPanel(JPanel iconSelectionPanel)
	{
		// Add Change View Button
		iconSelectionChangeViewButton = new JButton("View", new ImageIcon(getClass().getClassLoader().getResource("images/changeIcon.png")));

		iconSelectionChangeViewButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		iconSelectionChangeViewButton.setHorizontalTextPosition(SwingConstants.CENTER);
		
		iconSelectionChangeViewButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (currentRecordingState != RecordingStates.Disable)
				{
					setRecordingState(RecordingStates.Disable);
					setPlaybackState(PlaybackStates.Enable);
				}
				else
				{
					setPlaybackState(PlaybackStates.Disable);
					setRecordingState(RecordingStates.Enable);
				}
			}
		});

		iconSelectionChangeViewButton.setEnabled(true);
		iconSelectionPanel.add(iconSelectionChangeViewButton);
		
		// Add New Button
		iconSelectionNewButton = new JButton("New", new ImageIcon(getClass().getClassLoader().getResource("images/newButton.png")));

		iconSelectionNewButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		iconSelectionNewButton.setHorizontalTextPosition(SwingConstants.CENTER);
		
		iconSelectionNewButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("New Button pressed");
				
				if (currentRecordingState == RecordingStates.Disable)
				{
					resetPlaybackOptionsAndQueue(true);
				}
				else if (currentPlaybackState == PlaybackStates.Disable)
				{
					resetRecordingActionsTextArea(true, true, true);
				}
			}
		});

		iconSelectionNewButton.setEnabled(true);
		iconSelectionPanel.add(iconSelectionNewButton);
		
		// Add Record Button
		iconSelectionRecordPauseResumeButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		iconSelectionRecordPauseResumeButton.setHorizontalTextPosition(SwingConstants.CENTER);
		iconSelectionRecordPauseResumeButton.setToolTipText("<html>Start recording (ALT + R)<p>Pause recording (ALT + P)</html>");
		
		iconSelectionRecordPauseResumeButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (currentRecordingState == RecordingStates.Stop)
				{
					setRecordingState(RecordingStates.Record);
				}
				else if (currentRecordingState == RecordingStates.Record)
				{
					setRecordingState(RecordingStates.Pause);
				}
				else if (currentRecordingState == RecordingStates.Pause)
				{
					setRecordingState(RecordingStates.Resume);
				}
			}
		});

		iconSelectionPanel.add(iconSelectionRecordPauseResumeButton);

		// Add Stop Recording Button
		iconSelectionStopRecordingButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/stopButton.png")));
		iconSelectionStopRecordingButton.setText("Stop");
		iconSelectionStopRecordingButton.setToolTipText("Stop recording (ALT + R)");
		iconSelectionStopRecordingButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		iconSelectionStopRecordingButton.setHorizontalTextPosition(SwingConstants.CENTER);
		
		iconSelectionStopRecordingButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setRecordingState(RecordingStates.Stop);
			}
		});

		iconSelectionPanel.add(iconSelectionStopRecordingButton);

		// Add ClickZone Recording Button
		iconSelectionClickZoneButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		iconSelectionClickZoneButton.setHorizontalTextPosition(SwingConstants.CENTER);
		
		iconSelectionClickZoneButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				addClickZone();
			}
		});

		iconSelectionPanel.add(iconSelectionClickZoneButton);
		
		// Add Separator
		//iconSelectionPanel.add(new JSeparator(SwingConstants.VERTICAL));		
		
		// Add Play Button
		iconSelectionPlayPauseResumeButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		iconSelectionPlayPauseResumeButton.setHorizontalTextPosition(SwingConstants.CENTER);
		iconSelectionPlayPauseResumeButton.setToolTipText("<html>Start playback (ALT + S)<p>Pause playback (ALT + P)</html>");
		
		iconSelectionPlayPauseResumeButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (currentPlaybackState == PlaybackStates.Stop)
				{
					setPlaybackState(PlaybackStates.Play);
				}
				else if (currentPlaybackState == PlaybackStates.Pause)
				{
					setPlaybackState(PlaybackStates.Resume);
				}
				else if (currentPlaybackState == PlaybackStates.Play)
				{
					setPlaybackState(PlaybackStates.Pause);
				}
			}
		});

		iconSelectionPanel.add(iconSelectionPlayPauseResumeButton);
		
		// Add Stop Button
		iconSelectionStopPlayButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/stopButton.png")));
		iconSelectionStopPlayButton.setText("Stop");
		iconSelectionStopPlayButton.setToolTipText("Stop playback (ALT + S)");
		iconSelectionStopPlayButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		iconSelectionStopPlayButton.setHorizontalTextPosition(SwingConstants.CENTER);
		
		iconSelectionStopPlayButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setPlaybackState(PlaybackStates.Stop);
			}
		});

		iconSelectionPanel.add(iconSelectionStopPlayButton);
	}
	
	// be able to:
	// make it invisible + set the location of it + make it moveable(setenable(false))
	private void addClickZone()
	{
		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					// Set a new look and feel so the click-zone window doesn't have a border.
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					
					// Add click zone window with default location and dimensions.
					addedClickZones.add(new ClickZoneWindow(false));
					
					// Reset the look and feel.
					com.jtattoo.plaf.acryl.AcrylLookAndFeel.setTheme("Black", "", "");
					UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
					
				}
				catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Hides all click zone windows from the view.
	 */
	private void hideClickZones()
	{
		if (addedClickZones == null)
			addedClickZones = new ArrayList<ClickZoneWindow>();
		
		for (ClickZoneWindow window : addedClickZones)
		{
			window.setVisible(false);
		}
	}
	
	/**
	 * Makes all click zone windows visible.
	 */
	private void showClickZones()
	{
		if (addedClickZones == null)
			addedClickZones = new ArrayList<ClickZoneWindow>();
		
		for (ClickZoneWindow window : addedClickZones)
		{
			if (window != null && window.isDisplayable())
				window.setVisible(true);
		}
	}
	
	/**
	 * Changes all click zone window transparency values and updates the cached list accordingly.
	 * @param showAsTransparent True for windows to be transparent; otherwise false.
	 * @param lockPlacementAndSizeOfWindows Enables or disables the user's ability to move and resize the windows.
	 */
	private void changeClickZoneTransparency(final boolean showAsTransparent, final boolean lockPlacementAndSizeOfWindows)
	{
		if (addedClickZones == null)
			addedClickZones = new ArrayList<ClickZoneWindow>();
		
		final ArrayList<ClickZoneWindow> visibleWindows = new ArrayList<ClickZoneWindow>();
		
		for (final ClickZoneWindow window : addedClickZones)
		{
			// Make sure the window hasn't been disposed yet.
			if (window != null && window.isDisplayable())
			{
				EventQueue.invokeLater(new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							// Set a new look and feel so the click-zone window doesn't have a border.
							UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
							
							// Create click zone window with the same location and size but with updated transparency.
							visibleWindows.add(new ClickZoneWindow(showAsTransparent, lockPlacementAndSizeOfWindows, window.getLocation(), window.getRectanglePane().getSize()));
							
							// Reset the look and feel.
							com.jtattoo.plaf.acryl.AcrylLookAndFeel.setTheme("Black", "", "");
							UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
							
						}
						catch (ClassNotFoundException | InstantiationException | IllegalAccessException
								| UnsupportedLookAndFeelException e)
						{
							e.printStackTrace();
						}
					}
				});
			}
		}
		
		// Dispose of the old click zones.
		removeAllClickZones();
		
		// Update the list of click zones.
		addedClickZones = visibleWindows;
	}
	
	/**
	 * Disposes of all click zone windows and removes them from the addedClickZone list.
	 */
	private void removeAllClickZones()
	{
		for(Iterator<ClickZoneWindow> windowIter = addedClickZones.listIterator(); windowIter.hasNext();)
		{
			ClickZoneWindow nextWindow = windowIter.next();
			
			// Dispose of the window
			if (nextWindow != null)
			{
				nextWindow.dispose();
			}
			
			// Remove the window from the list.
			windowIter.remove();
		}
		
		addedClickZones = new ArrayList<ClickZoneWindow>();
	}
	
	/**
	 * Returns the non-disposed click zone windows as a list of formatted string representations.
	 * @return Returns the non-disposed click zone windows as a list of formatted string representations.
	 */
	private ArrayList<String> getExportableClickZoneList()
	{
		final ArrayList<String> exportableClickZoneWindows = new ArrayList<String>();

		// Add each of the non-disposed windows as a formatted string.
		for (ClickZoneWindow window : addedClickZones)
		{
			// Make sure the window hasn't been disposed.
			if (window != null && window.isDisplayable())
			{
				exportableClickZoneWindows.add(window.toExportableString());
			}
		}
		
		return exportableClickZoneWindows;
	}
	
	private boolean resetRecordingActionsTextArea(boolean promptUserToSave, boolean showFileChooser, boolean updatePlaybackQueue)
	{
		// Verify there are recorded actions already in the text area first.
		if (!recordingPanelRecordedActionsTextArea.getText().isEmpty())
		{
			boolean saveRecording = true;
			
			if (promptUserToSave)
			{
				// Show dialog
				int userAnswer = JOptionPane.showConfirmDialog(
						this, 
						"Would you like to save the recording before continuing?", 
						"Save Recording",
						JOptionPane.YES_NO_CANCEL_OPTION);
				
				// Yes or no option
				if (userAnswer == JOptionPane.YES_OPTION || userAnswer == JOptionPane.NO_OPTION)
					saveRecording = userAnswer == JOptionPane.YES_OPTION;
				// If the user canceled, return.
				else
					return false;
			}
			
			if (showFileChooser && saveRecording)
			{
				// Initialize the file chooser
				final JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new FileNameExtensionFilter("*.txt", "txt"));
				
				// Open dialog and wait for user to save.
				int returnValue = fileChooser.showSaveDialog(this);
				
				// Make sure the user actually saved.
				if (returnValue == JFileChooser.APPROVE_OPTION)
				{
					// Verify the filename ends in .txt, if not, add it for them.
					String fileSaveLocation = fileChooser.getSelectedFile().getPath();
					
					if (!fileSaveLocation.endsWith(".txt"))
					{
						fileSaveLocation += ".txt";
					}
					
					// Try and save the click zone info + recorded actions to the designated file save location.
					try
					{
						final LinkedList<String> exportData = new LinkedList<String>();
						
						// Add the click zone window data + recorded actions data.
						exportData.addAll(getExportableClickZoneList());
						exportData.addAll(recordedActions);
						
						DataIOHandlerUtils.exportStringDataToFile(exportData, fileSaveLocation);
					}
					catch (IllegalArgumentException | IOException e)
					{
						JOptionPane.showMessageDialog(this, "Error occured trying to save recorded data to: " + fileSaveLocation);
						return false;
					}
					
					// Update the playback queue
					if (updatePlaybackQueue)
					{
						// If "Current Recording" exists in the queue, change the file save location of it so 
						// the user is able to keep the playback options they've already created.
						if(recordNameExistsInQueue(CURRENT_RECORDING_NAME))
						{
							for(PlaybackOptions options : loadedPlaybackMap.keySet())
							{
								if (options.recordingFileLocation != null && options.recordingFileLocation.equals(CURRENT_RECORDING_NAME))
								{
									// Update the playback options and the recording.
									options.recordingFileLocation = fileSaveLocation;
									
									try
									{
										LoadedRecording loadedRecording = new LoadedRecording(fileSaveLocation);
										
										loadedPlaybackMap.replace(options, null, loadedRecording);
										
										// If there aren't any click zones, change the options.
										// If not, don't change anything.
										if (loadedRecording.getClickZoneDetails() == null || loadedRecording.getClickZoneDetails().isEmpty())
											options.ignoreClickZonesDuringPlaybackChecked = true;
										
									}
									catch (IllegalArgumentException | IOException | DataFormatException e)
									{
										JOptionPane.showMessageDialog(this, "There was an issue loading the recorded file.");
									}
								}
							}
						}
						// Else add the location with default options.
						else
						{
							try
							{
								LoadedRecording newLoadedRecording = new LoadedRecording(fileSaveLocation);
								PlaybackOptions newOptions = new PlaybackOptions(fileSaveLocation);
								
								// If there aren't any click zones, change the options.
								if (newLoadedRecording.getClickZoneDetails() == null || newLoadedRecording.getClickZoneDetails().isEmpty())
									newOptions.ignoreClickZonesDuringPlaybackChecked = true;
								
								loadedPlaybackMap.put(newOptions, newLoadedRecording);
							}
							catch (IllegalArgumentException | IOException | DataFormatException e)
							{
								System.out.println("There was an error updating the playback queue with the saved current recording..");
								e.printStackTrace();
							}
						}
					}
				}
				// Cancel / X button ==> Don't reset text area.
				else
					return false;
			}
			
			// Reset text area and recorded actions.
			recordingPanelRecordedActionsTextArea.setText(null);
			recordedActions = new LinkedList<String>();
			
			// Remove all click zone windows.
			removeAllClickZones();

			// Reset all the buttons
			if (currentRecordingState != RecordingStates.Disable)
				setRecordingState(RecordingStates.Enable);
			
			// Remove the "Current Recording" item from the queue.
			if (updatePlaybackQueue && recordNameExistsInQueue(CURRENT_RECORDING_NAME))
			{
				playbackQueueRecordsListModel.removeElement(CURRENT_RECORDING_NAME);
			}
		}
		
		return true;
	}
	
	private void startRecordingActionsSetup()
	{
		// If there are recorded actions already in the text area, prompt user if they
		// want to erase the contents.
		if (!recordingPanelRecordedActionsTextArea.getText().isEmpty())
		{
			this.setAlwaysOnTop(true);
			this.setState(JFrame.NORMAL);
			this.setAlwaysOnTop(false);
			stopUserShortcutListener();

			int userAnswer = JOptionPane.showConfirmDialog(
							this, 
							"You are about to erase the actions that have already been recorded.\n"
							+ "Are you sure you want to continue?", 
							"Overwrite Warning",
							JOptionPane.YES_NO_OPTION);
			
			// YES -- Overwrite
			if (userAnswer == 0)
			{
				recordingPanelRecordedActionsTextArea.setText("");
				recordedActions = new LinkedList<String>();
				startUserShortcutListener();
			}
			// NO -- Cancel and stop recording.
			else
			{
				startUserShortcutListener();
				setRecordingState(RecordingStates.Stop);
				return;
			}
		}
		
		iconSelectionClickZoneButton.setEnabled(false);
		startInputRecorder();
	}
	
	private void startInputRecorder()
	{
		// Minimize window.
		this.setState(JFrame.ICONIFIED);
		
		// Start the input recorder.
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
		        inputRecorder.Record(recordingPanelRecordedActionsTextArea, toPrimitive(recorderStopShortcutKeys));
			}
		});
	}
	
	private void stopInputRecorder()
	{
		inputRecorder.StopRecording();
	}
	
	private void resumeRecordingActions()
	{
		// Minimize window.
		this.setState(JFrame.ICONIFIED);
				
		// It's necessary to save here because by 'pausing' the recorder we are forced
		// to re-start a whole new GlobalInputTracker that doesn't have access to the
		// last recorded data.
		
		// Save the actions that we have so far to cache
		saveRecordedActions();
		
		// Re-start a new input recorder
		startInputRecorder();
	}
	
	private void saveRecordedActions()
	{
		// Add all recorded actions so far to our cached list.
		if (inputRecorder != null && inputRecorder.getLastRecordingValues() != null)
		{
			if (recordedActions == null)
				recordedActions = new LinkedList<String>();
			
			recordedActions.addAll(inputRecorder.getLastRecordingValues());
			inputRecorder.resetLastRecordingValues();
			
			System.out.println("TotalActions: " + recordedActions.size());
		}
	}
	
	private void pauseRecordingActions()
	{
		// Minimize window.
		this.setState(JFrame.NORMAL);
		
		// Stops the global listener from recording
		stopInputRecorder();
	}
	
	private void stopRecordingActions()
	{
		// Verify the input recorder is even running.
		if (inputRecorder != null && inputRecorder.getLastRecordingValues() != null)
		{
			// Stops the global listener from recording
			stopInputRecorder();
			
			// Save the recorded actions so far.
			saveRecordedActions();
			
			// Bring window to the front.
			this.setState(JFrame.NORMAL);
		}
	}
	
	private boolean recordNameExistsInQueue(String recordSaveLocation)
	{
		boolean currentRecordingExists = false;
		
		// Checking if "Current Recording" exists in the list already
		for(PlaybackOptions loadedOptions : loadedPlaybackMap.keySet())
		{
			if (loadedOptions.recordingFileLocation != null && loadedOptions.recordingFileLocation.equals(recordSaveLocation))
				currentRecordingExists = true;
		}
		
		return currentRecordingExists;
	}
	
	private void setRecordingState(RecordingStates recordingState)
	{
		switch (recordingState)
		{
			// Disable the recording buttons.
			case Disable:
				iconSelectionClickZoneButton.setText("Zone");
				iconSelectionClickZoneButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/shapesButton.png")));
				iconSelectionClickZoneButton.setEnabled(false);
				hideClickZones();
				iconSelectionRecordPauseResumeButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/recordButton.png")));
				iconSelectionRecordPauseResumeButton.setText("Record");
				iconSelectionRecordPauseResumeButton.setEnabled(false);
				iconSelectionStopRecordingButton.setEnabled(false);
				iconSelectionChangeViewButton.setEnabled(true);
				iconSelectionNewButton.setEnabled(false);
				recordingPanel.setEnabled(false);
				recordingPanel.setVisible(false);
				this.currentRecordingState = RecordingStates.Disable;
				break;
			//Re-enable the recording buttons
			case Enable:
				iconSelectionClickZoneButton.setText("Zone");
				iconSelectionClickZoneButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/shapesButton.png")));
				iconSelectionClickZoneButton.setEnabled(true);
				showClickZones();
				iconSelectionRecordPauseResumeButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/recordButton.png")));
				iconSelectionRecordPauseResumeButton.setText("Record");
				iconSelectionRecordPauseResumeButton.setEnabled(true);
				iconSelectionStopRecordingButton.setEnabled(false);
				iconSelectionChangeViewButton.setEnabled(true);
				recordingPanel.setEnabled(true);
				recordingPanel.setVisible(true);
				this.currentRecordingState = RecordingStates.Stop;
				iconSelectionNewButton.setEnabled(!recordingPanelRecordedActionsTextArea.getText().isEmpty());
				break;
			// In record mode, show the pause button.
			case Record:
				changeClickZoneTransparency(true, true);
				iconSelectionRecordPauseResumeButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/pauseButton.png")));
				iconSelectionRecordPauseResumeButton.setText("Pause");
				iconSelectionStopRecordingButton.setEnabled(true);
				this.currentRecordingState = RecordingStates.Record;
				iconSelectionChangeViewButton.setEnabled(false);
				iconSelectionNewButton.setEnabled(false);
				startRecordingActionsSetup();
				break;
			// In stopped mode, show the record button and disable the stop button
			case Stop:
				iconSelectionClickZoneButton.setText("Zone");
				iconSelectionClickZoneButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/shapesButton.png")));
				iconSelectionClickZoneButton.setEnabled(true);
				changeClickZoneTransparency(false, true);
				iconSelectionRecordPauseResumeButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/recordButton.png")));
				iconSelectionRecordPauseResumeButton.setText("Record");
				iconSelectionStopRecordingButton.setEnabled(false);
				this.currentRecordingState = RecordingStates.Stop;
				iconSelectionChangeViewButton.setEnabled(true);
				iconSelectionNewButton.setEnabled(true);
				stopRecordingActions();
				break;
			// In pause mode, show the resume button
			case Pause:
				iconSelectionRecordPauseResumeButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/resumeRecordButton.png")));
				iconSelectionRecordPauseResumeButton.setText("Resume");
				iconSelectionStopRecordingButton.setEnabled(true);
				this.currentRecordingState = RecordingStates.Pause;
				pauseRecordingActions();
				break;
			// In resume mode, show the pause button.
			case Resume:
				iconSelectionRecordPauseResumeButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/pauseButton.png")));
				iconSelectionRecordPauseResumeButton.setText("Pause");
				iconSelectionStopRecordingButton.setEnabled(true);
				this.currentRecordingState = RecordingStates.Record;
				resumeRecordingActions();
				break;

			default:
				break;
		}
	}
	
	private void addCurrentRecordingToQueueIfNeeded()
	{
		// if the text area isn't empty, add a "Current Recording" item to the playback queue with 
		// default playback option configuration
		if (!recordingPanelRecordedActionsTextArea.getText().isEmpty())
		{
			if (!recordNameExistsInQueue(CURRENT_RECORDING_NAME))
				loadedPlaybackMap.put(new PlaybackOptions(CURRENT_RECORDING_NAME), null);
		}
		// If the text area is empty, remove the "Current Recording" item from the playback queue.
		else if(recordingPanelRecordedActionsTextArea.getText().isEmpty())
		{
			if (recordNameExistsInQueue(CURRENT_RECORDING_NAME))
			{
				// Find the CURRENT_RECORDING in the playback queue map and remove it.
				for (Iterator<Map.Entry<PlaybackOptions, LoadedRecording>> mapIter = loadedPlaybackMap.entrySet().iterator(); mapIter.hasNext(); )
				{
					PlaybackOptions currOptions = mapIter.next().getKey();
					
					if (currOptions.recordingFileLocation != null && currOptions.recordingFileLocation.equals(CURRENT_RECORDING_NAME))
						mapIter.remove();
				}
			}
		}
	}
	
	private void startPlayingPlaybackQueue()
	{
		if (loadedPlaybackMap == null || loadedPlaybackMap.size() <= 0)
		{
			setPlaybackState(PlaybackStates.Stop);
			return;
		}
		
		// Minimize window.
		this.setState(JFrame.ICONIFIED);
		
		savePlaybackSettings();
		loadedPlaybackOptionsAndRecordings = new LinkedList<>();
		
		// Load the queue with the recordings.
		for (Map.Entry<PlaybackOptions, LoadedRecording> entry : loadedPlaybackMap.entrySet())
		{
			loadedPlaybackOptionsAndRecordings.add(new PlaybackQueueOptionsAndRecording(entry.getKey(), entry.getValue()));
		}
		
		// start the playback player.
		playNextRecording();
	}
	
	private void reenablePanelsAndButtonsAfterPlayback()
	{
		// Enable playback panel
		for (Component childComponent : playbackOptionsPanel.getComponents())
		{
			childComponent.setEnabled(true);
		}
		
		// Enable queue button panel
		for (Component childComponent : queueButtonPanel.getComponents())
		{
			childComponent.setEnabled(true);
		}
		
		if (currentPlaybackOptions == null)
			currentPlaybackOptions = new PlaybackOptions(null);
		
		showPlaybackControls(currentPlaybackOptions);
	}
	
	private void disablePanelsAndButtonsForPlayback()
	{
		// Disable playback panel
		for (Component childComponent : playbackOptionsPanel.getComponents())
		{
			childComponent.setEnabled(false);
		}
		
		// Disable queue button panel
		for (Component childComponent : queueButtonPanel.getComponents())
		{
			childComponent.setEnabled(false);
		}
	}
	
	private void playNextRecording()
	{
		// If there are no items left in the queue stop playback.
		if (loadedPlaybackOptionsAndRecordings == null || loadedPlaybackOptionsAndRecordings.size() <= 0)
		{
			reenablePanelsAndButtonsAfterPlayback();
			showPlaybackControls(currentPlaybackOptions);
			setPlaybackState(PlaybackStates.Stop);

			// Restore window.
			this.setState(JFrame.NORMAL);
			
			return;
		}

		// Get and remove the first item in the queue.
		currentlyPlayingOptionsAndRecording = loadedPlaybackOptionsAndRecordings.poll();
		
		// Select the item and that will force the playback options to update.
		playbackQueueRecordsList.setSelectedValue(currentlyPlayingOptionsAndRecording.options.getID(), true);

		// stop the player if its still playing.
		playbackPlayer.stopPlaybackAndUnload();
		
		// Disable controls
		disablePanelsAndButtonsForPlayback();
		
		try
		{
			LinkedList<AbstractInputAction> actionsToPlay = null;
			ArrayList<ClickZoneDetails> clickZonesToUse = null;
			
			// If we have the Current Recording file.
			if (currentlyPlayingOptionsAndRecording.options.recordingFileLocation.equals(CURRENT_RECORDING_NAME))
			{
				actionsToPlay = (LinkedList<AbstractInputAction>) DataIOHandlerUtils.convertToActionData(new LinkedList<>(new ArrayList<>(recordedActions)));
				clickZonesToUse = DataIOHandlerUtils.convertToClickZoneDetailsData(getExportableClickZoneList());
			}
			else
			{
				actionsToPlay = new LinkedList<>(currentlyPlayingOptionsAndRecording.recording.getInputActionList());
				clickZonesToUse = currentlyPlayingOptionsAndRecording.recording.getClickZoneDetails();
			}
			
			// Load the player with a new recording.
			playbackPlayer.loadNewRecording(actionsToPlay, clickZonesToUse);
			
			if (currentlyPlayingOptionsAndRecording.options.repeatLengthOfTimeChecked)
			{
				playbackPlayer.playLoadedRecording(!currentlyPlayingOptionsAndRecording.options.ignoreClickZonesDuringPlaybackChecked, 
						currentlyPlayingOptionsAndRecording.options.repeatLengthOfTimeNumericalValue, 
						currentlyPlayingOptionsAndRecording.options.repeatLengthOfTimeTimeQuantifier, 
						FINISHED_PLAYBACK_ITEM_SHORTCUT_KEYS, 
						ERROR_DURING_PLAYBACK_SHORTCUT_KEYS);
			}
			else if (currentlyPlayingOptionsAndRecording.options.repeatNumTimesChecked)
			{
				playbackPlayer.playLoadedRecording(!currentlyPlayingOptionsAndRecording.options.ignoreClickZonesDuringPlaybackChecked, 
													currentlyPlayingOptionsAndRecording.options.repeatNumTimesNumericalValue == 0 ? Integer.MAX_VALUE : currentlyPlayingOptionsAndRecording.options.repeatNumTimesNumericalValue, 
													FINISHED_PLAYBACK_ITEM_SHORTCUT_KEYS, 
													ERROR_DURING_PLAYBACK_SHORTCUT_KEYS);
			}
		}
		catch (Exception ex)
		{
			playbackPlayer.stopPlaybackAndUnload();
			JOptionPane.showMessageDialog(this, "ERROR! Stopping playback because there was an error..");
			ex.printStackTrace();
		}
	}
	
	private void performPlaybackFailAction()
	{
		if (playbackPlayer == null)
			return;
		
		playbackPlayer.stopPlaybackAndUnload();
		
		if (currentPlaybackOptions.stopPlaybackQueueIfFailsChecked)
		{
			// Stops the playback player.
			loadedPlaybackOptionsAndRecordings = null;
		}
		
		if (	currentPlaybackOptions.runAdditionalScriptIfFailsChecked && 
				currentPlaybackOptions.loadedAdditionalScriptIfFails != null && 
				currentPlaybackOptions.loadedAdditionalScriptIfFails.getInputActionList() != null)
		{
			// Load the player with the user inputs and click zones.
			playbackPlayer.loadNewRecording(new LinkedList<>(currentPlaybackOptions.loadedAdditionalScriptIfFails.getInputActionList()), 
											currentPlaybackOptions.loadedAdditionalScriptIfFails.getClickZoneDetails());
			
			// Play the script one time.
			try
			{
				System.out.println("Playing additional script because other one failed.");
				setPlaybackState(PlaybackStates.Resume);
				disablePanelsAndButtonsForPlayback();
				playbackPlayer.playLoadedRecording(true, 1, FINISHED_PLAYBACK_ITEM_SHORTCUT_KEYS, ERROR_DURING_PLAYBACK_SHORTCUT_KEYS);
			}
			catch (InvalidActivityException | AccessException | DataFormatException e)
			{
				JOptionPane.showMessageDialog(this, "There was an issue playing the additional script and that caused it to stop.");
			}
		}
		else
			playNextRecording();
	}
	
	private void setPlaybackState(PlaybackStates playbackState)
	{
		switch (playbackState)
		{
			case Disable:
				setPlaybackState(PlaybackStates.Stop);
				iconSelectionPlayPauseResumeButton.setEnabled(false);
				iconSelectionStopPlayButton.setEnabled(false);
				this.currentPlaybackState = PlaybackStates.Disable;
				playbackPanel.setEnabled(false);
				playbackPanel.setVisible(false);
				iconSelectionNewButton.setEnabled(false);
				break;
			case Enable:
				iconSelectionPlayPauseResumeButton.setEnabled(true);
				setPlaybackState(PlaybackStates.Stop);
				playbackPanel.setEnabled(true);
				playbackPanel.setVisible(true);
				addCurrentRecordingToQueueIfNeeded();
				refreshPlaybackQueue();
				iconSelectionNewButton.setEnabled(!playbackQueueRecordsListModel.isEmpty());
				playbackQueueAddButton.setEnabled(true);
				break;
			case Play:
				if (loadedPlaybackMap == null || loadedPlaybackMap.size() <= 0)
				{
					setPlaybackState(PlaybackStates.Stop);
					return;
				}
				
				iconSelectionPlayPauseResumeButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/pauseButton.png")));
				iconSelectionPlayPauseResumeButton.setText("Pause");
				iconSelectionStopPlayButton.setEnabled(true);
				iconSelectionChangeViewButton.setEnabled(false);
				iconSelectionNewButton.setEnabled(false);
				startPlayingPlaybackQueue();
				this.currentPlaybackState = PlaybackStates.Play;
				break;
			case Pause:
				playbackPlayer.pausePlayback();
				iconSelectionPlayPauseResumeButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/playButton.png")));
				iconSelectionPlayPauseResumeButton.setText("Resume");
				iconSelectionStopPlayButton.setEnabled(true);
				this.currentPlaybackState = PlaybackStates.Pause;
				break;
			case Stop:
				playbackPlayer.stopPlaybackAndUnload();
				loadedPlaybackOptionsAndRecordings = null;
				reenablePanelsAndButtonsAfterPlayback();
				this.setState(JFrame.NORMAL);
				iconSelectionPlayPauseResumeButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/playButton.png")));
				iconSelectionPlayPauseResumeButton.setText("Play All");
				iconSelectionStopPlayButton.setEnabled(false);
				iconSelectionChangeViewButton.setEnabled(true);
				iconSelectionNewButton.setEnabled(loadedPlaybackMap != null && !loadedPlaybackMap.isEmpty());
				playbackQueueRemoveButton.setEnabled(loadedPlaybackMap != null && !loadedPlaybackMap.isEmpty());
				this.currentPlaybackState = PlaybackStates.Stop;
				break;
			case Resume:
				this.currentPlaybackState = PlaybackStates.Play;
				iconSelectionChangeViewButton.setEnabled(false);
				iconSelectionNewButton.setEnabled(false);
				playbackPlayer.resumePlayback();
				iconSelectionPlayPauseResumeButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/pauseButton.png")));
				iconSelectionPlayPauseResumeButton.setText("Pause");
				iconSelectionStopPlayButton.setEnabled(true);
				break;
			default:
				break;
		}
	}
	
	private void resetPlaybackOptionsAndQueue(boolean promptUserToSaveConfig)
	{
		System.out.println("Resetting playback options.");
		
		setRecordingState(RecordingStates.Enable);
		setPlaybackState(PlaybackStates.Disable);
		resetRecordingActionsTextArea(true, true, true);
		
		setRecordingState(RecordingStates.Disable);
		setPlaybackState(PlaybackStates.Enable);
		
		if (playbackQueueRecordsListModel.isEmpty())
		{
			loadedPlaybackMap = new LinkedHashMap<PlaybackOptions, LoadedRecording>();
			refreshPlaybackQueue();
			refreshPlaybackControls();
			return;
		}
		
		int userAnswer = JOptionPane.YES_OPTION;
		
		if (promptUserToSaveConfig)
		{
			// Show dialog
			userAnswer = JOptionPane.showConfirmDialog(
					this, 
					"Would you like to save the playback configuration?", 
					"Save Configuration",
					JOptionPane.YES_NO_CANCEL_OPTION);
		}
		
		// Yes
		if (userAnswer == JOptionPane.YES_OPTION)	
		{
			// Initialize the file chooser
			final JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new FileNameExtensionFilter("*.txt", "txt"));
			
			// Open dialog and wait for user to save.
			int returnValue = fileChooser.showSaveDialog(this);
			
			// Make sure the user actually saved.
			if (returnValue == JFileChooser.APPROVE_OPTION)
			{
				// Verify the filename ends in .txt, if not, add it for them.
				String fileSaveLocation = fileChooser.getSelectedFile().getPath();
				
				if (!fileSaveLocation.endsWith(".txt"))
				{
					fileSaveLocation += ".txt";
				}
				
				// Save config stuff here.
				System.out.println("Save config stuff here..");
			}
			
			// Remove all items from the queue
			loadedPlaybackMap = new LinkedHashMap<PlaybackOptions, LoadedRecording>();
			refreshPlaybackQueue();
			refreshPlaybackControls();
			setRecordingState(RecordingStates.Disable);
			setPlaybackState(PlaybackStates.Enable);
		}
		else if (userAnswer == JOptionPane.NO_OPTION)
		{
			// Remove all items from the queue
			loadedPlaybackMap = new LinkedHashMap<PlaybackOptions, LoadedRecording>();
			refreshPlaybackQueue();
			refreshPlaybackControls();
			setRecordingState(RecordingStates.Disable);
			setPlaybackState(PlaybackStates.Enable);
		}
		else 
		{
			return;
		}
	}
	
	/**
	 * Unimplemented
	 *
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	public void windowActivated(WindowEvent e) { /* Do Nothing */ }

	/**
	 * Unimplemented
	 *
	 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	public void windowClosing(WindowEvent e) { /* Do Nothing */ }

	/**
	 * Unimplemented
	 *
	 * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
	 */
	public void windowDeactivated(WindowEvent e) { /* Do Nothing */ }

	/**
	 * Unimplemented
	 *
	 * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
	 */
	public void windowDeiconified(WindowEvent e) { /* Do Nothing */ }

	/**
	 * Unimplemented
	 *
	 * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
	 */
	public void windowIconified(WindowEvent e) { /* Do Nothing */ }
	
	/**
	 * Display information about the native keyboard and mouse along with any
	 * errors that may have occurred.
	 *
	 * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
	 */
	public void windowOpened(WindowEvent e) 
	{
		// Return the focus to the window.
		this.requestFocusInWindow();
	}
	
	/**
	 * Finalize and exit the program.
	 *
	 * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
	 */
	public void windowClosed(WindowEvent e) 
	{
		// Clean up the native hook.
		try 
		{
			GlobalScreen.unregisterNativeHook();
		}
		catch (NativeHookException ex) 
		{
			ex.printStackTrace();
		}
		
		System.runFinalization();
		System.exit(0);
	}

}
