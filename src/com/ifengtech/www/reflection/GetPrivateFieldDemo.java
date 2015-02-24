package com.ifengtech.www.reflection;

/**
 * ����ʾ����������ȡ˽�г�Ա
 * ʾ������������������Ů����ڨ�ļ򵥶Ի�������ģ�£�����������Ů��ѯ��������
 * ���Ů����ʵ�ش������ѯ�����䣬���Ů���ܾ��ش�����á��ǳ��桱�ֶλ�ȡ������ѯ����ֹ��
 * 
 * �Ի��е�а������ģ�¡�
 */
import java.lang.reflect.Field;
import java.util.Random;

//import sun.misc.Unsafe;

import com.ifengtech.www.textual.TextUtil;

public class GetPrivateFieldDemo {

	public static void main(String[] args) {
		Person girl1 = Person.bornGirl("����", 22);
		Person girl2 = Person.bornGirl("������", 28);
		Person boy1 = Person.bornBoy("��һ��", 33);
		Person boy2 = Person.bornBoy("�ν���", 30);
		
		boy1.askFor(girl1);
		boy1.askFor(girl2);
		boy2.askFor(girl1);
		boy2.askFor(girl2);
	}
}

/**
 * �����ƽ⹤��
 * @author Wang Gensheng
 *
 */
class BeyondAll {
	
	/**
	 * ������ȡ�����˽�г�Ա���ɵݹ�/������
	 * @param who �ƽ����
	 * @param fieldChain ˽�г�Ա������
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
			throw new IllegalArgumentException("����passArgs�ֱ�Ϊ{name,age}������ӷ���Ȩ��ֵ");
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
		System.out.println(String.format("%1$s���٣��ҽ�%1$s�����ʲô���֣�", selfName));
		
		String name = who.getFullName(this);
		if(TextUtil.isEmpty(name)) {
			System.out.println("...����ΪʲôҪ�����㣿");
		} else {
			System.out.println(String.format("%1$s���ҽ�%1$s!", name));
		}
		
		if(TextUtil.isEmpty(name)) {
			System.out.println(String.format("%1$s���١���", selfName));
			try {
				name = BeyondAll.getPrivateField(who, "mFullName.mPrivacy");
				System.out.println(String.format("%1$s�������������Ǳ���֪�������֣�%2$s�ǰɣ����Űɣ�", selfName, name));
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			
			return;
		} else {
			System.out.println(String.format("%1$s��%2$s���������أ�", selfName, name));
		}
		
		Integer age = who.getAge(this);
		if(age != null) {
			System.out.println(String.format("%1$s����%2$d", name, age.intValue()));
		} else {
			System.out.println(String.format("%1$s�����������ʲô��", name));
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
	 * �൱���Լ��ġ���ֵ��
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
 * ��Ȩֵ�����Խӿ�
 * @author Wang Gensheng
 *
 * @param <T>
 */
interface PrivacyProperty<T> {
	
	/**
	 * ���who�ġ���ֵ���Ƿ�����
	 * @param who ��who�ġ���ֵ�������������
	 * @return true �����˽Ȩֵ�ﵽԤ��ֵ<br>false �����˽Ȩֵ����������
	 */
	boolean checkPerm(FaceAnimal who);
	
	/**
	 * ���who�ġ���ֵ�������������򷵻�����ֵ�����򷵻�null��
	 * @param who
	 * @return
	 */
	T get(FaceAnimal who);
}

/**
 * <p>������˽Ȩֵ���ĸ�������</p>
 * ��ν<b>������˽</b>Ҳ����Եģ��á���˽Ȩֵ��������������Բ�ͬ����ʱ���˵Ľ��Ļ�������ͬ������������Ů���Ժܲ�Ҫ����Ҳ���ǡ���˽Ȩֵ����Խ�С��
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
