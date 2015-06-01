/****************************************Copyright (c)*************************************************
**                      Fujian Junpeng Communicaiton Technology Co.,Ltd.
**                               http://www.easivend.com.cn
**--------------File Info------------------------------------------------------------------------------
** File name:           EVprotocol.java
** Last modified Date:  2015-01-10
** Last Version:         
** Descriptions:        java����JNI�ӿڷ�װ��                   
**------------------------------------------------------------------------------------------------------
** Created by:          yanbo 
** Created date:        2015-01-10
** Version:             V1.0 
** Descriptions:        The original version       
********************************************************************************************************/

package com.easivend.evprotocol;
import java.util.Collection;
import java.util.EventListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;




/*********************************************************************************************************
**���� EVprotocol �ӿڷ�װ��
*********************************************************************************************************/
public class EVprotocol {
	public EVprotocol(){		//���캯�� Ĭ�Ͽ����̴߳���ص�����
		if(ev_thread == null){
			ev_thread = new EV_Thread();
			ev_thread.start();
		}
	} 
	
	
	
	//����JNI��̬���ӿ�
	static{
		System.loadLibrary("EVdirver");
		
	}
	
	
	
	
	
	/*********************************************************************************************************
	 **�������������
	*********************************************************************************************************/
	private static final int EV_NONE 		= 0;	//��������
	private static final int EV_REGISTER 	= 1;	//����ע��
	private static final int EV_RELEASE 	= 2;	//�����ͷ�
	
	//=====================��ݹ�����==============================================================================
	private static final int EV_BENTO_OPEN 	= 11;	//��ݹ���
	private static final int EV_BENTO_CHECK = 12;	//��ݹ��ѯ
	private static final int EV_BENTO_LIGHT = 13;	//��ݹ�����
	private static final int EV_BENTO_COOL 	= 14;	//��ݹ�����
	private static final int EV_BENTO_HOT 	= 15;	//��ݹ����
	
	//=====================MDB�ֽ�ģ������==============================================================================
	private static final int EV_MDB_INIT 	= 21;	//MDB�豸��ʼ��
	private static final int EV_MDB_ENABLE 	= 22;	//MDB�豸ʹ��
	private static final int EV_MDB_HEART 	= 23;	//MDB�豸����
	private static final int EV_MDB_B_INFO 	= 24;	//MDBֽ������Ϣ
	private static final int EV_MDB_C_INFO 	= 25;	//MDBӲ������Ϣ
	private static final int EV_MDB_COST 	= 26;	//MDB�豸�ۿ�
	private static final int EV_MDB_PAYBACK = 27;	//MDB�豸�˱�
	private static final int EV_MDB_PAYOUT 	= 28;	//MDB�豸�ұ�



	
	/*********************************************************************************************************
	 **�����������ӿ�
	*********************************************************************************************************/
	public static class RequestObject{
		public int type;
		public String portName;
		public int fd;
		public int addr;
		public int box;
		public int opt;
		
		public int bill;
		public int coin;
		
		public int billPay;
		public int coinPay;
		public int cost;
		
		
	};
	
	
	
	
	
	/*********************************************************************************************************
	** Function name	:		EV_portRegister
	** Descriptions		:		����ע��ӿ� 	[�첽]
	** input parameters	:       portName ���ں� ����"COM1"
	** output parameters:		��
	** Returned value	:		1��ָ��ͳɹ�  0��ָ���ʧ��
	*  ����Ľ��ͨ���ص�����json�� 		 ���磺 EV_JSON={"EV_json":{"EV_type":1,"port":"/dev/ttymxc2","port_id":0}}
	*  							"EV_type" = EV_REGISTER = 1;��ʾ����ע�������
	*                           "port"    ��ʾ���صĴ��ں�,����portName��ֵ
	*							"port_id":��ʾ���صĴ��ڱ�ţ����ʧ���򷵻� -1
	*********************************************************************************************************/
	public static int EV_portRegister(String portName)
	{
		RequestObject req = new RequestObject();
		req.type = EV_REGISTER;
		req.portName = portName;
		return pushReq(req);
	}
	
	
	
	/*********************************************************************************************************
	** Function name	:		EV_portRelease
	** Descriptions		:		�����ͷŽӿ�  [�첽]
	** input parameters	:       port_id ���ڱ��
	** output parameters:		��
	** Returned value	:		1��ָ��ͳɹ�  0��ָ���ʧ��
	*	����Ľ��ͨ���ص�����json��	  ���磺 EV_JSON={"EV_json":{"EV_type":2,"result":1}}
	*							"EV_type"= EV_RELEASE = 2; ��ʾ�����ͷŰ�����
	*							"result":��ʾ�������    1:��ʾ�ɹ��ͷ�   0:��ʾ�ͷ�ʧ��
	*********************************************************************************************************/
	public  static int EV_portRelease(int port_id)
	{
		RequestObject req = new RequestObject();
		req.type = EV_RELEASE;
		req.fd = port_id;
		return pushReq(req);
	}
	
	
	
