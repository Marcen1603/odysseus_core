package de.uniol.inf.is.odysseus.sensors.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class KeyValueFile 
{
	public static KeyValueFile Open(String fileName) throws IOException
	{
		return new KeyValueFile(fileName, true);
	}

	public static KeyValueFile Create(String fileName) throws IOException
	{
		return new KeyValueFile(fileName, false);
	}
	
	private Map<String, String> map;
	private String fileName;
	
	public KeyValueFile(String fileName, boolean mustExist) throws IOException
	{
		this.fileName = fileName;
		map = new HashMap<String, String>();

		try
		{
			BufferedReader br = new BufferedReader(new FileReader(fileName));

			try
			{
				String line;
				while((line = br.readLine()) != null) 
				{
					String[] tokens = line.split("=");
					if (tokens.length != 2) continue;
	        	
					map.put(tokens[0], tokens[1]);
				}
			}
			finally
			{
				br.close();			
			}
		}
		catch (FileNotFoundException e)
		{
			if (mustExist) throw e;
		}
	}
	
	public void save() throws IOException
	{
		BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
		try
		{	
			for (Map.Entry<String, String> entry : map.entrySet())
			{
				bw.write(entry.getKey() + "=" + entry.getValue() + "\n");
			}
		}
		finally
		{
			bw.close();
		}
	}	
	
	public String get(String key)
	{
		return map.get(key);
	}
	
	public void set(String key, String value)
	{
		map.put(key, value);
	}

	public String getFileName() { return fileName; }
}
