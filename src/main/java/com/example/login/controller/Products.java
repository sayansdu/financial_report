package com.example.login.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;

import com.example.login.entity.Product;
import com.example.login.entity.Project;
import com.example.login.util.HibernateUtil;

public class Products {
	
	public static ArrayList<Product> getProducts(){
		 Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		 session.beginTransaction();
		 ArrayList<Product> list =  (ArrayList) session.createCriteria(Product.class).list();		 
		 session.getTransaction().commit();	
		 return list;
	 }
	
	public static Set<Product> get_products(){
		 Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		 session.beginTransaction();
		 Set<Product> list = new HashSet<Product>( session.createCriteria(Product.class).list() );		 
		 session.getTransaction().commit();	
		 return list;
	 }
	
	public static void add_product(String name, Project project){
		 Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		 session.beginTransaction();
		 	Product product = new Product();
		 	product.setName(name);
		 	product.setProject(project);
		 	session.save(product);
		 session.getTransaction().commit();	
		 
	 }
}
