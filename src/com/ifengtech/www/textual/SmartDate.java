package com.ifengtech.www.textual;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * ��iPhone����¼�б��Ҳ���ʾ������
 * 
 */
public class SmartDate {
	
	private static final String SIMPLE_DATE_FORMAT = "M/dd";
	
	private static final String FULL_DATE_FORMAT = "Y/M/dd";
	
	private static final String SIMPLE_TIME_FORMAT = "HH:mm";
	
	private static final String NORMAL_DATETIME_FORMAT = "Y/M/d HH:mm:ss";
    
    private long mNanoTime;
    
    public SmartDate(String format, String date) {
    	try {
			Date d = new SimpleDateFormat(format).parse(date);
			mNanoTime = d.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
    }

    public SmartDate(long milliseconds) {
        mNanoTime = milliseconds;
    }
    
    public static void main(String[] args) {
    	SmartDate date = new SmartDate(System.currentTimeMillis() - 300000000L);
    	System.out.println(date);
    	System.out.println(date.getNormalDatetime());
    }

    public long getNanoTime() {
        return mNanoTime;
    }

    public void setNanoTime(long nanoTime) {
        this.mNanoTime = nanoTime;
    }

    private boolean isThisYear() {
    	GregorianCalendar calL = new GregorianCalendar();
        GregorianCalendar calR = new GregorianCalendar();
        calL.setTimeInMillis(mNanoTime);
        calR.setTimeInMillis(System.currentTimeMillis());
        
        return calL.get(Calendar.YEAR) == calR.get(Calendar.YEAR);
    }
    
    private boolean isThisMouth() {
    	GregorianCalendar calL = new GregorianCalendar();
        GregorianCalendar calR = new GregorianCalendar();
        calL.setTimeInMillis(mNanoTime);
        calR.setTimeInMillis(System.currentTimeMillis());
        
        return calL.get(Calendar.MONTH) == calR.get(Calendar.MONTH);
    }

    private boolean isThisWeek() {
    	GregorianCalendar calL = new GregorianCalendar();
        GregorianCalendar calR = new GregorianCalendar();
        calL.setTimeInMillis(mNanoTime);
        calR.setTimeInMillis(System.currentTimeMillis());
        
        return calL.get(Calendar.WEEK_OF_YEAR) == calR.get(Calendar.WEEK_OF_YEAR);
    }
    
    private boolean isDayBeforeYesterday() {
    	GregorianCalendar calL = new GregorianCalendar();
        GregorianCalendar calR = new GregorianCalendar();
        calL.setTimeInMillis(mNanoTime);
        calR.setTimeInMillis(System.currentTimeMillis());
        
        return calL.get(Calendar.DAY_OF_YEAR) + 2 == calR.get(Calendar.DAY_OF_YEAR);
    }

    private boolean isYesterday() {
    	GregorianCalendar calL = new GregorianCalendar();
        GregorianCalendar calR = new GregorianCalendar();
        calL.setTimeInMillis(mNanoTime);
        calR.setTimeInMillis(System.currentTimeMillis());
        
        return calL.get(Calendar.DAY_OF_YEAR) + 1== calR.get(Calendar.DAY_OF_YEAR);
    }
    
    private boolean isToday() {
    	GregorianCalendar calL = new GregorianCalendar();
        GregorianCalendar calR = new GregorianCalendar();
        calL.setTimeInMillis(mNanoTime);
        calR.setTimeInMillis(System.currentTimeMillis());
        
        return calL.get(Calendar.DAY_OF_YEAR) == calR.get(Calendar.DAY_OF_YEAR);
    }
    
    private boolean isTomorrow() {
    	GregorianCalendar calL = new GregorianCalendar();
        GregorianCalendar calR = new GregorianCalendar();
        calL.setTimeInMillis(mNanoTime);
        calR.setTimeInMillis(System.currentTimeMillis());
        
        return calL.get(Calendar.DAY_OF_YEAR) - 1 == calR.get(Calendar.DAY_OF_YEAR);
    }

    @Override
    public String toString() {
        String ret = null;
        DateFormat format;
        
        if(isThisYear()) {
        	if(isThisWeek()) {
        		if(isDayBeforeYesterday()) {
        			ret = "ǰ��";
        		} else if(isYesterday()) {
        			ret = "����";
        		} else if(isToday()) {
        			format = new SimpleDateFormat(SIMPLE_TIME_FORMAT);
                	ret = format.format(new Date(mNanoTime));
        		} else if(isTomorrow()) {
        			ret = "����";
        		} else {
        			GregorianCalendar calendar = new GregorianCalendar();
                    calendar.setTimeInMillis(mNanoTime);
                    calendar.setFirstDayOfWeek(Calendar.MONDAY);
                    int dayofWeek = calendar.get(GregorianCalendar.DAY_OF_WEEK);
                    String[] weekString = {
                            "����һ",
                            "���ڶ�",
                            "������",
                            "������",
                            "������",
                            "������",
                            "������"
                    };
                    ret = weekString[(dayofWeek  + weekString.length - 1) % 7];
        		}
        	} else {
        		format = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
            	ret = format.format(new Date(mNanoTime));
        	}
        } else {
        	format = new SimpleDateFormat(FULL_DATE_FORMAT);
        	ret = format.format(new Date(mNanoTime));
        }
        
        return ret;
    }
    
    public String getNormalDatetime() {
    	DateFormat format = new SimpleDateFormat(NORMAL_DATETIME_FORMAT);
    	return format.format(new Date(mNanoTime));
    }
}
