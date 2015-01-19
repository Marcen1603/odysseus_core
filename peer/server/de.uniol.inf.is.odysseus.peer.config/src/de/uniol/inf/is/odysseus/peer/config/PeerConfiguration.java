package de.uniol.inf.is.odysseus.peer.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;

public class PeerConfiguration {
	
	private static final Logger LOG = LoggerFactory.getLogger(PeerConfiguration.class);
	
	private static final String ODYSSEUS_HOME_ENV = "ODYSSEUS_HOME";
	public static final String ODYSSEUS_DEFAULT_HOME_DIR = determineOdysseusDefaultHome();
	public static final String ODYSSEUS_HOME_DIR = determineOdysseusHome();
	public static final String ODYSSEUS_P2P_CONFIGURATION_FILE = ODYSSEUS_HOME_DIR + "odysseusP2P.conf";

	private static final Properties properties = new Properties();
	private static boolean isFirstLoaded = false;

	private PeerConfiguration() {
		// no instance allowed
	}
	
	private static String determineOdysseusDefaultHome() {
		return String.format("%s" + File.separator + "%sodysseus" + File.separator, System.getProperty("user.home"), getDot(System.getProperty("os.name")));
	}
	
	private static String getDot(String os) {
		os = os.toLowerCase();
		if ((os.indexOf("win") >= 0)) {
			return "";
		} else if ((os.indexOf("mac") >= 0)) {
			return ".";
		} else if ((os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0)) {
			return ".";
		} else {
			return "";
		}
	}

	private static String determineOdysseusHome() {
		try {
			String homeDir = System.getenv(ODYSSEUS_HOME_ENV);
			if (Strings.isNullOrEmpty(homeDir)) {
				return ODYSSEUS_DEFAULT_HOME_DIR;
			}
			return homeDir;
		} catch (Exception e) {
			return ODYSSEUS_DEFAULT_HOME_DIR;
		}
	}

	private static void loadConfiguration() {
		try {
			File confFile = openOrCreateFile(ODYSSEUS_P2P_CONFIGURATION_FILE);
			
			FileInputStream in = new FileInputStream(confFile);
			try {
				setDefaultValues(properties);
				try {
					properties.loadFromXML(in);
				} catch( Throwable t ) {
					LOG.debug("Could not load configuration contents", t);
				}
				save();
				
			} finally {
				in.close();
				
				LOG.info("Loaded p2p configuration file");
			}
		} catch( InvalidPropertiesFormatException ex ) {
			LOG.warn("Could not load configuration file '" + ODYSSEUS_P2P_CONFIGURATION_FILE + "'", ex);
		} catch (IOException ex) {
			LOG.warn("Could not load configuration file '" + ODYSSEUS_P2P_CONFIGURATION_FILE + "'", ex);
		} 
	}

	private static void setDefaultValues(Hashtable<Object,Object> props) {
		setDefaultValue("peer.discovery.startinterval", "6000");
		setDefaultValue("peer.discovery.interval", "30000");
		
		setDefaultValue("peer.distribute.bidprovider", "costmodel");
		
		setDefaultValue("peer.log", "false");
		
		Optional<String> optAddress = getRealInetAddress();
		setDefaultValue("peer.name", optAddress.isPresent() ? optAddress.get() : "OdysseusPeer"); //
		setDefaultValue("peer.group.name", "OdysseusPeerGroup");
		setDefaultValue("peer.rdv.address", "");
		setDefaultValue("peer.rdv.active", "false");
		setDefaultValue("peer.port", String.valueOf(determineRandomPort()));
	}
	
	// this works even with linux
	public static Optional<String> getRealInetAddress() {
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()){
			    NetworkInterface current = interfaces.nextElement();
			    if (!current.isUp() || current.isLoopback() || current.isVirtual()) {
			    	continue;
			    }
			    
			    Enumeration<InetAddress> addresses = current.getInetAddresses();
			    while (addresses.hasMoreElements()){
			        InetAddress current_addr = addresses.nextElement();
			        if (current_addr.isLoopbackAddress()) {
			        	continue;
			        }
			        
			        if( current_addr instanceof Inet4Address ) {
			        	return Optional.of(current_addr.getHostAddress());
			        }
			    }
			}
		} catch( Throwable t ) {
			LOG.debug("Could not determine ip-address", t);
		}
		
		return Optional.absent();
	}

	private static int determineRandomPort() {
		return new Random().nextInt(20000) + 10000;
	}

	private static void setDefaultValue(String key, String defaultValue) {
		if( OdysseusConfiguration.exists(key)) {
			String valueFromOdysseusConfig = OdysseusConfiguration.get(key);
			if( !Strings.isNullOrEmpty(valueFromOdysseusConfig)) {
				properties.put(key, valueFromOdysseusConfig);
			} else {
				properties.put(key, defaultValue);
			}
		} else {
			properties.put(key, defaultValue);
		}
	}

	public static void save() {
		try {
			FileOutputStream out = new FileOutputStream(ODYSSEUS_P2P_CONFIGURATION_FILE);
			try {
				properties.storeToXML(out, "Odysseus Property File edit only if you know what you are doing");
			} finally {
				try {
					out.close();
				} catch (IOException ex) {
				}
			}
			
		} catch (IOException ex) {
			LOG.error("Could not save configuration file '" + ODYSSEUS_P2P_CONFIGURATION_FILE + "'", ex);
		} 
	}
	
	private static File openOrCreateFile(String path) throws IOException {
		File f = new File(path);
		boolean success = false;
		if (!f.exists()) {
			File d = f.getParentFile();
			if (d != null) {
				success = d.mkdirs();
			}
			success = f.createNewFile();
			if (success) {
				LOG.debug("Created new p2p configuration file " + f.getAbsolutePath());
			}
		} else {
			LOG.debug("Read from file " + f.getAbsolutePath());
		}
		return f;
	}

	public static void set(String key, String value) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(key), "SettingKey must not be null or empty!");

		firstLoadIfNeeded();
		
		String oldValue = (String)properties.get(key);
		if (oldValue == null || !oldValue.equals(value)) {
			properties.put(key, value);
			save();
		}
	}

	public static Optional<String> get(String key) {
		firstLoadIfNeeded();
		
		Object value = properties.get(key);
		if( value == null ) {
			// backwards compatible
			value = OdysseusConfiguration.get(key);
			
			if( value != null ) {
				set(key, (String)value);
			}
		}
		
		return Optional.fromNullable((String)value);
	}

	private static void firstLoadIfNeeded() {
		if( !isFirstLoaded ) {
			loadConfiguration();
			isFirstLoaded = true;
		}
	}

	public static String get(String key, String defaultValue) {
		Optional<String> optValue = get(key);
		return optValue.isPresent() ? optValue.get() : defaultValue;
	}

	public static boolean exists(String key) {
		firstLoadIfNeeded();

		return properties.containsKey(key);
	}
	
	public static Collection<String> getKeys() {
		firstLoadIfNeeded();
		List<String> keys = Lists.newArrayList();
		for( Object key : properties.keySet() ) {
			keys.add(key.toString());
		}
		return keys;
	}
}