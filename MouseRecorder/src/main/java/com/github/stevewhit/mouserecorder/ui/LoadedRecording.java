package com.github.stevewhit.mouserecorder.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.zip.DataFormatException;
import com.github.stevewhit.mouserecorder.datahandling.DataIOHandlerUtils;
import com.github.stevewhit.mouserecorder.userinputs.AbstractInputAction;

public class LoadedRecording
{
	/**
	 * The system path location of where to find this loaded recording.
	 */
	private String fileLocationPath;
	
	/**
	 * The queue of imported user input actions loaded from the file.
	 */
	private Queue<AbstractInputAction> inputActionList;
	
	/**
	 * A list of the click zone details imported from the file.
	 */
	private ArrayList<ClickZoneDetails> clickZoneDetails; 
	
	/**
	 * Constructor used to import items from the give file location.
	 * @param fileLocation The system path location of where to find this loaded recording.
	 * @throws IllegalArgumentException Throws if the file location is null.
	 * @throws DataFormatException Throws if there is an issue importing any of the data from the file.
	 * @throws IOException Throws if there is an error opening or reading from the file location.
	 */
	public LoadedRecording(String fileLocation) throws IllegalArgumentException, IOException, DataFormatException
	{
		if (fileLocation == null)
			throw new IllegalArgumentException("File location cannot be null.");
		
		this.fileLocationPath = fileLocation;
		
		loadRecordingInformationFromFile();
	}
	
	/**
	 * Returns the file location system path where this file is located.
	 * @return Returns the file location system path where this file is located. 
	 */
	public String getFileLocationPath()
	{
		return this.fileLocationPath;
	}
	
	/**
	 * Returns the user input action list that was loaded from the given file location.
	 * @return Returns the user input action list that was loaded from the given file location.
	 */
	public Queue<AbstractInputAction> getInputActionList()
	{
		return this.inputActionList;
	}
	
	/**
	 * Returns the click zone details information that was loaded from the given file location.
	 * @return Returns the click zone details information that was loaded from the given file location.
	 */
	public ArrayList<ClickZoneDetails> getClickZoneDetails()
	{
		return this.clickZoneDetails;
	}
	
	/**
	 * Loads all recording click zones and user input actions from the saved file location.
	 * @throws IllegalArgumentException Throws if the file location is null.
	 * @throws DataFormatException Throws if there is an issue importing any of the data from the file.
	 * @throws IOException Throws if there is an error opening or reading from the file location.
	 */
	private void loadRecordingInformationFromFile() throws IllegalArgumentException, IOException, DataFormatException
	{
		final ArrayList<String> importedData = DataIOHandlerUtils.importStringDataFromFile(fileLocationPath);
		
		inputActionList = DataIOHandlerUtils.convertToActionData(new LinkedList<>(DataIOHandlerUtils.extractInputActionStringData(importedData)));
		clickZoneDetails = DataIOHandlerUtils.convertToClickZoneDetailsData(DataIOHandlerUtils.extractClickZoneStringData(importedData));
	}
}
