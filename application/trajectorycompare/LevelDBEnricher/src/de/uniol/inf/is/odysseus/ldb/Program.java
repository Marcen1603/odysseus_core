package de.uniol.inf.is.odysseus.ldb;

import static org.fusesource.leveldbjni.JniDBFactory.bytes;
import static org.fusesource.leveldbjni.JniDBFactory.factory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.WriteBatch;

import com.ximpleware.AutoPilot;
import com.ximpleware.NavException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;
import com.ximpleware.XPathEvalException;
import com.ximpleware.XPathParseException;


@SuppressWarnings("all")
public class Program {

	public static void main(String[] args) throws IOException, XPathParseException, NavException, XPathEvalException, ClassNotFoundException {
		
		System.out.print("Enter filename: ");
		
		final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String filename = reader.readLine();
		reader.close();
		
		if(filename.length() == 0) {
			filename = "oldb";
		}		
	
		//randomWrite(filename);
		read(filename, 371);
		//createDB(filename);
		
	}
	
	private static void read(String filename, int id) throws XPathParseException, IOException, ClassNotFoundException {
		
		Options options = new Options();
		options.createIfMissing(false);
		DB db = factory.open(new File(filename), options);
		WriteBatch batch = db.createWriteBatch();
		
		final VTDGen vg = new VTDGen();
		vg.parseFile(filename + ".rand.rou.xml", false);
		
		final VTDNav vn = vg.getNav();
		
		final AutoPilot apVeh = new AutoPilot(vn);
		apVeh.selectXPath("//routes/vehicle");
		
		ByteArrayInputStream in = new ByteArrayInputStream(db.get(bytes(id + "")));
		ObjectInputStream o = new ObjectInputStream(in);
		Object unknown = (Map<String, String>)o.readObject();
		
		System.out.println(unknown.getClass());
		
		Map<String, String> map = (Map<String, String>)unknown;
		
		for(String key : map.keySet()) {
			System.out.println(key + ", " + map.get(key));
		}
	}

	private static void randomWrite(String filename) throws XPathParseException, NavException, IOException, XPathEvalException, ClassNotFoundException {
		FileUtils.deleteDirectory(new File(filename));
		
		Options options = new Options();
		options.createIfMissing(true);
		DB db = factory.open(new File(filename), options);
		WriteBatch batch = db.createWriteBatch();
		
		final VTDGen vg = new VTDGen();
		vg.parseFile(filename + ".rand.rou.xml", false);
		
		final VTDNav vn = vg.getNav();
		
		final AutoPilot apVeh = new AutoPilot(vn);
		apVeh.selectXPath("//routes/vehicle");
		
		final Random random = new Random(1234);
		
		while(apVeh.evalXPath() != -1) {
		    vn.toElement(VTDNav.FIRST_CHILD, "vehicle");
		    
		    final Map<String, String> attributesMap = new HashMap<>();
		    
		    //Zufallswerte schreiben
		    String travelStyle = random.nextInt(4) == 0 ? "grouped" : "independent";
		    attributesMap.put("travel_style", travelStyle);
		    
		    int persons = (int)(Math.abs(random.nextGaussian() * 2) + 1);
		    if(travelStyle.equals("grouped")) {
		    	persons++;
		    }
		    attributesMap.put("persons", persons > 10 ? 10d + "" : (double)persons + "");
		    
		    String transport;
		    int transportVal = random.nextInt(20);
		    if(transportVal == 1) {
		    	transport = "taxi";
		    } else if(transportVal < 9) {
		    	transport = "bus";
		    } else {
		    	transport = "car";
		    }
		    attributesMap.put("transport", transport);
		    
		    attributesMap.put("tollway", (double)random.nextInt(101) + "");
		    
		    attributesMap.put("highway", (double)random.nextInt(101) + "");
		    
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream o = new ObjectOutputStream(out);
			o.writeObject(attributesMap);		
		    
		    batch.put(bytes(vn.toNormalizedString(vn.getAttrVal("id"))), out.toByteArray());
		    
		    out.close();
		    o.close();
		}			
		db.write(batch);
		batch.close();
		
		db.close();
	}
	
	private static void createDB(String filename) throws IOException, XPathParseException, XPathEvalException, NavException, ClassNotFoundException {
		Options options = new Options();
		options.createIfMissing(false);
		DB db = factory.open(new File(filename), options);
		WriteBatch batch = db.createWriteBatch();
		
		final VTDGen vg = new VTDGen();
		vg.parseFile(filename + ".xml", false);
		
		final VTDNav vn = vg.getNav();
		
		final AutoPilot apVeh = new AutoPilot(vn);
		apVeh.selectXPath("//attributes/vehicle");
		
		final AutoPilot apAttr = new AutoPilot(vn);
		
		while(apVeh.evalXPath() != -1) {
		    vn.toElement(VTDNav.FIRST_CHILD, "vehicle");
		    
		    final Map<String, String> attributesMap = new HashMap<>();
		    
		    apAttr.selectXPath("child::attr");
		    while(apAttr.evalXPath() != -1) {
		    	vn.toElement(VTDNav.FIRST_CHILD, "attr");
		    	attributesMap.put(
		    			vn.toNormalizedString(vn.getAttrVal("name")), 
		    			vn.toNormalizedString(vn.getAttrVal("value")));
			}
		    
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream o = new ObjectOutputStream(out);
			o.writeObject(attributesMap);		
		    
		    batch.put(bytes(vn.toNormalizedString(vn.getAttrVal("id"))), out.toByteArray());
		}			
//		db.write(batch);
//		batch.close();
		
		ByteArrayInputStream in = new ByteArrayInputStream(db.get(bytes("6")));
		ObjectInputStream o = new ObjectInputStream(in);
		Object unknown = (Map<String, String>)o.readObject();
		
		System.out.println(unknown.getClass());
		
		Map<String, String> map = (Map<String, String>)unknown;
		
		for(String key : map.keySet()) {
			System.out.println(key + ", " + map.get(key));
		}
		
		db.close();
	}
	
	private static void read(String filename) throws IOException, ClassNotFoundException {
		
		Options options = new Options();
		options.createIfMissing(false);
		DB db = factory.open(new File(filename), options);
		
		
		for(int i = 0; i < 2000; i++) {
			
			System.out.println(i);
			byte[] b = db.get(bytes(i + ""));
			if(b == null) {
				continue;
			}
			
			ByteArrayInputStream in = new ByteArrayInputStream(b);
			ObjectInputStream o = new ObjectInputStream(in);
			Object unknown = (Map<String, String>)o.readObject();
			
			System.out.println(unknown.getClass());
			
			Map<String, String> map = (Map<String, String>)unknown;
			
			for(String key : map.keySet()) {
				System.out.println(key + ", " + map.get(key));
			}
			

		}
		db.close();
	}
}
