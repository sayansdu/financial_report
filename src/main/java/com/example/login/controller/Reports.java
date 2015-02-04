package com.example.login.controller;

import com.example.login.LoginUI;
import com.example.login.entity.Report;
import com.example.login.util.HibernateUtil;
import org.hibernate.Session;

import java.util.ArrayList;

public class Reports {

    public static ArrayList<Report> getReports(){
        Session session = LoginUI.getCurrentSession();
        if(session.getTransaction() == null){
            session.beginTransaction();
        }
        else
            session.getTransaction().begin();

        ArrayList<Report> list = (ArrayList<Report>) session.createCriteria(Report.class).list();
        session.getTransaction().commit();
        return list;
    }
}
