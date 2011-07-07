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
package de.uniol.inf.is.odysseus.physicaloperator.relational;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.IClone;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * @author Kai Pancratz
 *
 */

public class FileAccessPO <T extends IMetaAttributeContainer<? extends IClone>> extends AbstractIterableSource<T>{
	
	Logger logger = LoggerFactory.getLogger(FileAccessPO.class);
	
	//Definition for the location and the type of file
	private String path;
	private String fileType;
	private long delay;
	
	private boolean isDone = false;
	private BufferedReader bf;

	public FileAccessPO(String path, String fileType) {
		this.path = path;
		this.fileType = fileType;
	}
	
	public FileAccessPO(String path, String fileType,long delay) {
		this.path = path;
		this.fileType = fileType;
		this.delay = delay;
	}
	
	@Override
	public boolean hasNext() {
		try {
			if(bf.ready())
				return true;
			else{
				propagateDone();
				return false;
			}
		} catch (Exception e) {
			propagateDone();
			e.printStackTrace();
		}
		return false;

	}

	@SuppressWarnings("unchecked")
	@Override
	public void transferNext() {
			String line = "";
			try{
				/*
				 * @TODO 
				 * 
				 * Find a better solution for the delay implementation. 
				 * 
				
				Thread.sleep(delay);
				*/
				if(!(line = bf.readLine()).isEmpty()){
					String[] splittedLine = line.split(",");
					Object[] tuple = new Object[splittedLine.length];
		
					for(int i=0;i< splittedLine.length;i++){
						String datatype = this.getOutputSchema().getAttribute(i).getDatatype().getQualName();

						if(datatype.equalsIgnoreCase("STRING")){
							tuple[i] = splittedLine[i];
						}	
						if(datatype.equalsIgnoreCase("INTEGER")){
							tuple[i] = Integer.parseInt(splittedLine[i]);
						}
						if(datatype.equalsIgnoreCase("LONG")){
							tuple[i] = Long.parseLong(splittedLine[i]);
						}	
						if(datatype.equalsIgnoreCase("DOUBLE")){
							tuple[i] = Double.parseDouble(splittedLine[i]);
						}
						if(datatype.equalsIgnoreCase("FLOAT")){
							tuple[i] = Float.parseFloat(splittedLine[i]);
						}
						if(datatype.equalsIgnoreCase("BOOLEAN")){
							tuple[i] = Boolean.parseBoolean(splittedLine[i]);
						}			
						
					}
					transfer((T)(new RelationalTuple<IMetaAttribute>(tuple)));
				}
				else{
					isDone = true;
					propagateDone();
				}
			}
			catch(Exception e){
				isDone = true;
				propagateDone();	
				e.printStackTrace();
			}
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		logger.warn("Delay is deactivated.");
		logger.warn("Delay is set: "+delay+".");
		
		try {
			//logger.debug(fileType);
			if(fileType.equalsIgnoreCase("csv")){
			            File file = new File(path);
						bf = new BufferedReader(new FileReader(file));
						//logger.debug(path);
			}
    	} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public AbstractSource<T> clone() {
		throw new RuntimeException("Clone Not implemented yet");
	}
	
	@Override
	@SuppressWarnings({"rawtypes","unchecked"})
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if(!(ipo instanceof FileAccessPO)) {
			return false;
		}
		FileAccessPO fapo = (FileAccessPO) ipo;
		if(this.path.equals(fapo.path) && this.fileType.equals(fapo.fileType)) {
			return true;
		} else {
			return false;
		}
	}


}
