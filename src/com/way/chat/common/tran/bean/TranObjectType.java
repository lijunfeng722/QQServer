package com.way.chat.common.tran.bean;

/**
 * �����������
 * 
 * @author way
 * 
 */
public enum TranObjectType {
	REGISTER, // ע��
	LOGIN, // �û���¼
	LOGOUT, // �û��˳���¼
	FRIENDLOGIN, // ��������
	FRIENDLOGOUT, // ��������
	MESSAGE, // �û�������Ϣ
	UNCONNECTED, // �޷�����
	FILE, // �����ļ�
	REFRESH,//ˢ�º����б�
	FriendCheck,//��ѯ������Ϣ
	FriendAdd,//���Ӻ�����Ϣ
	FriendDelete,//ɾ���ú���
	AddFriendMsg, //���Ӻ���������Ϣ
}