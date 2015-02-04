package com.example.login.controller;

import com.example.login.entity.Report;
import com.example.login.util.HibernateUtil;
import org.hibernate.Session;

import java.util.ArrayList;

public class Reports {

    public static ArrayList<Report> getReports(){
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        ArrayList<Report> list = (ArrayList<Report>) session.createCriteria(Report.class).list();
        session.getTransaction().commit();
        return list;
    }
}
