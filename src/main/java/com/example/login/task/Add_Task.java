package com.example.login.task;


import java.util.HashSet;
import java.util.Set;

import com.example.login.LoginUI;
import org.hibernate.Session;

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
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.Reindeer;

@SuppressWarnings("serial")
public class Add_Task extends Window{
	VerticalLayout main;
	VerticalLayout users_layout;
	HorizontalLayout top;
	HorizontalLayout bottom;
	FormLayout form;
	TextArea ta;
	Label sub_text;
	
	private ComboBox users;
	private ComboBox projects;
	private Set<Student> user_list;
	private Set<Student> selected_users = new HashSet<Student>();
	final Student current_user;
	Project current_project;
	
	public Add_Task(Student username, Project project){
		current_user = username;
		current_project = project;
		setCaption("Create Task");
		setModal(true);
		setResizable(false);
        addStyleName("edit-dashboard");              
        setContent(main = new VerticalLayout());
		
        user_list = current_project.getUser();
        users = new ComboBox();
        users.setNullSelectionAllowed(false);
        for (Student student : user_list) {
			users.addItem(student);
		}
        
        projects = new ComboBox("Project List");
        projects.setImmediate(true);
        projects.setNullSelectionAllowed(false);
        for (Project proj : current_user.getProjects()) {
			projects.addItem(proj);
		}
        projects.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {

				current_project = (Project) projects.getValue();				
				ta.setValue("");
				selected_users.clear();
				users_layout.removeAllComponents();
				user_list = current_project.getUser();
		        users.removeAllItems();
		        for (Student student : user_list) {
					users.addItem(student);
				}
			}
		});
        
        main.addComponent(top = new HorizontalLayout(){
        	{
        		setMargin(true);
        		setSpacing(true);
        		addComponent(form = new FormLayout(){
        			{
        				setSpacing(true);
        				addComponent(projects);
        				addComponent(new Label());
    
        				ta = new TextArea("Description: ");
        				ta.setColumns(20);
        				ta.setRows(3);
        				addComponent(ta);
        				users_layout = new VerticalLayout();
        				users_layout.setSpacing(false);
        				users_layout.setCaption("Receiver User: ");
        				addComponent(users_layout);
        				addComponent(new HorizontalLayout(){
        					{
        						setSpacing(true);
        						addComponent(users);
        						addComponent(new Button("Add"){
        							{	       								
        								addClickListener(new ClickListener() {
        									
        									@Override
        									public void buttonClick(ClickEvent event) {
        										Student temp = (Student) users.getValue();
        										selected_users.add(temp);
        										update();
        									}
        								});
        							}
        						});
        					}
        				});
        				
        				sub_text = new Label();
        				sub_text.setStyleName("position_error");
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
                
                Button ok = new Button("Create");
                ok.setClickShortcut(KeyCode.ESCAPE, null);
                ok.addStyleName("wide");
                ok.addStyleName("default");
                addComponent(ok);
                setComponentAlignment(ok, Alignment.TOP_RIGHT);
                
                ok.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {

						if(!ta.getValue().isEmpty()){
							if(!selected_users.isEmpty()){
                                Session session = LoginUI.getCurrentSession();
                                if(session.getTransaction() == null){
                                    session.beginTransaction();
                                }
                                else
                                    session.getTransaction().begin();

								Task new_task = new Task();
								new_task.setProject(current_project);
								new_task.setStudent(current_user);
								new_task.setTo(selected_users);
								new_task.setText(ta.getValue());
								new_task.setDone(false);
								new_task.setReaded(false);
								session.save(new_task);
								session.getTransaction().commit();
								close();
							}
							else{  sub_text.setValue("Select at least one user");  }
						}
						else{ sub_text.setValue("Description is empty"); }
						
					}
				});
        	}
        });
        
	}
	
	private void update(){
		users_layout.removeAllComponents();
		for (final Student student : selected_users) {
			users_layout.addComponent(new HorizontalLayout(){
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
									selected_users.remove(student);
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
