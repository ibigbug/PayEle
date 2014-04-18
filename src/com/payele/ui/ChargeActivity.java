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
import android.widget.EditText;
import android.widget.Toast;

import com.payele.storage.DBManager;
import com.payele.storage.ModelApp;
import com.payele.utils.Application_;
import com.payele.utils.HttpRequest;


public class ChargeActivity extends Activity{
	private static String API_ROOT = "http://payele.sinaapp.com/api/front/history";
	private EditText chargeTotalText = null;
	private DBManager mgr = null;
	private ModelApp app = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.charge_activity);
		
		Application_.getInstatnce().addActivity(this);
		initView();
	}
	
	private void initView() {
		mgr = new DBManager(this);
		app = mgr.loadAppInfo();
		chargeTotalText = (EditText) findViewById(R.id.charge_total);
    }
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mgr.closeDB();
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
	
	public void doCharge(View v) {
		new chargeTask().execute();
	}
	
	private class chargeTask extends AsyncTask<String, Void, JSONObject> {
		private ProgressDialog dialog = new ProgressDialog(ChargeActivity.this);
		
		@Override
		protected void onPreExecute() {
			this.dialog.setMessage("充值中，请稍后");
			this.dialog.show();
		}
		

		@Override
        protected JSONObject doInBackground(String... params) {
			JSONObject rstJsonObject = null;
			String chargeTotal = chargeTotalText.getText().toString();
			String url = API_ROOT + "/add/" + String.valueOf(app.getCurrentUid()) + "/" + chargeTotal;
			String rstString = HttpRequest.sendPost(url, null);
			try {
	            rstJsonObject = new JSONObject(rstString);
            } catch (JSONException e) {
            	Log.e("Charge_Activity", "Cant parse JSONString: " + rstString);
            	e.printStackTrace();
            }
			
	        return rstJsonObject;
        }
		
		@Override
		protected void onPostExecute(JSONObject result) {
			String stat = null;
			if (this.dialog.isShowing())
				this.dialog.dismiss();
			try {
	            stat = result.getString("stat");
	            if (stat.equals("ok")) {
	            	Toast.makeText(ChargeActivity.this, "充值成功！", Toast.LENGTH_SHORT).show();
	            } else {
	            	Toast.makeText(ChargeActivity.this, "充值失败！", Toast.LENGTH_SHORT).show();
	            }
            } catch (JSONException e) {
            	Log.e("Charge Activity", "Cant handle JSONObject: " + result.toString());
            	e.printStackTrace();
            }
		}
		
	}

}
