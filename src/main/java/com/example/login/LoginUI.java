package com.example.login;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.example.login.HelpManager;
import com.example.login.HelpOverlay;
import com.example.login.controller.Users;
import com.example.login.entity.Project;
import com.example.login.entity.Student;
import com.example.login.task.Add_Project;
import com.example.login.task.Add_Task;
import com.example.login.task.Add_To_Project;
import com.example.login.task.Add_User;
import com.example.login.task.All_Task;
import com.example.login.task.Current_Project;
import com.example.login.task.Current_Task;
import com.example.login.task.Delete_Project;
import com.example.login.task.Delete_Task;
import com.example.login.task.Password;
import com.example.login.task.Profile;
import com.example.login.task.Setting_Project;
import com.example.login.task.product.Add_Product;
import com.example.login.task.product.Delete_Product;
import com.example.login.task.product.Edit_Product;
import com.example.login.task.report.Add_Report;
import com.example.login.util.HibernateUtil;
import com.vaadin.annotations.Theme;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.Transferable;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.AbstractSelect.AcceptItem;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.DragAndDropWrapper.DragStartMode;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

/**
 * Main UI class
 */
@Theme("dashboard")
@SuppressWarnings("serial")
public class LoginUI extends UI {
	Panel container;
	VerticalLayout loginLayout;
    CssLayout menu = new CssLayout();
	CssLayout loginPanel = new CssLayout();	
	CssLayout root = new CssLayout();
	CssLayout content = new CssLayout();
	
	
	private Transferable items;
	private Navigator nav;
	public String user_name = "";
	public static Student current_user = null;
	public static Project current_project = null;
	boolean is_user = false;
	boolean autoCreateReport = false;
	
			;
	HashMap<String, Button> viewNameToMenuButton = new HashMap<String, Button>();
	HelpManager helpManager;
	HashMap<String, Class<? extends View>> routes = new HashMap<String, Class<? extends View>>() {
        {
            put("/dashboard", DashboardView.class);
            put("/sales", SalesView.class);
            put("/transactions", TransactionsView.class);
        }
    };
    Session session;
    
	@Override
	protected void init(VaadinRequest request) {
		setContent(root);
        root.addStyleName("root");
        root.setSizeFull();
        helpManager = new HelpManager(this);
        Label bg = new Label();
        bg.setSizeUndefined();
        bg.addStyleName("login-bg");
        root.addComponent(bg);
        
        buildLoginView(false);
	}

	private void buildLoginView(boolean exit){
		if (exit) {  root.removeAllComponents();  }
    	helpManager.closeAll();
        HelpOverlay w = helpManager
                .addOverlay(
                        "Welcome to the Financial Report",
                        "<p>To sign in, you can just enter admin/admin</p>",
                        "login");
        w.center();
        addWindow(w);
        addStyleName("login");
    	loginLayout = new VerticalLayout();
        loginLayout.setSizeFull();
        loginLayout.addStyleName("login-layout");
        root.addComponent(loginLayout);
    	
        final CssLayout loginPanel = new CssLayout();
        loginPanel.addStyleName("login-panel");
        
        HorizontalLayout labels = new HorizontalLayout();
        labels.setWidth("100%");
        labels.setMargin(true);
        labels.addStyleName("labels");
        loginPanel.addComponent(labels);
        
        Label welcome = new Label("Welcome");
        welcome.setSizeUndefined();
        welcome.addStyleName("h4");
        labels.addComponent(welcome);
        labels.setComponentAlignment(welcome, Alignment.MIDDLE_LEFT);
        
        Label title = new Label("Analysis");
        title.setSizeUndefined();
        title.addStyleName("h2");
        title.addStyleName("light");
        labels.addComponent(title);
        labels.setComponentAlignment(title, Alignment.MIDDLE_RIGHT);
        
        HorizontalLayout fields = new HorizontalLayout();
        fields.setSpacing(true);
        fields.setMargin(true);
        fields.addStyleName("fields");
        
        final TextField username = new TextField("Username");
        
        username.focus();
        fields.addComponent(username);

        final PasswordField password = new PasswordField("Password");
       
        fields.addComponent(password);
        
        final Button signin = new Button("Sign In");
        signin.addStyleName("default");
        fields.addComponent(signin);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);
        
