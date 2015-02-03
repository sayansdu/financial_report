package com.example.login.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="task")
public class Task {

	private long id;
	private Project project;
	private Student from;
	private Set<Student> to;
	private String text;
	private boolean done;
	private boolean readed;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="project_id", nullable=false)
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
		
	@ManyToOne
	@JoinColumn(name = "from_id", nullable=false)
	public Student getStudent() {
		return from;
	}
	public void setStudent(Student from) {
		this.from = from;
	}
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="task_user", joinColumns = { 
			@JoinColumn(name = "task_id", nullable = false, updatable = false) },
			inverseJoinColumns = { @JoinColumn(name = "student_id", nullable = false, updatable = false) }
	)	
	public Set<Student> getTo() {
		return to;
	}
	public void setTo(Set<Student> to) {
		this.to = to;
	}
	
	@Column(name="text")
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	@Column(name="done")
	public boolean isDone() {
		return done;
	}
	public void setDone(boolean done) {
		this.done = done;
	}
	
	@Column(name="readed")
	public boolean isReaded() {
		return readed;
	}
	public void setReaded(boolean readed) {
		this.readed = readed;
	}

	
}
