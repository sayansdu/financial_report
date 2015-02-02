package com.example.login;

import java.util.ArrayList;
import java.util.List;

import com.example.login.data.DataProvider;
import com.example.login.data.DataProvider.Movie;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;


public class TopSixTheatersChart extends Chart {

	public TopSixTheatersChart(){
		setCaption("Top 10 movie");
		getConfiguration().setTitle("");
		getConfiguration().getChart().setType(ChartType.PIE);
		getConfiguration().getxAxis().getLabels().setEnabled(false);
		
		
        setWidth("100%");
        setHeight("90%");
        
        DataSeries series = new DataSeries();

        ArrayList<Movie> movies = DataProvider.getMovies();
        for (int i = 0; i < 6; i++) {
            Movie movie = movies.get(i);
            series.add(new DataSeriesItem(movie.title, movie.score));
        }
        getConfiguration().setSeries(series);
	}
	
}
	
	