        final ShortcutListener enter = new ShortcutListener("Sign In",
                KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                signin.click();
            }
        };
        
        signin.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {		
				String user_name = username.getValue();
				String user_password = password.getValue();
				try{				
					session = HibernateUtil.getSessionFactory().openSession();
					Users user = new Users();
					is_user = user.is_it_user(user_name, user_password);					
				}
				catch(HibernateException e){
					e.printStackTrace();					
				}
				
				if (is_user) {
					user_name = username.getValue();
					current_user = Users.get_user_by_name(user_name);
                    signin.removeShortcutListener(enter);
                    checkForProject();
                }
				else{
					if(loginPanel.getComponentCount() > 2)
					{
						loginPanel.removeComponent(loginPanel.getComponent(2));
					}
					Label error = new Label(
                            "Wrong username or password. Try again.",
                            ContentMode.HTML);
                    error.addStyleName("error");
                    error.setSizeUndefined();
                    error.addStyleName("light");
                    // Add animation
                    error.addStyleName("v-animate-reveal");
                    loginPanel.addComponent(error);
                    username.focus();
				}
			}
		});
        
        signin.addShortcutListener(enter);
        loginPanel.addComponent(fields);
        loginLayout.addComponent(loginPanel);
        loginLayout.setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
	}
	
	private void checkForProject(){
		ArrayList<Project> projects = new ArrayList<Project>(current_user.getProjects());
		if(projects.isEmpty() && !(current_user.getPosition().getTitle().toLowerCase().equals("admin"))){
			Window win = new Add_To_Project(current_user);
			getUI().addWindow(win);
			win.addCloseListener(new CloseListener(){
				@Override
				public void windowClose(CloseEvent e) {
					buildMainView();
				}				
			});
		}
		else{
			buildMainView();
		}
	}
	
	private void buildMainView()
	 {
		current_project = current_user.getCurrent_project();
		   
		
		nav = new Navigator(this, content);
        for (String route : routes.keySet()) {
            nav.addView(route, routes.get(route));
        }
        helpManager.closeAll();
        removeStyleName("login");
        root.removeComponent(loginLayout);
        
        root.addComponent(new HorizontalLayout(){
        	{
        		setSizeFull();
                addStyleName("main-view");
                addComponent(new VerticalLayout(){
                	{
                		addStyleName("sidebar");
                        setWidth(null);
                        setHeight("100%");
                        
                     // Branding element Left Menu part 
                        addComponent(new CssLayout() {
                            {
                            	addStyleName("branding");
                            	Label logo;
                            	if(current_user.getPosition().getTitle().toLowerCase().equals("admin")){
                            		logo = new Label(
                                            "<span>Financial</span>Analysis",
                                            ContentMode.HTML);
                            	}
                            	else
                            		logo = new Label(
                                        "<span>project: </span>"+current_project.getName(),
                                        ContentMode.HTML);
                                logo.setSizeUndefined();
                                addComponent(logo);
                                // addComponent(new Image(null, new
                                // ThemeResource(
                                // "img/branding.png")));
                            }
                        });
                        
                        addComponent(menu);
                        setExpandRatio(menu, 1);
                        
                        addComponent(new VerticalLayout() {
                            {
                            	 final Image profilePic;
                            	 setSizeUndefined();
                                 addStyleName("user");
                                 if(current_user.getLogo()==null)  profilePic = new Image(null,  new ThemeResource("img/profile-pic.png"));
                                 else  								   {profilePic = new Image();  profilePic.setSource(new FileResource(new File(current_user.getLogo()))); }
                                 
                                 profilePic.setWidth("34px");
                                 profilePic.setHeight("35px");
                                 addComponent(profilePic);
                                 
                                 Label userName = new Label(current_user.getName());
                                 userName.setSizeUndefined();
                                 addComponent(userName);
                                 setComponentAlignment(userName, Alignment.MIDDLE_CENTER);

                                 Command account_cmd = new Command() {
                                     @Override
                                     public void menuSelected( MenuItem selectedItem) {
                                         Window preference = new Profile(current_user);
                                         addWindow(preference);
                                         refresh_user(preference);
                                     }
                                 };
                                 Command password_cmd = new Command() {
                                     @Override
                                     public void menuSelected( MenuItem selectedItem) {
                                         Window passwoord = new Password(current_user);
                                         addWindow(passwoord);
                                         refresh_user(passwoord);
                                     }
                                 };
                                 Command add_user_cmd = new Command() {
                                     @Override
                                     public void menuSelected( MenuItem selectedItem) {
                                         Window add_user_window = new Add_User();
                                         addWindow(add_user_window);
                                         refresh_project(add_user_window);
                                     }
                                 };                                 
                                 Command add_project_cmd = new Command() {
                                     @Override
                                     public void menuSelected( MenuItem selectedItem) {
                                         Window project_window = new Add_Project(current_user);
                                         addWindow(project_window);
                                         refresh_project(project_window);
                                         refresh_user(project_window);
                                     }
                                 };                                   
                                 Command current_project_cmd = new Command() {
                                     @Override
                                     public void menuSelected( MenuItem selectedItem) {
                                         Window current_project_window = new Current_Project(current_project, current_user);
                                         if(current_project!=null)  addWindow(current_project_window);
                                         else Notification.show("Project is not created");
                                         
                                     }
                                 }; 
                                 Command delete_project_cmd = new Command() {
                                     @Override
                                     public void menuSelected( MenuItem selectedItem) {
                                         Window delete_project_window = new Delete_Project();
	                                         addWindow(delete_project_window);
	                                         refresh_project(delete_project_window);
	                                         refresh_user(delete_project_window);
                                         
                                     }
                                 }; 
                                 Command setting_project_cmd = new Command() {
                                     @Override
                                     public void menuSelected( MenuItem selectedItem) {
                                         Window setting_project_window = new Setting_Project(current_user);
	                                         addWindow(setting_project_window);
	                                         refresh_project(setting_project_window);
                                         
                                     }
                                 }; 
                                 Command add_task_cmd = new Command() {
                                     @Override
                                     public void menuSelected( MenuItem selectedItem) {
                                         Window add_task_window = new Add_Task(current_user, current_project);
                                         if(current_project!=null){
	                                         addWindow(add_task_window);
	                                         refresh_user(add_task_window);
                                         }
                                         else Notification.show("Project is not created");
                                         
                                     }
                                 }; 
                                 Command delete_task_cmd = new Command() {
                                     @Override
                                     public void menuSelected( MenuItem selectedItem) {
                                         Window delete_task_window = new Delete_Task(current_user, current_project);
                                         if(current_project!=null){
	                                         addWindow(delete_task_window);
	                                         refresh_user(delete_task_window);
                                         }
                                         else Notification.show("Project is not created");
                                     }
                                 }; 
                                 Command current_task_cmd = new Command() {
                                     @Override
                                     public void menuSelected( MenuItem selectedItem) {
                                         Window current_task_window = new Current_Task(current_user);
                                         addWindow(current_task_window);
                                     }
                                 }; 
                                 Command all_task_cmd = new Command() {
                                     @Override
                                     public void menuSelected( MenuItem selectedItem) {
                                    	 if(current_project!=null){
	                                         Window all_task_window = new All_Task();
	                                         addWindow(all_task_window);
                                    	 }
                                    	 else Notification.show("Project is not created");
                                     }
                                 }; 
                                 Command add_product_cmd = new Command() {
                                     @Override
                                     public void menuSelected( MenuItem selectedItem) {
	                                         Window add_product_window = new Add_Product();
	                                         addWindow(add_product_window);
                                     }
                                 }; 
                                 Command delete_product_cmd = new Command() {
                                     @Override
                                     public void menuSelected( MenuItem selectedItem) {
	                                         Window delete_product_window = new Delete_Product();
	                                         addWindow(delete_product_window);
                                     }
                                 }; 
                                 Command update_product_cmd = new Command() {
                                     @Override
                                     public void menuSelected( MenuItem selectedItem) {
	                                         Window update_product_window = new Edit_Product();
	                                         addWindow(update_product_window);
                                     }
                                 };
                                 Command add_report_cmd = new Command() {
                                     @Override
                                     public void menuSelected( MenuItem selectedItem) {
	                                         Window add_report_window = new Add_Report();
	                                         addWindow(add_report_window);
                                     }
                                 };
                                 
                                 MenuBar settings = new MenuBar();
                                 MenuItem settingsMenu = settings.addItem("", null);
                                 
                                 if((current_user.getPosition().getTitle().toLowerCase().equals("admin"))){
	                                 MenuItem projectMenu = settingsMenu.addItem("Project", current_project_cmd);
	                                 
	                                 projectMenu.addItem("New Project", add_project_cmd);
	                                 projectMenu.addItem("Current Project", current_project_cmd);
	                                 projectMenu.addItem("Project Settings", setting_project_cmd);
	                                 projectMenu.addItem("Delete Project", delete_project_cmd);
	                                 
	                                 MenuItem productMenu = projectMenu.addItem("Product", update_product_cmd);
	                                 productMenu.addItem("Create Product", add_product_cmd);
	                                 productMenu.addItem("Update Product", update_product_cmd);
	                                 productMenu.addItem("Delete Product", delete_product_cmd); 
	                                 
	                                 MenuItem reportMenu = productMenu.addItem("Report", null);
	                                 reportMenu.addItem("Create Report", add_report_cmd);
	                                 reportMenu.addItem("Update Report", null);
	                                 reportMenu.addItem("Delete Report", null);
	                                 
	                                 MenuItem tasktMenu = settingsMenu.addItem("Task", all_task_cmd);
	                                 tasktMenu.addItem("Create Task", add_task_cmd);
	                                 tasktMenu.addItem("Tasks List", current_task_cmd);
	                                 tasktMenu.addItem("All Tasks List", all_task_cmd);
	                                 tasktMenu.addItem("Delete Task", delete_task_cmd);   
	                                 
	                                 settingsMenu.addSeparator();
	                                 settingsMenu.setStyleName("icon-cog");
	                                 MenuItem accountMenu = settingsMenu.addItem("My Account", account_cmd);
	                                 accountMenu.addItem("Settings", account_cmd);
	                                 accountMenu.addItem("Add User",add_user_cmd );
	                                 accountMenu.addItem("Password",password_cmd);
	                                	                                 
                                 }
                                 else{
                                	 MenuItem projectMenu = settingsMenu.addItem("Project", current_project_cmd);
	                                 
	                                 MenuItem tasktMenu = settingsMenu.addItem("Task", current_task_cmd);
	                                 tasktMenu.addItem("Create Task", add_task_cmd);
	                                 tasktMenu.addItem("Tasks List", current_task_cmd);
	                                 tasktMenu.addItem("Delete Task", delete_task_cmd);                                
	                                 
	                                 MenuItem productMenu = projectMenu.addItem("Product", update_product_cmd);
	                                 productMenu.addItem("Create Product", add_product_cmd);
	                                 productMenu.addItem("Update Product", update_product_cmd);
	                                 productMenu.addItem("Delete Product", delete_product_cmd);  
	                                 
	                                 MenuItem reportMenu = productMenu.addItem("Report", null);
	                                 reportMenu.addItem("Create Report", add_report_cmd);
	                                 reportMenu.addItem("Update Report", null);
	                                 reportMenu.addItem("Delete Report", null);
	                                 
	                                 settingsMenu.setStyleName("icon-cog");
	                                 settingsMenu.addSeparator();
	                                 MenuItem accountMenu = settingsMenu.addItem("My Account", account_cmd);
	                                 accountMenu.addItem("Settings", account_cmd);
	                                 accountMenu.addItem("Password",password_cmd);	                                 
                                 }
                                 addComponent(settings);                                
                                 Button exit = new NativeButton("Exit");
                                 exit.addStyleName("icon-cancel");
                                 exit.setDescription("Sign Out");
                                 addComponent(exit);
                                 exit.addClickListener(new ClickListener() {
                                     @Override
                                     public void buttonClick(ClickEvent event) {
                                    	 session.close();
                                         buildLoginView(true);
                                     }
                                 });
                            }
                        });
                	}
                });
                addComponent(content);
                content.setSizeFull();
                content.addStyleName("view-content");
                setExpandRatio(content, 1);
        	}
        });
        menu.removeAllComponents();
        
        for (final String view : new String[] { "dashboard", "sales",  "transactions"}) {
        	Button b = new NativeButton(view.substring(0, 1).toUpperCase()
                    + view.substring(1).replace('-', ' '));
        	
        	b.addStyleName("icon-" + view);
        	b.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					clearMenuSelection();
					event.getButton().addStyleName("selected");
                    if (!nav.getState().equals("/" + view))
                        nav.navigateTo("/" + view);
				}
			});
        	
        	if (view.equals("reports")) {
        		DragAndDropWrapper reports = new DragAndDropWrapper(b);
                reports.setDragStartMode(DragStartMode.NONE);
                reports.setDropHandler(new DropHandler() {

					@Override
                    public void drop(DragAndDropEvent event) {
                        clearMenuSelection();
                        viewNameToMenuButton.get("/reports").addStyleName(
                                "selected");
                        autoCreateReport = true;
                        items = event.getTransferable();
                        nav.navigateTo("/reports");
                    }

                    @Override
                    public AcceptCriterion getAcceptCriterion() {
                        return AcceptItem.ALL;
                    }
                    
                });
                menu.addComponent(reports);
                menu.addStyleName("no-vertical-drag-hints");
                menu.addStyleName("no-horizontal-drag-hints");
        	}
        	else{
        		menu.addComponent(b);
        	}
        	viewNameToMenuButton.put("/" + view, b);
        }
        
        menu.addStyleName("menu");
        menu.setHeight("100%");
        viewNameToMenuButton.get("/dashboard").setHtmlContentAllowed(true);
