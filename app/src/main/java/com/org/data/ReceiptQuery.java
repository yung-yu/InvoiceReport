package com.org.data;

import android.util.Log;

import com.org.receipthelper.Period;
import com.org.receipthelper.ReceiptHelper;
import com.org.receipthelper.WinningNumber;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ReceiptQuery {
     public final static String TEST_URL = "http://invoice.etax.nat.gov.tw/";
     public final static String TAGLOG = "Query";
     public interface  CallBack{
         void onLoadNumbers(String year,Period period,WinningNumber numbers);
     }
     public static void query(final CallBack callback){

         new Thread(new Runnable() {
             @Override
             public void run() {
                 try {
                     Document doc = Jsoup.connect(TEST_URL).get();
                     Element area1 = doc.getElementById("area1");
                     String date = area1.getElementsByTag("h2").get(1).text();
                     Log.d(TAGLOG,date);

                     Elements t18Red = doc.getElementsByClass("t18Red");
                     WinningNumber numbers = new WinningNumber();
                     Log.d(TAGLOG,"t18Red count :"+t18Red.size());
                     for(int i=0;i<t18Red.size();i++){
                         switch (i){
                             case 0:
                                 numbers.setSuperNumber(t18Red.get(i).text());
                                 break;
                             case 1:
                                 numbers.setSpecialNumber(t18Red.get(i).text());
                                 break;
                             case 2:
                                 numbers.setOneAwadNumber(t18Red.get(i).text());
                                 break;
                             case 3:
                                 numbers.setSixAwadNumber(t18Red.get(i).text());
                                 break;
                         }
                         Log.d(TAGLOG,t18Red.get(i).text());
                     }
                     String year= date.substring(0,3);
                     int top = Integer.valueOf(date.substring(date.indexOf("年") + 1, date.indexOf("-")));
                     int bot = Integer.valueOf(date.substring(date.indexOf("-")+1,date.indexOf("月")));
                     Period period = ReceiptHelper.getPeriod(top);
                     Log.d(TAGLOG,year+" _ "+period.toString());
                     if(callback!=null){
                         callback.onLoadNumbers(year,period,numbers);
                     }
                 }catch (IOException e){
                     e.printStackTrace();
                 }catch (Exception e){
                     e.printStackTrace();
                 }
             }
         }).start();

     }
	 
}
