1. Locate _debug.keystore_ file: in Eclipse, go to Windows -> Preferences -> Android -> Build, you can see the keystore path and copy it for later use.

2. Find _Keytool.exe_ under "C:\Program Files\Java\<JDK version>\bin" folder.

3. Open the command window, navigate to "C:\Program Files\Java\<JDK version>\bin" and enter:

keytool.exe -list -alias androiddebugkey -keystore "<_debug.keystore_ path>" -storepass android -keypass android

4. If Google Map is not showing on your screen, the problem may be related with incorrect Google Map API key. You can go to http://code.google.com/android/maps-api-signup.html and generate your API key, replace it with the one in layout/mapview.xml file.