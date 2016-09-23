package com.easivend.app.maintain;

import com.easivend.common.ToolClass;
import com.example.evconsole.R;
import com.example.printdemo.MyFunc;
import com.example.printdemo.SerialHelper;
import android.app.Activity;
import android.os.Bundle;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

import com.bean.ComBean;
import com.printsdk.cmd.PrintCmd;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

public class PrintTest extends Activity {
	public final static int NORMAL=1;//����
	public final static int NOPOWER=2;//δ���ӻ�δ�ϵ�
	public final static int NOMATCH=3;//�쳣[��ӡ���͵��ÿⲻƥ��]
	public final static int HEADOPEN=4;//��ӡ��ͷ��
	public final static int CUTTERERR=5;//�е�δ��λ
	public final static int HEADHEAT=6;//��ӡͷ����
	public final static int BLACKMARKERR=7;//�ڱ����
	public final static int PAPEREXH=8;//ֽ��
	public final static int PAPERWILLEXH=9;//ֽ����
	public final static int UNKNOWERR=10;//�����쳣
	TextView txtMsg=null;
	CheckBox chktitle1=null,chktitle2=null,chkno=null,chksum=null,
			chkthank=null,chker=null,chkdate=null;
	EditText edittitle1=null,edittitle2=null,editthank=null,editer=null;
	Button btnopen=null,btnquery=null,btnprint=null,btnclose=null,btnsave=null,btnexit=null;
	boolean istitle1,istitle2,isno,issum,isthank,iser,isdate;
	int serialno=0;
	String title1str,title2str,thankstr,erstr;
	SerialControl ComA;                  // ���ڿ���
	static DispQueueThread DispQueue;    // ˢ����ʾ�߳�
	private boolean ercheck=false;//true���ڴ�ӡ���Ĳ����У����Ժ�falseû�д�ӡ���Ĳ���
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); // ���ʻ���־ʱ���ʽ��
	float amount=0;
	private Handler printmainhand=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.printtest);
		printmainhand=new Handler()
		{

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub				
				switch (msg.what) 
				{
					case NORMAL:
						txtMsg.setText(msg.obj.toString());
						break;
					case NOPOWER:	
						txtMsg.setText(msg.obj.toString());
						break;
					case NOMATCH:
						txtMsg.setText(msg.obj.toString());
						break;
					case HEADOPEN:	
						txtMsg.setText(msg.obj.toString());
						break;
					case CUTTERERR:
						txtMsg.setText(msg.obj.toString());
						break;
					case HEADHEAT:	
						txtMsg.setText(msg.obj.toString());
						break;
					case BLACKMARKERR:
						txtMsg.setText(msg.obj.toString());
						break;
					case PAPEREXH:	
						txtMsg.setText(msg.obj.toString());
						break;
					case PAPERWILLEXH://���Ҳ���Ե�����״̬ʹ��
						txtMsg.setText(msg.obj.toString());
						break;	
					case UNKNOWERR:
						txtMsg.setText(msg.obj.toString());
						break;
				}
			}
		};
        ComA = SerialControl.getInstance();
        ComA.setbLoopData(PrintCmd.GetStatus());
        DispQueue = new DispQueueThread();
		DispQueue.start();
        
        edittitle1 = (EditText) findViewById(R.id.edittitle1);        
        edittitle2 = (EditText) findViewById(R.id.edittitle2);
        editthank = (EditText) findViewById(R.id.editthank);
        editer = (EditText) findViewById(R.id.editer);
        txtMsg = (TextView) findViewById(R.id.txtMsg);
        chktitle1 = (CheckBox) findViewById(R.id.chktitle1);
        chktitle1.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				edittitle1.setEnabled(isChecked);
				istitle1=isChecked;
				if(isChecked==false)
				{
					edittitle1.setText("");
				}
				else
				{
					edittitle1.setText(title1str);
				}	
			}
		});
        chktitle2 = (CheckBox) findViewById(R.id.chktitle2);
        chktitle2.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				edittitle2.setEnabled(isChecked);
				istitle2=isChecked;
				if(isChecked==false)
				{
					edittitle2.setText("");
				}
				else
				{
					edittitle2.setText(title2str);
				}
			}
		});
        chkno = (CheckBox) findViewById(R.id.chkno);
        chkno.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				isno=isChecked;
			}
		});
        chksum = (CheckBox) findViewById(R.id.chksum);
        chksum.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				issum=isChecked;
			}
		});
        chkthank = (CheckBox) findViewById(R.id.chkthank);
        chkthank.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				editthank.setEnabled(isChecked);
				isthank=isChecked;
				if(isChecked==false)
				{
					editthank.setText("");
				}
				else
				{
					editthank.setText(thankstr);
				}
			}
		});
        chker = (CheckBox) findViewById(R.id.chker);
        chker.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				editer.setEnabled(isChecked);
				iser=isChecked;
				if(isChecked==false)
				{
					editer.setText("");
				}
				else
				{
					editer.setText(erstr);
				}
			}
		});
        chkdate = (CheckBox) findViewById(R.id.chkdate);
        chkdate.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				isdate=isChecked;
			}
		});
        txtMsg = (TextView) findViewById(R.id.txtMsg);
        ReadSharedPreferencesPrinter();
        
        //��
        btnopen = (Button)findViewById(R.id.btnopen);		
        btnopen.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {		    	
		    	ComA.setPort(ToolClass.getPrintcom());            // 1.1 �趨����
				ComA.setBaudRate("9600");// 1.2 �趨������
				OpenComPort(ComA); // 1.3 �򿪴���
		    }
		});
        // ��ѯ״̬
        btnquery = (Button)findViewById(R.id.btnquery);		
        btnquery.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {		    	
		    	GetPrinterStates(ComA, PrintCmd.GetStatus()); 
		    }
		});
        //����
        btnsave = (Button)findViewById(R.id.btnsave);		
        btnsave.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {		    	
		    	SaveSharedPreferencesPrinter();
		    }
		});
        //��ӡ
        btnprint = (Button)findViewById(R.id.btnprint);		
        btnprint.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {		    	
		    	PrintBankQueue();                             // ��ӡСƱ
		    }
		});
        //�ر�
        btnclose = (Button)findViewById(R.id.btnclose);		
        btnclose.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {		    	
		    	CloseComPort(ComA);// 2.1 �رմ���
		    }
		});
        //�˳�
        btnexit = (Button)findViewById(R.id.btnexit);		
        btnexit.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	CloseComPort(ComA);// 2.1 �رմ���
		    	finish();
		    }
		});
	}
	
	//��ȡ��ӡ��Ϣ
    private void ReadSharedPreferencesPrinter()
    {
    	//�ļ���˽�е�
    	SharedPreferences  user = getSharedPreferences("print_info",0);
    	//��ȡ
    	//����һ
    	istitle1=user.getBoolean("istitle1",true);
    	if(istitle1==false)
    	{
    		title1str="";
    	}
    	else
    	{
    		title1str=user.getString("title1str", "�����ۻ���");
    	}
		chktitle1.setChecked(istitle1);
		edittitle1.setEnabled(istitle1);
		edittitle1.setText(title1str);
		//�����
    	istitle2=user.getBoolean("istitle2",true);
    	if(istitle2==false)
    	{
    		title2str="";
    	}
    	else
    	{
    		title2str=user.getString("title2str", "����ƾ֤");
    	}
		chktitle2.setChecked(istitle2);
		edittitle2.setEnabled(istitle2);
		edittitle2.setText(title2str);
		//�������
		isno=user.getBoolean("isno",true);
		serialno=user.getInt("serialno", 1);
		chkno.setChecked(isno);		
		//���ͳ��
		issum=user.getBoolean("issum",true);
		chksum.setChecked(issum);
		//��л��ʾ
    	isthank=user.getBoolean("isthank",true);
    	if(isthank==false)
    	{
    		thankstr="";
    	}
    	else
    	{
    		thankstr=user.getString("thankstr", "ллʹ�������ۻ���,���ǽ��߳�Ϊ������!");
    	}
		chkthank.setChecked(isthank);
		editthank.setEnabled(isthank);
		editthank.setText(thankstr);
		//��ά��
    	iser=user.getBoolean("iser",true);
    	if(iser==false)
    	{
    		erstr="";
    	}
    	else
    	{
    		erstr=user.getString("erstr", "http://www.easivend.com.cn/");
    	}
		chker.setChecked(iser);
		editer.setEnabled(iser);
		editer.setText(erstr);
		//��ǰʱ��
		isdate=user.getBoolean("isdate",true);
		chkdate.setChecked(isdate);
    }
    //д���ӡ��Ϣ
    private void SaveSharedPreferencesPrinter()
    {
    	//�ļ���˽�е�
		SharedPreferences  user = getSharedPreferences("print_info",0);
		//��Ҫ�ӿڽ��б༭
		SharedPreferences.Editor edit=user.edit();
		//д��
		//����һ
		istitle1=chktitle1.isChecked();
		title1str=edittitle1.getText().toString();
		edit.putBoolean("istitle1", istitle1);
		edit.putString("title1str", title1str);
		//�����
		istitle2=chktitle2.isChecked();
		title2str=edittitle2.getText().toString();
		edit.putBoolean("istitle2", istitle2);
		edit.putString("title2str", title2str);
		//�������
		isno=chkno.isChecked();
		edit.putBoolean("isno", isno);
		edit.putInt("serialno", serialno);
		//���ͳ��
		issum=chksum.isChecked();
		edit.putBoolean("issum", issum);
		//��л��ʾ
		isthank=chkthank.isChecked();
		thankstr=editthank.getText().toString();
		edit.putBoolean("isthank", isthank);
		edit.putString("thankstr", thankstr);
		//��ά��
		iser=chker.isChecked();
		erstr=editer.getText().toString();
		edit.putBoolean("iser", iser);
		edit.putString("erstr", erstr);
		//��ǰʱ��
		isdate=chkdate.isChecked();
		edit.putBoolean("isdate", isdate);
		//�ύ����
		edit.commit();
    }
  
    //д����ˮ����Ϣ
    private void SaveSharedPreferencesSerialno()
    {
    	//�ļ���˽�е�
		SharedPreferences  user = getSharedPreferences("print_info",0);
		//��Ҫ�ӿڽ��б༭
		SharedPreferences.Editor edit=user.edit();
		//д��
		//�������
		edit.putInt("serialno", serialno);
		//�ύ����
		edit.commit();
    }
    
    // ------------------�򿪴���--------------------
    private void OpenComPort(SerialHelper ComPort) {
		try {
			ComPort.open();
		} catch (SecurityException e) {
			ToolClass.Log(ToolClass.ERROR,"EV_COM","û�ж�/дȨ��","com.txt");
		} catch (IOException e) {
			ToolClass.Log(ToolClass.ERROR,"EV_COM","δ֪����","com.txt");
		} catch (InvalidParameterException e) {
			ToolClass.Log(ToolClass.ERROR,"EV_COM","��������","com.txt");
		}
	}
    
    // ------------------�رմ���--------------------
 	private void CloseComPort(SerialHelper ComPort) {
 		if (ComPort != null) {
 			ComPort.stopSend();
 			ComPort.close();
 		}
 	}
 	
    // -------------------------��ѯ״̬---------------------------
  	private void GetPrinterStates(SerialHelper ComPort, byte[] sOut) {
  		Message childmsg=printmainhand.obtainMessage();
  		try { 			
 			if (ComPort != null && ComPort.isOpen()) {
 				ComPort.send(sOut);
 				ercheck = true; 				
 				ToolClass.Log(ToolClass.INFO,"EV_COM","״̬��ѯ��","com.txt");
 			} else
 			{
 				ToolClass.Log(ToolClass.ERROR,"EV_COM","����δ��","com.txt");
 				childmsg.what=UNKNOWERR;
 				childmsg.obj="����δ��";
 				printmainhand.sendMessage(childmsg);
 			}
 		} catch (Exception ex) {
 			ToolClass.Log(ToolClass.ERROR,"EV_COM",ex.getMessage(),"com.txt");
 			childmsg.what=UNKNOWERR;
			childmsg.obj=ex.getMessage();
			printmainhand.sendMessage(childmsg);
 		}
  		
 	}
 	/**
	 * ��ӡ���۵���
	 */
	private void PrintBankQueue() {
		try {
			// СƱ����
			byte[] bValue = new byte[100];
			ComA.send(PrintCmd.SetBold(0));
			ComA.send(PrintCmd.SetAlignment(1));
			ComA.send(PrintCmd.SetSizetext(1, 1));
			//����һ
			if(istitle1)
			{
				ComA.send(PrintCmd.PrintString(title1str+"\n\n", 0));
			}
			//�����
			if(istitle2)
			{
				ComA.send(PrintCmd.PrintString(title2str+"\n\n", 0));
			}
			//�������
			if(isno)
			{
				ComA.send(PrintCmd.SetBold(1));
				ComA.send(PrintCmd.PrintString(String.format("%04d", serialno++)+"\n\n", 0));
			}
			// СƱ��Ҫ����
			CleanPrinter(); // �����棬ȱʡģʽ
			//���ͳ��
			if(issum)
			{
				ComA.send(PrintCmd.PrintString("��������"+amount+"Ԫ\n\n", 0));
			}
			//��л��ʾ
			if(isthank)
			{
				ComA.send(PrintCmd.PrintString(thankstr+"\n", 0));
			}
			// ��ά��
			if(iser)
			{
				ComA.send(PrintCmd.PrintFeedline(2));    
				PrintQRCode();  // ��ά���ӡ                          
				ComA.send(PrintCmd.PrintFeedline(2));   
			}
			//��ǰʱ��
			if(isdate)
			{
				ComA.send(PrintCmd.SetAlignment(2));
				ComA.send(PrintCmd.PrintString(sdf.format(new Date()).toString() + "\n\n", 1));
			}
			// ��ֽ4��,����ֽ,������
			PrintFeedCutpaper(4);  
			SaveSharedPreferencesSerialno();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// ��ӡ��ά��
	private void PrintQRCode() throws IOException {
		byte[] bValue = new byte[50];
		bValue = PrintCmd.PrintQrcode(erstr, 25, 7, 1);
		ComA.send(bValue);
	}
	
	// ��ֽ���У�����ֽ��������
	private void PrintFeedCutpaper(int iLine) throws IOException{
		ComA.send(PrintCmd.PrintFeedline(iLine));
		ComA.send(PrintCmd.PrintCutpaper(0));
		ComA.send(PrintCmd.SetClean());
	}
	// �����棬ȱʡģʽ
	private void CleanPrinter(){
		ComA.send(PrintCmd.SetClean());
	}
    
	// -------------------------ˢ����ʾ�߳�---------------------------
	private class DispQueueThread extends Thread {
		private Queue<ComBean> QueueList = new LinkedList<ComBean>();
		@Override
		public void run() {
			super.run();
			while (!isInterrupted()) {
				final ComBean ComData;
				while ((ComData = QueueList.poll()) != null) {
					runOnUiThread(new Runnable() {
						public void run() {
							DispRecData(ComData);
						}
					});
					try {
						Thread.sleep(200);// ��ʾ���ܸߵĻ������԰Ѵ���ֵ��С��
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				}
			}
		}
		public synchronized void AddQueue(ComBean ComData) {
			QueueList.add(ComData);
		}
	}
	
	// ------------------------��ʾ��������----------------------------
	 /*
	  * 0 ��ӡ������ ��1 ��ӡ��δ���ӻ�δ�ϵ硢2 ��ӡ���͵��ÿⲻƥ�� 
	  * 3 ��ӡͷ�� ��4 �е�δ��λ ��5 ��ӡͷ���� ��6 �ڱ���� ��7 ֽ�� ��8 ֽ����
	  */
	private void DispRecData(ComBean ComRecData) {
		Message childmsg=printmainhand.obtainMessage();
		StringBuilder sMsg = new StringBuilder();
		try {
			sMsg.append(MyFunc.ByteArrToHex(ComRecData.bRec));
			int iState = PrintCmd.CheckStatus(ComRecData.bRec); // ���״̬
			ToolClass.Log(ToolClass.INFO,"EV_COM","����״̬��" + iState + "======="
					+ ComRecData.bRec[0],"com.txt");
			switch (iState) {
			case 0:
				sMsg.append("����");                 // ����
				ercheck = true;
				childmsg.what=NORMAL;
				break;
			case 1:
				sMsg.append("δ���ӻ�δ�ϵ�");//δ���ӻ�δ�ϵ�
				ercheck = true;
				childmsg.what=NOPOWER;
				break;
			case 2:
				sMsg.append("�쳣[��ӡ���͵��ÿⲻƥ��]");               //�쳣[��ӡ���͵��ÿⲻƥ��]
				ercheck = false;
				childmsg.what=NOMATCH;
				break;
			case 3:
				sMsg.append("��ӡ��ͷ��");        //��ӡ��ͷ��
				ercheck = true;
				childmsg.what=HEADOPEN;
				break;
			case 4:
				sMsg.append("�е�δ��λ");         //�е�δ��λ
				ercheck = true;
				childmsg.what=CUTTERERR;
				break;
			case 5:
				sMsg.append("��ӡͷ����");    // ��ӡͷ����
				ercheck = true;
				childmsg.what=HEADHEAT;
				break;
			case 6:
				sMsg.append("�ڱ����");         // �ڱ����
				ercheck = true;
				childmsg.what=BLACKMARKERR;
				break;
			case 7:
				sMsg.append("ֽ��");               //ֽ��
				ercheck = true;
				childmsg.what=PAPEREXH;
				break;
			case 8:
				sMsg.append("ֽ����");           //ֽ����
				ercheck = true;
				childmsg.what=PAPERWILLEXH;
				break;
			default:
				break;
			}
			childmsg.obj=sMsg.toString();
		} catch (Exception ex) {
			childmsg.what=UNKNOWERR;
			childmsg.obj=ex.getMessage();
		}
		printmainhand.sendMessage(childmsg);
	}
    // -------------------------�ײ㴮�ڿ�����---------------------------
    private static class SerialControl extends SerialHelper {
		public SerialControl() {
		}
		private static SerialControl single = null;
		// ��̬��������
		public static SerialControl getInstance() {
			if (single == null) {
				single = new SerialControl();
			}
			return single;
		}
		@Override
		protected void onDataReceived(final ComBean ComRecData) {
			DispQueue.AddQueue(ComRecData);// �̶߳�ʱˢ����ʾ(�Ƽ�)
		}
	}

}
