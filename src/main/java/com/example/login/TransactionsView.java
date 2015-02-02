package com.example.login;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Random;

import org.tepi.filtertable.FilterTable;
import org.vaadin.haijian.ExcelExporter;
import org.vaadin.haijian.PdfExporter;


import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomTable.RowHeaderMode;

public class TransactionsView extends VerticalLayout implements View {
	 FilterTable filterTable;
	 IndexedContainer container = new IndexedContainer();
	    
	 enum State {
	     CREATED, PROCESSING, PROCESSED, FINISHED;
	 }
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		setMargin(new MarginInfo(true, false, false, false));
		setSizeFull();
		
		try {
			VerticalLayout panel = (VerticalLayout) buildNormalTableTab();
			HorizontalLayout hr = new HorizontalLayout();
			hr.setWidth("100%");
			hr.setMargin(true);
			
			ExcelExporter excelExporter = new ExcelExporter(filterTable);
			excelExporter.setDateFormat("yyyy-MM-dd");
//			excelExporter.setContainerToBeExported(filterTable);
	        excelExporter.setCaption("Export as Excel");  
			hr.addComponent(excelExporter);
			
			PdfExporter pdf = new PdfExporter(filterTable);
//			pdf.setContainerToBeExported(filterTable);
			pdf.setHeader("pdf-file");

			pdf.setCaption("Export as PDF");  
			pdf.setWithBorder(false);
			hr.addComponent(pdf);			
			addComponent(hr); 		
			addComponent(panel); 
			setExpandRatio(panel, 1); 	
			
		} catch (UnsupportedOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Component buildNormalTableTab(){
		
		filterTable = buildFilterTable();
        final VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.setSpacing(true);
        mainLayout.setMargin(true);
        mainLayout.addComponent(filterTable);        
        return mainLayout;
	}

	private FilterTable buildFilterTable(){
		FilterTable filterTable = new FilterTable();
		filterTable.setSizeFull();

        filterTable.setFilterBarVisible(true);  
        filterTable.setSelectable(true);
        filterTable.setImmediate(true);
        filterTable.setMultiSelect(true);

        filterTable.setRowHeaderMode(RowHeaderMode.INDEX); 
        filterTable.setColumnCollapsingAllowed(true);
        filterTable.setColumnCollapsed("state", true);
        filterTable.setColumnReorderingAllowed(true);
        filterTable.setContainerDataSource(buildContainer());
       
        filterTable.setColumnHeaderMode(com.vaadin.ui.CustomTable.ColumnHeaderMode.EXPLICIT);	        
        filterTable.setColumnHeaders(new String[] { "Имя", "ИД", "Состояние", "Дата","Удостоверенный","Проверенный" /*, "Ð¡Ð¡Ð«Ð›ÐšÐ�", "Ð¡Ð¢Ð Ð�Ð�Ð˜Ð¦Ð� Ð�Ð’Ð¢ÐžÐ Ð�", "ÐšÐžÐœÐœÐ•Ð�Ð¢Ð�Ð Ð˜Ð˜"*/ });
        filterTable.setVisibleColumns(new String[] { "name", "id", "state",
                "date", "validated", "checked" });
		
		return filterTable;
	}

	private IndexedContainer buildContainer(){
		IndexedContainer cont = new IndexedContainer();
	    Calendar c = Calendar.getInstance();
	    
	    cont.addContainerProperty("name", String.class, null);
	    cont.addContainerProperty("id", Integer.class, null);
        cont.addContainerProperty("state", State.class, null);
        cont.addContainerProperty("date", Timestamp.class, null);
        cont.addContainerProperty("validated", Boolean.class, null);
        cont.addContainerProperty("checked", Boolean.class, null);
        
        Random random = new Random();
        for (int i = 0; i < 500; i++) { 
            cont.addItem(i);
            /* Set name and id properties */
            cont.getContainerProperty(i, "name").setValue("Order " + i);
            cont.getContainerProperty(i, "id").setValue(i);
            /* Set state property */
            int rndInt = random.nextInt(4);
            State stateToSet = State.CREATED;
            if (rndInt == 0) {
                stateToSet = State.PROCESSING;
            } else if (rndInt == 1) {
                stateToSet = State.PROCESSED;
            } else if (rndInt == 2) {
                stateToSet = State.FINISHED;
            }
            cont.getContainerProperty(i, "state").setValue(stateToSet);
            /* Set date property */
            cont.getContainerProperty(i, "date").setValue(
                    new Timestamp(c.getTimeInMillis()));
            c.add(Calendar.DAY_OF_MONTH, 1);
            /* Set validated property */
            cont.getContainerProperty(i, "validated").setValue(
                    random.nextBoolean());
            /* Set checked property */
            cont.getContainerProperty(i, "checked").setValue(
                    random.nextBoolean());
        }
	    
	    return cont;
	}

}
