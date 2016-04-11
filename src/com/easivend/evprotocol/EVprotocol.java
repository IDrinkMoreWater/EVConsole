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
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.easivend.common.ToolClass;
import com.google.gson.JsonArray;




/*********************************************************************************************************
**���� EVprotocol �ӿڷ�װ��
*********************************************************************************************************/
public class EVprotocol {
	public EVprotocol(){		//���캯�� Ĭ�Ͽ����̴߳���ص�����
		
	} 
	
	
	
	//����JNI��̬���ӿ�
	static{
		System.loadLibrary("EVdirver");
		
	}
	
	
	
	
	
	/*********************************************************************************************************
	 **�������������
	*********************************************************************************************************/
	public static final int EV_NONE 		= 0;	//��������
	public static final int EV_REGISTER 	= 1;	//����ע��
	public static final int EV_RELEASE 	= 2;	//�����ͷ�
	
	//=====================��ݹ�����==============================================================================
	public static final int EV_BENTO_OPEN 	= 11;	//��ݹ���
	public static final int EV_BENTO_CHECK = 12;	//��ݹ��ѯ
	public static final int EV_BENTO_LIGHT = 13;	//��ݹ�����
	public static final int EV_BENTO_COOL 	= 14;	//��ݹ�����
	public static final int EV_BENTO_HOT 	= 15;	//��ݹ����
	//=====================��������==============================================================================
	public static final int EV_COLUMN_OPEN  = 16;	//��������
	public static final int EV_COLUMN_CHECK = 17;	//������ѯ
	
