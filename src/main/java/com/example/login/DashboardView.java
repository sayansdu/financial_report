package com.example.login;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;

import com.example.login.TopSixTheatersChart;
import com.example.login.TopGrossingMoviesChart;
import com.example.login.data.DataProvider;
import com.example.login.entity.Project;
import com.example.login.entity.Student;
import com.example.login.entity.Task;
import com.example.login.util.HibernateUtil;
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
		current_user = LoginUI.current_user;
		current_project = LoginUI.current_project;
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
        final Label title = new Label("Analytics");
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
        edit.setDescription("Edit Dashboard");
        edit.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				final Window w = new Window("Edit Dashboard");
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
                    
                    
                });
				
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
        TextArea notes = new TextArea("Notes");
        notes.setValue("Remember to:\n Add graph to SalesView \n Connect to database\n Create a new data\n Change the style");
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
        
        t = new Table() {
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
        t.setCaption("Top 10 sales of month");

        t.setWidth("100%");
        t.setPageLength(0);
        t.addStyleName("plain");
        t.addStyleName("borderless");
        t.setSortEnabled(false);
        t.setColumnAlignment("Revenue", Align.RIGHT);
        t.setRowHeaderMode(RowHeaderMode.INDEX);
        
        row.addComponent(createPanel(t));
        row.addComponent(createPanel(new TopSixTheatersChart()));
        
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stubs
		
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
        	Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        	session.beginTransaction();
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
                Notification.show("Not implemented in this demo");
            }
        });
        panel.addComponent(configure);

        panel.addComponent(content);
        return panel;
	}
	
}
