package com.example.login;

import java.awt.Color;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import com.example.login.data.DataProvider;
import com.example.login.data.DataProvider.Movie;
import com.ibm.icu.text.SimpleDateFormat;

import java.util.Calendar;

import com.vaadin.addon.timeline.Timeline;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Field.ValueChangeEvent;

public class SalesView extends VerticalLayout implements View{

	private Timeline timeline = new Timeline();
    int colorIndex = -1;
    final Container.Indexed AAPL = buildContainer();
    
    DateField date;
    PopupDateField date2;
    Calendar cal = new GregorianCalendar(2011, Calendar.SEPTEMBER, 1);
    Calendar cal2 = new GregorianCalendar(2012, Calendar.MAY, 30);  
    Color[] colors = new Color[] { new Color(52, 154, 255),
            new Color(242, 81, 57), new Color(255, 201, 35),
            new Color(83, 220, 164) };
    
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		setSizeFull();
        addStyleName("timeline");
        setMargin(false);
        setSpacing(false);
        Label header = new Label("Revenue by Movie Title");
        header.addStyleName("h1");
        addComponent(header);
        
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.setWidth("100%");
        toolbar.setSpacing(true);
        toolbar.setMargin(true);
        toolbar.addStyleName("toolbar");
        addComponent(toolbar);
        
        final ComboBox movieSelect = new ComboBox();
        ArrayList<Movie> movies = DataProvider.getMovies();    
        
        movieSelect.setWidth("300px");
        toolbar.addComponent(movieSelect);       
        
        Button add = new Button("Add");
        add.addStyleName("default");
        toolbar.addComponent(add);
        toolbar.setComponentAlignment(add, Alignment.BOTTOM_LEFT);
        
        Button clear = new Button("Clear");
        clear.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                timeline.removeAllGraphDataSources();
            }
        });
        
        toolbar.addComponent(clear);
        toolbar.setComponentAlignment(clear, Alignment.BOTTOM_RIGHT);
        toolbar.setExpandRatio(clear, 1);
        
        HorizontalLayout csslay = new HorizontalLayout();
        csslay.setMargin(true);
        csslay.setSpacing(true);
        date = new DateField("start:");
        date.setValue(cal.getTime());
        csslay.addComponent(date);
        
        date2 = new PopupDateField("end:");
        date2.setValue(cal2.getTime());
        csslay.addComponent(date2);
        
        Button set = new Button("Set Range");
        csslay.addComponent(set);
        set.addClickListener(new ClickListener(){
     		@Override
    			public void buttonClick(ClickEvent event) {
     				getGraph();
    			}
    		});
        
        csslay.setComponentAlignment(set, Alignment.BOTTOM_RIGHT);
        addComponent(csslay);
        date.setSizeUndefined();
        
        getGraph();
	}
	
	private void getGraph(){
		timeline.removeAllGraphDataSources();
        timeline.setId("timeline");
        timeline.setDateSelectVisible(true);
        timeline.setChartModesVisible(true);
        timeline.setGraphShadowsEnabled(false);
        timeline.setZoomLevelsVisible(true);
        
        timeline.setBrowserVisible(true);
        timeline.setChartModesCaption("Graph Mode: ");

        timeline.addZoomLevel("1d", 86400000L);
        timeline.addZoomLevel("5d", 5 * 86400000L);
        timeline.addZoomLevel("1m", 2629743830L);
        timeline.addZoomLevel("3m", 3 * 2629743830L);
        timeline.addZoomLevel("6m", 6 * 2629743830L);
                
        timeline.setSizeFull();
        timeline.setNoDataSourceCaption("<span class=\"v-label h2 light\">Add a data set from the dropdown above</span>");
  
        addComponent(timeline);
        setExpandRatio(timeline, 2); 
        
        timeline.addGraphDataSource(AAPL, Timeline.PropertyId.TIMESTAMP, Timeline.PropertyId.VALUE);        
        timeline.setGraphLegend(AAPL, "Apple");
        
        if(date2.getValue().after(date.getValue()) ) {
        	timeline.setVisibleDateRange(date.getValue(), date2.getValue());
        }
        else 
        	timeline.setVisibleDateRange(cal.getTime(), cal2.getTime());
        
        timeline.setVerticalAxisLegendUnit(AAPL, "meter");
        timeline.setGraphOutlineColor(AAPL, colors[1]);
	}
	
	private IndexedContainer buildContainer(){
		// Create the container
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty(Timeline.PropertyId.TIMESTAMP,  Date.class, null);
        container.addContainerProperty(Timeline.PropertyId.VALUE, Integer.class, 0);
        
        Calendar cal = new GregorianCalendar(2011, Calendar.SEPTEMBER, 1);
        cal.add(Calendar.MONTH, -1);
        
        long counter = 0;
        Random random = new Random();
        while (counter < 300) {
            Item item = container.addItem(cal.getTime());
            item.getItemProperty(Timeline.PropertyId.TIMESTAMP).setValue(
                    cal.getTime());
            float val = random.nextInt(300);
            
            item.getItemProperty(Timeline.PropertyId.VALUE).setValue(
                    Math.round(val));
            cal.add(Calendar.DAY_OF_MONTH, 1);
            counter++;
        }
        
        return container;
	}



	
	



}
