package com.onmobile.fundesh_load_testing;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class FundeshLoadTesting {
	
	public static Properties prop;

    public static void main(String[] args) {
    	int totalThreadsCount = 20, hitsPerThread = 20, sleepIntervalInMilliSeconds = 500;
    	String outputFilesLocation = "";
    	try {
    		totalThreadsCount = Integer.parseInt(getProperty("THREADS_COUNT"));
    		hitsPerThread = Integer.parseInt(getProperty("HITS_PER_THREAD"));
    		sleepIntervalInMilliSeconds = Integer.parseInt(getProperty("SLEEP_INTERVAL_IN_MILLISECONDS"));
    		outputFilesLocation = getProperty("OUTPUT_FILES_LOCATION");
    	}catch (Exception e) {
			totalThreadsCount = 20;
			hitsPerThread = 20;
			sleepIntervalInMilliSeconds = 500;
		}
    	System.out.println("totalThreadsCount = "+totalThreadsCount+" hitsPerThread = "+hitsPerThread+" sleepIntervalInMilliSeconds = "+sleepIntervalInMilliSeconds);
    	for(int threadsCount=0; threadsCount<totalThreadsCount; threadsCount++) {
    		Thread clientThread = new MultithreadedHttpClient(hitsPerThread,sleepIntervalInMilliSeconds,outputFilesLocation);
    		clientThread.start();
    	}
    	
    }
    
    public static String getProperty(String key) {
    	String value = "";
    	if (prop==null) {
    		try {
    			InputStream input = new FileInputStream("config.properties");
    			prop = new Properties();
    			prop.load(input);
    		}catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	value = prop.getProperty(key);
    	return value;
    }
    

}


