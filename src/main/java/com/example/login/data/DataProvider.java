package com.example.login.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.icu.text.SimpleDateFormat;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.util.CurrentInstance;


public class DataProvider implements Serializable{

	/** Container with all the transactions */
    private TransactionsContainer transactions;	
	
	 public static class Movie {
	        public final String title;
	        public final String synopsis;
	        public final String thumbUrl;
	        public final String posterUrl;
	        /** In minutes */
	        public final int duration;
	        public Date releaseDate = null;

	        public int score;
	        public double sortScore = 0;

	        Movie(String title, String synopsis, String thumbUrl, String posterUrl,
	                JsonObject releaseDates, JsonObject critics) {
	            this.title = title;
	            this.synopsis = synopsis;
	            this.thumbUrl = thumbUrl;
	            this.posterUrl = posterUrl;
	            this.duration = (int) ((1 + Math.round(Math.random())) * 60 + 45 + (Math
	                    .random() * 30));
	            try {
	                String datestr = releaseDates.get("theater").getAsString();
	              //  SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	               ////there is some reason for change
	                
	                releaseDate = new Date();
	                score = critics.get("critics_score").getAsInt();
	                sortScore = 0.6 / (0.01 + (System.currentTimeMillis() - releaseDate
	                        .getTime()) / (1000 * 60 * 60 * 24 * 5));
	                sortScore += 10.0 / (101 - score);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }

	        }

	        public String titleSlug() {
	            return title.toLowerCase().replace(' ', '-').replace(":", "")
	                    .replace("'", "").replace(",", "").replace(".", "");
	        }

	        public void reCalculateSortScore(Calendar cal) {
	            if (cal.before(releaseDate)) {
	                sortScore = 0;
	                return;
	            }
	            sortScore = 0.6 / (0.01 + (cal.getTimeInMillis() - releaseDate
	                    .getTime()) / (1000 * 60 * 60 * 24 * 5));
	            sortScore += 10.0 / (101 - score);
	        }
	    }
	 	/*
	     * List of movies playing currently in theaters
	     */
	 
	  private static ArrayList<Movie> movies = new ArrayList<Movie>();

	    /**
	     * Get a list of movies currently playing in theaters.
	     * 
	     * @return a list of Movie objects
	     */
	   public static ArrayList<Movie> getMovies() {
	        return movies;
	    }
	   
	   public IndexedContainer getRevenueForTitle(String title) {
		   IndexedContainer revenue = new IndexedContainer();
	        revenue.addContainerProperty("timestamp", Date.class, new Date());
	        revenue.addContainerProperty("revenue", Double.class, 0.0);
	        revenue.addContainerProperty("date", String.class, "");
	        int index = 0;
	        for (Object id : transactions.getItemIds()) {
	            SimpleDateFormat df = new SimpleDateFormat();
	            df.applyPattern("MM/dd/yyyy");
	            
	            Item item = transactions.getItem(id);

	            if (title.equals(item.getItemProperty("Title").getValue())) {
	                Date d = (Date) item.getItemProperty("timestamp").getValue();

	                Item i = revenue.getItem(df.format(d));
	                if (i == null) {
	                    i = revenue.addItem(df.format(d));
	                    i.getItemProperty("timestamp").setValue(d);
	                    i.getItemProperty("date").setValue(df.format(d));
	                }
	                double current = (Double) i.getItemProperty("revenue")
	                        .getValue();
	                current += (Double) item.getItemProperty("Price").getValue();

	                i.getItemProperty("revenue").setValue(current);
	            }
	        }

	        revenue.sort(new Object[] { "timestamp" }, new boolean[] { true });
	        return revenue;
	   }
	   
