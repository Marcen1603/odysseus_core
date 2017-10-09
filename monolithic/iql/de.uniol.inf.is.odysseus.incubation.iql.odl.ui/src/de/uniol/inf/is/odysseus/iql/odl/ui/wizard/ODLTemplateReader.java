package de.uniol.inf.is.odysseus.iql.odl.ui.wizard;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.incubation.iql.odl.ui.internal.OdlActivator;



public class ODLTemplateReader {
	
	private static final Logger LOG = LoggerFactory.getLogger(ODLTemplateReader.class);


	public static String readTemplate(URL url) {
		try (InputStream inputStream = url.openStream()) {
		    return IOUtils.toString(inputStream);
		} catch (Exception e) {	
			LOG.error("Could not read template "+url.toString(), e);
		}
		return "";
	}
	
	public static String readTemplate(String path) {
		try (InputStream inputStream = OdlActivator.getInstance().getBundle().getEntry(path).openStream()) {
		    return IOUtils.toString(inputStream);
		} catch (Exception e) {			
			LOG.error("Could not read template "+path, e);
		}
		return "";
	}
	
	public static List<IODLTemplate> readTemplates(Bundle bundle) {
		List<IODLTemplate> result = new ArrayList<>();
		Enumeration<URL> entries = bundle.findEntries("/odl_templates/", "*.odl", true);
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
