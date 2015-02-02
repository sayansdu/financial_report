package com.example.login.task;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Session;

import com.example.login.controller.Positions;

import com.example.login.entity.Position;
import com.example.login.entity.Student;
import com.example.login.util.HibernateUtil;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.Reindeer;

@SuppressWarnings("serial")
public class Add_User extends Window {
	VerticalLayout main;
	HorizontalLayout top;
	HorizontalLayout bottom;
	FormLayout form;
	TextField tf;
	TextField tf2;
	PasswordField pas1;
	PasswordField pas2;
	Label sub_text;
	public static ComboBox positions;
	public static List<Position> position = Positions.getPositions();
	
	public Add_User(){

		setCaption("Add New User");
		setModal(true);
		setResizable(false);
        addStyleName("edit-dashboard");              
        setContent(main = new VerticalLayout());
        setImmediate(true);
        
        positions = new ComboBox("Select Position");
        for (Position string : position) {
			positions.addItem(string);
		}
        
        main.addComponent(top = new HorizontalLayout(){
        	{
        		setSpacing(true);
        		setMargin(true);
        		addComponent(form = new FormLayout(){
        			{
        				addComponent(new Label(""));
        				tf = new TextField("Name");
        				addComponent(tf);
        				
        				tf2 = new TextField("Email");
        				addComponent(tf2);
        				
        				pas1 = new PasswordField("Password");
        				addComponent(pas1);
        				
        				pas2 = new PasswordField("Confirm Password");
        				addComponent(pas2);
        				
        				positions.setNullSelectionAllowed(false);
        				positions.setInputPrompt("No position select");
        				addComponent(positions);
        				addComponent(new HorizontalLayout(){
        					{
        						setCaption("Add new position");
        						setSizeUndefined();
        						//addComponent(new Label("Add new position"));
        						addComponent(new Button(){
        							{	
        								setSizeUndefined();
        								setStyleName(Reindeer.BUTTON_LINK);    								
        								setIcon(new ThemeResource("img/signs-first.png"));
        								addClickListener(new ClickListener() {
											
											@Override
											public void buttonClick(ClickEvent event) {
												final Window pos = new Window();
												
												pos.setCaption("Add New Position");
												pos.setModal(true);
												pos.setResizable(false);
												pos.addStyleName("edit-dashboard");  
												getUI().addWindow(pos);
												
												pos.setContent(new VerticalLayout(){
													{
														setWidth("280px");
														final TextField add_pos = new TextField("Position title: ");														
														final Label pos_error = new Label("");
														pos_error.addStyleName("position_error");
														addComponent(new FormLayout(){
															{							
																addComponent(new Label(""));
																addComponent(add_pos);
																addComponent(pos_error);															
															}
														});
														addComponent(new HorizontalLayout(){
															{
																setMargin(true);
												                setSpacing(true);
												                addStyleName("footer");
												                setWidth("100%");
												                Button cancel = new Button("Cancel");
												                cancel.addClickListener(new ClickListener() {
												                    @Override
												                    public void buttonClick(ClickEvent event) {
												                        pos.close();
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
																		boolean position_exist=false;
																		
																		if(!add_pos.getValue().isEmpty()){
																			for (Position position2 : position) {
																				if(position2.getTitle().toLowerCase().equals(add_pos.getValue().toLowerCase().trim())){
																					position_exist=true;
																				}
																			}
																			if(!isLetter(add_pos.getValue().trim())) pos_error.setValue("Unknown letter type");
																			else if(position_exist)  pos_error.setValue("This position already exist");																								
																			else{
																				Position new_pos = new Position();
																				new_pos.setTitle(capitalize(add_pos.getValue().trim()));
																				Positions.addPosition(new_pos);
																				updatePosition();
																				System.out.println("position created "+new_pos.getTitle());
																				pos.close();
																			}
																		}
																		else{
																			pos_error.setValue("Title is empty");
																		}
																	}
																});
															}
														});
													}
												});
											}
										});
        							}
        						});
        					}
        				});
        				addComponent(sub_text = new Label(){
							{
        						setStyleName("position_error");
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
                    	if( !(tf.getValue().isEmpty()) && validate(tf2.getValue())){
                    		if(pas1.getValue().equals(pas2.getValue())){
                    			  if(positions.getValue()!=null) {                 			
		                    			
		                    			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		    	    		   		 	session.beginTransaction();
		    	    		   		 	Student newstudent = new Student();
		                    			newstudent.setName(tf.getValue());
		                    			newstudent.setEmail(tf2.getValue());
		                    			newstudent.setPassword(pas1.getValue());
		                    			newstudent.setPosition((Position)positions.getValue());
		                    			session.save(newstudent);
		    	    		   		 	session.getTransaction().commit();
		                    			close();
		                    			
                    			  }
                    			  else{
                    				  sub_text.setValue("Please, select position");
                    			  }
                    		}
                    		else sub_text.setValue("Passwords are mismatch");
                    	}
                    	else{
                    		sub_text.setValue("Name or Email is incorrect");
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
	
	public static boolean validate(final String hex) {
		Pattern pattern;
		Matcher matcher;
		String EMAIL_PATTERN = 
				"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		
		pattern = Pattern.compile(EMAIL_PATTERN);
		
		matcher = pattern.matcher(hex);
		return matcher.matches();
 
	}

	private static boolean isLetter(String a){
		char[] chars = a.toLowerCase().toCharArray();
		for (char c : chars) {
			if(!Character.isLetter(c)) return false;
		}
		return true;
	}
	
	private static String capitalize(String line)
	{
	  return Character.toUpperCase(line.charAt(0)) + line.substring(1);
	}
	
	private static void updatePosition(){
		position = Positions.getPositions();       
        positions.removeAllItems();
        for (Position string : position) {
			positions.addItem(string);
		}
	}

}
