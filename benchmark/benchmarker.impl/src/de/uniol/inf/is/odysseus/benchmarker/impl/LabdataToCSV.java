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
package de.uniol.inf.is.odysseus.benchmarker.impl;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;

import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class LabdataToCSV {
	@SuppressWarnings({"rawtypes"})
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
			for(int i = 1; i < tuple.size(); ++i) {
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
