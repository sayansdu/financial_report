package com.example.login.test;

import java.util.ArrayList;

import org.hibernate.Session;

import com.example.login.entity.Month;
import com.example.login.util.HibernateUtil;

public class TestMonth {
	public static void main(String[] args){
		get_month();
		
	}
	
	private static void add_month(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		Month january = new Month();
		january.setName("February");
		session.save(january);
		session.getTransaction().commit();
		session.close();
	}
	
	private static void get_month(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		ArrayList<Month> months = (ArrayList) session.createCriteria(Month.class).list();
		for (Month month : months) {
			System.out.println("sdk: "+month.getName());
		}
		session.getTransaction().commit();
		session.close();
	}
}
