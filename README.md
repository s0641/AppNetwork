# AppNetwork
This is a service application which will tell the user how much data is used by the particular application.
This application will store the data ina file in DOWNLOADS folder with name of NET.TXT.
This will give all the recieving and transmitting data for the application.


To use the application in android studio for testing
You have to use
Firstly you have to put the apk on the desktop.

If on ubuntu ->
To install -->  adb install -g  /home/radmin/Desktop/app-debug.apk     
To run the application -->  adb shell am start-foreground-service --user 0 -n com.pcloudy.networks/.MyService  --es "packnam" com.google.android.youtube

If on windows ->
To install -->adb install -g (path to the apk)
To run the application -->  adb shell am start-foreground-service --user 0 -n com.pcloudy.networks/.MyService  --es "packnam" com.google.android.youtube

In the last as you can see the youtube package name. You can also use any of the packages available on the android devices.
you can see the all the packages name in the android phones by using
--> adb shell pm list packages which ever application package you like just put instead of youtube one and you are good to go.

after packnam is the package name of the application you want to use for seeing the network data in the file. 


#Update License and copywrite

