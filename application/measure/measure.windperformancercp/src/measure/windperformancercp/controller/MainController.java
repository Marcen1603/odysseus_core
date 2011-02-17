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
package measure.windperformancercp.controller;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import measure.windperformancercp.model.sources.SourceModel;

import measure.windperformancercp.model.query.PerformanceModel;

public class MainController implements IController {

	private static MainController instance = new MainController();
	static SourceController srcControl;
	static PMController pmControl;
	JAXBContext jaxbContext;
	Marshaller marshaller;
	Unmarshaller unmarshaller;
	//File for saving model data (sources and performance measurings)
	String fileNameSrc = "windperformanceSrc.xml";
	String fileNamePm = "windperformanceMsr.xml";
	
	private MainController(){
		try{
			jaxbContext = JAXBContext.newInstance("measure.windperformancercp");
			marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
					   new Boolean(true));
			unmarshaller = jaxbContext.createUnmarshaller();
			loadData();
		}
		catch(Exception ex){
			System.out.println("Error while unmarshalling data: "+ex);
		}
		//System.out.println(this.toString()+": mainController says hi");
		srcControl = SourceController.getInstance();
		pmControl = PMController.getInstance();
		
		srcControl.setBrotherControl(pmControl);
		pmControl.setBrotherControl(srcControl);
		
		
	}
	
	public void saveData(){
		Writer w = null;
		
		try{
			w = new FileWriter(fileNameSrc);
			marshaller.marshal(SourceModel.getInstance(), w);
		}
		catch(IOException io){System.out.println("ioexception: "+io);}
		catch(JAXBException je){System.out.println("jaxbexception: "+je);}
		finally{
			try{
				w.close();
			} 
			catch(Exception ex){System.out.println("another exception: "+ex);}
		}
		
		try{
			//marshaller.marshal(SourceModel.getInstance(), System.out);
			w = new FileWriter(fileNamePm);
			marshaller.marshal(PerformanceModel.getInstance(), w);
			//System.out.println("wrote data to file: "+fileName);
		}
		catch(IOException io){System.out.println("ioexception: "+io);}
		catch(JAXBException je){System.out.println("jaxbexception: "+je);}
		finally{
			try{
				w.close();
			} 
			catch(Exception ex){System.out.println("another exception: "+ex);}
		}
	}
	
	public void loadData(){
		try{
			FileInputStream reader = new FileInputStream(fileNameSrc);
			if(reader != null){
		
				SourceModel bla = (SourceModel) unmarshaller.unmarshal(new FileInputStream(fileNameSrc));
		//System.out.println("bla has x elements: "+bla.getElemCount());
				SourceModel.getInstance().setSourcesList(bla.getSourcesList());
				reader = new FileInputStream(fileNamePm);
				PerformanceModel blub = (PerformanceModel) unmarshaller.unmarshal(new FileInputStream(fileNamePm));
		//System.out.println("blub has x elements: "+blub.getElemCount());
				PerformanceModel.getInstance().setQueryList(blub.getQueryList());
		//System.out.println("sourceModel: "+SourceModel.getInstance().getSourcesList().toString());
			}
		}
		catch(IOException io){System.out.println("ioexception: "+io);}
		catch(JAXBException je){System.out.println("jaxbexception: "+je);}
		catch(Exception e){
			System.out.println("exception: "+e);
		}
		
	}
	
	public static MainController getInstance(){
		return instance;
	}
	
	@Override
	public ArrayList<?> getContent(){return null;}
}
