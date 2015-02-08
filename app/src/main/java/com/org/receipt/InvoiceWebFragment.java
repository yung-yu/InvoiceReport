package com.org.receipt;

import com.org.receiptreport.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

@SuppressLint("SetJavaScriptEnabled")
public class InvoiceWebFragment extends Fragment {
	public final String URL = "http://invoice.etax.nat.gov.tw/";
	WebView webView;
	TextView tv_progress;
    private View findViewById(int id){
    	     return getView().findViewById(id);
    }
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		webView = (WebView) findViewById(R.id.webView1);
		tv_progress = (TextView) findViewById(R.id.textView1);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDisplayZoomControls(false);
		webView.getSettings().setLoadWithOverviewMode(false);
		webView.getSettings().setSupportZoom(true);

		webView.setWebChromeClient(new WebChromeClient(){

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				super.onProgressChanged(view, newProgress);
				
				 tv_progress.setText("載入進度 "+newProgress+"%");
				if(newProgress<100){
					tv_progress.setVisibility(View.VISIBLE);
				}else{
					tv_progress.setVisibility(View.GONE);
				}
			}
		});
		webView.setWebViewClient(new WebViewClient(){

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				return super.shouldOverrideUrlLoading(view, url);
			}
			
			
		});
		webView.loadUrl(URL);
		webView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub
				webView.reload();
				return true;
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.webviewfragment, null);
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

	

}
