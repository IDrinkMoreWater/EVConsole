/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           ProPictureAdapter.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        GridView�������࣬������������Ʒ����ҳ���ͼƬ����     
**------------------------------------------------------------------------------------------------------
** Created by:          guozhenzhen 
** Created date:        2015-01-10
** Version:             V1.0 
** Descriptions:        The original version       
********************************************************************************************************/


package com.easivend.common;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.evconsole.R;

public class ProPictureAdapter extends BaseAdapter {// ��������BaseAdapter������

    private LayoutInflater inflater;// ����LayoutInflater����
    private List<ProPicture> pictures;// ����List���ͼ���

    // Ϊ�ഴ�����캯��
    public ProPictureAdapter(String[] proID, String[] promarket,String[] prosales,String[] proImage,String[] procount, Context context) {
        super();
        pictures = new ArrayList<ProPicture>();// ��ʼ�����ͼ��϶���
        inflater = LayoutInflater.from(context);// ��ʼ��LayoutInflater����
        for (int i = 0; i < proImage.length; i++)// ����ͼ������
        {        	
            ProPicture picture = new ProPicture(proID[i],promarket[i],prosales[i], proImage[i],procount[i]);// ʹ�ñ����ͼ������ProPicture����
            pictures.add(picture);// ��Picture������ӵ����ͼ�����
            ToolClass.Log(ToolClass.INFO,"EV_JNI","APP<<proID="+proID[i]+",promarket="+promarket[i]+",prosales="+prosales[i]+",proImage="+proImage[i]+",procount="+procount[i]);
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
        ProViewHolder viewHolder;// ����ProViewHolder����
        if (arg1 == null) {// �ж�ͼ���ʶ�Ƿ�Ϊ��

            arg1 = inflater.inflate(R.layout.productgv, null);// ����ͼ���ʶ
            viewHolder = new ProViewHolder();// ��ʼ��ProViewHolder����
            viewHolder.proID = (TextView) arg1.findViewById(R.id.proID);// ����ͼ�����
            viewHolder.image = (ImageView) arg1.findViewById(R.id.proImage);// ����ͼ��Ķ�����ֵ
            viewHolder.promarket = (TextView) arg1.findViewById(R.id.promarket);// ����ͼ�����
            viewHolder.prosales = (TextView) arg1.findViewById(R.id.prosales);// ����ͼ�����
            viewHolder.count = (TextView) arg1.findViewById(R.id.count);// ����ʣ������
            
            arg1.setTag(viewHolder);// ������ʾ
        } 
        else
        {
            viewHolder = (ProViewHolder) arg1.getTag();// ������ʾ
        }
        if(Integer.parseInt(pictures.get(arg0).getProcount())>0)
        {
        	viewHolder.count.setText("ʣ������:"+pictures.get(arg0).getProcount());// ����ʣ������
        }
        else
        {
        	viewHolder.count.setText("ʣ������:������");// ����ʣ������
        	viewHolder.count.setTextColor(android.graphics.Color.RED);
        }
        viewHolder.proID.setText(pictures.get(arg0).getProID());// ����ͼ��ID
        viewHolder.promarket.setText("ԭ��:"+pictures.get(arg0).getPromarket());// ����ԭ��
        viewHolder.promarket.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //ɾ����
        viewHolder.prosales.setText("�ּ�:"+pictures.get(arg0).getProsales());// �����ּ�
        /*ΪʲôͼƬһ��Ҫת��Ϊ Bitmap��ʽ�ģ��� */
        Bitmap bitmap = ToolClass.getLoacalBitmap(pictures.get(arg0).getProImage()); //�ӱ���ȡͼƬ(��cdcard�л�ȡ)  //
        viewHolder.image.setImageBitmap(bitmap);// ����ͼ��Ķ�����ֵ
        return arg1;// ����ͼ���ʶ
    }
}

class ProViewHolder {// ����ProViewHolder���ſؼ�����

    public TextView proID;// ������ƷID������
    public ImageView image;// ����ImageView����
    public TextView promarket;// ������Ʒԭ��
    public TextView prosales;// ������Ʒ���ۼ�
    public TextView count;// ������Ʒʣ������
}

class ProPicture {// ����ProPicture��

    private String proID;// �����ַ�������ʾͼ�����
    private String proImage;//ͼ��λ��
    private String promarket;//ԭ��
    private String prosales;//�ּ�
    private String procount;//��Ʒ����
	public ProPicture(String proID, String promarket,String prosales,String proImage,String procount)
	{
		super();
		this.proID = proID;
		this.proImage = proImage;
		this.promarket = promarket;
		this.prosales = prosales;
		this.procount = procount;
	}
	public String getProID() {
		return proID;
	}
	public void setProID(String proID) {
		this.proID = proID;
	}
	public String getProImage() {
		return proImage;
	}
	public void setProImage(String proImage) {
		this.proImage = proImage;
	}
	public String getPromarket() {
		return promarket;
	}
	public void setPromarket(String promarket) {
		this.promarket = promarket;
	}
	public String getProsales() {
		return prosales;
	}
	public void setProsales(String prosales) {
		this.prosales = prosales;
	}
	public String getProcount() {
		return procount;
	}
	public void setProcount(String procount) {
		this.procount = procount;
	}
	
    
}