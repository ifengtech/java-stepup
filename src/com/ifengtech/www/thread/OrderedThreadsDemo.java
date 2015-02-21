package com.ifengtech.www.thread;

/**
 * ʾ��˵����
 * <P>�����ԡ�ͬ����������ʹ�����߳�b1��b2��b3����˳��ִ��</P>
 * <ul>
 * <li>b1.start();</li>
 * <li>b2.start();</li>
 * <li>b3.start();</li>
 * </ul>
 * <P>��ʾ����{@link Thread#join()}�������ʵ��ģ����ؤ�Ŷ�����ȡ��ĳ�����</P>
 * <P>��ʵ�ϣ�</P>
 * <ul>
 * <li>b1.join();</li>
 * <li>b2.join();</li>
 * <li>b3.join();</li>
 * </ul>
 * <P>������������join()���ɽ�����⣬ֻ������start()��join()���ʱ�����߳����ѽ��������join()������Ч��</P>
 * @author Wang Gensheng
 *
 */
public class OrderedThreadsDemo {
	
	public static void main(String[] args) {
		
		AutoGiver giver = AutoGiver.getInstance();
		
		Begger b1 = new Begger("b1", giver);
		Begger b2 = new Begger("b2", giver);
		Begger b3 = new Begger("b3", giver);
		
		giver.formInLine(new Begger[]{b1,b2,b3});
		giver.startGive();
		giver.addTailWhileRunning(new Begger("b4", giver));
		giver.addTailWhileRunning(new Begger("b5", giver));
		giver.addTailWhileRunning(new Begger("b6", giver));
	}
	
}

/**
 * �Զ�ȡ�����Ψһ��һ̨����һ̨������û����ִ�еĹ��ܣ����Բ����̱߳�ʾ��
 * ȡ���ʱ��ֻ��Ҫ�����Լ������֣��Ϳ��Ի�ȡ���ʵİ��ࡣ
 * @author Wang Gensheng
 */
class AutoGiver {
	
	private AutoGiver() {
		//
	}
	
	private static AutoGiver mAuto;
	
	public static AutoGiver getInstance() {
		if(mAuto == null) {
			mAuto = new AutoGiver();
		}
		
		return mAuto;
	}
	
	/**
	 * ƾ����ȡ��
	 * @param name
	 */
	public synchronized void getFood(String name) {
		System.out.println("[Warning]There's no much food left, sir!");
		System.out.println(name + ", have some please!");
	}
	
	private Begger mTailBegger;
	
	/**
	 * ����list������˳�����źö�
	 * @param list
	 * @return
	 */
	public void formInLine(Begger[] list) {
		for (Begger begger : list) {
			addTail(begger);
		}
	}
	
	private void addTail(Begger newBegger) {
		if(mTailBegger != null) {
			newBegger.joinBegger(mTailBegger);
			mTailBegger = newBegger;
		} else {
			mTailBegger = newBegger;
		}
	}
	
	/**
	 * ȡ��Ļ���ڽ���ʱ������������ˣ���ֱ�����ں��漴�ɡ�
	 * @param newBegger
	 */
	public void addTailWhileRunning(Begger newBegger) {
		addTail(newBegger);
		newBegger.start();
	}
	
	/**
	 * �������ź��ˣ��Ϳ��Կ�ʼʩ���ˡ�
	 */
	public void startGive() {
		if(mTailBegger == null) {
			return;
		}
		
		Begger curr = mTailBegger;
		while(true) {
			curr.start();
			if(!curr.hasPrevBegger()) {
				break;
			} else {
				curr = curr.getPrevBegger();
			}
		}
	}
}

/**
 * {@link Begger}��һ���ˣ�������ִ�У���һ���߳�����ʾ��
 * @author Wang Gensheng
 *
 */
class Begger extends Thread {
	
	private String mName;
	
	private AutoGiver mGiver;
	
	private Begger mJoin;
	
	public Begger(String name, AutoGiver giver) {
		mName = name;
		mGiver = giver;
	}

	private void askSome() {
		mGiver.getFood(mName);
	}
	
	@Override
	public void run() {
		try {
			if(mJoin != null) {
				mJoin.join();		// mJoin�߳�����ִ����ϣ�ԭ�߳�������
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		askSome();
	}
	
	public void joinBegger(Begger prev) {
		mJoin = prev;
	}
	
	public boolean hasPrevBegger() {
		return mJoin != null;
	}
	
	public Begger getPrevBegger() {
		return mJoin;
	}
}