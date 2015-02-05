package com.example.login.task.report;

import java.util.HashSet;
import java.util.Set;

import com.example.login.LoginUI;
import com.example.login.controller.Projects;
import com.example.login.entity.Product;
import com.example.login.entity.Project;
import com.example.login.entity.Report;
import com.example.login.entity.Student;
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
import org.hibernate.Session;

@SuppressWarnings("serial")
public class DeleteReport extends Window{

    VerticalLayout main;
    VerticalLayout productLayout;
    HorizontalLayout top;
    HorizontalLayout bottom;
    FormLayout form;

    private ComboBox projects;
    private Student currentUser;
    private Set<Product> productList = new HashSet<Product>();
    private Set<Report> reportList = new HashSet<Report>();
    private ComboBox products;

    public DeleteReport(){

        setCaption("Delete Reports");
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

                reportList = ( (Product) products.getValue()).getReport();
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
                        productLayout = new VerticalLayout();
                        productLayout.setCaption("Report List");
                        productLayout.setSpacing(false);
                        addComponent(productLayout);
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
                    public void buttonClick(ClickEvent event)
                    {
                        close();
                    }
                });
            }
        });

    }

    private void update(){
        productLayout.removeAllComponents();

        for (final Report report : reportList) {
//            productLayout.addComponent(new Label(report.toString()));
            productLayout.addComponent(new HorizontalLayout(){
                {
                    setSpacing(true);
                    addComponent(new Label(report.toString()));
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

                                reportList.remove(report);
                                session.delete(report);
                                session.getTransaction().commit();

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
