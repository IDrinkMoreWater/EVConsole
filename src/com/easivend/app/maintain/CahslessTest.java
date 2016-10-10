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
	public final static int OPENSUCCESS=1;//�򿪳ɹ�
	public final static int OPENFAIL=2;//��ʧ��
	public final static int CLOSESUCCESS=3;//�رճɹ�
	public final static int CLOSEFAIL=4;//�ر�ʧ��
	public final static int COSTSUCCESS=5;//�ۿ�ɹ�
	public final static int COSTFAIL=6;//�ۿ�ʧ��
	public final static int QUERYSUCCESS=7;//��ѯ�ɹ�
	public final static int QUERYFAIL=8;//��ѯʧ��
	public final static int DELETESUCCESS=9;//�����ɹ�
	public final static int DELETEFAIL=10;//����ʧ��
	public final static int PAYOUTSUCCESS=11;//�˿�ɹ�
	public final static int PAYOUTFAIL=12;//�˿�ʧ��
	private TextView txtcashlesstest=null;
	private EditText edtcashlesstest=null;
	private Button btncashlesstestopen=null,btncashlesstestok=null,btncashlesstestquery=null
			,btncashlesstestdelete=null,btncashlesstestpayout=null,btncashlesstestclose=null,
			btncashlesstestcancel=null;
	private Handler mainhand=null;
	private LfMISPOSApi mMyApi = new LfMISPOSApi();
	//�˿����
	private String rfd_card_no = "";
	private String rfd_spec_tmp_serial = "";
	float amount=0;//��Ʒ��Ҫ֧�����
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
		    	ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity �򿪶�����"+ToolClass.getCardcom(),"com.txt");
		    	txtcashlesstest.setText("�򿪶�����.."+ToolClass.getCardcom());
		    	//ip���˿ڡ����ڡ������ʱ���׼ȷ"121.40.30.62", 18080
				mMyApi.pos_init(ToolClass.getPosip(), Integer.parseInt(ToolClass.getPosipport())
						,ToolClass.getCardcom(), "9600", mIUserCallback);
		    }
		});
		//�ۿ�
		btncashlesstestok = (Button)findViewById(R.id.btncashlesstestok);
		btncashlesstestok.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {	
		    	amount=Float.parseFloat(edtcashlesstest.getText().toString());
		    	ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity �������ۿ�="+amount,"com.txt");
		    	txtcashlesstest.setText("�������ۿ�="+amount);
				mMyApi.pos_purchase(ToolClass.MoneySend(amount), mIUserCallback);				
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
		    	ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity �������˿�="+amount,"com.txt");
		    	txtcashlesstest.setText("�������˿�="+amount);
		    	mMyApi.pos_refund(rfd_card_no,ToolClass.MoneySend(amount),rfd_spec_tmp_serial, mIUserCallback);
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
					if(rst.code.equals(ErrCode._00.getCode())){//����00�������ɹ�
						ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity �򿪳ɹ�"+ToolClass.getCardcom(),"com.txt");
						childmsg.what=OPENSUCCESS;
						childmsg.obj="�򿪳ɹ�"+ToolClass.getCardcom();
					}else{
						ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity ��ʧ��"+ToolClass.getCardcom()+",code:"+rst.code+",info:"+rst.code_info,"com.txt");						
						childmsg.what=OPENFAIL;
						childmsg.obj="��ʧ��"+ToolClass.getCardcom()+",code:"+rst.code+",info:"+rst.code_info;
					}
				}
				//�رմ���
				else if(rst.op.equals(LfMISPOSApi.OP_RELEASE))
				{
					if(rst.code.equals(ErrCode._00.getCode())){//����00�������ɹ�
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
					if(rst.code.equals(ErrCode._00.getCode())){//����00�������ɹ�
						ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity �ۿ�ɹ�","com.txt");
						childmsg.what=COSTSUCCESS;
						childmsg.obj="�ۿ�ɹ�";
					}
					else if(rst.code.equals(ErrCode._XY.getCode())){
  						ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity �����ɹ�","com.txt");
  						childmsg.what=CahslessTest.DELETESUCCESS;
  						childmsg.obj="�����ɹ�";
  					}
					else{
						ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity �ۿ�ʧ��,code:"+rst.code+",info:"+rst.code_info,"com.txt");
						childmsg.what=COSTFAIL;
						childmsg.obj="�ۿ�ʧ��,code:"+rst.code+",info:"+rst.code_info;
					}
				}
				//�˿�
  				else if(rst.op.equals(LfMISPOSApi.OP_REFUND))
  				{
  					//����00�������ɹ�
					if(rst.code.equals(ErrCode._00.getCode()))
					{
						ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity �˿�ɹ�","com.txt");
						childmsg.what=CahslessTest.PAYOUTSUCCESS;
  						childmsg.obj="�˿�ɹ�";
					}else
					{
						ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity �˿�ʧ��,code:"+rst.code+",info:"+rst.code_info,"com.txt");
						childmsg.what=CahslessTest.PAYOUTFAIL;
						childmsg.obj="�˿�ʧ��,code:"+rst.code+",info:"+rst.code_info;
					}
				}
				//���ؽ��
				else if(rst.op.equals(LfMISPOSApi.OP_GETRECORD))
				{
					//����00�������ɹ�
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

  						//�˿������ȡ
						String tmp_spec = ((_04_GetRecordReply) (rst)).getSpecInfoField();
						int tmp_spec_len = tmp_spec!=null?tmp_spec.length():0;
						//����
						//rfd_amt_fen = amount;//ʹ���ϴ�ȫ����Խ���1��
						//���˿�š�
						if(tmp_spec!=null && tmp_spec_len>(2+19)){
							rfd_card_no = (((_04_GetRecordReply) (rst)).getSpecInfoField()).substring(0+2,2+19).trim();
						}
						//����ʱ������ˮ�š�
						if(tmp_spec!=null && tmp_spec_len>26){
							rfd_spec_tmp_serial = (((_04_GetRecordReply) (rst)).getSpecInfoField()).substring((tmp_spec_len-26),tmp_spec_len);
						}else{//ʹ�ÿո�ʱ����ʾ��һ�εġ���ʱ������ˮ�š�
							rfd_spec_tmp_serial = String.format("%1$-26s","");
						}
						ToolClass.Log(ToolClass.INFO,"EV_COM","COMActivity �˿����=���"+amount+"����="+rfd_card_no+"��ˮ��="+rfd_spec_tmp_serial,"com.txt");
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