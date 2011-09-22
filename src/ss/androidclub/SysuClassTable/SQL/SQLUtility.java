package ss.androidclub.SysuClassTable.SQL;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * �������ݿ����Ҫ����
 * 
 * @author YONG
 *
 */
public class SQLUtility {
	private static final String TAG = "db";
	private static final String DB_NAME = "lessons.db";
	private static final String DB_TABLE = "lessons";
	private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS lessons (" + "NAME TEXT ," +
			"TIME TEXT," + "CLASSROOM TEXT," + "WEEKS TEXT," + "DSZ TEXT," + "WEEKPOS TEXT)" ;
	private static final int DB_VERSION = 2;
	private Context mContext;
	private SQLHelper helper;
	private SQLiteDatabase db;
	
	public SQLUtility(Context context) {
		mContext = context;
		helper = new SQLHelper(context, DB_NAME, null, DB_VERSION);
		db = helper.getWritableDatabase();
	}
	
	/**
	 * ����
	 */
	public void createTable() {
		db.execSQL(CREATE_TABLE);
	}
	
	/**
	 * �Ȱ�ԭ���ı��������ȥ�������
	 */
	public void cleanDB() {
		db.execSQL("DROP TABLE " + DB_TABLE);
	}
	
	/**
	 * ���Lesson�����ݿ���
	 * 
	 * @param lesson	Ҫ��ӵĿγ�
	 * @return			��ӳɹ������Ѵ��ڸÿγ̣�����true�� ���򷵻�false
	 */
	public boolean addLesson(Lesson lesson) {
		String sql = "";
		
		//�鿴�Ƿ��Ѿ��иü�¼���˴�Ҳ���ԶԿγ���Ϣ���и���
		/*
		sql = "select * from " + DB_TABLE + " where NAME='" + lesson.getName() + "'";
		Cursor cursor = db.rawQuery(sql, null);
		if(cursor.getCount() > 0) {
			return true;
		}
		*/
		//���в������
		try {
			sql = "insert into " + DB_TABLE +" values ('" + lesson.getName() + "','" + lesson.getTime() + "','" +
					lesson.getClassRoom() + "','" + lesson.getWeeks() + "','" + lesson.getDanShuangZhou() + "','" +
					lesson.getWeekPosition() + "')";
			db.execSQL(sql);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, "insert error, sql=" + sql);
			return false;
		}
	}
	
	/**
	 * ͨ�����ڼ�����ȡ��������пγ�
	 * 
	 * @param weekpos	���ڼ�����ʽ:����һΪ1�����ڶ�Ϊ2����������
	 * @return			��������X�����пγ���Ϣ
	 */
	public ArrayList<Lesson> getLessonsByWeekPos(String weekpos) {
		ArrayList<Lesson> lessons = new ArrayList<Lesson>();
		
		String sql = "select * from " + DB_TABLE + " where WEEKPOS='" + weekpos + "'";
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Lesson lesson = new Lesson(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
			lessons.add(lesson);
			cursor.moveToNext();
		}
		
		return lessons;
	}

	/**
	 * ��ѯ���еĿγ�
	 * 
	 * @return	���ؿα��е����пγ�
	 */
	public ArrayList<Lesson> getAllLessons() {
		ArrayList<Lesson> lessons = new ArrayList<Lesson>();
		
		String sql = "select * from " + DB_TABLE;
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Lesson lesson = new Lesson(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
			lessons.add(lesson);
			cursor.moveToNext();
		}
		
		return lessons;
	}
	
	/*
	 * ����Ƿ��������
	 * 
	 * @return �������ݷ����棬���򷵻ؼ�
	 */
	public boolean checkDataBase(){
		boolean result = false;
		String sql = "select count(*) as c from sqlite_master  where type='table' and name ='" + DB_TABLE+"'";
		Cursor cursor = db.rawQuery(sql, null);
		if(cursor.moveToNext())
		{
			int count = cursor.getInt(0);
			if(count>0)
			result = true;
		}
			
		return result;
	}
	
	/**
	 * �ر����ݿ�
	 */
	public void closeDB() {
		db.close();
	}
	
}