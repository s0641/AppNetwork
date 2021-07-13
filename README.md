# AppNetwork
This is a service application which will tell the user how much data is used by the particular application.
This application will store the data ina file in DOWNLOADS folder with name of NET.TXT.


To use the application in android studio for testing
You have to use
This is for ubuntu desktop commands
To install -->  adb install -g  /home/radmin/Desktop/app-debug.apk
To run the application -->  adb shell am start-foreground-service --user 0 -n com.pcloudy.networks/.MyService  --es "packnam" com.google.android.youtube
This will give all the recieving and transmitting data for the application.


after packnam is the package name of the application you want to use for seeing the network data in the file. 

you can see the all the packages name in the android phones by using
--> adb shell pm list packages
