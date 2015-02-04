package com.example.login.task;

import com.example.login.LoginUI;
import org.hibernate.Session;

import com.example.login.entity.Student;
import com.example.login.util.HibernateUtil;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class Password extends Window {
	
	final Student current_user2;
	VerticalLayout main;
	HorizontalLayout top;
	HorizontalLayout bottom;
	FormLayout form;
	PasswordField pas1;
	PasswordField pas2;
	PasswordField pas3;
	Label sub_text;
	
	public Password(Student username){
		setCaption("Change Password");
		setModal(true);
		setResizable(false);
        addStyleName("edit-dashboard");              
        setContent(main = new VerticalLayout());
        current_user2 = username;
        
        main.addComponent(top = new HorizontalLayout(){
        	{
        		setMargin(true);
        		setSpacing(true);
        		addComponent(form = new FormLayout(){
        			{
        				pas1 = new PasswordField("Current Password");
        				addComponent(pas1);
        				pas2 = new PasswordField("New Password");
        				addComponent(pas2);
        				pas3 = new PasswordField("Confirm Password");
        				addComponent(pas3);
        				sub_text = new Label("");
        				sub_text.addStyleName("profile_sub");
        				addComponent(sub_text);
        				
        			}
        		});
        	}
        });	
        
        main.addComponent(bottom = new HorizontalLayout(){
        	{
        		setMargin(true);
                setSpacing(true);
                addStyleName("footer");
                setWidth("100%");
                Button cancel = new Button("Cancel");
                cancel.addClickListener(new ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                        close();
                    }
                });
                cancel.setClickShortcut(KeyCode.ESCAPE, null);
                addComponent(cancel);
                setExpandRatio(cancel, 1);
                
                Button ok = new Button("Do it");
                ok.addClickListener(new ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                    	if( (pas1.getValue().equals(current_user2.getPassword())) && (pas2.getValue().equals(pas3.getValue())))
                    	{
                    		Session session = LoginUI.getCurrentSession();
	    		   		 	if(session.getTransaction() == null){
                                session.beginTransaction();
                            }
                            else
                                session.getTransaction().begin();

	    		   		 	current_user2.setPassword(pas2.getValue());
	    		   		 	session.update(current_user2);
	    		    		session.getTransaction().commit();
	                        close();
                    	}
                    	else{
                    		sub_text.setValue("Password is incorrect, try again");
                    	}
                    }
                });
                ok.setClickShortcut(KeyCode.ESCAPE, null);
                ok.addStyleName("wide");
                ok.addStyleName("default");
                addComponent(ok);
                setComponentAlignment(ok, Alignment.TOP_RIGHT);
                
        	}
        });
        
	}
}
