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
	//����㲥�����������Ʋ��ţ���ͣ
	ActivityReceiver activityReceiver;
	int status=0x11;
	//���岥��״̬��0x11,δ���ţ�0x12�����ڲ��ţ�0x13����ͣ
	String []titleStrs=new String []{
			"���к�","������","��·��"};
	String [] authorStrs=new String[]{
			"�����ֵ�","����","����"};

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
		//Ϊ����Button��Ӽ�����
		
		 activityReceiver = new ActivityReceiver();
			// ����IntentFilter
			IntentFilter filter = new IntentFilter(UPDATE);
			// ע��BroadcastReceiver
			registerReceiver(activityReceiver, filter);
			Intent intent = new Intent(this, MusicService.class);
			startService(intent);
			// ������̨Service
		
	}
	public void onClick(View source) {

		Intent intent = new Intent(CONTROL);
		// ����Intent
		System.out.println(source.getId());
		System.out.println(source.getId() == R.id.btnPlay);
		switch (source.getId()) {
		
		case R.id.btnPlay:
			intent.putExtra("control", 1);
			break;
			// ���²���/��ͣ��ť
		
		case R.id.btnStop:
			intent.putExtra("control", 2);
			break;
			// ����ֹͣ��ť
		}
		
		sendBroadcast(intent);
		// ���͹㲥 ������Service����е�BroadcastReceiver���յ�
	}
	 public class ActivityReceiver extends BroadcastReceiver {

			public void onReceive(Context context, Intent intent) {

			
				int update = intent.getIntExtra("update", -1);
				// ��ȡIntent�е�update��Ϣ��update������״̬��Ĭ��Ϊ-1	
				int current = intent.getIntExtra("current", -1);
				// ��ȡIntent�е�current��Ϣ��current����ǰ���ڲ��ŵĸ�����Ĭ��Ϊ-1
				if (current >= 0) {
					tvTitle.setText(titleStrs[current]);
					tvAuthor.setText(authorStrs[current]);
				}
				switch (update) {
				case 0x11:
					btnPlay.setBackgroundResource(R.drawable.stop);
					status = 0x11;
					break;
				// ����ϵͳ���벥��״̬
				case 0x12:
					// ����״̬������ʹ����ͣͼ��
					btnPlay.setImageResource(R.drawable.pause);
					// ���õ�ǰ״̬
					status = 0x12;
					break;
				// ����ϵͳ������ͣ״̬
				case 0x13:
					// ��ͣ״̬������ʹ�ò���ͼ��
					btnPlay.setImageResource(R.drawable.play);
					// ���õ�ǰ״̬
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
