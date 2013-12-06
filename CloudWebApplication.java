package com.bigdata.impala;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/webapi")
public class CloudWebApplication extends Application {
	 @Override
	    public Set<Class<?>> getClasses() {
	        final Set<Class<?>> classes = new HashSet<Class<?>>();
	        // register root resource
	        classes.add(TrafficServer.class);
	        return classes;
	    }

}
