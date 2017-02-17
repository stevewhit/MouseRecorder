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
	public LoadedRecording loadedAdditionalScriptIfFails = null;
	
	public String toExportableString()
	{
		return String.format("playbackitem:%1$s:%2$s:%3$d:%4$s:%5$d:%6$s:%7$s:%8$s:%9$s:%10$s", 
								recordingFileLocation, 
								String.valueOf(repeatNumTimesChecked),
								repeatNumTimesNumericalValue,
								String.valueOf(repeatLengthOfTimeChecked),
								repeatLengthOfTimeNumericalValue,
								
								String.valueOf(repeatLengthOfTimeTimeQuantifier),
								String.valueOf(ignoreClickZonesDuringPlaybackChecked),
								String.valueOf(stopPlaybackQueueIfFailsChecked),
								String.valueOf(runAdditionalScriptIfFailsChecked),
								loadedAdditionalScriptIfFails == null ? "_" : loadedAdditionalScriptIfFails.getFileLocationPath()
								);
	}
}
