/**
 * 
 */
package de.uniol.inf.is.odysseus.gpu.physicaloperator;

//GPU Imports
import static jcuda.driver.JCudaDriver.*;
import jcuda.driver.CUcontext;
import jcuda.driver.CUdevice;
import jcuda.driver.CUdeviceptr;
import jcuda.driver.CUfunction;
import jcuda.driver.CUmodule;
import jcuda.driver.JCudaDriver;
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
import java.util.List;
import java.util.StringTokenizer;
import java.util.function.Predicate;

import javax.smartcardio.ATR;

import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.gpu.Prepare;

/**
 * @author Alexander
 *
 */
@SuppressWarnings("unused")
public class GpuSelectPO<T extends IStreamObject<?>> extends SelectPO<T> {

	// standard const
	private String predicate[];
	
	private IPredicate<? super T> predicateI = super.getPredicate();
	
	private String predikatObj;
	
	private SDFDatatype attributeDatatype;
	
	private  int a[] = new int [2];
	
	private int values[];
	
	//CUDA Variablen
	private static Pointer inputPtr, outputPtr, predicatePtr = new Pointer();
	
	private CUdeviceptr predDevicePtr , outputDevicePtr;
	
	private int SIZE;
	
	private Object type;
	
	private int gridDimX = 1;
	
	private int blockDimX = 1;
	
	private int attributeIndex;
	
	
	//CUDA Init Variablen
	private String ptxFileName;

	private CUcontext context = new CUcontext();
	
	private CUmodule modul = new CUmodule();	
	
	private CUfunction function = new CUfunction();
	
	
	@SuppressWarnings("unchecked")
	public GpuSelectPO(SelectAO ao) {
		super((IPredicate<? super T>) ao.getPredicate());
	}

	// copy const
	public GpuSelectPO(GpuSelectPO<T> po) {
		super(po);
	}

