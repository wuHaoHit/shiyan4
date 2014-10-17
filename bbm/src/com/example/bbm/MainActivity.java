package com.example.bbm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.example.bbm.AutoListView.OnLoadListener;
import com.example.bbm.AutoListView.OnRefreshListener;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity implements OnRefreshListener,
OnLoadListener
{
   private List<bbmInformationEntity> entityList=new ArrayList<bbmInformationEntity>();
   private bbmInformationAdapter entityAdapter;
   private AutoListView listview;
   private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			List<bbmInformationEntity> result = (List<bbmInformationEntity>) msg.obj;
			switch (msg.what) {
			case AutoListView.REFRESH:
				listview.onRefreshComplete();
				entityList.clear();
				entityList.addAll(result);
				break;
			case AutoListView.LOAD:
				listview.onLoadComplete();
				entityList.addAll(result);
				break;
			}
			listview.setResultSize(result.size());
			entityAdapter.notifyDataSetChanged();
		};
	};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        entityList=new ArrayList<bbmInformationEntity>();
        listview=(AutoListView)findViewById(R.id.bbm_listview);
        init();
        Button b=(Button)findViewById(R.id.bbm_publish);
        b.setOnClickListener(new OnClickListener()
        {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MainActivity.this,bbmPublishActivity.class);
				startActivity(intent);
			}
        	
        });
        	
        
    }
   public void init()
   {
	   
	    entityAdapter=new bbmInformationAdapter(this,entityList);
	    listview.setAdapter(entityAdapter);
	    listview.setOnRefreshListener(this);
		listview.setOnLoadListener(this);
		initData();
   }
   private void initData() {
		loadData(AutoListView.REFRESH);
	}
   private void loadData(final int what) {
		// 这里模拟从服务器获取数据
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(700);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Message msg = handler.obtainMessage();
				msg.what = what;
				msg.obj = getData();
				handler.sendMessage(msg);
			}
		}).start();
	}
	// 测试数据
	public List<bbmInformationEntity> getData() {
		List<bbmInformationEntity> list=new ArrayList<bbmInformationEntity>();
		for(int i=0;i<10;i++)
		   {
			   bbmInformationEntity entity=new bbmInformationEntity();
			   entity.setContent(i+"");
			   entity.setIsVoice(false);
			   entity.setPhoneNumber("13274603175"+i);
			   list.add(entity);
		   }
		   bbmInformationEntity entity=new bbmInformationEntity();
		   entity.setContent("fdsfsdf"+"");
		   entity.setIsVoice(true);
		   entity.setVoiceTime(7);
		   entity.setPhoneNumber("13274603175m");
		   list.add(entity);
		   return list;
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		loadData(AutoListView.LOAD);
	}
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		loadData(AutoListView.REFRESH);
	}
    
}
