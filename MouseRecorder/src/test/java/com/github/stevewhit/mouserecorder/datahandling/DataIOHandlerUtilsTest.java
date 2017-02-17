package com.github.stevewhit.mouserecorder.datahandling;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.Point;
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
import com.github.stevewhit.mouserecorder.datahandling.DataIOHandlerUtils;
import com.github.stevewhit.mouserecorder.ui.ClickZoneDetails;
import com.github.stevewhit.mouserecorder.userinputs.AbstractInputAction;
import com.github.stevewhit.mouserecorder.userinputs.keyboard.KeyboardKeyPress;
import com.github.stevewhit.mouserecorder.userinputs.keyboard.KeyboardKeyRelease;
import com.github.stevewhit.mouserecorder.userinputs.mouse.MouseButtonPress;
import com.github.stevewhit.mouserecorder.userinputs.mouse.MouseButtonRelease;
import com.github.stevewhit.mouserecorder.userinputs.mouse.MouseMove;

public class DataIOHandlerUtilsTest
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
		exportedItems.add("CZONEE:698:413:50:50");
		exportedItems.add("CZONEE:754:415:100:100");
		exportedItems.add("CZONEE:860:417:150:150");
		exportedItems.add("CZONEE:1015:418:200:200");
		
		exportedItems.add("MMOVED:469:76:548171866216160");
		exportedItems.add("KPRESS:17:548174113763558");
		exportedItems.add("KRELEA:17:548174760943927");
		exportedItems.add("MPRESS:3:469:76:1399149:548179641171616");
		exportedItems.add("MRELEA:3:469:76:1399149:548180249079966");
		exportedItems.add("MPRESS:2:469:76:1399149:548181397050226");
		exportedItems.add("MRELEA:2:469:76:1399149:548181525054397");
		exportedItems.add("KPRESS:18:548187439805729");
		
		exportedItems.add("CZONEE:1222:417:250:250");
		
		DataIOHandlerUtils.exportStringDataToFile(exportedItems, saveLocation);
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
		DataIOHandlerUtils.exportStringDataToFile(null, saveLocation);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testExportStringDataToFile_emptyList() throws IllegalArgumentException, IOException
	{
		DataIOHandlerUtils.exportStringDataToFile(new LinkedList<String>(), saveLocation);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testExportStringDataToFile_NullLocation() throws IllegalArgumentException, IOException
	{
		DataIOHandlerUtils.exportStringDataToFile(new LinkedList<String>(Arrays.asList("One", "Two", "Three")), null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testExportStringDataToFile_EmptyLocation() throws IllegalArgumentException, IOException
	{
		DataIOHandlerUtils.exportStringDataToFile(new LinkedList<String>(Arrays.asList("One", "Two", "Three")), "");
	}
	
	@Test
	public void testExportStringDataToFile_valid() throws IllegalArgumentException, IOException
	{
		DataIOHandlerUtils.exportStringDataToFile(new LinkedList<String>(Arrays.asList("One", "Two", "Three")), saveLocation);
		
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
		DataIOHandlerUtils.importStringDataFromFile(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testImportStringDataFromFile_EmptyFileName() throws IllegalArgumentException, IOException
	{
		DataIOHandlerUtils.importStringDataFromFile("");
	}
	
	@Test(expected=IOException.class)
	public void testImportStringDataFromFile_FileDoesntExist() throws IllegalArgumentException, IOException
	{
		DataIOHandlerUtils.importStringDataFromFile("FileThatDoesntExist.txt");
	}
	
	@Test
	public void testImportStringDataFromFile_Valid() throws IllegalArgumentException, IOException
	{
		ArrayList<String> importedStrings = DataIOHandlerUtils.importStringDataFromFile(saveLocation);
		
		assertTrue(importedStrings.size() == exportedItems.size());
	}

	//=======================================================

	@Test(expected=IllegalArgumentException.class)
	public void testExtractClickZonestringData_nullList() throws IllegalArgumentException, DataFormatException
	{
		DataIOHandlerUtils.extractClickZoneStringData(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testExtractClickZonestringData_EmptyList() throws IllegalArgumentException, DataFormatException
	{
		DataIOHandlerUtils.extractClickZoneStringData(new ArrayList<String>());
	}
	
	@Test(expected=DataFormatException.class)
	public void testExtractClickZonestringData_ContainsInvalidInfo() throws IllegalArgumentException, DataFormatException
	{
		exportedItems.add(null);
		DataIOHandlerUtils.extractClickZoneStringData(new ArrayList<>(exportedItems));
	}

	@Test
	public void testExtractClickZonestringData_Valid() throws IllegalArgumentException, DataFormatException
	{
		ArrayList<String> expectedList = new ArrayList<>();
		expectedList.add("CZONEE:698:413:50:50");
		expectedList.add("CZONEE:754:415:100:100");
		expectedList.add("CZONEE:860:417:150:150");
		expectedList.add("CZONEE:1015:418:200:200");
		expectedList.add("CZONEE:1222:417:250:250");
		
		assertEquals(expectedList, DataIOHandlerUtils.extractClickZoneStringData(new ArrayList<>(exportedItems)));
		assertTrue(DataIOHandlerUtils.extractClickZoneStringData(new ArrayList<>(exportedItems)).size() == 5);
	}
	
	//=======================================================
	
	@Test(expected=IllegalArgumentException.class)
	public void testExtractInputActionStringData_nullList() throws IllegalArgumentException, DataFormatException
	{
		DataIOHandlerUtils.extractInputActionStringData(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testExtractInputActionStringData_EmptyList() throws IllegalArgumentException, DataFormatException
	{
		DataIOHandlerUtils.extractInputActionStringData(new ArrayList<String>());
	}
	
	@Test(expected=DataFormatException.class)
	public void testExtractInputActionStringData_ContainsInvalidInfo() throws IllegalArgumentException, DataFormatException
	{
		exportedItems.add(null);
		DataIOHandlerUtils.extractInputActionStringData(new ArrayList<>(exportedItems));
	}

	@Test
	public void testExtractInputActionStringData_Valid() throws IllegalArgumentException, DataFormatException
	{
		assertTrue(DataIOHandlerUtils.extractInputActionStringData(new ArrayList<>(exportedItems)).size() == 8);
	}
	
	//=======================================================
	
	@Test(expected=IllegalArgumentException.class)
	public void testConvertToActionData_nullList() throws IllegalArgumentException, DataFormatException
	{
		DataIOHandlerUtils.convertToActionData(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConvertToActionData_emptyList() throws IllegalArgumentException, DataFormatException
	{
		DataIOHandlerUtils.convertToActionData(new LinkedList<String>());
	}
	
	@Test(expected=DataFormatException.class)
	public void testConvertToActionData_NotSupportedID() throws IllegalArgumentException, IOException, DataFormatException
	{
		exportedItems.add("NOTSUPPORTEDID");
		DataIOHandlerUtils.convertToActionData(exportedItems);
	}

	@Test(expected=DataFormatException.class)
	public void testConvertToActionData_DoesntHaveRequiredFields() throws IllegalArgumentException, IOException, DataFormatException
	{
		exportedItems.add("MPRESS:3:469:76:1399149");
		DataIOHandlerUtils.convertToActionData(exportedItems);
	}
	
	@Test(expected=DataFormatException.class)
	public void testConvertToActionData_FieldsArentOfExpectedType() throws IllegalArgumentException, IOException, DataFormatException
	{
		exportedItems.add("MPRESS:3:469:76:1399149:MRELEA");
		DataIOHandlerUtils.convertToActionData(exportedItems);
	}
	
	@Test
	public void testConvertToActionData_Valid() throws IllegalArgumentException, IOException, DataFormatException
	{
		Queue<AbstractInputAction> convertedActions = DataIOHandlerUtils.convertToActionData(new LinkedList<>(DataIOHandlerUtils.extractInputActionStringData(new ArrayList<>(exportedItems))));
		
		assertTrue(convertedActions.size() == 8);
		assertTrue(convertedActions.peek() instanceof MouseMove);
		assertTrue(convertedActions.toArray()[7] instanceof KeyboardKeyPress);
	}
	
	//=======================================================
	
	@Test(expected=IllegalArgumentException.class)
	public void testConvertToClickZoneDetailsData_nullList() throws IllegalArgumentException, DataFormatException
	{
		DataIOHandlerUtils.convertToClickZoneDetailsData(null);
	}
	
	@Test
	public void testConvertToClickZoneDetailsData_emptyList() throws IllegalArgumentException, DataFormatException
	{
		DataIOHandlerUtils.convertToClickZoneDetailsData(new ArrayList<String>());
	}
	
	@Test(expected=DataFormatException.class)
	public void testConvertToClickZoneDetailsData_NotSupportedID() throws IllegalArgumentException, IOException, DataFormatException
	{
		exportedItems.add("NOTSUPPORTEDID");
		DataIOHandlerUtils.convertToClickZoneDetailsData(new ArrayList<>(exportedItems));
	}

	@Test(expected=DataFormatException.class)
	public void testConvertToClickZoneDetailsData_DoesntHaveRequiredFields() throws IllegalArgumentException, IOException, DataFormatException
	{
		exportedItems.add("CZONEE:234:23:33");
		DataIOHandlerUtils.convertToClickZoneDetailsData(new ArrayList<>(exportedItems));
	}
	
	@Test(expected=DataFormatException.class)
	public void testConvertToClickZoneDetailsData_FieldsArentOfExpectedType() throws IllegalArgumentException, IOException, DataFormatException
	{
		exportedItems.add("CZONEE:234:23:33:NotInteger");
		DataIOHandlerUtils.convertToClickZoneDetailsData(new ArrayList<>(exportedItems));
	}
	
	@Test
	public void testConvertToClickZoneDetailsData_Valid() throws IllegalArgumentException, IOException, DataFormatException
	{
		ArrayList<ClickZoneDetails> convertedActions = DataIOHandlerUtils.convertToClickZoneDetailsData(DataIOHandlerUtils.extractClickZoneStringData(new ArrayList<>(exportedItems)));
		
		assertTrue(convertedActions.size() == 5);
		assertTrue(convertedActions.toArray()[4] instanceof ClickZoneDetails);
	}
	
	//=======================================================
	
	@Test(expected=DataFormatException.class)
	public void testImportActionDataFromFile_NotSupportedID() throws IllegalArgumentException, IOException, DataFormatException
	{
		exportedItems.add("NOTSUPPORTEDID");
		DataIOHandlerUtils.exportStringDataToFile(exportedItems, saveLocation);
		
		DataIOHandlerUtils.importActionDataFromFile(saveLocation);
	}

	@Test(expected=DataFormatException.class)
	public void testImportActionDataFromFile_DoesntHaveRequiredFields() throws IllegalArgumentException, IOException, DataFormatException
	{
		exportedItems.add("MPRESS:3:469:76:1399149");
		DataIOHandlerUtils.exportStringDataToFile(exportedItems, saveLocation);
		
		DataIOHandlerUtils.importActionDataFromFile(saveLocation);
	}
	
	@Test(expected=DataFormatException.class)
	public void testImportActionDataFromFile_FieldsArentOfExpectedType() throws IllegalArgumentException, IOException, DataFormatException
	{
		exportedItems.add("MPRESS:3:469:76:1399149:MRELEA");
		DataIOHandlerUtils.exportStringDataToFile(exportedItems, saveLocation);
		
		DataIOHandlerUtils.importActionDataFromFile(saveLocation);
	}
	
	@Test
	public void testImportActionDataFromFile_Valid() throws IllegalArgumentException, IOException, DataFormatException
	{
		Queue<AbstractInputAction> importedActions = DataIOHandlerUtils.importActionDataFromFile(saveLocation);
		
		assertTrue(importedActions.size() == 8);
		assertTrue(importedActions.peek() instanceof MouseMove);
		assertTrue(importedActions.toArray()[7] instanceof KeyboardKeyPress);
	}

	//=======================================================
	
	@Test
	public void testImportClickZoneDataFromFile_Valid() throws IllegalArgumentException, IOException, DataFormatException
	{
		ArrayList<ClickZoneDetails> importedActions = DataIOHandlerUtils.importClickZoneDataFromFile(saveLocation);
		
		assertTrue(importedActions.size() == 5);
		assertTrue(importedActions.get(0).getWindowDimensions().equals(new Dimension(50, 50)));
		assertTrue(importedActions.get(0).getWindowLocation().equals(new Point(698, 413)));
		
		assertTrue(importedActions.get(4).getWindowDimensions().equals(new Dimension(250, 250)));
		assertTrue(importedActions.get(4).getWindowLocation().equals(new Point(1222, 417)));
	}

	//=======================================================
	
	@Test(expected=IllegalArgumentException.class)
	public void testCreateInputActionByParsing_nullDetails() throws IllegalArgumentException, UnsupportedDataTypeException, DataFormatException
	{
		DataIOHandlerUtils.createInputActionByParsing(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCreateInputActionByParsing_EmptyDetails() throws IllegalArgumentException, UnsupportedDataTypeException, DataFormatException
	{
		DataIOHandlerUtils.createInputActionByParsing("");
	}
	
	@Test(expected=DataFormatException.class)
	public void testCreateInputActionByParsing_UnsupportedID() throws IllegalArgumentException, UnsupportedDataTypeException, DataFormatException
	{
		DataIOHandlerUtils.createInputActionByParsing("MMOVE:469:76:548171866216160");
	}
	
	@Test(expected=DataFormatException.class)
	public void testCreateInputActionByParsing_DoesntHaveRequiredFields() throws IllegalArgumentException, UnsupportedDataTypeException, DataFormatException
	{
		DataIOHandlerUtils.createInputActionByParsing("MMOVED:469:548171866216160");
	}
	
	@Test(expected=DataFormatException.class)
	public void testCreateInputActionByParsing_WrongDataTypeInField() throws IllegalArgumentException, UnsupportedDataTypeException, DataFormatException
	{
		DataIOHandlerUtils.createInputActionByParsing("MMOVED:469:blue:548171866216160");
	}
	
	@Test(expected=DataFormatException.class)
	public void testCreateInputActionByParsing_MissingColon() throws IllegalArgumentException, UnsupportedDataTypeException, DataFormatException
	{
		DataIOHandlerUtils.createInputActionByParsing("MMOVED469:blue:548171866216160");
	}

	@Test(expected=DataFormatException.class)
	public void testCreateInputActionByParsing_TooManyFields() throws IllegalArgumentException, UnsupportedDataTypeException, DataFormatException
	{
		DataIOHandlerUtils.createInputActionByParsing("MMOVED:469:76:548171866216160:1234");
	}
	
	@Test
	public void testCreateInputActionByParsing_Valid() throws IllegalArgumentException, UnsupportedDataTypeException, DataFormatException
	{
		assertTrue(DataIOHandlerUtils.createInputActionByParsing("MMOVED:469:76:548171866216160") instanceof MouseMove);
		assertTrue(DataIOHandlerUtils.createInputActionByParsing("KPRESS:17:548174113763558") instanceof KeyboardKeyPress);
		assertTrue(DataIOHandlerUtils.createInputActionByParsing("KRELEA:17:548174760943927") instanceof KeyboardKeyRelease);
		assertTrue(DataIOHandlerUtils.createInputActionByParsing("MPRESS:3:469:76:1399149:548179641171616") instanceof MouseButtonPress);
		assertTrue(DataIOHandlerUtils.createInputActionByParsing("MRELEA:3:469:76:1399149:548180249079966") instanceof MouseButtonRelease);
	}
	//=======================================================
	
}































