package com.example.login.task;

import java.util.Set;

import com.example.login.entity.Project;
import com.example.login.entity.Student;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class Current_Project extends Window{
	VerticalLayout main;
	VerticalLayout top;
	HorizontalLayout bottom;
	HorizontalLayout description;
	HorizontalLayout title;
	HorizontalLayout users;
	
	ComboBox projects;
	
	private Project current_project;
	private Student current_user;
	private Set<Project> projects2;
	
	public Current_Project(Project pro, Student user){
		current_project = pro;
		current_user = user;
		setCaption("Current Project");
		setModal(true);
		setResizable(false);
        addStyleName("edit-dashboard");              
        setContent(main = new VerticalLayout());
        projects2 = current_user.getProjects();
        projects = new ComboBox();
        projects.setCaption("Project List: ");
        projects.setImmediate(true);
        projects.setTextInputAllowed(false);
        projects.setNullSelectionAllowed(false);
        
        for (Project proj : projects2) {
			projects.addItem(proj);
		}
        projects.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				current_project = (Project) projects.getValue();
				update();
			}
		});
        main.addComponent(top = new VerticalLayout(){
        	{
        		setMargin(true);
        		setSpacing(true);
        		addComponent(new FormLayout(){
        			{
        				description = new HorizontalLayout(); description.setCaption("Description: ");
        				title = new HorizontalLayout();       title.setCaption("Project Title: "); 				
        				users = new HorizontalLayout();		  users.setCaption("Users: ");
        				update();
        				addComponent(projects);
        				addComponent(title);
        				addComponent(description);
        				addComponent(users);
        				
        				
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
                
        		 Button ok = new Button("Close");
                 ok.setClickShortcut(KeyCode.ESCAPE, null);
                 ok.addStyleName("wide");
                 ok.addStyleName("default");
                 addComponent(ok);
                 setComponentAlignment(ok, Alignment.TOP_RIGHT);
                 
                 ok.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						// TODO Auto-generated method stub
						close();
					}
				});
        	}
        });
	}
	
	private void update(){
		description.removeAllComponents();
		title.removeAllComponents();
		users.removeAllComponents();
		description.addComponent(new Label("<b>"+current_project.getDescription()+"</b>", ContentMode.HTML));
		title.addComponent(new Label("<b>"+current_project.getName()+"</b>", ContentMode.HTML));
		users.addComponent(new VerticalLayout(){
			{
				for (Student student : current_project.getUser()) {
					addComponent(new Label("<b>"+student.toString()+"</b>", ContentMode.HTML));
				}
			}
		});
	}
}