	/*********************************************************************************************************
	** Function name	:		EV_bentoOpen
	** Descriptions		:		��ݹ��Žӿ�  [�첽]
	** input parameters	:       port_id:���ڱ��, addr:���ӵ�ַ 01-16,box:���ŵĸ��Ӻ� 1-88
	** output parameters:		��
	** Returned value	:		1�����ͳɹ�  0������ʧ��
	*	����json��     ���磺 EV_JSON={"EV_json":{"EV_type":11,"port_id":0,"addr":1,"box":1,"is_success":1,"result":1}}
	*							"EV_type"= EV_BENTO_OPEN = 11; ���Ž����Ӧ������
	*							"port_id":ԭ������,
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"result": 	��ʾ������	1:���ųɹ�   0:����ʧ��
	*********************************************************************************************************/
	public  static int EV_bentoOpen(int port_id,int addr,int box)
	{
		RequestObject req = new RequestObject();
		req.type = EV_BENTO_OPEN;
		req.fd = port_id;
		req.addr = addr;
		req.box = box;
		return pushReq(req);
	}
	
	
	
	/*********************************************************************************************************
	** Function name	:		EV_bentoCheck
	** Descriptions		:		��ݹ��ѯ�ӿ�  [�첽]
	** input parameters	:       port_id:���ڱ��,addr:���ӵ�ַ 01-16
	** output parameters:		��
	** Returned value	:		1�����ͳɹ�  0������ʧ��
	*  ͨ���ص�����json��     ���磺 EV_JSON={"EV_json":{"EV_type":12,"port_id":0,"addr":0,"is_success":1,"ID":"xxxxxxxxx1",
	*								"cool":0,"hot":0,"light":1,"sum":88,[]}}
	*							"EV_type"= EV_BENTO_CHECK = 12: ���ѯ�����Ӧ������
	*							"port_id":ԭ������,"addr":ԭ�����ع��ӵ�ַ
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"ID":�������ID��
	*							"cool":�Ƿ�֧������ 	 	1:֧�� 0:��֧��
	*							"hot":�Ƿ�֧�ּ���  		1:֧�� 0:��֧��
	*							"light":�Ƿ�֧������  	1:֧�� 0:��֧��
	*							"sum":��������	�����������88 ��Ĭ�� ������� 1-88
	*********************************************************************************************************/
	public  static int EV_bentoCheck(int port_id,int addr)
	{
		RequestObject req = new RequestObject();
		req.type = EV_BENTO_CHECK;
		req.fd = port_id;
		req.addr = addr;
		return pushReq(req);
	}
	
	
	
	
	/*********************************************************************************************************
	** Function name	:		EV_bentoLight
	** Descriptions		:		��ݹ��������ƽӿ�  [�첽]
	** input parameters	:       port_id:���ڱ��,addr:���ӵ�ַ 01-16,opt:���������� 1:��  0:��
	** output parameters:		��
	** Returned value	:		1�����ͳɹ�  0������ʧ��
	*  ͨ���ص�����json��     ���磺 EV_JSON={"EV_json":{"EV_type":13,"port_id":0,"addr":1,"opt":1,"is_success":1,"result":1}}
	*							"EV_type"= EV_BENTO_LIGHT = 13: ���������ƽ����Ӧ������
	*							"port_id":ԭ������,"addr":ԭ�����ع��ӵ�ַ,"opt":ԭ�����ز���.
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"result": ��ʾ������	1:�ɹ�   0:ʧ��
	*********************************************************************************************************/
	public  static int EV_bentoLight(int port_id,int addr,int opt)
	{
		RequestObject req = new RequestObject();
		req.type = EV_BENTO_LIGHT;
		req.fd = port_id;
		req.addr = addr;
		req.opt = opt;
		return pushReq(req);
	}
	
	
	
	
	
	
	/*********************************************************************************************************
	** Function name	:		EV_mdbInit
	** Descriptions		:		MDB��ʼ���ӿ�  [�첽]
	** input parameters	:       port_id:���ڱ��,bill:����ֽ����  1:����,0:������,coin:����Ӳ����  1:����,0:������
	** output parameters:		��
	** Returned value	:		1�����ͳɹ�  0������ʧ��
	*  ͨ���ص�����json��     ���磺 EV_JSON={"EV_json":{"EV_type":21,"port_id":0,"bill":1,"coin":1,"is_success":1,"bill_result":1,"coin_result":1}}
	*							"EV_type"= EV_MDB_INIT = 21: ��MDB��ʼ�������Ӧ������
	*							"port_id":ԭ������,"bill":ԭ�����ع��ӵ�ַ,"coin":ԭ�����ز���.
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"bill_result": ֽ����������	1:�ɹ�   0:ʧ��
	*							"coin_result": ֽ����������	1:�ɹ�   0:ʧ��
	*********************************************************************************************************/
	public  static int EV_mdbInit(int port_id,int bill,int coin)
	{
		RequestObject req = new RequestObject();
		req.type = EV_MDB_INIT;
		req.fd = port_id;
		req.bill = bill;
		req.coin = coin;
		return pushReq(req);
	}
	
	
	
	
	/*********************************************************************************************************
	** Function name	:		EV_mdbEnable
	** Descriptions		:		MDBʹ�ܽӿ�  [�첽]
	** input parameters	:       port_id:���ڱ��; bill:����ֽ����  1:���� 0:������;coin:����Ӳ����  1:����,0:������ ;opt:���� 1:ʹ�� 0:����
	** output parameters:		��
	** Returned value	:		1�����ͳɹ�  0������ʧ��
	*  ͨ���ص�����json��     ���磺 EV_JSON={"EV_json":{"EV_type":22,"port_id":0,"bill":1,"coin":1,"opt":1,"is_success":1,"bill_result":1,"coin_result":1}}
	*							"EV_type"= EV_MDB_ENABLE = 22: ��MDBʹ�ܽ����Ӧ������
	*							"port_id":ԭ������,"bill":ԭ�����ع��ӵ�ַ,"coin":ԭ�����ز���;"opt":ԭ������
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"bill_result": ֽ����������	1:�ɹ�   0:ʧ��
	*							"coin_result": ֽ����������	1:�ɹ�   0:ʧ��
	*********************************************************************************************************/
	public  static int EV_mdbEnable(int port_id,int bill,int coin,int opt)
	{
		RequestObject req = new RequestObject();
		req.type = EV_MDB_ENABLE;
		req.fd = port_id;
		req.bill = bill;
		req.coin = coin;
		req.opt = opt;
		return pushReq(req);
	}
	
	
	
	
	/*********************************************************************************************************
	** Function name	:		EV_mdbHeart
	** Descriptions		:		MDB������ѯ�ӿ�  [�첽]
	** input parameters	:       port_id:���ڱ��;
	** output parameters:		��
	** Returned value	:		1�����ͳɹ�  0������ʧ��
	*  ͨ���ص�����json��     ���磺 EV_JSON={"EV_json":{"EV_type":23,"port_id":0,"is_success":1,
	*							"bill_enable":1,"bill_payback":0,"bill_err":0,"bill_recv":0,"bill_remain":0,
	*							"coin_enable":1,"coin_payback":0,"coin_err":0,"coin_recv":0,"coin_remain":0}}
	*							"EV_type"= EV_MDB_HEART = 23: ��ʾMDB������ѯ�����Ӧ������
	*							"port_id":ԭ������,
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"bill_enable": ֽ����ʹ��״̬		1:ʹ��   0:����
	*							"bill_payback": ֽ�����˱Ұ�ť����	1:����   0:�Ǵ���
	*							"bill_err":ֽ��������״̬			0:����   ��0 Ϊ������
	*							"bill_recv":ֽ������ǰ�ձҽ��	�Է�Ϊ��λ
	*							"bill_remain":ֽ������ǰ���ҽ��	�Է�Ϊ��λ
	*	*						"coin_enable": Ӳ����ʹ��״̬		1:ʹ��   0:����
	*							"coin_payback": Ӳ�����˱Ұ�ť����	1:����   0:�Ǵ���
	*							"coin_err":Ӳ��������״̬			0:����   ��0 Ϊ������
	*							"coin_recv":Ӳ������ǰ�ձҽ��	�Է�Ϊ��λ
	*							"coin_remain":Ӳ������ǰ���ҽ��	�Է�Ϊ��λ
	*********************************************************************************************************/
	public  static int EV_mdbHeart(int port_id)
	{
		RequestObject req = new RequestObject();
		req.type = EV_MDB_HEART;
		req.fd = port_id;
		return pushReq(req);
	}
	
	
	
	
	/*********************************************************************************************************
	** Function name	:		EVmdbBillInfoCheck
	** Descriptions		:		MDBֽ������ѯ�ӿ�  [�첽]
	** input parameters	:       port_id:���ڱ��;
	** output parameters:		��
	** Returned value	:		1�����ͳɹ�  0������ʧ��
	*	ͨ���ص�����json��     ���磺 EV_JSON={"EV_json":{"EV_type":24,"port_id":0,"is_success":1,
	*							"acceptor":2,"dispenser":2,"code":"ITL","sn":"12312....","model":"NV9",
	*							"ver":"1212","capacity":500,"ch_r":[],"ch_d":[]}}
	*							"EV_type"= EV_MDB_B_INFO = 24: ��ʾMDBֽ������Ϣ��ѯ�����Ӧ������
	*							"port_id":ԭ������,
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"acceptor": ֽ�ҽ�����Э������	0:�������ر�	2:MDBЭ�������
	*							"dispenser": ֽ��������Э������	0:�������ر�	2:MDBЭ��������
	*							"code": ֽ�������̴���			���磺ITL
	*							"sn":ֽ�������к�
	*							"model":ֽ�����ͺ�				����:NV9
	*							ver:ֽ��������汾��
	*							capacity:ֽ����������	
	*							ch_r:ֽ����������ͨ����ֵ 		�Է�Ϊ��λ
	*							ch_d:ֽ����������ͨ����ֵ		�Է�Ϊ��λ
	*********************************************************************************************************/
	public  static int EV_mdbBillInfoCheck(int port_id)
	{
		RequestObject req = new RequestObject();
		req.type = EV_MDB_B_INFO;
		req.fd = port_id;
		return pushReq(req);
	}
	
	
	
	
	
