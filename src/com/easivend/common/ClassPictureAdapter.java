/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           ClassPictureAdapter.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        GridView�������࣬������������Ʒ���ҳ���ͼƬ����     
**------------------------------------------------------------------------------------------------------
** Created by:          guozhenzhen 
** Created date:        2015-01-10
** Version:             V1.0 
** Descriptions:        The original version       
********************************************************************************************************/

package com.easivend.common;

import java.util.ArrayList;
import java.util.List;

import com.example.evconsole.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ClassPictureAdapter extends BaseAdapter 
{// ��������BaseAdapter������

    private LayoutInflater inflater;// ����LayoutInflater����
    private List<ClassPicture> pictures;// ����List���ͼ���
   

    // Ϊ�ഴ�����캯��
    public ClassPictureAdapter(String[] proclassName,String[] proImage, Context context) {
        super();
        pictures = new ArrayList<ClassPicture>();// ��ʼ�����ͼ��϶���
        inflater = LayoutInflater.from(context);// ��ʼ��LayoutInflater����
        for (int i = 0; i < proclassName.length; i++)// ����ͼ������
        {
        	ClassPicture picture = new ClassPicture(proclassName[i],proImage[i]);// ʹ�ñ����ͼ������ProPicture����
            pictures.add(picture);// ��Picture�������ӵ����ͼ�����
            ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<Img="+proclassName[i]+","+proImage[i]);
        }
    }

    @Override
    public int getCount() {// ��ȡ���ͼ��ϵĳ���
        if (null != pictures) {// ������ͼ��ϲ�Ϊ��
            return pictures.size();// ���ط��ͳ���
        } else {
            return 0;// ����0
        }
    }

    @Override
    public Object getItem(int arg0) {
        return pictures.get(arg0);// ��ȡ���ͼ���ָ������������
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;// ���ط��ͼ��ϵ�����
    }
    
    
    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
    	ClassViewHolder viewHolder;// ����ProViewHolder����
        if (arg1 == null) {// �ж�ͼ���ʶ�Ƿ�Ϊ��

            arg1 = inflater.inflate(R.layout.busgoodsclassgv, null);// ����ͼ���ʶ
            viewHolder = new ClassViewHolder();// ��ʼ��ProViewHolder����
            viewHolder.busgoodsclassName = (TextView) arg1.findViewById(R.id.busgoodsclassName);// ����ͼ�����
            viewHolder.busgoodsclassImage = (ImageView) arg1.findViewById(R.id.busgoodsclassImage);// ����ͼ��Ķ�����ֵ
            
            
            arg1.setTag(viewHolder);// ������ʾ
        } 
        else
        {
            viewHolder = (ClassViewHolder) arg1.getTag();// ������ʾ
        }
        
        viewHolder.busgoodsclassName.setText("���:"+pictures.get(arg0).getProclassName());// ����ͼ��ԭ��
        ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<Img2="+pictures.get(arg0).getProImage());
        if(pictures.get(arg0).getProImage()!=null)
        {
	        if(pictures.get(arg0).getProImage().equals("0")!=true)
	        {        	
	        	/*ΪʲôͼƬһ��Ҫת��Ϊ Bitmap��ʽ�ģ��� */
		        Bitmap bitmap = ToolClass.getLoacalBitmap(pictures.get(arg0).getProImage()); //�ӱ���ȡͼƬ(��cdcard�л�ȡ)  //
		        if(bitmap!=null)
		        	viewHolder.busgoodsclassImage.setImageBitmap(bitmap);// ����ͼ��Ķ�����ֵ
	        }
        }
        return arg1;// ����ͼ���ʶ
    }
}

class ClassViewHolder {// ����ProViewHolder���ſؼ�����

    public TextView busgoodsclassName;// �������
    public ImageView busgoodsclassImage;// ����ImageView����
    
}

class ClassPicture {// ����ProPicture��
    
    private String proclassName = null;//�������
	private String proImage = null;//ͼ��λ��
	public ClassPicture(String proclassName, String proImage) {
		super();
		this.proclassName = proclassName;
		this.proImage = proImage;
	}
	public String getProclassName() {
		return proclassName;
	}
	public void setProclassName(String proclassName) {
		this.proclassName = proclassName;
	}
	public String getProImage() {
		return proImage;
	}
	public void setProImage(String proImage) {
		this.proImage = proImage;
	}
	
	
    
}