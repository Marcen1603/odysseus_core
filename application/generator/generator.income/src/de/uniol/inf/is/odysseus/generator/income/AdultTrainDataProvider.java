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
package de.uniol.inf.is.odysseus.generator.income;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.StreamClientHandler;


public class AdultTrainDataProvider extends StreamClientHandler{
	
	private BufferedReader in;	
	private long number  = 0;
	

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
			
			for(int i=0;i<rawTuple.length;i++){
				if(rawTuple[i].equals("")){
					rawTuple[i] = "-1";
				}
			}
			
			//number
			tuple.addAttribute(new Long(number));
			//age								
			tuple.addAttribute(new Integer(rawTuple[0]));			
			//working class
			tuple.addAttribute(rawTuple[1].trim());
			//education
			tuple.addAttribute(rawTuple[3].trim());
			//education-num
			tuple.addAttribute(new Integer(rawTuple[4].trim()));
			//marriage status
			tuple.addAttribute(rawTuple[5].trim());
			//occupation
			tuple.addAttribute(rawTuple[6].trim());
			//relationship
			tuple.addAttribute(rawTuple[7].trim());
			//race
			tuple.addAttribute(rawTuple[8].trim());
			//sex
			tuple.addAttribute(rawTuple[9].trim());
			//capital-gain
			tuple.addAttribute(new Integer(rawTuple[10].trim()));
			//capital-loss
			tuple.addAttribute(new Integer(rawTuple[11].trim()));
			//hours-per-week
			tuple.addAttribute(new Integer(rawTuple[12].trim()));
			//country
			tuple.addAttribute(rawTuple[13].trim());
			//income
			tuple.addAttribute(rawTuple[14].trim());
			
			
			
			number++;
			try {
				Thread.sleep(0);
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
		URL fileURL = Activator.getContext().getBundle().getEntry("/data/adult.data");
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
		return new AdultTrainDataProvider();
	}
}
