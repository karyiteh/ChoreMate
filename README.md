# ChoreMate [![Build Status](https://travis-ci.com/karyiteh/ChoreMate.svg?token=YbEgce3bxeSS4LFDwdwp&branch=master)](https://travis-ci.com/karyiteh/ChoreMate)

![Choremate Logo](http://gdurl.com/A7D3t)

**Team SEGFAULT**

Members:
- Ashley Rujuin Chao - Business Analyst
- Inderpal Singh Dhillon - Software Architect
- Cong Tam Quang "Sasha" Hoang - Quality Assurance Lead    
- Karissa Raelynn Jacobsen - UI Specialist
- Yutao Jiang - Senior System Analyst
- Alexander Hau Chung "Alex" Lui - Quality Assurance Lead
- Vincent Vo Nguyen - Algorithm Specialist
- Rui Ren - Database Specialist
- Jamicko John Macaraeg "Jam" Tan - Project Manager
- Kar Yi "Kai" Teh - Software Development Lead


### Introduction
ChoreMate is a household management app that allows its users to manage household duties stress-free.
The various organizational features of ChoreMate such as task-tracking and payment-tracking features
as well as task notifications aim to improve communication between housemates and allow housemates 
to be accountable for their responsibilities in their respective households.

### Requirements
- At least two Android devices running a minimum of API 23 (Android Marshmallow) [API 27 (Android Oreo)
  is preferred].
- Google Play Services 15.0.0 or higher running on the Android devices.
- Active Internet connection.

*NOTE: If an emulator is used to test the app, it's best to use the Nexus 5X or Nexus 5 because they
are the only emulators with Google Play Services installed.*

### Installation Instructions
##### From the device
1. Make sure that "Install unknown app" permissions are turned on for the browser or the app
   that is used to download the APK. This can be found in Settings ->
    Apps & notifications -> Advanced -> Special app access -> Install unknown apps 
    (*NOTE:* Might vary across devices).
2. Download the APK file [here](https://drive.google.com/open?id=1et9CrXPBAlZ9og96hsy0E1YDkqKCDl0I).
3. Run the APK file on the Android device with "Package Installer".
4. Tap on "Install" when Package Installer display the prompt "Do you want to install this application?".
5. The app should be installed on the device.

### How-to-run Instructions
1. Navigate to "Apps" list on your device.
2. Tap on "ChoreMate" or this app icon:
    ![ChoreMate App Icon](http://gdurl.com/4FV5)

### Known Bugs
1. When a task/payment/charge is created and the user is redirected back to the corresponding tabs,
   it is possible that duplicates of tasks/housemate balances are displayed. If that happens, reload the tab and 
   the correct tasks/housemate balances will be displayed.
2. When the "Use Camera" option is selected when the user changes the avatar, the camera app might cause
   the app to crash. If that occurs, simply restart the app and attempt to change the avatar again using
   the steps mentioned in the workflow of UC-25.
3. On the rare occasion that a housemate's avatar is not updated in the "Payments" tab when the 
    housemate has recently updated their avatar, re-upload the new user avatar by following the steps 
    included in "Update User Avatar" use case to fix the bug.

### Troubleshooting
- On rare occasions that the app is behaving abnormally, force quit the app and relaunch the app by 
  following the steps mentioned in ***How-to-run Instructions***.

- If there is a notification that says "Update your Google Play services" when the user attempts to register 
  or sign-in, make sure that Google Play services installed on that device is updated to version 15.0.0 or 
  higher.
  
- For now, notifications can only be received when the user is running the app in the background. 
  Therefore, if the notification does not show up, try closing the app on the receiver's device into the 
  background and tap on "Remind" again on the sender's device. 
