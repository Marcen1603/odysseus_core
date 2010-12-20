package mining.generator.dodgers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import mining.generator.base.socket.StreamClientHandler;
import mining.generator.base.tuple.DataTuple;

public class DodgersDataProvider extends StreamClientHandler {

	private BufferedReader in;
	private SimpleDateFormat format = new SimpleDateFormat("M/d/yyyy HH:mm");


	@Override
	public List<DataTuple> next() {
		DataTuple tuple = new DataTuple();
		String line;
		try {
			line = in.readLine();	
			if(line==null){
				return null;
			}
			String[] rawTuple = line.split(",");
			long timestamp = format.parse(rawTuple[0]).getTime();
			
			tuple.addAttribute(new Long(timestamp));
			tuple.addAttribute(new Integer(rawTuple[1]));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			List<DataTuple> list = new ArrayList<DataTuple>();
			list.add(tuple);
			return list;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {			
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void init() {	
		URL fileURL = Activator.getContext().getBundle().getEntry("/data/dodgers.data");
		try {
			InputStream inputStream = fileURL.openConnection().getInputStream();
			in = new BufferedReader(new InputStreamReader(inputStream));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		try {
			in.close();
		} catch (IOException e) {			
			e.printStackTrace();
		}		
	}	
	
}
