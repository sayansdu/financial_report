package com.example.login.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.hibernate.Session;

import com.example.login.entity.Student;
import com.example.login.util.HibernateUtil;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FileResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class Profile extends Window {
	
	VerticalLayout main;
	HorizontalLayout top;
	HorizontalLayout bottom;
	FormLayout form;
	TextField tf;
	TextField tf2;
	TextField tf3;
	Label sub_text;
	Button remove;
	
	public Image image;
	final Student current_user2;
	
	public Profile(Student username){
		setCaption("User Profile");
		setModal(true);
		setResizable(false);
        addStyleName("edit-dashboard");              
        setContent(main = new VerticalLayout());
        current_user2 = username;
        
        main.addComponent(top = new HorizontalLayout(){
        	{
        		setSpacing(true);
        		setMargin(true);
        		addComponent(form = new FormLayout(){
        			{
        				tf = new TextField("Name");
        				tf.setValue(current_user2.getName());
        				addComponent(tf);
        				
        				tf2 = new TextField("Email");
        				tf2.setValue(current_user2.getEmail());
        				addComponent(tf2);
        				
        				tf3 = new TextField("Password");
        				addComponent(tf3);
        				sub_text = new Label("enter password to edit profile");
        				sub_text.addStyleName("profile_sub");
        				addComponent(sub_text);
        			}
        		});
        		addComponent(new VerticalLayout(){
        			{
        				setMargin(true);
        				setSpacing(true);
        				Upload upload = new Upload();
        				upload.setImmediate(true);
        				upload.setButtonCaption("Start Upload");
        				addComponent(upload);
        				addComponent(new HorizontalLayout(){
        					{
        						addComponent(new Label("Now, this is your logo &nbsp;", ContentMode.HTML));
        						addComponent(remove = new Button(){
        							{
        								
        								//setStyleName("nobackground");
        								setStyleName(Reindeer.BUTTON_LINK);
        								setIcon(new ThemeResource("img/delete-icon2.png"));
        								
        								addClickListener(new ClickListener() {
											
											@Override
											public void buttonClick(ClickEvent event) {
												// TODO Auto-generated method stub
												Session session = HibernateUtil.getSessionFactory().getCurrentSession();
									   		 	session.beginTransaction();
									   		 	current_user2.setLogo(null);
									   		 	session.update(current_user2);
									   		 	session.getTransaction().commit();	
												image.setSource((new FileResource(new File("C:\\tmp\\uploaded\\default-logo.png"))));											
											}
										});
        							}
        						});
        					}
        				});
        				image = new Image();
        				image.setVisible(true);        				
        				image.setWidth("150px");
        				image.setHeight("150px");
        				addComponent(image);
        				if(current_user2.getLogo()==null) image.setSource(new FileResource(new File("C:\\tmp\\uploaded\\default-logo.png")));
        				else 							 image.setSource(new FileResource(new File(current_user2.getLogo())));
        				
        				final ImageUploader uploader = new ImageUploader(); 
        				upload.setReceiver(uploader);
        				upload.addListener(uploader);
        				
        				
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
                    	if(tf3.getValue().equals(current_user2.getPassword())){
	                    	Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	    		   		 	session.beginTransaction();
	    		   		 	current_user2.setName(tf.getValue());
	    		   		 	current_user2.setEmail(tf2.getValue());
	    		   		 	session.update(current_user2);
	    		   		 	session.getTransaction().commit();
	                        close();
                    	}
                    	else{
                    		sub_text.setValue("you password is incorrect");
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
	
	class ImageUploader implements Receiver, SucceededListener {
		public File file;
		
		@Override
		public void uploadSucceeded(SucceededEvent event) {
			// TODO Auto-generated method stub
			 image.setVisible(true);
			 image.setSource(new FileResource(file));
		}

		@Override
		public OutputStream receiveUpload(String filename, String mimeType) {
			// TODO Auto-generated method stub
			String ext = filename.substring(filename.indexOf("."));
			String file_name = "C:\\tmp\\uploaded\\" +current_user2.getName()+"-"+"logo"+ext;
			
			FileOutputStream fos = null; 
			 try {
		            // Open the file for writing.
		            file = new File(file_name);
		            fos = new FileOutputStream(file);
		            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		   		 	session.beginTransaction();
		   		 	current_user2.setLogo(file_name);
		   		 	session.update(current_user2);
		   		 	session.getTransaction().commit();	
		        }
			 catch(FileNotFoundException e){
				 Notification.show(
		                    "Could not open file<br/>", e.getMessage(),
		                    Notification.TYPE_ERROR_MESSAGE);
				 return null;
			 }
			 return fos;			
		}
		
	}
}
