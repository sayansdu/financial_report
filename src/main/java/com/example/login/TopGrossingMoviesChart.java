package com.example.login;

import java.util.*;

import com.example.login.data.DataProvider;
import com.example.login.data.DataProvider.Movie;

import com.example.login.entity.Product;
import com.example.login.entity.Project;
import com.example.login.entity.Report;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.Series;



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

        currentProject = LoginUI.getCurrentProject();
        if(currentProject != null && currentProject.getProducts()!= null)
        {
            List<Product> products = new ArrayList<Product>(currentProject.getProducts());

            if(products.size()>6){
                List<Series> series = new ArrayList<Series>();

                for (int i = 0; i < 7; i++) {
                    Product product = products.get(i);
                    Set<Report> reports = product.getReport();
                    int score = 0;
                    for (Report report : reports) {
                        score += report.getSold_amount() * (report.getPrice()-report.getCost_price());
                    }

                    series.add(new ListSeries(product.getName(), score));
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
