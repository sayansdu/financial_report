package com.example.login.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.example.login.entity.Student;
import com.example.login.util.HibernateUtil;


public class Users {
	
	public static ArrayList<Student> getUsers(){
		 Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		 session.beginTransaction();
		 ArrayList<Student> list = (ArrayList<Student>) session.createCriteria(Student.class).list();
		 session.getTransaction().commit();	 
		 for (Student student : list) {
			if(student.getPosition().getTitle().toLowerCase().equals("admin")){
				list.remove(student);
				break;
			}
		}
		 return list;
	 }
	
	public boolean is_it_user(String user, String password){
		 Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		 session.beginTransaction();
		 ArrayList<Student> list = (ArrayList<Student>) session.createCriteria(Student.class).list();
		 for (Student student : list) {
			if(student.getName().equals(user.trim()) && student.getPassword().equals(password.trim())){
				session.getTransaction().commit();	
				return true;
			}
				 
		 }
		 session.getTransaction().commit();	 
		 return false;
	 }
	
	public static Student get_user_by_name(String name){
		 Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		 session.beginTransaction();
		 Student user = new Student();
		 ArrayList<Student> list = (ArrayList<Student>) session.createCriteria(Student.class).list();
		 for (Student student : list) {
			if(student.getName().equals(name.trim()) ){	user = student;	}				 
		 }
		 session.getTransaction().commit();	
		 return user;
	}
	
	public static Set<Student> get_by_name(String name){
		 Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		 session.beginTransaction();
		 Set<Student> list =  new HashSet<Student>(  session.createCriteria(Student.class).add(Restrictions.eq("name",name)).list() );
		 session.getTransaction().commit();	
		 return list;
	}
	
	
}
