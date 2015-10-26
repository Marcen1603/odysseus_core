extern "C"
__global__ void Hello(int *a, int *b)
{

for(int i = 0; i< 2; i++){

	b[i] = a[i] + 1;
}	
	printf("Hello World from GPU  \n%d %d ",b[0],b[1]);//thread x = %d and y = %d and z = %d\n %d %d\n",threadIdx.x, threadIdx.y, threadIdx.z, a, b);
	
	__syncthreads();
}