package com.github.stevewhit.mouserecorder.datahandling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.zip.DataFormatException;
import javax.activation.UnsupportedDataTypeException;

import com.github.stevewhit.mouserecorder.monitor.Pixel;
import com.github.stevewhit.mouserecorder.monitor.PixelColor;
import com.github.stevewhit.mouserecorder.monitor.PixelCoordinate2D;
import com.github.stevewhit.mouserecorder.monitor.ScreenUtils;
import com.github.stevewhit.mouserecorder.userinputs.AbstractInputAction;
import com.github.stevewhit.mouserecorder.userinputs.keyboard.AbstractKeyboardInputAction;
import com.github.stevewhit.mouserecorder.userinputs.keyboard.KeyboardKeyPress;
import com.github.stevewhit.mouserecorder.userinputs.keyboard.KeyboardKeyRelease;
import com.github.stevewhit.mouserecorder.userinputs.mouse.AbstractMouseButtonAction;
import com.github.stevewhit.mouserecorder.userinputs.mouse.AbstractMouseInputAction;
import com.github.stevewhit.mouserecorder.userinputs.mouse.MouseButton;
import com.github.stevewhit.mouserecorder.userinputs.mouse.MouseButtonPress;
import com.github.stevewhit.mouserecorder.userinputs.mouse.MouseButtonRelease;
import com.github.stevewhit.mouserecorder.userinputs.mouse.MouseMove;

/**
 * Utility class designed for import and export of user input action data.
 * @author Steve Whitmire (swhit114@gmail.com)
 *
 */
public class ActionDataHandlerUtils
{
	/**
	 * Private constructor to utilize this class as static.
	 */
	private ActionDataHandlerUtils() {}
	
	/**
	 * Export the given list to the desired save location. It utilizes the toString() method on each object to write it to the location.
	 * @param exportableData A List of objects that utilize the toString() method to be exported.
	 * @param saveLocation The desired filesystem save path with the file extension added (*.txt)
	 * @throws IllegalArgumentException Throws if the exportable data is null or empty, or if the save location is null or empty.
	 * @throws IOException Throws if there is an issue writing to the desired save location.
	 */
	public static void exportStringDataToFile(LinkedList<String> exportableData, String saveLocation) throws IllegalArgumentException, IOException
	{
		if (exportableData == null || exportableData.isEmpty())
		{
			// Throw this so they know it isn't just going to create a blank file.
			throw new IllegalArgumentException("Nothing to export in exportable data.");
		}
		
		if (saveLocation == null || saveLocation.isEmpty())
		{
			throw new IllegalArgumentException("Save location cannot be null or empty.");
		}
		
		File file = null;
		FileOutputStream os = null;
		PrintWriter pWriter = null;
		
		try
		{
			file = new File(saveLocation);
			os = new FileOutputStream(file);
			pWriter = new PrintWriter(os);
			
			// Write the contents of the list as a string.
			for (String listItem : exportableData)
			{
				pWriter.write(listItem.toString() + "\n");
			}
		}
		catch (IOException ex)
		{
			throw new IOException("Could not write to desired save location because ==> " + ex.getMessage());
		}
		finally 
		{
			// Clean up
			if (pWriter != null)
			{			
				pWriter.flush();
				pWriter.close();
			}
			if (os != null)
				os.close();
			
			file = null;
		}
	}
	
	/**
	 * Imports all data from a file into an arraylist. Each entry in the list is a horizontal line in the file.
	 * @param fileLocation The system path where the file is stored.
	 * @return Returns the file represented as a list of strings. Each entry in the list is a line in the file.
	 * @throws IllegalArgumentException Throws if the file location is null or empty.
	 * @throws IOException Throws if the file doesn't exist or if there's an issue reading information from the file.
	 */
	public static ArrayList<String> importStringDataFromFile(String fileLocation) throws IllegalArgumentException, IOException
	{
		if (fileLocation == null || fileLocation.isEmpty())
		{
			throw new IllegalArgumentException("File location cannot be null or empty.");
		}
		
		BufferedReader bReader = null;
		FileReader fileReader = null;
		
		// The list that contains all the file text from the file.
		ArrayList<String> fileContents = new ArrayList<String>();
		
		try
		{
			fileReader = new FileReader(fileLocation);
			bReader = new BufferedReader(fileReader);
			
			String lineRead = null;
			
			// Read each line of the file and store it.
			while ((lineRead = bReader.readLine()) != null)
			{
				fileContents.add(lineRead);
			}
		}
		catch (IOException ex)
		{
			throw new IOException("Had trouble reading or accessing contents of the file.");
		}
		finally 
		{
			// Clean up
			if (bReader != null)
			{
				bReader.close();
				bReader = null;
			}
			
			if (fileReader != null)
			{
				fileReader.close();
				fileReader = null;
			}
		}

		return fileContents;
	}
	
