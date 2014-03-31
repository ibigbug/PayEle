package com.payele.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.payele.storage.DBHelper;
import com.payele.storage.DBManager;
import com.payele.storage.ModelAccount;
import com.payele.storage.ModelApp;
import com.payele.utils.Application_;

/**
 * 
 * @ClassName: MainActivity 
 * @Description: 主界面
 * @author Yuwei Ba <i@xiaoba.me>
 * @date Mar 10, 2014 6:05:16 PM 
 *
 */


public class MainActivity extends Activity {
	private DBHelper dbHelper = null;
	private SQLiteDatabase db =null;
	private DBManager mgr = null;
	private ModelApp app = null;
	
	private int currentUid;  // used if logined
	private ModelAccount currentAccount = null;
	
	private GridView  gridView;
	private Button loginBtn;
	private TextView usernameView;
	private TextView usageView;
	private TextView remainView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_activity);
		
		initDB();	
		initView();
		
		Application_.getInstatnce().addActivity(this);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			Application_.getInstatnce().exit();
	        
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	private void initView() {
		loginBtn = (Button) findViewById(R.id.login_button_nav);
    	usernameView = (TextView) findViewById(R.id.main_username_value);
    	usageView = (TextView) findViewById(R.id.main_usage_value);
    	remainView = (TextView) findViewById(R.id.main_remain_value);
    	
    	app = mgr.loadAppInfo();
    	
	    if(app.getIsLogined() == 1) {
	    	// Has Logined
	    	currentUid = app.getCurrentUid();
	    	currentAccount = mgr.retriveAccount(currentUid);
	    	
	    	loginBtn.setText("退出");
	    	
	    	loginBtn.setOnClickListener(logOutListener);
	    	
	    	
	    	usernameView.setText(currentAccount.getScreen_name());
	    	usageView.setText("1000000");
	    	remainView.setText("-100000");
	    } else 
	    	loginBtn.setOnClickListener(toLoginViewListener);
		
    	
	    
		gridView = (GridView) findViewById(R.id.main_grid);
		gridView.setAdapter(new ImageAdapter(this));
		
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				if (position == 0) {  // 业界动态
					Intent intent = new Intent(MainActivity.this, FeedActivity.class);
					startActivity(intent);
					MainActivity.this.finish();
					overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

				} else if (position == 3) {  // 个人信息
					if (app.isLogined == 0)
						Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
					else {
						Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
						startActivity(intent);
						MainActivity.this.finish();
					}
				} else if (position == 1) { // 用电查询
					if (app.isLogined ==0)
						Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
					else {
						Intent intent = new Intent(MainActivity.this, QueryActivity.class);
						startActivity(intent);
						MainActivity.this.finish();
					}
				} else {
					Toast.makeText(MainActivity.this, "别点我", Toast.LENGTH_SHORT).show();
				}					
			}
		});
	}
	
	private void initDB() {
		
		mgr = new DBManager(this);
		dbHelper = new DBHelper(this);
		db = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("app_version", getResources().getString(R.string.app_version));
		db.update("app", cv, "_id = ?", new String[]{"1"});
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		//Release DB;
		mgr.closeDB();
	}
	
	
	private View.OnClickListener toLoginViewListener = new View.OnClickListener() {
		
		public void onClick(View view) {
			Intent intent = new Intent(MainActivity.this, LoginActivity.class);
			startActivity(intent);
				
			overridePendingTransition(
				R.anim.slide_in_left,
				R.anim.slide_out_left);
		}
	};
	
	private View.OnClickListener logOutListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View view) {
			mgr.logoutUser();
			mgr.deleteAccount(currentUid);
			refresh();
		}
	};
	
	private void refresh () {
		Toast.makeText(this, "退出成功", Toast.LENGTH_SHORT).show();
		loginBtn.setOnClickListener(toLoginViewListener);
		loginBtn.setText("登录");
		usernameView.setText(getResources().getString(R.string.field_username_value));
		usageView.setText(getResources().getString(R.string.field_usage_value));
		remainView.setText(getResources().getString(R.string.field_remain_value));
	}
	
	private class ImageAdapter extends BaseAdapter {
		private Context mContext;

	    public ImageAdapter(Context c) {
	        mContext = c;
	    }

	    public int getCount() {
	        return mThumbIds.length;
	    }

	    public Object getItem(int position) {
	        return null;
	    }

	    public long getItemId(int position) {
	        return 0;
	    }
	    
	    public View getView(int position, View convertView, ViewGroup parent) {
	    	View v;
			if(convertView==null){
				LayoutInflater li = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
				v = li.inflate(R.layout.grid_item, null);
				TextView tv = (TextView)v.findViewById(R.id.icon_text);
				tv.setText(mTexts[position]);
				ImageView iv = (ImageView)v.findViewById(R.id.icon_image);
				iv.setPadding(8, 8, 8, 8);
				iv.setImageResource(mThumbIds[position]);
							
			} else {
				v = convertView;
			}
			return v;
	    }

	    private Integer[] mThumbIds = {
	    		R.drawable.ic_news,
	    		R.drawable.ic_query,
	    		R.drawable.ic_cart,
	    		R.drawable.ic_profile
	    };
	    
	    private String[] mTexts = {
	    		"业界动态",
	    		"用电查询",
	    		"在线缴费",
	    		"个人信息"
	    };
	}

}