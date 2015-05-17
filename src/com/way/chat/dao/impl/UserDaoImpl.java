package com.way.chat.dao.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.way.chat.common.bean.User;
import com.way.chat.common.util.Constants;
import com.way.chat.common.util.DButil;
import com.way.chat.common.util.MyDate;
import com.way.chat.dao.UserDao;

public class UserDaoImpl<Bitmap> implements UserDao {

	@Override
	public int register(User u) throws IOException {
		int id;
		Connection con = DButil.connect();
		String sql1 = "insert into user(_name,_password,_email,_time, _img) values(?,?,?,?,?)";
		String sql2 = "select _id from user";

		try {
			PreparedStatement ps = con.prepareStatement(sql1);
			ps.setString(1, u.getName());
			ps.setString(2, u.getPassword());
			ps.setString(3, u.getEmail());
			ps.setString(4, MyDate.getDateCN());
	//		ps.setBinaryStream(1, in, (int) file.length()); 
			byte[] in=u.getImg();
			System.out.println(in);
			ps.setBytes(5, in);
			int res = ps.executeUpdate();
			if (res > 0) {
				PreparedStatement ps2 = con.prepareStatement(sql2);
				ResultSet rs = ps2.executeQuery();
				if (rs.last()) {
					id = rs.getInt("_id");
					createFriendtable(id);// ע��ɹ��󣬴���һ�����û�idΪ�����ı����ڴ�ź�����Ϣ
					return id;
				}
			}
		} catch (SQLException e) {
			 e.printStackTrace();
		} finally {
			DButil.close(con);
		}
		return Constants.REGISTER_FAIL;
	}

