package com.example.login.controller;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.example.login.entity.Position;
import com.example.login.util.HibernateUtil;

public class Positions {
	
	public static List<Position> getPositions(){
		 Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		 session.beginTransaction();
		 List<Position> list =  session.createCriteria(Position.class).list();		 
		 session.getTransaction().commit();	
		 for (Position position : list) {
				if(position.getTitle().toLowerCase().equals("admin")) {
					list.remove(position); 
					break;
				}
		}
		 return list;
	 }
	
	public Position get_user_by_name(String name){
		 Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		 session.beginTransaction();
		 Position user = new Position();
		 List<Position> list =  session.createCriteria(Position.class).list();
		 for (Position student : list) {
			if(student.getTitle().equals(name.trim()) ){	user = student;	}				 
		 }
		 session.getTransaction().commit();	
		 return user;
	}
	
	public static ArrayList<String> getTitles(){
		 Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		 session.beginTransaction();
		 List<Position> list =  session.createCriteria(Position.class).list();
		 ArrayList<String> titles = new ArrayList<String>();
		 for (Position student : list) {
				titles.add(student.getTitle());			 
		 }
		 session.getTransaction().commit();	 
		 return titles;
	 }
	
	public static void addPosition(Position student){
		 Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		 session.beginTransaction();		 
		 session.save(student);		 
		 session.getTransaction().commit();	 
	 }
}
