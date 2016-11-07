package com.tarena.fgr.test;

import java.util.List;

import com.tarena.fgr.biz.CalllogManager;
import com.tarena.fgr.biz.ContactManager;
import com.tarena.fgr.biz.SMSManager;
import com.tarena.fgr.db.DBUtil;
import com.tarena.fgr.db.MyDBHelper;
import com.tarena.fgr.entity.Calllog;
import com.tarena.fgr.entity.Contact;
import com.tarena.fgr.entity.Conversation;
import com.tarena.fgr.entity.Sms;
import com.tarena.fgr.utils.LogUtilS;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

/**
 * 单元测试案例
 * 
 * @author 冯国芮 2016年9月30日 09:59:23
 * 
 */
public class MyTestCase extends AndroidTestCase {
	// 右键单机运行.Android JUnit test
	// 测试联系人数据
	public void contactTest() {
		List<Contact> contacts = ContactManager.getContacts(getContext());
		for (Contact contact : contacts) {
			LogUtilS.i("TAG", "" + contact);
		}
	}

	// 测试通话记录数据
	public void calllogTest() {
		List<Calllog> calllogs = CalllogManager.getCalllogs(getContext());
		for (Calllog calllog : calllogs) {
			LogUtilS.i("TAG", "" + calllog);
		}
	}

	// 测试会话内容提供者提供的数据字段的名字
	public void conversationColumnTest() {
		SMSManager.getConversationColumn(getContext());
	}

	// 测试短信会话记录数据
	public void conversationTest() {
		List<Conversation> list = SMSManager.getConversations(getContext());
		for (Conversation conversation : list) {
			LogUtilS.i("TAG", "" + conversation);
		}
	}

	// 测试短信内容提供者提供的数据字段的名字
	public void smsColumnTest() {
		SMSManager.getSMSColumn(getContext());
	}

	// 测试短信聊天记录的数据
	public void smsTest() {
		// 模拟传入一个数字,模拟点击了数据库第7条
		List<Sms> list = SMSManager.getSMSes(getContext(), 7);
		for (Sms sms : list) {
			LogUtilS.i("TAG", "" + sms);
		}
	}

	// 数据库创建测试
	public void DBonCreateTest() {
		MyDBHelper helper = MyDBHelper.getInstance(getContext());
		SQLiteDatabase db = helper.getWritableDatabase();
		db.close();
	}

	// 数据库功能测试
	public void DBUtilTest() {
		DBUtil dbUtil = new DBUtil(getContext());
		dbUtil.insertBlackNumber("18812341234");
		boolean isHave = dbUtil.isBlackNumber("18812341234");
		LogUtilS.i("" + isHave);
	}
}
