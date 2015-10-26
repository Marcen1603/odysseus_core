package de.uniol.inf.is.odysseus.gpu;

import static jcuda.driver.JCudaDriver.cuCtxCreate;
import static jcuda.driver.JCudaDriver.cuCtxDestroy;
import static jcuda.driver.JCudaDriver.cuDeviceGet;
import static jcuda.driver.JCudaDriver.cuModuleGetFunction;
import static jcuda.driver.JCudaDriver.cuModuleLoad;
import static jcuda.driver.JCudaDriver.cuModuleUnload;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import jcuda.driver.CUcontext;
import jcuda.driver.CUdevice;
import jcuda.driver.CUfunction;
import jcuda.driver.CUmodule;
import jcuda.driver.JCudaDriver;

//@SuppressWarnings("unused")
public class Prepare {
	
	private static String path;	

	//CUDA DEVICE 
	private static CUdevice device = new CUdevice();	
	
	private static CUcontext context;

	private static CUmodule modul;

	private static CUfunction function;
	
	//getters
	public static String getPath() {
		return path;
	}
	
	public static CUcontext getContext() {
		return context;
	}

	public static CUmodule getModul() {
		return modul;
	}

	public static CUfunction getFunction() {
		return function;
	}
	
	//setters
	public static void setPath(String path) {
		Prepare.path = path;
	}
	
	public static String ptxFile(String cuFileName ) throws IOException{		
		
		
		String cuFile = preparePtxFile(path+cuFileName);
		
		return cuFile;
		
	}
	
	
	
	public static void cuRun(){
		
	JCudaDriver.cuInit(0);
	 
	device = new CUdevice();	
	cuDeviceGet(device, 0);	
	
	
	
	}
	
	public static void cuLoad(String ptxFileName, String functionName){		
		
		context = new CUcontext();
		cuCtxCreate(context, 0, device);
		
		// Load the PTX that contains the kernel.
		modul = new CUmodule();
		cuModuleLoad(modul, ptxFileName);
		
		function = new CUfunction();
		cuModuleGetFunction(function, modul, functionName);
				
	}
	
	public static void cuUnload(){
		
		if(modul != null){
			cuModuleUnload(modul);
		}
		
		if (context != null) {
			cuCtxDestroy(context);
		}
	}
	
	//Quelle JCUDA.org
	private static String preparePtxFile(String cuFileName) throws IOException
    {
        int endIndex = cuFileName.lastIndexOf('.');
        if (endIndex == -1)
        {
            endIndex = cuFileName.length()-1;
        }
        String ptxFileName = cuFileName.substring(0, endIndex+1)+"ptx";
        File ptxFile = new File(ptxFileName);
        if (ptxFile.exists())
        {
            return ptxFileName;
        }
        
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
