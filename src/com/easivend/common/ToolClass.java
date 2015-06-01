/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           ToolClass.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        �����࣬�������ŵ���Ҫ��static������static��Ա��ͳһ��Ϊȫ�ֱ�����ȫ�ֺ�����       
**------------------------------------------------------------------------------------------------------
** Created by:          guozhenzhen 
** Created date:        2015-01-10
** Version:             V1.0 
** Descriptions:        The original version       
********************************************************************************************************/


package com.easivend.common;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.json.JSONObject;

import com.easivend.app.business.BusZhiAmount;
import com.easivend.dao.vmc_logDAO;
import com.easivend.dao.vmc_orderDAO;
import com.easivend.dao.vmc_system_parameterDAO;
import com.easivend.model.Tb_vmc_log;
import com.easivend.model.Tb_vmc_order_pay;
import com.easivend.model.Tb_vmc_order_product;
import com.easivend.model.Tb_vmc_system_parameter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import android.R.integer;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.DisplayMetrics;
import android.util.Log;

public class ToolClass 
{
	public final static int VERBOSE=0;
	public final static int DEBUG=1;
	public final static int INFO=2;
	public final static int WARN=3;
	public final static int ERROR=4;
	private static int bentcom_id=-1,com_id=-1;
	
	
	public static int getBentcom_id() {
		return bentcom_id;
	}

	public static void setBentcom_id(int bentcom_id) {
		ToolClass.bentcom_id = bentcom_id;
	}

	public static int getCom_id() {
		return com_id;
	}

	public static void setCom_id(int com_id) {
		ToolClass.com_id = com_id;
	}

