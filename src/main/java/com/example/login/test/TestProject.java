package com.example.login.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;

import com.example.login.controller.Projects;
import com.example.login.controller.Users;
import com.example.login.entity.Project;
import com.example.login.entity.Student;
import com.example.login.util.HibernateUtil;

public class TestProject {

	public static void main(String[] args){
		update();
	}
	
	private static void creatProject(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		Project project = new Project();
		project.setName("student");
		project.setDescription("analysis about students in university");
		Set<Student> studs = new HashSet<Student>(Users.getUsers());
		project.setUser(studs);
		session.save(project);
		System.out.println("add project is complete");
		session.getTransaction().commit();
		session.close();
	}
	
	private static void getProjects(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		ArrayList<Project> projects = Projects.getProjects();
		for (Project project : projects) {
			System.out.println(project.getName()+" desc: "+ project.getDescription());
		}
		session.close();
	}
	
	private static void getProjectUsers(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		ArrayList<Project> projects = Projects.getProjects();
		for (Project project : projects) {
			Set<Student> users = project.getUser();
			System.out.print(project.getName()+" \nUsers: ");
			for (Student student : users) {
				System.out.print(student.getName()+" ");
			}
		}
		session.close();
	}
	
	private static void getProjectByName(String name){
		Session session = HibernateUtil.getSessionFactory().openSession();
		ArrayList<Project> projects = Projects.getByName(name);
		for (Project project : projects) {
			System.out.println(project.getName()+" "+project.getId());
		}
		session.close();
	}
	
	private static void delete(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Project temp = Projects.getByName("student").get(0);
		session.beginTransaction();	
		session.delete(temp);		
		session.getTransaction().commit();	 
		session.close();
	}
	
	private static void update(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Project temp = Projects.getByName("beverages").get(0);
		session.beginTransaction();	
		Set<Student> temp2 = new HashSet<Student>();
		ArrayList<Student> user = Users.getUsers();
		for(int i=1; i<4; i++){
			temp2.add(user.get(i));
		}
		temp.setUser(temp2);
		session.update(temp);		
		session.getTransaction().commit();	 
		session.close();
	}
	
}
