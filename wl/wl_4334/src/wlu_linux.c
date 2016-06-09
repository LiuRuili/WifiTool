/*
 * Linux port of wl command line utility
 *
 * Copyright (C) 2011, Broadcom Corporation
 * All Rights Reserved.
 * 
 * This is UNPUBLISHED PROPRIETARY SOURCE CODE of Broadcom Corporation;
 * the contents of this file may not be disclosed to third parties, copied
 * or duplicated in any form, in whole or in part, without the prior
 * written permission of Broadcom Corporation.
 *
 * $Id: wlu_linux.c 301294 2011-12-07 13:04:13Z $
 */
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <ctype.h>
#include <string.h>
#include <errno.h>
#ifndef TARGETENV_android
#include <error.h>
#endif /* TARGETENV_android */
#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <sys/ioctl.h>
#include <net/if.h>
#include <proto/ethernet.h>
#include <proto/bcmip.h>

#ifndef TARGETENV_android
typedef u_int64_t u64;
typedef u_int32_t u32;
typedef u_int16_t u16;
typedef u_int8_t u8;
typedef u_int64_t __u64;
typedef u_int32_t __u32;
typedef u_int16_t __u16;
typedef u_int8_t __u8;
#endif /* TARGETENV_android */

#include <linux/sockios.h>
#include <linux/ethtool.h>
#include <signal.h>
#include <typedefs.h>
#include <wlioctl.h>
#include <bcmutils.h>
#include <sys/wait.h>
#include <netdb.h>
#include <netinet/in.h>
#include "wlu.h"
#include <bcmcdc.h>
#include "wlu_remote.h"
#include "wlu_client_shared.h"
#include "wlu_pipe.h"
#include <miniopt.h>

#define DEV_TYPE_LEN					3 /* length for devtype 'wl'/'et' */
#define INTERACTIVE_NUM_ARGS			15
#define INTERACTIVE_MAX_INPUT_LENGTH	256
#define NO_ERROR						0
#define RWL_WIFI_JOIN_DELAY				5


#ifdef ANDROIDTEST
///////////////////////////////////////////////////////////////////////////////
// Headers                                                                   //
///////////////////////////////////////////////////////////////////////////////
#define LOG_TAG "WifiRfTest"

#include "cutils/log.h"
#include "cutils/properties.h"
#include "libwifirftest.h"


///////////////////////////////////////////////////////////////////////////////
// Definitions                                                               //
///////////////////////////////////////////////////////////////////////////////
#define PROGRAM_NAME                "wl"
#define WIFI_PROPERTY_KEY           "wifi.interface"
#define WIFI_DEVICE_NAME            "wlan0"
#define MAX_BUF_SIZE                64
#define MAX_CMD_NUM                 40
#define MAX_REPORT_BUF_SIZE         256


#ifdef WIFIRFTEST_DEBUG
#define DBGPRINT( msg, args... )        LOGD( "%s: " msg, __func__, ##args );
#else
#define DBGPRINT( msg, args... )
#endif


///////////////////////////////////////////////////////////////////////////////
// Variables                                                                 //
///////////////////////////////////////////////////////////////////////////////


static char *cmd[ MAX_CMD_NUM ];
char reportBuf[ MAX_REPORT_BUF_SIZE ];
int allcorrectPkt;
int packetNum;
char version[64];
char *LibraryVersion = WIFI_LIB_VERSION34;
char *LibraryReleaseDate = WIFI_LIB_RELEASE_DATE34;
#endif


/* Function prototypes */
static cmd_t *wl_find_cmd(char* name);
static int do_interactive(struct ifreq *ifr);
static int wl_do_cmd(struct ifreq *ifr, char **argv);
int process_args(struct ifreq* ifr, char **argv);
extern int g_child_pid;
/* RemoteWL declarations */
int remote_type = NO_REMOTE;
int rwl_os_type = LINUX_OS;
static bool rwl_dut_autodetect = TRUE;
static bool debug = FALSE;
extern char *g_rwl_buf_mac;
extern char* g_rwl_device_name_serial;
unsigned short g_rwl_servport;
char *g_rwl_servIP = NULL;
unsigned short defined_debug = DEBUG_ERR | DEBUG_INFO;
static uint interactive_flag = 0;
extern char *remote_vista_cmds[];
extern char g_rem_ifname[IFNAMSIZ];
static void
syserr(char *s)
{
	fprintf(stderr, "%s: ", wlu_av0);
	perror(s);
#ifndef ANDROIDTEST
	exit(errno);
#endif
}

int
wl_ioctl(void *wl, int cmd, void *buf, int len, bool set)
{
	struct ifreq *ifr = (struct ifreq *) wl;
	wl_ioctl_t ioc;
	int ret = 0;
	int s;

	/* open socket to kernel */
	if ((s = socket(AF_INET, SOCK_DGRAM, 0)) < 0)
		syserr("socket");

	/* do it */
	ioc.cmd = cmd;
	ioc.buf = buf;
	ioc.len = len;
	ioc.set = set;
	ifr->ifr_data = (caddr_t) &ioc;
	if ((ret = ioctl(s, SIOCDEVPRIVATE, ifr)) < 0) {
		if (cmd != WLC_GET_MAGIC) {
			ret = IOCTL_ERROR;
		}
	}

	/* cleanup */
	close(s);
	return ret;
}

static int
wl_get_dev_type(char *name, void *buf, int len)
{
	int s;
	int ret;
	struct ifreq ifr;
	struct ethtool_drvinfo info;

	/* open socket to kernel */
	if ((s = socket(AF_INET, SOCK_DGRAM, 0)) < 0)
		syserr("socket");

	/* get device type */
	memset(&info, 0, sizeof(info));
	info.cmd = ETHTOOL_GDRVINFO;
	ifr.ifr_data = (caddr_t)&info;
	strncpy(ifr.ifr_name, name, IFNAMSIZ);
	if ((ret = ioctl(s, SIOCETHTOOL, &ifr)) < 0) {

		/* print a good diagnostic if not superuser */
		if (errno == EPERM)
			syserr("wl_get_dev_type");

		*(char *)buf = '\0';
	} else {
		strncpy(buf, info.driver, len);
	}

	close(s);
	return ret;
}

static int
wl_find(struct ifreq *ifr)
{
	char proc_net_dev[] = "/proc/net/dev";
	FILE *fp;
	char buf[1000], *c, *name;
	char dev_type[DEV_TYPE_LEN];
	int status;

	ifr->ifr_name[0] = '\0';

	if (!(fp = fopen(proc_net_dev, "r")))
		return BCME_ERROR;

	/* eat first two lines */
	if (!fgets(buf, sizeof(buf), fp) ||
	    !fgets(buf, sizeof(buf), fp)) {
		fclose(fp);
		return BCME_ERROR;
	}

	while (fgets(buf, sizeof(buf), fp)) {
		c = buf;
		while (isspace(*c))
			c++;
		if (!(name = strsep(&c, ":")))
			continue;
		strncpy(ifr->ifr_name, name, IFNAMSIZ);
		if (wl_get_dev_type(name, dev_type, DEV_TYPE_LEN) >= 0 &&
			!strncmp(dev_type, "wl", 2))
			if (wl_check((void *) ifr) == 0)
				break;
		ifr->ifr_name[0] = '\0';
	}
	if (ifr->ifr_name[0] == '\0')
		status = BCME_ERROR;
	else
		status = BCME_OK;

	fclose(fp);
	return status;
}


