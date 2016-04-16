/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           OrderDetail.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        ������������Ϣ���������ŵ���Ҫ��static������static��Ա��ͳһ��Ϊȫ�ֱ�����ȫ�ֺ�����       
**------------------------------------------------------------------------------------------------------
** Created by:          guozhenzhen 
** Created date:        2015-01-10
** Version:             V1.0 
** Descriptions:        The original version       
********************************************************************************************************/

package com.easivend.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.Context;
import com.easivend.dao.vmc_orderDAO;
import com.easivend.model.Tb_vmc_order_pay;
import com.easivend.model.Tb_vmc_order_product;

public class OrderDetail 
{ 
	//��������Ϣ
	private static String proID = "";//����id+��Ʒ��
	private static String proType = "";//1����ͨ����ƷID����,2����ͨ����������	
	//����֧���� 
	private static String ordereID = "";// ����ID[pk]
	private static int payType = 0;// ֧����ʽ0�ֽ�1������2֧����������3֧������ά�룬4΢��ɨ��
	private static int payStatus = 0;// ����״̬0�����ɹ���1����ʧ�ܣ�2֧��ʧ�ܣ�3δ֧��
	private static int RealStatus = 0;// �˿�״̬��0����ʾδ�����˿����1�˿���ɣ�2�����˿3�˿�ʧ��
	private static float smallNote = 0;// ֽ�ҽ��
	private static float smallConi = 0;// Ӳ�ҽ��
	private static float smallAmount = 0;// �ֽ�Ͷ����
	private static float smallCard = 0;// ���ֽ�֧�����
	private static float shouldPay = 0;// ��Ʒ�ܽ�������Ʒ����
	private static int shouldNo = 0;// ��Ʒ������,����1��
	private static float realNote = 0;// ֽ���˱ҽ��
	private static float realCoin = 0;// Ӳ���˱ҽ��
	private static float realAmount = 0;// �ֽ��˱ҽ��
	private static float debtAmount = 0;// Ƿ����
	private static float realCard = 0;// ���ֽ��˱ҽ��
	//������ϸ��Ϣ��      
	private static String productID = "";// ��ƷID
    private static int yujiHuo = 0;//Ԥ�Ƴ���:����1��
    private static int realHuo = 0;//ʵ�ʳ���: 1����0
    private static String cabID = "";//�����
    private static String columnID = "";//������
    private static int huoStatus = 0;//����״̬: 0�����ɹ���1����ʧ��
    
    //������־
    public static void addLog(Context context)
	{
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
    	String date=df.format(new Date());
		vmc_orderDAO orderDAO = new vmc_orderDAO(context);// ����InaccountDAO����
		Tb_vmc_order_pay tb_vmc_order_pay = new Tb_vmc_order_pay(ordereID, payType, payStatus,
					RealStatus, smallNote, smallConi,
					smallAmount, smallCard, shouldPay, shouldNo,
					realNote, realCoin, realAmount, debtAmount,
					realCard, date);
		Tb_vmc_order_product tb_vmc_order_product=new Tb_vmc_order_product(ordereID, productID, yujiHuo,
	    		realHuo, cabID, columnID, huoStatus);
		orderDAO.add(tb_vmc_order_pay, tb_vmc_order_product);
		cleardata();
	}
    //�������
    public static void cleardata()
    {
    	//��������Ϣ
    	 proID = "";//����id+��Ʒ��
    	 proType = "";//1����ͨ����ƷID����,2����ͨ����������	
    	//����֧���� 
    	 ordereID = "";// ����ID[pk]
    	 payType = 0;// ֧����ʽ0�ֽ�1������2֧����������3֧������ά�룬4΢��ɨ��
    	 payStatus = 0;// ����״̬0�����ɹ���1����ʧ�ܣ�2֧��ʧ�ܣ�3δ֧��
    	 RealStatus = 0;// �˿�״̬��0����ʾδ�����˿����1�˿���ɣ�2�����˿3�˿�ʧ��
    	 smallNote = 0;// ֽ�ҽ��
    	 smallConi = 0;// Ӳ�ҽ��
    	 smallAmount = 0;// �ֽ�Ͷ����
    	 smallCard = 0;// ���ֽ�֧�����
    	 shouldPay = 0;// ��Ʒ�ܽ�������Ʒ����
    	 shouldNo = 0;// ��Ʒ������,����1��
    	 realNote = 0;// ֽ���˱ҽ��
    	 realCoin = 0;// Ӳ���˱ҽ��
    	 realAmount = 0;// �ֽ��˱ҽ��
    	 debtAmount = 0;// Ƿ����
    	 realCard = 0;// ���ֽ��˱ҽ��
    	//������ϸ��Ϣ��      
    	 productID = "";// ��ƷID
         yujiHuo = 0;//Ԥ�Ƴ���:����1��
         realHuo = 0;//ʵ�ʳ���: 1����0
         cabID = "";//�����
         columnID = "";//������
         huoStatus = 0;//����״̬: 0�����ɹ���1����ʧ��
    }
    
