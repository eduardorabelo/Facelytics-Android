# Facelytics

Facelytics is an SDK allowing mobile apps to detect face criterias of people by analyzing the front video feed in realtime. Facelytics is able to track multiple faces and then detect gender, some emotions, age range and accessories, for each detected face. For more [informations](http://face-lytics.com). You can download a sample application on the [Google Play Store](https://play.google.com/store/apps/details?id=com.wassa.whatsthatface.demo) to see usage exemples for the sdk

## Installation

Download the [latest code version](https://github.com/wassafr/Facelytics-Android/archive/master.zip).

### Eclipse

1. Drag and drop the **armeabi-v7a** directory from the archive in your project navigator under **libs**.
2. Drag and drop the **wassa** directory from the archive in your project navigator under **assets**.
3. Drag and drop the **wassa-facelytics-(version).jar** from the archive in your project navigator under **libs** and include it in built-path.

### Android Studio

1. Switch to "Project" view
2. Drag and drop the **armeabi-v7a** directory from the archive in your project navigator under **app/src/main/jniLibs**.
3. Drag and drop the **wassa** directory from the archive in your project navigator under **app/src/main/assets**.
4. Drag and drop the **wassa-facelytics-(version).jar** from the archive in your project navigator under **app/libs** and include it in built-path.

## Usage

To run the example project, clone the repo.

Make sure you also see :
- [Facelytics documentation](http://wassafr.github.io/Facelytics-Android/wassa-facelytics-jdoc/).
- [Facelytics Sample - Eclipse](https://github.com/wassafr/Facelytics-Android/tree/master/sample/Sample-Eclipse/).
- [Facelytics Sample - Android Studio](https://github.com/wassafr/Facelytics-Android/tree/master/sample/Sample-AndroidStudio/).

###Basics
1. Add the following code to your **AndroidManifest.xml** 

	```xml
	
		<uses-permission
	        android:name="android.permission.CAMERA"
	        android:required="true" />
	    <uses-permission
	        android:name="android.permission.READ_EXTERNAL_STORAGE"
	        android:required="false" />
	    <uses-permission
	        android:name="android.permission.INTERNET"
	        android:required="false" />
	    <uses-permission
	        android:name="android.permission.ACCESS_NETWORK_STATE"
	        android:required="false" />
	
	    <uses-feature
	        android:name="android.hardware.camera"
	        android:required="true" />
	    <uses-feature
	        android:name="android.hardware.camera.autofocus"
	        android:required="false" />
	    <uses-feature
	        android:name="android.hardware.camera.front"
	        android:required="false" />
	    <uses-feature
	        android:name="android.hardware.camera.front.autofocus"
	        android:required="false" />
	        
    ```


2. Plugin and files loading - Add the following line to your **Application.java** or **MainActiviy.java** , onCreate()

    ```java
    
        FacelyticsUtils.loadPlugin(this);
        
    ```

3. Render - Add the following line to your **layout_activity.xml**

    ```xml
    
        <com.wassa.noyau.capture.input.KFrameRender
            android:id="@+id/render"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />
            
    ```
    
4. Allow "Keep Screen ON" - Add the following line to your onCreate.

    ```java
    
		// this.setContentView(your_layout_activity.xml);
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
    ```

5. Setup your first Facelytics service - Add the following code to your onResume()

    ```java
    
        if (!FacelyticsUtils.isInit()) 
        {
        	FacelyticsCameraServiceNative newService = (FacelyticsCameraServiceNative) FacelyticsUtils.createInstance(
				new FacelyticsCameraServiceNative(<context>, <config_file>, <licence_key>));
            newService.setRenderToMat(true);
            newService.load(KInputCamId.CAMERA_ID_FRONT);
        }
        
    ```
    ```java
    
        final FacelyticsCameraServiceNative service = (FacelyticsCameraServiceNative) FacelyticsUtils.getInstance();
        
    ```
    ```java
    
        service.addOnEventListener(nnew OnFaceListener() {
			@Override
			public void onEvent(String rawEvent) throws JSONException {
				super.onEvent(rawEvent);
				// Do something...
				
				// #1 - Retrieve basis information
				// getEvent().getTimestamp() <-- Timestamp event
				// getEvent().getFrameWidth() <-- captured frame width
				// getEvent().getFrameHeight() <-- captured frame width
				// getEvent().getFDetectTime() <-- Time to process a simple face detection
				// getEvent().getFDetectNextTick() <-- Time to wait before the next face detection
				// getEvent().getFDetectByPassed() <-- Amount of face detect thread by passed
				
				// #2 - More information available with FacelyticsFaceEvent
				// FacelyticsFaceEvent faceEvent = (FacelyticsFaceEvent) getEvent();
				
				// #3 - Retrieve basis face information
				// faceEvent.getFacesCount() <-- Detected faces count
				// faceEvent.getFace(_index) <-- Return a Face object at specified index
				// faceEvent.getFaceId(_index) <-- Return id of a face at specified index
				// faceEvent.getFaces(); <-- Return an array of detected faces
				
				// #4 - Retrieve advanced face information at specified index
				// faceEvent.getPosition(_index)
				// Eyes eyes = faceEvent.getEyes(_index)
				// Age age = faceEvent.getAge(_index)
				// Emotion emotion = faceEvent.getEmotion(_index)
				// Gender gender = faceEvent.getGender(_index)
				// Glass glass = faceEvent.getGlass(_index)
				// Motion motion = faceEvent.getMotion(_index)
			}
		});
        
    ```

6. Start the record and the preview

    ```java
    
        service.record(MyActivity.this, (KFrameRender) findViewById(R.id.render), true, true);
        
    ```

## Requirements

* Eclipse or AndroidStudio
* Android SDK 14+
* armeabi-v7a

## License

Facelytics is available under a commercial license. See the LICENSE file for more info.

## Author

Wassa, contact@wassa.fr