package com.org.receipthelper;

import com.org.receiptreport.R;

import android.content.Context;

public enum  Period {
	one_TwoMonth,
	three_FourMonth,
	five_SixMonth,
	seven_EightMonth,
	nine_TenMonth,
	eleven_TwelveMonth;
	
	public String getString(Context context){
		return context.getResources().getStringArray(R.array.period)[this.ordinal()];
	}
}
