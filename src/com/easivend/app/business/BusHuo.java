package com.easivend.app.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.easivend.common.ToolClass;
import com.easivend.dao.vmc_columnDAO;
import com.easivend.evprotocol.EVprotocolAPI;
import com.easivend.evprotocol.JNIInterface;
import com.example.evconsole.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class BusHuo extends Activity 
{
	private final int SPLASH_DISPLAY_LENGHT = 10000; // �ӳ�10��
	private String proID = null;
	private String productID = null;
	private String proImage = null;
    private float prosales = 0;
    private int count = 0;
    private float reamin_amount = 0;
    private String data[][]=null;
    private ListView lvbushuo = null;
    //������ʾ�����ݰ�װ����Ϊ�������У���˾�����
    private List<Map<String,String>> listMap = new ArrayList<Map<String,String>>();
    private SimpleAdapter simpleada = null;//�������ݵ�ת������
    private ImageView ivbushuoquhuo=null;
    private int tempx=0;
    private String draw=null,info=null;
    private int cabinetvar=0,huodaoNo=0,cabinetTypevar=0;
    private vmc_columnDAO columnDAO =null;
    private int huorst=0;
    //�������
    private int device=0;//�������		
	private int status=0;//�������
	private int hdid=0;//����id
	private int hdtype=0;//��������
	private float cost=0;//��Ǯ
	private float totalvalue=0;//ʣ����
	private int huodao=0;//ʣ��������
	
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bushuo);
		if(BusgoodsSelect.BusgoodsSelectAct!=null)
			BusgoodsSelect.BusgoodsSelectAct.finish(); 
		//ע�����������
  	    EVprotocolAPI.setCallBack(new JNIInterface() {
			
			@Override
			public void jniCallback(Map<String, Integer> allSet) {
				// TODO Auto-generated method stub
				ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<bushuo�������");
				Map<String, Integer> Set= allSet;
				int jnirst=Set.get("EV_TYPE");
				switch (jnirst)
				{
					case EVprotocolAPI.EV_TRADE_RPT://�������߳���Ϣ
						device=allSet.get("device");//�������
						status=allSet.get("status");//�������
						hdid=allSet.get("hdid");//����id
						hdtype=allSet.get("type");//��������
						cost=ToolClass.MoneyRec(allSet.get("cost"));//��Ǯ
						totalvalue=ToolClass.MoneyRec(allSet.get("totalvalue"));//ʣ����
						huodao=allSet.get("huodao");//ʣ��������
						ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<�������"+"device=["+device+"],status=["+status+"],hdid=["+hdid+"],type=["+hdtype+"],cost=["
								+cost+"],totalvalue=["+totalvalue+"],huodao=["+huodao+"]");	
						if(status==0)
						{
							data[tempx][0]=String.valueOf(R.drawable.yes);
							data[tempx][1]=proID+"["+prosales+"]"+"->������ɣ��뵽"+cabinetvar+"��"+huodaoNo+"����ȡ��Ʒ";
						}
						else
						{
							data[tempx][0]=String.valueOf(R.drawable.no);
							data[tempx][1]=proID+"["+prosales+"]"+"->"+cabinetvar+"��"+huodaoNo+"��������ʧ�ܣ�δ��Ǯ";
						}
						updateListview();
						tempx++;
						huorst=0;
						while((huorst!=1)&&(tempx<count))
				 	    {
				 	    	huorst=chuhuoopt(tempx);
				 	    	if(huorst==2)
							{
								data[tempx][0]=String.valueOf(R.drawable.yes);
								data[tempx][1]=proID+"["+prosales+"]"+"->������ɣ��뵽"+cabinetvar+"��"+huodaoNo+"����ȡ��Ʒ";
								updateListview();
								tempx++;
							}
							else if(huorst==0)
							{
								data[tempx][0]=String.valueOf(R.drawable.no);
								data[tempx][1]=proID+"["+prosales+"]"+"->"+cabinetvar+"��"+huodaoNo+"��������ʧ�ܣ�δ��Ǯ";
								updateListview();
								tempx++;
							}
				 	    }
						if(tempx>=count)
				 	    {
							ivbushuoquhuo.setVisibility(View.VISIBLE);
				 	    	new Handler().postDelayed(new Runnable() 
							{
		                        @Override
		                        public void run() 
		                        {
		                               finish();
		                        }

							}, SPLASH_DISPLAY_LENGHT);
				 	    }
						break;					
				}
			}
		}); 
  	    
		//����Ʒҳ����ȡ����ѡ�е���Ʒ
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		proID=bundle.getString("proID");
		productID=bundle.getString("productID");
		proImage=bundle.getString("proImage");
		prosales=Float.parseFloat(bundle.getString("prosales"));
		count=Integer.parseInt(bundle.getString("count"));
		reamin_amount=Float.parseFloat(bundle.getString("reamin_amount"));
		        
		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷproID="+proID+" productID="
				+productID+" proImage="
				+proImage+" prosales="+prosales+" count="
				+count+" reamin_amount="+reamin_amount);
		this.data=new String[count][2];
		draw=String.valueOf(R.drawable.shuaxin);
		info=proID+"["+prosales+"]"+"->�ȴ�����";
		for(int x=0;x<count;x++)
		{
			data[x][0]=draw;
			data[x][1]=info;
		}
		this.lvbushuo =(ListView) super.findViewById(R.id.lvbushuo);		
		updateListview();
		this.ivbushuoquhuo =(ImageView) super.findViewById(R.id.ivbushuoquhuo);
		ivbushuoquhuo.setVisibility(View.GONE);
		
		//*******************
		//��˳�����
		//*******************
		// ����InaccountDAO�������ڴ����ݿ�����ȡ���ݵ�Tb_vmc_column����
 	    columnDAO = new vmc_columnDAO(this);
 	    while((huorst!=1)&&(tempx<count))
 	    {
 	    	huorst=chuhuoopt(tempx);
 	    	if(huorst==2)
			{
				data[tempx][0]=String.valueOf(R.drawable.yes);
				data[tempx][1]=proID+"["+prosales+"]"+"->������ɣ��뵽"+cabinetvar+"��"+huodaoNo+"����ȡ��Ʒ";
				updateListview();
				tempx++;				
			}
			else if(huorst==0)
			{
				data[tempx][0]=String.valueOf(R.drawable.no);
				data[tempx][1]=proID+"["+prosales+"]"+"->"+cabinetvar+"��"+huodaoNo+"��������ʧ�ܣ�δ��Ǯ";
				updateListview();
				tempx++;
			}
 	    }
 	    if(tempx>=count)
 	    {
 	    	ivbushuoquhuo.setVisibility(View.VISIBLE);
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
	
	//���س�����Ϣ���б���
	private void updateListview()
	{
		int x=0;
		this.listMap.clear();
		for(x=0;x<count;x++)
		{
		  	Map<String,String> map = new HashMap<String,String>();//����Map���ϣ�����ÿһ������
		   	map.put("ivbushuostatus", data[x][0]);//��Ʒ����
	    	map.put("txtbushuoname", data[x][1]);//��Ʒ״̬
	    	this.listMap.add(map);//����������
		}
		//��������ܼ��ص�data_list��
		this.simpleada = new SimpleAdapter(this,this.listMap,R.layout.bushuolist,
		    		new String[]{"ivbushuostatus","txtbushuoname"},//Map�е�key����
		    		new int[]{R.id.ivbushuostatus,R.id.txtbushuoname});
		this.lvbushuo.setAdapter(this.simpleada);
	}
	
	//����,����ֵ0ʧ��,1����ָ��ɹ����ȴ����ؽ��,2�������
	private int chuhuoopt(int huox)
	{
		int huorst=0;
		int rst=0;
		data[huox][1]=proID+"["+prosales+"]"+"->���ڳ���,���Ժ�...";
		updateListview();
 	    // ��ȡ����������Ϣ�����洢��Map������
		List<String> alllist = columnDAO.getproductColumn(productID);
		cabinetvar=Integer.parseInt(alllist.get(0));
		huodaoNo=Integer.parseInt(alllist.get(1));
		cabinetTypevar=Integer.parseInt(alllist.get(2));
		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷcabID="+cabinetvar+"huoID="+huodaoNo+"cabType="+cabinetTypevar); 
        //��������
		int typevar=0;
		if(prosales>0)
			typevar=0;
		else 
			typevar=2;
		
		//���ӹ�
		if(cabinetTypevar==5)
		{
			rst=EVprotocolAPI.bentoOpen(cabinetvar,huodaoNo);
			if(rst==0)//��������ʧ��
			{
				huorst=0;
			}
			else if(rst==1)//�������ͳɹ�
			{
				huorst=2;
			}
			
		}
		//��ͨ��
		else 
		{
			rst=EVprotocolAPI.trade(cabinetvar,huodaoNo,typevar,
		    			ToolClass.MoneySend(prosales));
			if(rst==0)//��������ʧ��
			{
				huorst=0;
			}
			else if(rst==1)//�������ͳɹ�
			{
				huorst=1;
			}
		}
		return huorst;
	}
}
