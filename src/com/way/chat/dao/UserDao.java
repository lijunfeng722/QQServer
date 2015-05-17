package com.way.chat.dao;

import java.io.IOException;
import java.util.ArrayList;

import com.way.chat.common.bean.User;

public interface UserDao {
	//ע��ɹ������û�id
	public int register(User u) throws IOException;

	public ArrayList<User> login(User u);

	public ArrayList<User> refresh(int id);
	public void logout(int id);
	public User findFriend(int id);
	public void AddFriend(User temp, int myId);
	public void DeleteFriend(User temp, int myId);
}
