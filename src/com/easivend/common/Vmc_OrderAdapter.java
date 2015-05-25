package com.easivend.common;

import java.util.List;

import android.content.Context;
import android.widget.Toast;

import com.easivend.app.maintain.Order;
import com.easivend.dao.vmc_orderDAO;
import com.easivend.dao.vmc_productDAO;
import com.easivend.model.Tb_vmc_order_pay;
import com.easivend.model.Tb_vmc_order_product;
import com.easivend.model.Tb_vmc_product;

public class Vmc_OrderAdapter
{
	int    count=0;
	//��֧������
	String[] ordereID;// ����ID[pk]
	String[] payType;// ֧����ʽ0�ֽ�1������2֧����������3֧������ά�룬4΢��ɨ��
	String[] payStatus;// ����״̬0�����ɹ���1����ʧ�ܣ�2֧��ʧ�ܣ�3δ֧��
	String[] RealStatus;// �˿�״̬��0����ʾδ�����˿����1�˿���ɣ�2�����˿3�˿�ʧ��
	String[] smallNote;// ֽ�ҽ��
	String[] smallConi;// Ӳ�ҽ��
	String[] smallAmount;// �ֽ�Ͷ����
	String[] smallCard;// ���ֽ�֧�����
	String[] shouldPay;// ��Ʒ�ܽ��
	String[] shouldNo;// ��Ʒ������
	String[] realNote;// ֽ���˱ҽ��
	String[] realCoin;// Ӳ���˱ҽ��
	String[] realAmount;// �ֽ��˱ҽ��
	String[] debtAmount;// Ƿ����
	String[] realCard;// ���ֽ��˱ҽ��
	String[] payTime;//֧��ʱ��
	//��ϸ֧������
	String[] productID;//��Ʒid
	String[] cabID;//�����
    String[] columnID;//������
    //��Ʒ��Ϣ
    String[] productName;// ��Ʒȫ��
    String[] salesPrice;// �Żݼ�,�硱20.00��
    
