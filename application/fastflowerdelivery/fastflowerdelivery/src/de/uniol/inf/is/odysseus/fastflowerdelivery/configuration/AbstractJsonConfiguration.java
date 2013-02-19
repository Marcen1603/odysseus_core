package de.uniol.inf.is.odysseus.fastflowerdelivery.configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * This is an abstract base class for configuration files.
 * Loading a configuration file via load() method will try
 * to find the file, read it and convert the json content
 * to the instance of the derived class.
 * If the file does not exist, the method will try to create
 * a new configuration file using the default values set in the
 * derived class.
 *
 * @author Weert Stamm
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
abstract public class AbstractJsonConfiguration<T extends AbstractJsonConfiguration> {
	
	/**
	 * Loads the configuration file from a certain location.
	 * If the file does not exist, this method will try to
	 * create a default configuration.
	 * 
	 * @param location
	 * 			the location of the configuration file.
	 * @return the
	 */
	@SuppressWarnings("unchecked")
	public T load(String location) {
		T result = null;
		
		File file = null;
		StringBuffer content = new StringBuffer();
		BufferedReader reader = null;
		
		try {
			file = new File(location);
			reader = new BufferedReader(new FileReader(file));
			String s = null;
			
			while ((s = reader.readLine()) != null)
				content.append(s).append(System.getProperty("line.separator"));
			
			result = (T)new Gson().fromJson(content.toString(), this.getClass());
			
		} catch (FileNotFoundException e) {
			// If file does not exist, create it and then load again
			save(location);
			return load(location);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return result;
	}

	/**
	 * Tries to create a default configuration file using the
	 * default values from fields in this (derived) class.
	 * @param location
	 * 			the location at which the configuration file should be created
	 */
	private void save(String location) {
		PrintWriter out = null;
		try {
			String json = new GsonBuilder().setPrettyPrinting().create().toJson(this, this.getClass());
            out = new PrintWriter(new FileWriter(location));
            out.print(json);
        } catch (IOException ex){
            throw new RuntimeException("A new config file could not be created at " + location, ex);
        } catch (Exception ex){
        	throw new RuntimeException("A new config file could not be created", ex);
        } finally {
        	try {
				if (out != null)
					out.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
        }
		System.out.println("Configuration created");
		// Try to load again
		load(location);
	}
	
}
