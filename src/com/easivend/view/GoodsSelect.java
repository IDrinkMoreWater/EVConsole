/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           GoodsSelect.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        ѡ����Ʒҳ��          
**------------------------------------------------------------------------------------------------------
** Created by:          guozhenzhen 
** Created date:        2015-01-10
** Version:             V1.0 
** Descriptions:        The original version       
********************************************************************************************************/

package com.easivend.view;

import com.easivend.app.maintain.HuodaoSet;
import com.easivend.common.ProPictureAdapter;
import com.easivend.common.ToolClass;
import com.easivend.common.Vmc_ProductAdapter;
import com.example.evconsole.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class GoodsSelect extends Activity 
{
	private GridView gvselectProduct=null;
	private Button btnselectexit=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goodsselect);// ���ò����ļ�	
		//���ú������������Ĳ��ֲ���
		this.setRequestedOrientation(ToolClass.getOrientation());
		this.gvselectProduct=(GridView) findViewById(R.id.gvselectProduct); 		
		
		// ��Ʒ���е�������Ʒ��Ϣ���䵽��Ʒ���ݽṹ������
		final Vmc_ProductAdapter productAdapter=new Vmc_ProductAdapter();
    	productAdapter.showProInfo(GoodsSelect.this,"","shoudong",""); 
    	ProPictureAdapter adapter = new ProPictureAdapter(productAdapter.getProID(),productAdapter.getPromarket(),productAdapter.getProsales(),productAdapter.getProImage(),productAdapter.getProcount(), GoodsSelect.this);// ����pictureAdapter����
    	gvselectProduct.setAdapter(adapter);// ΪGridView��������Դ
    	//�޸Ļ���ӻ�����Ӧ��Ʒ
    	gvselectProduct.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub cabinetID[0],
				String strInfo[]=productAdapter.getProductID();
				String productID = strInfo[arg2];// ��¼������Ϣ               
				ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷproductID="+productID,"log.txt");
				//�˳�ʱ������intent
	            Intent intent=new Intent();
	            intent.putExtra("productID", productID);
	            setResult(HuodaoSet.RESULT_OK,intent);
	            finish();
			}// ΪGridView��������¼�
    		
    	});
		//���·���
		this.btnselectexit=(Button) findViewById(R.id.btnselectexit); 
		btnselectexit.setOnClickListener(new OnClickListener() {// Ϊ�˳���ť���ü����¼�
		    @Override
		    public void onClick(View arg0) {
		    	finish();
		    }
		});
	}
	
}
