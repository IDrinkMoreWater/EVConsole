package com.easivend.app.business;

import com.easivend.common.ToolClass;
import com.example.evconsole.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.GridView;
import android.widget.LinearLayout;

public class Busgoods extends Activity 
{
	GridView gvbusgoodsProduct=null;
	String proclassID=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.busgoods);
		this.gvbusgoodsProduct=(GridView) findViewById(R.id.gvbusgoodsProduct); 
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
		
    	LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) gvbusgoodsProduct.getLayoutParams(); // ȡ�ؼ�mGrid��ǰ�Ĳ��ֲ���
    	linearParams.height =  screenHeight-170;// ���ؼ��ĸ�ǿ�����75����
    	gvbusgoodsProduct.setLayoutParams(linearParams); // ʹ���úõĲ��ֲ���Ӧ�õ��ؼ�mGrid2
    	//����Ʒ����ҳ����ȡ����ѡ�е���Ʒ����
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		proclassID=bundle.getString("proclassID");
		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷproclassID="+proclassID);
		if(proclassID!=null)
		{
			
		}
	}
	
}
