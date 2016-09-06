package com.easivend.app.maintain;

import com.easivend.common.ToolClass;
import com.example.evconsole.R;
import com.landfone.common.utils.IUserCallback;
import com.landfoneapi.mispos.Display;
import com.landfoneapi.mispos.DisplayType;
import com.landfoneapi.mispos.ErrCode;
import com.landfoneapi.mispos.ILfListener;
import com.landfoneapi.mispos.LfMISPOSApi;
import com.landfoneapi.protocol.pkg.REPLY;
import com.landfoneapi.protocol.pkg._04_GetRecordReply;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CahslessTest extends Activity {
	private final static int OPENSUCCESS=1;//�򿪳ɹ�
	private final static int OPENFAIL=2;//��ʧ��
	private final static int CLOSESUCCESS=3;//�رճɹ�
	private final static int CLOSEFAIL=4;//�ر�ʧ��
	private final static int COSTSUCCESS=5;//�ۿ�ɹ�
	private final static int COSTFAIL=6;//�ۿ�ʧ��
	private final static int QUERYSUCCESS=7;//��ѯ�ɹ�
	private final static int QUERYFAIL=8;//��ѯʧ��
	private final static int DELETESUCCESS=9;//�����ɹ�
	private final static int DELETEFAIL=10;//����ʧ��
	private final static int PAYOUTSUCCESS=11;//�˿�ɹ�
	private final static int PAYOUTFAIL=12;//�˿�ʧ��
	private TextView txtcashlesstest=null;
	private EditText edtcashlesstest=null;
	private Button btncashlesstestopen=null,btncashlesstestok=null,btncashlesstestquery=null
			,btncashlesstestdelete=null,btncashlesstestpayout=null,btncashlesstestclose=null,
			btncashlesstestcancel=null;
	private Handler mainhand=null;
	private LfMISPOSApi mMyApi = new LfMISPOSApi();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cashlesstest);	
		//���ú������������Ĳ��ֲ���
		this.setRequestedOrientation(ToolClass.getOrientation());
		mainhand=new Handler()
		{

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub				
				switch (msg.what) 
				{
					case OPENSUCCESS:
						txtcashlesstest.setText(msg.obj.toString());
						break;
					case OPENFAIL:	
						txtcashlesstest.setText(msg.obj.toString());
						break;
					case CLOSESUCCESS:
						txtcashlesstest.setText(msg.obj.toString());
						break;
					case CLOSEFAIL:	
						txtcashlesstest.setText(msg.obj.toString());
						break;
					case COSTSUCCESS:
						txtcashlesstest.setText(msg.obj.toString());
						break;
					case COSTFAIL:	
						txtcashlesstest.setText(msg.obj.toString());
						break;
					case QUERYSUCCESS:
						txtcashlesstest.setText(msg.obj.toString());
						break;
					case QUERYFAIL:	
						txtcashlesstest.setText(msg.obj.toString());
						break;
					case DELETESUCCESS:
						txtcashlesstest.setText(msg.obj.toString());
						break;
					case DELETEFAIL:	
						txtcashlesstest.setText(msg.obj.toString());
						break;	
					case PAYOUTSUCCESS:
						txtcashlesstest.setText(msg.obj.toString());
						break;
					case PAYOUTFAIL:	
						txtcashlesstest.setText(msg.obj.toString());
						break;		
				}
			}
		};				
		txtcashlesstest = (TextView)findViewById(R.id.txtcashlesstest);
		edtcashlesstest = (EditText)findViewById(R.id.edtcashlesstest);
		//��
		btncashlesstestopen = (Button)findViewById(R.id.btncashlesstestopen);
		btncashlesstestopen.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {		    	
		    	ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity �򿪶�����","com.txt");
		    	txtcashlesstest.setText("�򿪶�����..");
		    	//ip���˿ڡ����ڡ������ʱ���׼ȷ
				mMyApi.pos_init("121.40.30.62", 18080
						,ToolClass.getExtracom(), "9600", mIUserCallback);				
		    }
		});
		//�ۿ�
		btncashlesstestok = (Button)findViewById(R.id.btncashlesstestok);
		btncashlesstestok.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {	
		    	int money=ToolClass.MoneySend(Float.parseFloat(edtcashlesstest.getText().toString()));
		    	ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity �������ۿ�="+money,"com.txt");
		    	txtcashlesstest.setText("�������ۿ�="+money);
				mMyApi.pos_purchase(money, mIUserCallback);				
		    }
		});
		//��ѯ
		btncashlesstestquery = (Button)findViewById(R.id.btncashlesstestquery);		
		btncashlesstestquery.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {	
		    	ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity ��������ȡ������Ϣ","com.txt");
		    	txtcashlesstest.setText("��������ȡ������Ϣ");
		    	mMyApi.pos_getrecord("000000000000000", "00000000","000000", mIUserCallback);
		    }
		});
		//��������
		btncashlesstestdelete = (Button)findViewById(R.id.btncashlesstestdelete);		
		btncashlesstestdelete.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {		
		    	ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity ����������ˢ��ǰ��..","com.txt");
		    	txtcashlesstest.setText("POS ����������ˢ��ǰ��..");
		    	mMyApi.pos_cancel();
		    }
		});
		//�˿�
		btncashlesstestpayout = (Button)findViewById(R.id.btncashlesstestpayout);
		btncashlesstestpayout.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {	
		    }
		});
		//�ر�
		btncashlesstestclose = (Button)findViewById(R.id.btncashlesstestclose);
		btncashlesstestclose.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {		    
		    	ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity �رն�����","com.txt");
		    	txtcashlesstest.setText("POS �رն�����");
				mMyApi.pos_release();
		    }
		});
		//�˳�
		btncashlesstestcancel = (Button)findViewById(R.id.btncashlesstestcancel);		
		btncashlesstestcancel.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {		
		    	mMyApi.pos_release();
		    	finish();
		    }
		});
	}
	
	//�ӿڷ���
	private IUserCallback mIUserCallback = new IUserCallback(){
		@Override
		public void onResult(REPLY rst) 
		{
			if(rst!=null) 
			{
				Message childmsg=mainhand.obtainMessage();
				//info(rst.op + ":" + rst.code + "," + rst.code_info);
				//��������ʶ����LfMISPOSApi�¡�OP_����ͷ�ľ�̬�������磺LfMISPOSApi.OP_INIT��LfMISPOSApi.OP_PURCHASE�ȵ�
				//�򿪴���
				if(rst.op.equals(LfMISPOSApi.OP_INIT))
				{
					//�����������Ϣ��code��code_info�ķ���/˵������com.landfoneapi.mispos.ErrCode
					if(rst.code.equals(ErrCode._00.getCode())){//����00������ɹ�
						ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity �򿪳ɹ�","com.txt");
						childmsg.what=OPENSUCCESS;
						childmsg.obj="�򿪳ɹ�";
					}else{
						ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity ��ʧ��,code:"+rst.code+",info:"+rst.code_info,"com.txt");						
						childmsg.what=OPENFAIL;
						childmsg.obj="��ʧ��,code:"+rst.code+",info:"+rst.code_info;
					}
				}
				//�رմ���
				else if(rst.op.equals(LfMISPOSApi.OP_RELEASE))
				{
					if(rst.code.equals(ErrCode._00.getCode())){//����00������ɹ�
						ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity �رճɹ�","com.txt");
						childmsg.what=CLOSESUCCESS;
						childmsg.obj="�رճɹ�";
					}else{
						ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity �ر�ʧ��,code:"+rst.code+",info:"+rst.code_info,"com.txt");						
						childmsg.what=CLOSEFAIL;
						childmsg.obj="�ر�ʧ��,code:"+rst.code+",info:"+rst.code_info;
					}
				}
				//�ۿ�
				else if(rst.op.equals(LfMISPOSApi.OP_PURCHASE))
				{
					if(rst.code.equals(ErrCode._00.getCode())){//����00������ɹ�
						ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity �ۿ�ɹ�","com.txt");
						childmsg.what=COSTSUCCESS;
						childmsg.obj="�ۿ�ɹ�";
					}else{
						ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity �ۿ�ʧ��,code:"+rst.code+",info:"+rst.code_info,"com.txt");
						childmsg.what=COSTFAIL;
						childmsg.obj="�ۿ�ʧ��,code:"+rst.code+",info:"+rst.code_info;
					}
				}
				//���ؽ��
				else if(rst.op.equals(LfMISPOSApi.OP_GETRECORD))
				{
					//����00������ɹ�
					if(rst.code.equals(ErrCode._00.getCode()))
					{
						String tmp = "����:�ض���Ϣ=";
						tmp += "[" + ((_04_GetRecordReply) (rst)).getSpecInfoField();//�ض���Ϣ����Ա����Ҫ������
						/*�ض���Ϣ˵��
						+��ֵ����(19)
						+�ն���ˮ��(6)
						+�ն˱��(8)
						+���κ�(6)
						+�̻���(15)
						+�̻�����(60)
						+��Ա����(60)
						+����ʱ��(6)
						+��������(8)
						+���׵���(14)
						+���ѽ��(12)
						+�˻����(12)
						+��ʱ������ˮ�ţ�26��
						���϶��Ƕ��������Ƕ���12λ��ǰ��0����������λ���󲹿ո�

						* */
						tmp += "],�̻�����=[" + ((_04_GetRecordReply) (rst)).getMer();//�̻�����
						tmp += "],�ն˺�=[" + ((_04_GetRecordReply) (rst)).getTmn();//�ն˺�
						tmp += "],����=[" + ((_04_GetRecordReply) (rst)).getCardNo();//����
						tmp += "],�������κ�=[" + ((_04_GetRecordReply) (rst)).getTransacionBatchNo();//�������κ�
						tmp += "],ԭ��������=[" + ((_04_GetRecordReply) (rst)).getTransacionVoucherNo();//ԭ��������
						tmp += "],�������ں�ʱ��=[" + ((_04_GetRecordReply) (rst)).getTransacionDatetime();//�������ں�ʱ��
						tmp += "],���׽��=[" + ((_04_GetRecordReply) (rst)).getTransacionAmount();//���׽��
						tmp +="]";
						ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity ��ѯ�ɹ�="+tmp,"com.txt");
						childmsg.what=QUERYSUCCESS;
						childmsg.obj="��ѯ�ɹ�="+tmp;
					}
					else
					{
						ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity ��ѯʧ��,code:"+rst.code+",info:"+rst.code_info,"com.txt");
						childmsg.what=QUERYFAIL;
						childmsg.obj="��ѯʧ��";
					}
				}
				mainhand.sendMessage(childmsg);
			}
		}

		@Override
		public void onProcess(Display dpl) {//���̺���ʾ��Ϣ
			if(dpl!=null) {
				//lcd(dpl.getType() + "\n" + dpl.getMsg());

				//����ʾ��Ϣ���͡�type��˵������com.landfoneapi.mispos.DisplayType
				if(dpl.getType().equals(DisplayType._4.getType())){
					ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity ͨѶ��ʾ<<"+dpl.getMsg(),"com.txt");
				}

			}
		}
	};

}