static int
ioctl_queryinformation_fe(void *wl, int cmd, void* input_buf, unsigned long *input_len)
{
	int error = NO_ERROR;

	if (remote_type == NO_REMOTE) {
		error = wl_ioctl(wl, cmd, input_buf, *input_len, FALSE);
	} else {
		error = rwl_queryinformation_fe(wl, cmd, input_buf,
		              input_len, 0, REMOTE_GET_IOCTL);

	}
	return error;
}

static int
ioctl_setinformation_fe(void *wl, int cmd, void* buf, unsigned long *input_len)
{
	int error = 0;

	if (remote_type == NO_REMOTE) {
		error = wl_ioctl(wl,  cmd, buf, *input_len, TRUE);
	} else {
		error = rwl_setinformation_fe(wl, cmd, buf,
			input_len, 0, REMOTE_SET_IOCTL);
	}

	return error;
}

int
wl_get(void *wl, int cmd, void *buf, int len)
{
	int error = 0;
	unsigned long input_len = len;

	if ((rwl_os_type == WIN32_OS || rwl_os_type == WINVISTA_OS) && remote_type != NO_REMOTE)
		cmd += WL_OID_BASE;
	error = (int)ioctl_queryinformation_fe(wl, cmd, buf, &input_len);

	if (error == SERIAL_PORT_ERR)
		return SERIAL_PORT_ERR;
	else if (error == BCME_NODEVICE)
		return BCME_NODEVICE;
	else if (error != 0)
		return IOCTL_ERROR;

	return 0;
}

int
wl_set(void *wl, int cmd, void *buf, int len)
{
	int error = 0;
	unsigned long input_len = len;

	if ((rwl_os_type == WIN32_OS || rwl_os_type == WINVISTA_OS) && remote_type != NO_REMOTE)
		cmd += WL_OID_BASE;
	error = (int)ioctl_setinformation_fe(wl, cmd, buf, &input_len);

	if (error == SERIAL_PORT_ERR)
		return SERIAL_PORT_ERR;
	else if (error == BCME_NODEVICE)
		return BCME_NODEVICE;
	else if (error != 0)
		return IOCTL_ERROR;

	return 0;
}

#if defined(WLMSO)
int wl_os_type_get_rwl()
{
	return rwl_os_type;
}

void wl_os_type_set_rwl(int os_type)
{
	rwl_os_type = os_type;
}

int wl_ir_init_rwl(void **irh)
{
	switch (rwl_get_remote_type()) {
	case NO_REMOTE:
	case REMOTE_WIFI: {
		struct ifreq *ifr;
		ifr = malloc(sizeof(struct ifreq));
		if (ifr) {
			memset(ifr, 0, sizeof(ifr));
			wl_find(ifr);
		}
		*irh = ifr;
	}
		break;
	default:
		break;
	}

	return 0;
}

void wl_close_rwl(int remote_type, void *irh)
{
	switch (remote_type) {
	case NO_REMOTE:
	case REMOTE_WIFI:
		free(irh);
		break;
	default:
		break;
	}
}

#define LINUX_NUM_ARGS  16

static int
buf_to_args(char *tmp, char *new_args[])
{
	char line[256];
	char *token;
	int argc = 0;

	strcpy(line, tmp);
	while  ((argc < (LINUX_NUM_ARGS - 1)) &&
		((token = strtok(argc ? NULL : line, " \t")) != NULL)) {
		new_args[argc] = malloc(strlen(token)+1);
		strncpy(new_args[argc], token, strlen(token)+1);
		argc++;
	}
	new_args[argc] = NULL;
	return argc;
}