	/**
	 * Imports all data from a file into an arraylist of input actions.
	 * @param fileLocation The system path where the file is stored.
	 * @return Returns the file represented as a list of input actions. Each entry in the list is a line in the file.
	 * @throws IllegalArgumentException Throws if the file location is null or empty.
	 * @throws IOException Throws if the file doesn't exist or if there's an issue reading information from the file.
	 * @throws DataFormatException Throws if the data in the file doesn't conform to the pre-existing input action structures.
	 */
	public static Queue<AbstractInputAction> importActionDataFromFile(String fileLocation) throws IllegalArgumentException, IOException, DataFormatException
	{
		// Store the string data in the file in a list.
		ArrayList<String> importedFileData = importStringDataFromFile(fileLocation);
				
		// A queue of processed abstract input actions based on the file data that was read in.
		Queue<AbstractInputAction> processedActionData = new LinkedList<AbstractInputAction>();
		
		// Iterate the file data and convert each line into appropriate abstractuserinputactions.
		for (Iterator<String> fileDataIterator = importedFileData.listIterator(); fileDataIterator.hasNext();)
		{
			String line = fileDataIterator.next();
			
			try
			{
				processedActionData.add(createInputActionByParsing(line));
			}
			catch (UnsupportedDataTypeException | DataFormatException | IllegalArgumentException ex) 
			{
				throw new DataFormatException("Cancelled data import on line " + importedFileData.indexOf(line) + " because ==> "+ ex.getMessage());
			}
		}
		
		return processedActionData;
	}
	
	/**
	 * Creates a user input action by parsing the supplied string for relevant information.
	 * @param inputActionDetails A string detailing what kind of input action to create. All information is expected to be split by the ':' character.
	 * @return Returns the input action equivelant of the input action details string.
	 * @throws IllegalArgumentException Throws if the input action details string is null or empty. 
	 * @throws DataFormatException Throws if there is any kind of problem parsing necessary information to create the input action.
	 * @throws UnsupportedDataTypeException Throws if an unsupported action ID is identified in the string.
	 */
	protected static AbstractInputAction createInputActionByParsing(String inputActionDetails) throws IllegalArgumentException, DataFormatException, UnsupportedDataTypeException
	{
		if (inputActionDetails == null || inputActionDetails.isEmpty())
		{
			throw new IllegalArgumentException("Input action details cannot be null or empty.");
		}
		
		try
		{
			String[] splitLine = inputActionDetails.split(":");
		
			switch (splitLine[0].toUpperCase())
			{
				case "MPRESS":
					return createMouseInputActionByParsing(inputActionDetails);
				case "MRELEA":
					return createMouseInputActionByParsing(inputActionDetails);
				case "MMOVED":
					return createMouseInputActionByParsing(inputActionDetails);
				case "KPRESS":
					return createKeyboardInputActionByParsing(inputActionDetails);
				case "KRELEA":
					return createKeyboardInputActionByParsing(inputActionDetails);
				default:
					throw new UnsupportedDataTypeException("Action ID doesn't represent the supported input action ids: " + splitLine[0]);
			}
		}		
		catch (IndexOutOfBoundsException ex)
		{
			throw new DataFormatException("Could not access necessary information when parsing to create a valid input action.");
		}
		catch(UnsupportedDataTypeException | IllegalArgumentException | DataFormatException ex)
		{
			throw new DataFormatException("Could not create input action because ==> " + ex.getMessage());
		}
	}
	
