package com.tcl.startmenu;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

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
        
    }
}
