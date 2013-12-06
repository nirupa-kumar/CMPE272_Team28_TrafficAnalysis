package com.bigdata.impala;

import java.util.HashMap;

public class TrafficConstants {

	public static final String[] States = { "ALABAMA", "ALASKA", "ARIZONA",
			"ARKANSAS", "CALIFORNIA", "COLORADO", "CONNECTICUT",
			"DISTRICT OF COLUMBIA", "DELAWARE", "FLORIDA", "GEORGIA", "HAWAII",
			"IDAHO", "ILLINOIS", "INDIANA", "IOWA", "KANSAS", "KENTUCKY",
			"LOUISIANA", "MAINE", "MARYLAND", "MASSACHUSETTS", "MICHIGAN",
			"MINNESOTA", "MISSISSIPPI", "MISSOURI", "MONTANA", "NEBRASKA",
			"NEVADA", "NEW HAMPSHIRE", "NEW JERSEY", "NEW MEXICO", "NEW YORK",
			"NORTH CAROLINA", "NORTH DAKOTA", "OHIO", "OKLAHOMA", "OREGON",
			"PENNSYLVANIA", "RHODE ISLAND", "SOUTH CAROLINA", "SOUTH DAKOTA",
			"TENNESSEE", "TEXAS", "UTAH", "VERMONT", "VIRGINIA", "WASHINGTON",
			"WEST VIRGINIA", "WISCONSIN", "WYOMING" };

	public static HashMap<String, String> dbToUi = new HashMap<String, String>();
	public static HashMap<Integer, String> lightConditionsMap = new HashMap<Integer, String>();
	public static HashMap<Integer, String> weatherConditionsMap = new HashMap<Integer, String>();
	public static HashMap<Integer, String> relatedConditionsMap = new HashMap<Integer, String>();
	public static HashMap<Integer, HashMap<String, String>> holidayMaps = new HashMap<Integer, HashMap<String, String>>();

