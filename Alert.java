package com.violetsector.alarm;

class Alert extends Thread
{
	String subject, message;
	int delay;
	
	// Constructor
	Alert (String subject, String message)
	{
		this.subject = subject;
		this.message = message;
	}
	
	// Constructor
	Alert (String subject, String message, int delay)
	{
		this.subject = subject;
		this.message = message;
		this.delay = delay;
	}
	
	// The method invoked when the thread is started
	public void run()
	{
		// Sleep if applicable
		if (delay > 0)
		{
			try
			{
				// Sleep until relevant time period
				Thread.sleep(delay);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		// Display a new task tray message
		Tray.message(subject, message, "INFO");
				
		// Play sound if application not muted
		if (!Alarm.muted)
		{
			java.applet.AudioClip clip = java.applet.Applet.newAudioClip(Alarm.class.getResource("/resources/alarm.wav"));
			clip.play();
		}
	}
}
