/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.generator.bank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.StreamClientHandler;
import de.uniol.inf.is.odysseus.generator.StreamServer;

public class BankDataProvider extends StreamClientHandler {

	public static void main(String[] args) throws Exception {
		StreamServer server = new StreamServer(54321, new BankDataProvider());
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
		URL fileURL = Activator.getContext().getBundle().getEntry("/data/bank-data.csv");
		try {
			InputStream inputStream = fileURL.openConnection().getInputStream();
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
	
	@Override
	public StreamClientHandler clone() {
		return new BankDataProvider();
	}

}
