package com.ifengtech.www.thread;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

/**
 * 多线程阻塞式计算文件SHA值
 * @author Wang Gensheng
 *
 */
public class CalcFileSHADemo2 {
	
	public static void main(String[] args) {
		new CalcThreadContext2().calculateDigest();
	}

}

class CalcThreadContext2 implements FileSHACal.OnCalcOverListener {
	private File mFile;
	private byte[] mDigest;
	
	private FileSHACal mCal;
	
	public CalcThreadContext2() {
		mCal = FileSHACal.getInstance();
	}
	
	public void calculateDigest() {
		File folder = new File("./asset/CalcFileSHADemo");
		File[] files = folder.listFiles();
		for (File file : files) {
			mFile = file;
			CalFileSHAThread calcT = new CalFileSHAThread(mFile);
			calcT.start();
		}
	}

	@Override
	public void OnCalcOver(File file, byte[] digest) {
		mDigest = digest;
		System.out.println(composeResult(file, mDigest));
	}

	public String composeResult(File file, byte[] digest) {
		String ret = file.getAbsolutePath() + ":";
		
		if(digest != null) {
			ret += DatatypeConverter.printHexBinary(digest);
		} else {
			ret += "digest not available";
		}
		
		return ret;
	}
	
	private class CalFileSHAThread extends Thread {
		
		private File mFile;
		
		public CalFileSHAThread(File file) {
			this.mFile = file;
		}
		
		@Override
		public void run() {
			mCal.calcFileSHA(this.mFile, CalcThreadContext2.this);
		}
	}
	
}

class FileSHACal {
	
	public static final String ALG_MD5 = "md5";
	
	private static FileSHACal mObject;
	
	private FileSHACal() {
		// TODO Auto-generated constructor stub
	}
	
	public static FileSHACal getInstance() {
		if(mObject == null) {
			mObject = new FileSHACal();
		}
		
		return mObject;
	}
	
	public void calcFileSHA(File file, OnCalcOverListener listener) {
		try {
			FileInputStream fis = new FileInputStream(file);
			MessageDigest sha = MessageDigest.getInstance(ALG_MD5);
			
			synchronized (sha) {
				DigestInputStream dis = new DigestInputStream(fis, sha);
				
				int rflag = 0;
				while(rflag != -1) {
					rflag = dis.read();
				}
				
				fis.close();
				dis.close();
				
				byte[] digest = sha.digest();
				
				if(listener != null)
					listener.OnCalcOver(file, digest);
			}

		} catch (NoSuchAlgorithmException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public interface OnCalcOverListener {
		void OnCalcOver(File file, byte[] digest);
	}
	
}
