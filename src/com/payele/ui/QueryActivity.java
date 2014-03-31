package com.payele.ui;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.payele.storage.DBManager;
import com.payele.storage.ModelApp;
import com.payele.storage.ModelUsage;
import com.payele.utils.Application_;
import com.payele.utils.HttpRequest;


public class QueryActivity extends Activity{
	
	public final static String API_ROOT = "http://payele.sinaapp.com/api/front";
	
	private DBManager mgr;
	private ModelApp app;
	private int currentUid;
	
	private static TableAdapter adapter;
	private static TableLayout resultTable;
	private static EditText dateFromText;
	private static EditText dateToText;
	private static EditText currentDateEdit;
	private static ArrayList<ModelUsage> usageList = new ArrayList<ModelUsage>();
	private static OnTouchListener touchListener;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.query_activity);
		
		initDB();
		initView();
		
		Application_.getInstatnce().addActivity(this);
	}
	
	@Override
	protected void onDestroy() {
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
	
	private void initDB() {
		mgr = new DBManager(this);
		app = mgr.loadAppInfo();
		currentUid = app.getCurrentUid();
	}
	
	private void initView() {
		resultTable = (TableLayout) findViewById(R.id.query_result_table);
		dateFromText = (EditText) findViewById(R.id.query_date_from);
		dateToText = (EditText) findViewById(R.id.query_date_to);
		
		resultTable.setVisibility(View.GONE);
		dateFromText.setInputType(InputType.TYPE_NULL);
		dateToText.setInputType(InputType.TYPE_NULL);
		
		touchListener = new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				if (v.getId() == R.id.query_date_from)
					currentDateEdit = (EditText) findViewById(R.id.query_date_from);
				else
					currentDateEdit = (EditText) findViewById(R.id.query_date_to);
				if (event.getAction() == MotionEvent.ACTION_UP) {
					v.requestFocus();
					showDatePicker();
					return true;
				}
				return false;
			}
		};
		
		dateFromText.setOnTouchListener(touchListener);
		dateToText.setOnTouchListener(touchListener);
	}
	
	private void showDatePicker () {
		
		DialogFragment newFragment = new DatePickerFragment();
	    newFragment.show(getFragmentManager(), "datePicker");
	}
	
	public void doQuery(View v) {
		if (!validateDate())
			Toast.makeText(this, "日期选择有误", Toast.LENGTH_SHORT).show();
		else {
			String queryUrl = "/usage/query/" + String.valueOf(currentUid) +
					"/" + dateFromText.getText().toString() +
					"/" + dateToText.getText().toString();
			new QueryTask().execute(queryUrl);
		}
	}
	
	private boolean validateDate() {
	    String fromDateString = dateFromText.getText().toString();
	    String toDateString = dateToText.getText().toString();
	    return fromDateString.compareTo(toDateString) < 0;
    }
	
	private void showResult() {
		resultTable.setVisibility(View.VISIBLE);
		adapter = new TableAdapter();
		for (int i = 0; i < adapter.getCount(); i++) {
	        resultTable.addView(adapter.getView(i, null, resultTable));
        }
	}
	
	
	public static class DatePickerFragment extends DialogFragment
    implements DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			
			// Create a new instance of DatePickerDialog and return it
			
			DatePickerDialog picker = new DatePickerDialog(getActivity(), this, year, month, day);
			picker.getDatePicker().setMaxDate(c.getTimeInMillis());
			return picker;
		}
		
		public void onDateSet(DatePicker view, int year, int month, int day) {
			currentDateEdit.setText(formatString(year, month, day));
		}
		
		private String formatString (int year, int month, int day) {
			return new String(new StringBuffer().append(year).append('-').append((month + 1) % 12).append('-').append(day));
		}
	}
	
	public class TableAdapter extends BaseAdapter {
		private TextView dateView;
		private TextView usageView;
		
		@Override
        public int getCount() {
	        // TODO Auto-generated method stub
	        return usageList.size();
        }

		@Override
        public Object getItem(int position) {
	        // TODO Auto-generated method stub
	        return usageList.get(position);
        }

		@Override
        public long getItemId(int position) {
	        // TODO Auto-generated method stub
	        return position;
        }

		@Override
        public View getView(int position, View convertView, ViewGroup parent) {
			String date;
			int usage;
	        convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.result_item, null);
	        
	        date = usageList.get(position).getDate();
	        usage = usageList.get(position).getUsage();
	        
	        dateView = (TextView) convertView.findViewById(R.id.result_item_date);
	        usageView = (TextView) convertView.findViewById(R.id.result_item_usage);
	        
	        dateView.setText(date);
	        usageView.setText(String.valueOf(usage));
	        
	        return convertView;
        }
	}
	
	private class QueryTask extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog dialog = new ProgressDialog(QueryActivity.this);
		
		@Override
		protected void onPreExecute() {
			dialog.setMessage("查询中...");
			dialog.show();
		}
		@Override
        protected JSONObject doInBackground(String... params) {
			String queryString = params[0];
			JSONObject rstJsonObject = null;
			String rstString = HttpRequest.sendGet(API_ROOT + queryString, null);
			
			try {
	            rstJsonObject = new JSONObject(rstString);
	            
            } catch (JSONException e) {
            	Log.e("Query_activity_line_247", "Cant parse JSONString: "+ rstString);
            	e.printStackTrace();
            }
	        
			return rstJsonObject;
        }
		
		@Override
		protected void onPostExecute(JSONObject result) {
			if (dialog.isShowing())
				dialog.dismiss();
			String stat;
			JSONObject dataObject;
			JSONArray usageArray;
			JSONObject usageObj;
			ModelUsage usageModel;
			
			try {
	            stat = result.getString("stat");
	            if (stat.equals("ok")) {
	            	dataObject = result.getJSONObject("data");
	            	usageArray = dataObject.getJSONArray("usages");
	            	for (int i = 0; i < dataObject.getInt("count"); i++){
	            		usageObj = (JSONObject) usageArray.get(i);
	            		ContentValues cv = new ContentValues();
	            		cv.put("date", usageObj.getString("when"));
	            		cv.put("usage", usageObj.getInt("total"));
	            		usageModel = new ModelUsage(cv);
	            		
	            		usageList.add(usageModel);
	            	}
	            	showResult();
	            } else {
	            	Toast.makeText(QueryActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
	            }
            } catch (JSONException e) {
            	Log.e("Query-activity-line-263", "Error with JSON: " + result.toString());
            	e.printStackTrace();
            }
		}
		
	}
}
