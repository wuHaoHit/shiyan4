package com.example.bbm;


import java.io.File;
import java.util.HashMap;
import java.util.Map;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;

public class bbmPublishActivity extends Activity
{
     ImageButton voice;
     MyVoice myvoice=new MyVoice("test");
     Button button;
     boolean isVoice=false;
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-gener ated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bbm_publish);
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}

		addVoiceListener();
	    addListener();
	}
	public void addListener()
	{
		button=(Button)findViewById(R.id.bbm_publish_confirm);
		button.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				System.out.println("发布");
				if(isVoice==false)
				{
					HashMap<String,String> map=new HashMap<String,String>();
					map.put("电话","13274603175");
					map.put("发布内容", "宠物");
					SocketHttpRequester.postString("http://192.168.1.125:3000/publishhelp", map);
				}
				else
				{
					 final File uploadFile = new File(myvoice.getAudioPath());  
		             //上传音频文件  
		        	 Map<String, String> params = new HashMap<String, String>();  

		             params.put("电话", "13274603175");  
		            
		             FormFile formfile = new FormFile("test.3gp", uploadFile, "3gp", "image/jpeg");  
		             try {
						//UploadUtil.uploadFile(uploadFile, "http://192.168.1.125:3000/publishInformation");
		            	 SocketHttpRequester.post("http://192.168.1.125:3000/publishhelp", params, formfile);
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("上传出错");
					}  
				}
				
			}
		});
	}
	public void addVoiceListener()
	   {
		   voice=(ImageButton)findViewById(R.id.bbm_publish_voice);
		   voice.setOnTouchListener(new OnTouchListener()
			{

				@Override
				public boolean onTouch(View arg0, MotionEvent event) {
					// TODO Auto-generated method stub
					switch(event.getAction())
					{
	                case MotionEvent.ACTION_DOWN:
	                	isVoice=true;
						System.out.println("手机按下");
						voice.setBackgroundResource(R.drawable.voice_end);
						//filename=Utils.getNowDate();
						myvoice=new MyVoice("test");
						myvoice.startRecording();
						break;
					case MotionEvent.ACTION_UP:
						System.out.println("抬起手指");
						voice.setBackgroundResource(R.drawable.voice_start);
						myvoice.stopRecording();
						//byte[] res=myvoice.getAudioBytes(filename);
						//myvoice.startPlaying();
						//sendMessage("##",true,1);
						//sendVoiceToMannager(res);
					   break;
					}
				return true;
				}
			});
	   }
}