	/**
	 * Creates and returns a keyboard input action depending on the details provided in the string. 
	 * @param keyboardInputActionDetails The string of details representing a keyboard input action.
	 * @return Returns Returns the equivelant keyboard input action that represents the string provided.
	 * @throws IllegalArgumentException Throws if the keyboard input details string is null or empty. 
	 * @throws DataFormatException Throws if String doesn't have the required fields to be a keyboard input action. 
	 * @throws UnsupportedDataTypeException Throws if the id portion of the details string doesn't match the supported keyboard input actions.
	 */
	private static AbstractKeyboardInputAction createKeyboardInputActionByParsing(String keyboardInputActionDetails) throws IllegalArgumentException, DataFormatException, UnsupportedDataTypeException
	{
		if (keyboardInputActionDetails == null || keyboardInputActionDetails.isEmpty())
		{
			throw new IllegalArgumentException("Key input action details cannot be null or empty.");
		}
		
		try
		{
			String[] splitLine = keyboardInputActionDetails.split(":");
		
			if (splitLine[0].toUpperCase().equals("KPRESS") || splitLine[0].toUpperCase().equals("KRELEA"))
			{
				return createKeyboardActionByParsing(keyboardInputActionDetails);
			}
			else 
			{
				throw new UnsupportedDataTypeException("Action ID doesn't represent the supported keyboard input action ids: " + splitLine[0]);
			}
		}		
		catch (IndexOutOfBoundsException ex)
		{
			throw new DataFormatException("Keyboard input details string doesn't have any details to parse.");
		}
		catch(UnsupportedDataTypeException | IllegalArgumentException | DataFormatException ex)
		{
			throw new UnsupportedDataTypeException("Could not create Keyboard input action because ==> " + ex.getMessage());
		}
	}	

	/**
	 * Creates and returns a keyboard action depending on the details provided in the string. 
	 * @param keyActionDetails The string of details representing a keyboard key input action.
	 * @return Returns Returns the equivelant key input action that represents the string provided.
	 * @throws IllegalArgumentException Throws if the key input details string is null or empty. 
	 * @throws DataFormatException Throws if String doesn't have the required fields to be a key input action. 
	 * @throws UnsupportedDataTypeException Throws if the id portion of the details string doesn't match the supported key input actions.
	 */
	private static AbstractKeyboardInputAction createKeyboardActionByParsing(String keyActionDetails) throws IllegalArgumentException, DataFormatException, UnsupportedDataTypeException
	{
		if (keyActionDetails == null || keyActionDetails.isEmpty())
		{
			throw new IllegalArgumentException("Key action details cannot be null or empty.");
		}
		
		try
		{
			String[] splitLine = keyActionDetails.split(":");
		
			// If the split line doesn't contain the necessary number of items don't bother trying to parse it.
			if (splitLine.length != 3)
			{
				throw new DataFormatException("Key action details don't contain the proper number of details.");
			}
			
			// If the ID isn't a valid key input action ID
			if (!splitLine[0].toUpperCase().equals("KPRESS") && !splitLine[0].toUpperCase().equals("KRELEA"))
			{
				throw new UnsupportedDataTypeException("String ID doesn't represent a supported key action: " + splitLine[0]);
			}
			
			// Parsing necessary information.
			int key = Integer.valueOf(splitLine[1]);
			long timeNS = Long.valueOf(splitLine[2]);
				
			if (splitLine[0].toUpperCase().equals("KPRESS"))
			{
				return new KeyboardKeyPress(key, timeNS);
			}
			else if (splitLine[0].toUpperCase().equals("KRELEA"))
			{
				return new KeyboardKeyRelease(key, timeNS);
			}
			else 
			{
				throw new UnsupportedDataTypeException("String ID doesn't represent a supported key action: " + splitLine[0]);
			}
		}		
		catch (IndexOutOfBoundsException | DataFormatException ex)
		{
			throw new DataFormatException("String doesn't have the required fields to be a mouse moved action.");
		}
		catch(UnsupportedDataTypeException | IllegalArgumentException ex)
		{
			throw new UnsupportedDataTypeException("Could not create Mouse moved action because ==> " + ex.getMessage());
		}
	}
	
