package com.violetsector.alarm;

import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.Component;


import javax.swing.*;
import javax.swing.border.TitledBorder;

class Window implements ActionListener
{
	static JFrame frame;
	static JMenuBar menubar;
	static JMenu settingsMenu, actionsMenu, reminderMenu;
	static JMenuItem quit, oneMinute, fiveMinute, fifteenMinute, thirtyMinute, oneHour;
	static JCheckBoxMenuItem allwaysOnTop, exitOnClose, muteAudio;
	static JButton submitButton;
	static JPanel passwordPanel, usernamePanel;
	static JPasswordField password;
	static JTextField username;
	static JLabel header;

	void initialise()
	{		
		// Create the parent window
		frame = new JFrame("TVS Statschecker");
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setResizable(false);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Tray.class.getResource("/resources/icon.png")));
		
		// Create a menu bar and add it to the parent window
		menubar = new JMenuBar();
		frame.setJMenuBar(menubar);
		
		// Create statschecker logo
		java.net.URL imageURL = Tray.class.getResource("/resources/tvslogo.jpg");
		ImageIcon icon = new ImageIcon(imageURL);
		header = new JLabel(icon);
		frame.getContentPane().add(header);
		header.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// Create menu bar menus
		settingsMenu = new JMenu("Settings");
		actionsMenu = new JMenu("Actions");
		reminderMenu = new JMenu("Set Alarm");
		actionsMenu.add(reminderMenu);
		
		// Create the menu items
		quit = new JMenuItem("Quit");
		actionsMenu.add(quit);
		quit.addActionListener(this);
		
		oneMinute = new JMenuItem("1 Minute");
		reminderMenu.add(oneMinute);
		oneMinute.addActionListener(this);
		
		fiveMinute = new JMenuItem("5 Minutes");
		reminderMenu.add(fiveMinute);
		fiveMinute.addActionListener(this);
		
		fifteenMinute = new JMenuItem("15 Minutes");
		reminderMenu.add(fifteenMinute);
		fifteenMinute.addActionListener(this);
		
		thirtyMinute = new JMenuItem("30 Minutes");
		reminderMenu.add(thirtyMinute);
		thirtyMinute.addActionListener(this);
		
		oneHour = new JMenuItem("1 Hour");
		reminderMenu.add(oneHour);
		oneHour.addActionListener(this);
		
		allwaysOnTop = new JCheckBoxMenuItem("Allways on top");
		settingsMenu.add(allwaysOnTop);
		allwaysOnTop.addActionListener(this);
		
		exitOnClose = new JCheckBoxMenuItem("Exit on close");
		settingsMenu.add(exitOnClose);
		exitOnClose.addActionListener(this);
		
		muteAudio = new JCheckBoxMenuItem("Mute audio");
		settingsMenu.add(muteAudio);
		muteAudio.addActionListener(this);
		
		// Add the menu bars items to the menu bar
		menubar.add(settingsMenu);
		menubar.add(actionsMenu);
		
		// Create username box
		usernamePanel = new JPanel();
		usernamePanel.setBorder(new TitledBorder("Username"));
		username = new JTextField(12);
		usernamePanel.add(username);
		frame.add(usernamePanel);
		
		// Create password box
		passwordPanel = new JPanel();
		passwordPanel.setBorder(new TitledBorder("Password"));
		password = new JPasswordField(12);
		passwordPanel.add(password);
		frame.add(passwordPanel);
		
		// Create submit button
		submitButton = new JButton("LOGIN-->");
		frame.add(submitButton);
		submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		
		// Allow the user to submit his username and password
		submitButton.addActionListener(this);
		username.addActionListener(this);
		password.addActionListener(this);
		
		// Show the window
		frame.setSize(300, 450);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		
	}
		
	// Respond to user button clicks
	public void actionPerformed(ActionEvent evt) 
	{
		// Respond to quit menu selection
		if (evt.getSource() == quit)
		{
			System.exit(0);
		}
		
		// Respond to always on top requests
		else if (evt.getSource() == allwaysOnTop)
		{
			if (frame.isAlwaysOnTop()) frame.setAlwaysOnTop(false); else frame.setAlwaysOnTop(true);
		}
		
		// Respond to always on top requests
		else if (evt.getSource() == exitOnClose)
		{	
			if (frame.getDefaultCloseOperation() == 1) frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); else frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		}
		
		// Respond to mute requests
		else if (evt.getSource() == muteAudio)
		{
			Alarm.muted = (Alarm.muted) ? false : true;
		}
		
		// Respond to always on top requests
		else if (evt.getSource() == submitButton || evt.getSource() == password)
		{
			frame.remove(passwordPanel);
			frame.remove(usernamePanel);
			frame.remove(submitButton);
			
			frame.repaint();	
			Stats stats = new Stats(new String("username="+username.getText()+"&password="+new String( password.getPassword())));
			stats.start();
		}
		
		// Set an alarm
		else if (evt.getSource() == oneMinute)
		{
			// Do an alert in a separate thread
			Alert alert = new Alert("Alarm", "Timer expired\nClick here to open the website", 60000);
			alert.start();
		}
		
		else if (evt.getSource() == fiveMinute)
		{
			// Do an alert in a separate thread
			Alert alert = new Alert("Alarm", "Timer expired\nClick here to open the website", 300000);
			alert.start();
		}
		
		else if (evt.getSource() == fifteenMinute)
		{
			// Do an alert in a separate thread
			Alert alert = new Alert("Alarm", "Timer expired\nClick here to open the website", 900000);
			alert.start();
		}
		
		else if (evt.getSource() == thirtyMinute)
		{
			// Do an alert in a separate thread
			Alert alert = new Alert("Alarm", "Timer expired\nClick here to open the website", 1800000);
			alert.start();
		}
		
		else if (evt.getSource() == oneHour)
		{
			// Do an alert in a separate thread
			Alert alert = new Alert("Alarm", "Timer expired\nClick here to open the website", 3600000);
			alert.start();
		}
		
	}

}
