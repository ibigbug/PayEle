package com.payele.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.payele.storage.DBManager;
import com.payele.storage.ModelAccount;
import com.payele.utils.Application_;
import com.payele.utils.HttpRequest;

/**
 * 
 * @ClassName: LoginActivity 
 * @Description: 登录类
 * @author Yuwei Ba <i@xiaoba.me>
 * @date Mar 10, 2014 6:04:09 PM 
 *
 */

public class LoginActivity extends Activity {
	
	private static final String API_ROOT = "http://payele.sinaapp.com/api";
	private DBManager mgr;
	
	private EditText usernameText;
	private EditText passwordText;
	private Button loginButton;
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_activity);
		
		initView();
		mgr = new DBManager(this);
		
		Application_.getInstatnce().addActivity(this);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mgr.closeDB();
	}
	
	/**
	 * 
	 * @Title: initView 
	 * @Description: 初始化View
	 * @param  NULL
	 * @return void   
	 * @throws
	 */
	private void initView() {
		usernameText = (EditText) findViewById(R.id.login_form_username);
		passwordText = (EditText) findViewById(R.id.login_form_password);
		loginButton = (Button) findViewById(R.id.login_form_login);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
	        Intent intent = new Intent(this, MainActivity.class);
	        startActivity(intent);
	        this.finish();
	        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	public void doLogin(View view){
		
		loginButton.setEnabled(false);
	    new DoLoginTask().execute();
	    
	}
	
	public void toRegisterView(View view) {
		Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        this.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
	}
	
	
	private class DoLoginTask extends AsyncTask<String, String, JSONObject> {
		
		private ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
		
		@Override
		protected void onPreExecute() {
			this.dialog.setMessage("登录中...");
			this.dialog.show();
		}
		
		@Override
		protected JSONObject doInBackground(String...strings) {
			
			String param = null;
			String rstString = null;
			JSONObject rstJsonObject = null;
			String username = usernameText.getText().toString();
			String password = passwordText.getText().toString();
			
			param = "username=" + username + "&password=" + password;
			rstString = HttpRequest.sendPost(API_ROOT + "/account/login", param);
	    
			try {
	            rstJsonObject = new JSONObject(rstString);
	            
            } catch (JSONException e) {
            	Log.e("LoginActivity", "Cant parse JSONString: " + rstString);
	            e.printStackTrace();
            }
			return rstJsonObject;
		}
		
		@Override
		protected void onPostExecute(JSONObject result) {
        	String stat = null;
        	if (dialog.isShowing())
        		dialog.dismiss();
        	
        	loginButton.setEnabled(true);
            try {
	            stat = result.getString("stat");
            } catch (JSONException e) {
            	Log.e("LoginActivity", "JSONError: " + result.toString());
	            e.printStackTrace();
            }

        	System.out.println(result.toString());
        	if (stat.equals("ok")) {
        		loginUser(result);
        		Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        		Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
        		startActivity(intent);
        		LoginActivity.this.finish();
        	} else if (stat.equals(null)) {
        		Toast.makeText(LoginActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
			} else {
        		Toast.makeText(LoginActivity.this, "用户名不存在或密码错误", Toast.LENGTH_SHORT).show();
        	}
	    }
		
		private void loginUser (JSONObject result) {
			JSONObject accountJson = null;
			ModelAccount account = null;
            try {
	            accountJson = result.getJSONObject("account");
	            account = new ModelAccount(accountJson);
	            mgr.createOrUpdateAccount(account);
	            mgr.loginUser(account.getId(), account.getSession());
            } catch (JSONException e) {
            	Log.e("loginUser ERROR:", "Wrong result JSON: " + result.toString());
	            e.printStackTrace();
            }

			
		}
	}
}