	/**
	 * Creates and returns a mouse input action depending on the details provided in the string. 
	 * @param mouseInputActionDetails The string of details representing a mouse input action.
	 * @return Returns Returns the equivelant mouse input action that represents the string provided.
	 * @throws IllegalArgumentException Throws if the mouse input details string is null or empty. 
	 * @throws DataFormatException Throws if String doesn't have the required fields to be a mouse input action. 
	 * @throws UnsupportedDataTypeException Throws if the id portion of the details string doesn't match the supported mouse input actions.
	 */
	private static AbstractMouseInputAction createMouseInputActionByParsing(String mouseInputActionDetails) throws IllegalArgumentException, DataFormatException, UnsupportedDataTypeException
	{
		if (mouseInputActionDetails == null || mouseInputActionDetails.isEmpty())
		{
			throw new IllegalArgumentException("Mouse input action details cannot be null or empty.");
		}
		
		try
		{
			String[] splitLine = mouseInputActionDetails.split(":");
		
			if (splitLine[0].toUpperCase().equals("MPRESS"))
			{
				return createMouseButtonActionByParsing(mouseInputActionDetails);
			}
			else if (splitLine[0].toUpperCase().equals("MRELEA"))
			{
				return createMouseButtonActionByParsing(mouseInputActionDetails);
			}
			else if (splitLine[0].toUpperCase().equals("MMOVED"))
			{
				return createMouseMovedActionByParsing(mouseInputActionDetails);
			}
			else 
			{
				throw new UnsupportedDataTypeException("Action ID doesn't represent the supported mouse input action ids: " + splitLine[0]);
			}
		}		
		catch (IndexOutOfBoundsException ex)
		{
			throw new DataFormatException("Mouse input details string doesn't have any details to parse.");
		}
		catch(UnsupportedDataTypeException | IllegalArgumentException | DataFormatException ex)
		{
			throw new UnsupportedDataTypeException("Could not create Mouse input action because ==> " + ex.getMessage());
		}
	}
	
	/**
	 * Creates and returns a mouse moved action depending on the details provided in the string. 
	 * @param mouseMovedActionDetails The string of details representing a mouse moved action.
	 * @return Returns the equivelant mouse moved action that represents the string provided.
	 * @throws IllegalArgumentException Throws if the mouse moved details string is null or empty. 
	 * @throws DataFormatException Throws if String doesn't have the required fields to be a mouse moved action. 
	 * @throws UnsupportedDataTypeException Throws if the id portion of the details string doesn't match the supported mouse moved actions.
	 */
	private static AbstractMouseInputAction createMouseMovedActionByParsing(String mouseMovedActionDetails) throws IllegalArgumentException, DataFormatException, UnsupportedDataTypeException
	{
		if (mouseMovedActionDetails == null || mouseMovedActionDetails.isEmpty())
		{
			throw new IllegalArgumentException("Mouse moved action details cannot be null or empty.");
		}
		
		try
		{
			String[] splitLine = mouseMovedActionDetails.split(":");
		
			// If the split line doesn't contain the necessary number of items don't bother trying to parse it.
			if (splitLine.length != 4)
			{
				throw new DataFormatException("Mouse moved action details don't contain the proper number of details.");
			}
			
			// If the ID isn't a valid mouse button ID
			if (!splitLine[0].toUpperCase().equals("MMOVED"))
			{
				throw new UnsupportedDataTypeException("String ID doesn't represent a supported mouse moved action: " + splitLine[0]);
			}
			
			// Parsing necessary information.
			// Limit x and y coords from [0:screenWidth]
			int xCoord = Integer.valueOf(splitLine[1]);
			xCoord = (int) (xCoord < 0 ? 0 : 
							xCoord > ScreenUtils.getScreenDimensions().getWidth() ? ScreenUtils.getScreenDimensions().getWidth() : 
							xCoord);
			
			int yCoord = Integer.valueOf(splitLine[2]);
			yCoord = (int) (yCoord < 0 ? 0 : 
							yCoord > ScreenUtils.getScreenDimensions().getWidth() ? ScreenUtils.getScreenDimensions().getWidth() : 
							yCoord);
			
			long timeNS = Long.valueOf(splitLine[3]);
						
			return new MouseMove(new PixelCoordinate2D(xCoord, yCoord), timeNS);
			
		}		
		catch (IndexOutOfBoundsException | DataFormatException ex)
		{
			throw new DataFormatException("String doesn't have the required fields to be a mouse moved action.");
		}
		catch(UnsupportedDataTypeException | IllegalArgumentException ex)
		{
			throw new UnsupportedDataTypeException("Could not create Mouse moved action because ==> " + ex.getMessage());
		}
	}
	
