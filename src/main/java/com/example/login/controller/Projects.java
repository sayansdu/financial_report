package com.example.login.controller;

import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.example.login.entity.Project;
import com.example.login.util.HibernateUtil;

public class Projects {

	public static ArrayList<Project> getProjects(){
		 Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		 session.beginTransaction();
		 ArrayList<Project> list = (ArrayList<Project>) session.createCriteria(Project.class).list();
		 session.getTransaction().commit();	 
		 return list;
	 }
	
	public static void addPosition(Project project){
		 Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		 session.beginTransaction();		 
		 session.save(project);		 
		 session.getTransaction().commit();	 
	 }
	
	public static ArrayList<Project> getByName(String name){
		 Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		 session.beginTransaction();		 
		 ArrayList<Project> list = (ArrayList<Project>) session.createCriteria(Project.class).
				 											   add(Restrictions.eq("name", name.trim())).list();		 
		 session.getTransaction().commit();	 
		 return list;
	 }
	
	public static void delete(Project project){
		 Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		 session.beginTransaction();
		 session.delete(project);
		 session.getTransaction().commit();	 
	}
	
}
