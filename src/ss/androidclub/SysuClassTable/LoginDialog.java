package ss.androidclub.SysuClassTable;


import ss.androidclub.SysuClassTable.Request.Requests;
import android.app.Activity;  
import android.app.AlertDialog;  
import android.app.ProgressDialog;  
import android.content.DialogInterface;  
import android.content.Intent;
import android.os.Bundle;  
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;  
import android.view.View;  
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
  
public class LoginDialog extends Activity {  
    /** Called when the activity is first created. */  
    ProgressDialog p_dialog;
    private EditText sid;
    private EditText passwd;
   
    private ProgressDialog progressDialog;
    private String htmlString = null;
  
    
    private Handler handler = new Handler() {
    	public void handleMessage(Message msg) {
    		switch (msg.what) {
			case 0:		//��½�ɹ�
				Toast.makeText(LoginDialog.this, "Login Success!", Toast.LENGTH_LONG).show();
				progressDialog.cancel();
				Intent intent = new Intent();
				intent.setClass(LoginDialog.this, ShowLessons.class);
				intent.putExtra("JSON", htmlString);
				startActivity(intent);
				finish();
				//storeToSQLite();
				//new AlertDialog.Builder(SysuClassTableActivity.this).setMessage(htmlString).create().show();
				break;

			case 1:		//��¼ʧ��
				Toast.makeText(LoginDialog.this, "Login Failed!", Toast.LENGTH_LONG).show();
				Intent intent2 = new Intent();
				intent2.setClass(LoginDialog.this, ShowLessons.class);
			
				startActivity(intent2);
				finish();
				progressDialog.cancel();
				break;
			default:
				break;
			}
    	}
    };
    
  
   
    /**
     * �����̣߳����е�¼
     * 
     * @param sid		ѧ��
     * @param passwd	����
     */
    private void tologin(final String sid, final String passwd) {
    	progressDialog = ProgressDialog  
                .show(LoginDialog.this,  
                        "��ȴ�",  
                        "����Ϊ����¼...",  
                        true);  
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//Requests.sum(sid);
				boolean success = Requests.Login(sid, passwd);
			
				Requests.Sum(sid);
				//��¼�ɹ��Ժ����ӵ��α�ҳ��
				if(success) {
					htmlString = Requests.GetClassTable();
					htmlString = Requests.getTableInfo(htmlString);
					//Log.e("yong", result);
				}
				
				Message msg = new Message();
				if(success) {
					msg.what = 0;
				}
				else {
					msg.what = 1;
				}
				handler.sendMessage(msg);
			}
		}).start();
    };
    
    
    
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.lessons_detail);  
        
                        LayoutInflater factory = LayoutInflater  
                                .from(LoginDialog.this);  
                        final View DialogView = factory.inflate(  
                                R.layout.main, null);  
                        AlertDialog dlg = new AlertDialog.Builder(  
                                LoginDialog.this)  
                                .setTitle("��¼")  
                                .setView(DialogView)  
                                .setPositiveButton("ȷ��",  
                                        new DialogInterface.OnClickListener() {  
  
                                            @Override  
                                            public void onClick(  
                                                    DialogInterface dialog,  
                                                    int which) {  
                                            	
                                            	sid = (EditText)DialogView.findViewById(R.id.sid);
                                            	passwd = (EditText)DialogView.findViewById(R.id.passwd);
                                            	String sidString = sid.getText().toString();
                                				String passwdString = passwd.getText().toString();
                                				//�пգ���ΪedittextĬ����""��������null������Ҫ��������
                                				if(!sidString.equals("") && !passwdString.equals("")) {
                                					tologin(sidString, passwdString);
                                				}
                                				else {
                                					Toast.makeText(LoginDialog.this, "Please input the student id and your password!", Toast.LENGTH_LONG).show();
                                					Intent intent = new Intent();
                                					intent.setClass(LoginDialog.this, LoginDialog.class);
                                					startActivity(intent);
                                					finish();
                                					
                                				}
                                                // TODO Auto-generated method  
                                                // stub  
                                			
                                            }  
                                        })  
                                .setNegativeButton("ȡ��",  
                                        new DialogInterface.OnClickListener() {  
  
                                            @Override  
                                            public void onClick(  
                                                    DialogInterface dialog,  
                                                    int which) {  
                                                // TODO Auto-generated method  
                                                // stub  
                                            	Intent intent = new Intent();
                                				intent.setClass(LoginDialog.this, ShowLessons.class);
                                				
                                				startActivity(intent);
                                				finish();
                                            }  
                                        }).create();  
                        dlg.show();  
  
              
    }  
}  