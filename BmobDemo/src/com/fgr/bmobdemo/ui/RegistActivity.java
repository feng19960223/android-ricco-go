package com.fgr.bmobdemo.ui;

import java.io.File;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.fgr.bmobdemo.R;
import com.fgr.bmobdemo.bean.MyUser;
import com.nostra13.universalimageloader.core.ImageLoader;

public class RegistActivity extends Activity {
	@Bind(R.id.et_regist_username)
	EditText etUsername;
	@Bind(R.id.et_regist_password)
	EditText etPassword;
	@Bind(R.id.rg_regist_gender)
	RadioGroup rgGender;
	@Bind(R.id.iv_regist_avatar)
	ImageView ivAvatar;

	// 拍摄头像照片后存储照片的路径
	String photoPath;
	// 头像图片上传成功后，图片保存的地址
	String avatarUrl;
	// 当上传头像图片时给用户提示
	ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
		ButterKnife.bind(this);
	}

	@OnClick(R.id.btn_regist_regist)
	public void regist(View v) {
		registUser();
	}

	protected void registUser() {
		String username = etUsername.getText().toString();
		String password = etPassword.getText().toString();
		if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
			return;
		}
		MyUser user = new MyUser();
		user.setUsername(username);
		// 对password进行MD5转码
		String md5 = new String(Hex.encodeHex(DigestUtils.sha(password)))
				.toUpperCase();
		user.setPassword(md5);
		// 头像
		if (!TextUtils.isEmpty(avatarUrl)) {
			user.setAvatar(avatarUrl);
		} else {
			user.setAvatar("");
		}
		boolean gender = true;
		if (rgGender.getCheckedRadioButtonId() == R.id.rb_regist_girl) {
			gender = false;
		}
		user.setGender(gender);
		// 把用户对象保存到Bmob服务器
		// 添加当前所使用设备的设备id
		user.setInstallationId(BmobInstallation.getInstallationId(this));

		user.save(this, new SaveListener() {
			@Override
			public void onSuccess() {
				Toast.makeText(RegistActivity.this, "保存成功", Toast.LENGTH_SHORT)
						.show();
				avatarUrl = null;
				ivAvatar.setImageResource(R.drawable.ic_launcher);
				etUsername.setText("");
				etPassword.setText("");
				finish();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				switch (arg0) {
				case 401:
					Toast.makeText(RegistActivity.this, "用户名重复",
							Toast.LENGTH_SHORT).show();
					break;
				default:
					Toast.makeText(RegistActivity.this,
							"保存失败，错误代码：" + arg0 + ":" + arg1,
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		});
	}

	@OnClick(R.id.iv_regist_avatar)
	public void setAvatar(View view) {
		// 利用Intent选择器(IntentChooser)实现弹出对话框
		// 让用户进行拍照或者选图来作为头像
		// 创建打开图库的intent
		Intent intent1 = new Intent(Intent.ACTION_PICK);
		intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		// 创建打开系统拍照程序的intnet
		Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 告诉系统拍照程序，拍照完毕后照片保存的位置
		File file = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				System.currentTimeMillis() + ".jpg");
		photoPath = file.getAbsolutePath();
		Uri value = Uri.fromFile(file);
		intent2.putExtra(MediaStore.EXTRA_OUTPUT, value);

		// 创建Intent选择器(IntentChooser)
		Intent intent = Intent.createChooser(intent1, "选择头像...");
		intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { intent2 });

		startActivityForResult(intent, 101);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == 101) {
			// 从data中找到作为头像图片的本地地址
			String filePath;
			if (data != null) {
				// 头像图片是用户从图库选择的图片
				// uri是用户选取的图片在MeidaStroe数据表的位置
				Uri uri = data.getData();
				// 根据uri找到该图片在SD上的真实位置
				Cursor cursor = getContentResolver().query(uri,
						new String[] { MediaStore.Images.Media.DATA }, null,
						null, null);
				cursor.moveToNext();
				filePath = cursor.getString(0);
				cursor.close();
			} else {
				// 头像图片是用户从相机拍摄回来的
				filePath = photoPath;
			}
			// 显示一个提示框，告诉用户正在上传
			pd = ProgressDialog.show(this, "", "上传中...");
			// 利用BmobSDK提供的BmobFile类上传图片
			final BmobFile bf = new BmobFile(new File(filePath));
			bf.uploadblock(this, new UploadFileListener() {
				@Override
				public void onSuccess() {
					// 上传文件（图片）成功
					// 获取该文件（图片）在服务器上的地址
					avatarUrl = bf.getFileUrl(RegistActivity.this);
					// 利用ImageLoader将用户选取的图片放到ivAvatar中显示
					ImageLoader.getInstance().displayImage(avatarUrl, ivAvatar);
					// 让提示框消失
					pd.dismiss();
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					Toast.makeText(RegistActivity.this,
							"上传头像失败，请稍后重试。错误代码：" + arg0 + "," + arg1,
							Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
}
