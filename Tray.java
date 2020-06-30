package com.violetsector.alarm;

import java.awt.*;
import java.awt.event.*;

class Tray implements MouseListener, ActionListener
{
	MenuItem quit, openSite;
	static TrayIcon icon;
	void initialise()
	{
		// Check the system tray is accessible
		if (SystemTray.isSupported())
		{
			SystemTray tray = SystemTray.getSystemTray();

			// Create the tray icon
			java.net.URL imageURL = Tray.class.getResource("/resources/icon.png");
			Image image = Toolkit.getDefaultToolkit().getImage(imageURL);

			// Create the menu
			PopupMenu popup = new PopupMenu();
			openSite = new MenuItem("Open violetsector.com");
			popup.add(openSite);
			openSite.addActionListener(this);
	
			quit = new MenuItem("Quit");
			popup.add(quit);
			quit.addActionListener(this);

			// Display the tray to the user
			icon = new TrayIcon(image, "TVS Statschecker", popup);
			icon.addMouseListener(this);
			icon.addActionListener(this);
			try
			{
				tray.add(icon);
			}
			catch (Exception e)
			{
				System.err.println(e);
			}
		}
		// Stop the programme if system tray unavailable
		else
		{
			System.err.println("System tray not supported");
			System.exit(0);
		}
	}

	// Display tray message
	static void message(String subject, String message, String type) 
	{
		if (type.equalsIgnoreCase("INFO"))
		{
			icon.displayMessage(subject, message, TrayIcon.MessageType.INFO);
		}
		
		else if (type.equalsIgnoreCase("WARNING"))
		{
			icon.displayMessage(subject, message, TrayIcon.MessageType.WARNING);
		}

		else if (type.equalsIgnoreCase("ERROR"))
		{
			icon.displayMessage(subject, message, TrayIcon.MessageType.ERROR);
		}

		else
		{
			icon.displayMessage(subject, message, TrayIcon.MessageType.NONE);
		}
	}

	// Change tray image
	static void setIcon(String pic)
	{
		java.net.URL imageURL = Tray.class.getResource(pic);
		Image image = Toolkit.getDefaultToolkit().getImage(imageURL);
		Tray.icon.setImage(image);
	}

	// Respond to user button clicks
	public void actionPerformed(ActionEvent evt) 
	{
		// Respond to quit menu selection
		if (evt.getSource() == quit)
		{
			System.exit(0);
		}
		// Respond to open website menu selection
		else if (evt.getSource() == openSite || evt.getSource() == icon)
		{
			if (Desktop.isDesktopSupported())
			{
		        Desktop desktop = Desktop.getDesktop();
		        try
				{
					desktop.browse(new java.net.URI("https://www.violetsector.com"));
				} 
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	// Respond to user mouse clicks
	public void mouseClicked (MouseEvent me)
	{
		// Restore the tray icon to default
		setIcon("/resources/icon.png");
		
		// Don't hide on right clicks
		if (me.getButton() != 1) return;
		if (Window.frame.isVisible())
		{
			Window.frame.setVisible(false);
		}
		else
		{
			// Make window visible
			Window.frame.setVisible(true);
			// Bring window to the fore
			Window.frame.setVisible(true);
		}
	}
	public void mouseEntered (MouseEvent me) {}
	public void mousePressed (MouseEvent me) {}
	public void mouseReleased (MouseEvent me) {} 
	public void mouseExited (MouseEvent me) {}
}
