package com.payele.ui;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;

import com.payele.utils.Application_;


public class QueryActivity extends Activity{
	
	public final static String API_ROOT = "http://payele.sinaapp.com/api/front";
	private static TableLayout resultTable;
	private static EditText dateFromText;
	private static EditText dateToText;
	public static EditText currentDateEdit;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.query_activity);
		
		initView();
		
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
	
	private void initView() {
		resultTable = (TableLayout) findViewById(R.id.query_result_table);
		dateFromText = (EditText) findViewById(R.id.query_date_from);
		dateToText = (EditText) findViewById(R.id.query_date_to);
		
		resultTable.setVisibility(View.GONE);
	}
	
	public void showDatePicker (View v) {
		if (v.getId() == R.id.query_date_from)
			currentDateEdit = dateFromText;
		currentDateEdit = dateToText;
		DialogFragment newFragment = new DatePickerFragment();
	    newFragment.show(getFragmentManager(), "datePicker");
	}
	
	public void doQuery(View v) {}
	
	
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
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}
		
		public void onDateSet(DatePicker view, int year, int month, int day) {
			resultTable.setVisibility(View.VISIBLE);
		}
	}
	
	public class TableAdapter extends BaseAdapter {

		@Override
        public int getCount() {
	        // TODO Auto-generated method stub
	        return 0;
        }

		@Override
        public Object getItem(int arg0) {
	        // TODO Auto-generated method stub
	        return null;
        }

		@Override
        public long getItemId(int arg0) {
	        // TODO Auto-generated method stub
	        return 0;
        }

		@Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
	        // TODO Auto-generated method stub
	        return null;
        }}
}
