/**
 * 
 */
package de.uniol.inf.is.odysseus.gpu.physicaloperator;

//GPU Imports
import static jcuda.driver.JCudaDriver.*;
import jcuda.driver.CUcontext;
import jcuda.driver.CUdevice;
import jcuda.driver.CUfunction;
import jcuda.driver.CUmodule;
import jcuda.*;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Paths;
import java.util.StringTokenizer;

import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.gpu.prepare;

/**
 * @author Alexander
 *
 */

public class GpuSelectPO<T extends IStreamObject<?>> extends SelectPO<T> {

	
	
	//standard const
	private String predicate[] = new String[3];
	
	private Pointer P_Input, P_Output, P_Predicate = new Pointer();
	
	private int gridDimX = 1;
	private int blockDimX= 1;
	
	private static String ptxFileName;
	
	private static CUcontext context= new CUcontext();
    
	private static CUdevice device = new CUdevice();
			
    private static CUmodule modul = new CUmodule();
    
    private static CUfunction function = new CUfunction();
		
	
	@SuppressWarnings("unchecked")
	public GpuSelectPO(SelectAO ao) {
		super((IPredicate<? super T>) ao.getPredicate());
	}
	
	// copy const
	public GpuSelectPO(GpuSelectPO<T> po) {
		super(po);
	}
	
	@Override
	public void process_open() throws OpenFailedException {
		
		super.process_open();
		
		setName("GPU Select");
		
		System.out.println("Beginn CUDA");   
		
		
		
		try {
			
			//CUDA .ptx laden
			ptxFileName = prepare.CUDA("CUDA/Hello.cu");
			
			// Predikate vorbereiten
			StringTokenizer TokenPredicate = new StringTokenizer(super.getPredicate().toString(), " ");	
			
			predicate[0] = TokenPredicate.nextToken();
			predicate[1] = TokenPredicate.nextToken();
			predicate[2] = TokenPredicate.nextToken();
			
			predicate[0] = predicate[0].substring(1, predicate[0].length()-1);
			predicate[2] = predicate[2].substring(1, predicate[2].length()-1);
			System.out.println(predicate);   
			
			//CUDA Try- catch-Block 
			
			// CUDA-Vorbereitung		
					
			cuInit(0);	       
	        //device = new CUdevice();
	        
	        cuDeviceGet(device, 0);	        
	     
			// Initialisieren des Treibers und schaffen eines Kontext für das Gerät 0
	        
		} catch (IOException e) {
			
			e.printStackTrace();
		}	
        		
        //Finally allekolierten Speicher freigeben
		
	}
	
	@Override
    protected void process_next(T element, int port) {
    	//CUDA Zeitmessen
		
		long start = System.nanoTime();
		
		int Attribute;
		
		//Elemente vorbereiten
		if(getInputSchema(port).findAttributeIndex(predicate[2]) != -1)
		{
		
			Attribute = getInputSchema(port).findAttributeIndex(predicate[2]);
		
		}else{
		
			Attribute = getInputSchema(port).findAttributeIndex(predicate[0]);
		
		}		
    	
    	
		
		transfer(CUDA_process(element,Attribute));		
		
		
		
		//Elemente sammeln
		
		
		// CUDA Speicher allokeieren
		
		//Elemente auf die GPU übertragen
		
		//Elemente von der GPU zurückholen
		
		//transfer jedes/ reihe von Elements 
		
		
		
		//System.out.println(element.toString());	
		
		
		//System.out.println(Attribute);
		
		//Finally allekolierten Speicher freigeben
    	
    }
    
    @Override
    protected void process_close() {
    	super.process_close();
    	
    	// CUDA-Nachbereitung
        try{
    	 cuModuleUnload(modul);    	 
         
         if (context != null)
         {
             cuCtxDestroy(context);
         }
   	
         //Speicher auf der GPU freigeben
        // cuMemFree();
        }finally{
        	
        }
    }
    
    private T CUDA_process(T element, int attribute){
    	
    	
    	cuCtxCreate(context, 0, device);
        		
    	cuModuleLoad(modul,ptxFileName);
	    
    	cuModuleGetFunction(function , modul, "Hello");
    	
    	System.out.println(element.toString());
    	
//    	cuLaunchKernel(function, 
//				gridDimX, 1, 1, 
//				blockDimX, 5, 5, 
//				0, null, 
//				null, null);
//		
//		cuCtxSynchronize();
		
		cuModuleUnload(modul);    	 
        
        if (context != null)
        {
            cuCtxDestroy(context);
        }
		
		return element;
    }
   
}