int
wl_lib(char *input_str)
{
	struct ifreq ifr;
	char *ifname = NULL;
	int err = 0;
	int help = 0;
	int status = CMD_WL;
	void* serialHandle = NULL;
	char *tmp_argv[LINUX_NUM_ARGS];
	char **argv = tmp_argv;
	int argc;

	if ((argc = buf_to_args(input_str, argv)) <= 0) {
		printf("wl: can't convert input string\n");
		return (-1);
	}
#else
/* Main client function */
int
#ifdef ANDROIDTEST
submain (int argc, char **argv)
#else
main(int argc, char **argv)
#endif
{
	struct ifreq ifr;
	char *ifname = NULL;
	int err = 0;
	int help = 0;
	int status = CMD_WL;
#if defined(RWL_DONGLE) || RWL_SERIAL
	void* serialHandle = NULL;
#endif

#endif /* WLMSO */
	wlu_av0 = argv[0];

	wlu_init();
	memset(&ifr, 0, sizeof(ifr));
	(void)*argv++;

	if ((status = wl_option(&argv, &ifname, &help)) == CMD_OPT) {
		if (ifname)
			strncpy(ifr.ifr_name, ifname, IFNAMSIZ);
		/* Bug fix: If -h is used as an option, the above function call
		 * will notice it and raise the flag but it won't be processed
		 * in this function so we undo the argv increment so that the -h
		 * can be spotted by the next call of wl_option. This will ensure
		 * that wl -h [cmd] will function as desired.
		 */
		else if (help)
			(void)*argv--;
	}

	/* Linux client looking for an indongle reflector */
	if (*argv && strncmp (*argv, "--indongle", strlen(*argv)) == 0) {
		rwl_dut_autodetect = FALSE;
		(void)*argv++;
	}
	/* Linux client looking for a WinVista server */
	if (*argv && strncmp (*argv, "--vista", strlen(*argv)) == 0) {
		rwl_os_type = WINVISTA_OS;
		rwl_dut_autodetect = FALSE;
		(void)*argv++;
	}

	/* Provide option for disabling remote DUT autodetect */
	if (*argv && strncmp(*argv, "--nodetect", strlen(*argv)) == 0) {
		rwl_dut_autodetect = FALSE;
		argv++;
	}

	if (*argv && strncmp (*argv, "--debug", strlen(*argv)) == 0) {
		debug = TRUE;
		argv++;
	}

	/* RWL socket transport Usage: --socket ipaddr/hostname [port num] */
	if (*argv && strncmp (*argv, "--socket", strlen(*argv)) == 0) {
		(void)*argv++;

		remote_type = REMOTE_SOCKET;

		if (!(*argv)) {
			rwl_usage(remote_type);
			return err;
		}
		/* IP address validation is done in client_shared file */
		g_rwl_servIP = *argv;
		(void)*argv++;

		g_rwl_servport = DEFAULT_SERVER_PORT;
		if ((*argv) && isdigit(**argv)) {
			g_rwl_servport = atoi(*argv);
			(void)*argv++;
		}
	}

	/* RWL from system serial port on client to uart serial port on server */
	/* Usage: --serial /dev/ttyS0 */
	if (*argv && strncmp (*argv, "--serial", strlen(*argv)) == 0) {
		(void)*argv++;
		remote_type = REMOTE_SERIAL;
	}

	/* RWL from system serial port on client to uart dongle port on server */
	/* Usage: --dongle /dev/ttyS0 */
	if (*argv && strncmp (*argv, "--dongle", strlen(*argv)) == 0) {
		(void)*argv++;
		remote_type = REMOTE_DONGLE;
	}

#if defined(RWL_SERIAL) || defined(RWL_DONGLE)
	if (remote_type == REMOTE_SERIAL || remote_type == REMOTE_DONGLE) {
		if (!(*argv)) {
			rwl_usage(remote_type);
			return err;
		}
		g_rwl_device_name_serial = *argv;
		(void)*argv++;
		if ((serialHandle = rwl_open_pipe(remote_type, g_rwl_device_name_serial, 0, 0))
			 == NULL) {
			DPRINT_ERR(ERR, "serial device open error\r\n");
			return -1;
		}
		ifr = (*(struct ifreq *)serialHandle);
	}
#endif /*  RWL_SERIAL */

	/* RWL over wifi.  Usage: --wifi mac_address */
	if (*argv && strncmp (*argv, "--wifi", strlen(*argv)) == 0) {
		(void)*argv++;
		/* use default interface */
		if (!*ifr.ifr_name)
			wl_find(&ifr);
		/* validate the interface */
		if (!*ifr.ifr_name || wl_check((void*)&ifr)) {
			fprintf(stderr, "%s: wl driver adapter not found\n", wlu_av0);
#ifndef ANDROIDTEST
			return 1;
#else
			exit(1);
#endif
		}
		remote_type = REMOTE_WIFI;

		if (argc < 4) {
			rwl_usage(remote_type);
			return err;
		}
		/* copy server mac address to local buffer for later use by findserver cmd */
		if (!wl_ether_atoe(*argv, (struct ether_addr *)g_rwl_buf_mac)) {
			fprintf(stderr,
			"could not parse as an ethternet MAC address\n");
			return FAIL;
		}
		(void)*argv++;
	}

	if ((*argv) && (strlen(*argv) > 2) &&
		(strncmp(*argv, "--interactive", strlen(*argv)) == 0)) {
		interactive_flag = 1;
	}

	/* Process for local wl */
	if (remote_type == NO_REMOTE) {
		if (interactive_flag == 1)
			(void)*argv--;
		err = process_args(&ifr, argv);
		return err;
	} else {
#ifndef OLYMPIC_RWL
		/* Autodetect remote DUT */
		if (rwl_dut_autodetect == TRUE)
			rwl_detect((void*)&ifr, debug, &rwl_os_type);
#endif /* OLYMPIC_RWL */
	}

	if (interactive_flag == 1) {
		err = do_interactive(&ifr);
		return err;
	}

	if ((*argv) && (interactive_flag == 0)) {
		err = process_args(&ifr, argv);
		if ((err == SERIAL_PORT_ERR) && (remote_type == REMOTE_DONGLE)) {
			DPRINT_ERR(ERR, "\n Retry again\n");
			err = process_args((struct ifreq*)&ifr, argv);
		}
		return err;
	}
	rwl_usage(remote_type);
#if defined(RWL_DONGLE) || RWL_SERIAL
	if (remote_type == REMOTE_DONGLE || remote_type == REMOTE_SERIAL)
		rwl_close_pipe(remote_type, (void*)&ifr);
#endif /* RWL_DONGLE || RWL_SERIAL */
	return err;
}

/*
 * Function called for  'local' execution and for 'remote' non-interactive session
 * (shell cmd, wl cmd)
 */
int
process_args(struct ifreq* ifr, char **argv)
{
	char *ifname = NULL;
	int help = 0;
	int status = 0;
	int vista_cmd_index;
	int err = 0;
	cmd_t *cmd = NULL;
#ifdef RWL_WIFI
	int retry;
#endif

	while (*argv) {
		if ((strcmp (*argv, "sh") == 0) && (remote_type != NO_REMOTE)) {
			(void)*argv++; /* Get the shell command */
			if (*argv) {
				/* Register handler in case of shell command only */
				err = rwl_shell_cmd_proc((void*)ifr, argv, SHELL_CMD);
			} else {
				DPRINT_ERR(ERR, "Enter the shell "
				           "command, e.g. ls(Linux) or dir(Win CE)\n");
				err = -1;
			}
			return err;
		}

#ifdef RWLASD
		if ((strcmp (*argv, "asd") == 0) && (remote_type != NO_REMOTE)) {
			(void)*argv++; /* Get the asd command */
			if (*argv) {
				err = rwl_shell_cmd_proc((void*)ifr, argv, ASD_CMD);
			} else {
				DPRINT_ERR(ERR, "Enter the ASD command, e.g. ca_get_version\n");
				err = -1;
			}
			return err;
		}
#endif
		if (rwl_os_type == WINVISTA_OS) {
			for (vista_cmd_index = 0; remote_vista_cmds[vista_cmd_index] &&
				strcmp(remote_vista_cmds[vista_cmd_index], *argv);
				vista_cmd_index++);
			if (remote_vista_cmds[vista_cmd_index] != NULL) {
				err = rwl_shell_cmd_proc((void *)ifr, argv, VISTA_CMD);
				if ((remote_type == REMOTE_WIFI) && ((!strcmp(*argv, "join")))) {
#ifdef RWL_WIFI
					DPRINT_INFO(OUTPUT,
						"\nChannel will be synchronized by Findserver\n\n");
					sleep(RWL_WIFI_JOIN_DELAY);
					for (retry = 0; retry < RWL_WIFI_RETRY; retry++) {
						if ((rwl_find_remote_wifi_server(ifr,
							&g_rwl_buf_mac[0]) == 0)) {
						break;
					}
				}
#endif /* RWL_WIFI */
			}
			return err;
			}
		}

		if ((status = wl_option(&argv, &ifname, &help)) == CMD_OPT) {
			if (help)
				break;
			if (ifname) {
				if (remote_type == NO_REMOTE) {
					strncpy((*ifr).ifr_name, ifname, IFNAMSIZ);
				}
				else {
					strncpy(g_rem_ifname, ifname, IFNAMSIZ);
				}
			}
			continue;
		}
		/* parse error */
		else if (status == CMD_ERR)
			break;

		if (remote_type == NO_REMOTE) {
			/* use default interface */
			if (!*(*ifr).ifr_name)
				wl_find(ifr);
			/* validate the interface */
			if (!*(*ifr).ifr_name || wl_check((void *)ifr)) {
				fprintf(stderr, "%s: wl driver adapter not found\n", wlu_av0);

#ifdef ANDROIDTEST
				return 1;
#else
				exit(1);
#endif
			}

			if ((strcmp (*argv, "--interactive") == 0) || (interactive_flag == 1)) {
				err = do_interactive(ifr);
				return err;
			}
		 }
		/* search for command */
		cmd = wl_find_cmd(*argv);
		/* if not found, use default set_var and get_var commands */
		if (!cmd) {
			cmd = &wl_varcmd;
		}
#ifdef RWL_WIFI
		if (!strcmp(cmd->name, "findserver")) {
			remote_wifi_ser_init_cmds((void *) ifr);
		}
#endif /* RWL_WIFI */

		/* RWL over Wifi supports 'lchannel' command which lets client
		 * (ie *this* machine) change channels since normal 'channel' command
		 * applies to the server (ie target machine)
		 */
		if (remote_type == REMOTE_WIFI)	{
#ifdef RWL_WIFI
			if (!strcmp(argv[0], "lchannel")) {
				strcpy(argv[0], "channel");
				rwl_wifi_swap_remote_type(remote_type);
				err = (*cmd->func)((void *) ifr, cmd, argv);
				rwl_wifi_swap_remote_type(remote_type);
			} else {
				err = (*cmd->func)((void *) ifr, cmd, argv);
			}
			/* After join cmd's gets exeuted on the server side , client needs to know
			* the channel on which the server is associated with AP , after delay of
			* few seconds client will intiate the scan on different channels by calling
			* rwl_find_remote_wifi_server function
			*/
			if ((!strcmp(cmd->name, "join") || ((!strcmp(cmd->name, "ssid") &&
				(*(++argv) != NULL))))) {
				DPRINT_INFO(OUTPUT, "\n Findserver is called to synchronize the"
				"channel\n\n");
				sleep(RWL_WIFI_JOIN_DELAY);
				for (retry = 0; retry < RWL_WIFI_RETRY; retry++) {
					if ((rwl_find_remote_wifi_server(ifr,
					&g_rwl_buf_mac[0]) == 0)) {
						break;
					}
				}
			}
#endif /* RWL_WIFI */
		} else {
			/* do command */
			err = (*cmd->func)((void *) ifr, cmd, argv);
		}
		break;
	} /* while loop end */

/* provide for help on a particular command */
	if (help && *argv) {
		cmd = wl_find_cmd(*argv);
		if (cmd) {
			wl_cmd_usage(stdout, cmd);
		} else {
			DPRINT_ERR(ERR, "%s: Unrecognized command \"%s\", type -h for help\n",
			                                                          wlu_av0, *argv);
		}
	} else if (!cmd)
		wl_usage(stdout, NULL);
	else if (err == USAGE_ERROR)
		wl_cmd_usage(stderr, cmd);
	else if (err == IOCTL_ERROR)
		wl_printlasterror((void *) ifr);
	else if (err == BCME_NODEVICE)
		DPRINT_ERR(ERR, "%s : wl driver adapter not found\n", g_rem_ifname);

	return err;
}

/* Function called for 'local' interactive session and for 'remote' interactive session */
static int
do_interactive(struct ifreq *ifr)
{
	int err = 0;

#ifdef RWL_WIFI
	int retry;
#endif

	while (1) {
		char *fgsret;
		char line[INTERACTIVE_MAX_INPUT_LENGTH];
		fprintf(stdout, "> ");
		fgsret = fgets(line, sizeof(line), stdin);

		/* end of file */
		if (fgsret == NULL)
			break;
		if (line[0] == '\n')
			continue;

		if (strlen (line) > 0) {
			/* skip past first arg if it's "wl" and parse up arguments */
			char *argv[INTERACTIVE_NUM_ARGS];
			int argc;
			char *token;
			argc = 0;

			while ((argc < (INTERACTIVE_NUM_ARGS - 1)) &&
			       ((token = strtok(argc ? NULL : line, " \t\n")) != NULL)) {

				/* Specifically make sure empty arguments (like SSID) are empty */
				if (token[0] == '"' && token[1] == '"') {
				    token[0] = '\0';
				}

				argv[argc++] = token;
			}
			argv[argc] = NULL;
#ifdef RWL_WIFI
		if (!strcmp(argv[0], "findserver")) {
			remote_wifi_ser_init_cmds((void *) ifr);
		}
#endif /* RWL_WIFI */

			if (strcmp(argv[0], "q") == 0 || strcmp(argv[0], "exit") == 0) {
				break;
			}

			if ((strcmp(argv[0], "sh") == 0) && (remote_type != NO_REMOTE))  {
				if (argv[1]) {
					process_args(ifr, argv);
				} else {
					DPRINT_ERR(ERR, "Give shell command");
					continue;
				}
			} else { /* end shell */
				/* check for lchannel support,applicable only for wifi transport.
				* when lchannel is called remote type is swapped by calling swap_
				* remote_type.This is done to change, the remote type to local,
				* so that local machine's channel can be executed and
				* returned to the user.
				* To get back the original remote type, swap is recalled.
				*/
				if (remote_type == REMOTE_WIFI) {
#ifdef RWL_WIFI
					if (!strcmp(argv[0], "lchannel")) {
						strcpy(argv[0], "channel");
						rwl_wifi_swap_remote_type(remote_type);
						err = wl_do_cmd(ifr, argv);
						rwl_wifi_swap_remote_type(remote_type);
					} else {
						err = wl_do_cmd(ifr, argv);
					}
				/* After join cmd's gets exeuted on the server side, client
				 * needs to know the channel on which the server is associated
				 * with AP , after delay of few seconds client will intiate the
				 * scan on different channels by calling
				 * rwl_find_remote_wifi_server function
				 */
					if ((!strcmp(argv[0], "join")) ||
						(!strcmp(argv[0], "ssid"))) {
						DPRINT_INFO(OUTPUT, "\n Findserver is called"
						"after the join issued to remote \n \n");
						sleep(RWL_WIFI_JOIN_DELAY);
						for (retry = 0; retry < RWL_WIFI_RETRY; retry++) {
							if ((rwl_find_remote_wifi_server(ifr,
							&g_rwl_buf_mac[0]) == 0)) {
								break;
							}
						}
					}
#endif /* RWL_WIFI */
				} else {
					err = wl_do_cmd(ifr, argv);
				}
			} /* end of wl */
		} /* end of strlen (line) > 0 */
	} /* while (1) */

	return err;
}

/*
 * find command in argv and execute it
 * Won't handle changing ifname yet, expects that to happen with the --interactive
 * Return an error if unable to find/execute command
 */
static int
wl_do_cmd(struct ifreq *ifr, char **argv)
{
	cmd_t *cmd = NULL;
	int err = 0;
	int help = 0;
	char *ifname = NULL;
	int status = CMD_WL;

	/* skip over 'wl' if it's there */
	if (*argv && strcmp (*argv, "wl") == 0) {
		argv++;
	}

	/* handle help or interface name changes */
	if (*argv && (status = wl_option (&argv, &ifname, &help)) == CMD_OPT) {
		if (ifname) {
			fprintf(stderr,
			        "Interface name change not allowed within --interactive\n");
		}
	}

	/* in case wl_option eats all the args */
	if (!*argv) {
		return err;
	}

	if (status != CMD_ERR) {
		/* search for command */
		cmd = wl_find_cmd(*argv);

		/* defaults to using the set_var and get_var commands */
		if (!cmd) {
			cmd = &wl_varcmd;
		}
		/* do command */
		err = (*cmd->func)((void *)ifr, cmd, argv);
	}
	/* provide for help on a particular command */
	if (help && *argv) {
	  cmd = wl_find_cmd(*argv);
	 if (cmd) {
		wl_cmd_usage(stdout, cmd);
	} else {
			DPRINT_ERR(ERR, "%s: Unrecognized command \"%s\", type -h for help\n",
			       wlu_av0, *argv);
	       }
	} else if (!cmd)
		wl_usage(stdout, NULL);
	else if (err == USAGE_ERROR)
		wl_cmd_usage(stderr, cmd);
	else if (err == IOCTL_ERROR)
		wl_printlasterror((void *)ifr);
	else if (err == BCME_NODEVICE)
		DPRINT_ERR(ERR, "%s : wl driver adapter not found\n", g_rem_ifname);

	return err;
}

/* Search the wl_cmds table for a matching command name.
 * Return the matching command or NULL if no match found.
 */
static cmd_t *
wl_find_cmd(char* name)
{
	cmd_t *cmd = NULL;

	/* search the wl_cmds for a matching name */
	for (cmd = wl_cmds; cmd->name && strcmp(cmd->name, name); cmd++);

	if (cmd->name == NULL)
		cmd = NULL;

	return cmd;
}

void def_handler(int signum)
{
	UNUSED_PARAMETER(signum);
	kill(g_child_pid, SIGINT);
	kill(getpid(), SIGINT);
	exit(0);
}
/* Create a child that waits for Ctrl-C at the client side
 */
int rwl_shell_createproc(void *wl)
{
	UNUSED_PARAMETER(wl);
	signal(SIGINT, ctrlc_handler);
	signal(SIGTERM, def_handler);
	signal(SIGABRT, def_handler);
	return fork();
}

/* In case if the server shell command exits normally
 * then kill the thread that was waiting for Ctr-C to happen
 * at the client side
 */
void rwl_shell_killproc(int pid)
{
	kill(pid, SIGKILL);
	signal(SIGINT, SIG_DFL);
	wait(NULL);
}
#ifdef RWL_SOCKET
/* to validate hostname/ip given by the client */
int validate_server_address()
{
	struct hostent *he;
	struct ipv4_addr temp;
	if (!wl_atoip(g_rwl_servIP, &temp)) {
	/* Wrong IP address format check for hostname */
		if ((he = gethostbyname(g_rwl_servIP)) != NULL) {
			if (!wl_atoip(*he->h_addr_list, &temp)) {
				g_rwl_servIP =
				inet_ntoa(*(struct in_addr *)*he->h_addr_list);
				if (g_rwl_servIP == NULL) {
					DPRINT_ERR(ERR, "Error at inet_ntoa \n");
					return FAIL;
				}
			} else {
				DPRINT_ERR(ERR, "Error in IP address \n");
				return FAIL;
			}
		} else {
			DPRINT_ERR(ERR, "Enter correct IP address/hostname format\n");
			return FAIL;
		}
	}
	return SUCCESS;
}
#endif /* RWL_SOCKET */

#ifdef ANDROIDTEST
int netIfUp( void )
{
	int fd;
	struct ifreq ifr;
	char ifname[ PROPERTY_VALUE_MAX ];

	fd = socket( AF_INET, SOCK_STREAM, 0 );
	if( fd < 0 ) {
		return WIFI_STAT_FAILED;
	}

	property_get( WIFI_PROPERTY_KEY, ifname, WIFI_DEVICE_NAME );
	strncpy( ifr.ifr_name, ifname, IFNAMSIZ );
	if( ioctl( fd, SIOCGIFFLAGS, &ifr ) < 0 ) {
		close( fd );
		return WIFI_STAT_FAILED;
	}

	strncpy( ifr.ifr_name, ifname, IFNAMSIZ );
	ifr.ifr_flags |= (IFF_UP | IFF_RUNNING);
	if( ioctl( fd, SIOCSIFFLAGS, &ifr ) < 0 ) {
		close( fd );
		return WIFI_STAT_FAILED;
	}

	close( fd );
	return WIFI_STAT_SUCCESS;
}

static const char WIFI_MODULE_TYPE[]    = "wifi.module.type";

static int wifi_chip_type = -1;

static int get_chip_type( void )
{
	char wifi_module_type[PROPERTY_VALUE_MAX];

	if(wifi_chip_type < 0) {
		if (!property_get(WIFI_MODULE_TYPE, wifi_module_type, "0")) {
			ALOGE( "can not get wifi module type %s", wifi_module_type);
		} else {
			ALOGE( "wifi module : %s", wifi_module_type);
		}
		switch(atoi(wifi_module_type)) {
			case AZW_NH615:
			case MURATA_4329:
				wifi_chip_type = BCM4329;
			break;

			case MURATA_4334:
				wifi_chip_type = BCM4334;
			break;

			case AZW_AH691:
				wifi_chip_type = BCM4324;
			break;

			default:
				wifi_chip_type = BCM4330;
			break;
        }
    }
    return wifi_chip_type;
}

static int executeCmd( char *str )
{
	int len, i, c, t;
	char *s, *e;

	// Check NULL pointer
	if( !str ) {
		return WIFI_STAT_INVAL_CMD;
	}

	// Check Length
	len = strlen( str );
	if( len < 1 ) {
		return WIFI_STAT_INVAL_CMD;
	}

	cmd[ 0 ] = PROGRAM_NAME;

	// Handle string
	s = str;
	for( i = 0, c = 0 ; i < len ; i++ ) {
		if( (str[ i + 1 ] == ' ') || (str[ i + 1 ] == '\0') ) {

			// Argc counter
			c++;

			// Found the end of an argument
			e = &str[ i ];

			// Allocate buffer
			t = e - s + 2;
			cmd[ c ] = malloc( t );
			if( !cmd[ c ] ) {
				return WIFI_STAT_OUT_OF_MEMORY;
			}
			strncpy( cmd[ c ], s, t );
			cmd[ c ][ t - 1 ] = 0;

			// Next argument
			s = &str[ i + 2 ];
		}
	}

	for( i = 0 ; i < c ; i++ ) {
		DBGPRINT( "cmd[ %d ] = \"%s\"\n", i, cmd[ i ] );
	}
	DBGPRINT( "\n" );

	// Execute Command
	submain( c, cmd );

	// Free buffer
	for( i = 1 ; i < c ; i++ ) {
		free( cmd[ i ] );
		cmd[ i ] = NULL;
	}

	return WIFI_STAT_SUCCESS;
}


int startTxModulated( int channel, int antenna, int txPower, int dataRate, int dutyCycle )
{
	char buf[ MAX_BUF_SIZE ];

	DBGPRINT( "chan=%d, ant=%d, pwr=%d, rate=%d, duty=%d\n", channel, antenna, txPower, dataRate, dutyCycle );
	
	if( (channel < WIFI_CHANNEL_01) || (channel > WIFI_CHANNEL_165) ){
		return WIFI_STAT_INVAL_PARAM;
	}
	if( (antenna < WIFI_Antenna_00) || (antenna > WIFI_Antenna_01) ){
		return WIFI_STAT_INVAL_PARAM;
	}
	if( (txPower < WIFI_PWR_08_DBM) || (txPower > WIFI_PWR_18_DBM) ){
		return WIFI_STAT_INVAL_PARAM;
	}
	if( (dataRate < WIFI_RATE_01_MBPS) || (dataRate > WIFI_RATE_54_MBPS) ){
		return WIFI_STAT_INVAL_PARAM;
	}
	// Make ethernet interface up
	netIfUp();

	// Execute WL commands
	executeCmd( "mpc 0 " );
	executeCmd( "down " );
	executeCmd( "scansuppress 1 " );
	executeCmd( "country ALL " );

	// Bandwidth
	executeCmd( "mimo_bw_cap 1 " );
	executeCmd( "mimo_txbw 2 " );
		
	// Channel
	snprintf( buf, MAX_BUF_SIZE, "chanspec %d ", channel );
	executeCmd( buf );

	// Tx Power
	snprintf( buf, MAX_BUF_SIZE, "txpwr1 -o -d %d ", txPower );
	executeCmd( buf );

	// Antenna
	snprintf( buf, MAX_BUF_SIZE, "txant %d ", antenna );
	executeCmd( buf );

	snprintf( buf, MAX_BUF_SIZE, "antdiv %d ", antenna );
	executeCmd( buf );

	executeCmd( "phy_watchdog 0 " );
	executeCmd( "up " );

	// Data Rate
	if( dataRate == WIFI_RATE_05_5_MBPS) {
		snprintf( buf, MAX_BUF_SIZE, "nrate -r 5.5 " );
		executeCmd( buf );
	}else {
		snprintf( buf, MAX_BUF_SIZE, "nrate -r %d ", dataRate );
		executeCmd( buf );
	}

	// Packet
	switch (dutyCycle) {
		case WIFI_DUTYCYCLE_40:
			executeCmd( "pkteng_start 00:11:22:33:44:55 tx 100 1000 0 " );
		break;

		default:
			executeCmd( "pkteng_start 00:11:22:33:44:55 tx 20 1680 0 " );
		break;
	}

	return WIFI_STAT_SUCCESS;
}

int startTxModulated_11n( int channel, int antenna, int txPower, int mcs, int bandwidth, int dutyCycle )
{
	char buf[ MAX_BUF_SIZE ];

	DBGPRINT( "chan=%d, ant=%d, pwr=%d, mcs=%d, bw=%d, duty=%d\n", channel, antenna, txPower, mcs, bandwidth, dutyCycle );

	if( (channel < WIFI_CHANNEL_01) || (channel > WIFI_CHANNEL_165) ){
		return WIFI_STAT_INVAL_PARAM;
	}
	if( (antenna < WIFI_Antenna_00) || (antenna > WIFI_Antenna_01) ){
		return WIFI_STAT_INVAL_PARAM;
	}
	if( (txPower < WIFI_PWR_08_DBM) || (txPower > WIFI_PWR_18_DBM) ){
		return WIFI_STAT_INVAL_PARAM;
	}
	if( (mcs < WIFI_MCS_00) || (mcs > WIFI_MCS_07) ){
		return WIFI_STAT_INVAL_PARAM;
	}
	if( (bandwidth != HT_20) && (bandwidth != HT_40_LOWER) && (bandwidth != HT_40_UPPER) ){
		return WIFI_STAT_INVAL_PARAM;
	}
	// Make ethernet interface up
	netIfUp();

	// Execute WL commands
	executeCmd( "mpc 0 " );
	executeCmd( "down " );
	executeCmd( "scansuppress 1 " );
	executeCmd( "country ALL " );

	// Bandwidth
	executeCmd( "mimo_bw_cap 1 " );
	if( bandwidth == HT_20 )
		executeCmd( "mimo_txbw 2 " );
	else
		executeCmd( "mimo_txbw 4 " );
		
	// Channel
	if(bandwidth == HT_40_LOWER)
		snprintf( buf, MAX_BUF_SIZE, "chanspec %dl ", channel );
	else if(bandwidth == HT_40_UPPER)
		snprintf( buf, MAX_BUF_SIZE, "chanspec %du ", channel );
	else
		snprintf( buf, MAX_BUF_SIZE, "chanspec %d ", channel );

	executeCmd( buf );

	// Tx Power
	snprintf( buf, MAX_BUF_SIZE, "txpwr1 -o -d %d ", txPower );
	executeCmd( buf );

	// Antenna
	snprintf( buf, MAX_BUF_SIZE, "txant %d ", antenna );
	executeCmd( buf );

	snprintf( buf, MAX_BUF_SIZE, "antdiv %d ", antenna );
	executeCmd( buf );

	executeCmd( "phy_watchdog 0 " );
	executeCmd( "up " );

	// MCS index
	snprintf( buf, MAX_BUF_SIZE, "nrate -m %d ", mcs );
	executeCmd( buf );
	
	// Packet
	switch (dutyCycle) {
		case WIFI_DUTYCYCLE_40:
			executeCmd( "pkteng_start 00:11:22:33:44:55 tx 100 1000 0 " );
		break;

		default:
			executeCmd( "pkteng_start 00:11:22:33:44:55 tx 20 1680 0 " );
		break;
	}

	return WIFI_STAT_SUCCESS;
}

int stopTxRfTest( void )
{
	// Execute WL commands
	executeCmd( "pkteng_stop tx " );
	executeCmd( "pkteng_stop rx " );
	executeCmd( "fqacurcy 0 " );
	executeCmd( "down " );

	return WIFI_STAT_SUCCESS;
}

int startRx( int channel, int antenna, int bandwidth, const char *mac )
{
	char buf[ MAX_BUF_SIZE ];

	DBGPRINT( "chan=%d, ant=%d, bw=%d\n", channel, antenna, bandwidth);
	
	if( (channel < WIFI_CHANNEL_01) || (channel > WIFI_CHANNEL_165) ){
		return WIFI_STAT_INVAL_PARAM;
	}
	if( (antenna < WIFI_Antenna_00) || (antenna > WIFI_Antenna_01) ){
		return WIFI_STAT_INVAL_PARAM;
	}
	if( (bandwidth != HT_20) && (bandwidth != HT_40_LOWER) && (bandwidth != HT_40_UPPER) ){
		return WIFI_STAT_INVAL_PARAM;
	}
	// Make ethernet interface up
	netIfUp();

	// Execute WL commands
	executeCmd( "mpc 0 " );
	executeCmd( "down " );
	executeCmd( "scansuppress 1 " );
	executeCmd( "country ALL " );

	// Bandwidth
	executeCmd( "mimo_bw_cap 1 " );
	if( bandwidth == HT_20 )
		executeCmd( "mimo_txbw 2 " );
	else
		executeCmd( "mimo_txbw 4 " );
		
	// Channel
	if(bandwidth == HT_40_LOWER)
		snprintf( buf, MAX_BUF_SIZE, "chanspec %dl ", channel );
	else if(bandwidth == HT_40_UPPER)
		snprintf( buf, MAX_BUF_SIZE, "chanspec %du ", channel );
	else
		snprintf( buf, MAX_BUF_SIZE, "chanspec %d ", channel );

	executeCmd( buf );

	// Antenna Rx
	snprintf( buf, MAX_BUF_SIZE, "txant %d ", antenna );
	executeCmd( buf );

	snprintf( buf, MAX_BUF_SIZE, "antdiv %d ", antenna );
	executeCmd( buf );

	executeCmd( "phy_watchdog 0 " );
	executeCmd( "up " );

	executeCmd( "pkteng_start 00:11:22:33:44:55 rx " );

	executeCmd("counters ");
	DBGPRINT("Rx Ch %d, PacketNum 1000\n",channel);
	DBGPRINT("Rx packet number before transmit = %d\n",allcorrectPkt);
	printf( "Rx Ch %d, PacketNum 1000\n", channel );
	printf( "Rx packet number before transmit = %d\n", allcorrectPkt );
	return WIFI_STAT_SUCCESS;
}

char *stopRxRfTestAndReport( void ) {

	int correctPkt;
	int old_allcorrectPkt = allcorrectPkt;

	memset(reportBuf,0,MAX_REPORT_BUF_SIZE);

	//Get Rx packet
	executeCmd("counters ");

	if(get_chip_type() == BCM4324 ) {
		executeCmd( "rxchain 3 " );		// Reset antenna to auto
		usleep(200000);
	}

	//Stop Rf test
	executeCmd( "down " );
	correctPkt = allcorrectPkt - old_allcorrectPkt;

	snprintf(reportBuf,256,"%d",correctPkt);

	printf( "Rx packet number after transmit = %d\n",allcorrectPkt );
	DBGPRINT("Rx packet number after transmit = %d\n",allcorrectPkt);
	return reportBuf;
}

int startScan( void ) {

	// Make ethernet interface up
	netIfUp();

	// Execute WL commands
	// Start scan
	executeCmd( "scan " );
	sleep(1);
	// Get scan results
	executeCmd( "scanresults " );

	return WIFI_STAT_SUCCESS;
}

char *wifiFirmwareVersion( void ) {

	// Make ethernet interface up
	netIfUp();

	// get drviver version
	executeCmd( "ver " );

	return version;
}

char *wifiLibraryVersion( void ) {
	return LibraryVersion;
}

char *wifiLibraryReleaseDate( void ) {
	return LibraryReleaseDate;
}

int atdSetMode( int mode, int band, int spec, int datarate, int bandwidth, int channel, int stf_mode )
{
	char buf[ MAX_BUF_SIZE ];

	if( (mode != WIFI_MODE_TX_MODULATED) && (mode != WIFI_MODE_RX) ){
		DBGPRINT( "mode error\n" );
		return WIFI_STAT_INVAL_PARAM;
	}
	// 2:B band, 5:A band
	if( (band != WIFI_B_BAND) && (band != WIFI_A_BAND) ){
		DBGPRINT( "band error\n" );
		return WIFI_STAT_INVAL_PARAM;
	}
	// SPEC: B, G, N
	if( (spec < WIFI_SPEC_11B) || (spec > WIFI_SPEC_11N) ){
		DBGPRINT( "spec error\n" );
		return WIFI_STAT_INVAL_PARAM;
	}
	// Data Rate 1~54, MCS Index 0~ 15
	if( (datarate < WIFI_MCS_00) || (datarate > WIFI_RATE_54_MBPS) ){
		DBGPRINT( "datarate error\n" );
		return WIFI_STAT_INVAL_PARAM;
	}
	if( (bandwidth != HT_20) && (bandwidth != HT_40_LOWER) && (bandwidth != HT_40_UPPER) ){
		DBGPRINT( "bandwidth error\n" );
		return WIFI_STAT_INVAL_PARAM;
	}
	// Channel 1 ~ 165
	if( (channel < WIFI_CHANNEL_01) || (channel > WIFI_CHANNEL_165) ){
		DBGPRINT( "channel error\n" );
		return WIFI_STAT_INVAL_PARAM;
	}
	// STF Mode: SISO, CDD, SDM
	if( (stf_mode != WIFI_PKT_SISO) && (stf_mode != WIFI_PKT_CDD) && (stf_mode != WIFI_PKT_SDM) ){
		DBGPRINT( "stf_mode error\n" );
		return WIFI_STAT_INVAL_PARAM;
	}
	if(get_chip_type() == BCM4334 ) {
		// Make ethernet interface up
		netIfUp();

		// Execute WL commands
		executeCmd( "mpc 0 " );
		executeCmd( "down " );
		executeCmd( "scansuppress 1 " );
		executeCmd( "country ALL " );

		// Bandwidth
		executeCmd( "mimo_bw_cap 1 " );
		if(bandwidth == HT_20)
			executeCmd( "mimo_txbw 2 " );
		else
			executeCmd( "mimo_txbw 4 " );

		// Channel
		if(bandwidth == HT_40_LOWER)
			snprintf( buf, MAX_BUF_SIZE, "chanspec %dl ", channel );
		else if(bandwidth == HT_40_UPPER)
			snprintf( buf, MAX_BUF_SIZE, "chanspec %du ", channel );
		else
			snprintf( buf, MAX_BUF_SIZE, "chanspec %d ", channel );

		executeCmd( buf );

		if(mode == WIFI_MODE_RX) {
			executeCmd( "phy_watchdog 0 " );
			executeCmd( "up " );
			return WIFI_STAT_SUCCESS;
		}

		executeCmd( "phy_watchdog 0 " );
		executeCmd( "up " );

		// Data Rate
		if(spec == WIFI_SPEC_11N) { // 802.11n
			snprintf( buf, MAX_BUF_SIZE, "nrate -m %d ", datarate );
		}else { // 802.11g
			if( datarate == WIFI_RATE_05_5_MBPS)
				snprintf( buf, MAX_BUF_SIZE, "nrate -r 5.5 " );
			else
				snprintf( buf, MAX_BUF_SIZE, "nrate -r %d ", datarate );
		}
		executeCmd( buf );

	} else if(get_chip_type() == BCM4324 ) {

		// Make ethernet interface up
		netIfUp();

		// Execute WL commands
		executeCmd( "down " );
		executeCmd( "mpc 0 " );
		executeCmd( "country ALL " );
		executeCmd( "up " );

		usleep(200000);

		executeCmd( "phy_watchdog 0 " );
		executeCmd( "scansuppress 1 " );

		usleep(200000);

		executeCmd( "phy_oclscdenable 0 " );
		executeCmd( "down " );

		// Bandwidth
		executeCmd( "mimo_preamble 0 " );
		if(bandwidth == HT_20){
			executeCmd( "mimo_bw_cap 0 " );
			executeCmd( "mimo_txbw -1 " );
		} else {
			executeCmd( "mimo_bw_cap 1 " );
			executeCmd( "mimo_txbw 4 " );
		}

		// Band
		if(mode == WIFI_MODE_TX_MODULATED) {
			if(band == WIFI_B_BAND)
				executeCmd( "band b " );
			else
				executeCmd( "band a " );
		}else if(mode == WIFI_MODE_RX) {
			if(band == WIFI_B_BAND)
				executeCmd( "band b " );
			else
				 executeCmd( "band auto " );
		}

		// Channel
		if(bandwidth == HT_40_LOWER)
			snprintf( buf, MAX_BUF_SIZE, "chanspec %dl ", channel );
		else if(bandwidth == HT_40_UPPER)
			snprintf( buf, MAX_BUF_SIZE, "chanspec %du ", channel );
		else
			snprintf( buf, MAX_BUF_SIZE, "chanspec %d ", channel );

		executeCmd( buf );

		executeCmd( "up " );

		usleep(200000);

		executeCmd( "sgi_tx 0 " );

		// Data Rate
		if(spec == WIFI_SPEC_11N) { // 802.11n
			snprintf( buf, MAX_BUF_SIZE, "nrate -m %d -s %d ", datarate, stf_mode );
		}else { // 802.11g
			if( datarate == WIFI_RATE_05_5_MBPS)
				snprintf( buf, MAX_BUF_SIZE, "nrate -r 5.5 " );
			else
				snprintf( buf, MAX_BUF_SIZE, "nrate -r %d ", datarate );
		}
		executeCmd( buf );

		if(stf_mode == WIFI_PKT_CDD) {
			executeCmd( "stbc_tx 0 " );
			executeCmd( "stbc_rx 0 " );
		}
	}
	return WIFI_STAT_SUCCESS;
}

int atdStartTx( int power, int ant, int dutyCycle )
{
	char buf[ MAX_BUF_SIZE ];

	if( (power < WIFI_PWR_08_DBM) || (power > WIFI_PWR_18_DBM) ){
		DBGPRINT( "power error\n" );
		return WIFI_STAT_INVAL_PARAM;
	}
	if( (ant < WIFI_Antenna_00) || (ant > WIFI_Antenna_AUTO) ){
		DBGPRINT( "ant error\n" );
		return WIFI_STAT_INVAL_PARAM;
	}
	if(get_chip_type() == BCM4334 ) {

		// Tx Power
		snprintf( buf, MAX_BUF_SIZE, "txpwr1 -o -d %d ", power );
		executeCmd( buf );

		// Antenna
		snprintf( buf, MAX_BUF_SIZE, "txant %d ", ant );
		executeCmd( buf );

		snprintf( buf, MAX_BUF_SIZE, "antdiv %d ", ant );
		executeCmd( buf );

		// Packet
		switch (dutyCycle) {
			case WIFI_DUTYCYCLE_40:
				executeCmd( "pkteng_start 00:11:22:33:44:55 tx 100 1000 0 " );
			break;

			default:
				executeCmd( "pkteng_start 00:11:22:33:44:55 tx 20 1680 0 " );
			break;
		}

	} else if(get_chip_type() == BCM4324 ) {

		// Antenna
		snprintf( buf, MAX_BUF_SIZE, "txant %d ", ant );
		executeCmd( buf );

		usleep(200000);

		snprintf( buf, MAX_BUF_SIZE, "antdiv %d ", ant );
		executeCmd( buf );

		usleep(200000);

		// Tx Power
		snprintf( buf, MAX_BUF_SIZE, "txpwr1 -o -d %d ", power );
		executeCmd( buf );

		// Packet
		executeCmd( "pkteng_stop tx " );
		executeCmd( "phy_forcecal 1 " );

		usleep(200000);

		switch (dutyCycle) {
			case WIFI_DUTYCYCLE_40:
				executeCmd( "pkteng_start 10:20:30:40:50:60 tx 300 1500 0 " );
			break;

			default:
				executeCmd( "pkteng_start 10:20:30:40:50:60 tx 100 1500 0 " );
			break;
		}
	}
	return WIFI_STAT_SUCCESS;
}

int atdStartRx( int on, int ant )
{
	char buf[ MAX_BUF_SIZE ];

	if( (ant < WIFI_Antenna_00) || (ant > WIFI_Antenna_AUTO) ){
		DBGPRINT( "ant error\n" );
		return WIFI_STAT_INVAL_PARAM;
	}

	if(get_chip_type() == BCM4334 ) {

		snprintf( buf, MAX_BUF_SIZE, "txant %d ", ant );
		executeCmd( buf );

		snprintf( buf, MAX_BUF_SIZE, "antdiv %d ", ant );
		executeCmd( buf );

		if(!on)
			executeCmd( "pkteng_start 00:11:22:33:44:55 rx " );

		usleep(200000);

		executeCmd("counters ");
		DBGPRINT( "Number of received packets=%d\n", allcorrectPkt );
		printf( "Number of received packets=%d\n", allcorrectPkt );

		if(on)
			executeCmd( "pkteng_stop rx " );

	} else if(get_chip_type() == BCM4324 ) {

		if( on == 2 ) {
			snprintf( buf, MAX_BUF_SIZE, "rxchain %d ", ant+1 );
			executeCmd( buf );
			usleep(200000);
			DBGPRINT( "Switch antenna to ant %d\n", ant+1 );
			printf("1\n");
			return WIFI_STAT_SUCCESS;
		}

		if(!on) {
			snprintf( buf, MAX_BUF_SIZE, "rxchain %d ", ant+1 );
			executeCmd( buf );
			usleep(200000);

			executeCmd( "pkteng_start 00:11:22:33:44:55 rx " );
			usleep(200000);
		}

		executeCmd("counters ");

		DBGPRINT( "Number of received packets=%d\n", allcorrectPkt );
		printf( "Number of received packets=%d\n", allcorrectPkt );

		if(on) {
			executeCmd( "pkteng_stop rx " );
			usleep(200000);
		}
	}
	return WIFI_STAT_SUCCESS;
}

int startTxUnmodulated( int channel, int antenna )
{
	char buf[ MAX_BUF_SIZE ];
	int band;

	DBGPRINT( "chan=%d, ant=%d\n", channel, antenna);
	if( (channel < WIFI_CHANNEL_01) || (channel > WIFI_CHANNEL_165) ){
		return WIFI_STAT_INVAL_PARAM;
	}
	if( (antenna < WIFI_Antenna_00) || (antenna > WIFI_Antenna_01) ){
		return WIFI_STAT_INVAL_PARAM;
	}
	if ( channel <= WIFI_CHANNEL_14 ){
		band = WIFI_B_BAND;
	}else{
		band = WIFI_A_BAND;
	}

	// Make ethernet interface up
	netIfUp();

	if(get_chip_type() == BCM4324 ) {

		atdSetMode( WIFI_MODE_TX_MODULATED, band, WIFI_SPEC_11G, WIFI_RATE_54_MBPS, HT_20, channel, WIFI_PKT_SISO );
		atdStartTx( WIFI_PWR_16_DBM, antenna, WIFI_DUTYCYCLE_99 );

		executeCmd( "out " );

		snprintf( buf, MAX_BUF_SIZE, "fqacurcy %d ", channel );
		executeCmd( buf );

	} else {

		// Execute WL commands
		executeCmd( "down " );
		executeCmd( "up " );
		executeCmd( "out " );

		// Antenna
		snprintf( buf, MAX_BUF_SIZE, "txant %d ", antenna );
		executeCmd( buf );

		snprintf( buf, MAX_BUF_SIZE, "antdiv %d ", antenna );
		executeCmd( buf );

		// Band
		if(band == WIFI_B_BAND)
			executeCmd( "band b " );
		else
			executeCmd( "band a " );

		// Channel
		snprintf( buf, MAX_BUF_SIZE, "fqacurcy %d ", channel );
		executeCmd( buf );

		if ( band == WIFI_A_BAND ) {
			executeCmd( "down " );
			executeCmd( "up " );
			executeCmd( "out " );

			snprintf( buf, MAX_BUF_SIZE, "fqacurcy %d ", channel );
			executeCmd( buf );
		}
	}
	return WIFI_STAT_SUCCESS;
}
#endif