	@SuppressWarnings("unused")
	@Override
	public void process_open() throws OpenFailedException {

		super.process_open();

		setName("GPU Select");

		System.out.println("Beginn CUDA");

		try {

						//Predikate vorbereiten
						StringTokenizer TokenPredicate = new StringTokenizer(super.getPredicate().toString(), " ");
					
						int token = TokenPredicate.countTokens();
						predicate = new String[token];
						int n = TokenPredicate.countTokens();
						for(int i = 0; i<n;i++){
							predicate[i] = TokenPredicate.nextToken();
			
							if(predicate[i].length()-1 > 0){
								predicate[i] = predicate[i].substring(1, predicate[i].length() - 1);
							}
							
						}
						
			List<SDFAttribute> attibutesLst = predicateI.getAttributes();
			//Object e =  predicateI.getAttributes().get(4);
			
			
//CUDA-Vorbereitung
			
			//CUDA Try- catch-Block
			
			// CUDA .ptx laden
			try {
				ptxFileName = Prepare.ptxFile("CUDA/Select.cu");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// Initialisieren des Treibers und schaffen eines Kontext für das
			// Gerät 0
			
		
				Prepare.cuLoad(ptxFileName, "Select");			
				
				context = Prepare.getContext();			
				modul = Prepare.getModul();
				function = Prepare.getFunction();
											
//				CUdeviceptr deviceptr = new CUdeviceptr();
//				
//				cuMemAlloc(deviceptr, numElemt * Sizeof.INT);
//						
//				cuMemcpyHtoD(deviceptr, Pointer.to(values), numElemt * Sizeof.INT);
//				
//				CUdeviceptr DevicetoHost = new CUdeviceptr();
//				
//				cuMemAlloc(DevicetoHost, numElemt * Sizeof.INT);

//				predDevicePtr = deviceptr;
//				
//				outputDevicePtr = DevicetoHost;
				
			// Elemente vorbereiten
			
			if (getInputSchema(0).findAttributeIndex(predicate[2]) != -1) {
				attributeIndex = getInputSchema(0).findAttributeIndex(predicate[2]);
				predikatObj = predicate[0];
				
			} else {
				attributeIndex = getInputSchema(0).findAttributeIndex(predicate[0]);
				predikatObj = predicate[2];
			}

//			predikatObj = new Object[]{150,"Hello"};
			
			
			
			//CUDA Predikat vorbereiten und Speicher allekolieren 
			attributeDatatype = getInputSchema(0).getAttribute(attributeIndex).getDatatype();		
			
			CUdeviceptr predDevicePtr = new CUdeviceptr();
						
			CUdeviceptr outputDevicePtr = new CUdeviceptr();
			
			this.predDevicePtr = predDevicePtr;			
			this.outputDevicePtr = outputDevicePtr;
			
			if (attributeDatatype.equals(SDFDatatype.INTEGER)) {
				
				int predikat = 150;
				
				SIZE = 1 * Sizeof.INT;
				
				cuMemAlloc(predDevicePtr, SIZE);
				
				cuMemcpyHtoD(predDevicePtr, Pointer.to(predicatePtr), SIZE);
				
			} else if (attributeDatatype.equals(SDFDatatype.FLOAT)) {
				// float auf gpu reservieren
				float predikat = 150;
				
				SIZE = 1 * Sizeof.FLOAT;
				
				cuMemAlloc(predDevicePtr, SIZE);
				
				cuMemcpyHtoD(predDevicePtr, Pointer.to(predicatePtr), SIZE);
				
			} else if (attributeDatatype.equals(SDFDatatype.DOUBLE)) {								
				
				SIZE = 1 * Sizeof.DOUBLE;
				
				//this.predDevicePtr = predDevicePtr;
				
				cuMemAlloc(this.predDevicePtr, SIZE);
				
				cuMemcpyHtoD(this.predDevicePtr, Pointer.to(predicatePtr), SIZE);
				
				
				
			} else if(attributeDatatype.equals(SDFDatatype.CHAR)){

				
				
				SIZE = predikatObj.length() * Sizeof.CHAR;				
				
				cuMemAlloc(predDevicePtr, SIZE);
				
				cuMemcpyHtoD(predDevicePtr, Pointer.to(predicatePtr), SIZE);
			} else {
				throw new OpenFailedException("Nicht unterstützter Datentyp " + attributeDatatype);
				
				
			}

			
			// usw.

		}finally{
			//allekolierten Speicher freigeben
			
		}

		

	}

	@Override
	protected void process_next(T element, int port) {
		if (!(element instanceof Tuple)) {
			System.err.println("GPU kann nur mit Tupel arbeiten");
			return;
		}

		// CUDA Zeitmessen

		long start = System.nanoTime();
		
		Tuple<?> result = CUDA_process((Tuple<?>) element, attributeIndex);
		
		transfer((T) result);

		long end = System.nanoTime();
		// Elemente sammeln

		// CUDA Speicher allokeieren

		// Elemente auf die GPU übertragen

		// Elemente von der GPU zurückholen

		// transfer jedes/ reihe von Elements

		// Finally allekolierten Speicher freigeben

	}

	@Override
	protected void process_close() {
		super.process_close();

		// CUDA-Nachbereitung
		cuMemFree(this.predDevicePtr);
		cuMemFree(this.outputDevicePtr);
//		cuMemFree(ias);
		
		cuModuleUnload(modul);
		if (context != null) {
				cuCtxDestroy(context);
		}
			// Speicher auf der GPU freigeben
		
	}

	@SuppressWarnings("unused")
	private Tuple<?> CUDA_process(Tuple<?> element, int attributeIndex) {		
		
		cuCtxPushCurrent(context);
		
		//Abfrage auf Datentyp
		
		CUdeviceptr ias = new CUdeviceptr();
		
		if (attributeDatatype.equals(SDFDatatype.INTEGER)) {
			
			
			
		} else if (attributeDatatype.equals(SDFDatatype.FLOAT)) {
			
			
		} else if (attributeDatatype.equals(SDFDatatype.DOUBLE)) {								
			
			double attributeValue[] = new double[]{element.getAttribute(attributeIndex)};
			
			cuMemAlloc(ias, SIZE);
			
			cuMemcpyHtoD(ias, Pointer.to(attributeValue), SIZE);
		
			
		} else if(attributeDatatype.equals(SDFDatatype.CHAR)){

			
		} else {
			throw new OpenFailedException("Nicht unterstützter Datentyp " + attributeDatatype);
			
			
		}
		
		Pointer kernelParameter = Pointer.to(
				Pointer.to(predDevicePtr),
				Pointer.to(ias),
				Pointer.to(outputDevicePtr)
				);
		
		//Start Kernel auf der GPU
		cuLaunchKernel(function,
		gridDimX, 1, 1,
		blockDimX, 1, 1,
		0, null,
		kernelParameter, null);
		
		//blockiert bis alle Grids abgeschloßen sind
		cuCtxSynchronize();
		
		if (attributeDatatype.equals(SDFDatatype.INTEGER)) {
			
			
		} else if (attributeDatatype.equals(SDFDatatype.FLOAT)) {
			
			
		} else if (attributeDatatype.equals(SDFDatatype.DOUBLE)) {								
			
			double erg[] = new double[1];
			
			cuMemcpyDtoH(Pointer.to(erg), this.outputDevicePtr, SIZE);
			
		} else if(attributeDatatype.equals(SDFDatatype.CHAR)){

			
		} else {
			throw new OpenFailedException("Nicht unterstützter Datentyp " + attributeDatatype);
			
			
		}
		
		
		
				
		element = null;
		
		//cuCtxPopCurrent(context);
		
		return element;
	}

}
