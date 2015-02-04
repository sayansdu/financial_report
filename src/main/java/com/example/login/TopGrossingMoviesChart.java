package com.example.login;

import java.util.ArrayList;
import java.util.List;

import com.example.login.controller.Reports;
import com.example.login.data.DataProvider;
import com.example.login.data.DataProvider.Movie;

import com.example.login.entity.Project;
import com.example.login.entity.Report;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.Series;
import com.vaadin.addon.charts.model.XAxis;
import com.vaadin.addon.charts.model.YAxis;



public class TopGrossingMoviesChart extends Chart {

    private Project currentProject;

public TopGrossingMoviesChart() {
		
		setCaption("Top Grossing Products");
        getConfiguration().setTitle("");
        getConfiguration().getChart().setType(ChartType.BAR);
        getConfiguration().getxAxis().getLabels().setEnabled(false);
        getConfiguration().getyAxis().getLabels().setEnabled(false);
        getConfiguration().getxAxis().setTickWidth(0); 
        setWidth("100%");
        setHeight("90%");

        currentProject = LoginUI.current_project;
        if(currentProject != null)
        {
            List<Report> reports = Reports.getReports();

            if(reports!=null && reports.size()>6){
                List<Series> series = new ArrayList<Series>();

                int length = reports.size();
                if(length > 6)
                    length = 6;

                for (int i = 0; i < length; i++) {
                    Report report = reports.get(i);
                    int score = report.getSold_amount() * (report.getPrice()-report.getCost_price());
                    series.add(new ListSeries(report.getProduct().getName(), score));

                }
                getConfiguration().setSeries(series);
            }
            else
                loadMovieData();
        }
        else
        {
            loadMovieData();
        }
		 
    }

    private void loadMovieData(){
        ArrayList<Movie> movies = DataProvider.getMovies();
        List<Series> series = new ArrayList<Series>();

        for (int i = 0; i < 6; i++) {
            Movie movie = movies.get(i);
            series.add(new ListSeries(movie.title, movie.score));

        }
        getConfiguration().setSeries(series);
    }
	

}
