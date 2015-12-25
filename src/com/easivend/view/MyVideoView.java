/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           MyVideoView.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        ��װ��Ƶ�ؼ�        
**------------------------------------------------------------------------------------------------------
** Created by:          guozhenzhen 
** Created date:        2015-01-10
** Version:             V1.0 
** Descriptions:        The original version       
********************************************************************************************************/

package com.easivend.view;

import android.content.Context;  
import android.util.AttributeSet;
import android.widget.VideoView;

public class MyVideoView extends VideoView {
	public MyVideoView(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
    }  
  
    public MyVideoView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
  
    public MyVideoView(Context context) {  
        super(context);  
    }  
    
    //���c�ڴˣ�override�@�� function �ſ��������M��!
    @Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
        // TODO Auto-generated method stub  
  
        int width = getDefaultSize(0, widthMeasureSpec);  
        int height = getDefaultSize(0, heightMeasureSpec);  
        setMeasuredDimension(width, height);  
    }
}
