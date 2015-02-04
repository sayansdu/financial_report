package com.example.login.task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.example.login.LoginUI;
import org.hibernate.Session;

import com.example.login.controller.Tasks;
import com.example.login.entity.Project;
import com.example.login.entity.Student;
import com.example.login.entity.Task;
import com.example.login.util.HibernateUtil;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.Reindeer;

@SuppressWarnings("serial")
public class Delete_Task extends Window{
	VerticalLayout main;
	VerticalLayout top;
	VerticalLayout tasks_layout;
	HorizontalLayout bottom;
	ComboBox projects;
	
	private Student current_user;
	private Project current_project;
	private Set<Project> projects2;
	private Set<Task> task_list = new HashSet<Task>();
	
	public Delete_Task(Student username, Project project){
		current_user = username;
		current_project = project;
		for (Task task : Tasks.getTasks()) {
			if(task.getStudent().getId() == current_user.getId())
				if(task.getProject().getId()== (current_project.getId()))
					task_list.add(task);
				
		}
		
		setCaption("Delete Task");
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
				task_list.clear();
				for (Task task : Tasks.getTasks()) {
					if(task.getStudent().getId() == current_user.getId())
						if(task.getProject().getId()== (current_project.getId()))
							task_list.add(task);
						
				}
				update();
			}
		});
        
        main.addComponent(top = new VerticalLayout(){
        	{
        		setWidth("500px");
        		setMargin(true);
        		setSpacing(true);
        		addComponent(new FormLayout(){
        			{
        				setSpacing(true);
        				addComponent(projects);
        				tasks_layout = new VerticalLayout();
        				update();
        				addComponent(tasks_layout);
        				
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
		tasks_layout.removeAllComponents();
		for (final Task task : task_list) {
			tasks_layout.addComponent(new HorizontalLayout(){
				{
					addComponent(new Label("Desc: "+task.getText().substring(0, 15)+" ...&nbsp;&nbsp;"+
										   "To: "+(new ArrayList<Student>(task.getTo()).get(0)+"&nbsp;" ), ContentMode.HTML ));
					addComponent(new Button(){
						{
							setStyleName(Reindeer.BUTTON_LINK);
							setIcon(new ThemeResource("img/delete-icon2.png"));
							addClickListener(new ClickListener() {
								
								@Override
								public void buttonClick(ClickEvent event) {
                                    Session session = LoginUI.getCurrentSession();
                                    if(session.getTransaction() == null){
                                        session.beginTransaction();
                                    }
                                    else
                                        session.getTransaction().begin();

									session.delete(task);
									session.getTransaction().commit();
									task_list.clear();
									for (Task task : Tasks.getTasks()) {
										if(task.getStudent().getId() == current_user.getId())
											if(task.getProject().getId()== (current_project.getId()))
												task_list.add(task);
											
									}
									update();
									
								}
							});
						}
					});
				}
			});
		}
	}
}
