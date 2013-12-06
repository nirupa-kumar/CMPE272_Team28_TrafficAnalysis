package com.bigdata.impala;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.bigdata.impala.dataobjects.Location;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Path("/traffic")
public class TrafficServer {

	ClouderaImpalaCaller caller = new ClouderaImpalaCaller();

	@GET
	@Path("getAccidents/{year}/{month}/{day}")
	@Produces(MediaType.APPLICATION_JSON)
	public String addAccidents(@PathParam("year") int year,
			@PathParam("month") int month, @PathParam("day") int day) {

		String sql = String
				.format("select * from  accident where year = '%d' and month = '%d' and day = '%d' limit 5",
						year, month, day);

		String jsonStr = caller.makeQueryJSON(sql);

		
		return jsonStr;
	}
	
	@GET
	@Path("getAccidentLocations/{year}/{month}/{day}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAccidentLocations(@PathParam("year") int year,
			@PathParam("month") int month, @PathParam("day") int day) {

		String sql = String
				.format("select city, latitude, longitud from accident where year = %d and month = %d and day = %d limit 5",
						year, month, day);
		String jsonStr = "[ " +
							"{\"city\": \"San Jose\" , \"latitude\" : \"37.3333\", \"longitud\":\"-121.9000\"}," +
							"{\"city\": \"Palo Alto\" , \"latitude\" : \"37.4292\", \"longitud\":\"-122.1381\"}," +
							"{\"city\": \"Mountain View\" , \"latitude\" : \"37.3894\", \"longitud\":\"-122.0819\"}," +
							"{\"city\": \"San Francisco\" , \"latitude\" : \"37.7833\", \"longitud\":\"-122.4167\"}," +
							"{\"city\": \"Lake Tahoe\" , \"latitude\" : \"39.0917\", \"longitud\":\"-120.0417\"}" +
							"]";
		

//		Location loc1 = new Location("San Jose", "37.3333", "121.9000");
//		Location loc2 = new Location("PaloAlto", "37.7833", "122.4167");
//		Location loc3 = new Location("Mountain View", "37.3894", "122.0819");
//		
//		ArrayList<Location> locations = new ArrayList<Location>();
//		locations.add(loc1);
//		locations.add(loc2);
//		locations.add(loc3);
		
//		Gson gson = new Gson();
		//return gson.toJson(locations);
		jsonStr = caller.makeQueryJSON(sql);
		return jsonStr;
	
	}

	@GET
	@Path("states")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllStates(){
		String SQL = "select lc.state as statecode, lc.name as statename from " +
				"		locationcode lc where city is null and county is null " +
				"		and state is not null";
		
		String jsonStr = caller.makeQueryJSON(SQL);
		System.out.println(jsonStr);
//		String[] states = TrafficConstants.States;
//		String jsonStr = gson.toJson(states);
		return jsonStr;
	}