	//�������ṩ��Ϣ
	public void grid(Context context,int mYear,int mMon,int mDay,int eYear,int eMon,int eDay)
	{
		String mYearStr=null,mMonthStr=null,mDayStr=null;
		String eYearStr=null,eMonthStr=null,eDayStr=null;
		
		mYearStr=((mYear<10)?("0"+String.valueOf(mYear)):String.valueOf(mYear));
		mMonthStr=((mMon<10)?("0"+String.valueOf(mMon)):String.valueOf(mMon));
		mDayStr=((mDay<10)?("0"+String.valueOf(mDay)):String.valueOf(mDay));
		eYearStr=((eYear<10)?("0"+String.valueOf(eYear)):String.valueOf(eYear));
		eMonthStr=((eMon<10)?("0"+String.valueOf(eMon)):String.valueOf(eMon));
		eDayStr=((eDay<10)?("0"+String.valueOf(eDay)):String.valueOf(eDay));
		// ����InaccountDAO����
		vmc_orderDAO orderDAO = new vmc_orderDAO(context);
		vmc_productDAO productDAO = new vmc_productDAO(context);// ����InaccountDAO����
		String start=mYearStr+"-"+mMonthStr+"-"+mDayStr;
		String end=eYearStr+"-"+eMonthStr+"-"+eDayStr;			
		List<Tb_vmc_order_pay> listinfos=orderDAO.getScrollPay(start,end);
		String[] strInfos = new String[listinfos.size()];
		//��֧������
		ordereID = new String[listinfos.size()];
		payType = new String[listinfos.size()];
		payStatus = new String[listinfos.size()];
		RealStatus = new String[listinfos.size()];
		smallNote = new String[listinfos.size()];// ֽ�ҽ��
		smallConi = new String[listinfos.size()];// Ӳ�ҽ��
		smallAmount = new String[listinfos.size()];// �ֽ�Ͷ����
		smallCard = new String[listinfos.size()];// ���ֽ�֧�����
		shouldPay = new String[listinfos.size()];// ��Ʒ�ܽ��
		shouldNo = new String[listinfos.size()];// ��Ʒ������
		realNote = new String[listinfos.size()];// ֽ���˱ҽ��
		realCoin = new String[listinfos.size()];// Ӳ���˱ҽ��
		realAmount = new String[listinfos.size()];// �ֽ��˱ҽ��
		debtAmount = new String[listinfos.size()];// Ƿ����
		realCard = new String[listinfos.size()];// ���ֽ��˱ҽ��
		payTime = new String[listinfos.size()];//֧��ʱ��
		//��ϸ֧������
		productID = new String[listinfos.size()];//��Ʒid
		cabID = new String[listinfos.size()];//�����
	    columnID = new String[listinfos.size()];//������
	    //��Ʒ��Ϣ
	    productName = new String[listinfos.size()];// ��Ʒȫ��
	    salesPrice = new String[listinfos.size()];// �Żݼ�,�硱20.00��
	    
		count=listinfos.size();
		int m=0;
		// ����List���ͼ���
	    for (Tb_vmc_order_pay tb_inaccount : listinfos) 
	    {
	    	//��֧������
	    	ordereID[m]= tb_inaccount.getOrdereID();
	    	payType[m] = typestr(0,tb_inaccount.getPayType());
			payStatus[m] = typestr(1,tb_inaccount.getPayStatus());
			RealStatus[m] = typestr(2,tb_inaccount.getRealStatus());
			smallNote[m] = String.valueOf(tb_inaccount.getSmallAmount());// ֽ�ҽ��
			smallConi[m] = String.valueOf(tb_inaccount.getSmallConi());// Ӳ�ҽ��
			smallAmount[m] = String.valueOf(tb_inaccount.getSmallAmount());// �ֽ�Ͷ����
			smallCard[m] = String.valueOf(tb_inaccount.getSmallCard());// ���ֽ�֧�����
			shouldPay[m] = String.valueOf(tb_inaccount.getShouldPay());// ��Ʒ�ܽ��
			shouldNo[m] = String.valueOf(tb_inaccount.getShouldNo());// ��Ʒ������
			realNote[m] = String.valueOf(tb_inaccount.getRealNote());// ֽ���˱ҽ��
			realCoin[m] = String.valueOf(tb_inaccount.getRealCoin());// Ӳ���˱ҽ��
			realAmount[m] = String.valueOf(tb_inaccount.getRealAmount());// �ֽ��˱ҽ��
			debtAmount[m] = String.valueOf(tb_inaccount.getDebtAmount());// Ƿ����
			realCard[m] = String.valueOf(tb_inaccount.getRealCard());// ���ֽ��˱ҽ��
			payTime[m] = String.valueOf(tb_inaccount.getPayTime());//֧��ʱ��
			//��ϸ֧������
			Tb_vmc_order_product tb_vmc_order_product=orderDAO.getScrollProduct(ordereID[m]);
			productID[m] = tb_vmc_order_product.getProductID();
			cabID[m] = tb_vmc_order_product.getCabID();
			columnID[m] = tb_vmc_order_product.getColumnID();
			//��Ʒ��Ϣ
			Tb_vmc_product tb_product = productDAO.find(productID[m]);
			productName[m]=tb_product.getProductName();
			salesPrice[m]=String.valueOf(tb_product.getSalesPrice());
	    	m++;// ��ʶ��1
	    }
				
	}
	    
