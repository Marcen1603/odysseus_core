package de.uniol.inf.is.odysseus.iql.odl.ui.wizard;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.io.IOUtils;

import de.uniol.inf.is.odysseus.iql.odl.ui.internal.ODLActivator;


public class ODLTemplateReader {

	public static String readTemplate(URL url) {
		try (InputStream inputStream = url.openStream()) {
		    return IOUtils.toString(inputStream);
		} catch (Exception e) {			
		}
		return "";
	}
	
	public static String readTemplate(String path) {
		try (InputStream inputStream = ODLActivator.getInstance().getBundle().getEntry(path).openStream()) {
		    return IOUtils.toString(inputStream);
		} catch (Exception e) {			
		}
		return "";
	}
	
	public static List<IODLTemplate> readTemplates() {
		List<IODLTemplate> result = new ArrayList<>();
		Enumeration<URL> entries = ODLActivator.getInstance().getBundle().findEntries("/templates/", "*.odl", true);
		while(entries.hasMoreElements()) {
			URL url = entries.nextElement();
			final String text = readTemplate(url);
			final String operator = url.getFile().substring(url.getFile().lastIndexOf('/')+1, url.getFile().lastIndexOf('.'));
			result.add(new IODLTemplate() {
				
				@Override
				public String getText() {
					return text;
				}
				
				@Override
				public String getName() {
					return operator;
				}
				
				@Override
				public String getDescription() {
					return "Template for "+operator+" operator";
				}
			});
		}
		return result;
	}

}
