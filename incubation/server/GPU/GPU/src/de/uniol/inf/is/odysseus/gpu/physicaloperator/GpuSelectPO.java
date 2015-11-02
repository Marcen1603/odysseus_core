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

public class GpuSelectPO<T extends IStreamObject<?>> extends SelectPO<T> {

	// standard const
	private String predicate[];
	
	private IPredicate<? super T> predicateI = super.getPredicate();
	
	private SDFDatatype attributeDatatype;
		
	//CUDA Variablen
	private CUdeviceptr predDevicePtr, inputDevicePtr, outputDevicePtr;
	
	private long SIZE;
		
	private int gridDimX = 1;
	
	private int blockDimX = 1;
	
	private int attributeIndex;
	
	
	//CUDA Init Variablen

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

	@Override
	public void process_open() throws OpenFailedException {

		super.process_open();

		setName("GPU Select");

		System.out.println("Beginn CUDA - Select");

		attributeDatatype = getInputSchema(0).getAttribute(attributeIndex).getDatatype();	
		
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
		// Elemente vorbereiten
		
		if (getInputSchema(0).findAttributeIndex(predicate[2]) != -1) {
			attributeIndex = getInputSchema(0).findAttributeIndex(predicate[2]);
			
			
		} else {
			attributeIndex = getInputSchema(0).findAttributeIndex(predicate[0]);
			
		}
		
			
		
		List<SDFAttribute> attibutesLst = predicateI.getAttributes();
		//Object e =  predicateI.getAttributes().get(4);
		
		try {
//CUDA-Vorbereitung
						// CUDA .ptx laden
						
						String ptxFileName = Prepare.ptxFile("CUDA/Select.cu");
						
						
						// Initialisieren des Treibers und schaffen eines Kontext für das
						// Gerät 0		
			
			//			predikatObj = new Object[]{150,"Hello"};
						
						
						
//CUDA Speicher allekolieren 
						
						
						CUdeviceptr predDevicePtr = new CUdeviceptr();
						CUdeviceptr inputDevicePtr = new CUdeviceptr();			
						CUdeviceptr outputDevicePtr = new CUdeviceptr();
						
						this.inputDevicePtr = inputDevicePtr;
						this.outputDevicePtr = outputDevicePtr;
						this.predDevicePtr = predDevicePtr;
						
						if (attributeDatatype.equals(SDFDatatype.INTEGER)) {
							
							Prepare.cuLoad(ptxFileName, "Integer");								
							
							SIZE = 1 * Sizeof.INT;
							
							double predikat[] = new double[]{150};
										
							cuMemAlloc(predDevicePtr, SIZE);
							
							cuMemAlloc(inputDevicePtr, SIZE);
							
							cuMemAlloc(outputDevicePtr, SIZE);
							
							cuMemcpyHtoD(predDevicePtr, Pointer.to(predikat), SIZE);
							
						} else if (attributeDatatype.equals(SDFDatatype.FLOAT)) {

							Prepare.cuLoad(ptxFileName, "Float");	
							
							SIZE = 1 * Sizeof.FLOAT;
							
							double predikat[] = new double[]{150};
										
							cuMemAlloc(predDevicePtr, SIZE);
							
							cuMemAlloc(inputDevicePtr, SIZE);
							
							cuMemAlloc(outputDevicePtr, SIZE);
							
							cuMemcpyHtoD(predDevicePtr, Pointer.to(predikat), SIZE);
							
						} else if (attributeDatatype.equals(SDFDatatype.DOUBLE)) {								
							
							Prepare.cuLoad(ptxFileName, "Double");			
							
							
							SIZE = 1 * Sizeof.DOUBLE;
							
							double predikat[] = new double[]{150};
										
							cuMemAlloc(predDevicePtr, SIZE);
							
							cuMemAlloc(inputDevicePtr, SIZE);
							
							cuMemAlloc(outputDevicePtr, SIZE);
							
							cuMemcpyHtoD(predDevicePtr, Pointer.to(predikat), SIZE);				
							
							
							//Speicher freigeben von local Device Pointer
							
							//cuMemFree(predDevicePtr);
							
							
							
						} else if(attributeDatatype.equals(SDFDatatype.CHAR)){
							
							Prepare.cuLoad(ptxFileName, "Char");
							
							int zeichen = predicate.length;
							
							SIZE = zeichen * Sizeof.CHAR;
							
							double predikat[] = new double[]{150};
										
							cuMemAlloc(predDevicePtr, SIZE);
							
							cuMemAlloc(inputDevicePtr, SIZE);
							
							cuMemAlloc(outputDevicePtr, SIZE);
							
							cuMemcpyHtoD(predDevicePtr, Pointer.to(predikat), SIZE);
							
						} else {
							throw new OpenFailedException("Nicht unterstützter Datentyp " + attributeDatatype);		
						}

						context = Prepare.getContext();			
						modul = Prepare.getModul();
						function = Prepare.getFunction();
						
						
			//________________________TEST_____________________________________________________________________
						
			//
			//			double pred [] = new double []{1};
			//			
			//			CUdeviceptr predDevicePtr = new CUdeviceptr();
			//			CUdeviceptr inputDevicePtr= new CUdeviceptr();			
			//			CUdeviceptr outputDevicePtr = new CUdeviceptr();
			//			
			//			cuMemAlloc(predDevicePtr, 1 * Sizeof.DOUBLE);
			//			cuMemcpyHtoD(predDevicePtr, Pointer.to(pred), 1 * Sizeof.DOUBLE);
			//			
			//			this.predDevicePtr = predDevicePtr;
			//			this.inputDevicePtr = inputDevicePtr;
			//			this.outputDevicePtr = outputDevicePtr;
	//			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(T element, int port) {
		if (!(element instanceof Tuple)) {
			System.err.println("GPU kann nur mit Tupel arbeiten");
			return;
		}
		
		Tuple<?> inputTuple = (Tuple<?>)element;

		// CUDA Zeitmessen

		long start = System.nanoTime();
		
		// Elemente sammeln
		
		if(CUDA_process(inputTuple, attributeIndex)){
		
//		Tuple<?> outputTuple = new Tuple(new Object[] {1,2,3}, inputTuple.requiresDeepClone());
		
		transfer((T) inputTuple);
		}
		
		try {
			Thread.sleep(150);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		long end = System.nanoTime();
		
		
		// Elemente auf die GPU übertragen

		// Elemente von der GPU zurückholen

		// transfer jedes/ reihe von Elements

		// Finally allekolierten Speicher freigeben

	}

	@Override
	protected void process_close() {
		super.process_close();

		// CUDA-Nachbereitung
		
		cuMemFree(inputDevicePtr);
		cuMemFree(outputDevicePtr);
		cuMemFree(predDevicePtr);
		
		cuModuleUnload(modul);
		
		if (context != null) {
				cuCtxDestroy(context);
		}
			// Speicher auf der GPU freigeben
		
	}

	@SuppressWarnings("unused")
	private boolean CUDA_process(Tuple<?> element, int attributeIndex) {		
		
		cuCtxPushCurrent(context);
		
		
		
		//Abfrage auf Datentyp
		
		if (attributeDatatype.equals(SDFDatatype.INTEGER)) {
			
			int attributeValue[] = new int[]{element.getAttribute(attributeIndex)};			
			
			cuMemcpyHtoD(inputDevicePtr, Pointer.to(attributeValue), SIZE);
			
		} else if (attributeDatatype.equals(SDFDatatype.FLOAT)) {
			
			float attributeValue[] = new float[]{element.getAttribute(attributeIndex)};			
			
			cuMemcpyHtoD(inputDevicePtr, Pointer.to(attributeValue), SIZE);
		} else if (attributeDatatype.equals(SDFDatatype.DOUBLE)) {								
			
			double attributeValue[] = new double[]{element.getAttribute(attributeIndex)};			
			
			cuMemcpyHtoD(inputDevicePtr, Pointer.to(attributeValue), SIZE);
		
			
		} else if(attributeDatatype.equals(SDFDatatype.CHAR)){

			//String muss auf Char runtergebrochen werden.
			
		
			
		} else {
			throw new OpenFailedException("Nicht unterstützter Datentyp " + attributeDatatype);
			
			
		}
		
//______________________________TEST_____________________________________________			
//		int anzahl = 3;
//		
//		long size = anzahl * Sizeof.DOUBLE;
//		double input[] = new double [anzahl];
//		
//		
//		
//		
//		for(int i = 0; i<anzahl; i++){
//			
//			input[i] = i;
//			
//		}
//		cuMemAlloc(inputDevicePtr, size);
//		cuMemAlloc(outputDevicePtr, size);
//		
//		cuMemcpyHtoD(inputDevicePtr, Pointer.to(input), size );
//___________________________ENDE___________________________________________________		
		
		
		
		Pointer kernelParameter = Pointer.to(
				Pointer.to(predDevicePtr),
				Pointer.to(inputDevicePtr),
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
		
//		double erg [] = new double[1];
//		
//		cuMemcpyDtoH(Pointer.to(erg),outputDevicePtr , SIZE);
//		
//		
//		System.out.println(erg[0]);
		
		
		if (attributeDatatype.equals(SDFDatatype.INTEGER)) {
			
			int erg[] = new int[1];
			
			cuMemcpyDtoH(Pointer.to(erg), outputDevicePtr, SIZE);
			
			if(erg[0]!= -1){
				
				return true;
				
			}else{
				
				return false;
			}
			
		} else if (attributeDatatype.equals(SDFDatatype.FLOAT)) {
			
			float erg[] = new float[1];
			
			cuMemcpyDtoH(Pointer.to(erg), outputDevicePtr, SIZE);
			
			if(erg[0]!= -1){
				
				return true;
				
			}else{
				
				return false;
			}
			
		} else if (attributeDatatype.equals(SDFDatatype.DOUBLE)) {								
			
			double erg[] = new double[1];
			
			cuMemcpyDtoH(Pointer.to(erg), outputDevicePtr, SIZE);
			
			if(erg[0]!= -1){
				
				return true;
				
			}else{
				
				return false;
			}
			
			
		} else if(attributeDatatype.equals(SDFDatatype.CHAR)){

			double erg[] = new double[1];
			
			cuMemcpyDtoH(Pointer.to(erg), outputDevicePtr, SIZE);
			
			if(erg[0]!= -1){
				
				return true;
				
			}else{
				
				return false;
			}
			
		} else {
			throw new OpenFailedException("Nicht unterstützter Datentyp " + attributeDatatype);
			
			
		}
		
	}

}
