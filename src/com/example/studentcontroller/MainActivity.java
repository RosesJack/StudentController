package com.example.studentcontroller;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dao.studentDao;
import com.example.studentBean.Student;

public class MainActivity extends Activity implements OnClickListener {
	// 创建当前上下文对象
	Context context = this;
	// 数据库管理类
	studentDao sdao = new studentDao(context);
	// 声明控件
	EditText et1;
	Button button1;
	Button button2;
	RadioGroup rg1;
	ListView lv;
	// 创建适配器类
	MyAdapt myAdapt = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 初始化控件
		et1 = (EditText) findViewById(R.id.et1);
		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		rg1 = (RadioGroup) findViewById(R.id.rg1);
		lv = (ListView) findViewById(R.id.lv1);
		// 控件绑定事件
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);

		// 显示动画logo
		ImageView iv = (ImageView) findViewById(R.id.img_change);
		// 加载logo
		iv.setBackgroundResource(R.drawable.logo);
		//
		AnimationDrawable ad = (AnimationDrawable) iv.getBackground();
		// 开始动画
		ad.start();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		// 保存事件
		case R.id.button1:
			Student student = new Student();
			String sex = null;
			String name = et1.getText().toString().trim();
			int id = rg1.getCheckedRadioButtonId();
			// 判断单选框选中的radioButton
			if (id == R.id.rb1) {
				sex = "nan";
			} else if (id == R.id.rb2) {
				sex = "nv";
			}
			student.setName(name);
			student.setSex(sex);
			long result = sdao.insertDB(student);
			System.out.println(result);
			if (result > 0) {
				Toast.makeText(context, "数据成功写入到" + result + "行",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(context, "数据写入失败", Toast.LENGTH_SHORT).show();
			}
			// 刷新数据
			refreshData(sdao);
			break;
		// 读取事件
		case R.id.button2:
			refreshData(sdao);
			break;
		default:
			break;
		}
	}

	/**
	 * 刷新列表
	 */
	private void refreshData(studentDao sdao) {
		List<Student> stuList = sdao.selectAll();
		if (myAdapt == null) {
			myAdapt = new MyAdapt(stuList);
			// 展示到listview中
			lv.setAdapter(myAdapt);
		} else {
			myAdapt.setStuList(stuList);
			myAdapt.notifyDataSetChanged();
		}
	}

	/**
	 * 适配器类
	 */
	private class MyAdapt extends BaseAdapter {
		private List<Student> stuList;

		// 添加构造方法
		public MyAdapt(List<Student> stuList) {
			this.stuList = stuList;
		}

		private void setStuList(List<Student> stuList) {
			this.stuList = stuList;
		}

		public int getCount() {
			return stuList.size();
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		/**
		 * 重写getView方法
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			// 得到这个Item中的student
			final Student student = stuList.get(position);
			// 优化ListView
			if (convertView == null) {
				// 将xml文件转化成View对象
				view = View.inflate(context, R.layout.show, null);
			} else {
				// 复用
				view = convertView;
			}
			ImageView iv1 = (ImageView) view.findViewById(R.id.img1);
			ImageView iv2 = (ImageView) view.findViewById(R.id.img2);
			TextView tv1 = (TextView) view.findViewById(R.id.tv1);
			if ("nan".equals(student.getSex())) {
				iv1.setImageResource(R.drawable.nan);
			} else {
				iv1.setImageResource(R.drawable.nv);
			}
			tv1.setText(student.getName());
			iv2.setImageResource(R.drawable.delete);
			// 设置删除事件
			iv2.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// 设置弹窗提醒
					AlertDialog.Builder builder = new Builder(context);
					// 设置标题
					builder.setTitle("警告");
					// 设置内容
					builder.setMessage("将会删除内容！");
					// 取消按钮
					builder.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {

								}
							});
					// 确定按钮
					builder.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									sdao.delectData(student);
									// 刷新数据
									refreshData(sdao);
								}
							});
					// 显示对话框
					builder.show();
				}
			});
			return view;
		}
	}
}
