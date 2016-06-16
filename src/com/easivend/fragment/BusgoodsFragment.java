package com.easivend.fragment;

import java.util.HashMap;
import java.util.Map;

import com.easivend.common.ProPictureAdapter;
import com.easivend.common.ToolClass;
import com.easivend.common.Vmc_ProductAdapter;
import com.example.evconsole.R;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class BusgoodsFragment extends Fragment
{
	// ������Ʒ�б�
	Vmc_ProductAdapter productAdapter=null;
	GridView gvbusgoodsProduct=null;
	String proclassID=null;
	ImageView imgbtnbusgoodsback=null,imgback=null,imgnext=null;
	TextView txtpage=null;
	private String[] proID = null,pageproID=null;
	private String[] productID = null,pageproductID = null;
	private String[] productName = null,pageproductName = null;
	private String[] proImage = null,pageproImage = null;
	private String[] promarket = null,pagepromarket = null;
    private String[] prosales = null,pageprosales = null;
    private String[] procount = null,pageprocount = null;
    private Context context;
    int count=0,page=0,pageindex=0;
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
		this.gvbusgoodsProduct=(GridView) view.findViewById(R.id.gvbusgoodsProduct);
		this.imgbtnbusgoodsback=(ImageView)view.findViewById(R.id.imgbtnbusgoodsback);
		this.imgback=(ImageView)view.findViewById(R.id.imgback);
		this.imgnext=(ImageView)view.findViewById(R.id.imgnext);
		this.txtpage=(TextView)view.findViewById(R.id.txtpage);
		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷproclassID="+proclassID,"log.txt");
		//���������Ʒ����id
		if((proclassID!=null)&&(proclassID.isEmpty()!=true))
		{
			//ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ƷproclassID��ѯ");
			// ��Ʒ���е�������Ʒ��Ϣ���䵽��Ʒ���ݽṹ������
	    	productAdapter=new Vmc_ProductAdapter();
	    	productAdapter.showProInfo(context,"","",proclassID); 
		}
		//�����������Ʒ����id
		else 
		{
			//ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��Ʒȫ����ѯ");
			// ��Ʒ���е�������Ʒ��Ϣ���䵽��Ʒ���ݽṹ������
	    	productAdapter=new Vmc_ProductAdapter();
	    	productAdapter.showProInfo(context,"","shoudong",""); 
		}
		proID=productAdapter.getProID();
    	productID=productAdapter.getProductID();
    	productName=productAdapter.getProductName();
    	proImage=productAdapter.getProImage();
    	promarket=productAdapter.getPromarket();
    	prosales=productAdapter.getProsales();
    	procount=productAdapter.getProcount();
    	count=proID.length;
        page=(count%6>0)?(count/6)+1:(count/6);
        pageindex=0;
        updategrid(pageindex);
    	
        imgback.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	if(pageindex>0)
		    	{
		    		pageindex--;
		    		updategrid(pageindex);
		    	}
		    }
		});
        imgnext.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	if(pageindex<(page-1))
		    	{
		    		pageindex++;
		    		updategrid(pageindex);
		    	}
		    }
		});
		imgbtnbusgoodsback.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {		    	
		    	listterner.BusgoodsFinish();//�������fragment��activity���ͻص���Ϣ
		    }
		});
		
		gvbusgoodsProduct.setOnItemClickListener(new OnItemClickListener() {// ΪGridView��������¼�
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if(Integer.parseInt(pageprocount[arg2])>0)
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
                	str.put("proID", pageproID[arg2]);
                	str.put("productID", pageproductID[arg2]);
	            	str.put("proImage", pageproImage[arg2]);
	            	str.put("prosales", pageprosales[arg2]);
	            	str.put("procount", pageprocount[arg2]);
	            	str.put("proType", "1");//1����ͨ����ƷID����,2����ͨ����������
	            	str.put("cabID", "");//�������,proType=1ʱ��Ч
		        	str.put("huoID", "");//����������,proType=1ʱ��Ч
                	listterner.BusgoodsSwitch(str);//�������fragment��activity���ͻص���Ϣ
                }
            }
        });
		return view;
	}
    
    private void updategrid(int pagein)
    {
    	int max=((pagein*6+6)>count)?count:(pagein*6+6);
    	int index=pagein*6;
    	ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<count="+count+",page="+page+",pageindex="+pagein+"index="+index+"max="+max,"log.txt");
    	StringBuilder info=new StringBuilder();
    	info.append(pagein+1).append("/").append(page);    	
    	txtpage.setText(info);
    	pageproID=new String[max-index];
    	pageproductID=new String[max-index];
    	pageproductName=new String[max-index];
    	pageproImage=new String[max-index];
    	pagepromarket=new String[max-index];
    	pageprosales=new String[max-index];
    	pageprocount=new String[max-index];
    	for(int i=0;index<max;i++,index++)
        {
    		pageproID[i]=proID[index];
    		pageproductID[i]=productID[index];
    		pageproductName[i]=productName[index];
    		pageproImage[i]=proImage[index];
    		pagepromarket[i]=promarket[index];
    		pageprosales[i]=prosales[index];
    		pageprocount[i]=procount[index];
        }
    	ProPictureAdapter adapter = new ProPictureAdapter(pageproductName,pagepromarket,pageprosales,pageproImage,pageprocount, context);// ����pictureAdapter����
    	gvbusgoodsProduct.setAdapter(adapter);// ΪGridView��������Դ      
    }
}
