package windperformancercp.controller;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import windperformancercp.model.sources.SourceModel;

public class MainController implements IController {

	private static MainController instance = new MainController();
	static SourceController srcControl;
	static PMController pmControl;
	JAXBContext jaxbContext;
	Marshaller marshaller;
	Unmarshaller unmarshaller;
	//File for saving model data (sources and performance measurings)
	String fileName = "windperformanceData.xml";
	
	private MainController(){
		try{
			jaxbContext = JAXBContext.newInstance("windperformancercp");
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
			w = new FileWriter(fileName);
			marshaller.marshal(SourceModel.getInstance(), w);
			//marshaller.marshal(SourceModel.getInstance(), System.out);
			
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
			FileInputStream reader = new FileInputStream(fileName);
			if(reader != null){
		
				SourceModel bla = (SourceModel) unmarshaller.unmarshal(new FileInputStream(fileName));
		//System.out.println("bla has x elements: "+bla.getElemCount());
				SourceModel.getInstance().setSourcesList(bla.getSourcesList());
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
