package com.org.main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.org.data.ReceiptData;
import com.org.data.ReceiptQuery;
import com.org.data.ShareActionHelper;
import com.org.data.SharePerferenceHelper;
import com.org.receipt.InvoiceWebFragment;
import com.org.receipt.ReceiptFragment;
import com.org.receipt.ReportActivity;
import com.org.receipthelper.Period;
import com.org.receipthelper.ReceiptAwards;
import com.org.receipthelper.ReceiptHelper;
import com.org.receipthelper.WinningNumber;
import com.org.receiptreport.R;
import com.org.receiptreport.Receipt;
import com.org.receiptreport.Report;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends BaseAcivity{
	public final static int BASE_YEAR = 103;
	Button[] Bt_function;
	TextView tv_result;
	ReceiptData receiptData;
	ReceiptFragment receiptFragment;
	InvoiceWebFragment mInvoiceWebFragment;
	Toolbar mToolbar;
	ReceiptData mReceiptData;
	ViewPager mViewPager;
	MainFragmentAdapter mMainFragmentAdapter;
	List<Fragment> fragmentList;
	ReportActivity mReportFragment;
	ShareActionHelper mShareActionHelper;
    Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
        context = this;
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mViewPager  = (ViewPager) findViewById(R.id.viewpager);
		setSupportActionBar(mToolbar);
		
		mReceiptData = new ReceiptData(this);
		mToolbar.setOnMenuItemClickListener(new menuItemClick());
		receiptFragment = new ReceiptFragment(mReceiptData);
		mInvoiceWebFragment = new InvoiceWebFragment();
		fragmentList = new ArrayList<Fragment>();
		fragmentList.add(receiptFragment);
		fragmentList.add(mInvoiceWebFragment);
		mMainFragmentAdapter = new MainFragmentAdapter(
				getSupportFragmentManager(), fragmentList);
		mViewPager.setAdapter(mMainFragmentAdapter);
		mShareActionHelper = new ShareActionHelper(this);

	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		
		return true;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0) {

			if (resultCode == Activity.RESULT_OK) {
				String result = data.getStringExtra("SCAN_RESULT");
				if(ReceiptHelper.paserQRcode(mReceiptData,result)==1){
					showNextDialog(R.string.add_success);
					receiptFragment.updateCurrntValue();
				}else{
					showNextDialog(R.string.add_isExitTip);
				}
			} else if (resultCode == Activity.RESULT_CANCELED) {
			}
		}
	}
	private void showNextDialog(int id){
		showNextDialog(getString(id));
	}
	private void showNextDialog(String msg){
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle(msg);
		ab.setNegativeButton(R.string.alert_cancel, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				arg0.cancel();
			}
		});
		ab.setPositiveButton(R.string.continue_scan, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				toScan();
				arg0.cancel();
			}
		});
		ab.create().show();
	}

	// Call to update the share intent
	private void setShareIntent() {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_SEND);
		intent.setType("image/*");
		intent.putExtra(Intent.EXTRA_STREAM, mShareActionHelper.captureScreen());
		startActivity(Intent.createChooser(intent, getString(R.string.share_title)));
	}

	Calendar calender;
	DatePicker datePicker;
	EditText et_code,et_number;
	//手動新增
	private void showInputReceipt(){
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle(R.string.add_Receipt_title);
		View convertView = LayoutInflater.from(this).inflate(R.layout.inputdialog, null);
		et_code = (EditText) convertView.findViewById(R.id.et_code);
		et_number = (EditText) convertView.findViewById(R.id.et_number);
		datePicker = (DatePicker) convertView.findViewById(R.id.datePicker1);
		calender = Calendar.getInstance();
		datePicker.setCalendarViewShown(false);
		datePicker.init(
				calender.get(Calendar.YEAR), 
				calender.get(Calendar.MONTH), 
				calender.get(Calendar.DAY_OF_MONTH),null);
		ab.setView(convertView);
		ab.setNegativeButton(R.string.alert_cancel, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				arg0.cancel();
			}
		});
		ab.setPositiveButton(R.string.alert_ok, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				String date = YearFix(datePicker.getYear());
				date += DateFix(datePicker.getMonth()+1);
				date += DateFix(datePicker.getDayOfMonth());
				if(saveReceipt(et_code.getText().toString(),et_number.getText().toString(), date)){
					receiptFragment.updateCurrntValue();
					arg0.cancel();
				}
			}
		});
		ab.create().show();
	}
	//西元轉民國
	private  String YearFix(int year){
		return String.valueOf(year-1911);
	}
	//修正日期
	private  String DateFix(int c){
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}
	/**儲存統一發票*/
	private boolean  saveReceipt(String code,String number,String date){
		if(!code.matches("[A-Z][A-Z]")||number.length()!=8){
			Toast.makeText(this, getString(R.string.input_tip_current_number), Toast.LENGTH_SHORT).show();
			return false;
		}

		Receipt receipt = new Receipt();
		receipt.setCreateDate(date);
		receipt.setPeriod(ReceiptHelper.getPeriod(Integer.valueOf(date.substring(3, 5))).ordinal());
		receipt.setReceiptID(code+number);
		receipt.setStatus(false);
		receipt.setAwards(0);
		if(mReceiptData.addReceipt(receipt)==ReceiptData.RECEIPT_IS_EXIST){
			Toast.makeText(this, getString(R.string.add_isExitTip), Toast.LENGTH_SHORT).show();
			return false;
		}
		Toast.makeText(this, getString(R.string.add_success), Toast.LENGTH_SHORT).show();
		return true;
	}
	private class menuItemClick implements Toolbar.OnMenuItemClickListener{

		@Override
		public boolean onMenuItemClick(MenuItem arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getItemId()) {
			case R.id.checkReceipt:
				showCheckReceiptDialog();
				break;
			case R.id.addBySelf:
				showInputReceipt();
				break;
			case R.id.addByScan:
				toScan();
				break;
			case R.id.showlist:
				showListDialog();
				break;
			case R.id.report:
                showReportDialog();
				break;
			case R.id.share:
				setShareIntent();
				break;
            case R.id.delete:
                showDeleteDialog();
                break;
			default:

				break;
			}
			return true;
		}
	}
    public void showDeleteDialog(){
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle(R.string.delete_title);
        View view = LayoutInflater.from(this).inflate(R.layout.listdialog, null);
        sp_year = (Spinner) view.findViewById(R.id.spinner1);
        sp_month = (Spinner) view.findViewById(R.id.spinner2);
        cb_DisplayAll = (CheckBox)view.findViewById(R.id.checkBox1);
        cb_DisplayAll.setVisibility(View.GONE);
        yearlist = new ArrayList<String>();

        for(int i=0;i<20;i++){
            yearlist.add(String.valueOf(BASE_YEAR+i));
        }
        sp_year.setAdapter(new ArrayAdapter<String>(this,
                R.layout.datelistitem, yearlist));


        sp_month.setAdapter(new ArrayAdapter<String>(this,
                R.layout.datelistitem,getResources().getStringArray(R.array.period)));

        ab.setView(view);
        ab.setNegativeButton(R.string.alert_cancel, new OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                arg0.cancel();
            }
        });
        ab.setPositiveButton(R.string.alert_ok, new OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                if(yearlist!=null){
                    final String  yearStr = yearlist.get(sp_year.getSelectedItemPosition());
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    String msg = getString(R.string.delete_tip_message);
                    msg = msg.replace("&year",yearStr);
                    msg = msg.replace("&msg",(Period.values()[sp_month.getSelectedItemPosition()].getString(context)));
                    builder.setMessage(msg);
                    builder.setNegativeButton(R.string.alert_cancel,null);
                    builder.setPositiveButton(R.string.alert_ok,new OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mReceiptData.deleteReceipt(yearStr,sp_month.getSelectedItemPosition());
                            Toast.makeText(context,getString(R.string.delete_success),Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });
                    builder.create().show();
                }

                arg0.cancel();
            }
        });
        ab.create().show();
    }
    public void showReportDialog(){

        final List<Report> list = mReceiptData.getAllReports();
        if(list!=null){
            AlertDialog.Builder ab = new AlertDialog.Builder(this);
            ab.setTitle(R.string.select_award_date);
             ab.setAdapter(new ArrayAdapter<Report>(context, R.layout.datelistitem, list){
                 @Override
                 public View getView(int position, View convertView, ViewGroup parent) {
                     View view = super.getView(position, convertView, parent);
                     TextView tv  = (TextView) view;
                     Report report = getItem(position);
                     tv.setText(report.getYear()+getString(R.string.year_unit)+(Period.values()[report.getPeriod()].getString(context)));
                     return  view;
                 }
             },new OnClickListener(){

                 @Override
                 public void onClick(DialogInterface dialog, int which) {
                     toReport(list.get(which).getYear(),Period.values()[list.get(which).getPeriod()]);
                     dialog.cancel();
                 }
             });
            ab.setPositiveButton(R.string.alert_cancel,null);
            ab.create().show();
        }
        else{
            Toast.makeText(context,getString(R.string.award_repoirt_empty),Toast.LENGTH_SHORT).show();
        }
    }
    public void toReport(String year,Period period){
        Intent it = new Intent(MainActivity.this, ReportActivity.class);
        Bundle bd = new Bundle();
        bd.putString("year", year);
        bd.putInt("peroid", period.ordinal());
        it.putExtras(bd);
        startActivity(it);
    }
	public void toScan(){
		try {

			Intent intent = new Intent(
					"com.google.zxing.client.android.SCAN");
			intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
			startActivityForResult(intent, 0);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(this, "ERROR:" + e,Toast.LENGTH_SHORT).show();

		}
	}
	Spinner sp_year;
	Spinner sp_month;
	CheckBox cb_DisplayAll;		
	List<String> yearlist;
	public void showListDialog(){
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle(R.string.displayContent);
		View view = LayoutInflater.from(this).inflate(R.layout.listdialog, null);
		sp_year = (Spinner) view.findViewById(R.id.spinner1);
		sp_month = (Spinner) view.findViewById(R.id.spinner2);
		cb_DisplayAll = (CheckBox)view.findViewById(R.id.checkBox1);

		yearlist = new ArrayList<String>();

		for(int i=0;i<20;i++){
			yearlist.add(String.valueOf(BASE_YEAR+i));
		}
		sp_year.setAdapter(new ArrayAdapter<String>(this,
				R.layout.datelistitem, yearlist));

		sp_year.setSelection(SharePerferenceHelper.getIntent(this).getInt("display_year", 0));
		sp_month.setAdapter(new ArrayAdapter<String>(this,
                R.layout.datelistitem,getResources().getStringArray(R.array.period)));
		sp_month.setSelection(SharePerferenceHelper.getIntent(this).getInt("display_month", 0));


        cb_DisplayAll.setChecked(
                SharePerferenceHelper.getIntent(this).getboolean("display_all", true));
        ab.setView(view);
		ab.setNegativeButton(R.string.alert_cancel, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				arg0.cancel();
			}
		});
		ab.setPositiveButton(R.string.alert_ok, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				if(yearlist!=null){
					String  yearStr = yearlist.get(sp_year.getSelectedItemPosition());		
					receiptFragment.updateList(cb_DisplayAll.isChecked(), yearStr, sp_month.getSelectedItemPosition());
				}
				SharePerferenceHelper.getIntent(MainActivity.this).setInt("display_year", sp_year.getSelectedItemPosition());
				SharePerferenceHelper.getIntent(MainActivity.this).setInt("display_month", sp_month.getSelectedItemPosition());
				SharePerferenceHelper.getIntent(MainActivity.this).setboolean("display_all", cb_DisplayAll.isChecked());
				arg0.cancel();
			}
		});
		ab.create().show();
	}
	ProgressDialog progressDialog;
	private void showCheckReceiptDialog(){
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(getString(R.string.search_numbers_wait));
        progressDialog.show();
        ReceiptQuery.query(new ReceiptQuery.CallBack(){

            @Override
            public void onLoadNumbers(final String year, final Period period, final WinningNumber numbers) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.cancel();
                        progressDialog.setMessage(getString(R.string.check_Receipt_wait));
                        AlertDialog.Builder ab = new AlertDialog.Builder(context);
                        ab.setTitle(getString(R.string.select_award_date));
                        List<String> data = new ArrayList<String>();
                        data.add(year+" "+getString(R.string.year_unit)+" "+period.getString(context));
                        ab.setAdapter(new ArrayAdapter<String>(context, R.layout.datelistitem, data), new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                progressDialog.show();
                                List<Receipt> datas = mReceiptData.getNotCheckReceiptList(year, period.ordinal());
                                for (int i = 0; i < datas.size(); i++) {
                                    Receipt receipt = datas.get(i);
                                    ReceiptAwards awards = ReceiptHelper.CheckReceiptAwards(numbers, receipt.getReceiptID());
                                    receipt.setAwards(awards.ordinal());
                                    receipt.setStatus(true);
                                    mReceiptData.updateReceipt(receipt);
                                }
                                receiptFragment.updateCurrntValue();
                                update(year, period.ordinal());
                                toReport(year,period);
                                dialog.cancel();
                                progressDialog.cancel();
                            }
                        });
                        ab.setPositiveButton(R.string.alert_cancel, null);
                        ab.create().show();
                    }
                });

            }
        });

	}
	private void update(String year,int period){
		List<Receipt> list = mReceiptData.getReceiptList(year, period);
		int superTal = 0;
		int specialTal = 0;
		int oneTal = 0;
		int twoTal = 0;
		int threeTal = 0;
		int fourTal = 0;
		int fiveTal = 0;
		int sixTal = 0;
		int talcount = 0;
		for(Receipt item:list ){
			if(item.getStatus()){
				talcount++;
				switch (item.getAwards()) {
				case 1:
					sixTal++;
					break;
				case 2:
					fiveTal++;
					break;
				case 3:
					fourTal++;
					break;
				case 4:
					threeTal++;
					break;
				case 5:
					twoTal++;
					break;
				case 6:
					oneTal++;
					break;
				case 7:
					specialTal++;
					break;
				case 8:
					superTal++;
					break;

				default:
					break;
				}
			}
		}
		Report report = new Report();
		report.setYear(year);
		report.setPeriod(period);
		report.setSixAwardsCount(sixTal);
		report.setFiveAwardsCount(fiveTal);
		report.setFourAwardsCount(fourTal);
		report.setThreeAwardsCount(threeTal);
		report.setTwoAwardsCount(twoTal);
		report.setOneAwardsCount(oneTal);
		report.setSpecialAwardsCount(specialTal);
		report.setSuperAwardsCount(superTal);
		report.setTalcount(talcount);
		mReceiptData.updateReport(report);
	}
}
