package com.org.receipthelper;


import android.util.Log;


import com.org.data.ReceiptData;
import com.org.receiptreport.Receipt;


public class ReceiptHelper {
	private final static String  TAGLOG = "ReceiptHelper";
	public static int paserQRcode(ReceiptData receiptdata ,String data){
		if(data==null)
			return -1;
		if(data.startsWith("**"))
			return -1;
		Log.d(TAGLOG,data);
		String[] datas = data.split(":");
		Receipt receipt = getReceipt(datas[0]);
		int result = receiptdata.addReceipt(receipt);
		return result;
	}

	private static Receipt getReceipt(String data){
		Receipt receipt = new Receipt();
		receipt.setReceiptID(data.substring(0, 10));
		String createDate = data.substring(10, 17);
		receipt.setCreateDate(createDate);
		receipt.setPeriod(getPeriod(Integer.valueOf(createDate.substring(3, 5))).ordinal());
		receipt.setAwards(ReceiptAwards.NONE.ordinal());
		receipt.setStatus(false);
		return receipt;
	}
//	private static int getStrToInt(String value){
//		int number  = Integer.valueOf(value, 16).intValue();
//		return number;
//	}
    //對獎
	public static ReceiptAwards CheckReceiptAwards(WinningNumber numbers,String id){
		char[] receiptId = id.toCharArray();
		ReceiptAwards awards = ReceiptAwards.NONE;
        String[] sixWards = null;
        if(numbers.getSixAwadNumber()!=null)
		    sixWards = numbers.getSixAwadNumber().split("、");
		String[] oneWards = numbers.getOneAwadNumber().split("、");
		String curNumber="";
		for(int i=9;i>1;i--){
			curNumber = receiptId[i]+curNumber;
			if(curNumber.length()==3){
                if(sixWards!= null)
				for(String s:sixWards){
					if(s.equals(curNumber)){
						awards = ReceiptAwards.SixAwards;
					}
				}
				for(String s:oneWards){
					if(s.substring(5).equals(curNumber)){
						awards = ReceiptAwards.SixAwards;
					}
				}
			}else if(curNumber.length()==4){
				for(String s:oneWards){
					if(s.substring(4).equals(curNumber)){
						awards = ReceiptAwards.FiveAwards;
					}
				}
			}else if(curNumber.length()==5){
				for(String s:oneWards){
					if(s.substring(3).equals(curNumber)){
						awards = ReceiptAwards.FourAwards;
					}
				}
			}else if(curNumber.length()==6){
				for(String s:oneWards){
					if(s.substring(2).equals(curNumber)){
						awards = ReceiptAwards.ThreeAwards;
					}
				}
			}else if(curNumber.length()==7){
				for(String s:oneWards){
					if(s.substring(1).equals(curNumber)){
						awards = ReceiptAwards.TwoAwards;
					}
				}
			}else if(curNumber.length()==8){
				for(String s:oneWards){
					if(s.equals(curNumber)){
						awards = ReceiptAwards.OneAwards;
					}
				}
				if(numbers.getSpecialNumber().equals(curNumber)){
					awards = ReceiptAwards.SpecialAwards;
				}
				if(numbers.getSuperNumber().equals(curNumber)){
					awards = ReceiptAwards.SuperSpecialAwards;
				}
			}
		}
        return awards;
	}
	public static WinningNumber  getTestWinningNumber(){
		   WinningNumber numbers = new WinningNumber();
		   numbers.setSixAwadNumber("901&659&034&955");
		   numbers.setOneAwadNumber("43772058&44517895&45226602");
		   numbers.setSpecialNumber("18290129");
		   numbers.setSuperNumber("33516538");
		   return numbers;
	}
    public static Period getPeriod(int month){
		if(month>=1&&month<=2){
			return Period.one_TwoMonth;
		}
		else if(month>=3&&month<=4){
			return Period.three_FourMonth;
		} 
		else if(month>=5&&month<=6){
			return Period.five_SixMonth;
		}
		else if(month>=7&&month<=8){
			return Period.seven_EightMonth;
		}
		else if(month>=9&&month<=10){
			return Period.nine_TenMonth;
		}
		else {
			return Period.eleven_TwelveMonth;
		}	
    }
}
