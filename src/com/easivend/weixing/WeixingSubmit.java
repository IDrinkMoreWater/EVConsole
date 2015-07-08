package com.easivend.weixing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import android.util.Log;

import com.easivend.weixing.MD5;

public class WeixingSubmit 
{
	/**
     * ����Ҫ�����֧�����Ĳ�������
     * @param sParaTemp ����ǰ�Ĳ�������
     * @return Ҫ����Ĳ�������
     */
    public static String buildRequestPara(Map<String, String> sParaTemp,String key) {
    	//��ȥ�����еĿ�ֵ��ǩ������
    	Map<String, String> paraMap = paraFilter(sParaTemp);
    	Log.i("EV_JNI","Send0.3="+paraMap+"key="+key);
        //����ǩ�����
        String mysign = buildRequestMysign(paraMap,key);   
        return mysign;
    }
	 // <summary>
	 // ��ȥ�����еĿ�ֵ��ǩ������������ĸa��z��˳������
	 // </summary>
	 // <param name="dicArrayPre">����ǰ�Ĳ�����</param>
	 // <returns>���˺�Ĳ�����</returns>
	static Map<String, String> paraFilter(Map<String, String> sArray)
	{
	
			Map<String, String> result = new TreeMap<String, String>();
			
			List<String> alllist=new ArrayList<String>();
	        if (sArray == null || sArray.size() <= 0) {
	            return result;
	        }
	
	        for (String key : sArray.keySet()) {
	            String value = sArray.get(key);
	            if (value == null || value.equals("") || key.equalsIgnoreCase("key")
	                ) {
	                continue;
	            }             
	            alllist.add(key);
	        }
	        
	        String asc[]= alllist.toArray(new String[]{});
	        for(int i=0; i<asc.length; i++)
	        {
	            for(int j=i+1; j<asc.length; j++)
	            {
	
	               if(asc[i].compareTo(asc[j])>0)
	               {
	                   String t = asc[i];
	                   asc[i]=asc[j];
	                   asc[j]=t;
	               }
	            }
	        }
	        for (String key : asc) 
	        {  
	        	//Log.i("EV_JNI",key + " = " + result.get(key));  
	        	result.put(key, sArray.get(key));   
	        }  
	        
	        return result;
	}
	 
	//���MD5У������ǩ�����
	static String buildRequestMysign(Map<String, String> mapArr,String key)
	{
	   String preStr = createLinkString(mapArr);
	   preStr += "&key="+key;
	   //Log.i("EV_JNI","Send3="+preStr);
	    //Ĭ�� MD5У��
	   String mysign=null;
	   mysign=MD5.GetMD5Code(preStr).toUpperCase();
	   //String mysign=md5(preStr.getBytes());
	   Log.i("EV_JNI","Send0.4SIGN="+mysign);
	   return mysign;
	}
	//�Ѳ�����������Ԫ�أ����ա�����=����ֵ����ģʽ�á�&���ַ�ƴ�ӳ��ַ���
	static String createLinkString(Map<String, String> mapArr)
	{
		StringBuilder prestr = new StringBuilder();
		//��������
		Set<Map.Entry<String,String>> allset=mapArr.entrySet();  //ʵ����
	    Iterator<Map.Entry<String,String>> iter=allset.iterator();
	    while(iter.hasNext())
	    {
	        Map.Entry<String,String> me=iter.next();
	        prestr.append(me.getKey() + "=" + me.getValue() + "&");
	    }   
	    //ȥ������һ��&�ַ�
	    String str=prestr.toString();
	    //Log.i("EV_JNI","Send3.1="+str);
	    int len = str.length();
	    if(len > 0)
	    	str=str.substring(0,len-1);
	    //Log.i("EV_JNI","Send3.2="+str);
	    return str;
	}
}