	@GET
	@Path("cities/{statename}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllStates(@PathParam("statename") String state){
		String SQL = String.format("select city as citycode, name as cityname" +
				" from locationcode where state = '%s' and city is not null",state);
		
		String jsonStr = caller.makeQueryJSON(SQL);
		System.out.println(jsonStr);
//		String[] states = TrafficConstants.States;
//		String jsonStr = gson.toJson(states);
		return jsonStr;
	}
	
	
	@GET
	@Path("getAccidentLocations/{state}/{city}/{startDate}/{endDate}/{specificDate}/{holiday}/{hYear}/{acctype}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAccidentLocations(@PathParam("state") String state,
			@PathParam("city") String city, @PathParam("startDate") String startDate, @PathParam("endDate") String endDate, @PathParam("specificDate") String specificDate, @PathParam("holiday") String holiday, @PathParam("hYear") String hYear ,  @PathParam("acctype") int accType) {

		
		//String sql = "select state, city, latitude, longitud, date_t from accident where date_t is not null";
		String sql = "select * from accident where date_t is not null";
		if(!state.equalsIgnoreCase("ALL")){
			sql += " and state = "+state;
		}
		if(!city.equalsIgnoreCase("ALL")){
			sql += " and city = "+city;
		}
		if(!startDate.equalsIgnoreCase("None") && !endDate.equalsIgnoreCase("None")){
			sql += String.format(" and date_t between '%s' and '%s'",startDate,endDate);
		}else if(!startDate.equalsIgnoreCase("None") && endDate.equalsIgnoreCase("None")){
			sql+= " and date_t >= '"+startDate+"'";
		}else if(!endDate.equalsIgnoreCase("None") && startDate.equalsIgnoreCase("None")){
			sql+= " and date_t <= '"+endDate+"'";
		}
		
		if(!specificDate.equalsIgnoreCase("None")){
			sql += " and date_t = '"+specificDate+"'";
		}
		
		if(!holiday.equalsIgnoreCase("None") && !hYear.equalsIgnoreCase("None") ){
			if(hYear.equals("All")){
				Set<Integer> yearKeys = TrafficConstants.holidayMaps.keySet();
				int i=0;
				for(int key: yearKeys){
					String holidayDate = TrafficConstants.holidayMaps.get(key).get(holiday);
					if(i==0){
						sql += " and ( date_t = '"+holidayDate+"'";
					}else{
						sql += " or date_t = '"+holidayDate+"'";
					}
					i = i+1;
				}
				sql += ")";
				

			}else{
			
				HashMap<String,String> dates = TrafficConstants.holidayMaps.get(Integer.parseInt(hYear));
				if(dates != null && dates.size() > 0){
					String holidayDate = dates.get(holiday);
					sql += " and date_t = '"+holidayDate+"'";
				}
			}
			
		}
		
		if(accType != 999 ){
			switch(accType){
			case 100:
				sql += " and drunk_dr > 0";
				break;
			case 2: case 3: case 4: case 5: case 6: case 7: case 10: case 11:
				sql += " and weather = "+accType;
				break;
			
			}
		}
		
		System.out.println("...sql: "+sql);
//		String sql = String
//				.format("select city, latitude, longitud from accident where year = %d and month = %d and day = %d limit 5",
//						year, month, day);
//		String jsonStr = "[ " +
//							"{\"city\": \"San Jose\" , \"latitude\" : \"37.3333\", \"longitud\":\"-121.9000\"}," +
//							"{\"city\": \"Palo Alto\" , \"latitude\" : \"37.4292\", \"longitud\":\"-122.1381\"}," +
//							"{\"city\": \"Mountain View\" , \"latitude\" : \"37.3894\", \"longitud\":\"-122.0819\"}," +
//							"{\"city\": \"San Francisco\" , \"latitude\" : \"37.7833\", \"longitud\":\"-122.4167\"}," +
//							"{\"city\": \"Lake Tahoe\" , \"latitude\" : \"39.0917\", \"longitud\":\"-120.0417\"}" +
//							"]";
		

//		Location loc1 = new Location("San Jose", "37.3333", "121.9000");
//		Location loc2 = new Location("PaloAlto", "37.7833", "122.4167");
//		Location loc3 = new Location("Mountain View", "37.3894", "122.0819");
//		
//		ArrayList<Location> locations = new ArrayList<Location>();
//		locations.add(loc1);
//		locations.add(loc2);
//		locations.add(loc3);
		
//		Gson gson = new Gson();
		//return gson.toJson(locations);
		String jsonStr = caller.makeQueryJSON(sql);
		System.out.println(jsonStr);
		return jsonStr;
	}
	
