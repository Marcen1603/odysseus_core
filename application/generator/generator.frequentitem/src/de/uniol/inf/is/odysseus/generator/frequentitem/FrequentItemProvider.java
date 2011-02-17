/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.generator.frequentitem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.StreamClientHandler;

public class FrequentItemProvider extends StreamClientHandler {

	// CREATE STREAM frequent (timestamp STARTTIMESTAMP, transaction INTEGER, item INTEGER) CHANNEL localhost : 54321;

	//private final static String DATA_FILE = "T10I4D100K.dat";
	// T10I4D100K => 1000 items, average transaction length of 10 and 100k
	// (=100.000) transactions
	private final static String DATA_FILE = "retail.dat";

	private long time = 0;
	private int transId = 1;
	private BufferedReader in;

	@Override
	public List<DataTuple> next() {
		List<DataTuple> tuples = new ArrayList<DataTuple>();

		// each line = same transaction
		String line;
		try {
			line = in.readLine();

			if (line == null) {
				return null;
			}

			line = line.trim();
			String parts[] = line.split(" ");
			for (String part : parts) {
				int itemId = Integer.parseInt(part.trim());
				DataTuple tuple = new DataTuple();
				tuple.addAttribute(new Long(time));
				tuple.addAttribute(new Integer(transId));
				tuple.addAttribute(new Integer(itemId));
				tuples.add(tuple);
			}

			time = time + 10;
			transId++;

			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		}

		return tuples;
	}

	@Override
	public void init() {
		URL fileURL = Activator.getContext().getBundle().getEntry("/data/" + DATA_FILE);
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
