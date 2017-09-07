package edu.ap.movies;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.json.JSONException;
import org.json.JSONObject;

import netscape.javascript.JSObject;
import redis.clients.jedis.Jedis;

@Path("/movies")
public class MoviesResource {

	// Elke movie die er is
	@GET
	@Produces({ "text/html" })
	public String getAllMovies() {
		// Uncomment voor opvullen
		// this.fillDb(true);

		Jedis jedis = JedisConnection.getInstance().getConnection();
		StringBuilder builder = new StringBuilder();

		builder.append("<html>");
		builder.append("<head>");
		builder.append("<title>Movie</title>");
		builder.append("</head>");

		builder.append("<body>");
		builder.append("<ul>");
		for (String key : jedis.keys("title:*")) {
			for (String title : jedis.smembers(key)) {
				builder.append("<li>" + title);
			}
		}
		builder.append("</ul>");
		builder.append("</body>");
		builder.append("</html>");

		return builder.toString();
	}

	// Specifieke movie
	// Data => de data die wordt gepost
	@POST
	public String getMovieTitles(String data) {

		// this.fillDb(true);

		Jedis jedis = JedisConnection.getInstance().getConnection();
		StringBuilder builder = new StringBuilder();

		builder.append("<html>");
		builder.append("<head>");
		builder.append("<title>Movies</title>");
		builder.append("</head>");

		builder.append("<body>");
		// Selecteren van correcte title
		for (String title : jedis.keys("title:*")) {
			String tmpTitle = jedis.get(title);
			if (tmpTitle.equals(data)) {
				builder.append("testink");
				int titleId = Integer.parseInt(title.split(":")[1]);

				builder.append("<h1>" + tmpTitle + "</h1>");
				builder.append("<ul>");
				for (String movie : jedis.smembers("movies:" + titleId)) {
					builder.append("<li>" + movie+ "</li>");
				}
				builder.append("</ul>");

				break; // Breaken is normaal nooit een goed idee, maar het haalt
						// performantie omhoog als je het doet in een foreach
						// loop
			} else if (!tmpTitle.equals(data)) {
				String retrieveMovieTitle = "http://www.omdbapi.com/?t=" + data + "&apikey=plzBanMe";
				try {
					JSONObject json = readJsonFromUrl(retrieveMovieTitle);
					String titleJson = (String) json.get("Title");
					int year = (int) json.get("Year");
					String[] actors = (String[]) json.get("Actors");
					builder.append("<strong>Title</Strong>");
					builder.append("<br/>");
					builder.append("<h1>" + titleJson + "</h1>");
					builder.append("<br/>year: "+ year + "<br/>");
					builder.append("<ul>");
					for(String s : actors){
						builder.append("<li>" + s+ "</li>");
					}
					builder.append("</ul>");

					
				} catch (IOException | JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		builder.append("</body>");
		builder.append("</html>");

		return builder.toString();
	}

	private void fillDb(boolean flush) {
		Jedis jedis = JedisConnection.getInstance().getConnection();

		if (flush) {
			jedis.flushDB();
		}

		jedis.set("title:1", "Deadpool");
		jedis.set("title:2", "rise and fall");

	}

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

}