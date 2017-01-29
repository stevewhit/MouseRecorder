package com.github.stevewhit.mouserecorder.datahandling;

import static org.junit.Assert.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.zip.DataFormatException;
import javax.activation.UnsupportedDataTypeException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.github.stevewhit.mouserecorder.datahandling.ActionDataHandlerUtils;
import com.github.stevewhit.mouserecorder.userinputs.AbstractInputAction;
import com.github.stevewhit.mouserecorder.userinputs.keyboard.KeyboardKeyPress;
import com.github.stevewhit.mouserecorder.userinputs.keyboard.KeyboardKeyRelease;
import com.github.stevewhit.mouserecorder.userinputs.mouse.MouseButtonPress;
import com.github.stevewhit.mouserecorder.userinputs.mouse.MouseButtonRelease;
import com.github.stevewhit.mouserecorder.userinputs.mouse.MouseMove;

public class ActionDataHandlerUtilsTest
{
	File fileLoc;
	String saveLocation;
	LinkedList<String> exportedItems;
	
	@Before
	public void setUp() throws Exception
	{
		saveLocation = "TestDataExportModule.txt";
		fileLoc = new File(saveLocation);
		
		if (fileLoc != null && fileLoc.exists())
		{
			fileLoc.delete();
		}
		
		exportedItems = new LinkedList<String>();
		exportedItems.add("MMOVED:469:76:548171866216160");
		exportedItems.add("KPRESS:17:548174113763558");
		exportedItems.add("KRELEA:17:548174760943927");
		exportedItems.add("MPRESS:3:469:76:1399149:548179641171616");
		exportedItems.add("MRELEA:3:469:76:1399149:548180249079966");
		exportedItems.add("MPRESS:2:469:76:1399149:548181397050226");
		exportedItems.add("MRELEA:2:469:76:1399149:548181525054397");
		exportedItems.add("KPRESS:18:548187439805729");
		
		ActionDataHandlerUtils.exportStringDataToFile(exportedItems, saveLocation);
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
	public void testExportStringDataToFile_NullList() throws IllegalArgumentException, IOException
	{
		ActionDataHandlerUtils.exportStringDataToFile(null, saveLocation);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testExportStringDataToFile_emptyList() throws IllegalArgumentException, IOException
	{
		ActionDataHandlerUtils.exportStringDataToFile(new LinkedList<String>(), saveLocation);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testExportStringDataToFile_NullLocation() throws IllegalArgumentException, IOException
	{
		ActionDataHandlerUtils.exportStringDataToFile(new LinkedList<String>(Arrays.asList("One", "Two", "Three")), null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testExportStringDataToFile_EmptyLocation() throws IllegalArgumentException, IOException
	{
		ActionDataHandlerUtils.exportStringDataToFile(new LinkedList<String>(Arrays.asList("One", "Two", "Three")), "");
	}
	
	@Test
	public void testExportStringDataToFile_valid() throws IllegalArgumentException, IOException
	{
		ActionDataHandlerUtils.exportStringDataToFile(new LinkedList<String>(Arrays.asList("One", "Two", "Three")), saveLocation);
		
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
	
	@Test(expected=IllegalArgumentException.class)
	public void testImportStringDataFromFile_NullFileName() throws IllegalArgumentException, IOException
	{
		ActionDataHandlerUtils.importStringDataFromFile(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testImportStringDataFromFile_EmptyFileName() throws IllegalArgumentException, IOException
	{
		ActionDataHandlerUtils.importStringDataFromFile("");
	}
	
	@Test(expected=IOException.class)
	public void testImportStringDataFromFile_FileDoesntExist() throws IllegalArgumentException, IOException
	{
		ActionDataHandlerUtils.importStringDataFromFile("FileThatDoesntExist.txt");
	}
	
	@Test
	public void testImportStringDataFromFile_Valid() throws IllegalArgumentException, IOException
	{
		ArrayList<String> importedStrings = ActionDataHandlerUtils.importStringDataFromFile(saveLocation);
		
		assertTrue(importedStrings.size() == exportedItems.size());
	}

	//=======================================================
	
	@Test(expected=DataFormatException.class)
	public void testImportActionDataFromFile_NotSupportedID() throws IllegalArgumentException, IOException, DataFormatException
	{
		exportedItems.add("NOTSUPPORTEDID");
		ActionDataHandlerUtils.exportStringDataToFile(exportedItems, saveLocation);
		
		ActionDataHandlerUtils.importActionDataFromFile(saveLocation);
	}

	@Test(expected=DataFormatException.class)
	public void testImportActionDataFromFile_DoesntHaveRequiredFields() throws IllegalArgumentException, IOException, DataFormatException
	{
		exportedItems.add("MPRESS:3:469:76:1399149");
		ActionDataHandlerUtils.exportStringDataToFile(exportedItems, saveLocation);
		
		ActionDataHandlerUtils.importActionDataFromFile(saveLocation);
	}
	
	@Test(expected=DataFormatException.class)
	public void testImportActionDataFromFile_FieldsArentOfExpectedType() throws IllegalArgumentException, IOException, DataFormatException
	{
		exportedItems.add("MPRESS:3:469:76:1399149:MRELEA");
		ActionDataHandlerUtils.exportStringDataToFile(exportedItems, saveLocation);
		
		ActionDataHandlerUtils.importActionDataFromFile(saveLocation);
	}
	
	@Test
	public void testImportActionDataFromFile_Valid() throws IllegalArgumentException, IOException, DataFormatException
	{
		Queue<AbstractInputAction> importedActions = ActionDataHandlerUtils.importActionDataFromFile(saveLocation);
		
		assertTrue(importedActions.size() == 8);
		assertTrue(importedActions.peek() instanceof MouseMove);
		assertTrue(importedActions.toArray()[7] instanceof KeyboardKeyPress);
	}
	
	//=======================================================
	
	@Test(expected=IllegalArgumentException.class)
	public void testCreateInputActionByParsing_nullDetails() throws IllegalArgumentException, UnsupportedDataTypeException, DataFormatException
	{
		ActionDataHandlerUtils.createInputActionByParsing(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCreateInputActionByParsing_EmptyDetails() throws IllegalArgumentException, UnsupportedDataTypeException, DataFormatException
	{
		ActionDataHandlerUtils.createInputActionByParsing("");
	}
	
	@Test(expected=DataFormatException.class)
	public void testCreateInputActionByParsing_UnsupportedID() throws IllegalArgumentException, UnsupportedDataTypeException, DataFormatException
	{
		ActionDataHandlerUtils.createInputActionByParsing("MMOVE:469:76:548171866216160");
	}
	
	@Test(expected=DataFormatException.class)
	public void testCreateInputActionByParsing_DoesntHaveRequiredFields() throws IllegalArgumentException, UnsupportedDataTypeException, DataFormatException
	{
		ActionDataHandlerUtils.createInputActionByParsing("MMOVED:469:548171866216160");
	}
	
	@Test(expected=DataFormatException.class)
	public void testCreateInputActionByParsing_WrongDataTypeInField() throws IllegalArgumentException, UnsupportedDataTypeException, DataFormatException
	{
		ActionDataHandlerUtils.createInputActionByParsing("MMOVED:469:blue:548171866216160");
	}
	
	@Test(expected=DataFormatException.class)
	public void testCreateInputActionByParsing_MissingColon() throws IllegalArgumentException, UnsupportedDataTypeException, DataFormatException
	{
		ActionDataHandlerUtils.createInputActionByParsing("MMOVED469:blue:548171866216160");
	}

	@Test(expected=DataFormatException.class)
	public void testCreateInputActionByParsing_TooManyFields() throws IllegalArgumentException, UnsupportedDataTypeException, DataFormatException
	{
		ActionDataHandlerUtils.createInputActionByParsing("MMOVED:469:76:548171866216160:1234");
	}
	
	@Test
	public void testCreateInputActionByParsing_Valid() throws IllegalArgumentException, UnsupportedDataTypeException, DataFormatException
	{
		assertTrue(ActionDataHandlerUtils.createInputActionByParsing("MMOVED:469:76:548171866216160") instanceof MouseMove);
		assertTrue(ActionDataHandlerUtils.createInputActionByParsing("KPRESS:17:548174113763558") instanceof KeyboardKeyPress);
		assertTrue(ActionDataHandlerUtils.createInputActionByParsing("KRELEA:17:548174760943927") instanceof KeyboardKeyRelease);
		assertTrue(ActionDataHandlerUtils.createInputActionByParsing("MPRESS:3:469:76:1399149:548179641171616") instanceof MouseButtonPress);
		assertTrue(ActionDataHandlerUtils.createInputActionByParsing("MRELEA:3:469:76:1399149:548180249079966") instanceof MouseButtonRelease);
	}
	//=======================================================
	
}