	static {

		dbToUi.put("VE_FORMS", "Number of vehicles");
		dbToUi.put("PVH_INVL", "Number of parked vehicles in crash");
		dbToUi.put("PERNOTMVIT", "Number of Persons Not in Motor Vehicles");
		dbToUi.put("PERMVIT", "Number of Persons in Motor Vehicles");
		dbToUi.put("PERSONS", "Total Number of Persons in Accident");
		dbToUi.put("HARM_EV", "First Harmful Event");
		dbToUi.put("COUNTY", "County");
		dbToUi.put("STATE", "State");
		dbToUi.put("CITY", "City");
		dbToUi.put("DAY", "Day");
		dbToUi.put("MONTH", "Month");
		dbToUi.put("YEAR", "Year");
		dbToUi.put("DAY_OF_WEEK", "Day of Week");
		dbToUi.put("HOUR", "Hour");
		dbToUi.put("MINUTE", "Minute");
		dbToUi.put("NHS", "National Highway System?");
		dbToUi.put("ROUTE", "Route Signing");
		dbToUi.put("TWAY_ID", "Traffic Way ID");
		dbToUi.put("TWAY_ID2", "Traffic Way ID");
		dbToUi.put("LATITUDE", "Latitude");
		dbToUi.put("LONGITUD", "Longitude");
		dbToUi.put("LGT_COND", "Light Condition");
		dbToUi.put("WEATHER1", "Weather Condition 1");
		dbToUi.put("WEATHER2", "Weather Condition 2");
		dbToUi.put("WEATHER", "Weather Condition");
		dbToUi.put("SCH_BUS", "School Bus?");
		dbToUi.put("RAIL", "Near Railway Crossing");
		dbToUi.put("NOT_HOUR",
				"Hour of notification to Emergency Medical Services");
		dbToUi.put("NOT_MIN",
				"Minute of notification to Emergency Medical Services");
		dbToUi.put("ARR_HOUR", "Hour of Arrival of Emergency Medical Services");
		dbToUi.put("ARR_MIN", "Minute of Arrival of Emergency Medical Servicel");
		dbToUi.put("HOSP_HR", "Hour of EMS Arrival at Hospital ");
		dbToUi.put("HOSP_MN", "Minute of EMS Arrival at Hospital");
		dbToUi.put("CF1", "Related factors for Crash");
		dbToUi.put("CF2", "Related factors for Crash");
		dbToUi.put("CF3", "Related factors for Crash");
		dbToUi.put("FATALS", "Number of fatalities");
		dbToUi.put("DRUNK_DR", "Number of Drunk Drivers involved Crash");
		dbToUi.put("DATE_T", "Date");

		// 1- DayLight, 2- Dark, 3- Darl-Lighted, 4 - Dawn, 5- Dusk, 6 Dark -
		// Unknown lighting, 7 -Others, 8- unreported, 9-unknown
		lightConditionsMap.put(1, "DayLight");
		lightConditionsMap.put(2, "Dark");
		lightConditionsMap.put(3, "Dark But Lighted");
		lightConditionsMap.put(4, "Dawn");
		lightConditionsMap.put(5, "Dusk");
		lightConditionsMap.put(6, "Dark - Unknown Lighting");
		lightConditionsMap.put(7, "Others");
		lightConditionsMap.put(8, "Unreported");
		lightConditionsMap.put(9, "Unknown");

		// 01-Clear, 0 or 00 - No additional atmospheric conditions, 1 -
		// clear/cloud, 02- Rain, 3 or 03 - Sleet,
		// Hail (Freezing Rain or Drizzle) , 4 or 04 - Snow, 5 or 05 - Fog,
		// Smog, Smoke, 6 or 06 - severe crosswinds,
		// 7 or 07 - Blowing Sand, Soil, Dirt, 8 or 08 - Other, 10 -Cloudy, 11 -
		// Blowing Snow, 98 - Unreported, 99 -Unknown.

		weatherConditionsMap.put(1, "Clear");
		weatherConditionsMap.put(0, "No additional atmospheric conditions");
		weatherConditionsMap.put(1, "Clear/Clouds");
		weatherConditionsMap.put(2, "Rain");
		weatherConditionsMap.put(3, " Sleet, Hail (Freezing Rain or Drizzle)");
		weatherConditionsMap.put(4, "Snow");
		weatherConditionsMap.put(5, "Fog, Smog, Smoke");
		weatherConditionsMap.put(6, "Severe Crosswinds");
		weatherConditionsMap.put(7, "Blowing Sand, Soil, Dirt");
		weatherConditionsMap.put(8, "Other");
		weatherConditionsMap.put(9, "Unknown");
		weatherConditionsMap.put(10, "Cloudy");
		weatherConditionsMap.put(11, "Blowing Snow");
		weatherConditionsMap.put(98, "Unreported");
		weatherConditionsMap.put(99, "Unknown");

		// 00 None
		// 01 Inadequate Warning of Exits, Lanes Narrowing, Traffic Controls
		// etc.
		// 02 Shoulder Related (Design or Condition Since 2002)
		// 03 Other Construction – Created Condition
		// 04 No or Obscured Pavement Marking
		// 05 Surface Under Water
		// 06 Inadequate Construction or Poor Design of Roadway, Bridge, etc.
		// 07 Surface Washed Out (Caved in, Road Slippage)
		// 13 Aggressive Driving/Road Rage by Non-Contact Vehicle Driver (Since
		// 2006)
		// 14 Motor Vehicle (In Transport 1983-2004 Only) Struck By Falling
		// Cargo or Something
		// That Came Loose From or Something That Was Set in Motion By a Vehicle
		// (Since
		// 1983)
		// 15 Non-Occupant Struck By Falling Cargo, or Something Came Loose From
		// or
		// Something That Was Set In Motion By A Vehicle (Since 1983)
		// 16 Non-Occupant Struck Vehicle (Since 1983)
		// 17 Vehicle Set In Motion By Non-Driver (Since 1983)
		// 18 Date of Crash and Date of EMS Notification Were Not Same Day
		// (Since 1988)
		// 19 Recent Previous Crash Scene Nearby (Since 1989)
		// 20 Police-Pursuit-Involved (Since 1994)
		// 21 Within Designated School Zone (Since 1995)
		// 22 Speed Limit Is a Statutory Limit as Recorded or Was Determined as
		// This State’s
		// “Basic Rule” (Since 1999)
		// 23 Indication of a Stalled/Disabled Vehicle (Since 2008)
		// 99 Unknown

		relatedConditionsMap.put(0, "None");
		relatedConditionsMap
				.put(1,
						"Inadequate Warning of Exits, Lanes Narrowing, Traffic Controls etc.");
		relatedConditionsMap.put(2, "Shoulder Related (Design or Condition)");
		relatedConditionsMap.put(3, "Other Construction – Created Condition");
		relatedConditionsMap.put(4, "No or Obscured Pavement Marking");
		relatedConditionsMap.put(5, "Surface Under Water");
		relatedConditionsMap
				.put(6,
						"Inadequate Construction or Poor Design of Roadway, Bridge, etc.");
		relatedConditionsMap.put(7,
				"Surface Washed Out (Caved in, Road Slippage)");
		relatedConditionsMap.put(13,
				"Aggressive Driving/Road Rage by Non-Contact Vehicle Driver");
		relatedConditionsMap
				.put(14,
						"Motor Vehicle Struck By Falling Cargo or Something That Came Loose From or Something That Was Set in Motion By a Vehicle");
		relatedConditionsMap
				.put(15,
						"Non-Occupant Struck By Falling Cargo, or Something Came Loose From or Something That Was Set In Motion By A Vehicle");
		relatedConditionsMap.put(16, "Non-Occupant Struck Vehicle");
		relatedConditionsMap.put(17, "Vehicle Set In Motion By Non-Driver");
		relatedConditionsMap.put(18,
				"Date of Crash and Date of EMS Notification Were Not Same Day");
		relatedConditionsMap.put(19, "Recent Previous Crash Scene Nearby");
		relatedConditionsMap.put(20, "Police-Pursuit-Involved");
		relatedConditionsMap.put(21, "Within Designated School Zone");
		relatedConditionsMap
				.put(22,
						"Speed Limit Is a Statutory Limit as Recorded or Was Determined as This State’s “Basic Rule”");
		relatedConditionsMap
				.put(23, "Indication of a Stalled/Disabled Vehicle");
		relatedConditionsMap.put(99, "Unknown");

		HashMap<String, String> map = new HashMap<String, String>();

		map.put("New Year's Day", "2007-01-01");
		map.put("Memorial Day", "2007-05-28");
		map.put("Independence Day", "2007-07-04");
		map.put("Labor Day", "2007-09-03");
		map.put("Veteran's Day", "2007-11-12");
		map.put("Thanksgiving", "2007-11-22");
		map.put("Christmas", "2007-12-25");
		map.put("New Year's Eve", "2007-12-31");
		
		holidayMaps.put(2007, map);

		map.clear();
		
		map.put("New Year's Day", "2008-01-01");
		map.put("Memorial Day", "2008-05-26");
		map.put("Independence Day", "2008-07-04");
		map.put("Labor Day", "2008-09-01");
		map.put("Veteran's Day", "2008-11-12");
		map.put("Thanksgiving", "2008-11-27");
		map.put("Christmas", "2008-12-25");
		map.put("New Year's Eve", "2008-12-31");
		
		holidayMaps.put(2008, map);

		map.clear();

		map.put("New Year's Day", "2009-01-01");
		map.put("Memorial Day", "2009-05-25");
		map.put("Independence Day", "2009-07-04");
		map.put("Labor Day", "2009-09-07");
		map.put("Veteran's Day", "2009-11-11");
		map.put("Thanksgiving", "2009-11-26");
		map.put("Christmas", "2009-12-25");
		map.put("New Year's Eve", "2009-12-31");
		holidayMaps.put(2009, map);

		map.clear();

		map.put("New Year's Day", "2010-01-01");
		map.put("Memorial Day", "2010-05-31");
		map.put("Independence Day", "2010-07-04");
		map.put("Labor Day", "2010-09-06");
		map.put("Veteran's Day", "2010-11-11");
		map.put("Thanksgiving", "2010-11-25");
		map.put("Christmas", "2010-12-25");
		map.put("New Year's Eve", "2010-12-31");

		holidayMaps.put(2010, map);

		map.clear();

		map.put("New Year's Day", "2011-01-01");
		map.put("Memorial Day", "2011-05-30");
		map.put("Independence Day", "2011-07-04");
		map.put("Labor Day", "2011-09-05");
		map.put("Veteran's Day", "2011-11-11");
		map.put("Thanksgiving", "2011-11-24");
		map.put("Christmas", "2011-12-25");
		map.put("New Year's Eve", "2011-12-31");
		holidayMaps.put(2011, map);
	}

}
