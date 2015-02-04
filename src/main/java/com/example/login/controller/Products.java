package com.example.login.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.example.login.LoginUI;
import org.hibernate.Session;

import com.example.login.entity.Product;
import com.example.login.entity.Project;
import com.example.login.util.HibernateUtil;

public class Products {
	
	public static ArrayList<Product> getProducts(){
        Session session = LoginUI.getCurrentSession();
        if(session.getTransaction() == null){
            session.beginTransaction();
        }
        else
            session.getTransaction().begin();

		 ArrayList<Product> list =  (ArrayList) session.createCriteria(Product.class).list();		 
		 session.getTransaction().commit();	
		 return list;
	 }
	
	public static Set<Product> get_products(){
        Session session = LoginUI.getCurrentSession();
        if(session.getTransaction() == null){
            session.beginTransaction();
        }
        else
            session.getTransaction().begin();

		 Set<Product> list = new HashSet<Product>( session.createCriteria(Product.class).list() );		 
		 session.getTransaction().commit();	
		 return list;
	 }
	
	public static void add_product(String name, Project project){
        Session session = LoginUI.getCurrentSession();
        if(session.getTransaction() == null){
            session.beginTransaction();
        }
        else
            session.getTransaction().begin();
        Product product = new Product();
        product.setName(name);
        product.setProject(project);
        session.save(product);
		session.getTransaction().commit();
		 
	 }
}