	@GET
	@Path("getAccidentsDetailed/{state}/{city}/{startDate}/{endDate}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAccidentsDetailed(@PathParam("state") String state,
			@PathParam("city") String city, @PathParam("startDate") String startDate, @PathParam("endDate") String endDate) {

		
		//String sql = "select state, city, latitude, longitud, date_t from accident where date_t is not null";
		String sql = "select * from accident where date_t is not null";
		if(!state.equalsIgnoreCase("ALL")){
			sql += " and state = "+state;
		}
		if(!city.equalsIgnoreCase("ALL")){
			sql += " and city = "+city;
		}
		if(!startDate.equalsIgnoreCase("None") && !endDate.equalsIgnoreCase("None")){
			sql += String.format(" and date_t between '%s' and '%s'",startDate,endDate);
		}else if(!startDate.equalsIgnoreCase("None") && endDate.equalsIgnoreCase("None")){
			sql+= " and date_t >= '"+startDate+"'";
		}else if(!endDate.equalsIgnoreCase("None") && startDate.equalsIgnoreCase("None")){
			sql+= " and date_t <= '"+endDate+"'";
		}
		
		System.out.println("...sql: "+sql);

		String jsonStr = caller.makeQueryJSONFullColumnNames(sql);
		
		return jsonStr;
		
	}
	
	
	@GET
	@Path("getAccidentDetails/{state}/{city}/{latitude}/{longitud}/{date}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAccidentDetails(@PathParam("state") String state,
			@PathParam("city") String city, @PathParam("latitude") double latitude, @PathParam("longitud") double longitud, @PathParam("date") String date) {

		
		//String sql = "select * from accident where date_t is not null and state = "+state +" and city = " + city;
		String sql = String.format("select * from accident where date_t is not null and state = %s and city = %s and latitude = %s and longitud = %s and date_t = '%s'",state, city, latitude, longitud, date);
	
		
		System.out.println("...sql: "+sql);
		
		Gson gson = new Gson();
		//return gson.toJson(locations);
//		ResultSet rs = caller.makeQueryResultSet(sql);
				
//		JsonArray jsonArray = null;
		String jsonStr = caller.makeQueryJSONFullColumnNames(sql);
//		try {
//			jsonArray = caller.convertToJSONWithFullColumnNames(rs);
//
//			for(JsonElement ele: jsonArray){
//				JsonObject obj = ele.getAsJsonObject();
//				if(obj.get("latitude").getAsDouble() == latitude && obj.get("longitud").getAsDouble() == longitud && obj.get("date_t").equals(date)){
//					jsonStr = obj.getAsString();
//				}
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		
		
		return jsonStr;
					
	}
	
	@GET
	@Path("getStatsByCity/{state}/{city}/{year}/{holiday}/{acctype}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getStatsByCity(@PathParam("state") String state,
			@PathParam("city") String city, @PathParam("year") String year, @PathParam("holiday") String holiday, @PathParam("acctype") int accType) {

		if(state == "ALL" || city == "ALL"){
			return "";
		};
		
		String sql = "";
		//String sql = "select state, city, latitude, longitud, date_t from accident where date_t is not null";
		if(year.equalsIgnoreCase("ALL")) {
			sql += "select state, city, year, count(*) as numincidents from accident where date_t is not null";
		} else {
			 sql += "select state, city, month, year, count(*) as numincidents from accident where date_t is not null";
		}
		
		if(!state.equalsIgnoreCase("ALL")){
			sql += " and state = "+state;
		}
		if(!city.equalsIgnoreCase("ALL")){
			sql += " and city = "+city;
		}
		if(!year.equalsIgnoreCase("ALL")){
			sql += " and year =" +year;
		}		
		
		if(!holiday.equalsIgnoreCase("None") ){
			String hYear = year;
			if(hYear.equals("All")){
				Set<Integer> yearKeys = TrafficConstants.holidayMaps.keySet();
				int i=0;
				for(int key: yearKeys){
					String holidayDate = TrafficConstants.holidayMaps.get(key).get(holiday);
					if(i==0){
						sql += " and ( date_t = '"+holidayDate+"'";
					}else{
						sql += " or date_t = '"+holidayDate+"'";
					}
					i = i+1;
				}
				sql += ")";
				

			}else{
			
				HashMap<String,String> dates = TrafficConstants.holidayMaps.get(Integer.parseInt(hYear));
				if(dates != null && dates.size() > 0){
					String holidayDate = dates.get(holiday);
					sql += " and date_t = '"+holidayDate+"'";
				}
			}
			
		}
		
		if(accType != 999 ){
			switch(accType){
			case 100:
				sql += " and drunk_dr > 0";
				break;
			case 2: case 3: case 4: case 5: case 6: case 7: case 10: case 11:
				sql += " and weather = "+accType;
				break;
			
			}
		}
		
		sql += " group by state, city";
		
		if(!year.equalsIgnoreCase("ALL")){
			sql += " , year, month order by month asc limit 10000";
		}else{
			sql += " , year order by year asc limit 10000";
		}
		System.out.println("...sql: "+sql);

		String jsonStr = caller.makeQueryJSON(sql);
		System.out.println(jsonStr);
		return jsonStr;
	}
	
