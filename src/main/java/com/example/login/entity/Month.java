package com.example.login.entity;

import javax.persistence.*;

@Entity
@Table(name="month")
public class Month {
	private long id;
	private String name;
	private String rusname;
	
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
	
	@Column(name="rusname")
	public String getRusname() {
		return rusname;
	}
	public void setRusname(String rusname) {
		this.rusname = rusname;
	}
	
	public String toString(){
		return this.getName();
	}
}
