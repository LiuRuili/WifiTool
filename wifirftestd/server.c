#include <stdio.h>
#include <string.h>
#include <stdlib.h>.
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/wait.h>
#include <netinet/in.h>
#include <arpa/inet.h>

#define SERV_TCP_PORT 6000
#define SERV_HOST_ADDR "127.0.0.1"

#define LOG_TAG "WifiRfTestDaemon"

#define MAXLINE 512

#include "cutils/log.h"

int written(int fd, char *ptr, int nbytes)
{
    int nleft, nwriten;

    nleft = nbytes;
    while (nleft > 0) {
        nwriten = write(fd, ptr, nleft);
        if (nwriten <= 0)
            return nwriten;

        nleft -= nwriten;
        ptr += nwriten;
    }

    return (nbytes - nleft);
}

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

void exec_cmd(int sockfd, char *tmp_command)
{
    FILE *fp = NULL;
    int status = 0;
    char run_command[MAXLINE];
    char run_status[MAXLINE];

    memset(run_command, 0, MAXLINE);
    memset(run_status, 0, MAXLINE);

    strcpy(run_command, tmp_command);
    ALOGE("%s", run_command);

    fp = popen(run_command, "r");
    if (fp == NULL) {
        ALOGE("Cannot execute command %s", tmp_command);
    }

    /*usleep(500000);*/
    while (fgets(run_status, MAXLINE, fp) != NULL) {
        if (run_status != NULL) {
            *(run_status + (strlen(run_status) - 1)) = 0;
            ALOGE("run_status: %s", run_status);
        }
    }

    ALOGE("exec_cmd: return %s", run_status);
    if (written(sockfd, run_status, strlen(run_status) + 1) != (strlen(run_status) + 1))
        ALOGE("exec_cmd: write error");

    status = pclose(fp);
}

void str_exec(int sockfd)
{
    int n;
    char line[MAXLINE];
    char result[MAXLINE];

    memset(line, 0, MAXLINE);
    memset(result, 0, MAXLINE);

    n = readline(sockfd, line, MAXLINE);
    if (n > 0) {
        exec_cmd(sockfd, line);
    } else
        ALOGE("str_exec: readline error");
}

int main(int argc, char* argv[])
{
    int sockfd, newsockfd, clilen, childpid, n;
    struct sockaddr_in cli_addr, serv_addr;
    char *pname;
    char result[MAXLINE];
    int status;

    pname = argv[0];

    /*
     * Open a TCP socket (an Internet stream socket)
     */

    if ((sockfd = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
        ALOGE("can't open stream socket");
        return -1;
    }
    /*
     * Bind our local address so that the client can send to us
     */

    memset((char *) &serv_addr, 0, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = htonl(INADDR_ANY);
    serv_addr.sin_port = htons(SERV_TCP_PORT);

    if (bind(sockfd, (struct sockaddr *) &serv_addr, sizeof(serv_addr)) < 0)
        ALOGE("can't bind local address");

    listen(sockfd, 1);

    while (1) {
        /*
         * Wait for a connection from a client process.
         * This is an example of a concurrent server.
         */

        clilen = sizeof(cli_addr);
        newsockfd = accept(sockfd, (struct sockaddr *) &cli_addr, &clilen);

        if (newsockfd < 0)
            ALOGE("accept error");

        if ((childpid = fork()) < 0)
            ALOGE("fork error");
        else if (childpid == 0) {	/* child process */
            close(sockfd);		/* close original socket */
            str_exec(newsockfd);
            close(newsockfd);
            exit(0);
        } else {
            wait(&status);
        }

        close(newsockfd);
    }
}