	/*********************************************************************************************************
	** Function name	:		EV_mdbCoinInfoCheck
	** Descriptions		:		MDBӲ������Ϣ��ѯ�ӿ�  [�첽]
	** input parameters	:       port_id:���ڱ��;
	** output parameters:		��
	** Returned value	:		1�����ͳɹ�  0������ʧ��
	*	ͨ���ص�����json��     ���磺 EV_JSON={"EV_json":{"EV_type":25,"port_id":0,"is_success":1,
	*							"acceptor":2,"dispenser":2,"code":"MEI","sn":"12312....","model":"***",
	*							"ver":"1212","capacity":500,"ch_r":[],"ch_d":[]}}
	*							"EV_type"= EV_MDB_C_INFO = 25: ��ʾMDBӲ������Ϣ��ѯ�����Ӧ������
	*							"port_id":ԭ������,
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"acceptor": Ӳ�ҽ�����Э������	0:�������ر�	1:����Ӳ������Э��  2:MDBЭ������� 3:����Ӳ������Э��
	*							"dispenser": Ӳ��������Э������	0:�������ر�	1:hopper ����232Э��  2:MDBЭ��������
	*							"code": Ӳ�������̴���			���磺MEI
	*							"sn":Ӳ�������к�
	*							"model":Ӳ�����ͺ�			
	*							ver:Ӳ��������汾��
	*							capacity:Ӳ����������	
	*							ch_r:Ӳ����������ͨ����ֵ 		�Է�Ϊ��λ
	*							ch_d:Ӳ����������ͨ����ֵ		�Է�Ϊ��λ
	*********************************************************************************************************/
	public  static int EV_mdbCoinInfoCheck(int port_id)
	{
		RequestObject req = new RequestObject();
		req.type = EV_MDB_C_INFO;
		req.fd = port_id;
		return pushReq(req);
	}
	
	
	
