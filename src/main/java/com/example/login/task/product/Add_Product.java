package com.example.login.task.product;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;

import com.example.login.LoginUI;
import com.example.login.controller.Projects;
import com.example.login.entity.Product;
import com.example.login.entity.Project;
import com.example.login.entity.Student;
import com.example.login.util.HibernateUtil;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class Add_Product extends Window{
	VerticalLayout main;
	VerticalLayout product_layout;
	HorizontalLayout top;
	HorizontalLayout bottom;
	FormLayout form;
	
	Label sub_text;
	TextField name;
	
	private ComboBox projects;
	private Student current_user;
	private Set<Product> product_list = new HashSet<Product>();
	
	public Add_Product(){
		setCaption("Create Products");
		setModal(true);
		setResizable(false);
        addStyleName("edit-dashboard");              
        setContent(main = new VerticalLayout());
        
		current_user = LoginUI.getCurrentUser();
		projects = new ComboBox("Project List");
        projects.setImmediate(true);
        projects.setNullSelectionAllowed(false);
        
		if(current_user.getPosition().getTitle().toLowerCase().equals("admin"))
			for (Project project : Projects.getProjects()) 
				projects.addItem(project);
		else
			for (Project project : current_user.getProjects()) 
				projects.addItem(project);
		
		projects.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				product_list =((Project) projects.getValue()).getProducts();
				update();
			}
		});
		
		main.addComponent(top = new HorizontalLayout(){
        	{
        		setMargin(true);
        		setSpacing(true);
        		setWidth("390px");
        		addComponent(form = new FormLayout(){
        			{
        				setSpacing(true);
        				addComponent(projects);
        				
        				product_layout = new VerticalLayout();
        				product_layout.setCaption("Exist Products");
        				addComponent(product_layout);
        				
        				addComponent(new HorizontalLayout(){
        					{
        						setCaption("New Product");
        						setSpacing(true);
        						name = new TextField();
        						addComponent(name);
        						addComponent(new Button(){
        							{
        								setCaption("Add");
        								addClickListener(new ClickListener() {
											
											@Override
											public void buttonClick(ClickEvent event) {
												// TODO Auto-generated method stub
												if(projects.getValue()!=null){
													if(!name.getValue().isEmpty()){
                                                        Session session = LoginUI.getCurrentSession();
                                                        if(session.getTransaction() == null){
                                                            session.beginTransaction();
                                                        }
                                                        else
                                                            session.getTransaction().begin();
                                                        Product product = new Product();
                                                        product.setName(name.getValue());
                                                        product.setProject((Project)projects.getValue());
                                                        session.save(product);

                                                        product_list.add(product);
                                                        update();
														session.getTransaction().commit();
													}
													else	sub_text.setValue("product name is empty");
												}
												else sub_text.setValue("please, select the project");
											}
										});
        							}
        						});
        					}
        				});
        				
        				sub_text = new Label();
        				sub_text.setStyleName("position_error");
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
		product_layout.removeAllComponents();
		for (Product prod : product_list) {
			product_layout.addComponent(new Label(prod.getName()));
		}
	}
	
}
