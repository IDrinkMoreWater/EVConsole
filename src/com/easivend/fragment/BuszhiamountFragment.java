package com.easivend.fragment;

import java.util.Timer;

import com.easivend.app.business.BusZhiAmount;
import com.easivend.app.business.BusgoodsSelect;
import com.easivend.common.OrderDetail;
import com.easivend.common.ToolClass;
import com.easivend.fragment.BusgoodsselectFragment.BusgoodsselectFragInteraction;
import com.example.evconsole.R;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class BuszhiamountFragment extends Fragment 
{
	private final int SPLASH_DISPLAY_LENGHT = 1500; // �ӳ�1.5��
	//���ȶԻ���
	ProgressDialog dialog= null;
	private final static int REQUEST_CODE=1;//���������ʶ
	TextView txtbuszhiamountcount=null,txtbuszhiamountAmount=null,txtbuszhiamountbillAmount=null,txtbuszhiamounttime=null,
			txtbuszhiamounttsxx=null;
	ImageButton imgbtnbuszhiamountqxzf=null,imgbtnbuszhiamountqtzf=null;
	float amount=0;//��Ʒ��Ҫ֧�����
	float billmoney=0,coinmoney=0,money=0;//Ͷ�ҽ��
	float RealNote=0,RealCoin=0,RealAmount=0;//�˱ҽ��
	private int recLen = 180; 
	private int queryLen = 0; 
    private TextView txtView; 
    Timer timer = new Timer();
    private int iszhienable=0;//1���ʹ�ָ��,0��û���ʹ�ָ��
    private boolean isempcoin=false;//false��δ���͹�ֽ����ָ�true��Ϊȱ�ң��Ѿ����͹�ֽ����ָ��
    private int dispenser=0;//0��,1hopper,2mdb
    private boolean ischuhuo=false;//true�Ѿ��������ˣ������ϱ���־
//	private String proID = null;
//	private String productID = null;
//	private String proType = null;
//	private String cabID = null;
//	private String huoID = null;
//    private String prosales = null;
//    private String count = null;
//    private String reamin_amount = null;
    private String zhifutype = "0";//0�ֽ�1������2֧����������3֧������ά�룬4΢��ɨ��
//    private String id="";
    private String out_trade_no=null;
    private int iszhiamount=0;//1�ɹ�Ͷ��Ǯ,0û�гɹ�Ͷ��Ǯ
	private Context context;
	//=========================
    //fragment��activity�ص����
    //=========================
    /**
     * �������ⲿactivity������
     */
    private BuszhiamountFragInteraction listterner;
    /**
     * �����ġ���ContentFragment�����ص�activity��ʱ������ע��ص���Ϣ
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if(activity instanceof BuszhiamountFragInteraction)
        {
            listterner = (BuszhiamountFragInteraction)activity;
        }
        else{
            throw new IllegalArgumentException("activity must implements BuszhiamountFragInteraction");
        }

    }
    /**
     * ����һ������������activity����ʵ�ֵĽӿ�
     */
    public interface BuszhiamountFragInteraction
    {
        /**
         * Fragment ��Activity����ָ�����������Ը�������������
         * @param str
         */
        //void BusgoodsselectSwitch(int buslevel);//�л���BusZhixxҳ��
        void BuszhiamountFinish();      //�л���businessҳ��
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
		View view = inflater.inflate(R.layout.fragment_buszhiamount, container, false);  
		context=this.getActivity();//��ȡactivity��context
		out_trade_no=ToolClass.out_trade_no(context);
		amount=OrderDetail.getShouldPay()*OrderDetail.getShouldNo();
		OrderDetail.setOrdereID(out_trade_no);
    	OrderDetail.setPayType(Integer.parseInt(zhifutype));
		txtbuszhiamountcount= (TextView) view.findViewById(R.id.txtbuszhiamountcount);
		txtbuszhiamountcount.setText(String.valueOf(OrderDetail.getShouldNo()));
		txtbuszhiamountAmount= (TextView) view.findViewById(R.id.txtbuszhiamountAmount);
		txtbuszhiamountAmount.setText(String.valueOf(amount));
		txtbuszhiamountbillAmount= (TextView) view.findViewById(R.id.txtbuszhiamountbillAmount);		
		txtbuszhiamounttime = (TextView) view.findViewById(R.id.txtbuszhiamounttime);
		txtbuszhiamounttsxx = (TextView) view.findViewById(R.id.txtbuszhiamounttsxx);
		imgbtnbuszhiamountqxzf = (ImageButton) view.findViewById(R.id.imgbtnbuszhiamountqxzf);
		imgbtnbuszhiamountqxzf.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {		    	
		    	listterner.BuszhiamountFinish();//�������fragment��activity���ͻص���Ϣ
		    }
		});
		return view;
	}
}
