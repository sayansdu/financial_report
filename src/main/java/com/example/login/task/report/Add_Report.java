package com.example.login.task.report;

import java.util.HashSet;
import java.util.Set;

import com.example.login.LoginUI;
import com.example.login.controller.Months;
import com.example.login.controller.Projects;
import com.example.login.entity.Month;
import com.example.login.entity.Product;
import com.example.login.entity.Project;
import com.example.login.entity.Report;
import com.example.login.entity.Student;
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
public class Add_Report extends Window{
	VerticalLayout main;
	VerticalLayout product_layout;
	HorizontalLayout top;
	HorizontalLayout bottom;
	HorizontalLayout new_report;
	FormLayout form;
	
	Label sub_text;
	TextField amount;
	TextField sold_amount;
	TextField price;
	TextField cost_price;
	Button add;
	
	private ComboBox projects;
	private Student current_user;
	private Set<Product> product_list = new HashSet<Product>();
	private Set<Report> report_list = new HashSet<Report>();
	private ComboBox products;
	private ComboBox years;
	private ComboBox months;
	
	public Add_Report(){
		setCaption("Create Products");
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
		
		products = new ComboBox("Product List");
		products.setImmediate(true);
		products.setNullSelectionAllowed(false);
		
		projects.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				product_list.clear();
				product_list =((Project) projects.getValue()).getProducts();
				for (Product prod : product_list) {
					products.addItem(prod);
				}
			}
		});
		products.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				report_list = ( (Product) products.getValue()).getReport();
				update();
			}
		});
		
		
		years = new ComboBox("Year:");
		years.setImmediate(true);
		years.setNullSelectionAllowed(false);
		years.setWidth("60px");
		for(int i=0; i<11; i++){
			years.addItem("201"+i);
		}
		
		months = new ComboBox("Month:");
		months.setImmediate(true);
		months.setNullSelectionAllowed(false);
		months.setWidth("100px");
		for (Month month : Months.getMonths()) {
			months.addItem(month);
		}
		
		main.addComponent(top = new HorizontalLayout(){
        	{
        		setMargin(true);
        		setSpacing(true);
        		setWidth("800px");
        		addComponent(form = new FormLayout(){
        			{
        				setSpacing(true);
        				addComponent(projects);
        				addComponent(products);
        				product_layout = new VerticalLayout();
        				product_layout.setCaption("Report List");
        				product_layout.setSpacing(false);
        				addComponent(product_layout);
        				
        				addComponent(new_report = new HorizontalLayout(){
        					{
        						setCaption("New Report");
        						setSpacing(true);
        						setMargin(true);
        						addComponent(years);
        						addComponent(months);
        						
        						amount = new TextField("Amount:");
        						amount.setColumns(5);
        						addComponent(amount);
        						
        						sold_amount = new TextField("Sold amou:");
        						sold_amount.setColumns(5);
        						addComponent(sold_amount);
        						
        						cost_price = new TextField("Cost price:");
        						cost_price.setColumns(5);
        						addComponent(cost_price);
        						
        						price = new TextField("Price:");
        						price.setColumns(5);
        						addComponent(price);
        						
        						addComponent(add = new Button(){
        							{
        								setCaption("Add");
        								addClickListener(new ClickListener() {
											
											@Override
											public void buttonClick(ClickEvent event) {
												// TODO Auto-generated method stub
												if(projects.getValue()!=null){
													if(products.getValue()!=null){
														
													}
													else sub_text.setValue("please, select the product");
												}
												else	sub_text.setValue("please, select the project");
											}
										});
        							}
        						});
        						setComponentAlignment(add, Alignment.BOTTOM_RIGHT);
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
		for (Report report : report_list) {
			product_layout.addComponent(new Label(report.toString()));
		}
	}
	
	private void check_report(){
		
	}
}
