package com.github.stevewhit.mouserecorder.dataprocessing;

import static org.junit.Assert.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DataExportModuleTest
{
	File fileLoc;
	String saveLocation;
	
	@Before
	public void setUp() throws Exception
	{
		saveLocation = "TestDataExportModule.txt";
		fileLoc = new File(saveLocation);
		
		if (fileLoc != null && fileLoc.exists())
		{
			fileLoc.delete();
		}
	}
	
	@After
	public void tearDown() throws Exception
	{
		if (fileLoc != null && fileLoc.exists())
		{
			fileLoc.delete();
		}
		
		fileLoc = null;
		saveLocation = null;
	}
	
	//=======================================================
	
	@Test(expected=IllegalArgumentException.class)
	public void testExportListToFile_NullList() throws IllegalArgumentException, IOException
	{
		DataExportModule.exportListToFile(null, saveLocation);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testExportListToFile_emptyList() throws IllegalArgumentException, IOException
	{
		DataExportModule.exportListToFile(new LinkedList<>(), saveLocation);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testExportListToFileNullLocation() throws IllegalArgumentException, IOException
	{
		DataExportModule.exportListToFile(new LinkedList<String>(Arrays.asList("One", "Two", "Three")), null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testExportListToFileEmptyLocation() throws IllegalArgumentException, IOException
	{
		DataExportModule.exportListToFile(new LinkedList<String>(Arrays.asList("One", "Two", "Three")), "");
	}
	
	@Test
	public void testExportListToFile_valid() throws IllegalArgumentException, IOException
	{
		DataExportModule.exportListToFile(new LinkedList<String>(Arrays.asList("One", "Two", "Three")), saveLocation);
		
		assertTrue(fileLoc.exists());
		
		int count = 0;
		
		FileInputStream fStream = new FileInputStream(saveLocation);
		BufferedReader bReader = new BufferedReader(new InputStreamReader(fStream));
		
		while ((bReader.readLine() != null))
		{
			count++;
		}
		
		assertTrue(count == 3);
		
		fStream.close();
		fStream = null;
		bReader.close();
		bReader = null;
	}
	
	//=======================================================
	
}




































