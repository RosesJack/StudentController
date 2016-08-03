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
	// ������ǰ�����Ķ���
	Context context = this;
	// ���ݿ������
	studentDao sdao = new studentDao(context);
	// �����ؼ�
	EditText et1;
	Button button1;
	Button button2;
	RadioGroup rg1;
	ListView lv;
	// ������������
	MyAdapt myAdapt = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// ��ʼ���ؼ�
		et1 = (EditText) findViewById(R.id.et1);
		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		rg1 = (RadioGroup) findViewById(R.id.rg1);
		lv = (ListView) findViewById(R.id.lv1);
		// �ؼ����¼�
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);

		// ��ʾ����logo
		ImageView iv = (ImageView) findViewById(R.id.img_change);
		// ����logo
		iv.setBackgroundResource(R.drawable.logo);
		//
		AnimationDrawable ad = (AnimationDrawable) iv.getBackground();
		// ��ʼ����
		ad.start();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		// �����¼�
		case R.id.button1:
			Student student = new Student();
			String sex = null;
			String name = et1.getText().toString().trim();
			int id = rg1.getCheckedRadioButtonId();
			// �жϵ�ѡ��ѡ�е�radioButton
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
				Toast.makeText(context, "���ݳɹ�д�뵽" + result + "��",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(context, "����д��ʧ��", Toast.LENGTH_SHORT).show();
			}
			// ˢ������
			refreshData(sdao);
			break;
		// ��ȡ�¼�
		case R.id.button2:
			refreshData(sdao);
			break;
		default:
			break;
		}
	}

	/**
	 * ˢ���б�
	 */
	private void refreshData(studentDao sdao) {
		List<Student> stuList = sdao.selectAll();
		if (myAdapt == null) {
			myAdapt = new MyAdapt(stuList);
			// չʾ��listview��
			lv.setAdapter(myAdapt);
		} else {
			myAdapt.setStuList(stuList);
			myAdapt.notifyDataSetChanged();
		}
	}

	/**
	 * ��������
	 */
	private class MyAdapt extends BaseAdapter {
		private List<Student> stuList;

		// ��ӹ��췽��
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
		 * ��дgetView����
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			// �õ����Item�е�student
			final Student student = stuList.get(position);
			// �Ż�ListView
			if (convertView == null) {
				// ��xml�ļ�ת����View����
				view = View.inflate(context, R.layout.show, null);
			} else {
				// ����
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
			// ����ɾ���¼�
			iv2.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// ���õ�������
					AlertDialog.Builder builder = new Builder(context);
					// ���ñ���
					builder.setTitle("����");
					// ��������
					builder.setMessage("����ɾ�����ݣ�");
					// ȡ����ť
					builder.setNegativeButton("ȡ��",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {

								}
							});
					// ȷ����ť
					builder.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									sdao.delectData(student);
									// ˢ������
									refreshData(sdao);
								}
							});
					// ��ʾ�Ի���
					builder.show();
				}
			});
			return view;
		}
	}
}
