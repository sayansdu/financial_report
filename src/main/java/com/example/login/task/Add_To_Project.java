package com.example.login.task;

import java.util.ArrayList;

import org.hibernate.Session;

import com.example.login.controller.Projects;
import com.example.login.entity.Project;
import com.example.login.entity.Student;
import com.example.login.util.HibernateUtil;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class Add_To_Project extends Window{
	VerticalLayout main;
	VerticalLayout top;
	HorizontalLayout bottom;
	Label sub_text;
	
	private static ArrayList<Project> projects;
	private static ComboBox project;
	final Student current_user2;
	
	public Add_To_Project(Student student){
		current_user2 = student;
		setCaption("Select Project");
		setModal(true);
		setResizable(false);
		setClosable(false);
        addStyleName("edit-dashboard");              
        setContent(main = new VerticalLayout());
        setImmediate(true);
        
        main.addComponent(top = new VerticalLayout(){
        	{
        		setWidth("210px");
        		setMargin(true);
        		setSpacing(true);
        		project = new ComboBox();
        		addProjects();
        		addComponent(new Label(""));
        		addComponent(project);
        		setComponentAlignment(project, Alignment.MIDDLE_CENTER);
        		sub_text = new Label("");
        		addComponent(sub_text);
        	}
        });
        main.addComponent(bottom = new HorizontalLayout(){
        	{
        		setMargin(true);
                setSpacing(true);
                addStyleName("footer");
                setWidth("100%");
                
                Button ok = new Button("Continue");
                ok.setClickShortcut(KeyCode.ESCAPE, null);
                ok.addStyleName("wide");
                ok.addStyleName("default");
                addComponent(ok);
                setComponentAlignment(ok, Alignment.TOP_RIGHT);
                ok.addClickListener(new ClickListener() {
                	@Override
					public void buttonClick(ClickEvent event) {
                		if(project.isValid()){
                			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
                			session.beginTransaction();
                			Project current = (Project) project.getValue();
                			current.getUser().add(current_user2);
                			session.update(current);
                			current_user2.setCurrent_project(current);
                			session.update(current_user2);
                			session.getTransaction().commit();
                			close();
                		}
                		else{
                			sub_text.setValue("Please, select the project");
                		}
                	}
                });
        	}
        });
	}

	private void addProjects(){
		projects = Projects.getProjects();
		project.removeAllItems();
		for (Project pro : projects) {
			project.addItem(pro);
		}
	}
}
