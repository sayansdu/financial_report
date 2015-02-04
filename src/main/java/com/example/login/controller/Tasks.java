package com.example.login.controller;

import java.util.ArrayList;

import com.example.login.LoginUI;
import org.hibernate.Session;

import com.example.login.entity.Student;
import com.example.login.entity.Task;
import com.example.login.util.HibernateUtil;

public class Tasks {
	
	public static ArrayList<Task> getTasks(){
        Session session = LoginUI.getCurrentSession();
        if(session.getTransaction() == null){
            session.beginTransaction();
        }
        else
            session.getTransaction().begin();
		 ArrayList<Task> list = (ArrayList) session.createCriteria(Task.class).list();
		 session.getTransaction().commit();	 
		 return list;
	 }
	

	
}
