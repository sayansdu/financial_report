package com.example.login.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Report {
	
	private long id;
	private Product product;
	private int year;
	private Month month;
	
	private int amount;
	private int sold_amount;
	private int price;
	private int cost_price;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name="product", nullable=false)
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	
	@Column(name="year")
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	
	
	@ManyToOne
	@JoinColumn(name="month", nullable=false)
	public Month getMonth() {
		return month;
	}
	public void setMonth(Month month) {
		this.month = month;
	}
	
	@Column(name="amount")
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	@Column(name="sold_amount")
	public int getSold_amount() {
		return sold_amount;
	}
	public void setSold_amount(int sold_amount) {
		this.sold_amount = sold_amount;
	}
	
	@Column(name="price")
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	
	@Column(name="cost_price")
	public int getCost_price() {
		return cost_price;
	}
	public void setCost_price(int cost_price) {
		this.cost_price = cost_price;
	}
	
	@Override
	public String toString() {
		return "Report [year=" + year + ", month=" + month + ", amount="
				+ amount + ", sold_amount=" + sold_amount + ", price=" + price
				+ ", cost_price=" + cost_price + "]";
	}
}