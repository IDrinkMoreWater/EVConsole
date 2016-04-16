package com.easivend.fragment;


import com.easivend.app.business.BusPort;
import com.easivend.app.business.BusPort.BusPortFragInteraction;
import com.easivend.common.OrderDetail;
import com.easivend.common.ToolClass;
import com.easivend.http.Zhifubaohttp;
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

public class BuszhierFragment extends Fragment 
{
	TextView txtbuszhiercount=null,txtbuszhiamerount=null,txtbuszhierrst=null,txtbuszhiertime=null;
	ImageButton imgbtnbuszhierqxzf=null;
	ImageView ivbuszhier=null;
	private int recLen = 180; 
	private int queryLen = 0; 
    private TextView txtView; 
//  Timer timer = new Timer(); 
//	private String proID = null;
//	private String productID = null;
//	private String proType = null;
//	private String cabID = null;
//	private String huoID = null;
//    private String prosales = null;
//    private String count = null;
//    private String reamin_amount = null;
    private String zhifutype = "3";//0�ֽ�1������2֧����������3֧������ά�룬4΢��ɨ��
    private float amount=0;
    //�߳̽���֧������ά�����
//    private Thread thread=null;
//    private Handler mainhand=null,childhand=null;   
    private String out_trade_no=null;
    Zhifubaohttp zhifubaohttp=null;
    private int iszhier=0;//1�ɹ������˶�ά��,0û�гɹ����ɶ�ά��
	private Context context;
	//=========================
    //fragment��activity�ص����
    //=========================
    /**
     * �������ⲿactivity������
     */
    private BuszhierFragInteraction listterner;
    /**
     * �����ġ���ContentFragment�����ص�activity��ʱ������ע��ص���Ϣ
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if(activity instanceof BuszhierFragInteraction)
        {
            listterner = (BuszhierFragInteraction)activity;
        }
        else{
            throw new IllegalArgumentException("activity must implements BuszhierFragInteraction");
        }

    }
    /**
     * ����һ������������activity����ʵ�ֵĽӿ�
     */
    public interface BuszhierFragInteraction
    {
        /**
         * Fragment ��Activity����ָ�����������Ը�������������
         * @param str
         */
        //void BusgoodsselectSwitch(int buslevel);//�л���BusZhixxҳ��
        void BuszhierFinish();      //�л���businessҳ��
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
		View view = inflater.inflate(R.layout.fragment_buszhier, container, false);  
		context=this.getActivity();//��ȡactivity��context
		amount=OrderDetail.getShouldPay()*OrderDetail.getShouldNo();
		txtbuszhiercount= (TextView) view.findViewById(R.id.txtbuszhiercount);
		txtbuszhiercount.setText(String.valueOf(OrderDetail.getShouldNo()));
		txtbuszhiamerount= (TextView) view.findViewById(R.id.txtbuszhiamerount);
		txtbuszhiamerount.setText(String.valueOf(amount));
		txtbuszhierrst= (TextView) view.findViewById(R.id.txtbuszhierrst);
		ivbuszhier= (ImageView) view.findViewById(R.id.ivbuszhier);
		imgbtnbuszhierqxzf = (ImageButton) view.findViewById(R.id.imgbtnbuszhierqxzf);
		imgbtnbuszhierqxzf.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {	
		    	listterner.BuszhierFinish();//�������fragment��activity���ͻص���Ϣ
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
			txtbuszhierrst.setText(str);
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
			txtbuszhierrst.setText("���׽��:"+str);
			ivbuszhier.setImageBitmap(ToolClass.createQRImage(str));
		}

		@Override
		public void BusportMovie() {
			// TODO Auto-generated method stub
			
		}
	}
}
