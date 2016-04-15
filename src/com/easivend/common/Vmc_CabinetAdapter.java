package com.easivend.common;

import java.util.List;

import android.content.Context;

import com.easivend.dao.vmc_cabinetDAO;
import com.easivend.model.Tb_vmc_cabinet;

public class Vmc_CabinetAdapter
{
	private String[] cabinetID = null;//���������������
	private int[] cabinetType = null;//�����������������
	private String[] cabType={"0�޻���","1���ɻ���","2 ��ʽ����̨","3 ����̨+���ʹ�","4 ����̨+����","5���ӹ�"};
	// ��ʾ��Ʒ������Ϣ,һ���spinnerʹ�ã�����ѡ�������
	public String[] showSpinInfo(Context context) 
	{	  
		String[] strInfos = null;// �����ַ������飬�����洢������Ϣ
	    
		vmc_cabinetDAO cabinetDAO = new vmc_cabinetDAO(context);// ����InaccountDAO����
	    // ��ȡ����������Ϣ�����洢��List���ͼ�����
	    List<Tb_vmc_cabinet> listinfos = cabinetDAO.getScrollData();
	    strInfos = new String[listinfos.size()];// �����ַ�������ĳ���
	    cabinetID = new String[listinfos.size()];// �����ַ�������ĳ���
	    cabinetType=new int[listinfos.size()];// �����ַ�������ĳ���
	    int m = 0;// ����һ����ʼ��ʶ
	    // ����List���ͼ���
	    for (Tb_vmc_cabinet tb_inaccount : listinfos) 
	    {
	        // �����������Ϣ��ϳ�һ���ַ������洢���ַ����������Ӧλ��
	        strInfos[m] = "���:"+tb_inaccount.getCabID() + "<---|--->" + "����:"+cabType[tb_inaccount.getCabType()];
	        cabinetID[m] = tb_inaccount.getCabID();
	        cabinetType[m]= tb_inaccount.getCabType();
	        m++;// ��ʶ��1
	    }
	    return strInfos;
	}
	public String[] getCabinetID() {
		return cabinetID;
	}
	public void setCabinetID(String[] cabinetID) {
		this.cabinetID = cabinetID;
	}
	public int[] getCabinetType() {
		return cabinetType;
	}
	public void setCabinetType(int[] cabinetType) {
		this.cabinetType = cabinetType;
	}
	
}
