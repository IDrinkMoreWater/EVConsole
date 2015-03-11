/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           HuoPictureAdapter.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        GridView�������࣬���������û�������ҳ���ͼƬ����     
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.evconsole.R;

public class HuoPictureAdapter extends BaseAdapter {// ��������BaseAdapter������

    private LayoutInflater inflater;// ����LayoutInflater����
    private List<HuoPicture> pictures;// ����List���ͼ���
    private String cabinetID=null;

    // Ϊ�ഴ�����캯��
    public HuoPictureAdapter(String cabinetID,String[] huoID, String[] huoproID,String[] huoRemain,String[] huolasttime,String[] proImage, Context context) {
        super();
        this.cabinetID=cabinetID;
        pictures = new ArrayList<HuoPicture>();// ��ʼ�����ͼ��϶���
        inflater = LayoutInflater.from(context);// ��ʼ��LayoutInflater����
        for (int i = 0; i < proImage.length; i++)// ����ͼ������
        {
            HuoPicture picture = new HuoPicture(huoID[i],huoproID[i],huoRemain[i], huolasttime[i],proImage[i]);// ʹ�ñ����ͼ������ProPicture����
            pictures.add(picture);// ��Picture������ӵ����ͼ�����
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
        HuoViewHolder viewHolder;// ����ProViewHolder����
        if (arg1 == null) {// �ж�ͼ���ʶ�Ƿ�Ϊ��

            arg1 = inflater.inflate(R.layout.huodaogv, null);// ����ͼ���ʶ
            viewHolder = new HuoViewHolder();// ��ʼ��ProViewHolder����
            viewHolder.huoID = (TextView) arg1.findViewById(R.id.huoID);// ����ͼ�����
            viewHolder.huoproID = (TextView) arg1.findViewById(R.id.huoproID);// ����ͼ�����
            viewHolder.huoRemain = (TextView) arg1.findViewById(R.id.huoRemain);// ����ͼ�����
            viewHolder.huolasttime = (TextView) arg1.findViewById(R.id.huolasttime);// ����ͼ�����
            viewHolder.huoImage = (ImageView) arg1.findViewById(R.id.huoImage);// ����ͼ��Ķ�����ֵ
            
            
            arg1.setTag(viewHolder);// ������ʾ
        } 
        else
        {
            viewHolder = (HuoViewHolder) arg1.getTag();// ������ʾ
        }
        
        viewHolder.huoID.setText("����:"+cabinetID+pictures.get(arg0).getHuoID());// ����ͼ��ID
        viewHolder.huoproID.setText("��ƷID:"+pictures.get(arg0).getHuoproID());// ����ͼ��ԭ��
        viewHolder.huoRemain.setText("����:"+pictures.get(arg0).getHuoRemain());// ����ͼ��ԭ��
        viewHolder.huolasttime.setText("�ϼ�:"+pictures.get(arg0).getHuolasttime());
        /*ΪʲôͼƬһ��Ҫת��Ϊ Bitmap��ʽ�ģ��� */
        Bitmap bitmap = ToolClass.getLoacalBitmap(pictures.get(arg0).getProImage()); //�ӱ���ȡͼƬ(��cdcard�л�ȡ)  //
        if(bitmap!=null)
        	viewHolder.huoImage.setImageBitmap(bitmap);// ����ͼ��Ķ�����ֵ
        return arg1;// ����ͼ���ʶ
    }
}

class HuoViewHolder {// ����ProViewHolder���ſؼ�����

    public TextView huoID;// ����id
    public TextView huoproID;// ������Ӧ��Ʒid
    public TextView huoRemain;// ʣ��������
    public TextView huolasttime;// �ϼ�ʱ��
    public ImageView huoImage;// ����ImageView����
    
}

class HuoPicture {// ����ProPicture��
    
    private String huoID = null;//����id
	private String huoproID = null;//������Ӧ��Ʒid
    private String huoRemain = null;//ʣ��������
    private String huolasttime = null;//�ϼ�ʱ��
    private String proImage = null;//ͼ��λ��
	public HuoPicture(String huoID, String huoproID, String huoRemain,
			String huolasttime, String proImage) {
		super();
		this.huoID = huoID;
		this.huoproID = huoproID;
		this.huoRemain = huoRemain;
		this.huolasttime = huolasttime;
		this.proImage = proImage;
	}
	public String getHuoID() {
		return huoID;
	}
	public void setHuoID(String huoID) {
		this.huoID = huoID;
	}
	public String getHuoproID() {
		return huoproID;
	}
	public void setHuoproID(String huoproID) {
		this.huoproID = huoproID;
	}
	public String getHuoRemain() {
		return huoRemain;
	}
	public void setHuoRemain(String huoRemain) {
		this.huoRemain = huoRemain;
	}
	public String getHuolasttime() {
		return huolasttime;
	}
	public void setHuolasttime(String huolasttime) {
		this.huolasttime = huolasttime;
	}
	public String getProImage() {
		return proImage;
	}
	public void setProImage(String proImage) {
		this.proImage = proImage;
	}
	
    
}
