package com.easivend.alipay;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

import com.easivend.alipay.AlipayConfig;
import com.easivend.evprotocol.ToolClass;

public class AlipayConfigAPI {
	//����֧�����˺�
    public static void SetAliConfig(Map<String, String> list) 
    {
    	String str=null;
    	//alipay
    	str=list.get("partner");    	
    	AlipayConfig.setPartner(str);
    	ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<partner="+str);
    	
    	str=list.get("seller_email");
    	AlipayConfig.setSeller_email(str);
    	ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<seller_email="+str);
    	
    	str=list.get("alikey");
    	AlipayConfig.setKey(str);
    	ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<alikey="+str);
    	//weixing
    	
    }
    
    //����֧��������Ϣ
    public static Map<String, String> PostAliBuy(Map<String, String> list) 
    {
    	//��֧�������������ͽ�����Ϣ
		Map<String, String> sPara = new HashMap<String, String>();
		 sPara.put("service","alipay.acquire.precreate");//�ӿ�����
		 sPara.put("partner",AlipayConfig.getPartner());//֧���� PID��
		 sPara.put("_input_charset",AlipayConfig.getInput_charset());//����		
		 sPara.put("out_trade_no", list.get("out_trade_no"));//�������
		 sPara.put("subject",list.get("subject"));//��������
		 sPara.put("product_code","QR_CODE_OFFLINE");//��ά��
		 sPara.put("total_fee",list.get("total_fee"));//�����ܽ��	
		 sPara.put("seller_email", AlipayConfig.getSeller_email());//����֧�����ʻ�		 
		 sPara.put("goods_detail",list.get("goods_detail"));//��Ʒ��ϸ
		 sPara.put("it_b_pay",list.get("it_b_pay"));//���׹ر�ʱ��	
		 
		 Map<String, String> map1 = AlipaySubmit.buildRequestPara(sPara);
		 Log.i("EV_JNI","Send1="+map1);
    	return map1;
    }
    //���֧������ķ�����Ϣ
    public static Map<String, String> PendAliBuy(InputStream is)
    {
    	Map<String, String>list=new HashMap<String, String>();
    	
    	XmlPullParser parser = Xml.newPullParser();
        try {
            //parser.setInput(new ByteArrayInputStream(string.substring(1)
            //        .getBytes("UTF-8")), "UTF-8");
            parser.setInput(is, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT)
			{
                if (eventType == XmlPullParser.START_TAG) 
				{
                    if ("is_success".equals(parser.getName())) 
					{
                    	list.put("is_success", parser.nextText());
                    } 
					else if ("error".equals(parser.getName())) 
					{
						list.put("error", parser.nextText());
                    } 
					else if ("result_code".equals(parser.getName())) 
					{
						list.put("result_code", parser.nextText());
                    }
					else if ("detail_error_code".equals(parser.getName())) 
					{
						list.put("detail_error_code", parser.nextText());
                    }
					else if ("detail_error_des".equals(parser.getName())) 
					{
						list.put("detail_error_des", parser.nextText());
                    }
					else if ("qr_code".equals(parser.getName())) 
					{
						list.put("qr_code", parser.nextText());
                    }
					else if ("pic_url".equals(parser.getName())) 
					{
						list.put("pic_url", parser.nextText());
                    }
					else if ("small_pic_url".equals(parser.getName())) 
					{
						list.put("small_pic_url", parser.nextText());
                    }
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            System.out.println(e);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return list;
    }
}