	@GET
	@Path("getStatsByState/{state}/{city}/{year}/{holiday}/{acctype}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getStatsByState(@PathParam("state") String state,
			@PathParam("city") String city, @PathParam("year") String year, @PathParam("holiday") String holiday, @PathParam("acctype") int accType) {

		String sql = "";
		//String sql = "select state, city, latitude, longitud, date_t from accident where date_t is not null";
		if(year.equalsIgnoreCase("ALL")) {
			sql += "select state, year, count(*) as numincidents from accident where date_t is not null";
		} else {
			 sql += "select state, month, year, count(*) numincidents from accident where date_t is not null";
		}
		
		if(!state.equalsIgnoreCase("ALL")){
			sql += " and state = "+state;
		}
		
		if(!year.equalsIgnoreCase("ALL")){
			sql += " and year =" +year;
		}		
		
		if(!holiday.equalsIgnoreCase("None") ){
			String hYear = year;
			if(hYear.equals("All")){
				Set<Integer> yearKeys = TrafficConstants.holidayMaps.keySet();
				int i=0;
				for(int key: yearKeys){
					String holidayDate = TrafficConstants.holidayMaps.get(key).get(holiday);
					if(i==0){
						sql += " and ( date_t = '"+holidayDate+"'";
					}else{
						sql += " or date_t = '"+holidayDate+"'";
					}
					i = i+1;
				}
				sql += ")";
				

			}else{
			
				HashMap<String,String> dates = TrafficConstants.holidayMaps.get(Integer.parseInt(hYear));
				if(dates != null && dates.size() > 0){
					String holidayDate = dates.get(holiday);
					sql += " and date_t = '"+holidayDate+"'";
				}
			}
			
		}
		
		if(accType != 999 ){
			switch(accType){
			case 100:
				sql += " and drunk_dr > 0";
				break;
			case 2: case 3: case 4: case 5: case 6: case 7: case 10: case 11:
				sql += " and weather = "+accType;
				break;
			
			}
		}
		
		if(state != "ALL"){
			sql += " group by state ";
		}
		
		if(!year.equalsIgnoreCase("ALL")){
			if(sql.contains(" group by ")){
				sql += " , year, month order by month asc limit 10000";
			}else{
				sql += " group by year, month order by month asc limit 10000";
			}
		}else{
			if(sql.contains(" group by ")){
				sql += " , year order by year asc limit 10000";
			}else{
				sql += " group by year order by year asc limit 10000";
			}
		}
		
		System.out.println("...sql: "+sql);
//		String sql = String
//				.format("select city, latitude, longitud from accident where year = %d and month = %d and day = %d limit 5",
//						year, month, day);
//		String jsonStr = "[ " +
//							"{\"city\": \"San Jose\" , \"latitude\" : \"37.3333\", \"longitud\":\"-121.9000\"}," +
//							"{\"city\": \"Palo Alto\" , \"latitude\" : \"37.4292\", \"longitud\":\"-122.1381\"}," +
//							"{\"city\": \"Mountain View\" , \"latitude\" : \"37.3894\", \"longitud\":\"-122.0819\"}," +
//							"{\"city\": \"San Francisco\" , \"latitude\" : \"37.7833\", \"longitud\":\"-122.4167\"}," +
//							"{\"city\": \"Lake Tahoe\" , \"latitude\" : \"39.0917\", \"longitud\":\"-120.0417\"}" +
//							"]";
		

//		Location loc1 = new Location("San Jose", "37.3333", "121.9000");
//		Location loc2 = new Location("PaloAlto", "37.7833", "122.4167");
//		Location loc3 = new Location("Mountain View", "37.3894", "122.0819");
//		
//		ArrayList<Location> locations = new ArrayList<Location>();
//		locations.add(loc1);
//		locations.add(loc2);
//		locations.add(loc3);
		
//		Gson gson = new Gson();
		//return gson.toJson(locations);
		String jsonStr = caller.makeQueryJSON(sql);
		System.out.println(jsonStr);
		return jsonStr;
	}
	
	
	@GET
	@Path("getStatsAllStates/{year}/{holiday}/{acctype}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getStatsAllStates( @PathParam("year") String year, @PathParam("holiday") String holiday, @PathParam("acctype") int accType) {
		System.out.println("....in getStatsAllStates");
		String sql = "";
		//String sql = "select state, city, latitude, longitud, date_t from accident where date_t is not null";
		if(year.equalsIgnoreCase("ALL")) {
			sql += "select year, count(*) as numincidents from accident where date_t is not null ";
		} else {
			 sql += "select year, month, count(*) numincidents from accident where date_t is not null and year="+year;
		}
		
		if(!holiday.equalsIgnoreCase("None") ){
			String hYear = year;
			if(hYear.equals("All")){
				Set<Integer> yearKeys = TrafficConstants.holidayMaps.keySet();
				int i=0;
				for(int key: yearKeys){
					String holidayDate = TrafficConstants.holidayMaps.get(key).get(holiday);
					if(i==0){
						sql += " and ( date_t = '"+holidayDate+"'";
					}else{
						sql += " or date_t = '"+holidayDate+"'";
					}
					i = i+1;
				}
				sql += ")";
				

			}else{
			
				HashMap<String,String> dates = TrafficConstants.holidayMaps.get(Integer.parseInt(hYear));
				if(dates != null && dates.size() > 0){
					String holidayDate = dates.get(holiday);
					sql += " and date_t = '"+holidayDate+"'";
				}
			}
			
		}
		
		if(accType != 999 ){
			switch(accType){
			case 100:
				sql += " and drunk_dr > 0";
				break;
			case 2: case 3: case 4: case 5: case 6: case 7: case 10: case 11:
				sql += " and weather = "+accType;
				break;
			
			}
		}
		
				
		if(!year.equalsIgnoreCase("ALL")){
				sql += " group by year, month order by month asc limit 10000";
		}else{
				sql += " group by year order by year asc limit 10000";
		}
		
		System.out.println("...sql: "+sql);

		String jsonStr = caller.makeQueryJSON(sql);
		System.out.println(jsonStr);
		return jsonStr;
	}
	
