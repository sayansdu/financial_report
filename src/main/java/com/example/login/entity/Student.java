package com.example.login.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="student")
public class Student implements Serializable{
	private long id;
	private String name;
	private String password;
	private String email;
	private String logo;
	private Position position;
	private Set<Project> projects = new HashSet<Project>();
	private Set<Task> to;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	@Column(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(name="email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column(name="logo", nullable = true)
	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}
	
	@ManyToOne
	@JoinColumn(name="position_id", nullable=false)
	public Position getPosition(){
		return position;
	}

	public void setPosition(Position position){
		this.position = position;
	}
	
	@ManyToMany(mappedBy = "user", fetch=FetchType.EAGER)
	public Set<Project> getProjects() {
		return projects;
	}

	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}

	
	@ManyToMany(mappedBy = "to", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	public Set<Task> getTo() {
		return to;
	}

	public void setTo(Set<Task> projects) {
		this.to = projects;
	}
	
	public String toString(){
		return name+" [position:"+position.getTitle()+"]";
	}
}
