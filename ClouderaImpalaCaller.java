package com.bigdata.impala;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ClouderaImpalaCaller {

	// here is an example query based on one of the Hue Beeswax sample tables
	private static final String SQL_STATEMENT = "SELECT * FROM accident limit 10";

	// set the impalad host
	private static final String IMPALAD_HOST = "192.168.204.135";

	// port 21050 is the default impalad JDBC port
	private static final String IMPALAD_JDBC_PORT = "21050";

	private static final String CONNECTION_URL = "jdbc:hive2://" + IMPALAD_HOST
			+ ':' + IMPALAD_JDBC_PORT + "/;auth=noSasl";

	private static final String JDBC_DRIVER_NAME = "org.apache.hive.jdbc.HiveDriver";

	public static String makeQuery(String SQL) {
		System.out.println("Running Query: " + SQL);
		Connection con = null;

		try {

			Class.forName(JDBC_DRIVER_NAME);

			con = DriverManager.getConnection(CONNECTION_URL);

			Statement stmt = con.createStatement();

			ResultSet rs = stmt.executeQuery(SQL);

			return convertToXML(rs);

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				// swallow
			}
		}
		return null;

	}
	
	public static String makeQueryJSON(String SQL) {
		System.out.println("Running Query: " + SQL);
		Connection con = null;

		try {

			Class.forName(JDBC_DRIVER_NAME);

			con = DriverManager.getConnection(CONNECTION_URL);

			Statement stmt = con.createStatement();

			ResultSet rs = stmt.executeQuery(SQL);

			return convertToJSON(rs);

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				// swallow
			}
		}
		return null;

	}
	
	public static String makeQueryJSONFullColumnNames(String SQL) {
		System.out.println("Running Query: " + SQL);
		Connection con = null;

		try {

			Class.forName(JDBC_DRIVER_NAME);

			con = DriverManager.getConnection(CONNECTION_URL);

			Statement stmt = con.createStatement();

			ResultSet rs = stmt.executeQuery(SQL);

			return convertToJSONWithFullColumnNames(rs);

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				// swallow
			}
		}
		return null;

	}
//	public static ResultSet makeQueryResultSet(String SQL) {
//		System.out.println("Running Query: " + SQL);
//		Connection con = null;
//
//		try {
//
//			Class.forName(JDBC_DRIVER_NAME);
//
//			con = DriverManager.getConnection(CONNECTION_URL);
//
//			Statement stmt = con.createStatement();
//
//			ResultSet rs = stmt.executeQuery(SQL);
//
//			return rs;
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				con.close();
//				
//			} catch (Exception e) {
//				// swallow
//			}
//		}
//		return null;
//
//	}
	public static String convertToXML(ResultSet resultSet) throws SQLException {
		
		StringBuffer xmlArray = new StringBuffer("<?xml version=\"1.0\"?><results>");
		int s = resultSet.getFetchSize();
		while (resultSet.next()) {
			int total_rows = resultSet.getMetaData().getColumnCount();
			xmlArray.append("<result>");
			for (int i = 0; i < total_rows; i++) {
				String label = resultSet.getMetaData().getColumnLabel(i + 1)
						.toLowerCase();
				xmlArray.append("<"+label+">");
				xmlArray.append(resultSet.getObject(i + 1));
				xmlArray.append("</"+label+">");
				
			}
			xmlArray.append("</result>");
		}
		xmlArray.append("</results>");
		return xmlArray.toString();
	}
	
	private static class NameValue{
		String name;
		String value;
	}
	
	public static String getState(String state){
		String sql = "select name from locationcode where state = '"+state+"'";
		String jsonStr = makeQueryJSON(sql);
		Gson gson = new Gson();
		JsonElement element = new JsonParser().parse(jsonStr);
		
//		String ret_state = element.getAsJsonObject().get("name").toString();
		HashMap<String, String> map = gson.fromJson(element.getAsJsonArray().get(0), new HashMap<String,String>().getClass());
		String retState = map.get("name");
		return retState;
		
	}
	
	public static String getCounty(String state, String county){
		String sql = "select name from locationcode where state = '"+state+"' && county = "+county;
		String jsonStr = makeQueryJSON(sql);
		Gson gson = new Gson();
		JsonElement element = new JsonParser().parse(jsonStr);
		
//		String ret_state = element.getAsJsonObject().get("name").toString();
		HashMap<String, String> map = gson.fromJson(element.getAsJsonArray().get(0), new HashMap<String,String>().getClass());
		String ret = map.get("name");
		return ret;
		
	}
	
	public static String getCity(String state, String city){
		String sql = "select name from locationcode where state = '"+state+"' && city = "+city;
		String jsonStr = makeQueryJSON(sql);
		Gson gson = new Gson();
		JsonElement element = new JsonParser().parse(jsonStr);
		
//		String ret_state = element.getAsJsonObject().get("name").toString();
		HashMap<String, String> map = gson.fromJson(element.getAsJsonArray().get(0), new HashMap<String,String>().getClass());
		String ret = map.get("name");
		return ret;
		
	}

