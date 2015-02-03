package com.example.login.test;

import java.util.ArrayList;
import java.util.Set;

import org.hibernate.Session;

import com.example.login.controller.Users;
import com.example.login.entity.Project;
import com.example.login.entity.Student;
import com.example.login.entity.Task;
import com.example.login.util.HibernateUtil;

public class TestStudent {
	public static void main(String[] args){
				
		getStudents();
		
	}
	
	private static void addStudent(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		Student aidos =new Student();
		aidos.setName("Aidos");
		aidos.setPassword("aidos");
		aidos.setEmail("aidos@mail.ru");
		
		session.save(aidos);
		session.getTransaction().commit();
		session.close();
	}
	
	private static void getStudents(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		ArrayList<Student> stud = Users.getUsers();
		for (Student student : stud) {
			System.out.print(student.getName()+" is "+student.getPosition()+" tasks:");
			Set<Task> tasks = student.getSended();
			for (Task task : tasks) {
				System.out.print(task.getText()+" : ");
			}
			System.out.println();
		}
		
		session.getTransaction().commit();
		session.close();
	}
	
	private static void getStudentProject(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		ArrayList<Student> stud = Users.getUsers();
		for (Student student : stud) {
			Set<Project> projects = student.getProjects();
			System.out.print(student.getName()+" \nProjects:  ");
			for (Project project : projects) {
				System.out.print(project.getName()+" ");
			}
			System.out.println();
		}
		
		session.getTransaction().commit();
		session.close();
	}
	
	private static void getStudentByName(String name){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Set<Student> stud = Users.get_by_name(name);
		for (Student student : stud) {
			System.out.println(student.getName()+" ");
		}
		session.close();
	}
	
}
