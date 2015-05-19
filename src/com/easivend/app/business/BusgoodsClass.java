package com.easivend.app.business;

import com.easivend.app.maintain.GoodsManager;
import com.easivend.app.maintain.HuodaoTest;
import com.easivend.app.maintain.MaintainActivity;
import com.easivend.app.maintain.ParamManager;
import com.easivend.common.ClassPictureAdapter;
import com.easivend.common.HuoPictureAdapter;
import com.easivend.common.Vmc_ClassAdapter;
import com.example.evconsole.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.AdapterView.OnItemClickListener;

public class BusgoodsClass extends Activity
{
	public static BusgoodsClass BusgoodsClassAct=null;
	GridView gvbusgoodsclass=null;
	ImageButton imgbtnbusgoodsclassback=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.busgoodsclass);
		BusgoodsClassAct = this;
		gvbusgoodsclass=(GridView) findViewById(R.id.gvbusgoodsclass); 
		Vmc_ClassAdapter vmc_classAdapter=new Vmc_ClassAdapter();
	    String[] strInfos = vmc_classAdapter.showSpinInfo(BusgoodsClass.this);
	    ClassPictureAdapter adapter = new ClassPictureAdapter(vmc_classAdapter.getProclassName(),vmc_classAdapter.getProImage(),BusgoodsClass.this);// ����pictureAdapter����
	    final String proclassID[]=vmc_classAdapter.getProclassID();
	    gvbusgoodsclass.setAdapter(adapter);// ΪGridView��������Դ	
	    gvbusgoodsclass.setOnItemClickListener(new OnItemClickListener() {// ΪGridView��������¼�
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Intent intent = null;// ����Intent����
                switch (arg2) {
                case 0:
                	intent = new Intent(BusgoodsClass.this, Busgoods.class);// ʹ��Accountflag���ڳ�ʼ��Intent
                	intent.putExtra("proclassID", "");
                	startActivity(intent);// ��Accountflag
                    break;
                default:
                	intent = new Intent(BusgoodsClass.this, Busgoods.class);// ʹ��Accountflag���ڳ�ʼ��Intent
                	intent.putExtra("proclassID", proclassID[arg2]);
                	startActivity(intent);// ��Accountflag
                    break;                
                }
            }
        });
	    imgbtnbusgoodsclassback=(ImageButton)findViewById(R.id.imgbtnbusgoodsclassback);
	    imgbtnbusgoodsclassback.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	finish();
		    }
		});
	}
	
}
