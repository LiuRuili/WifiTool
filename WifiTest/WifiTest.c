#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <string.h>
#include <private/android_filesystem_config.h>

#define LOG_TAG "WifiTest"

#include "cutils/log.h"
#include "cutils/properties.h"

#define SERV_TCP_PORT 6000
#define SERV_HOST_ADDR "127.0.0.1"
#define MAXLINE 512

int readline(int fd, char *ptr, int maxlen)
{
    int n, rc;
    char c;

    for (n = 1; n < maxlen; n++) {
        if ((rc = read(fd, &c, 1)) == 1) {
            *ptr++ = c;
            if (c == '\n')
                break;
        } else if (rc == 0) {
            if (n == 1)
                return 0;
            else
                break;
        } else
            return -1;
    }

    *ptr = 0;
    return n;
}

int is_asus_project()
{
	FILE* pipe = popen("WifiSetup", "r");
	char buffer[128];
	if (pipe) {
		while(!feof(pipe)) {
			if(fgets(buffer, 128, pipe) != NULL){}
		}
		pclose(pipe);
		buffer[strlen(buffer)-1] = '\0';
	}
	return atoi(buffer);
}

int main( int argc, char **argv )
{
    char buf[MAXLINE] = "sh /etc/wifi/mfg_script/WifiTest.sh";
    char line[MAXLINE];
    int i, n;

    if(!is_asus_project()) {
        printf("PASS\n");
	    return 0;
	}
    for(i = 1; i < argc; i++) {
        strcat(buf, " ");
        strcat(buf, argv[i]);
    }

    if (argc == 1) {
        strcat(buf, " ");
        strcat(buf, "h");
        system(buf);
        return 0;
    } else if (strcmp(argv[1], "h") == 0) {
        system(buf);
        return 0;
    }

    int sockfd;
    struct sockaddr_in serv_addr;

    /*
     * Fill int the structure "serv_addr" with the address of the
     * server that we want to connect with
     */

    memset((char *) &serv_addr, 0, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = inet_addr(SERV_HOST_ADDR);
    serv_addr.sin_port = htons(SERV_TCP_PORT);

    /*
     * Open a TCP socket (an Internet stream socket).
     */

    if ((sockfd = socket(AF_INET, SOCK_STREAM, 0)) < 0)
        ALOGE("can't open stream server");

    /*
     * Connect to the server
     */

    if (connect(sockfd, (struct sockaddr *) &serv_addr, sizeof(serv_addr)) < 0)
        ALOGE("can't connect to server");

    ALOGD("write: %s", buf);
    write(sockfd, buf, MAXLINE);

    /*
     * Read a line from the socket
     */
    memset(line, 0, MAXLINE);
    n = readline(sockfd, line, MAXLINE);

    if (strlen(line) > 0) {
        if (strcmp(line,"PASS") == 0)
            printf("PASS\n");
        else if (strcmp(line, "FAIL") == 0)
            printf("FAIL\n");
        else
            printf("%s\n", line);
    } else
        ALOGE("readline error");

    close(sockfd);
    return 0;
}
