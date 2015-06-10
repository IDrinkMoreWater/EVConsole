package com.easivend.app.business;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.easivend.app.maintain.GoodsProSet;
import com.easivend.app.maintain.MaintainActivity;
import com.easivend.common.MediaFileAdapter;
import com.easivend.common.OrderDetail;
import com.easivend.common.ToolClass;
import com.easivend.dao.vmc_classDAO;
import com.easivend.dao.vmc_columnDAO;
import com.easivend.dao.vmc_productDAO;
import com.easivend.evprotocol.EVprotocolAPI;
import com.easivend.model.Tb_vmc_product;
import com.example.evconsole.R;

import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.text.StaticLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class Business extends Activity
{
	TextView txtadsTip=null;
	ImageButton btnads1=null,btnads2=null,btnads3=null,btnads4=null,btnads5=null,btnads6=null,
			   btnads7=null,btnads8=null,btnads9=null,btnadscancel=null,btnadsenter=null;
	ImageButton btnadsclass=null,btnadscuxiao=null,btnadsbuysale=null,btnadsquhuo=null,btnads0=null;	
	Intent intent=null;
	private static int count=0;
	private static String huo="";
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
    //���ͳ���ָ��
    private String proID = null;
	private String productID = null;
	private String proImage = null;
	private String cabID = null;
	private String huoID = null;
    private String prosales = null;    
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// ��title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // ȫ��
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.business);
		videoView=(VideoView)findViewById(R.id.video);
		ivads=(ImageView)findViewById(R.id.ivads);
