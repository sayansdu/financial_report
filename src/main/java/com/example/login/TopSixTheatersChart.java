package com.example.login;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.example.login.data.DataProvider;
import com.example.login.data.DataProvider.Movie;
import com.example.login.entity.Product;
import com.example.login.entity.Project;
import com.example.login.entity.Report;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.ListSeries;


public class TopSixTheatersChart extends Chart {

    private Project currentProject;

	public TopSixTheatersChart(){
		setCaption("Top 6 products");
		getConfiguration().setTitle("");
		getConfiguration().getChart().setType(ChartType.PIE);
		getConfiguration().getxAxis().getLabels().setEnabled(false);

        setWidth("100%");
        setHeight("90%");
        DataSeries series = new DataSeries();

        currentProject = LoginUI.getCurrentProject();
        if(currentProject != null && currentProject.getProducts()!= null)
        {
            List<Product> products = new ArrayList<Product>(currentProject.getProducts());
            if(products.size()>5){
                for (int i = 0; i < 5; i++) {
                    Product product = products.get(i);
                    Set<Report> reports = product.getReport();
                    int score = 0;
                    for (Report report : reports) {
                        score += report.getSold_amount() * (report.getPrice()-report.getCost_price());
                    }

                    series.add(new DataSeriesItem(product.getName(), score));
                }
                getConfiguration().setSeries(series);
            }
            else
                loadMovieData(series);
        }
        else
            loadMovieData(series);

	}

    private void loadMovieData(DataSeries series){
        ArrayList<Movie> movies = DataProvider.getMovies();
        for (int i = 0; i < 6; i++) {
            Movie movie = movies.get(i);
            series.add(new DataSeriesItem(movie.title, movie.score));
        }
        getConfiguration().setSeries(series);
    }
	
}
	
	