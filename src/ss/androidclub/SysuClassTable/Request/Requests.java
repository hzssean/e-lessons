package ss.androidclub.SysuClassTable.Request;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.DefaultedHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import ss.androidclub.SysuClassTable.ShowLessons;

import android.util.Log;

public class Requests {
	private final static String TAG = "request";
	private final static String SUMURL = "http://bepanda.me/mysysu/sum.php";
	private final static String HOST = "http://uems.sysu.edu.cn";
	private final static String LOGIN_PATH = "/jwxt/j_unieap_security_check.do";
	private final static String CLASS_TABLE_PATH = "/jwxt/sysu/xk/xskbcx/xskbcx.jsp?xnd=2011-2012&xq=1";
	private final static String UPDATE_INFO_PATH = "http://bepanda.me/mysysu/update.php";
	private static String cookie;
	private static String p = "(jc)=\'(.*?)\'.*?(kcmc)=\'(.*?)\'.*?(dd)=\'(.*?)\'.*?(zfw)=\'(.*?)\'.*?(dsz)=\'(.*?)\'.*?(weekpos)=(\\d)";
	
	/**
	 * ���е�¼
	 * 
	 * @param sid		ѧ��
	 * @param passwd	����
	 * @return			��½�ɹ�����true������false
	 */
	public static boolean Login(String sid, String passwd) {
		boolean success = false;
		
		URL url = null;
		
		//���ӵ�����ϵͳ��ҳ����ȡcookie
		try {
			url = new URL(HOST + "/jwxt");
			
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setInstanceFollowRedirects(true);
			connection.connect();
			
			cookie = connection.getHeaderField("set-cookie");
			//Log.e(TAG, cookie);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//���е�¼
		try {
			url = new URL(HOST + LOGIN_PATH);
			
			//����connection�������post����Ϊ��default��get��
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			
			//�����ض���
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			
			//����cookie
			connection.setRequestProperty("Cookie", cookie);
			
			//���ͱ���
			String content = "j_username=" + sid + "&" + "j_password=" + passwd;
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			out.writeBytes(content);
			out.flush();
			out.close();
			
			//�����ض�����ɹ�
			if(connection.getResponseCode() == 302) {
				success = true;
			}
			
			//��ȡ�����Ӧ�ÿ��Բ�д
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = "";
			while((line = reader.readLine()) != null) {
				Log.e(TAG, line);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return success;
	}
	
	
	/**
	 *����ͳ��ʹ������
	 */
	public static void Sum(String sid){
		boolean success = false;
		Log.e(TAG, "Have into this");
		URL url = null;
		try{
		url = new URL(SUMURL + "?number=" + sid + "&version=1.1");
		
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.connect();
		
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String result = "";
		String line = "";
		
		while((line = bufferedReader.readLine()) != null) {
			result += line + "\n";
		}
		Log.e(TAG, result);
		
		if(connection.getResponseCode() == 200) {
			Log.e(TAG, "store success");
			success = true;
		}
		}catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "1");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "2");
		}
	}
	/**
	 * ��ȡ�α���Ϣ
	 * 
	 * @return	��������htmlҳ��
	 */
	public static String GetClassTable() {
		String result = "";
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(HOST + CLASS_TABLE_PATH);
		httpGet.setHeader("Cookie", cookie);
		
		try {
			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				InputStream is = entity.getContent();
				BufferedReader bf = new BufferedReader(new InputStreamReader(is, "utf-8"));
				String line = "";
			
				while((line = bf.readLine()) != null) {
					result += line + "\n";
				}
			}
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	
	/*
	 * ��ȡ�α���Ϣ
	 * 
	 * @param html �α�Դhtml
	 * @return json�ַ������Ŀα���Ϣ����sqlite����
	 */
	public static String getTableInfo(String html)
	{
		Pattern pattern = Pattern.compile(p, Pattern.DOTALL);
		Matcher match = pattern.matcher(html);
		
		String json = "{\"tableinfo\":[";
		while(match.find())
		{
			json+="{";
			for(int i=1;i<match.groupCount()+1;i++)
			{
				json+="\"";
				json+=match.group(i).toString();
				json+="\"";
				if(i%2==0) //�м�ֵ
					json+=",";
				else
					json+=":";
			}
			json = json.substring(0, json.length()-1);//ȥ��ĩβ����Ķ���
			json+="},";
			}
		json = json.substring(0, json.length()-1);//ȥ��ĩβ����Ķ���
		json+="]}";
		
		return json;
	}
	
	/**
	 * ��÷���˵İ汾��Ϣ
	 * 
	 * @return		�����°汾��apk��·��
	 */
	public static String GetNewVersionName() {
		String jsonString = "";
		String path = null;

		URL url = null;
		
		try {
			url = new URL(UPDATE_INFO_PATH);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.connect();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
			String line = null;
			while((line = reader.readLine()) != null) {
				jsonString += line + "\n";
			}
			connection.disconnect();
			Log.e(TAG, jsonString);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "error 1");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "error 2");
		}
		
		//����json
		if(jsonString != null) {
			try {
				JSONObject jsonObject = new JSONObject(jsonString);
				ShowLessons.newVersionName = jsonObject.getString("version");
				ShowLessons.newVersionInfo = jsonObject.getString("intro");
				path = jsonObject.getString("updateurl");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e(TAG, "JSON ERROR");
			}
		}
		return path;
	}
	
	/**
	 * ����apk�����浽sdcard�ĸ�Ŀ¼
	 * 
	 * @param path		apk�ĵ�ַ
	 * @return			���سɹ�����true
	 */
	public static boolean DownloadNewVersion(String path) {
		boolean success = false;
		Log.e(TAG, path);
		
		URL url = null;
		
		try {
			url = new URL(path);
			
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			
			//���浽sdard�ĸ�Ŀ¼
			if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream is = connection.getInputStream();
				FileOutputStream fos = new FileOutputStream("/sdcard/e-lessons" + ShowLessons.newVersionName + ".apk");
				byte[] buf = new byte[1024];
				int i = 0;
				while((i = is.read(buf)) > 0) {
					fos.write(buf, 0, i);
				}
				fos.flush();
				fos.close();
				is.close();
				success = true;
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "Download Error 1");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "Download Error 2");
		}
		
		return success;
	}
}
	