package com.tarena.fgr.biz;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.support.v4.util.LruCache;
import android.util.SparseArray;

import com.tarena.fgr.entity.Contact;
import com.tarena.fgr.youlu.MainActivity;
import com.tarena.fgr.youlu.R;

/**
 * 联系人业务处理类
 * 
 * @author 冯国芮 2016年9月30日 09:27:55
 * 
 */
// 2016年9月30日 10:31:08
// 冯国芮:相关的三张表[文档搜索ContactsContract]
// ContactsContract.Data
// ContactsContract.RawContacts
// ContactsContract.Contacts
public class ContactManager {
	// 通过ContactsContract API查询所有的联系人的信息
	// 官方为了防止其他人开发人员,所以对数据进行增删改查,从而影响了数据

	// 去mimetype表粘贴
	public static final String MIMETYPE_NAME = "vnd.android.cursor.item/name";// 姓名类型
	public static final String MIMETYPE_EMAIL = "vnd.android.cursor.item/email_v2";// 邮箱类型
	public static final String MIMETYPE_PHONE = "vnd.android.cursor.item/phone_v2";// 电话类型
	public static final String MIMETYPE_ADDRESS = "vnd.android.cursor.item/postal-address_v2";// 地址类型

	// 用来替代HashMap的更高效的数据集合
	// 这样就不需要每次都去查询数据库了
	// 强引用,宁可报错,也不会被垃圾回收器回收掉
	public static SparseArray<Contact> cacheContact = new SparseArray<Contact>();// 保存到缓存,相似hashmap

	// 设计缓存的最大的一个空间,当达到是,会移除最先放入的数据,先入先出
	// 可以动态获得
	// 获得手机为应用程序分配的堆内存大小
	// 一般取堆内存1/8为缓存的大小
	// 模拟器的VM Heap的值为32
	// public static int maxSize = 1024 * 1024 * 4;// 4MB
	// 2016年10月9日 10:29:20
	// 冯国芮:如果这里不给出明确的值,而是从Activity中取值,测试时(test)会出现错误
	// 2016年9月30日 16:29:03
	// 冯国芮:这样的方法不知道可以不?好怀疑
	public static int maxSize = MainActivity.MEMORY_MAX_SIZE;
	/**
	 * LruCache缓存机制是内部存储的数据都是强引用，在设计的缓存空间不满的时候 数据不会被回收，
	 * 
	 * 当存储空间满了，会回收最近最少使用的存储空间
	 * */
	// 创建一个缓存LruCache对象
	public static LruCache<Integer, Bitmap> cachePhoto = new LruCache<Integer, Bitmap>(
			maxSize) {
		// 计算每一幅被缓存图片的大小
		protected int sizeOf(Integer key, Bitmap value) {
			// 重写该方法在图片被缓存时计算图片的大小
			// return value.getRowBytes() * value.getHeight();
			return value.getByteCount();
		};
	};