	/*********************************************************************************************************
	** Function name	:		EV_mdbCost
	** Descriptions		:		MDB�ۿ�ӿ�  [�첽]
	** input parameters	:       port_id:���ڱ��;cost:�ۿ���  �Է�Ϊ��λ
	** output parameters:		��
	** Returned value	:		1�����ͳɹ�  0������ʧ��
	*	ͨ���ص�����json��     ���磺 EV_JSON={"EV_json":{"EV_type":26,"port_id":0,"cost":100,"is_success":1,
	*							"result":1,"bill_recv":0,"coin_recv":0}}
	*							"EV_type"= EV_MDB_COST = 26: ��ʾMDB�ۿ�����Ӧ������
	*							"port_id":ԭ������,
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"result":�ۿ���	1:�ۿ�ɹ�     0:�ۿ�ʧ��
	*							"bill_recv":ֽ������ǰ�ձҽ��	  �Է�Ϊ��λ
	*							"coin_recv":Ӳ������ǰ�ձҽ��	  �Է�Ϊ��λ
	*********************************************************************************************************/
	public  static int EV_mdbCost(int port_id,int cost)
	{
		RequestObject req = new RequestObject();
		req.type = EV_MDB_COST;
		req.fd = port_id;
		req.cost = cost;
		return pushReq(req);
	}
	
	
	
	/*********************************************************************************************************
	** Function name	:		EV_mdbPayback
	** Descriptions		:		MDB�˱ҽӿ�  [�첽]
	** input parameters	:       port_id:���ڱ��;bill:����ֽ����  1:����,0:������,coin:����Ӳ����  1:����,0:������
	** output parameters:		��
	** Returned value	:		1�����ͳɹ�  0������ʧ��
	*	����json��     ���磺 EV_JSON={"EV_json":{"EV_type":26,"port_id":0,"bill":1,"coin":1,"is_success":1,
	*							"result":1,"bill_changed":0,"coin_changed":100}}
	*							"EV_type"= EV_MDB_COST = 26: ��ʾMDB�˱ҽ����Ӧ������
	*							"port_id":ԭ������,"bill":ԭ������,"coin":ԭ������,
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"result":�ۿ���	1:�˱ҳɹ�     0:�˱�ʧ��
	*							"bill_changed":ֽ������ǰ�ұҽ��	  �Է�Ϊ��λ
	*							"coin_changed":Ӳ������ǰ�ұҽ��	  �Է�Ϊ��λ
	*********************************************************************************************************/
	public  static int EV_mdbPayback(int port_id,int bill,int coin)
	{
		RequestObject req = new RequestObject();
		req.type = EV_MDB_PAYBACK;
		req.fd = port_id;
		req.bill = bill;
		req.coin = coin;
		return pushReq(req);
	}
	
	
	
	/*********************************************************************************************************
	** Function name	:		EV_mdbPayout
	** Descriptions		:		MDB�ұҽӿ�  [�첽]
	** input parameters	:       port_id:���ڱ��;bill:����ֽ����  1:����,0:������,coin:����Ӳ����  1:����,0:������;
	*							billPayout:ֽ�����·��ұҽ�� ��Ϊ��λ ;coinPayout:Ӳ�����·��ұҽ�� ��Ϊ��λ 
	** output parameters:		��
	** Returned value	:		1�����ͳɹ�  0������ʧ��
	*	����json��     ���磺 EV_JSON={"EV_json":{"EV_type":28,"port_id":0,"bill":0,"coin":1,
	*							"billPayout":0,"coinPayout":100,"is_success":1,
	*							"result":1,"bill_changed":0,"coin_changed":100}}
	*							"EV_type"= EV_MDB_PAYOUT = 28: ��ʾMDB�ұҽ����Ӧ������
	*							"port_id":ԭ������,"bill":ԭ������,"coin":ԭ������,"billPayout":ԭ������,"coinPayout":ԭ������,
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"result":�ۿ���	1:�ұҳɹ�     0:�ұ�ʧ��
	*							"bill_changed":ֽ������ǰ�ұҽ��	  �Է�Ϊ��λ
	*							"coin_changed":Ӳ������ǰ�ұҽ��	  �Է�Ϊ��λ
	*********************************************************************************************************/
	public  static int EV_mdbPayout(int port_id,int bill,int coin,int billPay,int coinPay)
	{
		RequestObject req = new RequestObject();
		req.type = EV_MDB_PAYOUT;
		req.fd = port_id;
		req.bill = bill;
		req.coin = coin;
		req.billPay = billPay;
		req.coinPay = coinPay;
		return pushReq(req);
	}
	
	
	
	
	
	
	/*********************************************************************************************************
	 **�������û�ע��ص������Ľӿڣ��û�����ע�Ḵ�ӵĺ������ûص�����java����߳�����
	*********************************************************************************************************/	
	private static Collection<EV_listener> listeners = null;//�����¼���������
	
	
	
