package com.payele.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import android.widget.TextView;
import android.widget.Toast;

import com.payele.utils.Application_;
import com.payele.utils.HttpRequest;


public class RegisterActivity extends Activity{
	private static final String API_ROOT = "http://payele.sinaapp.com/api";
	private TextView usernameView;
	private TextView passwordView;
	private TextView password2View;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register_activity);
		
		initView();
		
		Application_.getInstatnce().addActivity(this);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
	        Intent intent = new Intent(this, LoginActivity.class);
	        startActivity(intent);
	        this.finish();
	        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	private void initView() {
		usernameView = (TextView) findViewById(R.id.reg_form_username);
		passwordView = (TextView) findViewById(R.id.reg_form_password);
		password2View = (TextView) findViewById(R.id.reg_form_password2);
	}
	
	public void doRegister(View view) {
		String params;
		String username = usernameView.getText().toString();
		String password = passwordView.getText().toString();
		Pattern pattern = Pattern.compile("^(?!_)(?!.*?_$)[a-zA-Z0-9_]+$");
		Matcher matcher = pattern.matcher(username);
		
		if (!matcher.matches()) {
			Toast.makeText(RegisterActivity.this, "用户名不符合规则", Toast.LENGTH_SHORT).show();
		} else if (!password.equals(password2View.getText().toString())) {
			Toast.makeText(RegisterActivity.this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
		} else {
			params = "username=" + username +
					"&password=" + password;
			new doRegisterTask().execute(params);			
		}
	}
	
	private class doRegisterTask extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog dialog = new ProgressDialog(RegisterActivity.this);
		
		@Override
		protected void onPreExecute() {
			this.dialog.setMessage("注册中...");
			this.dialog.show();
		}
		
		@Override
        protected JSONObject doInBackground(String... values) {
			String rstString;
			JSONObject rstJson = null;
			
			String params = values[0];
			rstString = HttpRequest.sendPost(API_ROOT + "/account/signup", params);
			try {
	            rstJson = new JSONObject(rstString);
	            
            } catch (JSONException e) {
            	Log.e("REgisterActivity", "Cant parse JSONString: " + rstString);
            	e.printStackTrace();
            }
	        return rstJson;
        }
		
		@Override
		protected void onPostExecute(JSONObject result) {
			String stat;
			
			if (this.dialog.isShowing())
				this.dialog.dismiss();
			try {
				stat = result.getString("stat");
				if (stat.equals("ok")) {
					Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
					startActivity(intent);
					RegisterActivity.this.finish();
				} else if (stat.equals("fail")) {
					switch (result.getJSONObject("data").getInt("err_num")) {
						case 1:
							Toast.makeText(RegisterActivity.this, "用户名已经存在", Toast.LENGTH_SHORT).show();
							break;
						default:
							Toast.makeText(RegisterActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
							
					}
				} else {
					Toast.makeText(RegisterActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
				}
				
			} catch (JSONException e) {
				Log.e("RegisterActivity_doRegisterTask", "Can't parse reponse: " + result.toString());
				e.printStackTrace();
			}
		}
		
	}
	

}
