package com.example.login.test;

import java.util.ArrayList;
import java.util.Set;

import org.hibernate.Session;

import com.example.login.controller.Projects;
import com.example.login.controller.Tasks;
import com.example.login.controller.Users;
import com.example.login.entity.Project;
import com.example.login.entity.Student;
import com.example.login.entity.Task;
import com.example.login.util.HibernateUtil;

public class TaskTest {
	
	public static void main(String[] args){

			getTasks();
	}

	private static void addTask(){
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		Task task = new Task();
		Project project = Projects.getByName("houses").get(0);
			System.out.println(project.getName());
		Student from = Users.get_user_by_name("hp");
			System.out.println(from.getName());
		Set<Student> to = Users.get_by_name("stive");

		task.setProject(project);
		task.setStudent(from);
		task.setTo(to);		
		task.setText("flat analysis in akkkent");
		task.setDone(false);
		task.setReaded(false);
		
		session.save(task);
		session.getTransaction().commit();
		session.close();
	}
	
	private static void getTasks(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		ArrayList<Task> tasks = new Tasks().getTasks();
		for (Task task : tasks) {
			System.out.println(task.getText()+" from: "+ task.getStudent().getName());
		}
		session.close();
	}
	
	private static void checkReceivers(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		ArrayList<Task> tasks = new Tasks().getTasks();
		Set<Student> receiver = tasks.get(0).getTo();
		for (Student task : receiver) {
			System.out.println("name: "+task.getName()+" from: "+ task.getPosition());
		}
		session.close();
	}
	
	private static void delete(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		ArrayList<Task> tasks = new Tasks().getTasks();
		Task receiver = tasks.get(0);
		System.out.println("id: "+receiver.getId()+" project: "+receiver.getProject()+" from: "+receiver.getStudent() );
		session.delete(receiver);
		session.getTransaction().commit();
		session.close();
	}
	
	private static void check_sender(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		Student temp = Users.get_user_by_name("hp");
		Set<Task> tasks = temp.getReceived();
		for (Task task : tasks) {
			System.out.println(task.getText()+" receiver:"+ task.getStudent());
		}
		session.getTransaction().commit();
		session.close();
	}
}