//		//��̬���ÿؼ��߶�
//    	//
//    	DisplayMetrics  dm = new DisplayMetrics();  
//        //ȡ�ô�������  
//        getWindowManager().getDefaultDisplay().getMetrics(dm);  
//        //���ڵĿ��  
//        int screenWidth = dm.widthPixels;          
//        //���ڸ߶�  
//        int screenHeight = dm.heightPixels;      
//        ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��Ļ"+screenWidth
//				+"],["+screenHeight+"]");	
//		
//    	LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) videoView.getLayoutParams(); // ȡ�ؼ�videoView��ǰ�Ĳ��ֲ���
//    	//linearParams.height =  (int)screenHeight/3;// ���ؼ��ĸ�ǿ�����75����
//    	linearParams.weight= screenHeight;
//    	videoView.setLayoutParams(linearParams); // ʹ���úõĲ��ֲ���Ӧ�õ��ؼ�mGrid2
//    	ivads.setLayoutParams(linearParams);
    	
		listFiles(); 
		startVideo();		
		//=======
		//����ģ��
		//=======
		txtadsTip = (TextView) findViewById(R.id.txtadsTip);	
		btnads1 = (ImageButton) findViewById(R.id.btnads1);		
		btnads1.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	chuhuo("1",1);
		    }
		});
		btnads2 = (ImageButton) findViewById(R.id.btnads2);
		btnads2.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	chuhuo("2",1);
		    }
		});
		btnads3 = (ImageButton) findViewById(R.id.btnads3);
		btnads3.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	chuhuo("3",1);
		    }
		});
		btnads4 = (ImageButton) findViewById(R.id.btnads4);
		btnads4.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	chuhuo("4",1);
		    }
		});
		btnads5 = (ImageButton) findViewById(R.id.btnads5);
		btnads5.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	chuhuo("5",1);
		    }
		});
		btnads6 = (ImageButton) findViewById(R.id.btnads6);
		btnads6.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	chuhuo("6",1);
		    }
		});
		btnads7 = (ImageButton) findViewById(R.id.btnads7);
		btnads7.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	chuhuo("7",1);
		    }
		});
		btnads8 = (ImageButton) findViewById(R.id.btnads8);
		btnads8.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	chuhuo("8",1);
		    }
		});
		btnads9 = (ImageButton) findViewById(R.id.btnads9);
		btnads9.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	chuhuo("9",1);
		    }
		});
		btnads0 = (ImageButton) findViewById(R.id.btnads0);
		btnads0.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	chuhuo("0",1);
		    }
		});
		btnadscancel = (ImageButton) findViewById(R.id.btnadscancel);
		btnadscancel.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	chuhuo("0",0);
		    }
		});
		btnadsenter = (ImageButton) findViewById(R.id.btnadsenter);
		btnadsclass = (ImageButton) findViewById(R.id.btnadsclass);
		btnadsclass.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	vmc_classDAO classdao = new vmc_classDAO(Business.this);// ����InaccountDAO����
		    	long count=classdao.getCount();
		    	ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��Ʒ��������="+count);
		    	if(count>0)
		    	{
			    	intent = new Intent(Business.this, BusgoodsClass.class);// ʹ��Accountflag���ڳ�ʼ��Intent
	                startActivity(intent);// ��Accountflag
		    	}
		    	else
		    	{
		    		intent = new Intent(Business.this, Busgoods.class);// ʹ��Accountflag���ڳ�ʼ��Intent
                	intent.putExtra("proclassID", "");
                	startActivity(intent);// ��Accountflag
		    	}
		    }
		});
		btnadscuxiao = (ImageButton) findViewById(R.id.btnadscuxiao);
		btnadsbuysale = (ImageButton) findViewById(R.id.btnadsbuysale);
		btnadsquhuo = (ImageButton) findViewById(R.id.btnadsquhuo);
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
    
    //num�������,type=1�������֣�type=0��������
    private void chuhuo(String num,int type)
    {    	
		if(type==1)
		{
			if(count<3)
	    	{
	    		count++;
	    		huo=huo+num;
	    		txtadsTip.setText(huo);
	    	}
		}
		else if(type==0)
		{
			if(count>0)
			{
				count--;
				huo=huo.substring(0,huo.length()-1);
				if(count==0)
					txtadsTip.setText("");
				else
					txtadsTip.setText(huo);
			}
		}  
		if(count==3)
		{
			cabID=huo.substring(0,1);
		    huoID=huo.substring(1,huo.length());
		    vmc_columnDAO columnDAO = new vmc_columnDAO(Business.this);// ����InaccountDAO����		    
		    Tb_vmc_product tb_inaccount = columnDAO.getColumnproduct(cabID,huoID);
		    if(tb_inaccount!=null)
		    {
			    productID=tb_inaccount.getProductID().toString();
			    prosales=String.valueOf(tb_inaccount.getSalesPrice());
			    proImage=tb_inaccount.getAttBatch1();
			    proID=productID+"-"+tb_inaccount.getProductName().toString();
			    ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷproID="+proID+" productID="
						+productID+" proType="
						+"2"+" cabID="+cabID+" huoID="+huoID+" prosales="+prosales+" count="
						+"1");
			    count=0;
			    huo="";
			    txtadsTip.setText("");
				Intent intent = null;// ����Intent����                
	        	intent = new Intent(Business.this, BusgoodsSelect.class);// ʹ��Accountflag���ڳ�ʼ��Intent
	        	intent.putExtra("proID", proID);
	        	intent.putExtra("productID", productID);
	        	intent.putExtra("proImage", proImage);
	        	intent.putExtra("prosales", prosales);
	        	intent.putExtra("procount", "1");
	        	intent.putExtra("proType", "2");//1����ͨ����ƷID����,2����ͨ����������
	        	intent.putExtra("cabID", cabID);//�������,proType=1ʱ��Ч
	        	intent.putExtra("huoID", huoID);//����������,proType=1ʱ��Ч


//	        	OrderDetail.setProID(proID);
//            	OrderDetail.setProductID(productID);
//            	OrderDetail.setProType("2");
//            	OrderDetail.setCabID(cabID);
//            	OrderDetail.setColumnID(huoID);
//            	OrderDetail.setShouldPay(Float.parseFloat(prosales));
//            	OrderDetail.setShouldNo(1);
	        	startActivity(intent);// ��Accountflag
		    }
		    else
		    {
		    	ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷproID="+proID+" productID="
						+productID+" proType="
						+"2"+" cabID="+cabID+" huoID="+huoID+" prosales="+prosales+" count="
						+"1");
			    count=0;
			    huo="";
			    txtadsTip.setText("");
			}
		    
		}
    }
}
