# Facelytics

Facelytics is an SDK allowing mobile apps to detect face criterias of people by analyzing the front video feed in realtime. Facelytics is able to track multiple faces and then detect gender, some emotions, age range and accessories, for each detected face. For more [informations](http://face-lytics.com). You can download a sample application on the [Google Play Store](https://play.google.com/store/apps/details?id=com.wassa.whatsthatface.demo) to see usage exemples for the sdk

## Installation

1. Download the [latest code version](https://github.com/wassafr/Facelytics-Android/archive/master.zip).
2. Drag and drop the **armeabi-v7a** directory from the archive in your project navigator under **libs**.
3. Drag and drop the **wassa** directory from the archive in your project navigator under **assets**.
4. Drag and drop the **wassa-facelytics-(version).jar** from the archive in your project navigator under **libs** and include it in built-path.

## Usage

To run the example project, clone the repo.

Make sure you also see [Facelytics documentation](http://wassafr.github.io/Facelytics-Android/wassa-facelytics-jdoc/).

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


2. Plugin and files loading - Add the following line to your **Application.java** , onCreate()

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
			}
		});
        
    ```

6. Start the record and the preview

    ```java
    
        service.record(MyActivity.this, (KFrameRender) findViewById(R.id.render), true, true);
        
    ```

## Requirements

* Eclipse 4.3+
* Android SDK 14+
* armeabi-v7a

## License

Facelytics is available under a commercial license. See the LICENSE file for more info.

## Author

Wassa, contact@wassa.fr
