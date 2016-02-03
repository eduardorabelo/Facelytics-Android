package com.wassa.facelytics.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SampleActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_sample);
		
		findViewById(R.id.bt_camera).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(SampleActivity.this, SampleCameraActivity.class));
			}
		});
		
		findViewById(R.id.bt_bitmap).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(SampleActivity.this, SampleBitmapActivity.class));
			}
		});
	}
	
}