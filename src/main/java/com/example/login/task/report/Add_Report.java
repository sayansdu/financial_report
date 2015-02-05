package com.example.login.task.report;

import java.util.*;

import com.example.login.LoginUI;
import com.example.login.controller.Projects;
import com.example.login.entity.Product;
import com.example.login.entity.Project;
import com.example.login.entity.Report;
import com.example.login.entity.Student;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import org.hibernate.Session;

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
	private Student currentUser;
	private Set<Product> productList = new HashSet<Product>();
	private List<Report> reportList = new ArrayList<Report>();
	private ComboBox products;
    private DateField createDate;
	
	public Add_Report(){
		setCaption("Create Reports");
		setModal(true);
		setResizable(false);
        addStyleName("edit-dashboard");              
        setContent(main = new VerticalLayout());
        
		currentUser = LoginUI.current_user;
		projects = new ComboBox("Project List");
        projects.setImmediate(true);
        projects.setNullSelectionAllowed(false);

		if(currentUser.getPosition().getTitle().toLowerCase().equals("admin"))
			for (Project project : Projects.getProjects()) 
				projects.addItem(project);
		else
			for (Project project : currentUser.getProjects())
				projects.addItem(project);
		
		products = new ComboBox("Product List");
		products.setImmediate(true);
		products.setNullSelectionAllowed(false);
		
		projects.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				productList.clear();
                productList =((Project) projects.getValue()).getProducts();
				for (Product prod : productList) {
					products.addItem(prod);
				}
			}
		});
		products.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
                reportList.clear();
				reportList = new ArrayList<>(((Product) products.getValue()).getReport());
                Collections.sort(reportList, new Comparator<Report>() {
                    @Override
                    public int compare(Report o1, Report o2) {
                        return o1.getCreateDate().compareTo(o2.getCreateDate());
                    }
                });

                if(reportList.size() == 0){
                    if(new_report.getComponent(0) != createDate)
                        new_report.addComponent(createDate, 0);
                }
                else{
                    if(new_report.getComponent(0) == createDate)
                        new_report.removeComponent(createDate);
                }
				update();
			}
		});
		
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

                                if(reportList.size() == 0){
                                    createDate = new InlineDateField();
                                    createDate.setCaption("Select date:");
                                    createDate.setResolution(DateField.RESOLUTION_MONTH);
                                    addComponent(createDate);
                                }

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

												if(projects.getValue()!=null){
													if(products.getValue()!=null){
                                                        Session session = LoginUI.getCurrentSession();
                                                        if(session.getTransaction() == null){
                                                            session.beginTransaction();
                                                        }
                                                        else
                                                            session.getTransaction().begin();

                                                        Report report = new Report();
                                                        report.setProduct((Product) products.getValue());
                                                        report.setAmount(Integer.parseInt(amount.getValue()));
                                                        report.setSold_amount(Integer.parseInt(sold_amount.getValue()));
                                                        report.setCost_price(Integer.parseInt(cost_price.getValue()));
                                                        report.setPrice(Integer.parseInt(price.getValue()));

                                                        if(reportList.size() == 0)
                                                            report.setCreateDate(createDate.getValue());
                                                        else{
                                                            Date temp = reportList.get(reportList.size()-1).getCreateDate();
                                                            Calendar cal = Calendar.getInstance();
                                                            cal.setTime(temp);
                                                            cal.add(Calendar.MONTH, 1);
                                                            report.setCreateDate(cal.getTime());
                                                        }

                                                        session.save(report);
                                                        session.getTransaction().commit();

                                                        reportList.add(report);
                                                        if(new_report.getComponent(0) == createDate)
                                                            new_report.removeComponent(createDate);

                                                        update();
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
						close();
					}
				});
        	}
        });
		
	}
	
	private void update(){
		product_layout.removeAllComponents();
		for (Report report : reportList) {
			product_layout.addComponent(new Label(report.toString()));
		}
	}
}
