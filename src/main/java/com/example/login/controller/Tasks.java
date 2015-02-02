package com.example.login.controller;

import java.util.ArrayList;

import org.hibernate.Session;

import com.example.login.entity.Student;
import com.example.login.entity.Task;
import com.example.login.util.HibernateUtil;

public class Tasks {
	
	public static ArrayList<Task> getTasks(){
		 Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		 session.beginTransaction();
		 ArrayList<Task> list = (ArrayList<Task>) session.createCriteria(Task.class).list();
		 session.getTransaction().commit();	 
		 return list;
	 }
	

	
}
