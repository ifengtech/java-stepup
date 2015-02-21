package com.ifengtech.www.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorServiceDemo {

	public static void main(String[] args) {
		
	}
	
}

class FindMaxTask implements Callable<Integer> {
	
	private int[] mData;
	private int mStart;
	private int mEnd;
	
	public FindMaxTask(int[] data, int start, int end) {
		mData = data;
		mStart = start;
		mEnd = end;
	}

	@Override
	public Integer call() throws Exception {
		int max = Integer.MIN_VALUE;
		
		for (int i = mStart; i < mEnd; i++) {
			if(mData[i] > max) {
				max = mData[i];
			}
		}
		
		return max;
	}
	
}

class MultithreadedMaxFinder {
	public static int max(int[] data) throws InterruptedException, ExecutionException {
		if(data.length == 1) {
			return data[0];
		} else if(data.length == 0) {
			throw new IllegalArgumentException();
		}
		
		FindMaxTask task1 = new FindMaxTask(data, 0, data.length/2);
		FindMaxTask task2 = new FindMaxTask(data, data.length/2, data.length);
		
		ExecutorService service = Executors.newFixedThreadPool(2);
		
		Future<Integer> future1 = service.submit(task1);
		Future<Integer> future2 = service.submit(task2);
		
		return Math.max(future1.get(), future2.get());
	}
}
