#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>

void Usage() {
	printf("The input should be:\n");
	printf("usleep <micro seconds>\n");
}

int main(int argc, char **argv)
{
	int millisec = 0;	

	if (argc != 2) {
		printf("Invalid Input\n");
		Usage();
		return -1;
	}
	argv++;

	millisec = atoi(*argv);

	if (millisec > 0)
		usleep(millisec);
	else {
		printf("Invalid Input\n");
		Usage();
	}

	return 0;
}
