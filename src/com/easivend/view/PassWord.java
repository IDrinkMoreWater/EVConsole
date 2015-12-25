package com.easivend.view;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.easivend.app.maintain.HuodaoSet;
import com.easivend.common.ToolClass;
import com.example.evconsole.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class PassWord extends Activity 
{
	private BluetoothAdapter blueadapter=null;  
    private DeviceReceiver mydevice=new DeviceReceiver();  
    private List<String> deviceList=new ArrayList<String>();  
    private ListView deviceListview;  
    private Button btserch,btn_msg_send,btn_end; 
    private EditText editMsgView;
    private ArrayAdapter<String> adapter;  
    private boolean hasregister=false; 
    private BluetoothServerSocket mserverSocket = null;  
    private ServerThread startServerThread = null; 
    private readThread mreadThread = null; 
    private BluetoothSocket socket = null;  
    /* һЩ��������������������� */  
    public static final String PROTOCOL_SCHEME_RFCOMM = "btspp"; 
    private boolean isfile=false;
    ArrayList<Uri> uris = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password);// ���ò����ļ�
		//���ú������������Ĳ��ֲ���
		this.setRequestedOrientation(ToolClass.getOrientation());
		
		setView();//��ʼ���ؼ�  
		setBluetooth(); //1.�������豸
	}
	
	private void setView()
	{  
		editMsgView=(EditText)findViewById(R.id.editMsgView);  
        deviceListview=(ListView)findViewById(R.id.devicelist);  
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, deviceList);  
        deviceListview.setAdapter(adapter);          
        btserch=(Button)findViewById(R.id.start_seach);  
        btserch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(blueadapter.isDiscovering()){  
//	                blueadapter.cancelDiscovery();  
//	                btserch.setText("ɨһɨ1");  
	            }else{  
	                findAvalibleDevice(); //�����Ѿ����ڵ��豸 
	                blueadapter.startDiscovery();//2.���������豸
	                btserch.setText("����ɨ�裬���Ժ�");  
	            }  
			}
		}); 
        //����ѡ�е���������־�ļ������ϴ�
        deviceListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				   
		            final String msg = deviceList.get(pos);  
		              
		            if(blueadapter!=null&&blueadapter.isDiscovering())
		            {  
//		                blueadapter.cancelDiscovery();  
//		                btserch.setText("ɨһɨ2");  
		            }  
		            else 
		            {
		            	File f = new File(msg);
				          //����android������
				          Intent intent = new Intent();
				          intent.setAction(Intent.ACTION_SEND);
				          intent.setType("application/octet-stream");
				          intent.setClassName("com.android.bluetooth" , "com.android.bluetooth.opp.BluetoothOppLauncherActivity");
				      	  intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));				          
				          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				          startActivity(intent);
				          isfile=true;
					}	              
		          
			}
		});
        //ȷ�ϲ����˳�
        btn_msg_send = (Button) findViewById(R.id.btn_msg_send);
        btn_msg_send.setOnClickListener(new OnClickListener() {// Ϊ�˳���ť���ü����¼�
  		    @Override
  		    public void onClick(View arg0) {
  		        //�˳�ʱ������intent
	            Intent intent=new Intent();
	            intent.putExtra("pwd", editMsgView.getText().toString());
	            setResult(PassWord.RESULT_OK,intent);
  		    	finish();
  		    }
  		});  
  	   //�˳�
        btn_end = (Button) findViewById(R.id.btn_end);
        btn_end.setOnClickListener(new OnClickListener() {// Ϊ�˳���ť���ü����¼�
		    @Override
		    public void onClick(View arg0) {
		    	//�˳�ʱ������intent
	            Intent intent=new Intent();
	            intent.putExtra("pwd", "");
	            setResult(PassWord.RESULT_OK,intent);
		    	finish();
		    }
		});
    } 
	/** 
     * Setting Up Bluetooth 
     * //1.�������豸
     */  
    private void setBluetooth(){  
         blueadapter=BluetoothAdapter.getDefaultAdapter();  
           
            if(blueadapter!=null){  //Device support Bluetooth  
                //ȷ�Ͽ�������  
                if(!blueadapter.isEnabled()){  
                    //�����û�����  
                    Intent intent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);  
                    startActivityForResult(intent, RESULT_FIRST_USER);  
                    //ʹ�����豸�ɼ����������  ��Ĭ�ϴ�120�룬���Խ�ʱ���ӳ������300�룩
                    Intent in=new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);  
                    //0�������ÿɼ�����ֵ����ɼ�ʱ�䣬��sΪ��λ
                    in.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);  
                    startActivity(in);  
                    //ֱ�ӿ�������������ʾ  
                    blueadapter.enable(); 
                    //��ʾ����������ַ
                    deviceList.add("����"+blueadapter.getName()+'\n'+blueadapter.getAddress()); 
                }  
            }  
            else{   //Device does not support Bluetooth  
                  
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);  
                dialog.setTitle("û�������豸");  
                dialog.setMessage("����豸��֧������");  
                  
                dialog.setNegativeButton("cancel",  
                        new DialogInterface.OnClickListener() {  
                            @Override  
                            public void onClick(DialogInterface dialog, int which) {  
                                  
                            }  
                        });  
                dialog.show();  
            }  
    }
    
  
    /** 
     * Finding Devices 
     */  
    private void findAvalibleDevice(){  
        //��ȡ����������豸  
        Set<BluetoothDevice> device=blueadapter.getBondedDevices();  
          
        if(blueadapter!=null&&blueadapter.isDiscovering()){  
            deviceList.clear();  
            adapter.notifyDataSetChanged();  
        }  
        if(device.size()>0){ //�����Ѿ���Թ��������豸,������ʾ  
            for(Iterator<BluetoothDevice> it=device.iterator();it.hasNext();){  
                BluetoothDevice btd=it.next();  
                deviceList.add(btd.getName()+'\n'+btd.getAddress());  
                adapter.notifyDataSetChanged();  
            }  
        }else{  //�������Ѿ���Թ��������豸,������ʾ  
            deviceList.add("No can be matched to use bluetooth");  
            adapter.notifyDataSetChanged();  
        }  
    } 
    
    /** 
     * 4.��������״̬�㲥���� 
     * @author Andy 
     * 
     */  
    private class DeviceReceiver extends BroadcastReceiver{  
  
        @Override  
        public void onReceive(Context context, Intent intent) {  
            String action =intent.getAction();  
            if(BluetoothDevice.ACTION_FOUND.equals(action)){    //���������豸  
                BluetoothDevice btd=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);  
                //����û������Ե������豸  
                 if (btd.getBondState() != BluetoothDevice.BOND_BONDED) {  
                     deviceList.add(btd.getName()+'\n'+btd.getAddress());  
                     adapter.notifyDataSetChanged();  
                 }  
            }  
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
            {   //��������  
                   if(isfile==false)
                   {
	                    if (deviceListview.getCount() == 0) {  
	                        deviceList.add("No can be matched to use bluetooth");  
	                        adapter.notifyDataSetChanged();  
	                    }  
	                    btserch.setText("ɨһɨ3");  
	                    startServerThread = new ServerThread();  
	                    startServerThread.start();  
	                    BluetoothMsg.isOpen = true; 
                   }
            }             
        }     
    } 
    //3.ע���������չ㲥  
	/*startDiscovery()������һ���첽���������ú���������ء��÷�������ж����������豸���������ù��̻����12�롣�÷������ú���������ʵ��������һ��System Service�н��еģ����Կ��Ե���cancelDiscovery()������ֹͣ�������÷���������δִ��discovery����ʱ���ã���
	����Discovery��ϵͳ��ʼ���������豸������������У�ϵͳ�ᷢ�����������㲥��
    ACTION_DISCOVERY_START����ʼ����
	ACTION_DISCOVERY_FINISHED����������
	ACTION_FOUND���ҵ��豸�����Intent�а�������extra fields��EXTRA_DEVICE��EXTRA_CLASS���ֱ����BluetooDevice��BluetoothClass��
	���ǿ����Լ�ע����Ӧ��BroadcastReceiver��������Ӧ�Ĺ㲥���Ա�ʵ��ĳЩ����
	*/
    @Override  
    protected void onStart() 
    { 
        if(!hasregister)
        {  
            hasregister=true;  
            IntentFilter filterStart=new IntentFilter(BluetoothDevice.ACTION_FOUND);      
            IntentFilter filterEnd=new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);  
            registerReceiver(mydevice, filterStart);  
            registerReceiver(mydevice, filterEnd);  
        }         
        super.onStart();  
    }  
    
    //��ui��ʾ���̷߳���������ʾ��Ϣ
  	private Handler LinkDetectedHandler = new Handler() {  
          @Override  
          public void handleMessage(Message msg) {  
              //Toast.makeText(mContext, (String)msg.obj, Toast.LENGTH_SHORT).show();  
              if(((String)msg.obj).equals("getfile"))  
              {  
            	  deviceList.add("׼�������ļ�...");  
            	  listFiles(); 
              }  
              else if(((String)msg.obj).equals("getfilezip"))  
              {  
            	  deviceList.add("׼������ѹ����...");  
            	  zipFiles();
              }
              else  
              {  
                  deviceList.add((String)msg.obj);                    
              }  
              adapter.notifyDataSetChanged();  
              deviceListview.setSelection(deviceList.size() - 1);  
          }  
      };  
  	  //5.������ȡͨ��������  
      private class ServerThread extends Thread {   
          @Override  
          public void run() {  
                        
              try {  
                  /* ����һ������������  
                   * �����ֱ𣺷��������ơ�UUID  
                   * ����ͨ������listenUsingRfcommWithServiceRecord(String, UUID)��������
                   * ȡbluetoothserversocket���󣬲���string�����˸÷�������ƣ�UUID��
                   * ���˺Ϳͻ������ӵ�һ����ʶ��128λ��ʽ���ַ���ID���൱��pin�룩��
                   * UUID����˫��ƥ��ſ��Խ������ӡ���ε���accept������������������
                   * �������������󣬵��������Ժ󣬷���һ�������ϵ������׽�
                   * ��bluetoothsocket�� */   
                  mserverSocket = blueadapter.listenUsingRfcommWithServiceRecord(PROTOCOL_SCHEME_RFCOMM,  
                          UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));         
                    
                  ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<wait cilent connect...","log.txt");  
                  Message msg = new Message();  
                  msg.obj = "���Ժ����ڵȴ��ͻ��˵�����...";  
                  msg.what = 0;  
                  LinkDetectedHandler.sendMessage(msg);  
                    
                  /* ���ܿͻ��˵��������� */  
                  socket = mserverSocket.accept();  
                  ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<accept success !","log.txt");  
                  Message msg2 = new Message();  
                  String info = "�ͻ����Ѿ������ϣ����Է�����Ϣ��";  
                  msg2.obj = info;  
                  msg.what = 0;  
                  LinkDetectedHandler.sendMessage(msg2);  
                  //������������  
                  mreadThread = new readThread();  
                  mreadThread.start();  
              } catch (IOException e) {  
                  e.printStackTrace();  
              }  
          }  
      };  
      /* 7.ֹͣ��ȡͨ�������� */  
      private void shutdownServer() {  
          new Thread() {  
              @Override  
              public void run() {  
                  if(startServerThread != null)  
                  {  
                      startServerThread.interrupt();  
                      startServerThread = null;  
                  }  
                  if(mreadThread != null)  
                  {  
                      mreadThread.interrupt();  
                      mreadThread = null;  
                  }                 
                  try
                  {                     
                      if(socket != null)  
                      {  
                          socket.close();  
                          socket = null;  
                      }  
                      if (mserverSocket != null)  
                      {  
                          mserverSocket.close();/* �رշ����� */  
                          mserverSocket = null;  
                      }  
                  } catch (IOException e) {  
                      ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<mserverSocket.close()","log.txt"); 
                  }  
              };  
          }.start();  
      }
      //6.������ȡ�����߳�  
      private class readThread extends Thread {   
          @Override  
          public void run() {  
                
              byte[] buffer = new byte[1024];  
              int bytes;  
              InputStream mmInStream = null;  
                
              try {  
                  mmInStream = socket.getInputStream();  
              } catch (IOException e1) {  
                  // TODO Auto-generated catch block  
                  e1.printStackTrace();  
              }     
              while (true) {  
                  try {  
                      // Read from the InputStream  
                      if( (bytes = mmInStream.read(buffer)) > 0 )  
                      {  
                          byte[] buf_data = new byte[bytes];  
                          for(int i=0; i<bytes; i++)  
                          {  
                              buf_data[i] = buffer[i];  
                          }  
                          String s = new String(buf_data); 
                          if(s.equals("getfile")||s.equals("getfilezip"))  
                          {  
	                            Message msg = new Message();  
	                            msg.obj = s;  
	                            msg.what = 1;  
	                            LinkDetectedHandler.sendMessage(msg);
                          }
                          else 
                          {
                        	  //�˳�ʱ������intent
              	              Intent intent=new Intent();
              	              intent.putExtra("pwd", s);
              	              setResult(PassWord.RESULT_OK,intent);    		      
              	              finish();
						  }                          
                      }  
                  } catch (IOException e) {  
                      try {  
                          mmInStream.close();  
                      } catch (IOException e1) {  
                          // TODO Auto-generated catch block  
                          e1.printStackTrace();  
                      }  
                      break;  
                  }  
              }  
          }  
      } 
         
    //8.����ҳ��
    @Override  
    protected void onDestroy() 
    {  
    	shutdownServer();//���������߳�
    	//���������
        if(blueadapter!=null&&blueadapter.isDiscovering())
        {  
            blueadapter.cancelDiscovery();  
        }  
        if(hasregister)
        {  
            hasregister=false;  
            unregisterReceiver(mydevice);  
        }  
        //ֱ�ӹر������豸
        blueadapter.disable();  
        super.onDestroy();  
    } 
   
    /* ��������ȫ��������־�ļ� */  
    private void listFiles() 
    {  
    	//��������ļ�����������ļ�
		File file = new File(ToolClass.ReadLogFile());
		File[] files = file.listFiles();
		if (files.length > 0) 
		{  
			uris=new ArrayList<Uri>();
			for (int i = 0; i < files.length; i++) 
			{
			  if(!files[i].isDirectory())
			  {		
				  deviceList.add(files[i].toString());				  	
				  uris.add(Uri.fromFile(new File(files[i].toString())));
			  }
			}
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_SEND_MULTIPLE);
			intent.setType("application/octet-stream");
			intent.setClassName("com.android.bluetooth" , "com.android.bluetooth.opp.BluetoothOppLauncherActivity");
			intent.putExtra(Intent.EXTRA_STREAM, uris);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			isfile=true;
		}
		
		
    }
    
    /* ��������ȫ��������־��ѹ���ļ� */  
    private void zipFiles() 
    {  
    	//��������ļ�����������ļ�
		String srcFileString=ToolClass.ReadLogFile();
		String zipFileString=ToolClass.getEV_DIR()+File.separator+"logzip.zip";
		ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<srcFileString="+srcFileString+" zipFileString="+zipFileString,"log.txt"); 
		try {
			XZip.ZipFolder(srcFileString, zipFileString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File f = new File(zipFileString);
        //����android������
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("application/octet-stream");
        intent.setClassName("com.android.bluetooth" , "com.android.bluetooth.opp.BluetoothOppLauncherActivity");
    	intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));				          
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        isfile=true;
		
    }
	
}
