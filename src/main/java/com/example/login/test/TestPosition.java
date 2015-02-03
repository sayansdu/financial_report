package com.example.login.test;


import java.util.List;

import org.hibernate.Session;

import com.example.login.controller.Positions;
import com.example.login.entity.Position;
import com.example.login.util.HibernateUtil;

public class TestPosition {

	public static void main(String[] args){
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Position> position = Positions.getPositions();
		for (Position string : position) {
			System.out.println(string);
		}
		session.close();
		
	}
}
