/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           PictureAdapter.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        GridView�������࣬������������ά��ҳ���ͼƬ����     
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.evconsole.R;


public class PictureAdapter extends BaseAdapter {// ��������BaseAdapter������

    private LayoutInflater inflater;// ����LayoutInflater����
    private List<Picture> pictures;// ����List���ͼ���

    // Ϊ�ഴ�����캯��
    public PictureAdapter(String[] titles, int[] images, Context context) {
        super();
        pictures = new ArrayList<Picture>();// ��ʼ�����ͼ��϶���
        inflater = LayoutInflater.from(context);// ��ʼ��LayoutInflater����
        for (int i = 0; i < images.length; i++)// ����ͼ������
        {
            Picture picture = new Picture(titles[i], images[i]);// ʹ�ñ����ͼ������Picture����
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
    //��Ҫ���������������������ͼƬimage�ͱ���text��
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        ViewHolder viewHolder;// ����ViewHolder����
        if (arg1 == null) {// �ж�ͼ���ʶ�Ƿ�Ϊ��

            arg1 = inflater.inflate(R.layout.givtem, null);// ����ͼ���ʶ
            viewHolder = new ViewHolder();// ��ʼ��ViewHolder����
            viewHolder.title = (TextView) arg1.findViewById(R.id.ItemTitle);// ����ͼ�����
            viewHolder.image = (ImageView) arg1.findViewById(R.id.ItemImage);// ����ͼ��Ķ�����ֵ
            arg1.setTag(viewHolder);// ������ʾ
        } else {
            viewHolder = (ViewHolder) arg1.getTag();// ������ʾ
        }
        viewHolder.title.setText(pictures.get(arg0).getTitle());// ����ͼ�����
        viewHolder.image.setImageResource(pictures.get(arg0).getImageId());// ����ͼ��Ķ�����ֵ
        return arg1;// ����ͼ���ʶ
    }
}

class ViewHolder {// ����ViewHolder��

    public TextView title;// ����TextView����
    public ImageView image;// ����ImageView����
}

class Picture {// ����Picture������,����config������

    private String title;// �����ַ�������ʾͼ�����
    private int imageId;// ����int��������ʾͼ��Ķ�����ֵ

    public Picture() {// Ĭ�Ϲ��캯��

        super();
    }

    public Picture(String title, int imageId) {// �����вι��캯��

        super();
        this.title = title;// Ϊͼ����⸳ֵ
        this.imageId = imageId;// Ϊͼ��Ķ�����ֵ��ֵ
    }

    public String getTitle() {// ����ͼ�����Ŀɶ�����
        return title;
    }

    public void setTitle(String title) {// ����ͼ�����Ŀ�д����
        this.title = title;
    }

    public int getImageId() {// ����ͼ�������ֵ�Ŀɶ�����
        return imageId;
    }

    public void setimageId(int imageId) {// ����ͼ�������ֵ�Ŀ�д����
        this.imageId = imageId;
    }
}
