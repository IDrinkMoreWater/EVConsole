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
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.security.KeyStore;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import org.apache.http.conn.ssl.SSLContexts;
import org.json.JSONException;
import org.json.JSONObject;

import com.easivend.alipay.AlipayConfig;
import com.easivend.alipay.AlipayConfigAPI;
import com.easivend.app.business.BusZhiAmount;
import com.easivend.app.maintain.HuodaoSet;
import com.easivend.app.maintain.ParamManager;
import com.easivend.dao.vmc_columnDAO;
import com.easivend.dao.vmc_logDAO;
import com.easivend.dao.vmc_system_parameterDAO;
import com.easivend.evprotocol.EVprotocol;
import com.easivend.model.Tb_vmc_column;
import com.easivend.model.Tb_vmc_log;
import com.easivend.model.Tb_vmc_system_parameter;
import com.easivend.weixing.WeiConfig;
import com.easivend.weixing.WeiConfigAPI;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.Log;

public class ToolClass 
{
	public final static int VERBOSE=0;
	public final static int DEBUG=1;
	public final static int INFO=2;
	public final static int WARN=3;
	public final static int ERROR=4;
	public static String EV_DIR=null;//ev���ĵ�ַ
	private static int bentcom_id=-1,com_id=-1,columncom_id=-1;//����id��
	private static String bentcom="",com="",columncom="";//����������
	private static int bill_err=0,coin_err=0;//ֽ������Ӳ��������״̬
	public static String vmc_no="";//�������
	public static Bitmap mark=null;//����ͼƬ
	public static int goc=0;//�Ƿ�ʹ�ó���ȷ�ϰ�1��
	public static Map<Integer, Integer> huodaolist=null;//�����߼���������������Ķ�Ӧ��ϵ
	public static int orientation=0;//ʹ�ú�����������ģʽ
	public static SSLSocketFactory ssl=null;//ssl�������
	public static Context context=null;//��Ӧ��context
	private static int ServerVer=0;//0�ɵĺ�̨��1һ�ڵĺ�̨
	public static String version="";//�����汾��
	
	public static String getVersion() {
		String curVersion=null;
		int curVersionCode=0;
		 try {
	            PackageInfo pInfo = context.getPackageManager().getPackageInfo(
	            		context.getPackageName(), 0);
	            curVersion = pInfo.versionName;
	            curVersionCode = pInfo.versionCode;
	        } catch (NameNotFoundException e) {
	            Log.e("update", e.getMessage());
	            curVersion = "1.1.1000";
	            curVersionCode = 111000;
	        }
		 version=(curVersion+curVersionCode).toString();
		return version;
	}

	public static int getServerVer() {
		return ServerVer;
	}

	public static void setServerVer(int serverVer) {
		ServerVer = serverVer;
	}

	public static String getBentcom() {
		return bentcom;
	}

	public static void setBentcom(String bentcom) {
		ToolClass.bentcom = bentcom;
	}

	public static String getCom() {
		return com;
	}

	public static void setCom(String com) {
		ToolClass.com = com;
	}

	public static String getColumncom() {
		return columncom;
	}

	public static void setColumncom(String columncom) {
		ToolClass.columncom = columncom;
	}

	public static Context getContext() {
		return context;
	}

	public static void setContext(Context context) {
		ToolClass.context = context;
	}

	public static String getEV_DIR() {
		return EV_DIR;
	}

	public static void setEV_DIR(String eV_DIR) {
		EV_DIR = eV_DIR;
	}

	public static SSLSocketFactory getSsl() {
		return ssl;
	}

	public static void setSsl(SSLSocketFactory ssl) {
		ToolClass.ssl = ssl;
	}

	public static int getOrientation() {
		return orientation;
	}

	public static void setOrientation(int orientation) {
		ToolClass.orientation = orientation;
	}

	public static Bitmap getMark() {
		return mark;
	}

	public static void setMark(Bitmap mark) {
		ToolClass.mark = mark;
	}
	
	public static int getGoc() {
		return goc;
	}

	public static void setGoc(Context context) 
	{
		vmc_system_parameterDAO parameterDAO = new vmc_system_parameterDAO(context);// ����InaccountDAO����
	    // ��ȡ����������Ϣ�����洢��List���ͼ�����
    	Tb_vmc_system_parameter tb_inaccount = parameterDAO.find();
    	if(tb_inaccount!=null)
    	{
    		ToolClass.goc = tb_inaccount.getIsbuyCar();
    	}
    	ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<goc="+ToolClass.goc,"log.txt");	
	}

	public static String getVmc_no() {
		return vmc_no;
	}

	public static void setVmc_no(String vmc_no) {
		ToolClass.vmc_no = vmc_no;
	}

	public static int getBentcom_id() {
		return bentcom_id;
	}

	public static void setBentcom_id(int bentcom_id) {
		ToolClass.bentcom_id = bentcom_id;
	}
	
	public static int getColumncom_id() {
		return columncom_id;
	}

