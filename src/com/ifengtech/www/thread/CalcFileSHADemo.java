package com.ifengtech.www.thread;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

/**
 * 多线程不阻塞的计算多个文件SHA值
 * @author Wang Gensheng
 *
 */
public class CalcFileSHADemo {
	
	public static void main(String[] args) {
		File folder = new File("./asset/CalcFileSHADemo");
		File[] files = folder.listFiles();
		for (File file : files) {
			new CalcThreadContext(file.getAbsolutePath()).calculateDigest();
		}
	}

}

class CalcThreadContext implements CalcFileSHA.OnCalcOverListener {
	private String mFilename;
	private byte[] mDigest;
	
	public CalcThreadContext(String filename) {
		mFilename = filename;
	}
	
	public void calculateDigest() {
		Thread calcT = new Thread(new CalcFileSHA(mFilename, this));
		calcT.start();
	}

	@Override
	public void OnCalcOver(byte[] digest) {
		mDigest = digest;
		System.out.println(this);
	}

	@Override
	public String toString() {
		String ret = mFilename + ":";
		
		if(mDigest != null) {
			ret += DatatypeConverter.printHexBinary(mDigest);
		} else {
			ret += "digest not available";
		}
		
		return ret;
	}
	
}

class CalcFileSHA implements Runnable {
	
	public static final String ALG_MD5 = "md5";
	
	private String mFilename;
	private OnCalcOverListener mCalcOverListener;
	
	public CalcFileSHA(String filename, OnCalcOverListener listener) {
		mFilename = filename;
		mCalcOverListener = listener;
	}
	
	@Override
	public void run() {
		try {
			FileInputStream fis = new FileInputStream(mFilename);
			MessageDigest sha = MessageDigest.getInstance(ALG_MD5);
			DigestInputStream dis = new DigestInputStream(fis, sha);
			
			int rflag = 0;
			while(rflag != -1) {
				rflag = dis.read();
			}
			
			fis.close();
			dis.close();
			
			byte[] digest = sha.digest();
			
			mCalcOverListener.OnCalcOver(digest);
		} catch (NoSuchAlgorithmException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public interface OnCalcOverListener {
		void OnCalcOver(byte[] digest);
	}
	
}