public static String convertToJSON(ResultSet rs) throws SQLException {
	
		
		StringBuffer xmlArray = new StringBuffer("<?xml version=\"1.0\"?><results>");
		int s = rs.getFetchSize();
		JsonArray array = new JsonArray();
		while (rs.next()) {
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			
			 JsonObject obj = new JsonObject();
						 
			  for( int i=1; i<columnCount+1; i++) {
			    String column_name = rsmd.getColumnName(i);

			    switch( rsmd.getColumnType( i ) ) {
//			      case java.sql.Types.ARRAY:
//			        obj.add(column_name, new JsonElement(rs.getArray(column_name)));     break;
			      case java.sql.Types.BIGINT:
			    	
			    		  
			        obj.addProperty(column_name, rs.getInt(column_name));       break;
			      case java.sql.Types.BOOLEAN:
			        obj.addProperty(column_name, rs.getBoolean(column_name));   break;
			      case java.sql.Types.BLOB:
			        obj.addProperty(column_name, rs.getBlob(column_name).toString());      break;
			      case java.sql.Types.DOUBLE:
			        obj.addProperty(column_name, rs.getDouble(column_name));    break;
			      case java.sql.Types.FLOAT:
			        obj.addProperty(column_name, rs.getFloat(column_name));     break;
			      case java.sql.Types.INTEGER:
			        obj.addProperty(column_name, rs.getInt(column_name));       break;
			      case java.sql.Types.NVARCHAR:
			        obj.addProperty(column_name, rs.getNString(column_name));   break;
			      case java.sql.Types.VARCHAR:
			        obj.addProperty(column_name, rs.getString(column_name));    break;
			      case java.sql.Types.TINYINT:
			        obj.addProperty(column_name, rs.getInt(column_name));       break;
			      case java.sql.Types.SMALLINT:
			        obj.addProperty(column_name, rs.getInt(column_name));       break;
			      case java.sql.Types.DATE:
			        obj.addProperty(column_name, rs.getDate(column_name).toString());      break;
			      case java.sql.Types.TIMESTAMP:
			        obj.addProperty(column_name, rs.getTimestamp(column_name).toString()); break;
			      default:
			        obj.addProperty(column_name, rs.getObject(column_name).toString());    break;
			    }
			  }

			  array.add(obj);
			}
				
		return array.toString();
	}

