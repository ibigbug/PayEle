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
import android.widget.TextView;
import android.widget.Toast;

import com.payele.storage.DBManager;
import com.payele.storage.ModelAccount;
import com.payele.storage.ModelApp;
import com.payele.utils.Application_;
import com.payele.utils.HttpRequest;


public class ProfileActivity extends Activity{
	private static final String API_ROOT = "http://payele.sinaapp.com/api/account";
	private DBManager mgr;
	private ModelApp app;
	private ModelAccount account;
	private int currentUid;
	
	private TextView screenNameView;
	private TextView locationView;
	private TextView emailView;
	
	
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.profile_activity);
		
		initDB();
		initView();
		
		Application_.getInstatnce().addActivity(this);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
	        Intent intent = new Intent(this, MainActivity.class);
	        startActivity(intent);

	        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
	        this.finish();
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mgr.closeDB();
	}
	
	private void initDB() {
		mgr = new DBManager(this);
		app = mgr.loadAppInfo();
		currentUid = app.getCurrentUid();
		account = mgr.retriveAccount(currentUid);
		
	}
	
	private void initView() {
		screenNameView = (TextView) findViewById(R.id.profile_screen_name);
		locationView = (TextView) findViewById(R.id.profile_location);
		emailView = (TextView) findViewById(R.id.profile_email);
		
		screenNameView.setText(account.getScreen_name());
		locationView.setText(account.getLocation());
		emailView.setText(account.getEmail());
	}
	
	public void doUpdateProfile (View v) {
		String screenName, location, email;
		screenName = screenNameView.getText().toString();
		location = locationView.getText().toString();
		email = emailView.getText().toString();
		String queryString = "screen_name=" + screenName +
				"&location=" + location +
				"&email=" + email;
		new updateProfileTask().execute(queryString);
	}
	
	private class updateProfileTask extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog dialog = new ProgressDialog(ProfileActivity.this);
		
		@Override
		protected void onPreExecute() {
			this.dialog.setMessage("更新中...");
			this.dialog.show();
		}

		@Override
        protected JSONObject doInBackground(String... params) {
			JSONObject rstJsonObject = null;
	        String queryString = params[0];
	        String rstString = HttpRequest.sendPost(API_ROOT + "/profile/" + String.valueOf(currentUid), queryString);
	        try {
	            rstJsonObject = new JSONObject(rstString);
            } catch (JSONException e) {
            	Log.e("ProfileActivity", "Cant parse JSONString: " + rstString);
            	e.printStackTrace();
            }
	        return rstJsonObject;
        }
		
		@Override
		protected void onPostExecute(JSONObject result) {
			String stat;
			JSONObject profile;
			
			if (this.dialog.isShowing())
				this.dialog.dismiss();
			try {
	            stat = result.getString("stat");
	            if (stat.equals("ok")) {
	            	profile = result.getJSONObject("data");
	            	account = mgr.updateAccount(currentUid, profile);
	            	ProfileActivity.this.initView();
	            	Toast.makeText(ProfileActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
	            } else {
	            	Toast.makeText(ProfileActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
	            }
            } catch (JSONException e) {
            	Log.e("ProfileActivity", "Json erro with: " + result.toString());
            }
		}
	}
}