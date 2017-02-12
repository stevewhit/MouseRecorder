package com.github.stevewhit.mouserecorder.ui;

/**
 * A basic class that represents the playback options used in the MouseRecorderGUI 
 * @author Steve Whitmire (swhit114@gmail.com)
 *
 */
public class PlaybackOptions
{
	public enum TimeQuantifier
	{
		Seconds(0),
		Minutes(1),
		Hours(2);
		
		private final int comboBoxPosition;
		
		private TimeQuantifier(int position)
		{
			this.comboBoxPosition = position;
		}
		
		public int getComboBoxPosition()
		{
			return this.comboBoxPosition;
		}
	};
	
	public PlaybackOptions(String scriptFileLocation)
	{
		timeCreated = System.currentTimeMillis();
		this.recordingFileLocation = scriptFileLocation;
	}
	
	private long timeCreated;
	
	public String getID()
	{
		return String.format("(%1$s) : %2$s", String.valueOf(timeCreated), recordingFileLocation);
	}
	
	public String recordingFileLocation = null;
	
	public boolean repeatNumTimesChecked = true;
	public int repeatNumTimesNumericalValue = 0;
	
	public boolean repeatLengthOfTimeChecked = false;
	public int repeatLengthOfTimeNumericalValue = 1;
	public TimeQuantifier repeatLengthOfTimeTimeQuantifier = TimeQuantifier.Seconds;
	
	public boolean ignoreClickZonesDuringPlaybackChecked = false;
	
	public boolean stopPlaybackQueueIfFailsChecked = false;
	
	public boolean runAdditionalScriptIfFailsChecked = false;
	public String runAdditionalScriptIfFailsFileLocation = null;
}