	@GET
	@Path("getStateRanks/{year}/{holiday}/{acctype}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getStateRanks( @PathParam("year") String year, @PathParam("holiday") String holiday, @PathParam("acctype") int accType) {

		String sql = "";
		//String sql = "select state, city, latitude, longitud, date_t from accident where date_t is not null";
//		if(year.equalsIgnoreCase("ALL")) {
//			sql += "select state, count(*) as numincidents from accident where date_t is not null ";
//		} else {
//			 sql += "select state, count(*) numincidents from accident where date_t is not null ";
//		}
		sql += "select ac.state, lc.name, count(*) as numincidents from accident ac, locationcode lc where date_t is not null  and ac.state = cast(lc.state as INT) ";
		
		if(!holiday.equalsIgnoreCase("None") ){
			String hYear = year;
			if(hYear.equals("All")){
				Set<Integer> yearKeys = TrafficConstants.holidayMaps.keySet();
				int i=0;
				for(int key: yearKeys){
					String holidayDate = TrafficConstants.holidayMaps.get(key).get(holiday);
					if(i==0){
						sql += " and ( date_t = '"+holidayDate+"'";
					}else{
						sql += " or date_t = '"+holidayDate+"'";
					}
					i = i+1;
				}
				sql += ")";
				

			}else{
			
				HashMap<String,String> dates = TrafficConstants.holidayMaps.get(Integer.parseInt(hYear));
				if(dates != null && dates.size() > 0){
					String holidayDate = dates.get(holiday);
					sql += " and date_t = '"+holidayDate+"'";
				}
			}
			
		}
		
		if(accType != 999 ){
			switch(accType){
			case 100:
				sql += " and drunk_dr > 0";
				break;
			case 2: case 3: case 4: case 5: case 6: case 7: case 10: case 11:
				sql += " and weather = "+accType;
				break;
			
			}
		}
		
				
		if(!year.equalsIgnoreCase("ALL")){
				sql += " and year = "+year +" group by state, lc.name order by numincidents desc limit 10000";
		}else{
				sql += "  group by state, lc.name order by numincidents desc limit 10000";
		}
		
		System.out.println("...sql: "+sql);

		String jsonStr = caller.makeQueryJSON(sql);
		System.out.println(jsonStr);
		return jsonStr;
	}
	
