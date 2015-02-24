package com.ifengtech.www.textual;

import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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
public class SmartDate implements Serializable {
	
	private static final long serialVersionUID = -1853902075182832328L;

	private static final String SIMPLE_DATE_FORMAT = "M/dd";
	
	private static final String FULL_DATE_FORMAT = "Y/M/dd";
	
	private static final String SIMPLE_TIME_FORMAT = "HH:mm";
	
	private static final String NORMAL_DATETIME_FORMAT = "Y/M/d HH:mm:ss";
    
    private /*transient*/ long mNanoTime;
    
    private transient boolean mImmutable = false;
    
    public SmartDate() {
    	// so that mNanoTime be set only once
    	mImmutable = false;
    }

    public SmartDate(long milliseconds) {
        mNanoTime = milliseconds;
        mImmutable = true;
    }
    
    public SmartDate(String format, String date) {
    	try {
			Date d = new SimpleDateFormat(format).parse(date);
			mNanoTime = d.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	
    	mImmutable = true;
    }

    public static void main(String[] args) {
    	SmartDate date = null;
    	
    	
    	date = new SmartDate();
    	FileOutputStream fout;
    	ObjectOutputStream out = null;
    	try {
    		fout = new FileOutputStream(new File("./asset/SmartDate/obj_ar.dat"), true);
			out = new ObjectOutputStream(fout);
			out.writeObject(date);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    	
    	/*
    	FileInputStream fin;
    	ObjectInputStream in = null;
    	try {
			fin = new FileInputStream(new File("./asset/SmartDate/obj_ar.dat"));
			in = new ObjectInputStream(fin);
			date = (SmartDate) in.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}*/

    	try {
    		date.setNanoTime(System.currentTimeMillis());
    	} catch (ImmutableException e) {
    		e.printStackTrace();
    	}
    	
    	System.out.println(date);
    	System.out.println(date.getNormalDatetime());
    	
    }

    public long getNanoTime() {
        return mNanoTime;
    }

    public void setNanoTime(long nanoTime) throws ImmutableException {
    	if(!mImmutable && mNanoTime <= 0) {
    		this.mNanoTime = nanoTime;
    		mImmutable = true;
    	} else {
    		throw new ImmutableException("�����ʱ���ɸ���");
    	}
    }

    private boolean isThisYear() {
    	GregorianCalendar calL = new GregorianCalendar();
        GregorianCalendar calR = new GregorianCalendar();
        calL.setTimeInMillis(mNanoTime);
        calR.setTimeInMillis(System.currentTimeMillis());
        
        return calL.get(Calendar.YEAR) == calR.get(Calendar.YEAR);
    }
    
    @SuppressWarnings("unused")
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
    
    private void writeObject(ObjectOutputStream oos) throws IOException {
    	oos.defaultWriteObject();	// write mNanoTime
    	
    	if(mImmutable) {
    		oos.writeBoolean(mImmutable);
    	}
    }
    
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
    	ois.defaultReadObject();
    	try {
			mImmutable = ois.readBoolean();
		} catch (EOFException e) {
			// ignore it
		}
    }
    
    class ImmutableException extends SecurityException {
    	
		private static final long serialVersionUID = 5130986246839523392L;

		public ImmutableException() {
    		super();
    	}
    	
    	public ImmutableException(String message) {
    		super(message);
    	}
    }
}
