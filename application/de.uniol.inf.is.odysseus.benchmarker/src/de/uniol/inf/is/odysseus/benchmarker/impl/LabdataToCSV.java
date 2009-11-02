package de.uniol.inf.is.odysseus.benchmarker.impl;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;

import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class LabdataToCSV {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		if (args.length != 2) {
			System.out
					.println("Usage: LabdataToCSV <labdatafile> <outputfile>");
			return;
		}
		String inputFilename = args[0];
		String outputFilename = args[1];
		
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(inputFilename));
		FileWriter writer = new FileWriter(outputFilename);
		try {
		while(true) {
			RelationalTuple tuple = (RelationalTuple) in.readObject();
			writer.write(tuple.getAttribute(0).toString());
			for(int i = 1; i < tuple.getAttributeCount(); ++i) {
				writer.write(",");
				writer.write(tuple.getAttribute(i).toString());
			}
			writer.write("\n");
		}
		} catch (EOFException e) {
			
		}finally {
			writer.close();
			try {
			in.close();
			} catch (Exception e){
			}
		}
		System.out.println("Done");
	}
}