//        viewNameToMenuButton.get("/dashboard").setCaption(
//                "Dashboard<span class=\"badge\">2</span>");
        
        String f = Page.getCurrent().getUriFragment();
        if (f != null && f.startsWith("!")) {
            f = f.substring(1);
        }
        if (f == null || f.equals("") || f.equals("/")) {
            nav.navigateTo("/dashboard");
            menu.getComponent(0).addStyleName("selected");
            //helpManager.showHelpFor(DashboardView.class);
        } else {
            nav.navigateTo(f);
            //helpManager.showHelpFor(routes.get(f));
            viewNameToMenuButton.get(f).addStyleName("selected");
        }
        nav.addViewChangeListener(new ViewChangeListener() {

			@Override
			public boolean beforeViewChange(ViewChangeEvent event) {
				helpManager.closeAll();
				return true;
			}

			@Override
			public void afterViewChange(ViewChangeEvent event) {
				// TODO Auto-generated method stub
				View newView = event.getNewView();
                helpManager.showHelpFor(newView);
//                if (autoCreateReport && newView instanceof ReportsView) {
//                    ((ReportsView) newView).autoCreate(2, items, transactions);
//                }
                autoCreateReport = false;			
			}       	
        });
		
	 }
	
	HelpManager getHelpManager() {
	        return helpManager;
	 }
	
	 private void clearMenuSelection(){
			for (Iterator<Component> it = menu.getComponentIterator(); it.hasNext();) {
	            Component next = it.next();
	            if (next instanceof NativeButton) {
	                next.removeStyleName("selected");
	            } else if (next instanceof DragAndDropWrapper) {
	                // Wow, this is ugly (even uglier than the rest of the code)
	                ((DragAndDropWrapper) next).iterator().next()
	                        .removeStyleName("selected");
	            }
			}
	 }
 
	 void clearDashboardButtonBadge() {
	        viewNameToMenuButton.get("/dashboard").setCaption("Dashboard");
	 }
	
	private void refresh_project(Window window){
		 window.addCloseListener(new CloseListener() {
			
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				session.refresh(current_project);
			}
		});
	 }
	
	private void refresh_user(Window window){
		 window.addCloseListener(new CloseListener() {
			
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
               session.refresh(current_user);
			}
		});
	 }

    public static Student getCurrentUser(){
        return current_user;
    }
}