package com.example.login.task;

import java.util.ArrayList;
import java.util.Set;

import org.hibernate.Session;

import com.example.login.controller.Projects;
import com.example.login.controller.Users;
import com.example.login.entity.Project;
import com.example.login.entity.Student;
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
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.Reindeer;

@SuppressWarnings("serial")
public class Setting_Project extends Window {
	VerticalLayout main;
	VerticalLayout top;
	HorizontalLayout bottom;
	TextArea description;
	TextField title;
	PasswordField pf;
	HorizontalLayout users_layout;
	Label sub_text;
	ComboBox projects;
	ComboBox users;
	
	private Project current_project;
	private ArrayList<Project> projects2;
	private ArrayList<Student> users_list;
	private Set<Student> project_users;
	private Student current_user;
	
	public Setting_Project(Student username){
		current_user = username;
		setCaption("Current Project");
		setModal(true);
		setResizable(false);
        addStyleName("edit-dashboard");              
        setContent(main = new VerticalLayout());
        projects2 = Projects.getProjects();
		current_project = projects2.get(0);
		
        projects = new ComboBox();
        projects.setCaption("Projects List: ");
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
				project_users = current_project.getUser();
				update();
			}
		});
        project_users = current_project.getUser();
        
        users = new ComboBox();
        users.setImmediate(true);
        users.setTextInputAllowed(false);
        users.setNullSelectionAllowed(false);
        users_list = Users.getUsers();
        for (Student stud : users_list) {
			users.addItem(stud);
		}
        main.addComponent(top = new VerticalLayout(){
        	{
        		setMargin(true);
        		setSpacing(true);
        		addComponent(new FormLayout(){
        			{
        				description = new TextArea("Description: ");
        				description.setColumns(20);
        				description.setRows(5);
        				title = new TextField("Project Title: ");		
        				title.setColumns(20);
        				users_layout = new HorizontalLayout();		  users_layout.setCaption("Users: ");
        				pf = new PasswordField("Password: ");
        				sub_text = new Label();
        				sub_text.setStyleName("position_error");
        				update();
        				addComponent(projects);
        				addComponent(title);
        				addComponent(description);
        				addComponent(users_layout);      		
        				addComponent(pf);
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
                
        		 Button ok = new Button("Update");
        		 
                 ok.setClickShortcut(KeyCode.ESCAPE, null);
                 ok.addStyleName("wide");
                 ok.addStyleName("default");
                 addComponent(ok);
                 setComponentAlignment(ok, Alignment.TOP_RIGHT);
                 
                 ok.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						// TODO Auto-generated method stub
						if( !(title.getValue().isEmpty()) && (title.getValue().length()>2) ){
							if( !description.getValue().isEmpty() ){								
								if(!project_users.isEmpty()){
									if(pf.getValue().trim().equals(current_user.getPassword())){ 
										Session session = HibernateUtil.getSessionFactory().getCurrentSession();
										session.beginTransaction();
										current_project.setName(title.getValue().trim());
										current_project.setDescription(description.getValue());
										current_project.setUser(project_users);
										session.update(current_project);
										session.getTransaction().commit();
										
										close();
									}									
									else{ sub_text.setValue("User password is incorrect, try again"); }
								}
								else{	sub_text.setValue("Select at least one user");}
							}
							else{	sub_text.setValue("Description is empty");}
						}
						else{	sub_text.setValue("Name field empty or too short");}
						
					}
				});
        	}
        });
        
	}
	
	private void update(){
		users_layout.removeAllComponents();
		description.setValue(current_project.getDescription());
		title.setValue(current_project.getName());
		users_layout.addComponent(new VerticalLayout(){
			{				
				for (final Student student : project_users) {
					addComponent(new HorizontalLayout(){
						{
							addComponent(new Label("<b>"+student.toString()+"</b>&nbsp;&nbsp;", ContentMode.HTML));
							addComponent(new Button(){
								{
									setStyleName(Reindeer.BUTTON_LINK);
									setIcon(new ThemeResource("img/delete-icon2.png"));
									addClickListener(new ClickListener() {
										
										@Override
										public void buttonClick(ClickEvent event) {
											// TODO Auto-generated method stub
											project_users.remove(student);
											update();
										}
									});
								}
							});
						}
					});
				}
				addComponent(new Label());
				addComponent(new HorizontalLayout(){
					{
						setSpacing(true);
						addComponent(users);
						addComponent(new Button("Add"){
							{
								addClickListener(new ClickListener() {
									
									@Override
									public void buttonClick(ClickEvent event) {
										// TODO Auto-generated method stub
										Student temp = (Student) users.getValue();
										project_users.add(temp);
										update();
									}
								});
							}
						});
					}
				});
//				addComponent(new Button(){
//					{
//						setSizeUndefined();
//						setStyleName(Reindeer.BUTTON_LINK);    								
//						setIcon(new ThemeResource("img/signs-first.png"));
//						addClickListener(new ClickListener() {
//							
//							@Override
//							public void buttonClick(ClickEvent event) {
//								// TODO Auto-generated method stub
//								Window user = new Add_User();
//								getUI().addWindow(user);
//								user.addCloseListener(new CloseListener() {
//									
//									@Override
//									public void windowClose(CloseEvent e) {
//										//add_user_combobox();
//										
//									}
//								});
//							}
//						});
//					}
//				});
			}
		});
		
	}
	
}
