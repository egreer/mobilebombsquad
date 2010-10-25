--------------------------
PROJECT INFO
--------------------------
AUTHORS: Eric Greer & Andrew Yee 
USERNAMES: egreer & ayee
PROJECT: Project 1: Mobile Bomb Squad 
COURSE: CS525H 2010
SITES:
DESCRIPTION: http://web.cs.wpi.edu/~gogo/courses/cs525H/projects/proj1/
PROJECT SVN: http://code.google.com/p/mobilebombsquad/
--------------------------
PACKAGE CONTENTS
--------------------------
Part one is contained in project MobileBombSquad-AYEG
Part two is contained in project MobileBombSquad-AYEG-Accelerometer
The whole game is contained in project  MobileBombSquad-AYEG-Game

--------------------------
GAME DESCRIPTION
--------------------------

--------------------------
RUNNING THE GAME
--------------------------
Each part of the game is a self contained Android Eclipse project. The 
individual projects can be imported into Eclipse using the Import Tool.
From Eclipse with the Android SDK enabled you can connect an Android device.
With the device connected set up a run configuration for the project you 
would like to deploy. In the Run Configurations set the target to manual.
Run the project and select the device to deploy the application to.

You can also obtain the .apk from the MobileBombSquad-AYEG-Game folder. 
--------------------------
PLAYING THE GAME
--------------------------

When Mobile Bomb Squad begins the first player places his finger on the touch point 
and then tilts the bubble until it enters the circle.

Once the bubble enters the circle the player must hold it there until the next touch
point appears. 

The second player must hold the touch point, when the confirmation "OK" is heard the 
first player may release his finger and the timer will begin for the second player 
to attempt to get the bubble into the circle.   

Play continues until any of these conditions are triggered:
	A player releases the touch point during the transfer
	The bubble leaves the circle in the transfer
	The timer runs out and the ball is not in the circle
	
When the game ends you have the option to retry.  
  
--------------------------
TEAMMEMBER CONTRIBUTIONS
--------------------------
The majority of this project was peer programmed. Some specialties included
Andrew:
	Menus
	Audio & Artwork
Eric: 
	Accelerometer
	Documentation