	/*********************************************************************************************************
	** Function name:     	addListener
	** Descriptions:	             ע��ص������ӿ� �û���Ҫ�����ص�ֻ��Ҫע�ἴ��
	** input parameters:    EV_listener���ӿ���
	** output parameters:   ��
	** Returned value:      ��
	*********************************************************************************************************/
	public static void addListener(EV_listener listener){
		 if(listeners == null){
			 listeners = new HashSet<EV_listener>();
	     }
		 listeners.add(listener);
	}
	
	
	/*********************************************************************************************************
	** Function name:     	removeListener
	** Descriptions:	             ע��ע��ص������ӿ� �û�����Ҫ�����ص�ֻ��ע������
	** input parameters:    EV_listener���ӿ���
	** output parameters:   ��
	** Returned value:      ��
	*********************************************************************************************************/
	public static void removeListener(EV_listener listener){
		if(listeners != null){
			listeners.remove(listener);
		}
	}
	
	
	/*********************************************************************************************************
	** Function name:     	notifyListeners
	** Descriptions:	           �ڲ�ִ�лص������
	** input parameters:    String��JNI��Ӧ��json��
	** output parameters:   ��
	** Returned value:      ��
	*********************************************************************************************************/
	public static void notifyListeners(String json){
		if(listeners == null){return;}
	    Iterator<EV_listener> iter = listeners.iterator();
	    while(iter.hasNext()){
	    	EV_listener listener = (EV_listener)iter.next();
	        listener.do_json(json);
	    }
	 }
	
	/*********************************************************************************************************
	** Function name:     	EV_listener
	** Descriptions:	           �����¼��ص��ӿ�
	** input parameters:    ��
	** output parameters:   ��
	** Returned value:      ��
	*********************************************************************************************************/
	public interface EV_listener extends EventListener{
	    public void do_json(String json);
	}
	
	
	
	/*********************************************************************************************************
	 **�����Ǳ��ӿ�����ڲ������������ṩ
	*********************************************************************************************************/
	
	/*********************************************************************************************************
	 **������������� �������ӿ�
	*********************************************************************************************************/
	private static Queue<RequestObject> req_list = null;
	public static int pushReq(RequestObject req){
		if(req_list == null){
			req_list = new LinkedList<RequestObject>();
		}
		//System.out.print("pushReq:" + req + "\n\n");
		boolean ok = req_list.offer(req);
		
		if(ev_thread == null){
			ev_thread = new EV_Thread();
			ev_thread.start();
		}
		int ret = (ok == true) ? 1 : 0;
		return ret;
		
	}
	
	public static RequestObject pollReq(){
		if(req_list == null){
			return null;
		}
		else{
			return req_list.poll();
		}
	}
	
	
	public static void req_handle(){
		RequestObject req = pollReq();
		if(req == null){
			//System.out.printf("req_handle:req=null\n");
			return;
		}
		System.out.printf("req_handle:req.type=%d\n", req.type);
		String rptJson = null; 
		switch(req.type){
			case EV_REGISTER:
				rptJson = EVPortRegister(req.portName);
				break;
			case EV_RELEASE:
				rptJson = EVPortRelease(req.fd);
				break;
			case EV_BENTO_OPEN:
				rptJson = EVBentoOpen(req.fd, req.addr, req.box);
				break;
			case EV_BENTO_CHECK:
				rptJson = EVBentoCheck(req.fd, req.addr);
				break;
			case EV_BENTO_LIGHT:
				rptJson = EVBentoLight(req.fd, req.addr,req.opt);
				break;
			case EV_MDB_INIT:
				rptJson = EVmdbInit(req.fd, req.bill,req.coin);
				break;
			case EV_MDB_ENABLE:
				rptJson = EVmdbEnable(req.fd, req.bill,req.coin,req.opt);
				break;
			case EV_MDB_HEART:
				rptJson = EVmdbHeart(req.fd);
				break;
			case EV_MDB_B_INFO:
				rptJson = EVmdbBillInfoCheck(req.fd);
				break;
			case EV_MDB_C_INFO:
				rptJson = EVmdbCoinInfoCheck(req.fd);
				break;
			case EV_MDB_COST:
				rptJson = EVmdbCost(req.fd,req.cost);
				break;
			case EV_MDB_PAYBACK:
				rptJson = EVmdbPayback(req.fd,req.bill,req.coin);
				break;
			case EV_MDB_PAYOUT:
				rptJson = EVmdbPayout(req.fd,req.bill,req.coin,req.billPay,req.coinPay);
				break;
			default:
				rptJson = "";
				break;
		};
		
		if(rptJson != null){
			System.out.println("EV_JSON=" + rptJson);
			notifyListeners(rptJson);
		}
	}
	

	
	
