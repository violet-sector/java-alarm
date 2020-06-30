package com.violetsector.alarm;

import java.net.*;
import java.io.*;

class Alarm extends Thread
{
	static int secsLeft, tickLength, sleepPeriod;
	static String line, subject, message;
	static Boolean muted = false;
	
	// Method for obtaining variables from the server
	static void initialise()
	{	
		try
		{
			// Connect to the server
			URL url = new URL("https://violetsector.com/config.php");
			BufferedReader buff = new BufferedReader(new InputStreamReader(url.openStream()));

			// Process the page and assign the variables
			while ((line = buff.readLine()) != null)
			{
				if (line.startsWith("SECS_LEFT = "))
				{
					secsLeft = Integer.parseInt(line.substring(line.indexOf("=") + 2, line.indexOf("<")));
				}
				else if (line.startsWith("TICK_LENGTH = "))
				{
					tickLength = Integer.parseInt(line.substring(line.indexOf("=") + 2, line.indexOf("<")));
				}	
			}
			
			// Confirm a valid tick length
			// TODO
			if (tickLength != 10800 && tickLength != 900)
			{
				System.err.println("This programme only works with standard game lengths");
				System.exit(0);
			}

			// Start the alarm
			alarm();
	    }
		
		// Try again in 16 minutes if there is a problem retrieving the alarm settings
		catch (Exception e)
		{
			e.printStackTrace();
			try
			{
				Thread.sleep(960000);
			}
			catch (InterruptedException e1)
			{
				System.err.println("Alarm could not sleep, exiting");
				System.exit(0);
			}
	    }
	}
	
	// Method for displaying a notification at the start of every tick
	static void alarm()
	{
		int i;
		for (i = 1; i < 4; ++i)
		{
			try
			{
				// Jump reminder
				if (tickLength == 10800 && secsLeft > 1050)
				{
					sleepPeriod = secsLeft - 1050;
					subject = "Jump reminder";
					message = "The jump deadline is about to pass\nClick here to open the website";
				}
						
				// Shoot reminders
				else if (secsLeft > 150)
				{
					sleepPeriod = secsLeft - 150;
					subject = "EOT";
					message = "The tick is about to end\nClick here to open the website";
	
				}
				
				// Too late for this tick, do a reminder for next tick
				else 
				{
					sleepPeriod = (tickLength == 10800) ? secsLeft + 9750 : secsLeft + 750;
				}
				
				// Sleep until relevant time period
				Thread.sleep(sleepPeriod * 1000);

				// Do an alert in a separate thread
				Alert alert = new Alert(subject, message);
				alert.start();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			// Reset alarm to go off next tick
			secsLeft -= sleepPeriod;
			secsLeft = (secsLeft > 0) ? secsLeft : secsLeft + tickLength;
		}
		
		// Synchronise the alarm periodically
		try
		{
			int n = (int)(60 * Math.random()) + 300;
			Thread.sleep(n * 1000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		initialise();
	}
	
	// The method invoked when the alarm thread is created
	public void run()
	{
		initialise();
	}
}
