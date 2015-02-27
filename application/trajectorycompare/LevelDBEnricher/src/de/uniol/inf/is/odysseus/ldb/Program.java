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

import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.WriteBatch;

import com.ximpleware.AutoPilot;
import com.ximpleware.NavException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;
import com.ximpleware.XPathEvalException;
import com.ximpleware.XPathParseException;


public class Program {

	public static void main(String[] args) throws IOException, XPathParseException, NavException, XPathEvalException, ClassNotFoundException {
		
		System.out.print("Enter filename: ");
		
		final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String filename = reader.readLine();
		reader.close();
		
		if(filename.length() == 0) {
			filename = "textual_attributes";
		}
		
		//FileUtils.deleteDirectory(new File(filename));;
		
		
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
}
