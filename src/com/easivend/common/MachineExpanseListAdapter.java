/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           MachineExpanseListAdapter.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        ExpandableListView�������࣬�������������в��������ڵ�     
**------------------------------------------------------------------------------------------------------
** Created by:          guozhenzhen 
** Created date:        2015-01-10
** Version:             V1.0 
** Descriptions:        The original version       
********************************************************************************************************/

package com.easivend.common;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;


public class MachineExpanseListAdapter extends BaseExpandableListAdapter 
{
	private Context context=null;
	//������
	public static String group[]=new String[]{">>>��������",">>>�¶�����",">>>����",">>>����",">>>����"};
	//������
	public static String child[][]=new String[][]{{"�����տ�������:","�����տ���ʱ���","�ڼ��տ�������:","�ڼ��տ���ʱ���"},{"�¶�ֵ:","�����տ���ʱ���","�ڼ��տ���ʱ���"},
			{"�����տ���ʱ���","�ڼ��տ���ʱ���"},{"�����տ���ʱ���","�ڼ��տ���ʱ���"},{"�����տ���ʱ���","�ڼ��տ���ʱ���"}};
	//�����Ƶ�ֵ
	public static String childValue[][]=new String[][]{{"","","",""},{"","",""},
		{"",""},{"",""},{"",""}};

	//���췽��
	public MachineExpanseListAdapter(Context context,String[][] childValue)
	{
		this.context = context;
		this.childValue = childValue;
	}

	@Override
	//ȡ��ָ������ѡ��
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return this.child[arg0][arg1]+this.childValue[arg0][arg1];
	}

	@Override
	//ȡ����ID
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return arg1;
	}
	
	private TextView buildTextView()
	{
		AbsListView.LayoutParams params=new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,35); 
		TextView textView=new TextView(this.context);
		textView.setLayoutParams(params);
		//textView.setTextSize(25.0f);
		//textView.setGravity(Gravity.LEFT);
		//textView.setPadding(40, 8, 3, 3);
		return textView;
	}

	@Override
	//��������ͼ
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TextView textview=new TextView(this.context);
		textview.setText(this.getChild(groupPosition, childPosition).toString());
		return textview;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return this.child[groupPosition].length;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return this.group[groupPosition];
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return this.group.length;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TextView textview=this.buildTextView();
		textview.setText(this.getGroup(groupPosition).toString());
		return textview;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
	
	
}
