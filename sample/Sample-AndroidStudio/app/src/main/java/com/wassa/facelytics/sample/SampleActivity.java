package com.wassa.facelytics.sample;

import java.io.File;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.wassa.noyau.capture.input.KFrameRender;
import com.wassa.facelytics.all.FacelyticsCameraServiceNative;
import com.wassa.facelytics.all.FacelyticsUtils;
import com.wassa.facelytics.common.FacelyticsConfigActivity;
import com.wassa.facelytics.common.FacelyticsService;
import com.wassa.facelytics.common.FacelyticsServiceNative.SessionEndedListener;
import com.wassa.facelytics.events.FacelyticsEvent;
import com.wassa.facelytics.events.FacelyticsFaceEvent;
import com.wassa.facelytics.events.FacelyticsFaceEvent.Gender;
import com.wassa.facelytics.events.FacelyticsFaceEvent.Position;
import com.wassa.facelytics.events.OnEventListener;
import com.wassa.facelytics.events.OnFaceListener;

public class SampleActivity extends Activity {

	private String TAG = "SampleApp";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_sample);
	}
	
	/**
	 * Alloc a new instance of {@link OnFaceListener}.  
	 */
	final OnEventListener<?> eventListener = new OnFaceListener() {
		@Override
		public void onEvent(String rawEvent) throws JSONException {
			super.onEvent(rawEvent);
			SampleActivity.this.doSomethingWithEvent(getEvent());
		}
	};
	
	final SessionEndedListener sessionEndedListener = new SessionEndedListener() {
		@Override
		public void ended() {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(SampleActivity.this, "Session ended !", Toast.LENGTH_SHORT).show();
					finish();
				}
			});
		}
	};
	
	@Override
	protected void onResume() {
		super.onResume();
		
		/**
		 * Bind your activity with a {@link KFrameRender} view.
		 */
		KFrameRender renderView = ((KFrameRender) findViewById(R.id.render));
		
		/**
		 * Allow center cropped rendering.
		 */
		renderView.setCenterCropped(true);
		
		/**
		 * Start the capture and the detection in a row.
		 */
		FacelyticsCameraServiceNative service = SampleApplication.initService_4_SampleActivity(SampleActivity.this, eventListener, sessionEndedListener);
		service.record(this, renderView, true, true);
	}
	
	@Override
	public void onPause() {
		FacelyticsUtils.pauseInstance();
	    super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		FacelyticsUtils.destroyInstance();
		super.onDestroy();
	}
	
	/**
	 * Place to do things with raised detection event !
	 */
	private void doSomethingWithEvent(FacelyticsEvent _event) throws JSONException
	{
		if (_event == null) return;

		// #1 - Retrieve basis information
		// _event.getTimestamp() <-- Timestamp event
		// _event.getFrameWidth() <-- captured frame width
		// _event.getFrameHeight() <-- captured frame width
		// _event.getFDetectTime() <-- Time to process a simple face detection
		// _event.getFDetectNextTick() <-- Time to wait before the next face detection
		// _event.getFDetectByPassed() <-- Amount of face detect thread by passed
		
		// #2 - More information available with FacelyticsFaceEvent
		// FacelyticsFaceEvent faceEvent = (FacelyticsFaceEvent) _event;
		
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
		
		FacelyticsFaceEvent faceEvent = (FacelyticsFaceEvent) _event;
		
		// Example (1) - Retrieve gender information
		for (int cursor = 0; cursor < faceEvent.getFacesCount(); cursor++) 
		{
			Gender gender = faceEvent.getGender(cursor);
			
			/** True if this module is already activate */
			if (gender.actif)
			{
				// A simple exemple of switch case with gender
//				switch (gender.current)
//				{
//					case male:
//						break;
//					case female:
//						break;
//					case undetermined:
//						break;
//				}
				Log.v(TAG, "# Current gender detected (for index: " + cursor + "): " + gender.current);
			}
		}
		
		// Example (2) - Retrieve orientation tracking information
		for (int cursor = 0; cursor < faceEvent.getFacesCount(); cursor++) 
		{
			Position position = faceEvent.getPosition(cursor);
			
			// orientation x & y values [-1 ; 1], origin=0
			// rotation values [45° ; 45°], origin=0°
			// left & right eye direction values [0 : 1], origin=0.5
			// left & right eye wink values true, false
			
			Log.v(TAG, "# Current tracking information (for index: " + cursor + "): \n"
				+ "- position_x: " + position.x + " \n"
				+ "- position_y: " + position.y + " \n"
				+ "- position_width: " + position.width + " \n"
				+ "- position_height: " + position.height + " \n"
				+ "- rotation: " + position.rotation + " \n"
				+ "- orientation_x: " + position.orientation_x + " \n"
				+ "- orientation_y: " + position.orientation_y);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.actions_sample, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    	case R.id.action_switch:
	    	{
	    		
	    		if (FacelyticsUtils.isInit() && FacelyticsUtils.getInstance() instanceof FacelyticsCameraServiceNative) { 
					SampleApplication.CAM_ID = ((FacelyticsCameraServiceNative) FacelyticsUtils.getInstance()).getCurrentCamera().getCameraIndex().inverse();
				}	
				FacelyticsUtils.destroyInstance();
				SampleApplication
					.initService_4_SampleActivity(SampleActivity.this, eventListener, sessionEndedListener)
					.record(SampleActivity.this, (KFrameRender) findViewById(R.id.render), true, true);
	    		
	    		return true;
	    	}
	    	
	    	case R.id.action_config:
	    	{
	    		Intent intent = new Intent(SampleActivity.this, FacelyticsConfigActivity.class);
	    		Bundle bundle = new Bundle();
	    		bundle.putString(FacelyticsConfigActivity.EXTRA_CONFIGS_PATH, getFilesDir().getAbsolutePath() + File.separator + FacelyticsService.ASSET_FOLDER);
	    		bundle.putString(FacelyticsConfigActivity.EXTRA_FILE_FILTER_SEPARATOR, ";");
	    		bundle.putString(FacelyticsConfigActivity.EXTRA_FILE_FILTER_CONTAINS, "config");
	    		bundle.putString(FacelyticsConfigActivity.EXTRA_FILE_FILTER_NOT_CONTAINS, ".");
	    		bundle.putString(FacelyticsConfigActivity.EXTRA_COLOR_CELL_A, "#f5f9fd");
	    		bundle.putString(FacelyticsConfigActivity.EXTRA_COLOR_CELL_B, "#ffffff");
	    		bundle.putString(FacelyticsConfigActivity.EXTRA_COLOR_HIGHLIGHT, "#51A7F9");
				intent.putExtras(bundle);
				SampleActivity.this.startActivity(intent);
	    		return true;
	    	}
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