	/*********************************************************************************************************
	** Function name:     	typestr
	** Descriptions:	       ����������״̬ת���ַ�����ʾ
	** input parameters:    type=0֧����ʽ,1����״̬,2�˿�״̬
	** payType;// ֧����ʽ0�ֽ�1������2֧����������3֧������ά�룬4΢��ɨ��
	** payStatus;// ����״̬0�����ɹ���1����ʧ�ܣ�2֧��ʧ�ܣ�3δ֧��
	** RealStatus;// �˿�״̬��0����ʾδ�����˿����1�˿���ɣ�2�����˿3�˿�ʧ��
	** output parameters:   ��
	** Returned value:      ��
	*********************************************************************************************************/
	private String typestr(int type,int value)
	{
		switch(type)
		{
			case 0:// ֧����ʽ
				// ֧����ʽ0�ֽ�1������2֧����������3֧������ά�룬4΢��ɨ��
				switch(value)
				{
					case 0:
						return "�ֽ�";						
					case 1:
						return "����";	
					case 2:
						return "֧��������";
					case 3:
						return "֧������ά��";
					case 4:
						return "΢֧��";		
				}
				break;
			case 1:// ����״̬
				// ����״̬0�����ɹ���1����ʧ�ܣ�2֧��ʧ�ܣ�3δ֧��
				switch(value)
				{
					case 0:
						return "�����ɹ�";						
					case 1:
						return "����ʧ��";	
					case 2:
						return "֧��ʧ��";
					case 3:
						return "δ֧��";					
				}
				break;
			case 2:// �˿�״̬
				// �˿�״̬��0����ʾδ�����˿����1�˿���ɣ�2�����˿3�˿�ʧ��
				switch(value)
				{
					case 0:
						return "";						
					case 1:
						return "�˿����";	
					case 2:
						return "�����˿�";
					case 3:
						return "�˿�ʧ��";					
				}
				break;
		}
		return "";
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String[] getOrdereID() {
		return ordereID;
	}

	public void setOrdereID(String[] ordereID) {
		this.ordereID = ordereID;
	}

	public String[] getPayType() {
		return payType;
	}

	public void setPayType(String[] payType) {
		this.payType = payType;
	}

	public String[] getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String[] payStatus) {
		this.payStatus = payStatus;
	}

	public String[] getRealStatus() {
		return RealStatus;
	}

	public void setRealStatus(String[] realStatus) {
		RealStatus = realStatus;
	}

	public String[] getProductName() {
		return productName;
	}

	public void setProductName(String[] productName) {
		this.productName = productName;
	}

	public String[] getSmallAmount() {
		return smallAmount;
	}

	public void setSmallAmount(String[] smallAmount) {
		this.smallAmount = smallAmount;
	}

	public String[] getCabID() {
		return cabID;
	}

	public void setCabID(String[] cabID) {
		this.cabID = cabID;
	}

	public String[] getColumnID() {
		return columnID;
	}

	public void setColumnID(String[] columnID) {
		this.columnID = columnID;
	}

	public String[] getPayTime() {
		return payTime;
	}

	public void setPayTime(String[] payTime) {
		this.payTime = payTime;
	}
	public String[] getSmallNote() {
		return smallNote;
	}
	public void setSmallNote(String[] smallNote) {
		this.smallNote = smallNote;
	}
	public String[] getSmallConi() {
		return smallConi;
	}
	public void setSmallConi(String[] smallConi) {
		this.smallConi = smallConi;
	}
	public String[] getSmallCard() {
		return smallCard;
	}
	public void setSmallCard(String[] smallCard) {
		this.smallCard = smallCard;
	}
	public String[] getShouldPay() {
		return shouldPay;
	}
	public void setShouldPay(String[] shouldPay) {
		this.shouldPay = shouldPay;
	}
	public String[] getShouldNo() {
		return shouldNo;
	}
	public void setShouldNo(String[] shouldNo) {
		this.shouldNo = shouldNo;
	}
	public String[] getRealNote() {
		return realNote;
	}
	public void setRealNote(String[] realNote) {
		this.realNote = realNote;
	}
	public String[] getRealCoin() {
		return realCoin;
	}
	public void setRealCoin(String[] realCoin) {
		this.realCoin = realCoin;
	}
	public String[] getRealAmount() {
		return realAmount;
	}
	public void setRealAmount(String[] realAmount) {
		this.realAmount = realAmount;
	}
	public String[] getDebtAmount() {
		return debtAmount;
	}
	public void setDebtAmount(String[] debtAmount) {
		this.debtAmount = debtAmount;
	}
	public String[] getRealCard() {
		return realCard;
	}
	public void setRealCard(String[] realCard) {
		this.realCard = realCard;
	}
	public String[] getProductID() {
		return productID;
	}
	public void setProductID(String[] productID) {
		this.productID = productID;
	}
	public String[] getSalesPrice() {
		return salesPrice;
	}
	public void setSalesPrice(String[] salesPrice) {
		this.salesPrice = salesPrice;
	}
	
}
