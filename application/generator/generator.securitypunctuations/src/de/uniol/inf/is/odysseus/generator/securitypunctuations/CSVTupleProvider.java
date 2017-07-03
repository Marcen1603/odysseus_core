package de.uniol.inf.is.odysseus.generator.securitypunctuations;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.AbstractDataGenerator;
import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.IDataGenerator;

public class CSVTupleProvider extends AbstractDataGenerator {
	int delay;
	boolean secondStream;
	String csvFile;
	String fileName;
	URL fileURL;
	BufferedReader br = null;
	String line = "";
	int counter = 0;
	private long timestamp;
	int iterator = 0;
	int amount = 0;

	public CSVTupleProvider(int delay, String fileName, int amount) {
		super();
		this.delay = delay;
		this.amount = amount;
		this.fileName = fileName;
		this.fileURL = Activator.getContext().getBundle().getEntry("/data/" + fileName);

	}

	@Override
	public List<DataTuple> next() throws InterruptedException {
		if (counter <= amount) {
			if (br == null || iterator == 9) {
				initFileStream();
			}

			try {
				line = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String[] temp = line.split(",");
			List<DataTuple> list = new ArrayList<DataTuple>();
			timestamp = System.currentTimeMillis();
			DataTuple tuple = new DataTuple();
			// if (temp[0].equals("1")) {
			// tuple.addAttribute(true);
			// tuple.addAttribute(temp[1]);
			// tuple.addAttribute(temp[2]);
			// tuple.addAttribute(temp[3]);
			// if (temp[4].equals("true")) {
			// tuple.addAttribute(true);
			// } else {
			// tuple.addAttribute(false);
			// }if(temp[5].equals("true")){
			// tuple.addAttribute(true);
			// }else{
			// tuple.addAttribute(false);
			// }
			// } else {

//			tuple.addAttribute(counter);// TupelID
			
			tuple.addAttribute(Integer.parseInt(temp[0]));// patientenID
			tuple.addAttribute(Integer.parseInt(temp[1]));// Puls oder
															// Atemfrequenz

			tuple.addAttribute(timestamp);// start Timestamp
			tuple.addAttribute(timestamp + 2000);// end Timestamp
			list.add(tuple);
			if (iterator != 9) {
				iterator++;
			} else
				iterator = 0;
			counter++;
			Thread.sleep(delay);
			return list;
		}return null;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void process_init() {
		// TODO Auto-generated method stub

	}

	@Override
	public IDataGenerator newCleanInstance() {
		return new CSVTupleProvider(delay, fileName, amount);
	}

	public void initFileStream() {
		InputStream inputStream;
		try {
			inputStream = fileURL.openConnection().getInputStream();
			br = new BufferedReader(new InputStreamReader(inputStream));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String[] readCSV() {
		try {
			InputStream inputStream = fileURL.openConnection().getInputStream();
			br = new BufferedReader(new InputStreamReader(inputStream));

			// br = new BufferedReader(new FileReader(fileURL));
			while ((line = br.readLine()) != null) {

				String[] Attributes = line.split(",");

				return Attributes;

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;

	}

}