public static String convertToJSONWithFullColumnNames(ResultSet rs) throws SQLException {
	
	JsonArray array = new JsonArray();
	
	while (rs.next()) {
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		
		 JsonObject obj = new JsonObject();
		 String stateName = "";
		 
		  for( int i=1; i<columnCount+1; i++) {
		    String column_name = rsmd.getColumnName(i);
		    System.out.print(":"+column_name);
		    String fullColumnName = TrafficConstants.dbToUi.get(column_name.toUpperCase());
		    System.out.print(":"+fullColumnName);
		    if(column_name.equalsIgnoreCase("numincidents")){
		    	fullColumnName = column_name;
		    }
		    if(fullColumnName == null){
		    	continue;
		    }
		    
		    System.out.print(":"+fullColumnName);
		    switch( rsmd.getColumnType( i ) ) {
//		      case java.sql.Types.ARRAY:
//		        obj.add(column_name, new JsonElement(rs.getArray(column_name)));     break;
		      case java.sql.Types.BIGINT:
		    	  obj.addProperty(fullColumnName, rs.getInt(column_name));
		    	  
		    	  break;
		      case java.sql.Types.BOOLEAN:
		        obj.addProperty(fullColumnName, rs.getBoolean(column_name));   
		        break;
		      case java.sql.Types.BLOB:
		        obj.addProperty(fullColumnName, rs.getBlob(column_name).toString());      
		        break;
		      case java.sql.Types.DOUBLE:
		        obj.addProperty(fullColumnName, rs.getDouble(column_name));    
		        break;
		      case java.sql.Types.FLOAT:
		        obj.addProperty(fullColumnName, rs.getFloat(column_name));     
		        break;
		      case java.sql.Types.INTEGER:
		    	  String val;
		    	  if(column_name.equalsIgnoreCase("numincidents")){
		    		  obj.addProperty(fullColumnName, rs.getInt(column_name));
		    	  }else if(column_name.equalsIgnoreCase("CITY")){
		    		 
		    		  int cityCode = rs.getInt(column_name);
		    		 
		    		  if(!stateName.equals("") && cityCode != 0){
		    			  String cityName = getCity(stateName, ""+cityCode);
		    			  obj.addProperty(fullColumnName, cityName);
		    		  }
		    		 
		    	  }else{
			    	  
		    		  val = getStringValues(column_name, rs.getInt(column_name));
			    	  if(val == null || val == ""){
			    		  obj.addProperty(fullColumnName, rs.getInt(column_name));       
			    	  }else{
			    		  obj.addProperty(fullColumnName, val);
			    		  if(column_name.equalsIgnoreCase("state")){
			    			  stateName = val;
			    		  }
			    	  }
		    	  
		    	  }
		    	  
		          break;
		      case java.sql.Types.NVARCHAR:
		        obj.addProperty(fullColumnName, rs.getNString(column_name));   
		        break;
		      case java.sql.Types.VARCHAR:
		        obj.addProperty(fullColumnName, rs.getString(column_name));    
		        break;
		      case java.sql.Types.TINYINT:
		        obj.addProperty(fullColumnName, rs.getInt(column_name));       
		        break;
		      case java.sql.Types.SMALLINT:
		        obj.addProperty(fullColumnName, rs.getInt(column_name));       
		        break;
		      case java.sql.Types.DATE:
		        obj.addProperty(fullColumnName, rs.getDate(column_name).toString());      
		        break;
		      case java.sql.Types.TIMESTAMP:
		        obj.addProperty(fullColumnName, rs.getTimestamp(column_name).toString()); 
		        break;
		      default:
		        obj.addProperty(fullColumnName, rs.getObject(column_name).toString());    
		        break;
		    }
		  }

		  array.add(obj);
		}
			
	return array.toString();
}

public static String getStringValues(String colName, int key){
	String retVal = "";
	if(colName.equalsIgnoreCase("state")){
		retVal = getState(Integer.toString(key));
	}
	else if(colName.equalsIgnoreCase("LGT_COND")){
		retVal = TrafficConstants.lightConditionsMap.get(key);
	}else if(colName.equalsIgnoreCase("WEATHER1") || colName.equalsIgnoreCase("WEATHER2") || colName.equalsIgnoreCase("WEATHER")){
		retVal = TrafficConstants.weatherConditionsMap.get(key);
	}else if(colName.equalsIgnoreCase("CF1") || colName.equalsIgnoreCase("CF2") || colName.equalsIgnoreCase("CF3")){
		retVal = TrafficConstants.relatedConditionsMap.get(key);
	}else if(colName.equalsIgnoreCase("SCH_BUS")){
		switch(key){
		case 0:
			return "No"; 
		case 1:
			return "Yes, School bus involved";
		default:
			return "Unreported";
					
		}
	}else if(colName.equalsIgnoreCase("RAIL")){
		switch(key){
		case 0000000:
			return "Not Applicable"; 
		case 9999999:
			return "Unknown";
		default:
			return ""+key;
					
		}
	}else if(colName.equalsIgnoreCase("NHS")){
		switch(key){
			case 0 : return "Not on National Highway";
			case 1: return "On a National Highway";
			case 9: return "Unknown";
		}
	}
	return retVal;
	
}
	public static void main(String[] args) {

		System.out.println("\n=============================================");
		System.out.println("Cloudera Impala JDBC Example");
		System.out.println("Using Connection URL: " + CONNECTION_URL);
		System.out.println("Running Query: " + SQL_STATEMENT);

		Connection con = null;

		try {

//			Class.forName(JDBC_DRIVER_NAME);
//
//			con = DriverManager.getConnection(CONNECTION_URL);
//
//			Statement stmt = con.createStatement();
//
//			ResultSet rs = stmt.executeQuery(SQL_STATEMENT);
//
//			System.out
//					.println("\n== Begin Query Results ======================");
//
//			// print the results to the console
//			while (rs.next()) {
//				// the example query returns one String column
//				System.out.println(rs.getString("latitude") + ","
//						+ rs.getString("longitud"));
//			}
//
//			System.out
//					.println("== End Query Results =======================\n\n");
			String state = ClouderaImpalaCaller.getState("6");

		} catch (Exception e) {
			e.printStackTrace();
		} 
			finally {
//			try {
//				con.close();
//			} catch (Exception e) {
//				// swallow
//			}
		}
	}
