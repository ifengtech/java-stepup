package com.ifengtech.www.reflection;

/**
 * 反射示例：暴力获取私有成员
 * 示例引用生活中男生像女生搭讪的简单对话来进行模仿，首先男生像女生询问姓名，
 * 如果女生如实回答，则继续询问年龄，如果女生拒绝回答，则采用“非常规”手段获取姓名，询问终止。
 * 
 * 对话有点邪恶，请勿模仿。
 */
import java.lang.reflect.Field;
import java.util.Random;

//import sun.misc.Unsafe;

import com.ifengtech.www.textual.TextUtil;

public class GetPrivateFieldDemo {

	public static void main(String[] args) {
		Person girl1 = Person.bornGirl("贾琳", 22);
		Person girl2 = Person.bornGirl("范美美", 28);
		Person boy1 = Person.bornBoy("张一哥", 33);
		Person boy2 = Person.bornBoy("何建兵", 30);
		
		boy1.askFor(girl1);
		boy1.askFor(girl2);
		boy2.askFor(girl1);
		boy2.askFor(girl2);
	}
}

/**
 * 暴力破解工具
 * @author Wang Gensheng
 *
 */
class BeyondAll {
	
	/**
	 * 暴力获取对象的私有成员（可递归/级联）
	 * @param who 破解对象
	 * @param fieldChain 私有成员调用链
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getPrivateField(Person who, String fieldChain)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, 
			IllegalAccessException {
		
		String[] fieldName = fieldChain.split("\\.");
		
		Object caller = who;
		Class<?> clazz = caller.getClass();
		Field field;
		for (int i = 0; i < fieldName.length; i++) {
			field = clazz.getDeclaredField(fieldName[i]);
			field.setAccessible(true);
			caller = field.get(caller);
			clazz = caller.getClass();
		}
		
		return (T) caller;
	}
	
}

class Person extends FaceAnimal {
	
	private PrivacyProperty<String> mFullName;
	
	private PrivacyProperty<Integer> mAge;
	
	private Person() {
		throw new RuntimeException();
	}
	
	private Person(String name, int age) {
		mFullName = new PrivacyPropertyImpl<String>(name);
		mAge = new PrivacyPropertyImpl<Integer>(Integer.valueOf(age));
	}
	
	private Person(String name, int age, double[] passArgs) {
		if(passArgs.length < 2) {
			throw new IllegalArgumentException("请用passArgs分别为{name,age}属性添加访问权限值");
		}
		mFullName = new PrivacyPropertyImpl<String>(name, passArgs[0]);
		mAge = new PrivacyPropertyImpl<Integer>(age, passArgs[1]);
	}
	
	public String getFullName(FaceAnimal who) {
		return mFullName.get(who);
	}
	
	public Integer getAge(FaceAnimal who) {
		return mAge.get(who);
	}
	
	public void askFor(Person who) {
		String selfName = mFullName.get(SELF);
		System.out.println(String.format("%1$s：嘿，我叫%1$s，你叫什么名字？", selfName));
		
		String name = who.getFullName(this);
		if(TextUtil.isEmpty(name)) {
			System.out.println("...：我为什么要告诉你？");
		} else {
			System.out.println(String.format("%1$s：我叫%1$s!", name));
		}
		
		if(TextUtil.isEmpty(name)) {
			System.out.println(String.format("%1$s：操。。", selfName));
			try {
				name = BeyondAll.getPrivateField(who, "mFullName.mPrivacy");
				System.out.println(String.format("%1$s：贱货，还不是被我知道了名字，%2$s是吧，等着吧！", selfName, name));
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			
			return;
		} else {
			System.out.println(String.format("%1$s：%2$s，你多大了呢？", selfName, name));
		}
		
		Integer age = who.getAge(this);
		if(age != null) {
			System.out.println(String.format("%1$s：我%2$d", name, age.intValue()));
		} else {
			System.out.println(String.format("%1$s：你问这个干什么？", name));
		}
	}
	
	public static Person born(String name, int age) {
		return new Person(name, age);
	}
	
	public static Person born(String name, int age, double[] passArgs) {
		return new Person(name, age, passArgs);
	}
	
	public static Person bornBoy(String name, int age) {
		double[] boyPass = {0.3, 0.45};
		return new Person(name, age, boyPass);
	}
	
	public static Person bornGirl(String name, int age) {
		double[] girlPass = {0.6, 0.8};
		return new Person(name, age, girlPass);
	}
}

class FaceAnimal {
	
	static final FaceAnimal SELF = new FaceAnimal(Double.MAX_VALUE);
	
	/**
	 * 相当于自己的“颜值”
	 */
	protected double mAuthFactor;
	
	private static Random mRandom;
	
	static {
		mRandom = new Random();
	}
	
	public FaceAnimal() {
		this(mRandom.nextDouble());
	}
	
	public FaceAnimal(double auth) {
		mAuthFactor = auth;
	}
	
	public double getAuthFactor() {
		return mAuthFactor;
	}

}

/**
 * 带权值的属性接口
 * @author Wang Gensheng
 *
 * @param <T>
 */
interface PrivacyProperty<T> {
	
	/**
	 * 检测who的“颜值”是否满足
	 * @param who 用who的“颜值”来衡量其身份
	 * @return true 如果隐私权值达到预期值<br>false 如果隐私权值不满足条件
	 */
	boolean checkPerm(FaceAnimal who);
	
	/**
	 * 如果who的“颜值”满足条件，则返回属性值，否则返回null。
	 * @param who
	 * @return
	 */
	T get(FaceAnimal who);
}

/**
 * <p>带“隐私权值”的个人属性</p>
 * 所谓<b>个人隐私</b>也是相对的，用“隐私权值”来衡量。当面对不同的人时，人的戒心会有所不同，比如碰见美女可以很不要脸，也就是“隐私权值”相对较小。
 * @author Wang Gensheng
 *
 * @param <T>
 */
class PrivacyPropertyImpl<T> implements PrivacyProperty<T> {
	
	public static double DEFAULT_PASS_THRESHOLD = 0.6;
	
	private T mPrivacy;
	
	private double mPassThreshold;
	
	public PrivacyPropertyImpl(T property) {
		mPrivacy = property;
		mPassThreshold = DEFAULT_PASS_THRESHOLD;
	}
	
	public PrivacyPropertyImpl(T property, double pass) {
		mPrivacy = property;
		mPassThreshold = pass;
	}
	
	@Override
	public T get(FaceAnimal who) {
		if(checkPerm(who)) {
			return mPrivacy;
		}
		
		return null;		// Can't tell.
	}

	@Override
	public boolean checkPerm(FaceAnimal who) {
		// TODO Auto-generated method stub
		return mPassThreshold < who.getAuthFactor();
	}
	
}
