package com.example.login;

import java.text.DecimalFormat;
import java.util.*;

import com.example.login.entity.*;
import com.example.login.task.Add_Task;
import org.hibernate.Session;

import com.example.login.data.DataProvider;
import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.RowHeaderMode;



@SuppressWarnings("serial")
public class DashboardView extends VerticalLayout implements View{
	Table t;	
	Window notifications;
	Student current_user;
	Project current_project;
	Set<Task> tasks = new HashSet<Task>();
	
	public DashboardView() {
		current_user = LoginUI.getCurrentUser();
		current_project = LoginUI.getCurrentProject();
		for (Task task : current_user.getReceived()) {			
				if(task.isReaded()==false)
					tasks.add(task);			
		}
		
		DataProvider dataProvider = new DataProvider();
		dataProvider.loadMoviesData();		
		
		setSizeFull();
        addStyleName("dashboard-view");
        
        HorizontalLayout top = new HorizontalLayout();
        top.setWidth("100%");
        top.setSpacing(true);
        top.addStyleName("toolbar");
        addComponent(top);
        final Label title = new Label("Data");
        title.setSizeUndefined();
        title.addStyleName("h1");
        top.addComponent(title);
        top.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
        top.setExpandRatio(title, 1); 
        Button notify;
        if(!tasks.isEmpty())  { notify = new Button((tasks.size())+"");  notify.addStyleName("unread"); }
        else 				  notify = new Button("");
        notify.setDescription("Unread tasks");
        notify.addStyleName("borderless");
        notify.addStyleName("notifications");
        
        notify.addStyleName("icon-only");
        notify.addStyleName("icon-bell");
        notify.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				((LoginUI) getUI()).clearDashboardButtonBadge();
                event.getButton().removeStyleName("unread");
                event.getButton().setDescription("Notifications");
				
                if (notifications != null && notifications.getUI() != null)
                    notifications.close();
                else 
                {
                	buildNotifications(event);
                    getUI().addWindow(notifications);
                    notifications.focus();
                    ((CssLayout) getUI().getContent())
                            .addLayoutClickListener(new LayoutClickListener() {
                                @Override
                                public void layoutClick(LayoutClickEvent event) {
                                    notifications.close();
                                    ((CssLayout) getUI().getContent())
                                            .removeLayoutClickListener(this);
                                }
                            });
                }
			}
		});
        top.addComponent(notify);
        top.setComponentAlignment(notify, Alignment.MIDDLE_LEFT);
        
        Button edit = new Button();
        edit.addStyleName("icon-edit");
        edit.addStyleName("icon-only");
        top.addComponent(edit);
        edit.setDescription("Add Task");
        edit.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {


                if(current_project!=null){
                    Window add_task_window = new Add_Task(current_user, current_project);
                    getUI().addWindow(add_task_window);
                }

				/*final Window w = new Window("Edit Dashboard");
				w.setModal(true);
                w.setClosable(false);
                w.setResizable(false);
                w.addStyleName("edit-dashboard");

                getUI().addWindow(w);
                
                w.setContent(new VerticalLayout() {
                	TextField name = new TextField("Dashboard Name");
                    {
                        addComponent(new FormLayout() {
                            {
                                setSizeUndefined();
                                setMargin(true);
                                name.setValue(title.getValue());
                                addComponent(name);
                                name.focus();
                                name.selectAll();
                            }
                        });
                        addComponent(new HorizontalLayout() {
                        	{
                        		setMargin(true);
                                setSpacing(true);
                                addStyleName("footer");
                                setWidth("100%");

                                Button cancel = new Button("Cancel");
                                cancel.addClickListener(new ClickListener() {
                                    @Override
                                    public void buttonClick(ClickEvent event) {
                                        w.close();
                                    }
                                });
                                cancel.setClickShortcut(KeyCode.ESCAPE, null);
                                addComponent(cancel);
                                setExpandRatio(cancel, 1);
                                setComponentAlignment(cancel,
                                        Alignment.TOP_RIGHT);

                                Button ok = new Button("Save");
                                ok.addStyleName("wide");
                                ok.addStyleName("default");
                                ok.addClickListener(new ClickListener() {
                                    @Override
                                    public void buttonClick(ClickEvent event) {
                                        title.setValue(name.getValue());
                                        w.close();
                                    }
                                });
                                ok.setClickShortcut(KeyCode.ENTER, null);
                                addComponent(ok);
                        	}	
                        		
                        });
                    }
                    
                    
                });*/
				
			}
		});
        top.setComponentAlignment(edit, Alignment.MIDDLE_LEFT);
        
        // show results
        HorizontalLayout row = new HorizontalLayout();
        row.setSizeFull();
        row.setMargin(new MarginInfo(true, true, false, true));
        row.setSpacing(true);
        addComponent(row);
        setExpandRatio(row, 1.5f);
        
        row.addComponent(createPanel(new TopGrossingMoviesChart()));
        TextArea notes = new TextArea("Tasks");

        String value = "";
        if(current_user!=null && current_user.getReceived().size()>0){

            String task_str = "";
            for (Task task: current_user.getReceived()) {
                task_str = "";
                task_str += ("From: "+task.getStudent().getName()+"\n");
                task_str += (" Description: "+task.getText()+"\n");
                task_str += "\n";
                value += task_str;
            }
        }
        else{
            value = "You don't have any tasks";
        }
        notes.setValue(value);
        notes.setSizeFull();
        CssLayout panel = (CssLayout) createPanel(notes);
        panel.addStyleName("notes");
        row.addComponent(panel);
        
        row = new HorizontalLayout();
        row.setMargin(true);
        row.setSizeFull();
        row.setSpacing(true);
        addComponent(row);
        setExpandRatio(row, 2);
        
        t = getPreparedTable();
        
        row.addComponent(createPanel(t));
        row.addComponent(createPanel(new TopSixTheatersChart()));
        
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		
	}

	private void buildNotifications(ClickEvent event) {
        notifications = new Window("Notifications");
        VerticalLayout l = new VerticalLayout();
        l.setMargin(true);
        l.setSpacing(true);
        notifications.setContent(l);
        notifications.setWidth("300px");
        notifications.addStyleName("notifications");
        notifications.setClosable(false);
        notifications.setResizable(false);
        notifications.setDraggable(false);
        notifications.setPositionX(event.getClientX() - event.getRelativeX());
        notifications.setPositionY(event.getClientY() - event.getRelativeY());
        notifications.setCloseShortcut(KeyCode.ESCAPE, null);

        for (Task task : tasks) {
        	Label label = new Label(
                    "<hr><b>From: " + task.getStudent()+"</b><br>"
                     + "<span>Task: " + task.getText() +"</span><br>" , ContentMode.HTML);
        	l.addComponent(label);
        	task.setReaded(true);

            Session session = LoginUI.getCurrentSession();
            if(session.getTransaction() == null){
                session.beginTransaction();
            }
            else
                session.getTransaction().begin();

        	session.update(task);
        	session.getTransaction().commit();
		}

    }

	private Component createPanel(Component content) {
    	CssLayout panel = new CssLayout();
        panel.addStyleName("layout-panel");
        panel.setSizeFull();

        Button configure = new Button();
        configure.addStyleName("configure");
        configure.addStyleName("icon-cog");
        configure.addStyleName("icon-only");
        configure.addStyleName("borderless");
        configure.setDescription("Configure");
        configure.addStyleName("small");
        configure.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                Notification.show("Not implemented in this project");
            }
        });
        panel.addComponent(configure);

        panel.addComponent(content);
        return panel;
	}

    private Table getPreparedTable(){
        Table temp = new Table() {
            @Override
            protected String formatPropertyValue(Object rowId, Object colId,
                                                 Property<?> property) {
                if (colId.equals("Revenue")) {
                    if (property != null && property.getValue() != null) {
                        Double r = (Double) property.getValue();
                        String ret = new DecimalFormat("#.##").format(r);
                        return "$" + ret;
                    } else {
                        return "";
                    }
                }
                return super.formatPropertyValue(rowId, colId, property);
            }
        };
        temp.setCaption("Top products by average revenue");

        temp.setWidth("100%");
        temp.setPageLength(0);
        temp.addStyleName("plain");
        temp.addStyleName("borderless");
        temp.setSortEnabled(false);
        temp.setColumnAlignment("Revenue", Align.RIGHT);
        temp.setRowHeaderMode(RowHeaderMode.INDEX);

        Project currentProject = LoginUI.getCurrentProject();
        Set<Product> products = currentProject.getProducts();

        if(currentProject != null && (products!=null && products.size()>0)){
            temp.addContainerProperty("Product", String.class, null);
            temp.addContainerProperty("Average Revenue (tg)",  Integer.class, null);
            List<Product> productList = new ArrayList<Product>(products);
            int length = (productList.size()>8 ? 8: productList.size());

            for (int i=0; i<length; i++) {
                Set<Report> reports = productList.get(i).getReport();
                int score = 0;
                for (Report report : reports) {
                    score += report.getSold_amount() * (report.getPrice()-report.getCost_price());
                }

                temp.addItem(new Object[]{productList.get(i).getName(), score/reports.size()}, i+1);
            }


        }

        return temp;
    }
}
