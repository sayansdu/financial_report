package com.example.login.test;

import java.util.ArrayList;

import org.hibernate.Session;

import com.example.login.controller.Positions;
import com.example.login.util.HibernateUtil;

public class TestPosition {

	public static void main(String[] args){
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		ArrayList<String> position = Positions.getTitles();
		for (String string : position) {
			System.out.println(string);
		}
		session.close();
		
	}
}