	@Override
	public ArrayList<User> login(User u) {
		Connection con = DButil.connect();
		String sql = "select * from user where _id=? and _password=?";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, u.getId());
			ps.setString(2, u.getPassword());
			ResultSet rs = ps.executeQuery();
			if (rs.first()) {
				setOnline(u.getId());// ���±�״̬Ϊ����
				ArrayList<User> refreshList = refresh(u.getId());
				return refreshList;
			}
			else {
		//		System.out.println("null");
				return null;
				
			}
		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			DButil.close(con);
		}
		return null;
	}
	
	

	/**
	 * �����Լ�
	 */
	public User findMe(int id) {
		User me = new User();
		Connection con = DButil.connect();
		String sql = "select * from user where _id=?";
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.first()) {
				me.setId(rs.getInt("_id"));
				me.setEmail(rs.getString("_email"));
				me.setName(rs.getString("_name"));
				me.setImg(rs.getBytes("_img"));
			}
			return me;
		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			DButil.close(con);
		}
		return null;
	}
	
	
	
	public User findFriend(int id) {
		User me = new User();
		Connection con = DButil.connect();
		String sql = "select * from user where _id=?";
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs==null)
				return null;
			if (rs.first()) {
				me.setId(rs.getInt("_id"));
				me.setEmail(rs.getString("_email"));
				me.setName(rs.getString("_name"));
				me.setImg(rs.getBytes("_img"));
			}
			return me;
		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			DButil.close(con);
		}
		return null;
	}
	
	public void AddFriend(User temp, int myId) {
	//	System.out.println(temp);
		User to = new User();
		Connection con = DButil.connect();
		to=findMe(temp.getId());
		System.out.println(to);
		try {
			String sql1 = "insert into _? (_name,_img,_qq,_group)values(?,?,?,?)";
			PreparedStatement ps;
		//	System.out.println("num1");
			ps = con.prepareStatement(sql1);
			ps.setInt(1, myId);
			ps.setString(2,to.getName());
			ps.setBytes(3,to.getImg());
			ps.setInt(4,to.getId());
			ps.setInt(5,1);
			ps.executeUpdate();
			String sql2 = "insert into _? (_name,_img,_qq,_group)values(?,?,?,?)";
			PreparedStatement ps1;
			ps1 = con.prepareStatement(sql2);
			ps1.setInt(1, to.getId());
			to=findMe(myId);
			ps1.setString(2,to.getName());
			ps1.setBytes(3,to.getImg());
			ps1.setInt(4,to.getId());
			ps1.setInt(5,1);
			ps1.executeUpdate();
		/*	ResultSet rs = ps.executeQuery();
			if (rs.first()) {
				me.setId(rs.getInt("_id"));
				me.setEmail(rs.getString("_email"));
				me.setName(rs.getString("_name"));
				me.setImg(rs.getInt("_img"));
			}
			return me;*/
		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			DButil.close(con);
		}
	//	System.out.println("add friend successful!");
	}
	
	
	public void DeleteFriend(User temp, int myId) {
			Connection con = DButil.connect();
		//	System.out.println(to);
			try {
				String sql1 = "delete from _? where _qq = ?";
				PreparedStatement ps;
			//	System.out.println("num1");
				ps = con.prepareStatement(sql1);
				ps.setInt(1, myId);
				ps.setInt(2,temp.getId());
			//	System.out.println(ps);
				ps.executeUpdate();
				String sql2 = "delete from _? where _qq = ?";
				PreparedStatement ps1;
				ps1 = con.prepareStatement(sql2);
				ps1.setInt(1, temp.getId());
				ps1.setInt(2,myId);
			//	System.out.println(ps1);
				ps1.executeUpdate();
			} catch (SQLException e) {
				// e.printStackTrace();
			} finally {
				DButil.close(con);
			}
		}

	/**
	 * ˢ�º����б�
	 */
	public ArrayList<User> refresh(int id) {
		ArrayList<User> list = new ArrayList<User>();
		User me = findMe(id);
		list.add(me);// ������Լ�
		Connection con = DButil.connect();
		String sql = "select * from _? ";
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.first()) {
				do {
					User friend = new User();
					friend.setId(rs.getInt("_qq"));
					friend.setName(rs.getString("_name"));
					friend.setIsOnline(rs.getInt("_isOnline"));
					friend.setImg(rs.getBytes("_img"));
					friend.setGroup(rs.getInt("_group"));
					list.add(friend);
				} while (rs.next());
			}
			return list;
		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			DButil.close(con);
		}
		return null;
	}

	/**
	 * ����״̬Ϊ����
	 * 
	 * @param id
	 */
	public void setOnline(int id) {
		Connection con = DButil.connect();
		try {
			String sql = "update user set _isOnline=1 where _id=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ps.executeUpdate();
			updateAllOn(id);// �������б�״̬Ϊ����
		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			DButil.close(con);
		}
	}

	/**
	 * ע��ɹ��󣬴���һ���û���������û�����
	 * 
	 * @param id
	 */
	public void createFriendtable(int id) {
		Connection con = DButil.connect();
		try {
			String sql = "create table _" + id
					+ " (_id int auto_increment not null primary key,"
					+ "_name varchar(20) not null,"
					+ "_isOnline int(11) not null default 0,"
					+ "_group int(11) not null default 0,"
					+ "_qq int(11) not null default 0,"
					+ "_img mediumblob not null)";
			PreparedStatement ps = con.prepareStatement(sql);
			int res = ps.executeUpdate();
			System.out.println(res);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DButil.close(con);
		}
	}

	@Override
	/**
	 * ���߸���״̬Ϊ����
	 */
	public void logout(int id) {
		Connection con = DButil.connect();
		try {
			String sql = "update user set _isOnline=0 where _id=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ps.executeUpdate();
			updateAllOff(id);
			// System.out.println(res);
		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			DButil.close(con);
		}
	}

	/**
	 * ���������û���״̬Ϊ����
	 * 
	 * @param id
	 */
	public void updateAllOff(int id) {
		Connection con = DButil.connect();
		try {
			String sql = "update _? set _isOnline=0 where _qq=?";
			PreparedStatement ps = con.prepareStatement(sql);
			for (int offId : getAllId()) {
				ps.setInt(1, offId);
				ps.setInt(2, id);
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			DButil.close(con);
		}
	}

	/**
	 * ���������û�״̬Ϊ����
	 * 
	 * @param id
	 */
	public void updateAllOn(int id) {
		Connection con = DButil.connect();
		try {
			String sql = "update _? set _isOnline=1 where _qq=?";
			PreparedStatement ps = con.prepareStatement(sql);
			for (int OnId : getAllId()) {
				ps.setInt(1, OnId);
				ps.setInt(2, id);
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			DButil.close(con);
		}
	}

	public List<Integer> getAllId() {
		Connection con = DButil.connect();
		List<Integer> list = new ArrayList<Integer>();
		try {
			String sql = "select _id from user";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.first()) {
				do {
					int id = rs.getInt("_id");
					list.add(id);
				} while (rs.next());
			}
			// System.out.println(list);
			return list;
		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			DButil.close(con);
		}
		return null;
	}

/*	public static void main(String[] args) {
		User u = new User();
		UserDaoImpl dao = new UserDaoImpl();
		// u.setId(2016);
		// u.setName("qq");
		// u.setPassword("123");
		// u.setEmail("158342219@qq.com");
		// System.out.println(dao.register(u));
		// // System.out.println(dao.login(u));
		// // dao.logout(2016);
		// dao.setOnline(2016);
		// // dao.getAllId();
		List<User> list = dao.refresh(2016);
		System.out.println(list);

	}
	*/

}
