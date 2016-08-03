package com.example.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.studentBean.Student;

public class studentDao {
	private Context context;
	private MyOpenHelpDb mhd;

	public studentDao(Context context) {
		this.context = context;
		mhd = new MyOpenHelpDb(context);
	}

	/**
	 * ��������
	 */
	public long insertDB(Student student) {
		String name = student.getName();
		String sex = student.getSex();

		SQLiteDatabase db = mhd.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("name", name);
		values.put("sex", sex);
		long row = db.insert("students", "1", values);
		// �ͷ���Դ
		db.close();
		return row;
	}

	/**
	 * c��ѯ���и�����
	 */
	public List<Student> selectAll() {
		List<Student> stuList = new ArrayList<Student>();

		SQLiteDatabase db = mhd.getWritableDatabase();
		// ��ѯ����
		Cursor cursor = db.query("students", new String[] { "name", "sex" },
				null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				// �α��0��ʼ
				String name = cursor.getString(0);
				String sex = cursor.getString(1);
				Student student = new Student();
				student.setName(name);
				student.setSex(sex);
				stuList.add(student);
			} while (cursor.moveToNext());
		}
		// �ͷ���Դ
		db.close();
		return stuList;
	}

	/**
	 * ɾ������
	 * 
	 */
	public int delectData(Student student) {
		String name = student.getName();
		String sex = student.getSex();

		SQLiteDatabase db = mhd.getWritableDatabase();
		int result = db.delete("students", "name = ? and sex = ?", new String[] {
				name, sex });
		return result;
	}
}
