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
package de.uniol.inf.is.odysseus.generator.classification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.StreamClientHandler;

/**
 * @author Dennis Geesen
 *
 */
public class AdultDataProvider extends StreamClientHandler{

	private BufferedReader in;
	private long counter = 0L;
	private String filename;


	public AdultDataProvider(AdultDataProvider old){
		this.filename = old.filename;
	}
	
	public AdultDataProvider(String filename){
		this.filename = filename;
	}
	
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
			long timestamp = counter;
			
			tuple.addAttribute(new Long(timestamp));
			// age
			tuple.addAttribute(new Integer(rawTuple[0]));
			// workclass
			tuple.addAttribute(new String(rawTuple[1]));
			//fnlwgt
			tuple.addAttribute(new Integer(rawTuple[2].trim()));
			// education
			tuple.addAttribute(new String(rawTuple[3]));
			// education-num
			tuple.addAttribute(new Integer(rawTuple[4].trim()));
			//marital-status
			tuple.addAttribute(new String(rawTuple[5]));
			//occupation
			tuple.addAttribute(new String(rawTuple[6]));
			//relationship
			tuple.addAttribute(new String(rawTuple[7]));
			//race
			tuple.addAttribute(new String(rawTuple[8]));
			//sex
			tuple.addAttribute(new String(rawTuple[9]));
			//capital-gain
			tuple.addAttribute(new Integer(rawTuple[10].trim()));
			//capital-loss
			tuple.addAttribute(new Integer(rawTuple[11].trim()));
			//hours-per-week
			tuple.addAttribute(new Integer(rawTuple[12].trim()));
			//native-country
			tuple.addAttribute(new String(rawTuple[13]));
			//income (the class attribute)
			tuple.addAttribute(new String(rawTuple[14]));
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			List<DataTuple> list = new ArrayList<DataTuple>();
			list.add(tuple);
			counter++;
			return list;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void init() {	
		URL fileURL = Activator.getContext().getBundle().getEntry("/data/"+this.filename);
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

	@Override
	public StreamClientHandler clone() {
		return new AdultDataProvider(this);
	}

}
