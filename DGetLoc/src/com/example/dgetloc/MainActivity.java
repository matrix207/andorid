package com.example.dgetloc;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Process;

public class MainActivity extends Activity {

	private Button mStartBtn;
	private Button mLocBtn;
	private Button mPoiBtn;
	private boolean mIsStart;
	private static int count = 1;
	
	private static String TAG = "Dennis Loc Test";
	
	private LocationClient mLocClient;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mLocClient = ((Location)getApplication()).mLocationClient;
		
		mStartBtn = (Button)findViewById(R.id.StartBtn);
		mLocBtn = (Button)findViewById(R.id.locBtn);
		mPoiBtn = (Button)findViewById(R.id.PoiBtn);
		
		mStartBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!mIsStart) {
					setLocationOption();
					mLocClient.start();
					mIsStart = true;
					mStartBtn.setText("Stop");
				} else {
					mLocClient.stop();
					mIsStart = false;
					mStartBtn.setText("Start");
				}
				Log.d(TAG, "... mStartBtn onClick... pid="+Process.myPid()+" count="+count++);
			}
			
		});
		
		mLocBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mLocClient != null && mLocClient.isStarted()) {
					setLocationOption();
					mLocClient.requestLocation();
				} else {
					Log.d(TAG, "locClient is null or not started");
				}
				Log.d(TAG, "... mStartBtn onClick... pid="+Process.myPid()+" count="+count++);
			}
		});
		
		mPoiBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mLocClient != null && mLocClient.isStarted()) {
					setLocationOption();
					mLocClient.requestPoi();
				}
				Log.d(TAG, "... mStartBtn onClick... pid="+Process.myPid()+" count="+count++);
			}
		});
	}
	
	@Override
	public void onDestroy() {
		if (mLocClient != null && mLocClient.isStarted()) {
			mLocClient.stop();
		}
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void setLocationOption() {
	    LocationClientOption option = new LocationClientOption();
	    option.setOpenGps(true);
	    option.setAddrType("all");//返回的定位结果包含地址信息
	    option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
	    option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
	    option.disableCache(true);//禁止启用缓存定位
	    option.setPoiNumber(5);	//最多返回POI个数
	    option.setPoiDistance(1000); //poi查询距离
	    option.setPoiExtraInfo(true); //是否需要POI的电话和地址等详细信息
	    mLocClient.setLocOption(option);
	}

}
