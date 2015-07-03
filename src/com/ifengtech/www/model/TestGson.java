package com.ifengtech.www.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TestGson {
	public static void main(String[] args) {
		User user = new User();
		user.nickname = "幸运号";
		user.uuid = "wsij-12dessf-wwl1-ddd452";
		user.mobile = "18025423291";
		
		List<BankCard> list = new ArrayList<>();
		BankCard card1 = new BankCard();
		card1.bankName = "中国银行";
		card1.bankType = 1;
		card1.cardNo = "62221111";
		list.add(card1);
		
		card1.bankName = "中国银行";
		card1.bankType = 1;
		card1.cardNo = "62221111";
		list.add(card1);
		
		card1.bankName = "中国银行";
		card1.bankType = 1;
		card1.cardNo = "62221111";
		list.add(card1);
		user.cardList = list;
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();		
		User user2 = gson.fromJson(gson.toJson(user), User.class);
		System.out.println(gson.toJson(user2));
		
	}
}
