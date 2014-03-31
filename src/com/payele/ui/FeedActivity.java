package com.payele.ui;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.payele.storage.ModelFeed;
import com.payele.utils.Application_;
import com.payele.utils.HttpRequest;


public class FeedActivity extends Activity {
	private static String API_ROOT = "http://payele.sinaapp.com/api/front/feed";
	
	private FeedAdapter adapter;
	private ArrayList<ModelFeed> feedObjects = new ArrayList<ModelFeed>();
	
	private ListView listView;
	private TextView titleView;
	private TextView dateTimeView;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.feed_activity);
		
		new FeedFetcher().execute();
		Application_.getInstatnce().addActivity(this);
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
	
	private void initList() {
		listView = (ListView) findViewById(R.id.feed_container);
		adapter = new FeedAdapter();
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
            public void onItemClick(AdapterView<?> parent, View v, int position,
                    long id) {
				TextView tv = (TextView) v.findViewById(R.id.feed_title);
				Bundle bundle = new Bundle();
				int feedId = Integer.parseInt(tv.getTag().toString());
				
				bundle.putInt("postId", feedId);
				Intent intent = new Intent(FeedActivity.this, PostActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
				FeedActivity.this.finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
		});
	}
	
	
	
	private class FeedAdapter extends BaseAdapter {

		@Override
	    public int getCount() {
		    // TODO Auto-generated method stub
		    return feedObjects.size();
	    }
	
		@Override
	    public Object getItem(int position) {
		    // TODO Auto-generated method stub
		    return feedObjects.get(position);
	    }
	
		@Override
	    public long getItemId(int position) {
		    // TODO Auto-generated method stub
		    return position;
	    }
		
		

	
		@Override
	    public View getView(int position, View convertView, ViewGroup parent) {
			
			String title;
			String datetime;
			int feedId;
			
			convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.feed_item, null);
			
			title = feedObjects.get(position).getTitle();
			datetime = feedObjects.get(position).getDatetime();
			feedId = feedObjects.get(position).getId();
			
			titleView = (TextView)convertView.findViewById(R.id.feed_title);
			dateTimeView = (TextView)convertView.findViewById(R.id.feed_datetime);
			
			titleView.setText(title);
			dateTimeView.setText(datetime);
			
			titleView.setTag(String.valueOf(feedId));
			
			return convertView;
	    }
	}
	
	private class FeedFetcher extends AsyncTask<String, String, JSONObject> {
		
		private ProgressDialog dialog = new ProgressDialog(FeedActivity.this);
		
		@Override
		protected void onPreExecute() {
			this.dialog.setMessage("获取中...");
			this.dialog.show();
		}
		
		@Override
        protected  JSONObject doInBackground(String... params) {
	        JSONObject rstJsonObject = null;
	        String rstString;
	        rstString = HttpRequest.sendGet(API_ROOT + "/page/1", null);
	        try {
	            rstJsonObject = new JSONObject(rstString);
            } catch (JSONException e) {
            	e.printStackTrace();
            }
	        
	        return rstJsonObject;
        }
		
		@Override
		protected void onPostExecute(JSONObject result) {
			String stat;
			JSONObject rstData;
			JSONArray rstPosts;
			JSONObject feedObj;
			ModelFeed feedModel;
			int i = 0;
			
			if (this.dialog.isShowing())
				this.dialog.dismiss();
			
			try {
	            stat = result.getString("stat");
            
	            if (stat.equals("ok")){  // success
					rstData = result.getJSONObject("data");
					rstPosts = rstData.getJSONArray("posts");
					for (i = 0; i < rstData.getInt("count"); i++) {
						feedObj = (JSONObject) rstPosts.get(i);
						
						feedModel = new ModelFeed();
	                    feedModel.setId(feedObj.getInt("id"));
	                    feedModel.setTitle(feedObj.getString("title"));
	                    feedModel.setDatetime((feedObj.getString("updated")));
                    
						feedObjects.add(feedModel);
					}
					initList();
				} else if (stat.equals("fail")) {
					Toast.makeText(FeedActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(FeedActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
            	Log.e("FeedActivity_onPoseExecut", "cant save feed with object:" + result.toString());
            	e.printStackTrace();
            }
			
		}
		
	}
}
