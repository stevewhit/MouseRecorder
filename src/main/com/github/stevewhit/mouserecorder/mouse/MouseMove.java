package com.github.stevewhit.mouserecorder.mouse;

import java.awt.geom.Point2D;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.stevewhit.mouserecorder.monitor.Pixel;

public class MouseMove extends MouseAction
{
	/**
	 * The defined coordinate path movements for the given mouse move.
	 */
	private List<Pixel> mousePath;
	
	/**
	 * Variable that establishes the max distance between any two consecutive pixel coordinates for a given move.
	 */
	private int maxPixelDistancePerMove = 1;
	
	/**
	 * Constructor that takes a pre-defined mouse path.
	 * @param mousePath Path of 2D pixels {@link Point2D} 
	 * @throws IllegalArgumentException
	 */
	public MouseMove(List<Pixel> mousePath) throws IllegalArgumentException
	{
		super(LocalDateTime.now());
		
		setMousePath(mousePath);
	}
	
	/**
	 * Constructor that takes a start and end coordinate.
	 * @param startPixelCoord Starting pixel coordinate {@link Point2D}
	 * @param endPixelCoord Ending pixel coordinate {@link Point2D}
	 * @throws IllegalArgumentException
	 */
	public MouseMove(Pixel startPixel, Pixel endPixel) throws IllegalArgumentException
	{
		this(Arrays.asList(startPixel, endPixel));
	}
	
	/**
	 * Returns the mouse path associated with this MouseMove.
	 * @return Returns a list of {@link Point2D} pixel coordinates representing a mousePath.
	 */
	public List<Pixel> getMousePath()
	{
		return this.mousePath;
	}
	
	/**
	 * Updates the mouse path given a pre-defined mouse path.
	 * @param mousePath Path of pixel coordinates {@see Point2D}
	 * @throws IllegalArgumentException
	 */
	private void setMousePath(List<Pixel> mousePath) throws IllegalArgumentException
	{
		if (mousePath == null || mousePath.size() <= 1)
			throw new IllegalArgumentException("Passed mouse path must not be null, empty, or only contain one point when creating a new mouse path.");
		
		if (pathHasCorrectPixelDistances(mousePath))
		{
			this.mousePath = mousePath;
		}
		else
		{
			throw new IllegalArgumentException("Cannot set mouse path with the given path because the pixel distances are too large.");
		}
	}
	
	/**
	 * Searches through the given mouse path and determines if any of the pixels are further apart than 
	 * the known maxPixelDistancePerMove.
	 * @param mousePath The mouse pixel path that is to be iteratively tested for pixel distance per entry.
	 * @return Returns true if the given path is valid. Returns false if path is not valid.
	 * @throws IllegalArgumentException
	 */
	private boolean pathHasCorrectPixelDistances(List<Pixel> mousePath) throws IllegalArgumentException
	{
		if (mousePath == null || mousePath.size() <= 1)
			throw new IllegalArgumentException("Passed mouse path must not be null, empty, or only contain one point when creating a new mouse path.");
		
		Pixel lastPixel = null;
		
		for (Pixel currentPixel : mousePath)
		{
			if (currentPixel == null)
			{
				throw new IllegalArgumentException("Invalid pixel found.");
			}
			
			if (lastPixel == null)
			{
				lastPixel = new Pixel(currentPixel.getPixelCoordinate2D());
			}
			
			else
			{
				// Compare X and Y values to determine pixel distances.
				if (Math.abs(currentPixel.getPixelCoordinate2D().getX() - lastPixel.getPixelCoordinate2D().getX()) > maxPixelDistancePerMove || 
					Math.abs(currentPixel.getPixelCoordinate2D().getY() - lastPixel.getPixelCoordinate2D().getY()) > maxPixelDistancePerMove)
				{
					return false;
				}
				else
				{
					lastPixel = currentPixel;
				}
			}
		}
	
		return true;
	}
	
	/**
	 * Returns the first/starting coordinate from the movement path.
	 * @return Returns the first/starting coordinate from the movement path {@link Point2D}
	 * @throws IllegalStateException
	 */
	public Pixel getStartingPoint() throws IllegalStateException
	{
		if (this.mousePath == null || this.mousePath.size() <= 1)
			throw new IllegalStateException("Mouse path needs to be defined and populated before extracting any points");
		
		return this.mousePath.get(0);
	}
	
	/**
	 * Returns the last/ending coordinate from the movement path.
	 * @return Returns the last/ending coordinate from the movement path {@link Point2D}
	 * @throws IllegalStateException
	 */
	public Pixel getEndingPoint() throws IllegalStateException
	{
		if (this.mousePath == null || this.mousePath.size() <= 1)
			throw new IllegalStateException("Mouse path needs to be defined and populated before extracting any points");
		
		return this.mousePath.get(this.mousePath.size() - 1);
	}
	
	@Override
	public String toString()
	{
		return String.format("Moving: %1s --> %2s", getStartingPoint().toString(), getEndingPoint().toString());
	}
}



























