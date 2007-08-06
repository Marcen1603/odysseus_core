package mg.dynaquest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.hp.hpl.jena.rdf.model.RDFException;

import mg.dynaquest.sourcedescription.sdm.RelationalSourceDescriptionManager;

public class LoadAutomarktTestData {

	public static void main(String[] args) throws RDFException,
			MalformedURLException, FileNotFoundException, IOException,
			SQLException, ClassNotFoundException {
		
		
        Logger root = Logger.getRootLogger();
        root.removeAllAppenders();
        root.addAppender(new ConsoleAppender(
                new PatternLayout("[%t] %l %x - %m%n")));
        
        root.setLevel(Level.ALL);
		
		Properties properties = new Properties();
		String propFile = System.getProperty("user.home")
				+ "/DynaQuest.properties";
		properties.load(new FileInputStream(propFile));

		String sdm_user = properties.getProperty("sdm_user");
		String sdm_password = properties.getProperty("sdm_password");
		String sdm_jdbcString = properties.getProperty("sdm_jdbcString");
		String sdm_driverClass = properties.getProperty("sdm_driverClass");
		String sdm_sqlfile = properties.getProperty("sdm_sqlfile");

		RelationalSourceDescriptionManager sdm = RelationalSourceDescriptionManager.getInstance(
				"DynaQuestLoaderSDM",sdm_user, sdm_password, sdm_jdbcString, sdm_driverClass, true, sdm_sqlfile);

		String baseDir = "http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2004/08/";

		sdm.loadRDF(baseDir + "Automarkt2004.sdf", true, true, "");

		for (int i = 1; i <= 4; i++) {
			sdm.loadRDF(
					// baseDir+"Buchsuche2005_Quelle_"
					// + i + ".sdf", false, false, baseDir+"Buchsuche2005.sdf");
					baseDir + "Automarkt2004_Quelle_" + i + ".sdf", false,
					false, baseDir + "Automarkt2004.sdf");
		}

		sdm.loadRDF(baseDir + "Automarkt2004_Quelle_1b" + ".sdf", false, false,
				baseDir + "Automarkt2004.sdf");

		sdm.loadRDF(baseDir + "Automarkt2004_Quelle_1c" + ".sdf", false, false,
				baseDir + "Automarkt2004.sdf");
		sdm.loadRDF(baseDir + "Automarkt2004_Quelle_1d" + ".sdf", false, false,
				baseDir + "Automarkt2004.sdf");
		sdm.loadRDF(baseDir + "Automarkt2004_Quelle_1e" + ".sdf", false, false,
				baseDir + "Automarkt2004.sdf");

		// Zusätzliche Quelle ohne H_Plz als Input
		sdm.loadRDF(baseDir + "Automarkt2004_Quelle_3b" + ".sdf", false, false,
				baseDir + "Automarkt2004.sdf");
		
		// Quellen von Benjamin
		sdm.loadRDF(baseDir + "Automarkt2006Quelle1" + ".sdf", false, false,
				baseDir + "Automarkt2004.sdf");
		sdm.loadRDF(baseDir + "Automarkt2006Quelle2" + ".sdf", false, false,
				baseDir + "Automarkt2004.sdf");
		sdm.loadRDF(baseDir + "Automarkt2006Quelle3" + ".sdf", false, false,
				baseDir + "Automarkt2004.sdf");

		System.out.println("Daten vollständig eingelesen....");
	}
}
