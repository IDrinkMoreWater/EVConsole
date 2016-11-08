package com.easivend.fragment;

import com.easivend.app.business.BusPort;
import com.easivend.app.business.BusPort.BusPortFragInteraction;
import com.easivend.common.OrderDetail;
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

public class BuszhiposFragment extends Fragment 
{
	TextView txtbuszhiposcount=null,txtbuszhiposAmount=null,txtbuszhipostime=null,
			txtbuszhipostsxx=null;
	ImageButton imgbtnbuszhiposqxzf=null;
	ImageView imgbtnbusgoodsback=null;
	float amount=0;//��Ʒ��Ҫ֧�����	 
//	private String proID = null;
//	private String productID = null;
//	private String proType = null;
//	private String cabID = null;
//	private String huoID = null;
//    private String prosales = null;
//    private String count = null;
//    private String reamin_amount = null;    
//    private String id="";
    private String out_trade_no=null;    
	private Context context;
	//=========================
    //fragment��activity�ص����
    //=========================
    /**
     * �������ⲿactivity������
     */
    private BuszhiposFragInteraction listterner;
    /**
     * �����ġ���ContentFragment�����ص�activity��ʱ������ע��ص���Ϣ
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if(activity instanceof BuszhiposFragInteraction)
        {
            listterner = (BuszhiposFragInteraction)activity;
        }
        else{
            throw new IllegalArgumentException("activity must implements BuszhiposFragInteraction");
        }

    }
    /**
     * ����һ������������activity����ʵ�ֵĽӿ�
     */
    public interface BuszhiposFragInteraction
    {
        /**
         * Fragment ��Activity����ָ�����������Ը�������������
         * @param str
         */
        //void BusgoodsselectSwitch(int buslevel);//�л���BusZhixxҳ��
        void BuszhiposFinish();      //�л���businessҳ��
    }
    @Override
    public void onDetach() {
        super.onDetach();

        listterner = null;
    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_buszhipos, container, false);  
		context=this.getActivity();//��ȡactivity��context
		amount=OrderDetail.getShouldPay()*OrderDetail.getShouldNo();		
		txtbuszhiposcount= (TextView) view.findViewById(R.id.txtbuszhiposcount);
		txtbuszhiposcount.setText(String.valueOf(OrderDetail.getShouldNo()));
		txtbuszhiposAmount= (TextView) view.findViewById(R.id.txtbuszhiposAmount);
		txtbuszhiposAmount.setText(String.valueOf(amount));
		txtbuszhipostime = (TextView) view.findViewById(R.id.txtbuszhipostime);
		txtbuszhipostsxx = (TextView) view.findViewById(R.id.txtbuszhipostsxx);
		imgbtnbuszhiposqxzf = (ImageButton) view.findViewById(R.id.imgbtnbuszhiposqxzf);
		imgbtnbuszhiposqxzf.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {		    	
		    	listterner.BuszhiposFinish();//�������fragment��activity���ͻص���Ϣ
		    }
		});
		this.imgbtnbusgoodsback=(ImageView)view.findViewById(R.id.imgbtnbusgoodsback);
		imgbtnbusgoodsback.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {		    	
		    	listterner.BuszhiposFinish();//�������fragment��activity���ͻص���Ϣ
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
			txtbuszhipostsxx.setText(str);
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
			
		}

		@Override
		public void BusportSendWei(String str) {
			// TODO Auto-generated method stub
			
		}

		
	}

}
