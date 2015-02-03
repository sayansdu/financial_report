package com.example.login.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name="product")
public class Product {
	private int id;
	private String name;
	private Project project;
	private Set<Report> report = new HashSet<Report>();
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="project", nullable=false)
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "product")
	public Set<Report> getReport() {
		return report;
	}
	public void setReport(Set<Report> report) {
		this.report = report;
	}
	
	@Override
	public String toString() {
		return "[" + name + "]";
	}
	
}
