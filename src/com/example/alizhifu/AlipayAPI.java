package com.example.alizhifu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.easivend.alipay.AlipayConfig;
import com.easivend.common.ToolClass;

public class AlipayAPI 
{
	static String appId ="2015072300183987";//�������˺�appid,Ψһ
	//˽Կ��ÿ���̻��Ŷ���ӦΨһ�ģ��ڱ���������������RSAǩ������
	//static String privateKey ="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANHnYMLymQkH3wa2TgtMaDy9FwhqTEdfwsXWbv4MmoF0ag8X/ZffwaQFfcORWfzOXSMC9epJFvXFY4MPx4HaX1dU6nsM0WBYHmBFxCwSwOmrOVHAq9VdSNKdE3W4EoeCFWfD8vC4vjGTTUpowHeQWMx0NIEDBKHRPOQbsKIwAL7PAgMBAAECgYAazfJEUtiKF7A6WjNzK+mvv/HeCDz/bFIiE3UPCir81xHoJYcjytYejPj3bWtRZkTsgKdIqNa+wdsoVG6EvY8pC3CpMlliWfRuYBTOyMCS/RVGTkftM6vuS07bW4Je1qvoO26pCk4kgRl0Z3GoYw9AOi5cl1q0fZDvsncDG8Dh0QJBAPs2cqE3K/+UaDdxWd/di1xi0p4SZJ/4WbSUUCwe1L3o3/jwLKgvCASlPeIVcJamhyfdbmJ4E1QWsfeuPhOUinkCQQDV52cvWs1vDyTKxvjQzwEFkajHzctLpCpBoO1funxwVkmYU/cmRZn292mFGywHA4Gq9vgF+S7jwzOYvwQw20GHAkByVcGuZnH8DQux0EFbhnXbQo8hqrVpqZsKeUZUDmQ9WzQ1FPr+QQmhM6QKtj9cEccJ+do3rvb9Gqc9V2yhdMXhAkEAscu4PupQ28FQqaQdaSLHDKP4EKwEEQmRfh+PbwSJLq7qWU1hn1Q3F8qq0NK3E9VcUIkbu4tV6Ed2eb48c4ervQJAHWO0ocjLrtgIHW41b2u4mEcD3bjeeOvdfGCIDXHPkchHw5L+fbk2JkLv3DEY468eEUyH7xFHCXjFHYYWQICYWw==";
	//��Կ��ÿ���̻��Ŷ���ӦΨһ�ģ��ֱ�ע�ᵽ֧��������ƽ̨����ڱ��������治ʹ��
	String public_key ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDR52DC8pkJB98Gtk4LTGg8vRcIakxHX8LF1m7+DJqBdGoPF/2X38GkBX3DkVn8zl0jAvXqSRb1xWODD8eB2l9XVOp7DNFgWB5gRcQsEsDpqzlRwKvVXUjSnRN1uBKHghVnw/LwuL4xk01KaMB3kFjMdDSBAwSh0TzkG7CiMAC+zwIDAQAB";
	//���Ͷ�ά��
	public static String PostAliBuy(Map<String, String> list)
	{
		String rsp=null;
		
		ToolClass.CheckAliWeiFile();
		//��֧�������������ͽ�����Ϣ
		// (����) �̻���վ����ϵͳ��Ψһ�����ţ�64���ַ����ڣ�ֻ�ܰ�����ĸ�����֡��»��ߣ�
        // �豣֤�̻�ϵͳ�˲����ظ�������ͨ�����ݿ�sequence���ɣ�
        String outTradeNo = list.get("out_trade_no");

        // (����) �������⣬���������û���֧��Ŀ�ġ��硰xxxƷ��xxx�ŵ굱�渶ɨ�����ѡ�
        String subject =  list.get("subject");

        // (����) �����ܽ���λΪԪ�����ܳ���1��Ԫ
        // ���ͬʱ�����ˡ����۽�,�����ɴ��۽�,�������ܽ�����,�����������������:�������ܽ�=�����۽�+�����ɴ��۽�
        String totalAmount = list.get("total_fee");
        
        JSONObject bizContent=new JSONObject();
        try {
			bizContent.put("out_trade_no", outTradeNo);
			bizContent.put("total_amount", totalAmount);
			bizContent.put("subject", subject);
			
			//��ӷ����˺�
			if(AlipayConfig.getIsalisub()>0)
	        {
	        	JSONObject royalty_info=new JSONObject();
	        	royalty_info.put("royalty_type", "ROYALTY");
	        	
	        	JSONArray royalty_detail_infos=new JSONArray();
	        	JSONObject royalty_detail=new JSONObject();
	        	royalty_detail.put("serial_no", 1);
	        	royalty_detail.put("trans_in_type", "userId");
	        	royalty_detail.put("batch_no", 123);
	        	royalty_detail.put("trans_out_type", "userId");
	        	royalty_detail.put("trans_out", AlipayConfig.getPartner());//���˺�id��
	        	royalty_detail.put("trans_in", "2088101126708402");//���˺�id��
	        	royalty_detail.put("amount", totalAmount);
	        	royalty_detail.put("desc", "���˲���1");
	        	royalty_detail.put("amount_percentage", "100");
	        	royalty_detail_infos.put(royalty_detail);
	        	royalty_info.put("royalty_detail_infos", royalty_detail_infos);
	        	bizContent.put("royalty_info", royalty_info);
	        }
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        Log.i("EV_JNI","trade.precreate bizContent:" + bizContent);
        
        Map<String,String> request = new HashMap<String,String>();
        request.put("biz_content", bizContent.toString());
        request.put("method", "alipay.trade.precreate");//Ԥ�µ�

        try {
        	rsp=doPost(request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return rsp;
	}
	
	//���ɲ�ѯ������Ϣ
    public static String PostAliQuery(Map<String, String> list) 
    {
    	String rsp=null;
    	
    	String outTradeNo = list.get("out_trade_no");
    	
    	JSONObject bizContent=new JSONObject();
        try {
			bizContent.put("out_trade_no", outTradeNo);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        Log.i("EV_JNI","trade.query bizContent:" + bizContent);
        
        Map<String,String> request = new HashMap<String,String>();
        request.put("biz_content", bizContent.toString());
        request.put("method", "alipay.trade.query");//��ѯ
        
        try {
        	rsp=doPost(request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return rsp;
    }
    
    //����������Ϣ
    public static String PostAliDelete(Map<String, String> list) 
    {
    	String rsp=null;
    	
    	String outTradeNo = list.get("out_trade_no");
    	
    	JSONObject bizContent=new JSONObject();
        try {
			bizContent.put("out_trade_no", outTradeNo);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        Log.i("EV_JNI","trade.cancel bizContent:" + bizContent);
        
        Map<String,String> request = new HashMap<String,String>();
        request.put("biz_content", bizContent.toString());
        request.put("method", "alipay.trade.cancel");//����
        
        try {
        	rsp=doPost(request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return rsp;
    }
    
    //�����˿���Ϣ
    public static String PostAliPayout(Map<String, String> list) 
    {
    	String rsp=null;
    	
    	String outTradeNo = list.get("out_trade_no");
    	
    	String totalAmount = list.get("refund_amount");
    	
    	JSONObject bizContent=new JSONObject();
        try {
			bizContent.put("out_trade_no", outTradeNo);
			bizContent.put("refund_amount", totalAmount);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        Log.i("EV_JNI","trade.refund bizContent:" + bizContent);
        
        Map<String,String> request = new HashMap<String,String>();
        request.put("biz_content", bizContent.toString());
        request.put("method", "alipay.trade.refund");//��ѯ
        
        try {
        	rsp=doPost(request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return rsp;
    }
	
	//���
    private static String doPost(Map<String,String> request)
            throws Exception
	{
		
		RequestParametersHolder requestHolder = new RequestParametersHolder();
		Map<String,String> appParams = new HashMap<String,String>();
		appParams.put("biz_content",request.get("biz_content"));
		requestHolder.setApplicationParams(appParams);
		
		
		
		Map<String,String> protocalMustParams = new HashMap<String,String>();
		protocalMustParams.put("method", request.get("method"));//Ԥ�µ�
		protocalMustParams.put("version", "1.0");
		protocalMustParams.put("app_id", appId);
		protocalMustParams.put("sign_type", "RSA");
		protocalMustParams.put("charset", "UTF-8");
		
		Long timestamp = System.currentTimeMillis();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		df.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		protocalMustParams.put("timestamp", df.format(new Date(timestamp)));
		requestHolder.setProtocalMustParams(protocalMustParams);
		
		Map<String,String> protocalOptParams = new HashMap<String,String>();
		protocalOptParams.put("format", "json");
		protocalOptParams.put("alipay_sdk", "alipay-sdk-java-dynamicVersionNo");
		requestHolder.setProtocalOptParams(protocalOptParams);
		
		
		//sign
		String signContent = AlipaySignature.getSignatureContent(requestHolder);
		//�õ�RSA��sign
		protocalMustParams.put("sign",
		AlipaySignature.rsaSign(signContent, AlipayConfig.getAliprivateKey(), "UTF-8"));
		
		
		StringBuffer urlSb = new StringBuffer("https://openapi.alipay.com/gateway.do");
		try {
			String sysMustQuery = buildQuery(requestHolder.getProtocalMustParams(),
				"UTF-8");
			String sysOptQuery = buildQuery(requestHolder.getProtocalOptParams(), "UTF-8");
			
			urlSb.append("?");
			urlSb.append(sysMustQuery);
			if (sysOptQuery != null & sysOptQuery.length() > 0) {
			urlSb.append("&");
			urlSb.append(sysOptQuery);
			}
		} 
		catch (IOException e) {
			throw new Exception(e);
		}
		Log.i("EV_JNI","Send1=urlandSign="+urlSb);
		Log.i("EV_JNI","Send1=content="+appParams);
		
		
		String rsp = null;
		try {			
			rsp = doPost(urlSb.toString(), appParams, "UTF-8", 9000,
					3000);			
		} catch (IOException e) {
			throw new Exception(e);
		}
		return rsp;		
	}
    
    static String buildQuery(Map<String, String> params, String charset) throws IOException {
        if (params == null || params.isEmpty()) {
            return null;
        }

        StringBuilder query = new StringBuilder();
        Set<Entry<String, String>> entries = params.entrySet();
        boolean hasParam = false;

        for (Entry<String, String> entry : entries) {
            String name = entry.getKey();
            String value = entry.getValue();
            // 忽略参数名或参数值为空的参数
            if (StringUtils.areNotEmpty(name, value)) {
                if (hasParam) {
                    query.append("&");
                } else {
                    hasParam = true;
                }

                query.append(name).append("=").append(URLEncoder.encode(value, charset));
            }
        }

        return query.toString();
    }
    
    //AES����
    static String doPost(String url, Map<String, String> params, String charset,
            int connectTimeout, int readTimeout) throws IOException {
		String ctype = "application/x-www-form-urlencoded;charset=" + charset;
		//AES����
		String query = buildQuery(params, charset);
		Log.i("EV_JNI","send2=AES="+query);
		byte[] content = {};
		//getbytes
		if (query != null) {
			content = query.getBytes(charset);
		} 
		return doPost(url, ctype, content, connectTimeout, readTimeout);
    }
    
    //����
    public static String doPost(String urlstr, String ctype, byte[] contents, int connectTimeout,
            int readTimeout) throws IOException 
    {
		String content=null;    	
		
		Log.i("EV_JNI","send3=str="+urlstr);
		Log.i("EV_JNI","send3=ctype="+ctype);
		Log.i("EV_JNI","Send3=content="+contents);
		//�ڶ��ֿ��еķ���
		//4.������Ϣ	
		byte[] xmlbyte = contents;
		URL url = new URL(urlstr);    
		HttpsURLConnection  conn = (HttpsURLConnection) url.openConnection();
		//if(ToolClass.getSsl()!=null)
		//{
		//conn.setSSLSocketFactory(ToolClass.getSsl());
		//}
		conn.setConnectTimeout(connectTimeout);//��������������ʱ����λ�����룩
		conn.setReadTimeout(readTimeout);//���ô�������ȡ���ݳ�ʱ����λ�����룩
		conn.setDoOutput(true);// �������
		conn.setDoInput(true);// ��������
		conn.setUseCaches(false);// ��ʹ�û���
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Connection", "Keep-Alive");// ά�ֳ�����
		conn.setRequestProperty("Charset", "UTF-8");
		conn.setRequestProperty("Content-Length",String.valueOf(contents.length));
		conn.setRequestProperty("Content-Type", ctype);
		conn.setRequestProperty("X-ClientType", "2");//�����Զ����ͷ��Ϣ
		
		conn.getOutputStream().write(xmlbyte);
		conn.getOutputStream().flush();
		conn.getOutputStream().close();
		
		
		if (conn.getResponseCode() != 200)
		throw new RuntimeException("����urlʧ��");
		
		InputStream in = conn.getInputStream();// ��ȡ��������
		
		BufferedReader bufferedReader = new BufferedReader(  
		new InputStreamReader(in));  
		StringBuffer temp = new StringBuffer();  
		String line = bufferedReader.readLine();  
		while (line != null) {  
		temp.append(line).append("\r\n");  
		line = bufferedReader.readLine();  
		}  
		bufferedReader.close(); 
		content = new String(temp.toString().getBytes(), "UTF-8"); 				            
		Log.i("EV_JNI","rec1="+content);
		
		
		return content;
    }
}
