package com.violetsector.alarm;

import javax.swing.*;          

class Start
{
	final static Window window = new Window();
	
	public static void main(String[] args)
	{
		// Set the system look and feel
		try
		{
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } 
	    catch (Exception e)
	    {
	    	e.printStackTrace();
	    }

		// Add the programme to the system tray
		Tray systemTray = new Tray();
		systemTray.initialise();
		
		// Start a thread for the alarm
		Alarm alarm = new Alarm();
		alarm.start();
		
		// Open a window for the programme
		window.initialise();
	}
}