	/*********************************************************************************************************
	 **�ڲ��߳�ר�����ڴ���ص���� ���ڴ����ӵ����� Ӧ�ò���Ծ���ĸ�����Ҫ������
	*********************************************************************************************************/
	private static EV_Thread ev_thread = null;
	private static class EV_Thread extends Thread{
		public void run(){			
			while(true){
					req_handle();
					//System.out.printf("EV_Thread:runing...\n");
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}
	
	
	
	/*********************************************************************************************************
	 **JNI��ӿں�����ע�����º����ӿھ�Ϊ������ʽ �û����������
	*********************************************************************************************************/
	
	
	/*********************************************************************************************************
	** Function name	:		EVPortRegister
	** Descriptions		:		����ע��ӿ�  [ͬ��]
	** input parameters	:       portName ���ں� ����"COM1"
	** output parameters:		��
	** Returned value	:		����json��     ���磺 EV_JSON={"EV_json":{"EV_type":1,"port_id":0}}
	*							"EV_type" = EV_REGISTER = 1; ��ʾ����ע�������
	*							"port_id":��ʾ���صĴ��ڱ�ţ����ʧ���򷵻� -1
	*********************************************************************************************************/
	public  native static String EVPortRegister(String portName);
	
	
	/*********************************************************************************************************
	** Function name	:		EVPortRelease 
	** Descriptions		:		�����ͷŽӿ�  [ͬ��]
	** input parameters	:       port_id ���ڱ��
	** output parameters:		��
	** Returned value	:		����json��     ���磺 EV_JSON={"EV_json":{"EV_type":2,"result":1}}
	*							"EV_type"= EV_RELEASE = 2�� ��ʾ�����ͷŰ�����
	*							"result":��ʾ�������    1:��ʾ�ɹ��ͷ�   0:��ʾ�ͷ�ʧ��
	*********************************************************************************************************/
	public  native static String EVPortRelease(int port_id);
	
	
	
	
	
	
	
	
	/*********************************************************************************************************
	** Function name	:		EVBentoOpen
	** Descriptions		:		��ݹ��Žӿ�  [ͬ��]
	** input parameters	:       port_id:���ڱ��,addr:���ӵ�ַ 00-15,box:���ŵĸ��Ӻ� 1-88
	** output parameters:		��
	** Returned value	:		����json��     ���磺 EV_JSON={"EV_json":{"EV_type":11,"port_id":0,"addr":0,"box":1,"is_success":1,"result":1}}
	*							"EV_type"= EV_BENTO_OPEN = 11: ���Ž����Ӧ������
	*							"port_id":ԭ������,"addr":ԭ�����ع��ӵ�ַ,"box":ԭ�����ع��ڸ��Ӻ�.
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"result": ��ʾ������	1:���ųɹ�   0:����ʧ��
	*********************************************************************************************************/
	public  native static String EVBentoOpen(int port_id,int addr,int box);
	
	
	
	
	
	/*********************************************************************************************************
	** Function name	:		EVBentoCheck
	** Descriptions		:		��ݹ��ѯ�ӿ�  [ͬ��]
	** input parameters	:       port_id:���ڱ��,addr:���ӵ�ַ 00-15
	** output parameters:		��
	** Returned value	:		����json��     ���磺 EV_JSON={"EV_json":{"EV_type":12,"port_id":0,"addr":0,"is_success":1,"ID":"xxxxxxxxx1",
	*								"cool":0,"hot":0,"light":1,"sum":88,[]}}
	*							"EV_type"= EV_BENTO_CHECK = 12: ���ѯ�����Ӧ������
	*							"port_id":ԭ������,"addr":ԭ�����ع��ӵ�ַ
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"ID":�������ID��
	*							"cool":�Ƿ�֧������ 	 	1:֧�� 0:��֧��
	*							"hot":�Ƿ�֧�ּ���  		1:֧�� 0:��֧��
	*							"light":�Ƿ�֧������  	1:֧�� 0:��֧��
	*							"sum":��������	�����������88 ��Ĭ�� ������� 1-88
	*********************************************************************************************************/
	public  native static String EVBentoCheck(int port_id,int addr);
	
	
	
	/*********************************************************************************************************
	** Function name	:		EVBentoLight
	** Descriptions		:		��ݹ��������ƽӿ�  [ͬ��]
	** input parameters	:       port_id:���ڱ��,addr:���ӵ�ַ 00-15,opt:���������� 1:��  0:��
	** output parameters:		��
	** Returned value	:		����json��     ���磺 EV_JSON={"EV_json":{"EV_type":13,"port_id":0,"addr":0,"opt":1,"is_success":1,"result":1}}
	*							"EV_type"= EV_BENTO_LIGHT = 13: �����������Ӧ������
	*							"port_id":ԭ������,"addr":ԭ�����ع��ӵ�ַ,"opt":ԭ�����ز���.
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"result": ��ʾ������	1:�ɹ�   0:ʧ��
	*********************************************************************************************************/
	public  native static String EVBentoLight(int port_id,int addr,int opt);
	
	
	/*********************************************************************************************************
	** Function name	:		EVmdbInit
	** Descriptions		:		MDB��ʼ���ӿ�  [ͬ��]
	** input parameters	:       port_id:���ڱ��,bill:����ֽ����  1:����,0:������,coin:����Ӳ����  1:����,0:������
	** output parameters:		��
	** Returned value	:		����json��     ���磺 EV_JSON={"EV_json":{"EV_type":21,"port_id":0,"bill":1,"coin":1,"is_success":1,"bill_result":1,"coin_result":1}}
	*							"EV_type"= EV_MDB_INIT = 21: ��ʾMDB��ʼ�������Ӧ������
	*							"port_id":ԭ������,"bill":ԭ�����ع��ӵ�ַ,"coin":ԭ�����ز���.
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"bill_result": ֽ����������	1:�ɹ�   0:ʧ��
	*							"coin_result": ֽ����������	1:�ɹ�   0:ʧ��
	*********************************************************************************************************/
	public  native static String EVmdbInit(int port_id,int bill,int coin);
	
	
	
	
	
	/*********************************************************************************************************
	** Function name	:		EVmdbEnable
	** Descriptions		:		MDBʹ�ܽӿ�  [ͬ��]
	** input parameters	:       port_id:���ڱ��; bill:����ֽ����  1:���� 0:������;coin:����Ӳ����  1:����,0:������ ;opt:���� 1:ʹ�� 0:����
	** output parameters:		��
	** Returned value	:		����json��     ���磺 EV_JSON={"EV_json":{"EV_type":22,"port_id":0,"bill":1,"coin":1,"opt":1,"is_success":1,"bill_result":1,"coin_result":1}}
	*							"EV_type"= EV_MDB_ENABLE = 22: ��ʾMDBʹ�ܽ����Ӧ������
	*							"port_id":ԭ������,"bill":ԭ�����ع��ӵ�ַ,"coin":ԭ�����ز���;"opt":ԭ������
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"bill_result": ֽ����������	1:�ɹ�   0:ʧ��
	*							"coin_result": ֽ����������	1:�ɹ�   0:ʧ��
	*********************************************************************************************************/
	public  native static String EVmdbEnable(int port_id,int bill,int coin,int opt);
	
	
	
	
	/*********************************************************************************************************
	** Function name	:		EVmdbHeart
	** Descriptions		:		MDB������ѯ�ӿ�  [ͬ��]
	** input parameters	:       port_id:���ڱ��;
	** output parameters:		��
	** Returned value	:		����json��     ���磺 EV_JSON={"EV_json":{"EV_type":23,"port_id":0,"is_success":1,
	*							"bill_enable":1,"bill_payback":0,"bill_err":0,"bill_recv":0,"bill_remain":0,
	*							"coin_enable":1,"coin_payback":0,"coin_err":0,"coin_recv":0,"coin_remain":0}}
	*							"EV_type"= EV_MDB_HEART = 23: ��ʾMDB������ѯ�����Ӧ������
	*							"port_id":ԭ������,
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"bill_enable": ֽ����ʹ��״̬		1:ʹ��   0:����
	*							"bill_payback": ֽ�����˱Ұ�ť����	1:����   0:�Ǵ���
	*							"bill_err":ֽ��������״̬			0:����   ��0 Ϊ������
	*							"bill_recv":ֽ������ǰ�ձҽ��	�Է�Ϊ��λ
	*							"bill_remain":ֽ������ǰ���ҽ��	�Է�Ϊ��λ
	*	*						"coin_enable": Ӳ����ʹ��״̬		1:ʹ��   0:����
	*							"coin_payback": Ӳ�����˱Ұ�ť����	1:����   0:�Ǵ���
	*							"coin_err":Ӳ��������״̬			0:����   ��0 Ϊ������
	*							"coin_recv":Ӳ������ǰ�ձҽ��	�Է�Ϊ��λ
	*							"coin_remain":Ӳ������ǰ���ҽ��	�Է�Ϊ��λ
	*********************************************************************************************************/
	public  native static String EVmdbHeart(int port_id);
	
	
	
	
	
	/*********************************************************************************************************
	** Function name	:		EVmdbBillInfoCheck
	** Descriptions		:		MDBֽ������ѯ�ӿ�  [ͬ��]
	** input parameters	:       port_id:���ڱ��;
	** output parameters:		��
	** Returned value	:		����json��     ���磺 EV_JSON={"EV_json":{"EV_type":24,"port_id":0,"is_success":1,
	*							"acceptor":2,"dispenser":2,"code":"ITL","sn":"12312....","model":"NV9",
	*							"ver":"1212","capacity":500,"ch_r":[],"ch_d":[]}}
	*							"EV_type"= EV_MDB_B_INFO = 24: ��ʾMDBֽ������Ϣ��ѯ�����Ӧ������
	*							"port_id":ԭ������,
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"acceptor": ֽ�ҽ�����Э������	0:�������ر�	2:MDBЭ�������
	*							"dispenser": ֽ��������Э������	0:�������ر�	2:MDBЭ��������
	*							"code": ֽ�������̴���			���磺ITL
	*							"sn":ֽ�������к�
	*							"model":ֽ�����ͺ�				����:NV9
	*							ver:ֽ��������汾��
	*							capacity:ֽ����������	
	*							ch_r:ֽ����������ͨ����ֵ 		�Է�Ϊ��λ
	*							ch_d:ֽ����������ͨ����ֵ		�Է�Ϊ��λ
	*********************************************************************************************************/
	public  native static String EVmdbBillInfoCheck(int port_id);
	
	
	
	
	/*********************************************************************************************************
	** Function name	:		EVmdbCoinInfoCheck
	** Descriptions		:		MDBӲ������Ϣ��ѯ�ӿ�  [ͬ��]
	** input parameters	:       port_id:���ڱ��;
	** output parameters:		��
	** Returned value	:		����json��     ���磺 EV_JSON={"EV_json":{"EV_type":25,"port_id":0,"is_success":1,
	*							"acceptor":2,"dispenser":2,"code":"MEI","sn":"12312....","model":"***",
	*							"ver":"1212","capacity":500,"ch_r":[],"ch_d":[]}}
	*							"EV_type"= EV_MDB_C_INFO = 25: ��ʾMDBӲ������Ϣ��ѯ�����Ӧ������
	*							"port_id":ԭ������,
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"acceptor": Ӳ�ҽ�����Э������	0:�������ر�	1:����Ӳ������Э��  2:MDBЭ������� 3:����Ӳ������Э��
	*							"dispenser": Ӳ��������Э������	0:�������ر�	1:hopper ����232Э��  2:MDBЭ��������
	*							"code": Ӳ�������̴���			���磺MEI
	*							"sn":Ӳ�������к�
	*							"model":Ӳ�����ͺ�			
	*							ver:Ӳ��������汾��
	*							capacity:Ӳ����������	
	*							ch_r:Ӳ����������ͨ����ֵ 		�Է�Ϊ��λ
	*							ch_d:Ӳ����������ͨ����ֵ		�Է�Ϊ��λ
	*********************************************************************************************************/
	public  native static String EVmdbCoinInfoCheck(int port_id);
	
	
	
	
	
	/*********************************************************************************************************
	** Function name	:		EVmdbCost
	** Descriptions		:		MDB�ۿ�ӿ�  [ͬ��]
	** input parameters	:       port_id:���ڱ��;cost:�ۿ���  �Է�Ϊ��λ
	** output parameters:		��
	** Returned value	:		����json��     ���磺 EV_JSON={"EV_json":{"EV_type":26,"port_id":0,"cost":100,"is_success":1,
	*							"result":1,"bill_recv":0,"coin_recv":0}}
	*							"EV_type"= EV_MDB_COST = 26: ��ʾMDB�ۿ�����Ӧ������
	*							"port_id":ԭ������,
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"result":�ۿ���	1:�ۿ�ɹ�     0:�ۿ�ʧ��
	*							"bill_recv":ֽ������ǰ�ձҽ��	  �Է�Ϊ��λ
	*							"coin_recv":Ӳ������ǰ�ձҽ��	  �Է�Ϊ��λ
	*********************************************************************************************************/
	public  native static String EVmdbCost(int port_id,int cost);
	
	
	
	/*********************************************************************************************************
	** Function name	:		EVmdbPayback
	** Descriptions		:		MDB�˱ҽӿ�  [ͬ��]
	** input parameters	:       port_id:���ڱ��;bill:����ֽ����  1:����,0:������,coin:����Ӳ����  1:����,0:������
	** output parameters:		��
	** Returned value	:		����json��     ���磺 EV_JSON={"EV_json":{"EV_type":26,"port_id":0,"bill":1,"coin":1,"is_success":1,
	*							"result":1,"bill_changed":0,"coin_changed":100}}
	*							"EV_type"= EV_MDB_COST = 26: ��ʾMDB�˱ҽ����Ӧ������
	*							"port_id":ԭ������,"bill":ԭ������,"coin":ԭ������,
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"result":�ۿ���	1:�˱ҳɹ�     0:�˱�ʧ��
	*							"bill_changed":ֽ������ǰ�ұҽ��	  �Է�Ϊ��λ
	*							"coin_changed":Ӳ������ǰ�ұҽ��	  �Է�Ϊ��λ
	*********************************************************************************************************/
	public  native static String EVmdbPayback(int port_id,int bill,int coin);
	
	
	
	/*********************************************************************************************************
	** Function name	:		EVmdbPayout
	** Descriptions		:		MDB�ұҽӿ�  [ͬ��]
	** input parameters	:       port_id:���ڱ��;bill:����ֽ����  1:����,0:������,coin:����Ӳ����  1:����,0:������;
	*							billPayout:ֽ�����·��ұҽ�� ��Ϊ��λ ;coinPayout:Ӳ�����·��ұҽ�� ��Ϊ��λ 
	** output parameters:		��
	** Returned value	:		����json��     ���磺 EV_JSON={"EV_json":{"EV_type":26,"port_id":0,"bill":0,"coin":1,
	*							"billPayout":0,"coinPayout":100,"is_success":1,
	*							"result":1,"bill_changed":0,"coin_changed":100}}
	*							"EV_type"= EV_MDB_COST = 26: ��ʾMDB�ұҽ����Ӧ������
	*							"port_id":ԭ������,"bill":ԭ������,"coin":ԭ������,"billPayout":ԭ������,"coinPayout":ԭ������,
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"result":�ۿ���	1:�ұҳɹ�     0:�ұ�ʧ��
	*							"bill_changed":ֽ������ǰ�ұҽ��	  �Է�Ϊ��λ
	*							"coin_changed":Ӳ������ǰ�ұҽ��	  �Է�Ϊ��λ
	*********************************************************************************************************/
	public  native static String EVmdbPayout(int port_id,int bill,int coin,int billPay,int coinPay);
	
	
	
}