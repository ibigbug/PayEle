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
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.payele.storage.ModelFeed;
import com.payele.utils.Application_;
import com.payele.utils.HttpRequest;


public class PostActivity extends Activity{
	private static final String API_ROOT = "http://payele.sinaapp.com/api/front";
	private Bundle bundle;
	private int postId;
	private ModelFeed postModel;
	private TextView postTitleView;
	private TextView postAuthorView;
	private TextView postDateView;
	private TextView postContentView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.post_activity);
		
		bundle = this.getIntent().getExtras();
		postId = bundle.getInt("postId");
		new doFetchPostTask().execute(postId);
		
		Application_.getInstatnce().addActivity(this);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(PostActivity.this, FeedActivity.class);
			startActivity(intent);
	        this.finish();
	        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
			
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	private void initView () {
		postTitleView = (TextView) findViewById(R.id.post_title);
		postAuthorView = (TextView) findViewById(R.id.post_author);
		postDateView = (TextView) findViewById(R.id.post_date);
		postContentView = (TextView) findViewById(R.id.post_content);
		
		postTitleView.setText(postModel.getTitle());
		postAuthorView.setText(postModel.getAuthor());
		postDateView.setText(postModel.getDatetime());
		postContentView.setText(postModel.getContnet());
		
	}
	
	private class doFetchPostTask extends AsyncTask<Integer, String, JSONObject> {
		private ProgressDialog dialog = new ProgressDialog(PostActivity.this);
		
		@Override
		protected void onPreExecute() {
			this.dialog.setMessage("获取中...");
			this.dialog.show();
		}
		@Override
        protected JSONObject doInBackground(Integer... params) {
			String rstString;
			JSONObject rstJsonObject = null;
			
			rstString = HttpRequest.sendGet(API_ROOT + "/feed/" + String.valueOf(postId), null);
			try {
	            rstJsonObject = new JSONObject(rstString);
            } catch (JSONException e) {
            	Log.e("PostActivity", "Cant parse JSONString: " + rstString);
            	e.printStackTrace();
            }
			
			return rstJsonObject;
        }
		
		@Override
		protected void onPostExecute(JSONObject result) {
			if (this.dialog.isShowing())
				this.dialog.dismiss();
			
			String stat;
			try {
	            stat = result.getString("stat");
	            if (stat.equals("ok")) {
	            	JSONObject postJson = result.getJSONObject("data");
	            	postModel = new ModelFeed();
	            	postModel.setId(postJson.getInt("id"));
	            	postModel.setTitle(postJson.getString("title"));
	            	postModel.setAuthor(postJson.getString("author"));
	            	postModel.setDatetime(postJson.getString("dateinfo"));
	            	postModel.setContnet(postJson.getString("content"));
	            	
	            	initView();
	            } else if (stat.equals("fail")) {
	            	Toast.makeText(PostActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
	            } else {
	            	Toast.makeText(PostActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
	            }
            } catch (JSONException e) {
            	Log.e("PostActivity", "Error with JSONObject: " + result.toString());
            	e.printStackTrace();
            }
		}
		
	}
}
