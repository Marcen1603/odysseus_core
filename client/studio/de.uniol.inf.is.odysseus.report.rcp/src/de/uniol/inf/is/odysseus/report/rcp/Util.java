package de.uniol.inf.is.odysseus.report.rcp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class Util {
	
	private static final Logger LOG = LoggerFactory.getLogger(Util.class);
	
	public static void loadFromFile(final StringBuilder report, Path path, String title) {

		if (Files.exists(path)) {
			try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
				report.append("\t* " + title + ":\n");
				String line = "";
				while ((line = reader.readLine()) != null) {
					report.append(line).append("\n");
				}
				report.append("\n");
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}
	
	public static List<String> read(File file) throws IOException  {
		BufferedReader br = new BufferedReader(new FileReader(file));
		List<String> result = Lists.newArrayList();
	    try {
	    	
	        String line = br.readLine();

	        while (line != null) {
	            result.add(line);
	            line = br.readLine();
	        }
	    } finally {
	        br.close();
	    }
	    
	    return result;
	}

}
