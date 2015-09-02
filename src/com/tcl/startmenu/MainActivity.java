package com.tcl.startmenu;


import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SeekBar;

public class MainActivity extends Activity {

	MySurfaceView mySurfaceView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //设置为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        setContentView(R.layout.activity_main);
        mySurfaceView = new MySurfaceView(this);
        mySurfaceView.requestFocus();
        mySurfaceView.setFocusableInTouchMode(true);
        
        LinearLayout ll = (LinearLayout) findViewById(R.id.main_liner);
        ll.addView(mySurfaceView);
        
      //普通拖拉条被拉动的处理代码
        SeekBar sb=(SeekBar)this.findViewById(R.id.SeekBar);
        sb.setOnSeekBarChangeListener(
            new SeekBar.OnSeekBarChangeListener()
            {
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					//mySurfaceView.setLightOffset((seekBar.getMax()/2.0f-progress)/(seekBar.getMax()/2.0f)*-4);
					Constant.sacleX = (50 + progress) * 0.01f;
					Constant.sacleY = (50 + progress) * 0.01f;

				}
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {	}
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) { }            	
            }
        );
        
        DisplayMetrics dm = getResources().getDisplayMetrics();
    	
    	Constant.SCREEN_WIDTH=dm.widthPixels;
    	Constant.SCREEN_HEIGHT=dm.heightPixels;
    	
    	Log.d("SCREEN_WIDTH", "SCREEN_WIDTH = " + Constant.SCREEN_WIDTH);
    	Log.d("SCREEN_HEIGHT", "SCREEN_HEIGHT = " + Constant.SCREEN_HEIGHT);
        
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        mySurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mySurfaceView.onPause(); 
    }
}
