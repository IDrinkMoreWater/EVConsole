package com.easivend.app.business;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.easivend.app.maintain.MaintainActivity;
import com.easivend.common.MediaFileAdapter;
import com.easivend.common.ToolClass;
import com.easivend.evprotocol.EVprotocolAPI;
import com.example.evconsole.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class Business extends Activity
{
	TextView txtadsTip=null;
	Button btnads1=null,btnads2=null,btnads3=null,btnads4=null,btnads5=null,btnads6=null,
		   btnads7=null,btnads8=null,btnads9=null,btnadscancel=null,btnads0=null,btnadsenter=null,
		   btnadsclass=null,btnadscuxiao=null,btnadsbuysale=null,btnadsquhuo=null;	
	Intent intent=null;
	//VideoView
	private VideoView videoView=null;
	private File filev;
	private int curIndex = 0,isClick=0;//  
    Random r=new Random(); 
    private List<String> mMusicList = new ArrayList<String>();  
    //ImageView
    private ImageView ivads=null;
    private List<String> imgMusicList = new ArrayList<String>();  
    private boolean viewvideo=false;
    private final int SPLASH_DISPLAY_LENGHT = 30000; // �ӳ�30��
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.business);
		videoView=(VideoView)findViewById(R.id.video);
		ivads=(ImageView)findViewById(R.id.ivads);
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
    	ivads.setLayoutParams(linearParams);
    	
		listFiles(); 
		startVideo();		
		//=======
		//����ģ��
		//=======
		txtadsTip = (TextView) findViewById(R.id.txtadsTip);
		btnads1 = (Button) findViewById(R.id.btnads1);
		btnads2 = (Button) findViewById(R.id.btnads2);
		btnads3 = (Button) findViewById(R.id.btnads3);
		btnads4 = (Button) findViewById(R.id.btnads4);
		btnads5 = (Button) findViewById(R.id.btnads5);
		btnads6 = (Button) findViewById(R.id.btnads6);
		btnads7 = (Button) findViewById(R.id.btnads7);
		btnads8 = (Button) findViewById(R.id.btnads8);
		btnads9 = (Button) findViewById(R.id.btnads9);
		btnadscancel = (Button) findViewById(R.id.btnadscancel);
		btnads0 = (Button) findViewById(R.id.btnads0);
		btnadsenter = (Button) findViewById(R.id.btnadsenter);
		btnadsclass = (Button) findViewById(R.id.btnadsclass);
		btnadsclass.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	intent = new Intent(Business.this, BusgoodsClass.class);// ʹ��Accountflag���ڳ�ʼ��Intent
                startActivity(intent);// ��Accountflag
		    }
		});
		btnadscuxiao = (Button) findViewById(R.id.btnadscuxiao);
		btnadsbuysale = (Button) findViewById(R.id.btnadsbuysale);
		btnadsquhuo = (Button) findViewById(R.id.btnadsquhuo);
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
				  ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��Ʒ1ID="+files[i].toString());
				  //�Ƿ���Ƶ�ļ�
				  if(MediaFileAdapter.isVideoFileType(files[i].toString())==true)
				  {
					  ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷvideoID="+files[i].toString());
					  mMusicList.add(files[i].toString());
				  }
				  //�Ƿ�ͼƬ�ļ�
				  else if(MediaFileAdapter.isImgFileType(files[i].toString())==true)
				  {
					  ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷimageID="+files[i].toString());
					  imgMusicList.add(files[i].toString());
				  }
			  }
			}
		}    
    } 
    //�򿪲�����
    private void startVideo()
    { 
    	videoView.requestFocus(); 
    	show();  
    	//��������¼�������
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {  
  
                    @Override  
                    public void onCompletion(MediaPlayer mp) {  
                        // TODO Auto-generated method stub  
                    	show();//��������ټ�����һ��  
                    }  
                });  
        //���ų����¼�������        
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {  
              
            @Override  
            public boolean onError(MediaPlayer mp, int what, int extra) {  
                // TODO Auto-generated method stub  
            	show();//���ų����ټ�����һ��  
                return true;  
            }  
        });  
        
        //�����¼�������
        videoView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
//				if(isClick==0)
//				{
//					onPause();	
//					isClick=1;
//				}
//				else 
//				{
//					onResume();	
//					isClick=0;
//				}
				return true;
			}
		});       
        
	}  
    //ͼƬ����Ƶ�л���ʾ
    private void show()
    {
    	//������Ƶ
    	if(viewvideo==false)
    	{
    		viewvideo=true;
    		ivads.setVisibility(View.GONE);//ͼƬ�ر�
    		videoView.setVisibility(View.VISIBLE);//��Ƶ��
    		play();
    	}
    	//����ͼƬ
    	else 
    	{
    		viewvideo=false;
    		videoView.setVisibility(View.GONE);//��Ƶ�ر�
    		ivads.setVisibility(View.VISIBLE);//ͼƬ��
    		showImage();
		}
    }
    //��ʾͼƬ
    private void showImage()
    {  
        curIndex=r.nextInt(imgMusicList.size()); 
        try 
		{
	        /*ΪʲôͼƬһ��Ҫת��Ϊ Bitmap��ʽ�ģ��� */
	        Bitmap bitmap = ToolClass.getLoacalBitmap(imgMusicList.get(curIndex)); //�ӱ���ȡͼƬ(��cdcard�л�ȡ)  //
	        ivads.setImageBitmap(bitmap);// ����ͼ��Ķ�����ֵ
	        //��ʱ10s
	        new Handler().postDelayed(new Runnable() 
			{
                @Override
                public void run() 
                {
                	viewvideo=true;
            		ivads.setVisibility(View.GONE);//ͼƬ�ر�
            		videoView.setVisibility(View.VISIBLE);//��Ƶ��
            		play();
                }

			}, SPLASH_DISPLAY_LENGHT);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}	
    }  
    //������Ƶ
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
