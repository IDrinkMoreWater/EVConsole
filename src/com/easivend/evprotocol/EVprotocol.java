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
			ev_thread = new EV_Thread(this);
			ev_thread.start();
		}
		ev = this;//����й�����ָ�봫�ݸ���̬����
	} 
	
	private static EVprotocol ev = null;
	public static EVprotocol obtain(){ //��Լ�ڴ�����
		if(ev == null){
			ev = new EVprotocol();
		}
		return ev;
	}
	
	//����JNI��̬���ӿ�
	static{
		System.loadLibrary("EVprotocol");
		
	}
	
	
	/*********************************************************************************************************
	** Function name:     	EV_callBack
	** Descriptions:	    VMC�ص���ڣ����нϳ��Ľ����Ӧ����ͨ���ú����ص���ʾ��
	** input parameters:    json_msg:��Ӧ�����JSON���� 
	** output parameters:   ��
	** Returned value:      ��
	*********************************************************************************************************/
	public static void EV_callBack(String json_msg)
	{
		System.out.println("JSON:"+json_msg);		
		addMsg(json_msg);
	}

	
	/*********************************************************************************************************
	** Function name:     	vmcStart
	** Descriptions:	    VMC���ذ忪���ӿ�
	** input parameters:    portName ���ں� ����/dev/tty0
	** output parameters:   ��
	** Returned value:      1:�����ɹ�      -1:����ʧ��  ��ֱ�ӷ��� �����лص���
	*********************************************************************************************************/
	public  native int vmcStart(String portName);
	
	
	
	
	
	/*********************************************************************************************************
	** Function name:     	vmcStop
	** Descriptions:	    VMC���ذ�Ͽ��ӿ� ��vmcStart����ʹ�� һ������������
	** input parameters:    ��
	** output parameters:   ��
	** Returned value:      �ޣ�ֱ�ӷ��� �����лص���
	*********************************************************************************************************/
	public  native void vmcStop();
	
	
	/*********************************************************************************************************
	** Function name:     	trade
	** Descriptions:	    VMC�����ӿ�  
	**						PC���͸�ָ��������жϷ���ֵΪ1�������ͳɹ���Ȼ��ͨ���ص��������س����Ľ�����н���
	**						�ص�JSON�� ��ʽ 
	** input parameters:    cabinet:���  column:������   type:֧����ʽ  type= 0 :�ֽ�  type = 1  ���ֽ�    
	**					    cost:�ۿ���(��λ:�� ;���type=1���ֵ����Ϊ0)
	** output parameters:   ��
	** Returned value:      1�����ͳɹ�   0:������ʧ��  
	*********************************************************************************************************/
	public  native int trade(int cabinet,int column,int type,int cost);
	
	
	
	/*********************************************************************************************************
	** Function name:     	payout
	** Descriptions:	    VMC���ҽӿ�
	**						PC���͸�ָ��������жϷ���ֵΪ1�������ͳɹ���Ȼ��ͨ���ص��������س����Ľ�����н���
	** input parameters:    value:Ҫ���ҵĽ��(��λ:��)
	** output parameters:   ��
	** Returned value:      1�����ͳɹ�   0:������ʧ��
	*********************************************************************************************************/
	public  native int payout(long value);
	
	
	
	/*********************************************************************************************************
	** Function name:     	getStatus
	** Descriptions:	             ��ȡVMC״̬�ӿ�  PC���͸�ָ��������жϷ���ֵΪ1�������ͳɹ���Ȼ��ͨ���ص��������س����Ľ�����н���
	** input parameters:    ��
	** output parameters:   ��
	** Returned value:      1�����ͳɹ�   0:������ʧ��
	*********************************************************************************************************/
	public  native int getStatus();
	
	
	/*********************************************************************************************************
	** Function name:     	getRemainAmount
	** Descriptions:	             ��ȡVMCͶ�����  �����лص� ֱ�ӷ��ص�ǰ��� 
	** input parameters:    ��
	** output parameters:   ��
	** Returned value:      ���ص�ǰ��� ��λ:��
	*********************************************************************************************************/
	public  native long getRemainAmount();
	
	
	
	/*********************************************************************************************************
	** Function name:     	cashControl
	** Descriptions:	             �����ֽ��豸 ֱ�ӷ��� �����лص�
	** input parameters:    flag 1:�����ֽ��豸  0�ر��ֽ��豸
	** output parameters:   ��
	** Returned value:      1�ɹ�  0ʧ��
	*********************************************************************************************************/
	public  native int cashControl(int flag);
	
	
	
	/*********************************************************************************************************
	** Function name:     	setDate
	** Descriptions:	             ������λ��ʱ�� ֱ�ӷ��� �����лص�
	** input parameters:    date ���� ��ʽ"2014-10-10 12:24:24"
	** output parameters:   ��
	** Returned value:      1�ɹ�  0ʧ��
	*********************************************************************************************************/
	public  native int setDate(String date);
	
	
	/*********************************************************************************************************
	** Function name:     	cabinetControl
	** Descriptions:	            �����豸���� ֱ�ӷ��� �����лص�
	** input parameters:    cabinet ��ţ�1:��1  2:��2��  dev �豸(1:����   2:���� 3:����  4:����)       flag 1��  0��
	** output parameters:   ��
	** Returned value:      1�ɹ�  0ʧ��
	*********************************************************************************************************/
	public  native int cabinetControl(int cabinet,int dev,int flag);
	
	
	
	
	
	
	/*********************************************************************************************************
	** Function name:     	bentoRegister
	** Descriptions:	             �������ʼ���ӿ�
	** input parameters:    portName ���ں� ����/dev/tty0
	** output parameters:   ��
	** Returned value:      1:��ʼ���ɹ�      -1:��ʼ��ʧ�� (ʧ��ԭ��Ϊ���ڴ�ʧ��)
	*********************************************************************************************************/
	public  native int bentoRegister(String portName);
	
	
	
	/*********************************************************************************************************
	** Function name:     	bentoRelease
	** Descriptions:	             �������ͷ���Դ�ӿڣ���bentoRegister���׵��ã�
	** input parameters:    ��
	** output parameters:   ��
	** Returned value:      1:�ɹ�      �����ñض��ɹ���
	*********************************************************************************************************/
	public  native int bentoRelease();
	

	/*********************************************************************************************************
	** Function name:     	bentoOpen
	** Descriptions:	             �����񿪸��ӽӿ�
	** input parameters:    cabinet:���     box:���Ӻ�
	** output parameters:   ��
	** Returned value:      1:�򿪳ɹ�      0:��ʧ��
	*********************************************************************************************************/
	public  native int bentoOpen(int cabinet,int box);
	
	
	
	/*********************************************************************************************************
	** Function name:     	bentoLight
	** Descriptions:	             �������������ƽӿ�
	** input parameters:    cabinet:���     flag:1 ��ʾ������    0:��ʾ������
	** output parameters:   ��
	** Returned value:      1:�򿪳ɹ�      0:��ʧ��
	*********************************************************************************************************/
	public  native int bentoLight(int cabinet,int flag);//flag 1����  0�ص�
	
	
	/*********************************************************************************************************
	** Function name:     	bentoCheck
	** Descriptions:	             ������״̬��ѯ�ӿ�
	** input parameters:    cabinet:���     
	** output parameters:   ��
	** Returned value:      ���ص���һ��JSON�� ����{{}}
	*********************************************************************************************************/
	public  native String bentoCheck(int cabinet);
	
	
	
	
	
	
	
	
	
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
	public void addListener(EV_listener listener){
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
	public void removeListener(EV_listener listener){
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
	    Iterator<EV_listener> iter = listeners.iterator();
	    while(iter.hasNext())
	    {
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
	private static Queue<String> queue_json = null;
	public  static void addMsg(String json){
		if(queue_json == null)
			queue_json = new LinkedList<String>();
		queue_json.offer(json); 
	}
	
	public String pollMsg(){
		if(queue_json != null)
			return queue_json.poll();
		else
			return null;
	}
	
	public static void EV_msg_handle(){
		if(queue_json == null)
			return;
		if(listeners != null){
			if(listeners.isEmpty() == false){
				String json = queue_json.poll();
				if(json != null)
					notifyListeners(json);
				return;
			}
		}
		else{
			if(queue_json.size() >= 50){//û��ע���¼����� ��Ĭ�ϱ�����Ϣ50��
				queue_json.poll();
			}	
		}

		

		
				
	}
	
	
	/*********************************************************************************************************
	 **�ڲ��߳�ר�����ڴ���ص���� ���ڴ����ӵ����� Ӧ�ò���Ծ���ĸ�����Ҫ������
	*********************************************************************************************************/
	private static EV_Thread ev_thread = null;
	private static class EV_Thread extends Thread{
		public EV_Thread(EVprotocol ev){
			this.ev = ev;
		}
		private EVprotocol ev = null;
		public void run(){			
			while(true){
					EV_msg_handle();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}
	
	
	
	//JNI ��̬�ص����� ʾ������ û����
//	public static void EV_callBackStatic(int i) 
//	{
//		System.out.println("EV_callBackStatic...");
//	}
	
}