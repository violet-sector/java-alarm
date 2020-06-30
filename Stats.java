package com.violetsector.alarm;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

class Stats extends Thread implements MouseListener
{
	static JEditorPane mainPanel;
	JScrollPane scrollPane;
	static JFrame frame = Window.frame;
	String loginDetails;
	static int localLastAttack = -1, serverLastAttack = -1;
	static JButton openSite;
	URL url;

	// List of pages that should be loaded in turn	
	final static String pages[] = {"scans_incoming.php", "scans_outgoing.php", "main.php"};

	// Constructor
	Stats(String loginDetails)
	{
		// Register a cookie manager for the class
		CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));

		// Store the login post variables
		this.loginDetails = loginDetails;
	}
	
	// Method for displaying stats from the server
	void initialise()
	{
		mainPanel = new JEditorPane();
		mainPanel.setContentType("text/html");
		//mainPanel.setEditable(false);
		//scrollPane = new JScrollPane(mainPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);  
		frame.add(mainPanel);
		
		// Create open TVS website button
		openSite = new JButton("Open browser window to TVS site");
		frame.add(openSite);
		openSite.setAlignmentX(Component.CENTER_ALIGNMENT);
		openSite.addMouseListener(this);
		
		//frame.pack();

	}
	
	void getStats()
	{
		String html;
		// Enter continuous stat displaying loop
		while (true)
		{
			// Loop through the page list
			for (String page : pages)
			{
				try
				{
					// Don't load pages close to tick change
					if (Alarm.secsLeft < 2 || Alarm.tickLength - Alarm.secsLeft < 5)
					{
						Thread.sleep(12000);
					}
					// Load the page and update the display
					html = getPage(page, null);
					mainPanel.setText(html);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
					
				// Check to see when the player was last attacked
				for(String line: mainPanel.getText().split("\n"))
				{
					if (line.startsWith("<!--last_attack="))
					{
						try
						{
							serverLastAttack = Integer.parseInt(line.substring(16, line.indexOf("-->")));
						}
						catch (NumberFormatException | IndexOutOfBoundsException e)
						{
							// This shouldn't happen if the server is set up properly
							System.err.println("The response from the server could not be processed " + line);
						}
						if (localLastAttack == -1) localLastAttack = serverLastAttack;
						else if (serverLastAttack > localLastAttack)
						{
							// Play attack warning sound if application not muted
							if (!Alarm.muted)
							{
								java.applet.AudioClip clip = java.applet.Applet.newAudioClip(Alarm.class.getResource("/resources/klaxon.wav"));
								clip.play();
							}
							// Show tray alert
							Tray.message("Damage taken", "You have been attacked\nClick here to open the website", "WARNING");
							// Change tray icon
							Tray.setIcon("sad.gif");
							localLastAttack = serverLastAttack;
						}
					}
					
					// Deal with incorrect passwords etc
					else if (line.equals("<!--unauthorised-->"))
					{
						System.err.println("Unauthorised access response received from server");
						return;
					}
				}
					
				// Wait 12 seconds before loading the next page
				try
				{
					Thread.sleep(12000);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	boolean login()
	{
		String html = getPage("login.php", loginDetails + "&version=1");
		if (html.equals("<!--unauthorised-->"))
		{
			return false;
		}
		else
		{
			// Update the GUI
			mainPanel.setText(html);
			
			// Reset login details
			loginDetails = null;
			
			// Get the last attack data
			for(String line: html.split("\n"))
			{
				if (line.startsWith("<!--last_attack="))
				{
					try
					{
						localLastAttack = serverLastAttack = Integer.parseInt(line.substring(16, line.indexOf("-->")));
					}
					catch (NumberFormatException | IndexOutOfBoundsException e)
					{
						// This shouldn't happen if the server is set up properly
						System.err.println("The response from the server could not be processed " + line);
					}
				}
			}

			// Wait 12 seconds before loading the next page
			try
			{
				Thread.sleep(12000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			return true;
		}
	}

	// Method to load a HTML page
	public String getPage(String url, String postData)
	{
		String html = "", line = null;
		try
		{
			// Open connection
			URLConnection connection = new URL("https://www.violetsector.com/statschecker/" + url).openConnection();

			// Determinine if user is logging on or not
			if (loginDetails != null)
			{
				// Send the data
				connection.setDoOutput(true);
				OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
				out.write(postData);
				out.close();
			}

			// Get the HTML response
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while ((line = in.readLine()) != null)
			{
				html += line + "\n";				
			}
			
			in.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		// Return html response for display
		return html;
	}

	// The method invoked when the stats thread is created
	public void run()
	{
		initialise();
		if (login())
		{
			getStats();
		}

		//Start.window.initialise();
		frame.remove(mainPanel);
		frame.remove(openSite);
		frame.add(Window.passwordPanel);
		frame.add(Window.usernamePanel);
		frame.add(Window.submitButton);
		Window.password.setBackground(Color.RED);
		Window.username.setBackground(Color.RED);
		frame.repaint();
	}
	
	
	// Respond to user mouse clicks
	public void mouseClicked (MouseEvent me)
	{
		if (me.getSource() == openSite && Desktop.isDesktopSupported())
		{
	        Desktop desktop = Desktop.getDesktop();
	        try {
				desktop.browse(new java.net.URI("https://www.violetsector.com"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void mouseEntered (MouseEvent me) {}
	public void mousePressed (MouseEvent me) {}
	public void mouseReleased (MouseEvent me) {} 
	public void mouseExited (MouseEvent me) {}	
}
