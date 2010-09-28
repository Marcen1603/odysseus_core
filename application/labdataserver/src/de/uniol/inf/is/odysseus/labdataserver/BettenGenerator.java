package de.uniol.inf.is.odysseus.labdataserver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BettenGenerator {

	public static void main(String[] args) throws IOException {
		for (int bett=1;bett<=10;bett++){
			FileWriter writer = new FileWriter(new File("labdata_cfg/ideaal_bett"+bett+".csv"));
			System.out.println("Bett "+bett);
			writer.write("timestamp;id;weight0;weight1;weight2;weight3\n");
			for (int messung=1;messung <= 10000; messung++){
				writer.write(messung+";"+bett+";"+Math.round(Math.random()*60.0)+";"+Math.round(Math.random()*60.0)+";"+Math.round(Math.random()*60.0)+";"+Math.round(Math.random()*60.0)+"\n");
			}
			writer.flush();
		}
	}
}
