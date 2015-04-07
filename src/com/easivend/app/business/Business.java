package com.easivend.app.business;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.easivend.common.ToolClass;
import com.example.evconsole.R;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class Business extends Activity
{
	//VideoView
	private VideoView videoView=null;
	private File filev;
	private int curIndex = 0,isClick=0;//  
    Random r=new Random(); 
    private List<String> mMusicList = new ArrayList<String>();  
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.business);
		videoView=(VideoView)findViewById(R.id.video);
		//��̬���ÿؼ��߶�
    	//
    	DisplayMetrics  dm = new DisplayMetrics();  
        //ȡ�ô�������  
        getWindowManager().getDefaultDisplay().getMetrics(dm);  
        //���ڵĿ��  
        int screenWidth = dm.widthPixels;          
        //���ڸ߶�  
        int screenHeight = dm.heightPixels;      
        ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��Ļ"+screenWidth
				+"],["+screenHeight+"]");	
		
    	LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) videoView.getLayoutParams(); // ȡ�ؼ�videoView��ǰ�Ĳ��ֲ���
    	//linearParams.height =  (int)screenHeight/3;// ���ؼ��ĸ�ǿ�����75����
    	linearParams.weight= screenHeight;
    	videoView.setLayoutParams(linearParams); // ʹ���úõĲ��ֲ���Ӧ�õ��ؼ�mGrid2
    	
		listFiles(); 
		startVideo();		
	}
	
	 /* �����б� */  
    private void listFiles() 
    {  
    	//��������ļ�����������ļ�
		File file = new File("/sdcard/ads/");
		File[] files = file.listFiles();
		if (files.length > 0) 
		{  
			for (int i = 0; i < files.length; i++) 
			{
			  if(!files[i].isDirectory())
			  {
				  ToolClass.Log(ToolClass.INFO,"EV_JNI",files[i].toString());
				  mMusicList.add(files[i].toString());
			  }
			}
		}    
    } 
    //�򿪲�����
    private void startVideo()
    { 
    	videoView.requestFocus(); 
    	play();  
    	//��������¼�������
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {  
  
                    @Override  
                    public void onCompletion(MediaPlayer mp) {  
                        // TODO Auto-generated method stub  
                        play();//��������ټ�����һ��  
                    }  
                });  
        //���ų����¼�������        
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {  
              
            @Override  
            public boolean onError(MediaPlayer mp, int what, int extra) {  
                // TODO Auto-generated method stub  
                play();//���ų����ټ�����һ��  
                return true;  
            }  
        });  
        
        //�����¼�������
        videoView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(isClick==0)
				{
					onPause();	
					isClick=1;
				}
				else 
				{
					onResume();	
					isClick=0;
				}
				return true;
			}
		});       
        
	}  
    //����
    private void play()
    {  
        curIndex=r.nextInt(mMusicList.size()); 
        try 
		{
	        videoView.setVideoPath(mMusicList.get(curIndex));  
	        videoView.start(); 
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}	
    }  
    
    //��ͣ
    @Override  
    protected void onPause() {  
        // TODO Auto-generated method stub  
        super.onPause();  
        if(videoView!=null&&videoView.isPlaying()){  
            videoView.pause();  
        }  
          
    }  
  
    //�ָ�
    @Override  
    protected void onResume() {  
        // TODO Auto-generated method stub  
        super.onResume();  
        startVideo();  
    } 
}
