package com.ifengtech.www.thread;

/**
 * 示例说明：
 * <P>经测试“同步”并不能使下列线程b1，b2，b3按照顺序执行</P>
 * <ul>
 * <li>b1.start();</li>
 * <li>b2.start();</li>
 * <li>b3.start();</li>
 * </ul>
 * <P>该示例用{@link Thread#join()}来解决，实例模拟乞丐排队自助取粥的场景。</P>
 * <P>事实上，</P>
 * <ul>
 * <li>b1.join();</li>
 * <li>b2.join();</li>
 * <li>b3.join();</li>
 * </ul>
 * <P>这样依次罗列join()即可解决问题，只是由于start()到join()这段时间内线程早已结束，造成join()罗列无效。</P>
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
 * 自动取粥机，唯一的一台。是一台机器，没有自执行的功能，所以不用线程表示。
 * 取粥的时候，只需要输入自己的名字，就可以获取新鲜的白粥。
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
	 * 凭名字取粥
	 * @param name
	 */
	public synchronized void getFood(String name) {
		System.out.println("[Warning]There's no much food left, sir!");
		System.out.println(name + ", have some please!");
	}
	
	private Begger mTailBegger;
	
	/**
	 * 按照list给出的顺序先排好队
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
	 * 取粥的活动正在进行时，如果又来了人，则直接排在后面即可。
	 * @param newBegger
	 */
	public void addTailWhileRunning(Begger newBegger) {
		addTail(newBegger);
		newBegger.start();
	}
	
	/**
	 * 当队伍排好了，就可以开始施粥了。
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
 * {@link Begger}是一个人，可以自执行，用一个线程来表示。
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
				mJoin.join();		// mJoin线程优先执行完毕，原线程阻塞。
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