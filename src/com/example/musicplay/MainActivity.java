package com.example.musicplay;

import android.os.Bundle;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView tvTitle,tvAuthor;
	private Button btnPlay,btnStop;
	public static final String CONTROL="com.example.musicplay.control";
	public static final String UPDATE="com.example.musicplay.update";
	//定义广播接收器，控制播放，暂停
	ActivityReceiver activityReceiver;
	int status=0x11;
	//定义播放状态，0x11,未播放；0x12，正在播放；0x13，暂停
	String []titleStrs=new String []{
			"老男孩","春天里","在路上"};
	String [] authorStrs=new String[]{
			"筷子兄弟","汪峰","刘欢"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {


		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btnPlay=(Button) this.findViewById(R.id.btnPlay);
		btnStop=(Button) this.findViewById(R.id.btnStop);
		tvTitle=(TextView) this.findViewById(R.id.tvTitle);
		tvAuthor=(TextView) this.findViewById(R.id.tvAuthor);
		
		
		btnPlay.setOnClickListener((OnClickListener) this);
		btnStop.setOnClickListener((OnClickListener) this);
		//为两个Button添加监听器
		
		 activityReceiver = new ActivityReceiver();
			// 创建IntentFilter
			IntentFilter filter = new IntentFilter(UPDATE);
			// 注册BroadcastReceiver
			registerReceiver(activityReceiver, filter);
			Intent intent = new Intent(this, MusicService.class);
			startService(intent);
			// 启动后台Service
		
	}
	public void onClick(View source) {

		Intent intent = new Intent(CONTROL);
		// 创建Intent
		System.out.println(source.getId());
		System.out.println(source.getId() == R.id.btnPlay);
		switch (source.getId()) {
		
		case R.id.btnPlay:
			intent.putExtra("control", 1);
			break;
			// 按下播放/暂停按钮
		
		case R.id.btnStop:
			intent.putExtra("control", 2);
			break;
			// 按下停止按钮
		}
		
		sendBroadcast(intent);
		// 发送广播 ，将被Service组件中的BroadcastReceiver接收到
	}
	 public class ActivityReceiver extends BroadcastReceiver {

			public void onReceive(Context context, Intent intent) {

			
				int update = intent.getIntExtra("update", -1);
				// 获取Intent中的update消息，update代表播放状态，默认为-1	
				int current = intent.getIntExtra("current", -1);
				// 获取Intent中的current消息，current代表当前正在播放的歌曲，默认为-1
				if (current >= 0) {
					tvTitle.setText(titleStrs[current]);
					tvAuthor.setText(authorStrs[current]);
				}
				switch (update) {
				case 0x11:
					btnPlay.setBackgroundResource(R.drawable.stop);
					status = 0x11;
					break;
				// 控制系统进入播放状态
				case 0x12:
					// 播放状态下设置使用暂停图标
					btnPlay.setImageResource(R.drawable.pause);
					// 设置当前状态
					status = 0x12;
					break;
				// 控制系统进入暂停状态
				case 0x13:
					// 暂停状态下设置使用播放图标
					btnPlay.setImageResource(R.drawable.play);
					// 设置当前状态
					status = 0x13;
					break;
				}
			}
	 }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        
        

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
