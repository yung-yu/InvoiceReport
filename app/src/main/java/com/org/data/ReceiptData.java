package com.org.data;

import java.util.List;

import com.org.receiptreport.DaoMaster;
import com.org.receiptreport.DaoMaster.DevOpenHelper;
import com.org.receiptreport.Receipt;
import com.org.receiptreport.ReceiptDao;
import com.org.receiptreport.Report;
import com.org.receiptreport.ReportDao;

import android.content.Context;
import de.greenrobot.dao.query.QueryBuilder;


public class ReceiptData {
    DevOpenHelper receiptDB;
    DevOpenHelper reportDB;
    DaoMaster daoMaster_read;
    DaoMaster daoMaster_write;
    public final static int RECEIPT_IS_EXIST = 2;
    public final static int RECEIPT_ADD_SUCCESS = 1;
    public ReceiptData(Context context){
        receiptDB = new DaoMaster.DevOpenHelper(context, "Receipt", null);
        reportDB = new DaoMaster.DevOpenHelper(context, "Report", null);
    }
    public int addReceipt(Receipt receipt){
        if(checkExist(receipt))
            return RECEIPT_IS_EXIST;
        daoMaster_write = new DaoMaster(receiptDB.getWritableDatabase());
        daoMaster_write.newSession().getReceiptDao().insertOrReplace(receipt);
        return RECEIPT_ADD_SUCCESS;
    }
    public void updateReceipt(Receipt receipt){
        daoMaster_write = new DaoMaster(receiptDB.getWritableDatabase());
        daoMaster_write.newSession().getReceiptDao().update(receipt);
    }

    public  List<Receipt> getReceiptList(String year,int period){
        daoMaster_read = new DaoMaster(receiptDB.getReadableDatabase());
        QueryBuilder<Receipt> qb =daoMaster_read.newSession().getReceiptDao().queryBuilder();
        qb.orderDesc(ReceiptDao.Properties.Id);
        qb.where(
                ReceiptDao.Properties.CreateDate.like(year+"%"),
                ReceiptDao.Properties.Period.eq(period));

        return qb.list();
    }
    public void deleteReceipt(Receipt item){
        daoMaster_write = new DaoMaster(receiptDB.getWritableDatabase());
        daoMaster_write.newSession().getReceiptDao().delete(item);
    }
    public void deleteReceipt(String year,int period){
        daoMaster_write = new DaoMaster(receiptDB.getWritableDatabase());
        List<Receipt> data = getReceiptList(year,period);
        if(data.size()>0) {
            for (Receipt item:data)
                daoMaster_write.newSession().getReceiptDao().delete(item);
        }

    }
    public  List<Receipt> getReceiptList(){
        daoMaster_read = new DaoMaster(receiptDB.getReadableDatabase());
        QueryBuilder<Receipt> qb =daoMaster_read.newSession().getReceiptDao().queryBuilder();
        qb.orderDesc(ReceiptDao.Properties.Id);
        return qb.list();
    }
    public  List<Receipt> getNotCheckReceiptList(String year,int period){
        daoMaster_read = new DaoMaster(receiptDB.getReadableDatabase());
        QueryBuilder<Receipt> qb =daoMaster_read.newSession().getReceiptDao().queryBuilder();
        qb.where(ReceiptDao.Properties.Status.eq(false),
                ReceiptDao.Properties.CreateDate.like(year+"%"),
                ReceiptDao.Properties.Period.eq(period));
        return qb.list();
    }
    public boolean checkExist(Receipt receipt){
        daoMaster_read = new DaoMaster(receiptDB.getReadableDatabase());
        QueryBuilder<Receipt> qb =daoMaster_read.newSession().getReceiptDao().queryBuilder();
        qb.where(
                ReceiptDao.Properties.CreateDate.eq(receipt.getCreateDate()),
                ReceiptDao.Properties.ReceiptID.eq(receipt.getReceiptID()));
        return qb.list().size()!=0;
    }
    public boolean checkExist(Report report){
        daoMaster_read = new DaoMaster(reportDB.getReadableDatabase());
        QueryBuilder<Report> qb =daoMaster_read.newSession().getReportDao().queryBuilder();
        qb.where(
                ReportDao.Properties.Year.eq(report.getYear()),
                ReportDao.Properties.Period.eq(report.getPeriod()));
        return qb.list().size()!=0;
    }
    public void updateReport(Report report){
        List<Report> items = getReports(report.getYear(),report.getPeriod());
        daoMaster_write = new DaoMaster(reportDB.getWritableDatabase());
        if(items!=null){
            for(Report item:items)
                daoMaster_write.newSession().getReportDao().delete(item);
        }
        daoMaster_write.newSession().getReportDao().insertOrReplace(report);
    }
    public List<Report> getReports(String year,int period){
        daoMaster_read = new DaoMaster(reportDB.getReadableDatabase());
        QueryBuilder<Report> qb =daoMaster_read.newSession().getReportDao().queryBuilder();
        qb.where(
                ReportDao.Properties.Year.eq(year),
                ReportDao.Properties.Period.eq(period));
        if(qb.list().size()>0)
            return qb.list();
        return null;
    }
    public List<Report> getAllReports(){
        daoMaster_read = new DaoMaster(reportDB.getReadableDatabase());
        QueryBuilder<Report> qb =daoMaster_read.newSession().getReportDao().queryBuilder();
        if(qb.list().size()>0)
            return qb.list();
        return null;
    }
    public Report getReport(String year,int period){
        daoMaster_read = new DaoMaster(reportDB.getReadableDatabase());
        QueryBuilder<Report> qb =daoMaster_read.newSession().getReportDao().queryBuilder();
        qb.where(
                ReportDao.Properties.Year.eq(year),
                ReportDao.Properties.Period.eq(period));
        if(qb.list().size()>0)
            return qb.list().get(0);
        return null;
    }
}
