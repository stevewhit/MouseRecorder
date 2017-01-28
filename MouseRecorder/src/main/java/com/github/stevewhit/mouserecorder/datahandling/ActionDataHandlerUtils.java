package com.github.stevewhit.mouserecorder.dataprocessing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

public class DataExportModule
{
	/**
	 * Private constructor to utilize this class as static.
	 */
	private DataExportModule() {}
	
	/**
	 * Export the given list to the desired save location. It utilizes the toString() method on each object to write it to the location.
	 * @param exportableData A List of objects that utilize the toString() method to be exported.
	 * @param saveLocation The desired filesystem save path with the file extension added (*.txt)
	 * @throws IllegalArgumentException Throws if the exportable data is null or empty, or if the save location is null or empty.
	 * @throws IOException Throws if there is an issue writing to the desired save location.
	 */
	public static <T> void exportListToFile(LinkedList<T> exportableData, String saveLocation) throws IllegalArgumentException, IOException
	{
		if (exportableData == null || exportableData.isEmpty())
		{
			// Throw this so they know it isn't just going to create a blank file.
			throw new IllegalArgumentException("Nothing to export..");
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
			for (T listItem : exportableData)
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
			pWriter.flush();
			pWriter.close();
			os.close();
			file = null;
		}
	}
}
