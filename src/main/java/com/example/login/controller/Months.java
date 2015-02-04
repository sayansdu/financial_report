package com.example.login.controller;

import java.util.ArrayList;

import com.example.login.LoginUI;
import org.hibernate.Session;

import com.example.login.entity.Month;
import com.example.login.util.HibernateUtil;

public class Months {

	public static ArrayList<Month> getMonths(){
        Session session = LoginUI.getCurrentSession();
        if(session.getTransaction() == null){
            session.beginTransaction();
        }
        else
            session.getTransaction().begin();

		 ArrayList<Month> list =  (ArrayList) session.createCriteria(Month.class).list();
		 session.getTransaction().commit();	
		 return list;
	 }
}