	//����Map����<String,Object>�����ݼ���
	public static Map<String, Object> getMapListgson(String jsonStr)
	{
		Map<String, Object> list=new HashMap<String,Object>();
		try {
			JSONObject object=new JSONObject(jsonStr);//{"EV_json":{"EV_type":"EV_STATE_RPT","state":2}}
			JSONObject perobj=object.getJSONObject("EV_json");//{"EV_type":"EV_STATE_RPT","state":2}
			Gson gson=new Gson();
			list=gson.fromJson(perobj.toString(), new TypeToken<Map<String, Object>>(){}.getType());
			//Log.i("EV_JNI",perobj.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}
	
	//Log���������ڴ�ӡlog�������ı��ļ��д�ӡ������־
	public static void Log(int info,String tag,String str)
	{
		String infotype="";
		switch(info)
		{
			case VERBOSE:
				infotype="VERBOSE";
				Log.v(tag,str);
				break;
			case DEBUG:
				infotype="DEBUG";
				Log.d(tag,str);
				break;
			case INFO:
				infotype="INFO";
				Log.i(tag,str);
				break;
			case WARN:
				infotype="WARN";
				Log.w(tag,str);
				break;
			case ERROR:
				infotype="ERROR";
				Log.e(tag,str);
				break;	
		}	
		infotype="("+infotype+"),["+tag+"] "+str;
		AppendLogFile(infotype);
	}
	
	/**
     * ׷���ļ���ʹ��FileWriter
     */
    public static void AppendLogFile(String content) 
    {
    	final String SDCARD_DIR=File.separator+"sdcard"+File.separator+"logs";
    	final String NOSDCARD_DIR=File.separator+"logs";
    	String  sDir =null;
    	File fileName=null;
    	SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd" + " "  
                + "hh:mm:ss:SSS"); //��ȷ������ 
        String datetime = tempDate.format(new java.util.Date()).toString();  
        String cont=datetime+content+"\n";
    	
        try {
        	  //�����ж�sdcard�Ƿ����
        	  String status = Environment.getExternalStorageState();
        	  if (status.equals(Environment.MEDIA_MOUNTED)) 
        	  {
        		 sDir = SDCARD_DIR;;
        	  } 
        	  else
        	  {
        		  sDir = NOSDCARD_DIR;
        	  }
        	  File dirName = new File(sDir);
        	 //���Ŀ¼�����ڣ��򴴽�Ŀ¼
        	 if (!dirName.exists()) 
        	 {  
                //����ָ����·�������ļ���  
        		dirName.mkdirs(); 
             }
        	 
        	 fileName=new File(sDir+File.separator+"log.txt");         	
        	//��������ڣ��򴴽��ļ�
        	if(!fileName.exists())
        	{  
    	      fileName.createNewFile(); 
    	    }  
            //��һ��д�ļ��������캯���еĵڶ�������true��ʾ��׷����ʽд�ļ�
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(cont);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * ��ȡ�����ļ�
     */
    public static Map<String, String> ReadConfigFile() 
    {
    	final String SDCARD_DIR=File.separator+"sdcard";
    	final String NOSDCARD_DIR=File.separator;
    	File fileName=null;
    	String  sDir =null,str=null;
    	Map<String, String> list=null;
    	    	
        try {
        	  //�����ж�sdcard�Ƿ����
        	  String status = Environment.getExternalStorageState();
        	  if (status.equals(Environment.MEDIA_MOUNTED)) 
        	  {
        		 sDir = SDCARD_DIR;;
        	  } 
        	  else
        	  {
        		  sDir = NOSDCARD_DIR;
        	  }
        	 
        	 
        	  fileName=new File(sDir+File.separator+"easivendconfig.txt");
        	  //������ڣ��Ŷ��ļ�
        	  if(fileName.exists())
        	  {
	    	  	 //���ļ�
	    		  FileInputStream input = new FileInputStream(sDir+File.separator+"easivendconfig.txt");
	    		 //�����Ϣ
	  	          Scanner scan=new Scanner(input);
	  	          while(scan.hasNext())
	  	          {
	  	           	str=scan.next()+"\n";
	  	          }
	  	         ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<config="+str);
	  	         //��json��ʽ���
	  	         list=new HashMap<String,String>();      			
				JSONObject object=new JSONObject(str);      				
				Gson gson=new Gson();
				list=gson.fromJson(object.toString(), new TypeToken<Map<String, Object>>(){}.getType());
				//Log.i("EV_JNI",perobj.toString());
				ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<config2="+list.toString());
        	  }
        	             
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    /**
     * д�������ļ�
     */
    public static void WriteConfigFile(String com,String bentcom) 
    {
    	final String SDCARD_DIR=File.separator+"sdcard";
    	final String NOSDCARD_DIR=File.separator;
    	File fileName=null;
    	String  sDir =null,str=null;
    	
    	    	
        try {
        	  //�����ж�sdcard�Ƿ����
        	  String status = Environment.getExternalStorageState();
        	  if (status.equals(Environment.MEDIA_MOUNTED)) 
        	  {
        		 sDir = SDCARD_DIR;;
        	  } 
        	  else
        	  {
        		  sDir = NOSDCARD_DIR;
        	  }
        	 
        	 
        	  fileName=new File(sDir+File.separator+"easivendconfig.txt");
        	  //��������ڣ��򴴽��ļ�
          	  if(!fileName.exists())
          	  {  
      	        fileName.createNewFile(); 
      	      } 
        	  
          	  //1.�����ݴ��ļ��ж���
    	  	  //���ļ�
    		  FileInputStream input = new FileInputStream(sDir+File.separator+"easivendconfig.txt");
    		  //�����Ϣ
  	          Scanner scan=new Scanner(input);
  	          while(scan.hasNext())
  	          {
  	           	str=scan.next()+"\n";
  	          }
  	         ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<config="+str);
  	         if(str!=null)
  	         {
	  	        Map<String, String> list=new HashMap<String,String>();      			
				JSONObject object=new JSONObject(str);      				
				Gson gson=new Gson();
				list=gson.fromJson(object.toString(), new TypeToken<Map<String, Object>>(){}.getType());
				//Log.i("EV_JNI",perobj.toString());
				ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<config2="+list.toString());
				Map<String,String> list2=new HashMap<String,String>();
				//�������
		        Set<Map.Entry<String,String>> allset=list.entrySet();  //ʵ����
		        Iterator<Map.Entry<String,String>> iter=allset.iterator();
		        while(iter.hasNext())
		        {
		            Map.Entry<String,String> me=iter.next();
		            if((me.getKey().equals("com")!=true)&&(me.getKey().equals("bentcom")!=true))
		            	list2.put(me.getKey(), me.getValue());
		            	//ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<config3="+me.getKey()+"--"+me.getValue());
		        } 	
		        list2.put("com", com);
		        list2.put("bentcom", bentcom);
		        ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<config3="+list2.toString());
		        JSONObject jsonObject = new JSONObject(list2);
		        String mapstrString=jsonObject.toString();
		        ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<config4="+mapstrString);
		        //��һ��д�ļ��������캯���еĵڶ�������true��ʾ��׷����ʽд�ļ�
	            FileWriter writer = new FileWriter(fileName);
	            writer.write(mapstrString);
	            writer.close();
  	         }
  	         else
  	         {
  	        	//ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<com="+com+","+bentcom);
  	        	JSONObject jsonObject = new JSONObject();
  	        	jsonObject.put("com", com);
  	        	jsonObject.put("bentcom", bentcom);
  	        	String mapstrString=jsonObject.toString();
  	        	ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<config2="+mapstrString);
  	            //��һ��д�ļ��������캯���еĵڶ�������true��ʾ��׷����ʽд�ļ�
  	            FileWriter writer = new FileWriter(fileName, true);
  	            writer.write(mapstrString);
  	            writer.close();
			 }
//  	         //��json��ʽ���
//  	         list=new HashMap<String,String>();      			
//			JSONObject object=new JSONObject(str);      				
//			Gson gson=new Gson();
//			list=gson.fromJson(object.toString(), new TypeToken<Map<String, Object>>(){}.getType());
//			//Log.i("EV_JNI",perobj.toString());
//			ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<config2="+list.toString());
        	//2.д�ص��ļ���  
        	             
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }
    
    //���������־
    public static void addOptLog(Context context, int logType, String logDesc)
	{
    	String id="";
 	    vmc_system_parameterDAO parameterDAO = new vmc_system_parameterDAO(context);// ����InaccountDAO����
	    // �õ��豸ID��
    	Tb_vmc_system_parameter tb_inaccount = parameterDAO.find();
    	if(tb_inaccount!=null)
    	{
    		id=tb_inaccount.getDevhCode().toString();
    	}
    	Log.i("EV_JNI","Send0.0="+id);
    	SimpleDateFormat tempDate = new SimpleDateFormat("yyyyMMddhhmmssSSS"); //��ȷ������ 
        String datetime = tempDate.format(new java.util.Date()).toString(); 					
        String logID="log"+id+datetime;
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//�������ڸ�ʽ
    	String date=df.format(new Date());
    	       
    	vmc_logDAO logDAO = new vmc_logDAO(context);// ����InaccountDAO����		
    	Tb_vmc_log tb_vmc_log=new Tb_vmc_log(logID, logType, logDesc,
    			date);
		logDAO.add(tb_vmc_log);		
	}
	
	//���ͽ����������Ԫ,תΪ�Է�Ϊ��λ���͵�����
	public static int MoneySend(float sendMoney)
	{
		int values=(int)(sendMoney*100);
		return values;
	}
	
	//���ս��ת�����������յķ�,תΪ�����Ԫ
	public static float MoneyRec(long Money)
	{
		float amount = (float)(Money/100);
		return amount;
	}
	
	/**
	 * �������Uri��������ļ�ϵͳ�е�·��
	 *
	 * @param context
	 * @param uri
	 * @return the file path or null
	 */
	public static String getRealFilePath( final Context context, final Uri uri )
	{
	    if ( null == uri ) 
	    	return "";
	    final String scheme = uri.getScheme();
	    String data = null;
	    if ( scheme == null )
	        data = uri.getPath();
	    else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
	        data = uri.getPath();
	    } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
	        Cursor cursor = context.getContentResolver().query( uri, new String[] { ImageColumns.DATA }, null, null, null );
	        if ( null != cursor ) {
	            if ( cursor.moveToFirst() ) {
	                int index = cursor.getColumnIndex( ImageColumns.DATA );
	                if ( index > -1 ) {
	                    data = cursor.getString( index );
	                }
	            }
	            cursor.close();
	        }
	    }
	    return data;
	}
	
	/**
     * ���ر���ͼƬ
     * @param url
     * @return
     */
     public static Bitmap getLoacalBitmap(String url) {
          try {
               FileInputStream fis = new FileInputStream(url);
               return BitmapFactory.decodeStream(fis);  ///����ת��ΪBitmapͼƬ        

            } catch (FileNotFoundException e) {
               e.printStackTrace();
               return null;
          }
     }
     
     /**
      * @��������˵��: ���ɶ�ά��ͼƬ,ʵ��ʹ��ʱҪ��ʼ��sweepIV,��Ȼ�ᱨ��ָ�����
      * @����:������
      * @ʱ��:2013-4-18����11:14:16
      * @����: @param url Ҫת���ĵ�ַ���ַ���,����������
      * @return void
      * @throws
      */
     //Ҫת���ĵ�ַ���ַ���,����������
     public static Bitmap createQRImage(String url)
     {
    	int QR_WIDTH = 200;
		int QR_HEIGHT = 200; 
     	try
     	{
     		//�ж�URL�Ϸ���
     		if (url == null || "".equals(url) || url.length() < 1)
     		{
     			return null;
     		}
     		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
     		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
     		//ͼ������ת����ʹ���˾���ת��
     		BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
     		int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
     		//�������ﰴ�ն�ά����㷨��������ɶ�ά���ͼƬ��
     		//����forѭ����ͼƬ����ɨ��Ľ��
     		for (int y = 0; y < QR_HEIGHT; y++)
     		{
     			for (int x = 0; x < QR_WIDTH; x++)
     			{
     				if (bitMatrix.get(x, y))
     				{
     					pixels[y * QR_WIDTH + x] = 0xff000000;
     				}
     				else
     				{
     					pixels[y * QR_WIDTH + x] = 0xffffffff;
     				}
     			}
     		}
     		//���ɶ�ά��ͼƬ�ĸ�ʽ��ʹ��ARGB_8888
     		Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
     		bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
     		return bitmap;
     	}
     	catch (WriterException e)
     	{
     		e.printStackTrace();
     	}
		return null;
     }    
     
    //out_trade_no���������ɶ�����
 	public static String out_trade_no(Context context)
 	{
 		String id="";
 	    String out_trade_no=null;
 		vmc_system_parameterDAO parameterDAO = new vmc_system_parameterDAO(context);// ����InaccountDAO����
	    // �õ��豸ID��
    	Tb_vmc_system_parameter tb_inaccount = parameterDAO.find();
    	if(tb_inaccount!=null)
    	{
    		id=tb_inaccount.getDevhCode().toString();
    	}
    	Log.i("EV_JNI","Send0.0="+id);
    	SimpleDateFormat tempDate = new SimpleDateFormat("yyyyMMddhhmmssSSS"); //��ȷ������ 
        String datetime = tempDate.format(new java.util.Date()).toString(); 					
        out_trade_no=id+datetime;
        return out_trade_no;
 	}
 	
 	/*********************************************************************************************************
	** Function name:     	typestr
	** Descriptions:	       ����������״̬ת���ַ�����ʾ
	** input parameters:    type=0֧����ʽ,1����״̬,2�˿�״̬
	** payType;// ֧����ʽ0�ֽ�1������2֧����������3֧������ά�룬4΢��ɨ��
	** payStatus;// ����״̬0�����ɹ���1����ʧ�ܣ�2֧��ʧ�ܣ�3δ֧��
	** RealStatus;// �˿�״̬��0����ʾδ�����˿����1�˿���ɣ�2�����˿3�˿�ʧ��
	** logType    ��������0���,1�޸�,2ɾ��
	** output parameters:   ��
	** Returned value:      ��
	*********************************************************************************************************/
	public static String typestr(int type,int value)
	{
		switch(type)
		{
			case 0:// ֧����ʽ
				// ֧����ʽ0�ֽ�1������2֧����������3֧������ά�룬4΢��ɨ��
				switch(value)
				{
					case 0:
						return "�ֽ�";						
					case 1:
						return "����";	
					case 2:
						return "֧��������";
					case 3:
						return "֧������ά��";
					case 4:
						return "΢֧��";		
				}
				break;
			case 1:// ����״̬
				// ����״̬0�����ɹ���1����ʧ�ܣ�2֧��ʧ�ܣ�3δ֧��
				switch(value)
				{
					case 0:
						return "�����ɹ�";						
					case 1:
						return "����ʧ��";	
					case 2:
						return "֧��ʧ��";
					case 3:
						return "δ֧��";					
				}
				break;
			case 2:// �˿�״̬
				// �˿�״̬��0����ʾδ�����˿����1�˿���ɣ�2�����˿3�˿�ʧ��
				switch(value)
				{
					case 0:
						return "";						
					case 1:
						return "�˿����";	
					case 2:
						return "�����˿�";
					case 3:
						return "�˿�ʧ��";					
				}
				break;
			case 3:// ��������
				// ��������0���,1�޸�,2ɾ��
				switch(value)
				{
					case 0:
						return "���";						
					case 1:
						return "�޸�";	
					case 2:
						return "ɾ��";									
				}
				break;	
		}
		return "";
	}
}