	public static String getProID() {
		return proID;
	}
	public static void setProID(String proID) {
		OrderDetail.proID = proID;
	}
	public static String getProType() {
		return proType;
	}
	public static void setProType(String proType) {
		OrderDetail.proType = proType;
	}	
	public static String getOrdereID() {
		return ordereID;
	}
	public static void setOrdereID(String ordereID) {
		OrderDetail.ordereID = ordereID;
	}
	public static int getPayType() {
		return payType;
	}
	public static void setPayType(int payType) {
		OrderDetail.payType = payType;
	}
	public static int getPayStatus() {
		return payStatus;
	}
	public static void setPayStatus(int payStatus) {
		OrderDetail.payStatus = payStatus;
	}
	public static int getRealStatus() {
		return RealStatus;
	}
	public static void setRealStatus(int realStatus) {
		RealStatus = realStatus;
	}
	public static float getSmallNote() {
		return smallNote;
	}
	public static void setSmallNote(float smallNote) {
		OrderDetail.smallNote = smallNote;
	}
	public static float getSmallConi() {
		return smallConi;
	}
	public static void setSmallConi(float smallConi) {
		OrderDetail.smallConi = smallConi;
	}
	public static float getSmallAmount() {
		return smallAmount;
	}
	public static void setSmallAmount(float smallAmount) {
		OrderDetail.smallAmount = smallAmount;
	}
	public static float getSmallCard() {
		return smallCard;
	}
	public static void setSmallCard(float smallCard) {
		OrderDetail.smallCard = smallCard;
	}
	public static float getShouldPay() {
		return shouldPay;
	}
	public static void setShouldPay(float shouldPay) {
		OrderDetail.shouldPay = shouldPay;
	}
	public static int getShouldNo() {
		return shouldNo;
	}
	public static void setShouldNo(int shouldNo) {
		OrderDetail.shouldNo = shouldNo;
	}
	public static float getRealNote() {
		return realNote;
	}
	public static void setRealNote(float realNote) {
		OrderDetail.realNote = realNote;
	}
	public static float getRealCoin() {
		return realCoin;
	}
	public static void setRealCoin(float realCoin) {
		OrderDetail.realCoin = realCoin;
	}
	public static float getRealAmount() {
		return realAmount;
	}
	public static void setRealAmount(float realAmount) {
		OrderDetail.realAmount = realAmount;
	}
	public static float getDebtAmount() {
		return debtAmount;
	}
	public static void setDebtAmount(float debtAmount) {
		OrderDetail.debtAmount = debtAmount;
	}
	public static float getRealCard() {
		return realCard;
	}
	public static void setRealCard(float realCard) {
		OrderDetail.realCard = realCard;
	}
	public static String getProductID() {
		return productID;
	}
	public static void setProductID(String productID) {
		OrderDetail.productID = productID;
	}
	public static int getYujiHuo() {
		return yujiHuo;
	}
	public static void setYujiHuo(int yujiHuo) {
		OrderDetail.yujiHuo = yujiHuo;
	}
	public static int getRealHuo() {
		return realHuo;
	}
	public static void setRealHuo(int realHuo) {
		OrderDetail.realHuo = realHuo;
	}
	public static String getCabID() {
		return cabID;
	}
	public static void setCabID(String cabID) {
		OrderDetail.cabID = cabID;
	}
	public static String getColumnID() {
		return columnID;
	}
	public static void setColumnID(String columnID) {
		OrderDetail.columnID = columnID;
	}
	public static int getHuoStatus() {
		return huoStatus;
	}
	public static void setHuoStatus(int huoStatus) {
		OrderDetail.huoStatus = huoStatus;
	}
    
    
    	
	
    
    
}