	/**
	 * Creates and returns a mouse button action depending on the details provided in the string. 
	 * @param mouseInputActionDetails The string of details representing a mouse button action.
	 * @return Returns the equivelant mouse button action that represents the string provided.
	 * @throws IllegalArgumentException Throws if the mouse button details string is null or empty. 
	 * @throws DataFormatException Throws if String doesn't have the required fields to be a mouse button action. 
	 * @throws UnsupportedDataTypeException Throws if the id portion of the details string doesn't match the supported button actions.
	 */
	private static AbstractMouseButtonAction createMouseButtonActionByParsing(String mouseButtonActionDetails) throws IllegalArgumentException, DataFormatException, UnsupportedDataTypeException
	{
		if (mouseButtonActionDetails == null || mouseButtonActionDetails.isEmpty())
		{
			throw new IllegalArgumentException("Mouse button action details cannot be null or empty.");
		}
		
		try
		{
			String[] splitLine = mouseButtonActionDetails.split(":");
		
			// If the split line doesn't contain the necessary number of items don't bother trying to parse it.
			if (splitLine.length != 6)
			{
				throw new DataFormatException("Mouse button action details don't contain the proper number of details.");
			}
			
			// If the ID isn't a valid mouse button ID
			if (!splitLine[0].toUpperCase().equals("MPRESS") && !splitLine[0].toUpperCase().equals("MRELEA"))
			{
				throw new UnsupportedDataTypeException("String ID doesn't represent a supported mouse button action: " + splitLine[0]);
			}
			
			// Parsing necessary information.
			MouseButton buttonPressed = determineMouseButton(splitLine[1]);
			int xCoord = Integer.valueOf(splitLine[2]);
			int yCoord = Integer.valueOf(splitLine[3]);
			int rgbVal = Integer.valueOf(splitLine[4]);
			long timeNS = Long.valueOf(splitLine[5]);
			
			// Button Press
			if (splitLine[0].toUpperCase().equals("MPRESS"))
			{
				return new MouseButtonPress(buttonPressed, new Pixel(new PixelColor(rgbVal), new PixelCoordinate2D(xCoord, yCoord)), timeNS);
			}
			// Button Release
			else if (splitLine[0].toUpperCase().equals("MRELEA"))
			{
				return new MouseButtonRelease(buttonPressed, new Pixel(new PixelColor(rgbVal), new PixelCoordinate2D(xCoord, yCoord)), timeNS);
			}
			else 
			{
				throw new UnsupportedDataTypeException("String ID doesn't represent the supported mouse button actions: " + splitLine[0]);
			}
			
		}		
		catch (IndexOutOfBoundsException | DataFormatException ex)
		{
			throw new DataFormatException("String doesn't have the required fields to be a mouse button action.");
		}
		catch(UnsupportedDataTypeException | IllegalArgumentException ex)
		{
			throw new UnsupportedDataTypeException("Could not create Mouse button action because ==> " + ex.getMessage());
		}
	}
	
	/**
	 * Determines which mouse button the string represents and returns it as a MouseButton object.
	 * @param mouseButtonString The string that represents the mouse button/
	 * @return Returns the MouseButton object represented by this string.
	 * @throws UnsupportedDataTypeException Throws if this button isn't supported yet.
	 * @throws IllegalArgumentException Throws if the mouse button string is null.
	 */
	private static MouseButton determineMouseButton(String mouseButtonString) throws UnsupportedDataTypeException, IllegalArgumentException
	{
		if (mouseButtonString == null)
		{
			throw new IllegalArgumentException("Mousebutton string cannot be null.");
		}
		
		MouseButton mouseButton = 	mouseButtonString.equals("1") ? MouseButton.Left : 
									mouseButtonString.equals("2") ? MouseButton.Right : 
									mouseButtonString.equals("3") ? MouseButton.ScrollWheel : 
									null;

		if (mouseButton == null) 
		{
			throw new UnsupportedDataTypeException("String contains an unsupported mouse button: " + mouseButtonString);
		}
		
		return mouseButton;
	}
	
}