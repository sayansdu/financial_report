package com.example.login.task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.example.login.controller.Projects;
import com.example.login.controller.Tasks;
import com.example.login.entity.Project;
import com.example.login.entity.Student;
import com.example.login.entity.Task;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class All_Task extends Window{
	VerticalLayout main;
	VerticalLayout top;
	HorizontalLayout bottom;
	FormLayout task_layout;
	ComboBox projects;
	Panel main_panel;
	
	private ArrayList<Project> projects2;
	private Set<Task> tasks = new HashSet<Task>();
	
	public All_Task(){
		projects2 = Projects.getProjects();
		for (Task task : Tasks.getTasks()) {
				tasks.add(task);
		}
		
		setCaption("All Tasks");
		setModal(true);
		setResizable(false);
        addStyleName("edit-dashboard"); 
        setContent(main = new VerticalLayout());
        
        projects = new ComboBox();
        projects.setCaption("Select By Project: ");
        projects.setImmediate(true);
        projects.setTextInputAllowed(false);
        projects.setNullSelectionAllowed(false);
        
        projects.addItem("All task");
        for (Project proj : projects2) {
			projects.addItem(proj);
		}
		
        projects.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				if(projects.getValue() == "All task") update("All task");
				else  update((Project)projects.getValue());
			}
		});      
        
        task_layout = new FormLayout();
		task_layout.setCaption("Task List: ");
		task_layout.setSpacing(true);
		update("all task");
		
        main.addComponent(top = new VerticalLayout(){
        	{
        		setMargin(true);
        		setSpacing(true);
        		setWidth("500px");
        		addComponent(new Panel(){
        			{
        				setHeight("500px");
        				setContent(new FormLayout(){
		        			{
				        		addComponent(projects);		        		
				        		addComponent(task_layout);
		        			}
	        			});
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
	
	private void update(String all){
		task_layout.removeAllComponents();
		for (final Task task : tasks) {
			task_layout.addComponent(new FormLayout(){
				{
					setSpacing(false);
					setMargin(false);
					addComponent(new Label(task.getText()){
						{
							setCaption("Description: ");
						}
					});
					addComponent(new Label(){
						{
							setCaption("From: ");
							setValue(task.getStudent().toString());
						}
					});
					addComponent(new Label(){
						{
							setCaption("Receivers: ");
							String a = "";
							for (Student student : task.getTo()) {
								a+=student.getName()+" ";
							}
							setValue(a);
						}
					});
					
				}
			});
		}
		
	}
	
	private void update(Project project){
		task_layout.removeAllComponents();
		for (final Task task : tasks) {
			if(task.getProject().getId() == project.getId()){
				task_layout.addComponent(new FormLayout(){
					{
						setSpacing(false);
						setMargin(false);
						addComponent(new Label(task.getText()){
							{
								setCaption("Description: ");
							}
						});
						addComponent(new Label(){
							{
								setCaption("From: ");
								setValue(task.getStudent().toString());
							}
						});
						addComponent(new Label(){
							{
								setCaption("Receivers: ");
								String a = "";
								for (Student student : task.getTo()) {
									a+=student.getName()+" ";
								}
								setValue(a);
							}
						});
						
					}
				});
			}
		}
		
	}
}