	/**
	 * @param context
	 * @return所以联系人数据
	 * 
	 */
	// 有一个最小安卓12要求
	public static List<Contact> getContacts(Context context) {
		List<Contact> contacts = new ArrayList<Contact>();
		// 创建一个内容简析器,来访问内容提供者提供的数据
		ContentResolver resolver = context.getContentResolver();

		Uri uri = ContactsContract.Contacts.CONTENT_URI;// 根据uri访问到数据库资源

		// String[] projection = new String[]{"_id","photo_id"};
		String[] projection = new String[] { ContactsContract.Contacts._ID,
				ContactsContract.Contacts.PHOTO_ID };

		Cursor cursor = resolver.query(uri, projection,// 要查询的列名
				null,// 程序条件的值
				null,// 条件中的"?"的值
				null);// 排序方式

		while (cursor.moveToNext()) {
			Contact contact = new Contact();
			int id = cursor.getInt(cursor.getColumnIndex(projection[0]));// _id
			int photoid = cursor.getInt(cursor.getColumnIndex(projection[1]));// photo_id
			contact.setId(id);
			contact.setPhotoid(photoid);

			// 判断缓存中是否存在该联系人
			if (cacheContact.get(id) != null) {
				// 如果在缓存中保存了联系人的信息,直接从缓存中获得该联系人对象,并加入联系人的集合
				// 直接跳过本次循环,不再执行其他操作
				contacts.add(cacheContact.get(id));
				continue;
			}

			// 根据联系人的账户查联系人的详细数据信息
			// name,phone,address,email
			// 查打data表
			Uri dataUri = ContactsContract.Data.CONTENT_URI;
			String[] projection2 = new String[] { Data.MIMETYPE, Data.DATA1 };
			String selection = Data.RAW_CONTACT_ID + "=?";// 查询条件
			Cursor cursor2 = resolver.query(dataUri, projection2, selection,
					new String[] { String.valueOf(id) }, null);
			while (cursor2.moveToNext()) {
				String mimetype = cursor2.getString(cursor2
						.getColumnIndex(Data.MIMETYPE));// mimetype
				String data1 = cursor2.getString(cursor2
						.getColumnIndex(Data.DATA1));// data1
				if (MIMETYPE_NAME.equals(mimetype)) {
					contact.setName(data1);
				} else if (MIMETYPE_PHONE.equals(mimetype)) {
					contact.setPhone(data1);
				} else if (MIMETYPE_EMAIL.equals(mimetype)) {
					contact.setEmail(data1);
				} else if (MIMETYPE_ADDRESS.equals(mimetype)) {
					contact.setAddress(data1);
				}
			}
			cursor2.close();

			// 把联系人的信息进行缓存
			cacheContact.put(id, contact);

			// 把联系人对象加到联系人集合中
			contacts.add(contact);
		}
		cursor.close();
		return contacts;
	}

	/**
	 * 根据头像id查联系人头像 2016年9月30日 15:33:38
	 * 
	 * @param context
	 * @param photoId联系人头像图片id
	 * @return 联系人头像图片
	 */
	public static Bitmap getPhotoByPhotoId(Context context, int photoId) {
		Bitmap bitmap = cachePhoto.get(photoId);
		if (bitmap == null) {// 没有头像,缓存没有存在
			if (photoId == 0) {// 没有头像
				// 默认头像
				bitmap = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.ic_contact);
				// 问题1:2016年9月30日 16:30:50
				// 我在布局文件中设置了图片,这里有必要再设置吗?
				// 问题2:2016年10月8日 08:42:52
				// 在添加第一个"添加联系人",出现bug,如果不写这句话,添加联系人的头像不会显示
			} else {// 没有缓存

				ContentResolver resolver = context.getContentResolver();

				Uri uri = Data.CONTENT_URI;
				String[] projection = new String[] { Data.DATA15 };// 查询的列
				String selection = Data._ID + "=?";// 查询条件
				Cursor cursor = resolver.query(uri, projection, selection,
						new String[] { String.valueOf(photoId) }, null);

				// 只有一个头像,所以不需要while循环了
				if (cursor.moveToNext()) {// 有数据,说明有头像
					byte[] blob = cursor.getBlob(0);// 只有一列所以得到0就可以了
					// 通过工厂,将二进制,转化为图片
					bitmap = BitmapFactory
							.decodeByteArray(blob, 0, blob.length);
					if (bitmap != null) {
						// 将图片存如缓存
						cachePhoto.put(photoId, bitmap);
					}
				}
				cursor.close();
			}
		}

		return bitmap;
	}

	/**
	 * 修改的时候使用此方法,如果不将要修改的数据清除掉,他从缓存中读取,还是旧数据
	 * 
	 * @param contact
	 */
	public static void clearCache(Contact contact) {
		// 将联系人的信息在SparseArray中清除掉
		cacheContact.remove(contact.getId());
		// 将联系人的头像从LruCache中清除掉
		cachePhoto.remove(contact.getPhotoid());
	}

	// 把联系人从数据库里删除
	public static void deleteContact(Context context, Contact contact) {
		ContentResolver resolver = context.getContentResolver();
		// 联系人的账户表
		resolver.delete(ContactsContract.RawContacts.CONTENT_URI,
				RawContacts.CONTACT_ID + "=?",
				new String[] { String.valueOf(contact.getId()) });

		// 联系人的账户数据表
		Uri uri = Data.CONTENT_URI;
		String where = Data.CONTACT_ID + "=?";
		String[] args = new String[] { String.valueOf(contact.getId()) };
		resolver.delete(uri, where, args);
	}

}