	//=====================MDB�ֽ�ģ������==============================================================================
	public static final int EV_MDB_INIT 	= 21;	//MDB�豸��ʼ��
	public static final int EV_MDB_ENABLE 	= 22;	//MDB�豸ʹ��
	public static final int EV_MDB_HEART 	= 23;	//MDB�豸����
	public static final int EV_MDB_B_INFO 	= 24;	//MDBֽ������Ϣ
	public static final int EV_MDB_C_INFO 	= 25;	//MDBӲ������Ϣ
	public static final int EV_MDB_COST 	= 26;	//MDB�豸�ۿ�
	public static final int EV_MDB_PAYBACK = 27;	//MDB�豸�˱�
	public static final int EV_MDB_PAYOUT 	= 28;	//MDB�豸�ұ�
	public static final int EV_MDB_B_CON 	= 29;	//MDBֽ��������
	public static final int EV_MDB_C_CON 	= 30;	//MDBӲ��������
	public static final int EV_MDB_HP_PAYOUT 	= 31;	//hopperӲ��������

	
	
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
	** input parameters	:       port_id:���ڱ��,addr:���ӵ�ַ 01-16,box:���ŵĸ��Ӻ� 1-88
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
	** input parameters	:       port_id:���ڱ��,addr:���ӵ�ַ 01-16
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
	** input parameters	:       port_id:���ڱ��,addr:���ӵ�ַ 01-16,opt:���������� 1:��  0:��
	** output parameters:		��
	** Returned value	:		����json��     ���磺 EV_JSON={"EV_json":{"EV_type":13,"port_id":0,"addr":0,"opt":1,"is_success":1,"result":1}}
	*							"EV_type"= EV_BENTO_LIGHT = 13: �����������Ӧ������
	*							"port_id":ԭ������,"addr":ԭ�����ع��ӵ�ַ,"opt":ԭ�����ز���.
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"result": ��ʾ������	1:�ɹ�   0:ʧ��
	*********************************************************************************************************/
	public  native static String EVBentoLight(int port_id,int addr,int opt);
	
	
	/*********************************************************************************************************
	** Function name	:		trade
	** Descriptions		:		��ͨ������ӿ�  [�첽]
	** input parameters	:       fd:���ڱ��, columntype:��������1���ɣ�3����̨+���ʹ���4����̨+����
	*                          ,addr:���ӵ�ַ 01-16,box:���ŵĸ��Ӻ� 1-88,goc=1��������ȷ��,0�ر�
	** output parameters:		��
	** Returned value	:		1�����ͳɹ�  0������ʧ��
	*	����json��     ���磺 EV_JSON={"EV_json":{"EV_type":16,"port_id":2,"addr":1,"box":34,"is_success":1,"result":0}}
	*							"EV_type"= EV_COLUMN_OPEN = 16; ���ɳ��������Ӧ������
	*							"port_id":ԭ������,
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"result": 	��ʾ������	
	*********************************************************************************************************/
	public  native static String EVtrade(int port_id,int columntype,int addr,int box,int goc);
	
	
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
	*							"coin_result": Ӳ����������	1:�ɹ�   0:ʧ��
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
	** Returned value	:		����json��     ���磺 EV_JSON={"EV_json":{"EV_type":27,"port_id":0,"bill":1,"coin":1,"is_success":1,
	*							"result":1,"bill_changed":0,"coin_changed":100}}
	*							"EV_type"= EV_MDB_PAYBACK = 27: ��ʾMDB�˱ҽ����Ӧ������
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
	** Returned value	:		����json��     ���磺 EV_JSON={"EV_json":{"EV_type":28,"port_id":0,"bill":0,"coin":1,
	*							"billPayout":0,"coinPayout":100,"is_success":1,
	*							"result":1,"bill_changed":0,"coin_changed":100}}
	*							"EV_type"= EV_MDB_PAYOUT = 28: ��ʾMDB�ұҽ����Ӧ������
	*							"port_id":ԭ������,"bill":ԭ������,"coin":ԭ������,"billPayout":ԭ������,"coinPayout":ԭ������,
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"result":�ۿ���	1:�ұҳɹ�     0:�ұ�ʧ��
	*							"bill_changed":ֽ������ǰ�ұҽ��	  �Է�Ϊ��λ
	*							"coin_changed":Ӳ������ǰ�ұҽ��	  �Է�Ϊ��λ
	*********************************************************************************************************/
	public  native static String EVmdbPayout(int port_id,int bill,int coin,int billPay,int coinPay);
	
	
	/*********************************************************************************************************
	** Function name	:		EVmdbBillConfig
	** Descriptions		:		MDBֽ��������  [ͬ��]
	** input parameters	:       port_id:���ڱ��;req:���ò��� json��
 								EV_JSON={"EV_json":{"EV_type":29,"port_id":0,"acceptor":2,"dispenser":2,
 								"ch_r":[{"ch":1,"value":100},{"ch":2,"value":500},{"ch":3,"value":1000},{"ch":4,"value":2000},
 										{"ch":5,"value":0},{"ch":6,"value":0},{"ch":7,"value":0},{"ch":8,"value":0},
 										{"ch":9,"value":0},{"ch":10,"value":0},{"ch":11,"value":0},{"ch":12,"value":0},
 										{"ch":13,"value":0},{"ch":14,"value":0},{"ch":15,"value":0},{"ch":16,"value":0}],
 								"ch_d":[{"ch":1,"value":0},{"ch":2,"value":0},{"ch":3,"value":0},{"ch":4,"value":0},
 										{"ch":5,"value":0},{"ch":6,"value":0},{"ch":7,"value":0},{"ch":8,"value":0},
 										{"ch":9,"value":0},{"ch":10,"value":0},{"ch":11,"value":0},{"ch":12,"value":0},
 										{"ch":13,"value":0},{"ch":14,"value":0},{"ch":15,"value":0},{"ch":16,"value":0}]}}
	** output parameters:		��
	** Returned value	:		����json��     ���磺 EV_JSON={"EV_json":{"EV_type":29,"port_id":0,"acceptor":0,"dispenser":1,
	*							"is_success":1,"result":1}}
	*							"EV_type"= EV_MDB_B_CON = 29: ��ʾMDBֽ�����ý����Ӧ������
	*							"port_id":ԭ������,
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"result":�ۿ���	1:�ɹ�     0:ʧ��			
	*********************************************************************************************************/
	public  native static String EVmdbBillConfig(String req);
	
	
	
	
	
	
	/*********************************************************************************************************
	** Function name	:		EVmdbCoinConfig
	** Descriptions		:		MDBӲ��������  [ͬ��]
	** input parameters	:       port_id:���ڱ��;req:���ò��� json��
 								EV_JSON={"EV_json":{"EV_type":30,"port_id":0,"acceptor":2,"dispenser":2,"hight_en":0,
 								"ch_r":[{"ch":1,"value":100},{"ch":2,"value":500},{"ch":3,"value":1000},{"ch":4,"value":2000},
 										{"ch":5,"value":0},{"ch":6,"value":0},{"ch":7,"value":0},{"ch":8,"value":0},
 										{"ch":9,"value":0},{"ch":10,"value":0},{"ch":11,"value":0},{"ch":12,"value":0},
 										{"ch":13,"value":0},{"ch":14,"value":0},{"ch":15,"value":0},{"ch":16,"value":0}],
 								"ch_d":[{"ch":1,"value":0},{"ch":2,"value":0},{"ch":3,"value":0},{"ch":4,"value":0},
 										{"ch":5,"value":0},{"ch":6,"value":0},{"ch":7,"value":0},{"ch":8,"value":0},
 										{"ch":9,"value":0},{"ch":10,"value":0},{"ch":11,"value":0},{"ch":12,"value":0},
 										{"ch":13,"value":0},{"ch":14,"value":0},{"ch":15,"value":0},{"ch":16,"value":0}]}}
	** output parameters:		��
	** Returned value	:		����json��     ���磺 EV_JSON={"EV_json":{"EV_type":30,"port_id":0,"acceptor":0,"dispenser":1,
	*							"is_success":1,"result":1}}
	*							"EV_type"= EV_MDB_C_CON = 30: ��ʾMDBӲ�����ý����Ӧ������
	*							"port_id":ԭ������,
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"result":�ۿ���	1:�ɹ�     0:ʧ��			
	*********************************************************************************************************/
	public  native static String EVmdbCoinConfig(String req);
	
	
	/*********************************************************************************************************
	** Function name	:		EVmdbHopperPayout
	** Descriptions		:		MDB�ұҽӿ�  [ͬ��]
	** input parameters	:       port_id:���ڱ��;no:hopper��� 1-8  nums:��Ҫ�ұҵ�ö�� ;
	** output parameters:		��
	** Returned value	:		����json��     ���磺 EV_JSON={"EV_json":{"EV_type":31,"port_id":0,"no":1,"nums":5,
	*							"is_success":1,"result":1,"changed":5}}
	*							"EV_type"= EV_MDB_HP_PAYOUT = 31: ��ʾhopper�ұҽ����Ӧ������
	*							"port_id":ԭ������,"no":ԭ������,"nums":ԭ������,
	*							"is_success":��ʾָ���Ƿ��ͳɹ�,1:���ͳɹ��� 0:����ʧ�ܣ�ͨ�ų�ʱ��
	*							"result":�ۿ���	1:�ұҳɹ�     0:�ұ�ʧ��
	*							"changed":ʵ������ö��
	*********************************************************************************************************/
	public  native static String EVmdbHopperPayout(int port_id,int no,int nums);
	
	
	
	
}