	public static void setColumncom_id(int columncom_id) {
		ToolClass.columncom_id = columncom_id;
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
	
	/**
     * ���ø�Ŀ¼�ļ�
     */
    public static void SetDir() 
    {
    	final String SDCARD_DIR=File.separator+"sdcard"+File.separator+"ev";
    	final String NOSDCARD_DIR=File.separator+"ev";
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
        	  ToolClass.setEV_DIR(sDir); 
        	             
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	//Log���������ڴ�ӡlog�������ı��ļ��д�ӡ������־
	public static void Log(int info,String tag,String str,String filename)
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
		AppendLogFile(infotype,filename);
	}
	
	/**
     * ׷���ļ���ʹ��FileWriter
     */
    public static void AppendLogFile(String content,String filename) 
    {
    	String  sDir =null;
    	File fileName=null;
    	SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd" + " "  
                + "HH:mm:ss:SSS"); //��ȷ������ 
        String datetime = tempDate.format(new java.util.Date()).toString();  
        String cont=datetime+content+"\n";
    	
        try {
        	 sDir = ToolClass.getEV_DIR()+File.separator+"logs";
        	
        	  File dirName = new File(sDir);
        	 //���Ŀ¼�����ڣ��򴴽�Ŀ¼
        	 if (!dirName.exists()) 
        	 {  
                //����ָ����·�������ļ���  
        		dirName.mkdirs(); 
             }
        	 
        	 fileName=new File(sDir+File.separator+filename);         	
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
     * ����������־����һ����������ļ�������¾��������ļ�
     */
    public static void optLogFile() 
    {
    	String  sDir =null;
    	File fileName=null;
    	SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd" + " "  
                + "HH:mm:ss"); //��ȷ������ 
        String datetime = tempDate.format(new java.util.Date()).toString();  
        
    	
        try {
        	  sDir = ToolClass.getEV_DIR()+File.separator+"logs";
        	  
        	  File dirName = new File(sDir);
        	 //���Ŀ¼�����ڣ��򴴽�Ŀ¼
        	 if (!dirName.exists()) 
        	 {  
                //����ָ����·�������ļ���  
        		dirName.mkdirs(); 
             }
        	        	
        	//2.�������log�ļ������ж�
        	fileName=new File(sDir+File.separator+"log.txt"); 
        	if(fileName.exists())
        	{  
        		System.out.println(" �ж��������ļ�log.txt");
            	String logdatetime = getFileCreated(fileName);
            	int inter=getInterval(logdatetime,datetime); 
            	if(inter>=4)
            	{
            		updatefile(fileName,sDir);
            	}
    	    }
        	//3.�������dog�ļ������ж�
        	fileName=new File(sDir+File.separator+"dog.txt"); 
        	if(fileName.exists())
        	{  
        		System.out.println(" �ж��������ļ�dog.txt");
        		String logdatetime = getFileCreated(fileName);
            	int inter=getInterval(logdatetime,datetime); 
            	if(inter>=4)
            	{
            		updatefile(fileName,sDir);
            	}
    	    } 
        	//4.�������server�ļ������ж�
        	fileName=new File(sDir+File.separator+"server.txt"); 
        	if(fileName.exists())
        	{  
        		System.out.println(" �ж��������ļ�server.txt");
        		String logdatetime = getFileCreated(fileName);
            	int inter=getInterval(logdatetime,datetime); 
            	if(inter>=4)
            	{
            		updatefile(fileName,sDir);
            	}
    	    }
        	//5.�������com�ļ������ж�
        	fileName=new File(sDir+File.separator+"com.txt"); 
        	if(fileName.exists())
        	{  
        		System.out.println(" �ж��������ļ�com.txt");
        		String logdatetime = getFileCreated(fileName);
            	int inter=getInterval(logdatetime,datetime); 
            	if(inter>=4)
            	{
            		updatefile(fileName,sDir);
            	}
    	    }
        	//6.��Ŀ¼�µ������ļ�������г�������µģ�ȫ��ɾ��
        	delFiles(dirName,datetime);
        	
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	//��ȡ�ļ�����ʱ��
    public static String getFileCreated(final File file)  
    {  
		 String res=null;
		 Scanner scan = null ;
		 try{
		 	scan = new Scanner(file) ;	// ���ļ���������
		 	if(scan.hasNext())
		 	{
		 		res=scan.next()+" "+scan.next();	//	ȡ����		 		
		 	}
		 	
		 }catch(Exception e){}
		 res=res.substring(0, res.indexOf("(INFO)"));// ��������Ϣ�н�ȡ������
		 System.out.println(" �ļ�����ʱ��1="+res);
         return res;
    }
	
	 /**
     * �ж��뵱ǰʱ������,createtime���ļ�����ʱ��,datetime�ǵ�ǰʱ��
     * �����ʱ���ʽ����������2012-8-21 17:53:20�����ĸ�ʽ  
     * ����ֵ��1�룬2�֣�3ʱ��4�죬5�����
     */
    public static int getInterval(String createtime,String datetime) 
	 { 
	        String interval = null;  
	        int inter=0;
	        
	        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	        
	        ParsePosition pos = new ParsePosition(0);  
	        Date d1 = (Date) sd.parse(createtime, pos); 
	        System.out.println(" �ļ�����ʱ��2="+createtime+",="+d1.getTime());
	        
	        ParsePosition posnow = new ParsePosition(0);  
	        Date dnow = (Date) sd.parse(datetime, posnow);
	        System.out.println(" ��ǰʱ��="+datetime+",="+dnow.getTime());
	          
	        //�����ھ���1970���ʱ����new Date().getTime()��ȥ��ǰ��ʱ�����1970���ʱ����d1.getTime()�ó��ľ�����ǰ��ʱ��������ʱ���ʱ����  
	        long time = dnow.getTime() - d1.getTime();// �ó���ʱ�����Ǻ���  
	        
        	  
	        if(time/1000 < 60 && time/1000 >= 0) 
	        {  
	        //���ʱ����С��60������ʾ������ǰ  
	        	if(time/1000>0)
	        	{
		            int se = (int) (time/1000);  
		            interval = se + "��ǰ";  
		            inter=1;
	        	}	        	
	        }
	        else if(time/60000 < 60 && time/60000 >= 0)
	        {  
	            //���ʱ����С��60��������ʾ���ٷ���ǰ  
	        	if(time/60000>0)
	        	{
		            int m = (int) (time/60000);//�ó���ʱ�����ĵ�λ�Ƿ���  
		            interval = m + "����ǰ"; 
		            inter=2;
	        	}
	        }
	        else if(time/3600000 < 24 && time/3600000 >= 0) 
	        {  
	            //���ʱ����С��24Сʱ����ʾ����Сʱǰ  
	        	if(time/3600000>0)
	        	{
		            int h = (int) (time/3600000);//�ó���ʱ�����ĵ�λ��Сʱ  
		            interval = h + "Сʱǰ";  
		            inter=3;
	        	}
	        }
	        else if(time/86400000 < 15 && time/86400000 >= 0)
	        {  
	        	//���ʱ����С��15������ʾ������ǰ  
	        	if(time/86400000>0)
	        	{
		            int d = (int) (time/86400000);//�ó���ʱ�����ĵ�λ��Сʱ 
		            interval = d + "��ǰ";  
		            inter=4;
	        	}
	        } 
	        else
	        {
	        	interval = "���˰������"; 
	            inter=5;
			}
	        System.out.println(" ʱ�����="+time+",interval="+interval);
	        return inter;  
	 }
	 
	//�������ļ���fileNameԭ�ļ���,sDir��Ŀ¼
    public static void updatefile(File fileName,String  sDir)
	{
		SimpleDateFormat tempDate2 = new SimpleDateFormat("yyyy-MM-dd-HHmmss"); //��ȷ������ 
        String datetime2 = tempDate2.format(new java.util.Date()).toString();
        String oldname=fileName.getName();
		String newname=datetime2+oldname;
		System.out.println(fileName+" �޸��ļ�����="+newname);            		
		fileName.renameTo(new File(sDir+File.separator+newname));
	}
	
	 /* ����Ŀ¼���ļ��б� file��Ŀ¼����datetime�ǵ�ǰʱ�䣬�����������£���ɾ��������ļ�
	  * */  
    public static void delFiles(File file,String datetime) 
    {  
    	//��������ļ�����������ļ�
		File[] files = file.listFiles();
		if (files.length > 0) 
		{  
			for (int i = 0; i < files.length; i++) 
			{
			  if(!files[i].isDirectory())
			  {		
				    System.out.println(" �ж�ɾ��Ŀ¼���ļ�="+files[i].toString()); 
				    //3.�������dog�ļ������ж�
		        	File fileName=new File(files[i].toString()); 
		        	if(fileName.exists())
		        	{  
		        		String logdatetime = getFileCreated(fileName);
		            	int inter=getInterval(logdatetime,datetime); 
		            	if(inter>=5)
		            	{
		            		System.out.println(" ���ļ�ɾ��");
		            		fileName.delete();		            		
		            	}
		    	    } 
			  }
			}
		}    
    }
    
    /**
     * ʹ��isAPKFile,�ж�����������Ѿ�����Ŀ¼��,true����,false������
     */
    public static boolean isAPKFile(String filename) 
    {
    	String  sDir =null;
    	File fileName=null;
    	boolean fileext=false;
        try {
        	  sDir = ToolClass.getEV_DIR();
        	  File dirName = new File(sDir);
        	 //���Ŀ¼�����ڣ��򴴽�Ŀ¼
        	 if (!dirName.exists()) 
        	 {  
                //����ָ����·�������ļ���  
        		dirName.mkdirs(); 
             }
        	 
        	 fileName=new File(sDir+File.separator+filename);         	
        	//��������ڣ��򴴽��ļ�
        	if(!fileName.exists())
        	{  
        		fileext=false; 
    	    }  
        	else
        		fileext=true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileext; 
    }
    
     
    /**
     * ʹ��isImgFile,�ж������ƷͼƬ���Ѿ�����Ŀ¼��,true����,false������
     */
    public static boolean isImgFile(String filename) 
    {
    	String  sDir =null;
    	File fileName=null;
    	boolean fileext=false;
        try {
        	  sDir = ToolClass.getEV_DIR()+File.separator+"productImage";
        	  File dirName = new File(sDir);
        	 //���Ŀ¼�����ڣ��򴴽�Ŀ¼
        	 if (!dirName.exists()) 
        	 {  
                //����ָ����·�������ļ���  
        		dirName.mkdirs(); 
             }
        	 
        	 fileName=new File(sDir+File.separator+filename+".jpg");         	
        	//��������ڣ��򴴽��ļ�
        	if(!fileName.exists())
        	{  
        		fileext=false; 
    	    }  
        	else
        		fileext=true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileext; 
    }
            
    //��BitmapͼƬ�����ڱ���
    public static boolean  saveBitmaptofile(Bitmap bmp,String filename)
    {      	
    	String  sDir =null;
    	File fileName=null;
    	boolean fileext=false;
        try {
        	  sDir = ToolClass.getEV_DIR()+File.separator+"productImage";
        	  File dirName = new File(sDir);
        	 //���Ŀ¼�����ڣ��򴴽�Ŀ¼
        	 if (!dirName.exists()) 
        	 {  
                //����ָ����·�������ļ���  
        		dirName.mkdirs(); 
             }
        	 
        	 fileName=new File(sDir+File.separator+filename+".jpg");         	
        	//��������ڣ���ʼ����ͼƬ
        	if(!fileName.exists())
        	{  
        		CompressFormat format= Bitmap.CompressFormat.JPEG;  
    	        int quality = 100;  
    	        OutputStream stream = null;  
    	        stream = new FileOutputStream(fileName);      	         
    	        fileext=bmp.compress(format, quality, stream); 
    	    }  
        	else
        		fileext=false;
        } catch (Exception e) {
            e.printStackTrace();
        }   
       return fileext; 
     } 
    
    /**
     * ʹ��getImgFile,�õ������ƷͼƬ������Ŀ¼
     */
    public static String getImgFile(String filename) 
    {
    	String  sDir =null;
    	String fileName=null;
    	try {
    		  sDir = ToolClass.getEV_DIR()+File.separator+"productImage";
        	  File dirName = new File(sDir);
        	 //���Ŀ¼�����ڣ��򴴽�Ŀ¼
        	 if (!dirName.exists()) 
        	 {  
                //����ָ����·�������ļ���  
        		dirName.mkdirs(); 
             }
        	 
        	fileName=sDir+File.separator+filename+".jpg";  
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName; 
    }
    
    /**
     * ��ȡ����ļ�
     */
    public static String ReadAdsFile() 
    {
    	String  sDir =null;
    	sDir = ToolClass.getEV_DIR()+File.separator+"ads"+File.separator;
    	return sDir;
    }
    
    /**
     * ��ȡ��־�ļ�
     */
    public static String ReadLogFile() 
    {
    	String  sDir =null;
    	sDir = ToolClass.getEV_DIR()+File.separator+"logs"+File.separator;
    	return sDir;
    }
    
    /**
     * �������û�е���ɹ�֧��������΢�ŵ��˺���Ϣ�����µ���һ��
     */
    public static void CheckAliWeiFile()
    {
    	if(
    	  (AlipayConfig.getPartner()==null)||(AlipayConfig.getSeller_email()==null)
    	  ||(AlipayConfig.getKey()==null)
    	  ||(WeiConfig.getWeiappid()==null)||(WeiConfig.getWeimch_id()==null)
    	  ||(WeiConfig.getWeikey()==null)
    	  )
    	{
	    	//�������ļ���ȡ����
			Map<String, String> list=ReadConfigFile();
			if(list!=null)
			{
		        AlipayConfigAPI.SetAliConfig(list);//���ð����˺�
		        WeiConfigAPI.SetWeiConfig(list);//����΢���˺�	        
			}
			//����΢��֤��
			setWeiCertFile();
    	}
    }
    
    /**
     * ��ȡ�����ļ�
     */
    public static Map<String, String> ReadConfigFile() 
    {
    	File fileName=null;
    	String  sDir =null,str=null;
    	Map<String, String> list=null;
    	    	
        try {
        	  sDir = ToolClass.getEV_DIR()+File.separator+"easivendconfig.txt";
        	  fileName=new File(sDir);
        	  //������ڣ��Ŷ��ļ�
        	  if(fileName.exists())
        	  {
	    	  	 //���ļ�
	    		  FileInputStream input = new FileInputStream(sDir);
	    		 //�����Ϣ
	  	          Scanner scan=new Scanner(input);
	  	          while(scan.hasNext())
	  	          {
	  	           	str=scan.next()+"\n";
	  	          }
	  	         ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<config="+str,"log.txt");
	  	         //��json��ʽ���
	  	         list=new HashMap<String,String>();      			
				JSONObject object=new JSONObject(str);      				
				Gson gson=new Gson();
				list=gson.fromJson(object.toString(), new TypeToken<Map<String, Object>>(){}.getType());
				//Log.i("EV_JNI",perobj.toString());
				ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<config2="+list.toString(),"log.txt");
        	  }
        	             
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    /**
     * д�������ļ�
     */
    public static void WriteConfigFile(String com,String bentcom,String columncom,String server,String isallopen) 
    {
    	File fileName=null;
    	String  sDir =null,str=null;
    	
    	    	
        try {
        	  sDir = ToolClass.getEV_DIR()+File.separator+"easivendconfig.txt";
        	 
        	  fileName=new File(sDir);
        	  //��������ڣ��򴴽��ļ�
          	  if(!fileName.exists())
          	  {  
      	        fileName.createNewFile(); 
      	      } 
        	  
          	  //1.�����ݴ��ļ��ж���
    	  	  //���ļ�
    		  FileInputStream input = new FileInputStream(sDir);
    		  //�����Ϣ
  	          Scanner scan=new Scanner(input);
  	          while(scan.hasNext())
  	          {
  	           	str=scan.next()+"\n";
  	          }
  	         ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<config="+str,"log.txt");
  	         if(str!=null)
  	         {
	  	        Map<String, String> list=new HashMap<String,String>();      			
				JSONObject object=new JSONObject(str);      				
				Gson gson=new Gson();
				list=gson.fromJson(object.toString(), new TypeToken<Map<String, Object>>(){}.getType());
				//Log.i("EV_JNI",perobj.toString());
				ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<config2="+list.toString(),"log.txt");
				Map<String,String> list2=new HashMap<String,String>();
				//�������
		        Set<Map.Entry<String,String>> allset=list.entrySet();  //ʵ����
		        Iterator<Map.Entry<String,String>> iter=allset.iterator();
		        while(iter.hasNext())
		        {
		            Map.Entry<String,String> me=iter.next();
		            if(
		            		(me.getKey().equals("com")!=true)
		            	  &&(me.getKey().equals("bentcom")!=true)
		            	  &&(me.getKey().equals("columncom")!=true)
		            	  &&(me.getKey().equals("isallopen")!=true)
		            	  &&(me.getKey().equals("server")!=true)
		              )
		            	list2.put(me.getKey(), me.getValue());
		            	//ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<config3="+me.getKey()+"--"+me.getValue());
		        } 	
		        list2.put("com", com);
		        list2.put("bentcom", bentcom);
		        list2.put("columncom", columncom);
		        list2.put("isallopen", isallopen);
		        list2.put("server", server);
		        ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<config3="+list2.toString(),"log.txt");
		        JSONObject jsonObject = new JSONObject(list2);
		        String mapstrString=jsonObject.toString();
		        ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<config4="+mapstrString,"log.txt");
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
  	        	jsonObject.put("columncom", columncom);
  	        	jsonObject.put("isallopen", isallopen);
  	        	jsonObject.put("server", server);
  	        	String mapstrString=jsonObject.toString();
  	        	ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<config2="+mapstrString,"log.txt");
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
    
    /**
     * ����΢��֤���ļ�
     */
    public static void setWeiCertFile() 
    {
    	File fileName=null;
    	String  sDir =null,str=null,mch_id=null;
    	
    	mch_id=WeiConfig.getWeimch_id();
        try {
        	  sDir = ToolClass.getEV_DIR()+File.separator+"cert"+File.separator+"apiclient_cert.p12";
        	 
        	 
        	  fileName=new File(sDir);
        	  //������ڣ��Ŷ��ļ�
        	  if(fileName.exists())
        	  {
        		//ָ����ȡ֤���ʽΪPKCS12
    	    	KeyStore keyStore = KeyStore.getInstance("PKCS12");
    	    	//��ȡ������ŵ�PKCS12֤���ļ�
    	    	FileInputStream instream = new FileInputStream(sDir);
    	    	try 
    	    	{
    	    		//ָ��PKCS12������(�̻�ID)
    	    		keyStore.load(instream, mch_id.toCharArray());
	    		} 
    	    	finally 
    	    	{
    	    		instream.close();
	    		}	
    	    	SSLContext sslcontext = SSLContexts.custom()
    	        		.loadKeyMaterial(keyStore, mch_id.toCharArray()).build();
    	    	ToolClass.ssl=sslcontext.getSocketFactory(); 
				ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<havessl,mch_id="+mch_id,"log.txt");
        	  }
        	  else 
        	  {
        		  ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<nossl","log.txt");
			  }
        	             
        } catch (Exception e) {
            e.printStackTrace();
            ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<sslerror","log.txt");
        }
        
    }
    
    /**
     * ��ȡ���������ļ�
     */
    public static Map<String, Integer> ReadColumnFile() 
    {
    	File fileName=null;
    	String  sDir =null,str=null;
    	Map<String, Integer> list=null;
    	    	
        try {
        	  sDir = ToolClass.getEV_DIR()+File.separator+"evColumnconfig.txt";
        	  fileName=new File(sDir);
        	  //������ڣ��Ŷ��ļ�
        	  if(fileName.exists())
        	  {
	    	  	 //���ļ�
	    		  FileInputStream input = new FileInputStream(sDir);
	    		 //�����Ϣ
	  	          Scanner scan=new Scanner(input);
	  	          while(scan.hasNext())
	  	          {
	  	           	str=scan.next()+"\n";
	  	          }
	  	         ToolClass.Log(ToolClass.INFO,"EV_COM","APP<<config="+str,"com.txt");
	  	         //��json��ʽ���
	  	         list=new HashMap<String,Integer>();   
	  	         huodaolist=new HashMap<Integer,Integer>(); 
				JSONObject object=new JSONObject(str);      				
				Gson gson=new Gson();
				list=gson.fromJson(object.toString(), new TypeToken<Map<String, Integer>>(){}.getType());
				//�������
		        Set<Entry<String, Integer>> allmap=list.entrySet();  //ʵ����
		        Iterator<Entry<String, Integer>> iter=allmap.iterator();
		        while(iter.hasNext())
		        {
		            Entry<String, Integer> me=iter.next();	
	            	huodaolist.put(Integer.parseInt(me.getKey()),(Integer)me.getValue());		            
		        } 			
				//Log.i("EV_JNI",perobj.toString());
				ToolClass.Log(ToolClass.INFO,"EV_COM","APP<<config2="+huodaolist.toString(),"com.txt");
        	  }
        	             
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    /**
     * ��ȡ�߼�����������ñ�
     */
    public static Map<Integer, Integer> getHuodaolist() {
		return huodaolist;
	}
    
    /**
     * ���ɳ���
     */
    public static int columnChuhuo(Integer logic)
    {
    	int val=0;
    	if(huodaolist!=null)
    	{
    		ToolClass.Log(ToolClass.INFO,"EV_COM","[APPcolumn>>]huodaolist="+huodaolist,"com.txt");
    		if(huodaolist.containsKey(logic))
    		{
    			//����keyȡ������
    		    val=huodaolist.get(logic); 
    		    ToolClass.Log(ToolClass.INFO,"EV_COM","[APPcolumn>>]logic="+logic+"physic="+val,"com.txt");
    		}
    	}
    	return val;
    }
    /**
     * ���ɷ��س������
     *   GOODS_SOLD_ERR          (1 << 0)   //bit0�ܹ���λ
	* 	 GOODS_SOLDOUT_BIT       (1 << 1)   //bit1�������
	* 	 MOTO_MISPLACE_BIT       (1 << 2)   //bit2�����ת֮ǰ�Ͳ�����ȷ��λ����(Ҳ��������)
	* 	 MOTO_NOTMOVE_BIT        (1 << 3)   //bit3�������ת(Ҳ��������)
	* 	 MOTO_NOTRETURN_BIT      (1 << 4)   //bit4���ûת����ȷλ��(Ҳ��������)
	* 	 GOODS_NOTPASS_BIT       (1 << 5)   //bit5��Ʒû��(����ȷ��û��⵽)
	* 	 DRV_CMDERR_BIT          (1 << 6)   //bit6�������(ֻ�з�������Ͳ�ѯ������������������������������ͱ���)
	* 	 DRV_GOCERR_BIT          (1 << 7)   //bit7�������ģ��״̬(GOC����)
	 * 1:�ɹ�;0:����;2:����������;3:���δ��λ;4:����ʧ�� 5:ͨ�Ź���
     */
    public static int colChuhuorst(int Rst)
    {
    	int GOODS_SOLD_ERR=(1 << 0);   //bit0�ܹ���λ
    	int GOODS_SOLDOUT_BIT=(1 << 1);   //bit1�������
    	int MOTO_MISPLACE_BIT=(1 << 2);  //bit2�����ת֮ǰ�Ͳ�����ȷ��λ����(Ҳ��������)
    	int MOTO_NOTMOVE_BIT=(1 << 3); //bit3�������ת(Ҳ��������)
    	int MOTO_NOTRETURN_BIT=(1 << 4);   //bit4���ûת����ȷλ��(Ҳ��������)
    	int GOODS_NOTPASS_BIT=(1 << 5);   //bit5��Ʒû��(����ȷ��û��⵽)
    	int DRV_CMDERR_BIT   =(1 << 6);   //bit6�������(ֻ�з�������Ͳ�ѯ������������������������������ͱ���)
    	int DRV_GOCERR_BIT   =(1 << 7);   //bit7�������ģ��״̬(GOC����)
    	if(Rst == 0x00)
    	{
    		ToolClass.Log(ToolClass.INFO,"EV_COM","[APPcolumn>>]rst=OutGoods_OK","com.txt");
    		return 1;	
    	}
    	else if(Rst == 0xff)
    	{
    		ToolClass.Log(ToolClass.INFO,"EV_COM","[APPcolumn>>]rst=OutGoods_COMERR","com.txt");
    		return 5;
    	}	
    	//1.�ж�GOC�Ƿ����bit7:  GOC����->��Ǯ
    	else if((Rst&DRV_GOCERR_BIT)>0)
    	{
    		if((Rst&GOODS_SOLD_ERR)>0)
    		{
    			//2.���жϵ����
    			//ûת��λ������״��bit1,bit2,bit3,bit4->�����ù���  
    			if( (Rst & (GOODS_SOLDOUT_BIT|MOTO_MISPLACE_BIT|MOTO_NOTMOVE_BIT|MOTO_NOTRETURN_BIT))>0) 
    			{
    				ToolClass.Log(ToolClass.INFO,"EV_COM","[APPcolumn>>]rst=OutGoods_3","com.txt");
    				return 3;								
    			}
    			else
    			{
    				ToolClass.Log(ToolClass.INFO,"EV_COM","[APPcolumn>>]rst=OutGoods_4","com.txt");
    				return 4;	
    			}
    		}
    		else
    		{
    			ToolClass.Log(ToolClass.INFO,"EV_COM","[APPcolumn>>]rst=OutGoods_1","com.txt");
    			return 1;
    		}
    	}
    	else if((Rst&GOODS_SOLD_ERR)>0)
    	{
    		/*
    		//���δת��
    		if((GocType==1)&&(Rst&0x08))
    			return 1;
    		//����ȷ�Ͽ���������ȷ��δ��⵽��Ʒ����
    		if((GocType==1)&&(Rst&0x20))
    			return 4;	
    		else
    		//����ȷ�Ͽ��������δת��λ��������ȷ�ϼ�⵽��Ʒ����
    		if((GocType==1)&&(Rst==0x11))
    			return 0;
    		else
    		//����ȷ��δ���������δת��λ
    		if((GocType==0)&&(Rst&0x10))
    			return 3;
    		else
    		//����ȷ��δ�������������ת��
    		if((GocType==0)&&(Rst&0x08))
    			return 1;
    		*/
    		/*********GOC�򿪵������********************/
    		if(ToolClass.getGoc()==1)
    		{			
    			//1.���δת��,����Ǯ
    			if((Rst&MOTO_NOTMOVE_BIT)>0)
    			{
    				ToolClass.Log(ToolClass.INFO,"EV_COM","[APPcolumn>>]rst=OutGoods_0","com.txt");
    				return 0;
    			}			
    			//2.���ж�GOC�Ƿ��⵽bit5,  ==0˵������ȷ�ϼ�⵽��Ʒ���� 			
    			else if((Rst & GOODS_NOTPASS_BIT) == 0) 
    			{				
    				//�м�⵽->�����ɹ���Ǯ  
    				/*************************************************************************/
    				ToolClass.Log(ToolClass.INFO,"EV_COM","[APPcolumn>>]rst=OutGoods_1","com.txt");
    				return 1;		
    			}
    			else
    			{ 
    				   //û��⵽->����ʧ�ܲ���Ǯ
    				   //3.���жϵ����
    				   //ûת��λ������״��bit1,bit2,bit3,bit4->�����ù���	
    				   if( (Rst & (GOODS_SOLDOUT_BIT|MOTO_MISPLACE_BIT|MOTO_NOTMOVE_BIT|MOTO_NOTRETURN_BIT))>0) 
    				   {
    						ToolClass.Log(ToolClass.INFO,"EV_COM","[APPcolumn>>]rst=OutGoods_3","com.txt");
    						return 3;
    				   }
    				   else
    				   {
    						ToolClass.Log(ToolClass.INFO,"EV_COM","[APPcolumn>>]rst=OutGoods_4","com.txt");
    						return 4;
    				   }				   
    			}
    			
    		}	
    		/*********GOC�رյ������********************/
    		else
    		{
    			//2.���жϵ����
    		   //ûת��λ������״��bit1,bit2,bit3,bit4->�����ù���	
    		   if( (Rst & (GOODS_SOLDOUT_BIT|MOTO_MISPLACE_BIT|MOTO_NOTMOVE_BIT|MOTO_NOTRETURN_BIT))>0) 
    		   {
    				ToolClass.Log(ToolClass.INFO,"EV_COM","[APPcolumn>>]rst=OutGoods_3","com.txt");
    				return 3;								
    		   }
    		   else
    		   {
    				ToolClass.Log(ToolClass.INFO,"EV_COM","[APPcolumn>>]rst=OutGoods_4","com.txt");
    				return 4;	
    		   }
    		}
    	}
    	return 0;
    }
    
    /**
     * д����������ļ�
     */
    public static void WriteColumnFile(String str) 
    {
    	File fileName=null;
    	String  sDir =null;
    	
    	    	
        try {
        	  sDir = ToolClass.getEV_DIR()+File.separator+"evColumnconfig.txt";
        	 
        	  fileName=new File(sDir);
        	  //��������ڣ��򴴽��ļ�
          	  if(!fileName.exists())
          	  {  
      	        fileName.createNewFile(); 
      	      }  
  	         
  	          //��һ��д�ļ��������캯���еĵڶ�������true��ʾ��׷����ʽд�ļ�
  	          FileWriter writer = new FileWriter(fileName, false);
  	          writer.write(str);
  	          writer.close();	
  	          
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
    	SimpleDateFormat tempDate = new SimpleDateFormat("yyyyMMddHHmmssSSS"); //��ȷ������ 
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
		float amount1=0,amount2=0;
		amount1=(float)(Money/100);
		amount2=(float)(Money%100);
		amount2=amount2/100;
		float amount = amount1+amount2;
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
    		id=tb_inaccount.getDevID().toString();
    	}
    	Log.i("EV_JNI","Send0.0="+id);
    	SimpleDateFormat tempDate = new SimpleDateFormat("yyyyMMddHHmmssSSS"); //��ȷ������ 
        String datetime = tempDate.format(new java.util.Date()).toString(); 					
        out_trade_no=id+datetime;
        return out_trade_no;
 	}
 	
 	//vmc_no�������õ��豸id,ǩ����id��
 	public static Map<String, String> getvmc_no(Context context)
 	{
 		Map<String,String> allSet = new HashMap<String,String>() ;
 	    vmc_system_parameterDAO parameterDAO = new vmc_system_parameterDAO(context);// ����InaccountDAO����
	    // �õ��豸ID��
    	Tb_vmc_system_parameter tb_inaccount = parameterDAO.find();
    	if(tb_inaccount!=null)
    	{
    		allSet.put("vmc_no",tb_inaccount.getDevID().toString());
    		allSet.put("vmc_auth_code",tb_inaccount.getDevhCode().toString());
    		ToolClass.setVmc_no(tb_inaccount.getDevID().toString());
    	}
    	return allSet;
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
	
	/**
	 * ��ʽ��ʱ��
	 * @Title:getLastDayOfMonth
	 * @Description:
	 * @param:@param year
	 * @param:@param month
	 * @param:@param type=0�³�,1����Ѯ,2����Ѯ
	 * @param:@return
	 * @return:String
	 * @throws
	 */
	public static String getDayOfMonth(int year,int month,int day)
	{
		Calendar cal = Calendar.getInstance();
		//�������
		cal.set(Calendar.YEAR,year);
		//�����·�
		cal.set(Calendar.MONTH, month-1);
		//������
		cal.set(Calendar.DATE, day);
		
		//��ʽ������
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String lastDayOfMonth = sdf.format(cal.getTime());
		
		return lastDayOfMonth;
	}
	/**
	 * ��ȡĳ�µ����һ�������Ѯ
	 * @Title:getLastDayOfMonth
	 * @Description:
	 * @param:@param year
	 * @param:@param month
	 * @param:@param type=0�³�,1����Ѯ,2����Ѯ
	 * @param:@return
	 * @return:String
	 * @throws
	 */
	public static String getLastDayOfMonth(int year,int month,int type)
	{
		Calendar cal = Calendar.getInstance();
		//�������
		cal.set(Calendar.YEAR,year);
		//�����·�
		cal.set(Calendar.MONTH, month-1);
		if(type==0)
		{
			//�����������·ݵ���Ѯ
			cal.set(Calendar.DAY_OF_MONTH, 1);
		}
		else if(type==1)
		{
			//�����������·ݵ���Ѯ
			cal.set(Calendar.DAY_OF_MONTH, 15);
		}
		else if(type==2)
		{
			//��ȡĳ���������
			int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			//�����������·ݵ��������
			cal.set(Calendar.DAY_OF_MONTH, lastDay);
		}
		//��ʽ������
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String lastDayOfMonth = sdf.format(cal.getTime());
		
		return lastDayOfMonth;
	}
	//�Ƿ���ʱ��������,s����Ҫ�Ƚϵ�ʱ��,begin,end��ʱ������
	//�Ƿ���true
	public static boolean isdatein(String begin,String end,String s)
	{
		//ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<"+s+"��"+begin+"="+dateCompare(s,begin));
		//ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<"+s+"��"+end+"="+dateCompare(end,s));
		if((dateCompare(s,begin)>=0)&&(dateCompare(end,s)>=0))
		{
			return true;
		}
		return false;
	}
	//ʱ��Ƚ�,����ֵresult==0s1���s2,result<0s1С��s2,result:>0s1����s2,
	public static int dateCompare(String s1,String s2)
	{
		//ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<"+s1);
		//ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<"+s2);
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Calendar c1=java.util.Calendar.getInstance();
		java.util.Calendar c2=java.util.Calendar.getInstance();
		try
		{
			c1.setTime(df.parse(s1));
			c2.setTime(df.parse(s2));
		}catch(java.text.ParseException e){
			System.err.println("��ʽ����ȷ");
		}
		return c1.compareTo(c2);
	}
	
	//�õ�hopper�豸�ĵ�ǰ״̬
	public static String gethopperstats(int hopper)
	{
		String res=null;
		//"hopper":8��hopper��״̬,0����,1ȱ��,2����,3ͨѶ����
		if(hopper==0)
		{
			res="����";
		}
		else if(hopper==1)
		{
			res="ȱ��";
		}
		else if(hopper==2)
		{
			res="����";
		}
		else if(hopper==3)
		{
			res="ͨѶ����";
		}
		return res;
	}
	
	//Ϊ�ϴ���serverʹ�ã�����ǰʱ��ת��Ϊ��server�ϱ���ʱ��
	public static String getLasttime()
	{
		SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd"); //��ȷ������ 
		SimpleDateFormat tempTime = new SimpleDateFormat("HH:mm:ss"); //��ȷ������ 
        String datetime = tempDate.format(new java.util.Date()).toString()+"T"
        		+tempTime.format(new java.util.Date()).toString(); 
		return datetime;
	}
	
	//Ϊ�ϴ���serverʹ�ã�����һ��ʱ�䣬ת��Ϊ��server�ϱ���ʱ��
	public static String getStrtime(String orderTime)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date =null;
		try {
			date = df.parse(orderTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd"); //��ȷ������ 
		SimpleDateFormat tempTime = new SimpleDateFormat("HH:mm:ss"); //��ȷ������ 
        String datetime = tempDate.format(date).toString()+"T"
        		+tempTime.format(date).toString(); 
		return datetime;
	}
	
	//��ȡ�豸״̬���ϴ���������
	//dev�豸��1ֽ����,2Ӳ����,3hopper1,4hopper2,5hopper3,6hopper4,7hopper5,8hopper6,9hopper7,10hopper8
	//���أ�2���ϣ�0����
	public static int getvmcStatus(Map<String, Object> Set,int dev)
	{
		//ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<�ֽ��豸״̬="+rst,"log.txt");	
		int rst=0;
		switch (dev) 
		{
			//ֽ����
			case 1:
				int bill_err=(Integer)Set.get("bill_err");
				int bill_enable=(Integer)Set.get("bill_enable");
				if(bill_err>0)
				{
					rst=2;
				}
				else
				{
					rst=0;
				}
				break;
			//Ӳ����	
			case 2:
				int coin_err=(Integer)Set.get("coin_err");
				int coin_enable=(Integer)Set.get("coin_enable");
				if(coin_err>0)
				{
					rst=2;
				}
				else
				{
					rst=0;
				}
				break;
			//hopper1	
			case 3:
				int hopper1=(Integer)Set.get("hopper1");
				if((hopper1==3)||(hopper1==2))
				{
					rst=2;
				}
				else if(hopper1==1)
				{
					rst=1;
				}
				else
				{
					rst=0;
				}
				break;
			//hopper2	
			case 4:
				int hopper2=(Integer)Set.get("hopper2");
				if((hopper2==3)||(hopper2==2))
				{
					rst=2;
				}
				else if(hopper2==1)
				{
					rst=1;
				}
				else
				{
					rst=0;
				}
				break;
			//hopper3	
			case 5:
				int hopper3=(Integer)Set.get("hopper3");
				if((hopper3==3)||(hopper3==2))
				{
					rst=2;
				}
				else if(hopper3==1)
				{
					rst=1;
				}
				else
				{
					rst=0;
				}
				break;
			//hopper4		
			case 6:
				int hopper4=(Integer)Set.get("hopper4");
				if((hopper4==3)||(hopper4==2))
				{
					rst=2;
				}
				else if(hopper4==1)
				{
					rst=1;
				}
				else
				{
					rst=0;
				}
				break;
			//hopper5	
			case 7:
				int hopper5=(Integer)Set.get("hopper5");
				if((hopper5==3)||(hopper5==2))
				{
					rst=2;
				}
				else if(hopper5==1)
				{
					rst=1;
				}
				else
				{
					rst=0;
				}
				break;
			//hopper6
			case 8:
				int hopper6=(Integer)Set.get("hopper6");
				if((hopper6==3)||(hopper6==2))
				{
					rst=2;
				}
				else if(hopper6==1)
				{
					rst=1;
				}
				else
				{
					rst=0;
				}
				break;
			//hopper7
			case 9:
				int hopper7=(Integer)Set.get("hopper7");
				if((hopper7==3)||(hopper7==2))
				{
					rst=2;
				}
				else if(hopper7==1)
				{
					rst=1;
				}
				else
				{
					rst=0;
				}
				break;
			//hopper8
			case 10:
				int hopper8=(Integer)Set.get("hopper8");
				if((hopper8==3)||(hopper8==2))
				{
					rst=2;
				}
				else if(hopper8==1)
				{
					rst=1;
				}
				else
				{
					rst=0;
				}
				break;	
			default:
				break;
		}		
		return rst;
	}
	
	public static int getBill_err() {
		return bill_err;
	}

	public static void setBill_err(int bill_err) {
		ToolClass.bill_err = bill_err;
	}

	public static int getCoin_err() {
		return coin_err;
	}

	public static void setCoin_err(int coin_err) {
		ToolClass.coin_err = coin_err;
	}
	
	//�����ȶ�
	private static boolean passcmp(String pwd,String value)
    {
    	boolean istrue=false;
    	if((pwd==null)||(pwd.equals("")==true))
    	{
    		if(value.equals("83718557"))
    		{
    			istrue=true;
    		}
    	}
    	else
    	{
    		if(value.equals(pwd))
    		{
    			istrue=true;
    		}
    	}
    	return istrue;
    }
	//��ȡ�Ƿ�������ȷ
	//���أ�true��ȷ,false����
	public static boolean getpwdStatus(Context context,String passwd)
	{
		boolean istrue=false;
		//����ά��ҳ������
		vmc_system_parameterDAO parameterDAO = new vmc_system_parameterDAO(context);// ����InaccountDAO����
	    // ��ȡ����������Ϣ�����洢��List���ͼ�����
    	Tb_vmc_system_parameter tb_inaccount = parameterDAO.find();
    	if(tb_inaccount!=null)
    	{
    		String Pwd=tb_inaccount.getMainPwd().toString();
    		if(Pwd==null)
    		{
    			//ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ֵ=null","log.txt");
    			istrue=passcmp(null,passwd);
    		}
    		else
    		{
    			//ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<��ֵ="+Pwd,"log.txt");
    			istrue=passcmp(Pwd,passwd);
    		}
    	}
    	else
    	{
    		istrue=passcmp(null,passwd);
		}
		return istrue;
	}
	
	//�����ȶ�
	//���أ�true��ȷ,false����
	public static boolean getzhitihuo(Context context,String cabid,String columnID,String pwd)
	{
		boolean istrue=false;
		if((pwd==null)||(pwd.equals("")==true))
		{
			istrue=false;
		}
		else if((cabid==null)||(cabid.equals("")==true))
		{
			istrue=false;
		}	
		else if((columnID==null)||(columnID.equals("")==true))
		{
			istrue=false;
		}
		//���������������
		else
		{
			// ����InaccountDAO����
			vmc_columnDAO columnDAO = new vmc_columnDAO(context);
			Tb_vmc_column tb_vmc_column = columnDAO.find(cabid,columnID);// �����Ʒ��Ϣ
			if(tb_vmc_column!=null)
			{
				String str=tb_vmc_column.getTihuoPwd();
				if((str==null)||(str.equals("")==true))
				{
					istrue=false;
				}
				else
				{
					if(pwd.equals(str)==true)
					{
						istrue=true;
					}
				}
			}	
		}
		return istrue;
	}
	//�Ƿ����ʹ����������
	//���أ�true��ȷ,false����
	public static boolean getzhitihuotype(Context context,String cabid,String columnID)
	{
		boolean istrue=false;
		if((cabid==null)||(cabid.equals("")==true))
		{
			istrue=false;
		}	
		else if((columnID==null)||(columnID.equals("")==true))
		{
			istrue=false;
		}
		//���������������
		else
		{
			// ����InaccountDAO����
			vmc_columnDAO columnDAO = new vmc_columnDAO(context);
			Tb_vmc_column tb_vmc_column = columnDAO.find(cabid,columnID);// �����Ʒ��Ϣ
			if(tb_vmc_column!=null)
			{
				String str=tb_vmc_column.getTihuoPwd();
				if((str==null)||(str.equals("")==true))
				{
					istrue=false;
				}
				else
				{
					istrue=true;
				}
			}	
		}
		return istrue;
	}
	/*********************************************************************************************************
	** Function name:     	CrcCheck
	** Descriptions:	    CRCУ���
	** input parameters:    msg��Ҫ���������;len���ݳ���
	** output parameters:   ��
	** Returned value:      CRC������
	*********************************************************************************************************/
	public static int crcCheck(byte msg[], int len){
        int i, j, crc = 0, current;
        for(i = 0;i < len;i++)
        {
            current = ((msg[i] & 0x000000FF) << 8);
            for (j = 0;j < 8;j++)
            {
                if((short)(crc ^ current) < 0)
                {
                    crc =  ((crc << 1) ^ 0x1021) & 0x0000FFFF;
                }
                else
                {
                    crc = (crc << 1) & 0x0000FFFF;
                }
                current = (current << 1) & 0x0000FFFF;
            }
        }

        return crc;
    }

	/*********************************************************************************************************
	** Function name:     	printHex
	** Descriptions:	    ��ӡʮ��������Ϣ
	** input parameters:    bentrecv��Ҫ��ӡ������;bentindex���ݳ���
	** output parameters:   ��
	** Returned value:      ��ӡ��ʮ��������Ϣ
	*********************************************************************************************************/
	public static StringBuilder printHex(byte[] bentrecv,int bentindex)
	{
		StringBuilder res=new StringBuilder();
		for(int i=0;i<bentindex;i++)
	    {
	    	String hex = Integer.toHexString(bentrecv[i] & 0xFF);
            if (hex.length() == 1)
            {
                hex = '0' + hex;
            }	
	    	res.append("[").append(hex.toUpperCase()).append("]");				    	
	    }
		return res;
	}
	
	//��ȡ��portid
	public static int Resetportid(String bentcom)
	{
		int bentcom_id=0;
		//2.�������
		try {
			JSONObject jsonObject = new JSONObject(bentcom); 
			//����keyȡ������
			JSONObject ev_head = (JSONObject) jsonObject.getJSONObject("EV_json");
			int str_evType =  ev_head.getInt("EV_type");
			if(str_evType==EVprotocol.EV_REGISTER)
			{
				bentcom_id=ev_head.getInt("port_id");				
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return bentcom_id;
	}
	
	//type=1���ֽ�,2�Ǹ��ӹ�3�ǵ��ɹ�
	public static void ResstartPort(int type)
	{
		if(type==1)//�ֽ�
		{
			//���ֽ��豸����
			if(ToolClass.getCom().equals("")==false) 
			{
				ToolClass.Log(ToolClass.INFO,"EV_COM","comSend="+ToolClass.getCom(),"com.txt");
				EVprotocol.EVPortRelease(ToolClass.getCom());
				String com = EVprotocol.EVPortRegister(ToolClass.getCom());
				ToolClass.Log(ToolClass.INFO,"EV_COM","com="+com,"com.txt");
				ToolClass.setCom_id(ToolClass.Resetportid(com));
			}			
		}
		else if(type==2)
		{
			//�򿪸��ӹ񴮿�
			if(ToolClass.getBentcom().equals("")==false)
			{
				ToolClass.Log(ToolClass.INFO,"EV_COM","bentcomSend="+ToolClass.getBentcom(),"com.txt");
				EVprotocol.EVPortRelease(ToolClass.getBentcom());
				String bentcom = EVprotocol.EVPortRegister(ToolClass.getBentcom());
				ToolClass.Log(ToolClass.INFO,"EV_COM","bentcom="+bentcom,"com.txt");
				ToolClass.setBentcom_id(ToolClass.Resetportid(bentcom));
			}
		}
		else if(type==3)
		{
			//�򿪵��ɹ񴮿�
			if(ToolClass.getColumncom().equals("")==false)
			{
				ToolClass.Log(ToolClass.INFO,"EV_COM","columncomSend="+ToolClass.getColumncom(),"com.txt");
				EVprotocol.EVPortRelease(ToolClass.getColumncom());
				String columncom = EVprotocol.EVPortRegister(ToolClass.getColumncom());
				ToolClass.Log(ToolClass.INFO,"EV_COM","columncom="+columncom,"com.txt");
				ToolClass.setColumncom_id(ToolClass.Resetportid(columncom));
			}
		}
	}
	
}
