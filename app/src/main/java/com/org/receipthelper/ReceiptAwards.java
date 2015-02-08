package com.org.receipthelper;

import com.org.receiptreport.R;


public enum ReceiptAwards {
	 /**沒中獎*/
     NONE,
     /**六獎:同期統一發票收執聯末3 位數號碼與頭獎中獎號碼末3 位相同者各得獎金2百元*/
     SixAwards,
     /**五獎:同期統一發票收執聯末4 位數號碼與頭獎中獎號碼末4 位相同者各得獎金1 千元*/
     FiveAwards, 
     /**四獎	同期統一發票收執聯末5 位數號碼與頭獎中獎號碼末5 位相同者各得獎金4 千元*/
     FourAwards,
     /**三獎	同期統一發票收執聯末6 位數號碼與頭獎中獎號碼末6 位相同者各得獎金1 萬元*/
     ThreeAwards,
     /**二獎	同期統一發票收執聯末7 位數號碼與頭獎中獎號碼末7 位相同者各得獎金4 萬元*/
     TwoAwards,
     /**頭獎：同期統一發票收執聯8位數號碼與上列號碼相同者獎金20 萬元*/
     OneAwards,
     /**特獎:同期統一發票收執聯8位數號碼與上列號碼相同者獎金200 萬元*/
     SpecialAwards,
     /**特別獎:同期統一發票收執聯8位數號碼與上列號碼相同者獎金1,000 萬元*/
     SuperSpecialAwards
     ;
    public static int getImage(int index){
    	
    	    switch (index) {
			case 0:
				return R.drawable.star_1;
			case 1:
				return R.drawable.star_2;
			case 2:
				return R.drawable.star_3;
			case 3:
				return R.drawable.star_4;
			case 4:
				return R.drawable.star_5;
			case 5:
				return R.drawable.star_6;
			case 6:
				return R.drawable.star_7;
			case 7:
				return R.drawable.star_8;
			case 8:
				return R.drawable.star_9;
			default:
				break;
			}
    	    return R.drawable.wait;
    }
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		switch (this) {
		case NONE:
			return "沒中獎";
		case SixAwards:
			return "兩百塊,摸魚蝦媽喝";
		case FiveAwards:
			return "一張小朋友";
		case FourAwards:
			return "小朋友組一桌麻將";
		case ThreeAwards:
			return "一萬塊";
		case TwoAwards:
			return "四萬塊";
		case OneAwards:
			return "二十萬塊";
		case SpecialAwards:
			return "兩百萬拉";
		case SuperSpecialAwards:
			return "鄉親啊!! 一千萬!!";
		default:
			break;
		}
		return super.toString();
	}
     
}
