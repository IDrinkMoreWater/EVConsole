package com.easivend.fragment;


import com.easivend.app.business.BusPort;
import com.easivend.app.business.BusPort.BusPortFragInteraction;
import com.easivend.common.OrderDetail;
import com.easivend.common.ToolClass;
import com.easivend.fragment.BuszhierFragment.BuszhierFragInteraction;
import com.easivend.http.Weixinghttp;
import com.example.evconsole.R;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class BuszhiweiFragment extends Fragment
{
	TextView txtbuszhiweicount=null,txtbuszhiweirount=null,txtbuszhiweirst=null,txtbuszhiweitime=null;
	ImageButton imgbtnbuszhiweiqxzf=null;
	ImageView ivbuszhiwei=null;
	private int recLen = 180; 
	private int queryLen = 0; 
    private TextView txtView; 
    ImageView imgbtnbusgoodsback=null;
//  Timer timer = new Timer(); 
//	private String proID = null;
//	private String productID = null;
//	private String proType = null;
//	private String cabID = null;
//	private String huoID = null;
//    private String prosales = null;
//    private String count = null;
//    private String reamin_amount = null;
    private String zhifutype = "4";//0�ֽ�1������2֧����������3֧������ά�룬4΢��ɨ��
    private float amount=0;
    //�߳̽���΢�Ŷ�ά�����
//    private Thread thread=null;
//    private Handler mainhand=null,childhand=null;   
    private String out_trade_no=null;
    Weixinghttp weixinghttp=null;
    private int iszhiwei=0;//1�ɹ������˶�ά��,0û�гɹ����ɶ�ά��
    private Context context;
    //=========================
    //fragment��activity�ص����
    //=========================
    /**
     * �������ⲿactivity������
     */
    private BuszhiweiFragInteraction listterner;
    /**
     * �����ġ���ContentFragment�����ص�activity��ʱ������ע��ص���Ϣ
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if(activity instanceof BuszhierFragInteraction)
        {
            listterner = (BuszhiweiFragInteraction)activity;
        }
        else{
            throw new IllegalArgumentException("activity must implements BuszhiweiFragInteraction");
        }

    }
    /**
     * ����һ������������activity����ʵ�ֵĽӿ�
     */
    public interface BuszhiweiFragInteraction
    {
        /**
         * Fragment ��Activity����ָ�����������Ը�������������
         * @param str
         */
        //void BuszhiweiGoodselect();//�л���Busgoodsselectҳ��
        void BuszhiweiFinish();      //�л���businessҳ��
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
		View view = inflater.inflate(R.layout.fragment_buszhiwei, container, false);  
		context=this.getActivity();//��ȡactivity��context
		amount=OrderDetail.getShouldPay()*OrderDetail.getShouldNo();
		txtbuszhiweicount= (TextView) view.findViewById(R.id.txtbuszhiweicount);
		txtbuszhiweicount.setText(String.valueOf(OrderDetail.getShouldNo()));
		txtbuszhiweirount= (TextView) view.findViewById(R.id.txtbuszhiweirount);
		txtbuszhiweirount.setText(String.valueOf(amount));
		txtbuszhiweirst= (TextView) view.findViewById(R.id.txtbuszhiweirst);
		txtbuszhiweitime= (TextView) view.findViewById(R.id.txtbuszhiweitime);
		ivbuszhiwei= (ImageView) view.findViewById(R.id.ivbuszhiwei);
		imgbtnbuszhiweiqxzf = (ImageButton) view.findViewById(R.id.imgbtnbuszhiweiqxzf);
		imgbtnbuszhiweiqxzf.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {		    	 
		    	listterner.BuszhiweiFinish();//�������fragment��activity���ͻص���Ϣ		    	
		    }
		});
		this.imgbtnbusgoodsback=(ImageView)view.findViewById(R.id.imgbtnbusgoodsback);
		imgbtnbusgoodsback.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {		    	
		    	listterner.BuszhiweiFinish();//�������fragment��activity���ͻص���Ϣ
		    }
		});
		
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
			txtbuszhiweirst.setText(str);
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
			//txtbuszhiweirst.setText("���׽��:"+str);
			txtbuszhiweirst.setText("���׽��:��ɨ���ά��");
			ivbuszhiwei.setImageBitmap(ToolClass.createQRImage(str));
		}

		@Override
		public void BusportSendWei(String str) {
			// TODO Auto-generated method stub
			
		}
		
	}
    
}
