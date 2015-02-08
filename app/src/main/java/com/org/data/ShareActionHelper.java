package com.org.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.org.receiptreport.R;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 分享
 * @author Andy.Li
 *
 */
public class ShareActionHelper{
	public final static String LOGTAG = "shareAction";
	public final static String FACEBOOK_PACKAGE = "com.facebook.katana";
	private Context context;
	private Bitmap cacheBitmap;
	Uri fileUri;
	ImageButton shareView;
	ListPopupWindow  popupWindow;
	ShareAdapter shareAdapter;
	View actionview;
	int mListPopupWidth;
	public ShareActionHelper(Context context) {
		this.context = context;
		shareView = new ImageButton(context);    
		shareView.setImageResource(android.R.drawable.ic_menu_share);
		shareView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showPopupWindow();
			}
		});
		Resources resources = context.getResources();
		mListPopupWidth = resources.getDisplayMetrics().widthPixels*3/5;
	}
	
	public View getView(){
		return shareView;
	}
	
	

	public List<ResolveInfo> getActionLists(){
		PackageManager manager = context.getPackageManager();
		Intent   shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
		shareIntent.setType("image/*");
		List<ResolveInfo> apps = manager.queryIntentActivities(
				shareIntent, 0);
		Collections.sort(apps, new Comparator<ResolveInfo>() {
			@Override
			public int compare(ResolveInfo lhs, ResolveInfo rhs) {
				// TODO Auto-generated method stub
				int point1 = lhs.activityInfo.packageName.equals(FACEBOOK_PACKAGE)?1:0;
				int point2 = rhs.activityInfo.packageName.equals(FACEBOOK_PACKAGE)?1:0;
				return point2-point1;
			}
		});
		return apps;
	}
	
	public void showPopupWindow(){
		ListPopupWindow  listPopupWindow  = getListPopupWindow();	
		if(listPopupWindow.isShowing())
			return;
		listPopupWindow.setContentWidth(mListPopupWidth);
		listPopupWindow.show();
	} 
	
	public ListPopupWindow getListPopupWindow(){
		if(popupWindow==null){
			popupWindow  = new ListPopupWindow(context);
			shareAdapter = new ShareAdapter(context); 
			shareAdapter.setData(getActionLists());
			popupWindow.setAnchorView(shareView);
			popupWindow.setAdapter(shareAdapter);
			popupWindow.setModal(true);
			popupWindow.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					sendMessage(shareAdapter.getItem(position));
				}
			});
		}
		return popupWindow;
	}
	
	public void sendMessage(ResolveInfo info){
		if(info==null)
			return;
		
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);	
			sendIntent.setType("image/*");	
			sendIntent.putExtra(Intent.EXTRA_STREAM, captureScreen());	
			sendIntent.setComponent(getComponentName(info));
			context.startActivity(sendIntent);

		ListPopupWindow  listPopupWindow  = getListPopupWindow();	
		listPopupWindow.dismiss();
	} 
	private ComponentName getComponentName(ResolveInfo info){
		return new ComponentName(
				info.activityInfo.applicationInfo.packageName,
				info.activityInfo.name);
	}
	
	public  void destoryTmpShareFile(){
	
			ShareActionHelper mShareActionHelper = new ShareActionHelper(context);
			String path = mShareActionHelper.getAvailablePath()+File.separator+mShareActionHelper.getFileName();
			File file = new File(path);
			if(file.exists())
				file.delete();
	}
	private  String getAvailablePath(){
		Boolean isSDExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		String path = "";
		if(isSDExist){
			path = Environment.getExternalStorageDirectory().getAbsolutePath();
		}
		else{
			path = Environment.getDataDirectory().getAbsolutePath() ;
		}
		return path;
	}
    
	private String getFileName(){
		return context.getString(R.string.app_name)+".png";
	}

	public  Uri captureScreen(){
		View vi = ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
		vi.destroyDrawingCache();
		vi.setDrawingCacheEnabled(true);
		cacheBitmap=Bitmap.createBitmap(vi.getDrawingCache());
		String path = getAvailablePath()+File.separator+getFileName();
		File file = new File(path);
		if(file.exists())
			file.delete();
		try{	
			file.createNewFile();
			FileOutputStream fos =  new FileOutputStream(file);
			cacheBitmap.compress(CompressFormat.PNG, 100, fos) ;
			fos.flush();
			fos.close();
		}catch(IOException e){
			e.printStackTrace();
            Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show();
		}

		return Uri.fromFile(file) ;
	}
    public void setShareIntent() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, captureScreen());
        context.startActivity(Intent.createChooser(intent, "分享到:"));
    }
	private class ShareAdapter extends BaseAdapter{
		List<ResolveInfo> data;
		Context context;
		PackageManager manager;
		public void setData(List<ResolveInfo> data) {
			this.data = data;
		}	
		public ShareAdapter(Context context){
			this.context = context;
			this.manager = context.getPackageManager();
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if(data!=null)
				return data.size();
			return 0;
		}

		@Override
		public ResolveInfo getItem(int position) {
			// TODO Auto-generated method stub
			if(data!=null){
				if(position<data.size())
					return data.get(position);
			}
			return null;
		}
        
		@Override
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub
			return super.getItemViewType(position);
		}
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
		public class ViewHolder{
			TextView tv;
			ImageView iv;
		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder vh;
			if(convertView==null){
				vh = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.shareitem, null);
				vh.tv = (TextView) convertView.findViewById(R.id.title);
				vh.iv = (ImageView) convertView.findViewById(R.id.icon);
				convertView.setTag(vh);
			}else{
				vh = (ViewHolder) convertView.getTag();
			}

			ResolveInfo info = data.get(position);
			vh.tv.setText(info.loadLabel(manager));
			vh.iv.setImageDrawable(info.activityInfo.loadIcon(manager));
			return convertView;
		}
	}
}
