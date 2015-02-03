package com.example.login.test;

import org.hibernate.Session;

import com.example.login.controller.Months;
import com.example.login.controller.Products;
import com.example.login.entity.Report;
import com.example.login.util.HibernateUtil;

public class TestReport {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		add_data();
	}

	private static void add_data(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
			Report rep = new Report();
			rep.setProduct(Products.getProducts().get(1));
			rep.setYear(2012);
			rep.setMonth(Months.getMonths().get(0));
			
			rep.setAmount(93);
			rep.setSold_amount(80);
			rep.setCost_price(110);
			rep.setPrice(140);
			session.save(rep);
		session.getTransaction().commit();
		session.close();
	}
}
