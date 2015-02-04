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
import com.vaadin.server.ThemeResource;
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
public class Delete_Product extends Window{
	VerticalLayout main;
	VerticalLayout top;
	VerticalLayout products_layout;
	HorizontalLayout product_layout;
	HorizontalLayout bottom;
	ComboBox projects;
	
	private Student current_user;
	private static  Project current_project;
	private Set<Project> projects2;
	private Set<Product> products = new HashSet<Product>();
	
	public Delete_Product(){
		current_user = LoginUI.current_user;
		if(current_user.getPosition().getTitle().toLowerCase().equals("admin"))
			projects2 = new HashSet<Project>(Projects.getProjects());
		else
			projects2 = current_user.getProjects();
		
		setCaption("Delete Product");
		setModal(true);
		setResizable(false);
        addStyleName("edit-dashboard");              
        setContent(main = new VerticalLayout());
        
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
				products = current_project.getProducts();
				update();
			}
		});
        
        main.addComponent(top = new VerticalLayout(){
        	{
        		setWidth("300px");
        		setMargin(true);
        		setSpacing(true);
        		addComponent(new FormLayout(){
        			{
        				setSpacing(true);
        				addComponent(projects);
        				
        				products_layout = new VerticalLayout();
        				addComponent(products_layout);
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
		products_layout.removeAllComponents();
		for (final Product product : products ) {
			products_layout.addComponent(new HorizontalLayout(){
				{
					setSpacing(true);
					addComponent(new Label(product.toString()));
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

									session.delete(product);
									session.getTransaction().commit();
									products.remove(product);
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
