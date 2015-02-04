package com.example.login.task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.hibernate.Session;

import com.example.login.LoginUI;
import com.example.login.controller.Users;
import com.example.login.entity.Project;
import com.example.login.entity.Student;
import com.example.login.util.HibernateUtil;
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
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.Reindeer;

@SuppressWarnings("serial")
public class Add_Project extends Window{
	VerticalLayout main;
	HorizontalLayout top;
	HorizontalLayout bottom;
	FormLayout form;
	TextField tf;
	TextArea ta;
	Label sub_text;
	
	private static ComboBox users;
	private static List<Student> user = Users.getUsers();
	private static ArrayList<Student> selected_users = new ArrayList<Student>();
	private static VerticalLayout selected_users_layout;
	final Student current_user2;
	
	public Add_Project(Student username){
		current_user2 = username;		
		setCaption("Add New Project");
		setModal(true);
		setResizable(false);
        addStyleName("edit-dashboard");              
        setContent(main = new VerticalLayout());
        
        main.addComponent(top = new HorizontalLayout(){
        	{
        		setMargin(true);
        		setSpacing(true);
        		addComponent(form = new FormLayout(){
        			{
        				addComponent(new Label(""));
        				tf = new TextField("Project Name: ");       		
        				tf.setColumns(20);
        				tf.setRequired(true);
        				tf.setRequiredError("Name field cannot be empty or too short");
        				addComponent(tf);
        				
        				ta = new TextArea("Description: ");
        				ta.setColumns(20);
        				ta.setRows(5);
        				ta.setRequired(true);
        				ta.setRequiredError("Description cannot be empty");
        				addComponent(ta);
        				        				
        				selected_users_layout = new VerticalLayout();
        				addComponent(selected_users_layout); 
        				
        				addComponent(new HorizontalLayout(){
        					{
        						setCaption("Select User");
        						users = new ComboBox();
        						users.addStyleName("window-label");
        						users.setNullSelectionAllowed(false);
        						add_user_combobox();
        						addComponent(new HorizontalLayout(){
        							{
        								setSpacing(true);
        								addComponent(users);
        								addComponent(new Button("Add"){
        									{
        										setImmediate(true);
        										addClickListener(new ClickListener() {
													
													@Override
													public void buttonClick(ClickEvent event) {
														Student temp_user = (Student) users.getValue();
														selected_users.add(temp_user);
														removeDublicates();
														add_selected_user();
														
													}
												});
        									}
        								});
        							}
        						});
        								
        					}
        				});
        				addComponent(new HorizontalLayout(){
        					{
        						setCaption("Add Users");
        						addComponent(new Button(){
        							{	
        								setSizeUndefined();
        								setStyleName(Reindeer.BUTTON_LINK);    								
        								setIcon(new ThemeResource("img/signs-first.png"));
        								
        								addClickListener(new ClickListener() {
											
											@Override
											public void buttonClick(ClickEvent event) {
												// TODO Auto-generated method stub
												Window user = new Add_User();
												getUI().addWindow(user);
												user.addCloseListener(new CloseListener() {
													
													@Override
													public void windowClose(CloseEvent e) {
														add_user_combobox();
														
													}
												});
											}
										});
        							}
        						});
        								
        					}
        				});

        				sub_text = new Label("");
        				sub_text.addStyleName("position_error");
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
						// TODO Auto-generated method stub
						if( !(tf.getValue().isEmpty()) && (tf.getValue().length()>2) ){
							if(!ta.getValue().isEmpty()){
								if(!selected_users.isEmpty()){
									Project project = new Project();

                                    Session session = LoginUI.getCurrentSession();
                                    if(session.getTransaction() == null){
                                        session.beginTransaction();
                                    }
                                    else
                                        session.getTransaction().begin();
									
									project.setName(tf.getValue().trim());
									project.setDescription(ta.getValue().trim());
									project.setUser(new HashSet<Student>(selected_users));
									LoginUI.current_project = project;
									session.save(project);

									for (Student student : selected_users) {
										student.setCurrent_project(project);
										session.update(student);
									}
									session.getTransaction().commit();
									selected_users.clear();
									close();
								}
								else{
									sub_text.setValue("Select at least ane user");
								}
							}							
							else{
								sub_text.setValue("Description is empty");
							}
						}
						else{
							sub_text.setValue("Name field empty or too short");
						}
					}
				});
        	}
        });
        
	}	
	
	private static void add_user_combobox(){
		user = Users.getUsers();
		users.removeAllItems();
		for (Student stud : user) {
			users.addItem(stud);
		}
	}
	
	private static void add_selected_user(){
		selected_users_layout.removeAllComponents();
		for (final Student student : selected_users) {
			selected_users_layout.addComponent(new HorizontalLayout(){
				{
					addComponent(new Label(student.toString()+"&nbsp;&nbsp;", ContentMode.HTML){
						{
							addStyleName(Reindeer.LABEL_SMALL);
						}
					});
					addComponent(new Button(){
						{
							setStyleName(Reindeer.BUTTON_LINK);
							setIcon(new ThemeResource("img/delete-icon2.png"));
							
							addClickListener(new ClickListener() {
								
								@Override
								public void buttonClick(ClickEvent event) {
									// TODO Auto-generated method stub
									selected_users.remove(student);
									add_selected_user();
								}
							});
						}
					});
				}
			});
		}
	}
	
	private static void removeDublicates(){
		HashSet<Student> hash = new HashSet<Student>();
		hash.addAll(selected_users);
		selected_users.clear();
		selected_users.addAll(hash);
	}
}
