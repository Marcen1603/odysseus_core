package de.uniol.inf.is.odysseus.net.discovery.iplist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.config.OdysseusBaseConfiguration;
import de.uniol.inf.is.odysseus.net.INodeManager;
import de.uniol.inf.is.odysseus.net.discovery.AbstractNodeDiscoverer;

public final class IPListNodeDiscoverer extends AbstractNodeDiscoverer {

	private static final Logger LOG = LoggerFactory.getLogger(IPListNodeDiscoverer.class);
	private static final String IP_LIST_FILENAME = "nodeIPList.conf";
	
	@Override
	public void startImpl(INodeManager manager) {
		LOG.info("Beginning node discovery with ip list from file");
		
		String filename = determineFullFilename();
		LOG.info("File is {}", filename);
		
		Collection<String> ips = tryReadIPsFromFile(filename);
		if( !ips.isEmpty() ) {
			logIPList(ips);
			
			
			
		} else {
			LOG.error("List of ips is empty! Discovering no nodes!");
		}
	}

	private static void logIPList(Collection<String> ips) {
		LOG.info("Got {} ips", ips.size());
		if( LOG.isDebugEnabled() ) {
			for( String ip : ips ) {
				LOG.debug("\t{}", ip);
			}
		}
	}

	private static Collection<String> tryReadIPsFromFile(String filename) {
		LOG.info("Loading file '{}' of list of ips", filename);
		Collection<String> lines = Lists.newLinkedList();
		
	    try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
	        String line = br.readLine();

	        while (line != null) {
	        	lines.add(line);
	            line = br.readLine();
	        }
	    } catch( FileNotFoundException e ) {
	    	LOG.error("File '{}' with list of ips does not exist", filename);
	    } catch( IOException e ) {
	    	LOG.error("Could not read file of ips '{}'", filename, e);
	    }
	    
	    return lines;
	}

	private static String determineFullFilename() {
		return OdysseusBaseConfiguration.getHomeDir() + File.separator + IP_LIST_FILENAME;
	}

	@Override
	public void stopImpl() {
		
	}

}
