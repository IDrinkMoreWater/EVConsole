package com.easivend.app.business;

import com.easivend.common.ToolClass;
import com.easivend.dao.vmc_system_parameterDAO;
import com.easivend.model.Tb_vmc_system_parameter;
import com.example.evconsole.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

public class BusTishi extends Activity 
{
	private final int SPLASH_DISPLAY_LENGHT = 5*60*1000; // �ӳ�5����	
	ImageView imgbtnbusgoodsback=null;
	private WebView webtishiInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// ��title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // ȫ��
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.bustishi);
		
		//����Ʒ����ҳ����ȡ����ѡ�е���ʾ��Ϣ����
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		int infotype=bundle.getInt("infotype");
		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<tishi��ʾ����="+infotype,"log.txt");
		 //�õ���ʾ����
		 webtishiInfo = (WebView) findViewById(R.id.webtishiInfo); 
		 WebSettings settings = webtishiInfo.getSettings();
	     settings.setSupportZoom(true);
	     settings.setTextSize(WebSettings.TextSize.LARGEST);
	     webtishiInfo.getSettings().setSupportMultipleWindows(true);
	     webtishiInfo.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); //���ù�������ʽ
	     webtishiInfo.getSettings().setDefaultTextEncodingName("UTF -8");//����Ĭ��Ϊutf-8
	     String info="�����ڴ�!";
	     //������ʾ����ʾ��Ϣ
 		 vmc_system_parameterDAO parameterDAO = new vmc_system_parameterDAO(ToolClass.getContext());// ����InaccountDAO����
	     // ��ȡ����������Ϣ�����洢��List���ͼ�����
	   	 Tb_vmc_system_parameter tb_inaccount = parameterDAO.find();
	   	 if(tb_inaccount!=null)
	   	 {
	   		 //������ʾ
	   		 if(infotype==1)
	   		 {
	   			 if(ToolClass.isEmptynull(tb_inaccount.getDemo())==false)	 
	   			 {
	   				 info=tb_inaccount.getDemo();
	   			 }	   			 
	   		 }
	   		 //���Ϣ
	   		 else if(infotype==2)
	   		 {
	   			 if(ToolClass.isEmptynull(tb_inaccount.getEvent())==false)
	   			 {
	   				 info=tb_inaccount.getEvent();
	   			 }	   			 
	   		 }
	   	 }
	     
	     webtishiInfo.loadDataWithBaseURL(null,info, "text/html; charset=UTF-8","utf-8", null);//����д��������ȷ���Ľ���
		   
		imgbtnbusgoodsback=(ImageView)findViewById(R.id.imgbtnbusgoodsback);
	    imgbtnbusgoodsback.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	finish();
		    }
		});
	    //5������֮���˳�ҳ��
	    new Handler().postDelayed(new Runnable() 
		{
            @Override
            public void run() 
            {	
            	finish();  
            }

		}, SPLASH_DISPLAY_LENGHT);
	}
	
}
