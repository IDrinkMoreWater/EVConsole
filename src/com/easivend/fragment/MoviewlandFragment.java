package com.easivend.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.easivend.app.business.BusPort;
import com.easivend.app.business.BusPort.BusPortFragInteraction;
import com.easivend.common.MediaFileAdapter;
import com.easivend.common.ToolClass;
import com.easivend.view.MyVideoView;
import  com.example.evconsole.R;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

public class MoviewlandFragment extends Fragment {
	//VideoView
	private MyVideoView videoView=null;
	private File filev;
	private int curIndex = 0,isClick=0;//  
    Random r=new Random(); 
    private List<String> mMusicList = new ArrayList<String>();  
    private WebView webtishiInfo;
    private ImageView ivads=null;
    private List<String> imgMusicList = new ArrayList<String>();  
    private boolean viewvideo=false;
    private final int SPLASH_DISPLAY_LENGHT = 30000; // �ӳ�30��
    private Context context;
    
    //=========================
    //fragment��activity�ص����
    //=========================
    /**
     * �������ⲿactivity������
     */
    private MovieFragInteraction listterner;
    /**
     * �����ġ���ContentFragment�����ص�activity��ʱ������ע��ص���Ϣ
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if(activity instanceof MovieFragInteraction)
        {
            listterner = (MovieFragInteraction)activity;
        }
        else{
            throw new IllegalArgumentException("activity must implements MovieFragInteraction");
        }

    }
    /**
     * ����һ������������activity����ʵ�ֵĽӿ�
     */
    public interface MovieFragInteraction
    {
        /**
         * Fragment ��Activity����ָ�����������Ը�������������
         * @param str
         */
        void switchBusiness();
    }
    @Override
    public void onDetach() {
        super.onDetach();

        listterner = null;
    }
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_movieland, container, false);  
		context=this.getActivity();//��ȡactivity��context
		videoView=(MyVideoView)view.findViewById(R.id.video);
		//�õ���ʾ����
		webtishiInfo = (WebView) view.findViewById(R.id.webtishiInfo); 
		ivads=(ImageView)view.findViewById(R.id.ivads);
		ivads.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				changefragment();
			}
		});
		listFiles(); 
		startVideo();
		/**
	     * ����������fragment������,
	     * �����塢��Fragment�����ص�activity��ʱ��ע��ص���Ϣ
	     * @param activity
	     */
		BusPort.setCallBack(new buportInterfaceImp());
		return view;
	}
	
	private class buportInterfaceImp implements BusPortFragInteraction//���ؽӿ�
	{
		/**
	     * ����������fragment������,
	     * ��������ʵ��BusPortFragInteraction�ӿ�
	     * @param activity
	     */
		@Override
		public void BusportTsxx(String str) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void BusportTbje(String str) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void BusportChjg(int sta) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void BusportSend(String str) {
			// TODO Auto-generated method stub
		}

		@Override
		public void BusportMovie() {
			// TODO Auto-generated method stub
			 ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<��ʾ������ʾ��Ϣ","log.txt");
			 showtishiInfo();
		}

		@Override
		public void BusportAds() {
			// TODO Auto-generated method stub
			ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<ˢ�¹���б�������","log.txt");
			mMusicList.clear();
			imgMusicList.clear();
			listFiles(); 
			startVideo();
		}
	}
	
	/* �����б� */  
    private void listFiles() 
    {  
    	//��������ļ�����������ļ�
		File file = new File(ToolClass.ReadAdsFile());
		File[] files = file.listFiles();
		if (files.length > 0) 
		{  
			for (int i = 0; i < files.length; i++) 
			{
			  if(!files[i].isDirectory())
			  {		
				  ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��Ʒ1ID="+files[i].toString(),"log.txt");
				  //�Ƿ���Ƶ�ļ�
				  if(MediaFileAdapter.isVideoFileType(files[i].toString())==true)
				  {
					  ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷvideoID="+files[i].toString(),"log.txt");
					  mMusicList.add(files[i].toString());
				  }
				  //�Ƿ�ͼƬ�ļ�
				  else if(MediaFileAdapter.isImgFileType(files[i].toString())==true)
				  {
					  ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷimageID="+files[i].toString(),"log.txt");
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
				changefragment();								
				return true;
			}
		});       
        
	}  
    //ͼƬ����Ƶ�л���ʾ
    private void show()
    {
    	//��Ƶ��ͼƬ�ļ���Ҫ�в���
    	if((mMusicList.size()>0)||(imgMusicList.size()>0))
    	{
	    	//������Ƶ
	    	if((viewvideo==false)&&(mMusicList.size()>0))
	    	{
	    		viewvideo=true;
	    		ivads.setVisibility(View.GONE);//ͼƬ�ر�
	    		webtishiInfo.setVisibility(View.GONE);//��ʾ�ر�
	    		videoView.setVisibility(View.VISIBLE);//��Ƶ��
	    		play();
	    	}
	    	//����ͼƬ
	    	else 
	    	{
	    		viewvideo=false;
	    		if(imgMusicList.size()>0)
	    		{
		    		videoView.setVisibility(View.GONE);//��Ƶ�ر�
		    		webtishiInfo.setVisibility(View.GONE);//��ʾ�ر�
		    		ivads.setVisibility(View.VISIBLE);//ͼƬ��
		    		showImage();
	    		}
	    		else
	    		{
	    			show();
	    		}
			}
    	}
    }
    //��ʾͼƬ
    private void showImage()
    {  
        curIndex=r.nextInt(imgMusicList.size()); 
        try 
		{
        	ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<imageID="+imgMusicList.get(curIndex),"log.txt");
        	if(checkAds(imgMusicList.get(curIndex)))
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
	                	show();
	                }
	
				}, SPLASH_DISPLAY_LENGHT);
        	}
        	else
        	{
        		show();
        	}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			show();
		}	
    }  
    //������Ƶ
    private void play()
    {  
        curIndex=r.nextInt(mMusicList.size()); 
        try 
		{
        	
        	ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<videoID="+mMusicList.get(curIndex),"log.txt");
	        if(checkAds(mMusicList.get(curIndex)))
	        {
	        	videoView.setVideoPath(mMusicList.get(curIndex));  
		        videoView.start(); 
	        }
	        else
	        {
	        	show();
	        }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}	
    }  
    
    private boolean checkAds(String CLS_URL)
    {
    	String ATT_ID="",TypeStr="";
  		int FileType=0;//1ͼƬ,2��Ƶ
  		boolean rst=false;
  		if(CLS_URL.equals("null")!=true)
  		{
  			String a[] = CLS_URL.split("/");  
  			ATT_ID=a[a.length-1];  
  			String tmp = ATT_ID;
	    	ATT_ID=tmp.substring(0,tmp.lastIndexOf("."));		    	
	    	TypeStr=tmp.substring(tmp.lastIndexOf(".")+1);
		    //�Ƿ���Ƶ�ļ�
		    if(MediaFileAdapter.isVideoFileType(tmp)==true)
		    {
		    	FileType=2;
		        ToolClass.Log(ToolClass.INFO,"EV_JNI","�����ƵATT_ID="+ATT_ID+"."+TypeStr,"log.txt");										
		    }
		    //�Ƿ�ͼƬ�ļ�
		    else if(MediaFileAdapter.isImgFileType(tmp)==true)
		    {
		    	FileType=1;
	  			ToolClass.Log(ToolClass.INFO,"EV_JNI","���ͼƬATT_ID="+ATT_ID+"."+TypeStr,"log.txt");										
	  		}
  			
		    if(ATT_ID.equals("")==true)
  			{
  				ToolClass.Log(ToolClass.INFO,"EV_JNI","���["+ATT_ID+"]��","log.txt");
  			}
		    else if(ToolClass.isAdsFile(ATT_ID,TypeStr))
			{
				ToolClass.Log(ToolClass.INFO,"EV_JNI","���["+ATT_ID+"]�Ѵ���","log.txt");
				rst=true;
			}
		    else
		    {
				ToolClass.Log(ToolClass.INFO,"EV_JNI","���["+ATT_ID+"]������","log.txt");
			}
  		}
  		return rst;
    }
    
    //��ʾ��ʾ��Ϣ
    private void showtishiInfo()
    {  
    	ivads.setVisibility(View.GONE);//ͼƬ�ر�
    	videoView.setVisibility(View.GONE);//��Ƶ�ر�
    	
		webtishiInfo.setVisibility(View.VISIBLE);//��ʾ��
		 WebSettings settings = webtishiInfo.getSettings();
	     settings.setSupportZoom(true);
	     settings.setTextSize(WebSettings.TextSize.LARGEST);
	     webtishiInfo.getSettings().setSupportMultipleWindows(true);
	     webtishiInfo.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); //���ù�������ʽ
	     webtishiInfo.getSettings().setDefaultTextEncodingName("UTF -8");//����Ĭ��Ϊutf-8
	     webtishiInfo.loadDataWithBaseURL(null,"�����ڴ�!", "text/html; charset=UTF-8","utf-8", null);//����д��������ȷ���Ľ���
		    
    	//��ʱ10s
        new Handler().postDelayed(new Runnable() 
		{
            @Override
            public void run() 
            {
            	show();
            }

		}, SPLASH_DISPLAY_LENGHT);
    }  
    
    
    //�л�������fragment
    private void changefragment()
    {
    	ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<goto=businesslandFragment","log.txt");
    	//�������fragment��activity���ͻص���Ϣ
    	listterner.switchBusiness();
    }
    
//    //��ͣ
//    @Override  
//    protected void onPause() {  
//        // TODO Auto-generated method stub  
//        super.onPause();  
//        if(videoView!=null&&videoView.isPlaying()){  
//            videoView.pause();  
//        }  
//          
//    }  
//  
//    //�ָ�
//    @Override  
//    protected void onResume() {  
//        // TODO Auto-generated method stub  
//        super.onResume();  
//        startVideo();  
//    } 
}
