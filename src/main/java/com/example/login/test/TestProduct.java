package com.example.login.test;

import java.util.Set;

import org.hibernate.Session;

import com.example.login.controller.Products;
import com.example.login.controller.Projects;
import com.example.login.entity.Product;
import com.example.login.entity.Project;
import com.example.login.util.HibernateUtil;

public class TestProduct {
	
	public static void main(String[] args){
		get_all();
	}
	
	private static void add_task(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Project project = Projects.getByName("beverages").get(0);
		Products.add_product("sprite", project);
		session.close();
	}
	
	private static void get_all(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Set<Product> pro = Products.get_products();
		for (Product product : pro) {
			System.out.println("name: "+product);
		}
		session.close();
	}
}
