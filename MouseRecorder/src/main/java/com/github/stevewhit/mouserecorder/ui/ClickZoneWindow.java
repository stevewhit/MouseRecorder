package com.github.stevewhit.mouserecorder.ui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Shows a square frame with either a transparent background or a semi-transparent red background.
 * @author Steve Whitmire (swhit114@gmail.com)
 *
 */
public class ClickZoneWindow extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8695937812379099402L;
	
	/**
	 * The rectangle pane that is used to draw the rectangle.
	 */
	private RectanglePane rectanglePane;

	/**
	 * A constructor that creates a click zone window with the option to choose a transparent background or a semi-transparent red background. 
	 * By default, the window will be placed in the center of the screen.
	 * @param showTransparentMode Indicates if the window should be fully transparent or semi-transparent.
	 */
	public ClickZoneWindow(boolean showTransparentMode)
	{
		this(showTransparentMode, showTransparentMode, null, null);
	}
	
	/**
	 * A constructor that creates a click-zone window with the option to choose a transparent background or a semi-transparent red background
	 * at the given window location.
	 * @param showTransparentMode Indicates if the window should be fully transparent or semi-transparent.
	 * @param lockPlacementAndSize Indicates if the window will allow the user to resize and move the window.
	 * @param windowLocation The location of the top left corner of the window.
	 * @param windowDimensions The preferred size of the click zone frame.
	 */
	public ClickZoneWindow(boolean showTransparentMode, boolean lockPlacementAndSize, Point windowLocation, Dimension windowDimensions)
	{
		// Makes it so that the window doesn't show up in task bar.
		setType(Type.UTILITY);
		
		// Initialization of the frame.
		setUndecorated(true);
		setBackground(new Color(0, 0, 0, 0));
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Try and set the location of the window
		if (windowLocation == null)
			setLocationRelativeTo(null);
		else
			setLocation(windowLocation);
		
		rectanglePane = new RectanglePane(showTransparentMode, lockPlacementAndSize, this, windowDimensions);
		
		// Add the Rectangle to the viewer
		getContentPane().add(rectanglePane);

		pack();
		setVisible(true);
	}
	
	public RectanglePane getRectanglePane()
	{
		return rectanglePane;
	}
	
	/**
	 * Returns this window as a formatted string representation.
	 * <pre>
	 * Example output: 'CZONEE:150:100:250:350'
	 * </pre>
	 * @return Returns this window as an exportable string.
	 */
	public String toExportableString()
	{
		return String.format("CZONEE:%1$d:%2$d:%3$d:%4$d", (int)getLocation().getX(), (int)getLocation().getY(), (int)getSize().getWidth(), (int)getSize().getHeight());
	}
	
	/**
	 * A rectangle JPanel with the option to make the panel fully transparent or semi-transparent.
	 * @author Steve Whitmire (swhit114@gmail.com)
	 *
	 */
	protected class RectanglePane extends JPanel
	{
		/**
		 * The serial version UID used on dispose.
		 */
		private static final long serialVersionUID = 64654321651L;
		
		/**
		 * Variable to indicate if the panel should be transparent or not.
		 */
		private boolean showTransparentMode = false;
		
		/**
		 * The frame that this JPanel is added to.
		 */
		private JFrame parentFrame;
		
		/**
		 * Constructor that indicates whether the jpanel should be transparent or not and it identifies the jframe that this panel is added to.
		 * @param showTransparentMode Variable to indicate if the panel should be transparent or not.
		 * @param lockPlacementAndSize Enables or disables the user's ability to move and resize the window.
		 * @param parentFrame The frame that this jpanel is added to.
		 * @param preferredSize The preferred size of the rectangle panel.
		 */
		public RectanglePane(boolean showTransparentMode, boolean lockPlacementAndSize, JFrame parentFrame, Dimension preferredSize)
		{
			this.parentFrame = parentFrame;
			
			// Jpanel initialization.
			if (lockPlacementAndSize)
				setBackground(new Color(200, 150, 150));
			else
				setBackground(new Color(150, 200, 200));
			
		    setOpaque(false);
            setLayout(new GridBagLayout());
            
            // Try to set the dimensions of the window.
    		if (preferredSize == null)
    			setPreferredSize(new Dimension(150, 150));
    		else
    			setPreferredSize(preferredSize);
            
            // Show the transparent or not, jpanel.
            setViewMode(showTransparentMode, lockPlacementAndSize);
           
            // Add mouse listeners to dispose, resize, and move the frame only if we're
            // not in transparent mode.
            if (!showTransparentMode)
            {            
            	MouseMoveListener mouseListener = new MouseMoveListener(this, this.parentFrame, lockPlacementAndSize);
            	addMouseListener(mouseListener);
            	addMouseMotionListener(mouseListener);
            }
	    }
		
		/**
		 * Updates the showtransparentmode variable and the tooltiptext of the jpanel.
		 * @param showTransparentMode Variable to indicate if the panel should be transparent or not.
		 * @param lockPlacementAndSize Enables or disables the user's ability to move and resize the window.
		 */
		public void setViewMode(boolean showTransparentMode, boolean lockPlacementAndSize)
		{
			this.showTransparentMode = showTransparentMode;
			
			if (showTransparentMode)
			{
				setToolTipText(null);
			}
			else
			{
				if (!lockPlacementAndSize)
					setToolTipText("<html>Double-Click LEFT mouse button to shrink.<br/>Double-Click RIGHT Mouse button to enlarge.<br/>Double-Click Scroll Wheel to close.</html>");
				else
					setToolTipText("<html>Double-Click Scroll Wheel to close.</html>");
			}
			
			// Repaints this jpanel to include the transparency options in the drawing.
			this.repaint();
		}

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(getBackground());
            
            if (!showTransparentMode)
            {
            	g2d.setComposite(AlphaComposite.SrcOver.derive(0.5f));	// comment this for click-through
            	g2d.fillRect(0, 0, getWidth(), getHeight());				// comment this for click-through
            }
            
            g2d.setColor(new Color(255, 0, 0));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            g2d.dispose();
        }
	}
	
	/**
	 * A mouse listener specifically designed for the RectanglePanel for this window.
	 * @author Steve Whitmire (swhit114@gmail.com)
	 *
	 */
	private class MouseMoveListener implements MouseListener, MouseMotionListener
	{
		/**
		 * The rectangle panel that this mouse listener applies to.
		 */
		private JComponent target;
		
		/**
		 * The frame that the rectangle panel is added to.
		 */
		private JFrame parentFrame;
		
		/**
		 * The start point of any drag operation to move the rectangle panel.
		 */
		private Point startDragPoint;
		
		/**
		 * The start point of any drag operation to move the rectangle panel.
		 */
		private Point startDragLocation;
		
		/**
		 * Enables or disables the user's ability to move and resize the window.
		 */
		private boolean lockPlacementAndSize;
		
		/**
		 * Constructor that accepts parameters indicating relationships of components that are used by this listener.
		 * @param target The rectangle panel that this mouse listener applies to.
		 * @param parentFrame The frame that the rectangle panel is added to. 
		 * @param lockPlacementAndSize Enables or disable the user's ability to move and resize the window.
		 */
		public MouseMoveListener(JComponent target, JFrame parentFrame, boolean lockPlacementAndSize)
		{
			this.target = target;
			this.parentFrame = parentFrame;
			this.lockPlacementAndSize = lockPlacementAndSize;
		}

		@Override
		public void mouseDragged(MouseEvent e)
		{
			if (!lockPlacementAndSize)
			{
	    	    Point current = this.getScreenLocation(e);
	    	    
	    	    Point offset = new Point(
	    	    			(int) current.getX() - (int) startDragPoint.getX(),
	    	    			(int) current.getY() - (int) startDragPoint.getY());
	
	    	    Point new_location = new Point(
	    	    			(int)(this.startDragLocation.getX() + offset.getX()), 
	    	    			(int)(this.startDragLocation.getY() + offset.getY()));
	    	    
	    	    parentFrame.setLocation(new_location);
			}
    	}

		@Override
		public void mouseClicked(MouseEvent e)
		{
			// Left Mouse Click
			if (!lockPlacementAndSize && e.getButton() == 1 && e.getClickCount() >= 2)
			{
				if (target.getParent().getSize().getWidth() > 75 && target.getParent().getSize().getHeight() > 75)
				{
					Dimension smallerSize = new Dimension((int)target.getParent().getSize().getWidth() - 50, (int)target.getParent().getSize().getHeight() - 50);
					  
					target.getParent().setSize(smallerSize);
					target.setSize(smallerSize);
				}
			}
			// Scroll Wheel Mouse click
			else if (e.getButton() == 2 && e.getClickCount() >= 2)
			{
				parentFrame.dispose();
			}
			// Right Mouse Click
			else if (!lockPlacementAndSize && e.getButton() == 3 && e.getClickCount() >= 2)
			{
				Dimension biggerSize = new Dimension((int)target.getParent().getSize().getWidth() + 50, (int)target.getParent().getSize().getHeight() + 50);
			
				target.getParent().setPreferredSize(biggerSize);
				target.setPreferredSize(biggerSize);
				target.getParent().setSize(biggerSize);
				target.setSize(biggerSize);
				target.repaint();
				parentFrame.pack();
			}
		}

		@Override
		public void mousePressed(MouseEvent e)
		{
			if (!lockPlacementAndSize)
			{
	    	    this.startDragPoint = this.getScreenLocation(e);
	    	    this.startDragLocation = parentFrame.getLocation();
			}
    	}

		@Override
		public void mouseMoved(MouseEvent e)
		{
		}
		
		@Override
		public void mouseReleased(MouseEvent e)
		{
		}

		@Override
		public void mouseEntered(MouseEvent e)
		{
		}

		@Override
		public void mouseExited(MouseEvent e)
		{
		}
		
		/**
		 * Returns the screen location of a given mouse event.
		 * @param e A mouse event.
		 * @return Returns the location of the given mouse event.
		 */
		private Point getScreenLocation(MouseEvent e)
		{
			Point cursorLocation = e.getPoint();
			Point targetLocation = this.target.getLocationOnScreen();
			
			return new Point(
					(int)(targetLocation.getX() + cursorLocation.getX()),
	    	        (int)(targetLocation.getY() + cursorLocation.getY()));
		}
	}
}