	@GET
	@Path("getCityRanks/{year}/{holiday}/{acctype}/{numcities}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getCityRanks( @PathParam("year") String year, @PathParam("holiday") String holiday, @PathParam("acctype") int accType, @PathParam("numcities") int numCities) {

		String sql = "";
		//String sql = "select state, city, latitude, longitud, date_t from accident where date_t is not null";
		if(year.equalsIgnoreCase("ALL")) {
			sql += "select state, city, count(*) as numincidents from accident where date_t is not null and city != 0 ";
		} else {
			 sql += "select state, city, count(*) numincidents from accident where date_t is not null and city !=0 and year = "+year;
		}
		
		if(!holiday.equalsIgnoreCase("None") ){
			String hYear = year;
			if(hYear.equals("All")){
				Set<Integer> yearKeys = TrafficConstants.holidayMaps.keySet();
				int i=0;
				for(int key: yearKeys){
					String holidayDate = TrafficConstants.holidayMaps.get(key).get(holiday);
					if(i==0){
						sql += " and ( date_t = '"+holidayDate+"'";
					}else{
						sql += " or date_t = '"+holidayDate+"'";
					}
					i = i+1;
				}
				sql += ")";
				

			}else{
			
				HashMap<String,String> dates = TrafficConstants.holidayMaps.get(Integer.parseInt(hYear));
				if(dates != null && dates.size() > 0){
					String holidayDate = dates.get(holiday);
					sql += " and date_t = '"+holidayDate+"'";
				}
			}
			
		}
		
		if(accType != 999 ){
			switch(accType){
			case 100:
				sql += " and drunk_dr > 0";
				break;
			case 2: case 3: case 4: case 5: case 6: case 7: case 10: case 11:
				sql += " and weather = "+accType;
				break;
			
			}
		}
	
		sql += " group by state, city order by numincidents desc limit "+ numCities;
		
		
		System.out.println("...sql: "+sql);

	//	String jsonStr = caller.makeQueryJSON(sql);
		String jsonStr = caller.makeQueryJSONFullColumnNames(sql);
		System.out.println(jsonStr);
		return jsonStr;
	}
	public static String convertToXML(ResultSet resultSet) throws SQLException {
		StringBuffer xmlArray = new StringBuffer("<results>");
		int s = resultSet.getFetchSize();
		while (resultSet.next()) {
			int total_rows = resultSet.getMetaData().getColumnCount();
			xmlArray.append("<result ");
			for (int i = 0; i < total_rows; i++) {
				xmlArray.append("< "
						+ resultSet.getMetaData().getColumnLabel(i + 1)
								.toLowerCase() + "='"
						+ resultSet.getObject(i + 1) + "'");
			}
			xmlArray.append(" />");
		}
		xmlArray.append("</results>");
		return xmlArray.toString();
	}
	
	

}
