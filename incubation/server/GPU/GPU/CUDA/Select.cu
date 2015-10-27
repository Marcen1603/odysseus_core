extern "C"

// Es werden mehrer Methoden ben�tigt um alle eingaben auszuwerten

__global__ void Integer (double *predict, double *input, double *output) 
{

	// Das Predikat auswerten, dazu schauen ob es mehr als ein Predikat existtiert 
	
	// Die Inputdaten pr�fen auf Dimension f�r mehr als ein Predikat
	
	
	
	if(input[0]<predict[0]){
	
		output[0]= 1;
	
	}else{
		
		output[0] = -1;
	}
	
	//printf("%d \n %d",input[0],output[0]);
	
	__syncthreads();
	
}

__global__ void Double (double *predict, double *input, double *output) 
{

	// Das Predikat auswerten, dazu schauen ob es mehr als ein Predikat existtiert 
	
	// Die Inputdaten pr�fen auf Dimension f�r mehr als ein Predikat
	
	
	if(input[0]<predict[0]){
	
		output[0]= 1;
	
	}else{
		
		output[0] = -1;
	}
	
	//printf("%d \n %d",input[0],output[0]);
	
	__syncthreads();
	
}
