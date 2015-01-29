/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           EVprotocol.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        vmc_order_pay ����֧����          
**------------------------------------------------------------------------------------------------------
** Created by:          yanbo 
** Created date:        2015-01-10
** Version:             V1.0 
** Descriptions:        The original version       
********************************************************************************************************/

package com.easivend.model;

import java.util.Date;

public class Tb_vmc_order_pay 
{
	private String ordereID;// ����ID[pk]
	private int payType;// ֧����ʽ0�ֽ�1������2֧����������3֧������ά�룬4΢��ɨ��
	private int payStatus;// ����״̬0�����ɹ���1����ʧ�ܣ�2֧��ʧ�ܣ�3δ֧��
	private int RealStatus;// �˿�״̬��0����ʾδ�����˿����1�˿���ɣ�2�����˿3�˿�ʧ��
	private float smallNote;// ֽ�ҽ��
	private float smallConi;// Ӳ�ҽ��
	private float smallAmount;// �ֽ�Ͷ����
	private float smallCard;// ���ֽ�֧�����
	private float shouldPay;// ��Ʒ�ܽ��
	private int shouldNo;// ��Ʒ������
	private float realNote;// ֽ���˱ҽ��
	private float realCoin;// Ӳ���˱ҽ��
	private float realAmount;// �ֽ��˱ҽ��
	private float debtAmount;// Ƿ����
	private float realCard;// ���ֽ��˱ҽ��
	private Date payTime;//֧��ʱ��
	// �����вι��캯����������ʼ��������Ϣʵ�����еĸ����ֶ�
	public Tb_vmc_order_pay(String ordereID, int payType, int payStatus,
			int realStatus, float smallNote, float smallConi,
			float smallAmount, float smallCard, float shouldPay, int shouldNo,
			float realNote, float realCoin, float realAmount, float debtAmount,
			float realCard, Date payTime) {
		super();
		this.ordereID = ordereID;
		this.payType = payType;
		this.payStatus = payStatus;
		RealStatus = realStatus;
		this.smallNote = smallNote;
		this.smallConi = smallConi;
		this.smallAmount = smallAmount;
		this.smallCard = smallCard;
		this.shouldPay = shouldPay;
		this.shouldNo = shouldNo;
		this.realNote = realNote;
		this.realCoin = realCoin;
		this.realAmount = realAmount;
		this.debtAmount = debtAmount;
		this.realCard = realCard;
		this.payTime = payTime;
	}
	public String getOrdereID() {
		return ordereID;
	}
	public void setOrdereID(String ordereID) {
		this.ordereID = ordereID;
	}
	public int getPayType() {
		return payType;
	}
	public void setPayType(int payType) {
		this.payType = payType;
	}
	public int getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
	}
	public int getRealStatus() {
		return RealStatus;
	}
	public void setRealStatus(int realStatus) {
		RealStatus = realStatus;
	}
	public float getSmallNote() {
		return smallNote;
	}
	public void setSmallNote(float smallNote) {
		this.smallNote = smallNote;
	}
	public float getSmallConi() {
		return smallConi;
	}
	public void setSmallConi(float smallConi) {
		this.smallConi = smallConi;
	}
	public float getSmallAmount() {
		return smallAmount;
	}
	public void setSmallAmount(float smallAmount) {
		this.smallAmount = smallAmount;
	}
	public float getSmallCard() {
		return smallCard;
	}
	public void setSmallCard(float smallCard) {
		this.smallCard = smallCard;
	}
	public float getShouldPay() {
		return shouldPay;
	}
	public void setShouldPay(float shouldPay) {
		this.shouldPay = shouldPay;
	}
	public int getShouldNo() {
		return shouldNo;
	}
	public void setShouldNo(int shouldNo) {
		this.shouldNo = shouldNo;
	}
	public float getRealNote() {
		return realNote;
	}
	public void setRealNote(float realNote) {
		this.realNote = realNote;
	}
	public float getRealCoin() {
		return realCoin;
	}
	public void setRealCoin(float realCoin) {
		this.realCoin = realCoin;
	}
	public float getRealAmount() {
		return realAmount;
	}
	public void setRealAmount(float realAmount) {
		this.realAmount = realAmount;
	}
	public float getDebtAmount() {
		return debtAmount;
	}
	public void setDebtAmount(float debtAmount) {
		this.debtAmount = debtAmount;
	}
	public float getRealCard() {
		return realCard;
	}
	public void setRealCard(float realCard) {
		this.realCard = realCard;
	}
	public Date getPayTime() {
		return payTime;
	}
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	
}