	   public IndexedContainer getRevenueByTitle() {
	        IndexedContainer revenue = new IndexedContainer();
	        revenue.addContainerProperty("Title", String.class, "");
	        revenue.addContainerProperty("Revenue", Double.class, 0.0);

	        for (Object id : transactions.getItemIds()) {

	            Item item = transactions.getItem(id);

//	            String title = item.getItemProperty("Title").getValue().toString();
	            String title = "There must be title";
	            if (title == null || "".equals(title))
	                continue;

	            Item i = revenue.getItem(title);
	            if (i == null) {
	                i = revenue.addItem(title);
	                i.getItemProperty("Title").setValue(title);
	            }
	            double current = (Double) i.getItemProperty("Revenue").getValue();
	            current += (Double) item.getItemProperty("Price").getValue();
	            i.getItemProperty("Revenue").setValue(current);
	        }

	        revenue.sort(new Object[] { "Revenue" }, new boolean[] { false });

	        // TODO sometimes causes and IndexOutOfBoundsException
	        if (revenue.getItemIds().size() > 10) {
	            // Truncate to top 10 items
	            List<Object> remove = new ArrayList<Object>();
	            for (Object id : revenue
	                    .getItemIds(10, revenue.getItemIds().size())) {
	                remove.add(id);
	            }
	            for (Object id : remove) {
	                revenue.removeItem(id);
	            }
	        }

	        return revenue;
	    }
	   
	   public static void loadMoviesData() {
		    File cache;
		    
	        // TODO why does this sometimes return null?
	        VaadinRequest vaadinRequest = CurrentInstance.get(VaadinRequest.class);
	        if (vaadinRequest == null) {
	            // PANIC!!!
	            cache = new File("movies.txt");
	        } else {
			           	
	            String baseDirectory = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
	            cache = new File("C:\\dev\\finance\\files\\" + "movies.txt");
	        }

	        JsonObject json = null;
	        try {
	            // TODO check for internet connection also, and use the cache anyway
	            // if no connection is available
	            if (cache.exists()) {
	                json = readJsonFromFile(cache);
				    	                
	            } else {
	                json = readJsonFromUrl("http://api.rottentomatoes.com/api/public/v1.0/lists/movies/in_theaters.json?page_limit=30&apikey=6ycavarreaaqj4s92d523g9n");
	                // Store in cache
	                FileWriter fileWriter = new FileWriter(cache);
	                fileWriter.write(json.toString());
	                fileWriter.close();
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        
	        JsonArray moviesJson;
	        movies.clear();
	        moviesJson = json.getAsJsonArray("movies");
	        for (int i = 0; i < moviesJson.size(); i++) {
	            JsonObject movieJson = moviesJson.get(i).getAsJsonObject();
	            JsonObject posters = movieJson.get("posters").getAsJsonObject();
	            if (!posters.get("profile").getAsString()
	                    .contains("poster_default")) {
	                Movie movie = new Movie(movieJson.get("title").getAsString(),
	                        movieJson.get("synopsis").getAsString(), posters.get(
	                                "profile").getAsString(), posters.get(
	                                "detailed").getAsString(), movieJson.get(
	                                "release_dates").getAsJsonObject(), movieJson
	                                .get("ratings").getAsJsonObject());
	                movies.add(movie);
	            }
	        }

		   
	   }

	   /* JSON utility method */
	    private static String readAll(Reader rd) throws IOException {
	        StringBuilder sb = new StringBuilder();
	        int cp;
	        while ((cp = rd.read()) != -1) {
	            sb.append((char) cp);
	        }
	        return sb.toString();
	    }
	    
	private static JsonObject readJsonFromUrl(String url) throws IOException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                    Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JsonElement jelement = new JsonParser().parse(jsonText);
            JsonObject jobject = jelement.getAsJsonObject();
            return jobject;
        } finally {
            is.close();
        }
    }

	private static JsonObject readJsonFromFile(File path) throws IOException {
        BufferedReader rd = new BufferedReader(new FileReader(path));
        String jsonText = readAll(rd);
        JsonElement jelement = new JsonParser().parse(jsonText);
        JsonObject jobject = jelement.getAsJsonObject();
        return jobject;
    }
	 
	public static Movie getMovieForTitle(String title) {
        for (Movie movie : movies) {
            if (movie.title.equals(title))
                return movie;
        }
        return null;
    }
	
	public TransactionsContainer getTransactions() {
        return transactions;
    }
}
