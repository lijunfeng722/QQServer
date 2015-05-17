package com.way.chat.common.bean;

import java.io.Serializable;

/**
 * 文本消息
 * 
 * @author way
 */
public class TextMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	private byte[] voiceByte;
	private int messageType;
	private byte[] ImageByte;

	public TextMessage() {

	}

	public TextMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public byte[] getVoiceByte()
	{
		return voiceByte;
	}

	public void setVoiceByte(byte[] voiceByte)
	{
		this.voiceByte = voiceByte;
	}

	public int getMessageType()
	{
		return messageType;
	}

	public void setMessageType(int messageType)
	{
		this.messageType = messageType;
	}
}
