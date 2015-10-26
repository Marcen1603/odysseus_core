extern "C"

// Es werden mehrer Methoden benötigt um alle eingaben auszuwerten
__global__ void Select (double *predict, double *input, double *output) 
{

	// Das Predikat auswerten, dazu schauen ob es mehr als ein Predikat existtiert 
	
	// Die Inputdaten prüfen auf Dimension für mehr als ein Predikat
	
	int i = 0; 
	
	output[i]= input[i];
	
	printf("%d \n %d",input[0],output[0]);
	
	__syncthreads();
	
}