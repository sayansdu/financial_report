package com.example.login.task;

import java.util.ArrayList;

import com.example.login.controller.Projects;
import com.example.login.entity.Project;
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
public class Delete_Project extends Window {
	VerticalLayout main;
	HorizontalLayout top;
	HorizontalLayout bottom;
	
	ComboBox projects;
	private ArrayList<Project> projects2 = Projects.getProjects();
	
	public Delete_Project(){
		setCaption("Delete Project");
		setModal(true);
		setResizable(false);
        addStyleName("edit-dashboard");              
        setContent(main = new VerticalLayout());
        
        projects = new ComboBox();
        projects.setNullSelectionAllowed(false);
        for (Project proj : projects2) {
			projects.addItem(proj);
		}
        main.addComponent(new Label());
        main.addComponent(top = new HorizontalLayout(){
        	{
        		setMargin(true);
        		setSpacing(true);
        		
        		addComponent(projects);
        		addComponent(new Button("Delete"){
        			{
        				addClickListener(new ClickListener() {
							
							@Override
							public void buttonClick(ClickEvent event) {
								// TODO Auto-generated method stub
								Project temp = (Project) projects.getValue();
								Projects.delete(temp);
								update();
							}
						});
        				
        			}
        		});
        	}
        });
        main.addComponent(new Label(){
        	{
        		setHeight("2px");
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
		projects.removeAllItems();
		projects2 = Projects.getProjects();
		for (Project proj : projects2) {
			projects.addItem(proj);
		}
	}
}
