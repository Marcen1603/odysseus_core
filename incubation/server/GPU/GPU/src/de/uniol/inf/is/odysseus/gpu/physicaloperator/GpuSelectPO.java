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
import java.nio.file.Paths;
import java.util.StringTokenizer;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;

/**
 * @author Alexander
 *
 */

public class GpuSelectPO<T extends IStreamObject<?>> extends SelectPO<T> {

	
	
	//standard const
	private String predicate[] = new String[3];
	
	//GPU pointer
	
	private Pointer P_Input, P_Output, P_Predicate = new Pointer();
	
	private CUfunction function;
	
	private int gridDimX = 1;
	private int blockDimX= 1;
	
	
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
		
		//CUDA .ptx laden
		
		try {
	    	
			String ptxFileName = preparePtxFile("CUDA/Hello.cu");
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	    
		
	    //Pfad finden
		
//		System.out.println("TXT");
//		Path p4 = FileSystems.getDefault().getPath("/users/sally");
//		System.out.println(p4);
		
		PrintWriter pWriter = null; 
	
        try { 
        	
        	//String path = System.getProperty("user.dir");
        	
        	//System.out.println(path);
        	
        	System.out.println( Paths.get("CUDA\\"));
        	
        	System.out.println(this.getClass().getResource(""));
        	
            pWriter = new PrintWriter(new BufferedWriter(new FileWriter("Tst.txt"))); 
            pWriter.println("Hallo Welt!"); 
          
           
          
            
        } catch (IOException ioe) { 
            ioe.printStackTrace(); 
            
        } finally { 
            if (pWriter != null){ 
                pWriter.flush(); 
                pWriter.close();
            } 
        } 
				
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
				
		// Initialisieren des Treibers und schaffen eines Kontext für das Gerät 0
        cuInit(0);
        CUcontext pctx = new CUcontext();
        CUdevice dev = new CUdevice();
        cuDeviceGet(dev, 0);
        cuCtxCreate(pctx, 0, dev);
				
//        CUmodule modul = new CUmodule();
//        cuModuleLoad(modul, "Hello.ptx");
//        
//        function = new CUfunction();
//        cuModuleGetFunction(function, modul, "Hello_World");
        
        
        		
        //Finally allekolierten Speicher freigeben
		
	}
	

	@Override
    protected void process_next(T element, int port) {
    	
		int Attribute;
		
		//Elemente vorbereiten
		if(getInputSchema(port).findAttributeIndex(predicate[2]) != -1)
		{
		
			Attribute = getInputSchema(port).findAttributeIndex(predicate[2]);
		
		}else{
		
			Attribute = getInputSchema(port).findAttributeIndex(predicate[0]);
		
		}
		
		Pointer kernelParameter = Pointer.to();
		
//		cuLaunchKernel(function, 
//				gridDimX, 1, 1, 
//				blockDimX, 1, 1, 
//				0, null, 
//				null, null);
//		
//		cuCtxSynchronize();
		
		//CUDA Zeitmessen
		
		//Elemente sammeln
		
		
		// CUDA Speicher allokeieren
		
		//Elemente auf die GPU übertragen
		
		//Elemente von der GPU zurückholen
		
		//transfer jedes Elements 
		
		
		
		//System.out.println(element.toString());	
		
		
		//System.out.println(Attribute);
		
		//Finally allekolierten Speicher freigeben
    	
    }
    
    @Override
    protected void process_close() {
    	super.process_close();
    	
    	// CUDA-Nachbereitung
        
    	
   	
    	// Speicher auf der GPU freigeben
    }
    
    //Quelle JCUDA.org
    static String preparePtxFile(String cuFileName) throws IOException
    {
        int endIndex = cuFileName.lastIndexOf('.');
        if (endIndex == -1)
        {
            endIndex = cuFileName.length()-1;
        }
        String ptxFileName = cuFileName.substring(0, endIndex+1)+"ptx";
        File ptxFile = new File(ptxFileName);
//        if (ptxFile.exists())
//        {
//            return ptxFileName;
//        }
        
        File cuFile = new File(cuFileName);
        if (!cuFile.exists())
        {
            throw new IOException("Input file not found: "+cuFileName);
        }
        String modelString = "-m"+System.getProperty("sun.arch.data.model");        
        String command = 
            "nvcc " + modelString + " -ptx "+
            cuFile.getPath()+" -o "+ptxFileName;
        
        System.out.println("Executing\n"+command);
        Process process = Runtime.getRuntime().exec(command);

        String errorMessage = 
            new String(toByteArray(process.getErrorStream()));
        String outputMessage = 
            new String(toByteArray(process.getInputStream()));
        int exitValue = 0;
        try
        {
            exitValue = process.waitFor();
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
            throw new IOException(
                "Interrupted while waiting for nvcc output", e);
        }

        if (exitValue != 0)
        {
            System.out.println("nvcc process exitValue "+exitValue);
            System.out.println("errorMessage:\n"+errorMessage);
            System.out.println("outputMessage:\n"+outputMessage);
            throw new IOException(
                "Could not create .ptx file: "+errorMessage);
        }
        
        System.out.println("Finished creating PTX file");
        return ptxFileName;
    }

    /**
     * Fully reads the given InputStream and returns it as a byte array
     *  
     * @param inputStream The input stream to read
     * @return The byte array containing the data from the input stream
     * @throws IOException If an I/O error occurs
     */
    private static byte[] toByteArray(InputStream inputStream) 
        throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte buffer[] = new byte[8192];
        while (true)
        {
            int read = inputStream.read(buffer);
            if (read == -1)
            {
                break;
            }
            baos.write(buffer, 0, read);
        }
        return baos.toByteArray();
    }
   
}
