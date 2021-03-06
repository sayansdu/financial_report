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
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
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
public class Edit_Product extends Window{
	VerticalLayout main;
	VerticalLayout product_layout;
	HorizontalLayout top;
	HorizontalLayout bottom;
	FormLayout form;
	
	Label sub_text;
	TextField name;
	
	private ComboBox projects;
	private Student current_user;
	private Product current_product;
	private Set<Product> product_list = new HashSet<Product>();

	public Edit_Product(){
		setCaption("Update Products");
		setModal(true);
		setResizable(false);
	    addStyleName("edit-dashboard");              
	    setContent(main = new VerticalLayout());
    
		current_user = LoginUI.current_user;
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

				product_list =((Project) projects.getValue()).getProducts();
				update();
			}
		});
		
		main.addComponent(top = new HorizontalLayout(){
        	{
        		setMargin(true);
        		setSpacing(true);
        		setWidth("410px");
        		addComponent(form = new FormLayout(){
        			{
        				setSpacing(true);
        				addComponent(projects);
        				
        				product_layout = new VerticalLayout();
        				product_layout.setCaption("All Products");
        				addComponent(product_layout);
        				
        				addComponent(new HorizontalLayout(){
        					{
        						setCaption("Selected Product");
        						setSpacing(true);
        						name = new TextField();
        						addComponent(name);
        						addComponent(new Button(){
        							{
        								setCaption("Update");
        								addClickListener(new ClickListener() {
											
											@Override
											public void buttonClick(ClickEvent event) {

												String product_name = name.getValue();
                                                Session session = LoginUI.getCurrentSession();
                                                if(session.getTransaction() == null){
                                                    session.beginTransaction();
                                                }
                                                else
                                                    session.getTransaction().begin();

                                                current_product.setName(product_name);
                                                session.update(current_product);
												session.getTransaction().commit();
												product_list =((Project) projects.getValue()).getProducts();
												update(); 
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
						close();
					}
				});
        	}
        });
		
	}
	
	private void update(){
		product_layout.removeAllComponents();
		for (final Product prod : product_list) {
			product_layout.addComponent(new HorizontalLayout(){
				{
					addComponent(new Label(prod.getName()));
					addLayoutClickListener(new LayoutClickListener() {
						
						@Override
						public void layoutClick(LayoutClickEvent event) {

							current_product = prod;
							name.setValue(current_product.getName());
						}
					});
				}
			});
		}
	}
	
}
