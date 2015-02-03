package com.example.login.controller;

import java.util.ArrayList;

import org.hibernate.Session;

import com.example.login.entity.Month;
import com.example.login.util.HibernateUtil;

public class Months {

	public static ArrayList<Month> getMonths(){
		 Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		 session.beginTransaction();
		 ArrayList<Month> list =  (ArrayList) session.createCriteria(Month.class).list();
		 session.getTransaction().commit();	
		 return list;
	 }
}
