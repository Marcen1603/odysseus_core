package de.uniol.inf.is.odysseus.datamining.classification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import mining.generator.base.socket.StreamClientHandler;
import mining.generator.base.socket.StreamServer;
import mining.generator.base.tuple.DataTuple;

public class BankDataProvider extends StreamClientHandler {

	public static void main(String[] args) throws Exception {
		StreamServer server = new StreamServer(54321, BankDataProvider.class);
		server.start();
	}
 
	private BufferedReader in;

	@Override
	public void init() {
		System.out.println("startng stream...");
		initFileStream();
	}

	@Override
	public void close() {
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initFileStream(){
		try {
			InputStream inputStream = getClass().getResourceAsStream(
					"bank-data.csv");
			in = new BufferedReader(new InputStreamReader(inputStream));
			in.readLine();  //skip the header
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<DataTuple> next() {
		DataTuple tuple = new DataTuple();
		String line;
		try {
			line = in.readLine();
			if (line == null) {

				System.out.println("restarting stream...");
				//restart data
				in.close();
				initFileStream();
				line = in.readLine();
			}
			String[] rawTuple = line.split(",");

			tuple.addAttribute(rawTuple[0]); // id
			tuple.addAttribute(new Integer(rawTuple[1])); // age
			tuple.addAttribute(rawTuple[2]); // sex
			tuple.addAttribute(rawTuple[3]); // region
			tuple.addAttribute(new Double(rawTuple[4])); // income
			tuple.addAttribute(rawTuple[5]); // married
			tuple.addAttribute(new Integer(rawTuple[6])); // children
			tuple.addAttribute(rawTuple[7]); // car
			tuple.addAttribute(rawTuple[8]); // save_act
			tuple.addAttribute(rawTuple[9]); // current_act
			tuple.addAttribute(rawTuple[10]); // mortgage
			tuple.addAttribute(rawTuple[11]); // pep
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(line);
			List<DataTuple> list = new ArrayList<DataTuple>();
			list.add(tuple);
			return list;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
