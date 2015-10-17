package com.easivend.fragment;

import java.util.HashMap;
import java.util.Map;

import com.easivend.app.business.Busgoods;
import com.easivend.app.business.BusgoodsClass;
import com.easivend.app.business.BusgoodsSelect;
import com.easivend.common.ProPictureAdapter;
import com.easivend.common.ToolClass;
import com.easivend.common.Vmc_ProductAdapter;
import com.easivend.fragment.BusgoodsclassFragment.BusgoodsclassFragInteraction;
import com.example.evconsole.R;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.AdapterView.OnItemClickListener;

public class BusgoodsFragment extends Fragment
{
	// ������Ʒ�б�
	Vmc_ProductAdapter productAdapter=null;
	Gallery gvbusgoodsProduct=null;
	String proclassID=null;
	ImageButton imgbtnbusgoodsback=null;
	private String[] proID = null;
	private String[] productID = null;
	private String[] proImage = null;
    private String[] prosales = null;
    private String[] procount = null;
    private Context context;
    //=========================
    //fragment��activity�ص����
    //=========================
    /**
     * �������ⲿactivity������
     */
    private BusgoodsFragInteraction listterner;
    /**
     * �����ġ���ContentFragment�����ص�activity��ʱ������ע��ص���Ϣ
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if(activity instanceof BusgoodsFragInteraction)
        {
            listterner = (BusgoodsFragInteraction)activity;
        }
        else{
            throw new IllegalArgumentException("activity must implements BusgoodsFragInteraction");
        }

    }
    /**
     * ����һ������������activity����ʵ�ֵĽӿ�
     */
    public interface BusgoodsFragInteraction
    {
        /**
         * Fragment ��Activity����ָ�����������Ը�������������
         * @param str
         */
        void BusgoodsSwitch(Map<String, String> str);//�л���BusgoodsSelectҳ��
        void BusgoodsFinish();      //�л���businessҳ��
    }
    @Override
    public void onDetach() {
        super.onDetach();

        listterner = null;
    }
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_busgoods, container, false);  
		//������������activity���������
		//����Ʒ����ҳ����ȡ����ѡ�е���Ʒ����
		Bundle data = getArguments();//��ô�activity�д��ݹ�����ֵ
		proclassID=data.getString("proclassID");
		context=this.getActivity();//��ȡactivity��context
		this.gvbusgoodsProduct=(Gallery) view.findViewById(R.id.gvbusgoodsProduct);
		this.imgbtnbusgoodsback=(ImageButton)view.findViewById(R.id.imgbtnbusgoodsback);
		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷproclassID="+proclassID,"log.txt");
		//���������Ʒ����id
		if((proclassID!=null)&&(proclassID.isEmpty()!=true))
		{
			//ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷproclassID��ѯ");
			// ��Ʒ���е�������Ʒ��Ϣ���䵽��Ʒ���ݽṹ������
	    	productAdapter=new Vmc_ProductAdapter();
	    	productAdapter.showProInfo(context,"","",proclassID);  
	    	ProPictureAdapter adapter = new ProPictureAdapter(productAdapter.getProductName(),productAdapter.getPromarket(),productAdapter.getProsales(),productAdapter.getProImage(),productAdapter.getProcount(), context);// ����pictureAdapter����
	    	gvbusgoodsProduct.setAdapter(adapter);// ΪGridView��������Դ
	    	gvbusgoodsProduct.setSelection(adapter.getCount()/2);//�м��ͼƬѡ��
	    	proID=productAdapter.getProID();
	    	productID=productAdapter.getProductID();
	    	proImage=productAdapter.getProImage();
	    	prosales=productAdapter.getProsales();
	    	procount=productAdapter.getProcount();
		}
		//�����������Ʒ����id
		else 
		{
			//ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��Ʒȫ����ѯ");
			// ��Ʒ���е�������Ʒ��Ϣ���䵽��Ʒ���ݽṹ������
	    	productAdapter=new Vmc_ProductAdapter();
	    	productAdapter.showProInfo(context,"","shoudong","");  
	    	ProPictureAdapter adapter = new ProPictureAdapter(productAdapter.getProductName(),productAdapter.getPromarket(),productAdapter.getProsales(),productAdapter.getProImage(),productAdapter.getProcount(), context);// ����pictureAdapter����
	    	gvbusgoodsProduct.setAdapter(adapter);// ΪGridView��������Դ
	    	gvbusgoodsProduct.setSelection(adapter.getCount()/2);//�м��ͼƬѡ��
	    	proID=productAdapter.getProID();
	    	productID=productAdapter.getProductID();
	    	proImage=productAdapter.getProImage();
	    	prosales=productAdapter.getProsales();
	    	procount=productAdapter.getProcount();
		}
		
		
		imgbtnbusgoodsback.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	listterner.BusgoodsFinish();//�������fragment��activity���ͻص���Ϣ
		    }
		});
		
		gvbusgoodsProduct.setOnItemClickListener(new OnItemClickListener() {// ΪGridView��������¼�
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if(Integer.parseInt(procount[arg2])>0)
                {
//	            	Intent intent = null;// ����Intent����                
//	            	intent = new Intent(Busgoods.this, BusgoodsSelect.class);// ʹ��Accountflag���ڳ�ʼ��Intent
//	            	intent.putExtra("proID", proID[arg2]);
//	            	intent.putExtra("productID", productID[arg2]);
//	            	intent.putExtra("proImage", proImage[arg2]);
//	            	intent.putExtra("prosales", prosales[arg2]);
//	            	intent.putExtra("procount", procount[arg2]);
//	            	intent.putExtra("proType", "1");//1����ͨ����ƷID����,2����ͨ����������
//	            	intent.putExtra("cabID", "");//�������,proType=1ʱ��Ч
//		        	intent.putExtra("huoID", "");//����������,proType=1ʱ��Ч
//	            	startActivity(intent);// ��Accountflag
                	//�������fragment��activity���ͻص���Ϣ
                	Map<String, String>str=new HashMap<String, String>();
                	str.put("proID", proID[arg2]);
                	str.put("productID", productID[arg2]);
	            	str.put("proImage", proImage[arg2]);
	            	str.put("prosales", prosales[arg2]);
	            	str.put("procount", procount[arg2]);
	            	str.put("proType", "1");//1����ͨ����ƷID����,2����ͨ����������
	            	str.put("cabID", "");//�������,proType=1ʱ��Ч
		        	str.put("huoID", "");//����������,proType=1ʱ��Ч
                	listterner.BusgoodsSwitch(str);//�������fragment��activity���ͻص���Ϣ
                }
            }
        });
		return view;
	}
}
