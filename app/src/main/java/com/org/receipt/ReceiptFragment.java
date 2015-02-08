package com.org.receipt;


import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.org.data.ReceiptData;
import com.org.data.SharePerferenceHelper;
import com.org.main.MainActivity;
import com.org.receipthelper.Period;
import com.org.receipthelper.ReceiptAwards;
import com.org.receiptreport.R;
import com.org.receiptreport.Receipt;



public class ReceiptFragment extends Fragment{
    ReceiptData mReceiptData;
    Button bt_scan,bt_awards;
    ListView listview;
    ReciptAdapter adapter;
    List<Receipt> datas = new ArrayList<Receipt>();
    Activity activity;
    ImageLoader mImageLoader;
    DisplayImageOptions options;
    public ReceiptFragment(ReceiptData mReceiptData){
        this.mReceiptData = mReceiptData;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.receiptlist, null);
    }
    private View findViewById(int id){
        return getView().findViewById(id);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();
        init(savedInstanceState);

    }

    public void init(Bundle savedInstanceState) {
        listview =(ListView)findViewById(R.id.listView1);
        initImageLoader();
        adapter = new ReciptAdapter(activity, datas);
        listview.setAdapter(adapter);
        updateCurrntValue();
    }
    public void initImageLoader(){
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(activity).build();
        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(config);
        options = new DisplayImageOptions.Builder()
                .displayer(new SimpleBitmapDisplayer()) // default
                .build();
    }
    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    public void updateCurrntValue(){
        updateList(
                SharePerferenceHelper.getIntent(activity).getboolean("display_all", true),
                String.valueOf(
                        MainActivity.BASE_YEAR
                                +SharePerferenceHelper.getIntent(activity).getInt("display_year", 0)),
                SharePerferenceHelper.getIntent(activity).getInt("display_month", 0));
    }
    public void updateList(boolean displayAll,String year,int period){
        if(displayAll){
            updateAllValue();
        }else{
            updateValue( year, period);
        }
    }
    public void updateAllValue(){
        datas.clear();
        datas.addAll(mReceiptData.getReceiptList());
        adapter.notifyDataSetChanged();
    }
    public void updateValue(String year,int period){
        datas.clear();
        datas.addAll(mReceiptData.getReceiptList(year,period));
        adapter.notifyDataSetChanged();
    }
    public List<Receipt> getCurrentDataList(){
        return datas;
    }
    private class ReciptAdapter extends BaseAdapter{
        Context context;
        List<Receipt> datas;
        public ReciptAdapter(Context context,List<Receipt> datas){
            this.context = context;
            this.datas = datas;
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if(datas!=null)
                return datas.size();
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

        class ViewHolder{
            TextView tv_id;
            TextView tv_period;
            TextView tv_createDate;
            ImageView iv;
            ImageView edite;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder vh;
            if(convertView==null){
                vh = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.receipt, null);
                vh.tv_id = (TextView) convertView.findViewById(R.id.textView1);
                vh.tv_period = (TextView) convertView.findViewById(R.id.textView2);
                vh.tv_createDate = (TextView) convertView.findViewById(R.id.textView3);
                vh.iv = (ImageView) convertView.findViewById(R.id.imageView1);
                vh.edite = (ImageView)convertView.findViewById(R.id.edit);
                convertView.setTag(vh);
            }else{
                vh = (ViewHolder) convertView.getTag();
            }
            if(datas!=null)
                if(position < datas.size()){
                    vh.tv_id.setText(datas.get(position).getReceiptID());
                    vh.tv_period.setText(getDateRange(datas.get(position).getCreateDate(),
                            datas.get(position).getPeriod()));
                    vh.tv_createDate.setText(datas.get(position).getCreateDate());
                    String imageUri;
                    if(datas.get(position).getStatus()){
                        imageUri = "drawable://" + ReceiptAwards.getImage(datas.get(position).getAwards());
                        mImageLoader.displayImage(imageUri,vh.iv ,options);
                    }else{
                        imageUri = "drawable://" + R.drawable.wait;
                    }
                    mImageLoader.displayImage(imageUri,vh.iv ,options);
                    final int width = convertView.getWidth();
                    vh.edite.setClickable(true);
                    vh.edite.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            getListPopupWindow(v,datas.get(position)).show();
                        }
                    });
                }

            return convertView;
        }
        public String getDateRange(String createDate,int period){
            int year = Integer.valueOf(createDate.substring(0, 3));
            String msg = year+getString(R.string.year_unit);
            msg+= Period.values()[period].getString(context);
            return msg;
        }
        ListPopupWindow popupWindow;

        public ListPopupWindow getListPopupWindow(View  view , final Receipt receipt){

            popupWindow  = new ListPopupWindow(context);
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            popupWindow.setContentWidth(dm.widthPixels/4);
            popupWindow.setAnchorView(view);
            popupWindow.setAdapter(new ArrayAdapter<String>(context,
                    android.R.layout.simple_list_item_1,
                    context.getResources().getStringArray(R.array.edit)));
            popupWindow.setModal(true);

            popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // TODO Auto-generated method stub
                    switch (position){
                        case 0:
                            showEditeReceipt(receipt);
                            break;
                        case 1:
                            showDeleteTip(receipt);
                            break;
                    }
                    popupWindow.dismiss();
                }
            });
            return popupWindow;
        }
        private void showDeleteTip(final Receipt receipt){
            AlertDialog.Builder ab = new AlertDialog.Builder(context);
            ab.setTitle(R.string.delete_tip);
            String msg = getString(R.string.delete_tip_receipt);
            msg = msg.replace("&id",receipt.getReceiptID());
            msg = msg.replace("&date",receipt.getCreateDate());
            ab.setMessage(msg);
            ab.setNegativeButton(R.string.alert_cancel,new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            ab.setPositiveButton(R.string.alert_ok,new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mReceiptData.deleteReceipt(receipt);
                    updateCurrntValue();
                    Toast.makeText(context,getString(R.string.delete_success),Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }
            });
            ab.create().show();
        }
        private void showEditeReceipt(final Receipt receipt){
            AlertDialog.Builder ab = new AlertDialog.Builder(context);
            ab.setTitle(R.string.repair_receipt);
            View convertView = LayoutInflater.from(context).inflate(R.layout.inputdialog, null);
            final EditText et_code = (EditText) convertView.findViewById(R.id.et_code);
            final EditText et_number = (EditText) convertView.findViewById(R.id.et_number);
            final DatePicker datePicker = (DatePicker) convertView.findViewById(R.id.datePicker1);
            et_code.setText(receipt.getReceiptID().substring(0,2));
            et_number.setText(receipt.getReceiptID().substring(2));
            datePicker.setCalendarViewShown(false);
            int year =Integer.valueOf(receipt.getCreateDate().substring(0,3))+1911;
            int month =Integer.valueOf(receipt.getCreateDate().substring(3, 5));
            int day =Integer.valueOf(receipt.getCreateDate().substring(5));
            datePicker.init(
                    year,
                    month-1,
                    day,null);
            ab.setView(convertView);
            ab.setNegativeButton(R.string.alert_cancel, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    arg0.cancel();
                }
            });
            ab.setPositiveButton(R.string.alert_ok, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    String date = YearFix(datePicker.getYear());
                    date += DateFix(datePicker.getMonth()+1);
                    date += DateFix(datePicker.getDayOfMonth());
                    String receiptId = et_code.getText().toString()+et_number.getText().toString();
                    if(!date.equals(receipt.getCreateDate())
                            ||!(receiptId).equals(receipt.getReceiptID())) {
                        receipt.setCreateDate(date);
                        receipt.setReceiptID(receiptId);
                        mReceiptData.updateReceipt(receipt);
                        receipt.setStatus(false);
                        updateCurrntValue();
                        Toast.makeText(context,getString(R.string.repair_success),Toast.LENGTH_SHORT).show();
                    }
                    arg0.cancel();

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
    }

}
