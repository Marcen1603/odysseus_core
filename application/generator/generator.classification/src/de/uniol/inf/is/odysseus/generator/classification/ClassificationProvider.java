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
public class ClassificationProvider extends StreamClientHandler{

	private BufferedReader in;


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
			long timestamp = 1L;
			
			tuple.addAttribute(new Long(timestamp));
			tuple.addAttribute(new String(rawTuple[0]));
			tuple.addAttribute(new String(rawTuple[1]));
			tuple.addAttribute(new String(rawTuple[2]));
			tuple.addAttribute(new String(rawTuple[3]));
			tuple.addAttribute(new String(rawTuple[4]));
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			List<DataTuple> list = new ArrayList<DataTuple>();
			list.add(tuple);
			return list;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void init() {	
		URL fileURL = Activator.getContext().getBundle().getEntry("/data/buyscomputer.data");
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
		return new ClassificationProvider();
	}

}
