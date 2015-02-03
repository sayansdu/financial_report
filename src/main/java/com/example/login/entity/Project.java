package com.example.login.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="project")
public class Project {
	private long id;
	private String name;
	private String description;
	private Set<Student> user;
	private Set<Task> tasks;
	private Set<Product> products;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="project_user", joinColumns = { 
			@JoinColumn(name = "student_id", nullable = false, updatable = false) },
			inverseJoinColumns = { @JoinColumn(name = "project_id", nullable = false, updatable = false) }
	)	
	public Set<Student> getUser() {
		return user;
	}
	public void setUser(Set<Student> user) {
		this.user = user;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "project" )
	public Set<Task> getTask() {
		return tasks;
	}
	public void setTask(Set<Task> tasks) {
		this.tasks = tasks;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "project" )
	public Set<Product> getProducts() {
		return products;
	}
	public void setProducts(Set<Product> products) {
		this.products = products;
	}
	
	public String toString(){
		return this.name;
	}
}
