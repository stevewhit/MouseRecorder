package com.github.stevewhit.mouserecorder;

import java.awt.AWTException;
import java.io.IOException;
import java.util.zip.DataFormatException;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import com.github.stevewhit.mouserecorder.ui.MouseRecorderGUI;

public class App 
{
    public static void main( String[] args ) throws AWTException, IOException, IllegalArgumentException, DataFormatException, InterruptedException
    {
        SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				com.jtattoo.plaf.acryl.AcrylLookAndFeel.setTheme("Black", "INSERT YOUR LICENSE KEY HERE", "");
		        
		        // select the Look and Feel
		        try
				{
					UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

		        new MouseRecorderGUI();
			}
		});
    }
}