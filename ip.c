#include <stdio.h>
#include <string.h>

int main(int argc, char* argv[]) {
	if(argc==1){
		return 1;
	}
	char** code;
	FILE *fptr=fopen(argv[0],"r");
	for(int i=0;fscanf(ptr,"%s",code[i];i++){
		int numCount=0;
		int blockCount=0;
		int validLine=1;
		
		for(int j=0;j<strlen(code[i])&&validLine;j++){
			if(47<code[i][j]&&code[i][j]<58){
				numCount++;
			}else if(code[i][j]==':'){
				if(numCount>5){
					validLine=0;
				}else{
					numCount=0;
					blockCount++;
				}
			}
		}
		if(validLine){
			
		}
	}
	
	fclose(fptr);
	return 0;
}
struct codeLine {
	int storeLoc;
	int operand1;
	int operation1;
	int operand2;
	int operation2;
};