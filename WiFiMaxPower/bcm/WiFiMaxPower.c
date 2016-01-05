#include <stdio.h>
#include <stdbool.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

#define LOG_TAG "WiFiMaxPower"

#include "cutils/log.h"
#include "cutils/properties.h"

#define MAX_SUPPORT_MODE 7
#define MAXLINE 256

    /*    Project    codename    wifi chip
     *
     *    TF701T     K00C        BCM43340
     *    ME560CG    K00G        BCM43340
     *    TF303CL    K014        BCM43340
     *    TF103C     K010        BCM43340
     *    TF103CE    K010E       BCM43340
     *    ME176CE    K013C       BCM43340
     *
     *    P1802      P1802-T     BCM43241
     *
     *    ME581C     K01H        BCM4339
     *    ME581CL    K015        BCM4339
     *    Z581C      P025        BCM4339
     *    SANTA      P025        BCM4339
     *
     *    FE170CG    K012        BCM43362
     *    ME170C     K017        BCM43362
     *    ME70C      K01A        BCM43362
     *    TF103CG    K018        BCM43362
     *    FE375CG    mofd_v0 / K019     BCM43362
     *    ME572C     K007        BCM43362
     *    ME572CL    K00R        BCM43362
     *
     *    TF303K     K01B        WCN3660
     *    TV500I     KIWI        BCM43241/BCM4354
     *
     *    ME375CL    K00X        BCM4343S
     *    FE375CL    K01Q        BCM4343S
     *    RD001      K01R        BCM4343S
     *    Z300CL     P01T        BCM4343S
     *
     *    Z170C      K01Z/P01Z   AG620
     *    Z170CG     K01Y/P01Y   AG620
     *    Z370C      K01W/P01W   AG620
     *    Z370CG     K01V/P01V   AG620
     *
     *    Z370CL     P01I/SofiaLTE_AOSP EPAD  Lnp
     */

bool is_asus_project()
{
    char project_id[PROPERTY_VALUE_MAX];
    char spec[PROPERTY_VALUE_MAX];
    property_get("ro.product.model", project_id, "");

    if (!strncmp(project_id, "K00G", 4) || !strncmp(project_id, "K00C", 4) ||     // ME560CG & TF701T
        !strncmp(project_id, "TF303CL", 7) || !strncmp(project_id, "K014", 4) ||  // TF303CL
        !strncmp(project_id, "K010", 4) || !strncmp(project_id, "K013C", 5) ||
        !strncmp(project_id, "K010E", 5)|| !strncmp(project_id, "K00X", 4) ||
        !strncmp(project_id, "K01Q", 4) || !strncmp(project_id, "K013", 4) ||
        !strncmp(project_id, "K011", 4) || !strncmp(project_id, "K007", 4) ||
        !strncmp(project_id, "K00R", 4) || !strncmp(project_id, "K019", 4) ||
        !strncmp(project_id, "K018", 4) || !strncmp(project_id, "K017", 4) ||
        !strncmp(project_id, "K01A", 4) || !strncmp(project_id, "K012", 4) ||
        !strncmp(project_id, "K01Z", 4) || !strncmp(project_id, "K01Y", 4) ||
        !strncmp(project_id, "K01W", 4) || !strncmp(project_id, "K01V", 4) ||
        !strncmp(project_id, "P01Z", 4) || !strncmp(project_id, "P01Y", 4) ||
        !strncmp(project_id, "P01W", 4) || !strncmp(project_id, "P01V", 4) ||
        !strncmp(project_id, "K01R", 4) || !strncmp(project_id, "P01T", 4) ||
        !strncmp(project_id, "P01I", 4) || !strncmp(project_id, "P025", 4) ||
        !strncmp(project_id, "SofiaLTE_AOSP EPAD", 18)) {
        return true;  // ASUS project
    }
    return false; // Not ASUS project
}

int main(int argc, char **argv)
{
    char line[MAXLINE];
    char nvram_path[MAXLINE] = "/etc/nvram.txt";
    char *mode[MAX_SUPPORT_MODE] = {"11a", "11b", "11g", "11n", "11ag", "11an", "11ac"};
    bool mode_supported = false;
    FILE *fp;
    char *find;
    int i, num;
    float max_2g_power = 0, max_5g_power = 0, cck_offset = 0, ofdm_2g_offset = 0, 
            ofdm_5g_offset = 0, mcs_2g_offset = 0, mcs_5g_offset = 0;
#ifdef CHIP_BCM4334X
    float total_offset = 1;
#else
    float total_offset = 1.5;
#endif /* CHIP_BCM4334X */

    if (!is_asus_project()) {
        printf("Project not supported\n");
        return 0;
    }

    if (argc != 2) {
        printf("Unsupported foramt!\n");
        printf("Format: WiFiMaxPower <11a/11b/11g/11n/11ag/11an/11ac>\n");
        return -1;
    }

    for (i = 0; i < MAX_SUPPORT_MODE ; i++) {
        if (strcmp(mode[i], argv[1]) == 0) {
            mode_supported = true;
            break;
        }
    }

    if (!mode_supported) {
        printf("%s is not supported to get WiFiMaxPower, current supported: " 
               "{11a, 11b, 11g, 11n, 11ag, 11an, 11ac}\n", argv[1]);
        return -1;
    }

    fp = fopen(nvram_path, "r");
    if (fp == NULL) {
        printf("File open failed: %d\n", errno);
        return -1;
    }

    while (fgets(line, sizeof line, fp)) {
        if (strstr(line, "maxp2ga0")) {
            find = strstr(line, "=");
            find += 1;
            if (*(find) == '0' && *(find + 1) == 'x') {
               find += 2;
               *(find + 4) = '\0';
               num = atoi(find);
               num = num/10*16 + num%10;
            } else {
               *(find + 2) = '\0';
               num = atoi(find);
            }
            max_2g_power = ((float)num)/4;
        } else if (strstr(line, "maxp5ga0")) {
            find = strstr(line, "=");
            find += 1;
            if (*(find) == '0' && *(find + 1) == 'x') {
                find += 2;
                *(find + 4) = '\0';
                num = atoi(find);
                num = num/10*16 + num%10;
            } else {
                *(find + 2) = '\0';
                num = atoi(find);
            }
            max_5g_power = ((float)num)/4;
        } else if (strstr(line, "cck2gpo") || strstr(line, "cckbw202gpo")) {
            find = strstr(line, "=");
            if (*(find + 1) == '0' && *(find + 2) == 'x')
                find += 6;
            else
                find += 1;
            *(find + 1) = '\0';
            cck_offset = ((float)atoi(find))/2;
        } else if (strstr(line, "ofdm2gpo") || strstr(line, "legofdmbw202gpo")) {
            find = strstr(line, "=");
            if (*(find + 1) == '0' && *(find + 2) == 'x')
                find += 10;
            else
                find += 1;
            *(find + 1) = '\0';
            ofdm_2g_offset = ((float)atoi(find))/2;
        } else if (strstr(line, "ofdm5gpo")) {
            find = strstr(line, "=");
            if (*(find + 1) == '0' && *(find + 2) == 'x')
                find += 10;
            else 
                find += 1;
            *(find + 1) = '\0';
            ofdm_5g_offset = ((float)atoi(find))/2;
        } else if (strstr(line, "mcs2gpo0")) {
            find = strstr(line, "=");
            if (*(find + 1) == '0' && *(find + 2) == 'x')
                find += 6;
            else 
                find += 1;
            *(find + 1) = '\0';
            mcs_2g_offset = ((float)atoi(find))/2;
        } else if (strstr(line, "mcs5gpo0")) {
            find = strstr(line, "=");
            if (*(find + 1) == '0' && *(find + 2) == 'x')
                find += 6;
            else
                find += 1;
            *(find + 1) = '\0';
            mcs_5g_offset = ((float)atoi(find))/2;
        } else if (strstr(line, "mcsbw202gpo")) {
            find = strstr(line, "=");
            if (*(find + 1) == '0' && *(find + 2) == 'x')
                find += 10;
            else
                find += 1;
            *(find + 1) = '\0';
            mcs_2g_offset = ((float)atoi(find))/2;
        } else if (strstr(line, "txpwrbckof")) {
            find = strstr(line, "=");
            find += 1;
            *(find + 1) = '\0';
            total_offset = ((float)atoi(find))/4;
        }
    }

    fclose(fp);

    if (strcmp(argv[1], mode[0]) == 0)
        printf("%.1f\n", max_5g_power - total_offset - ofdm_5g_offset);
    else if (strcmp(argv[1], mode[1]) == 0)
        printf("%.1f\n", max_2g_power - total_offset - cck_offset);
    else if (strcmp(argv[1], mode[2]) == 0)
        printf("%.1f\n", max_2g_power - total_offset - ofdm_2g_offset);
    else if (strcmp(argv[1], mode[3]) == 0)
        printf("%.1f\n", max_2g_power - total_offset - mcs_2g_offset);
    else if (strcmp(argv[1], mode[4]) == 0)
        printf("%.1f\n", max_5g_power - total_offset - ofdm_5g_offset);
    else
        printf("%.1f\n", max_5g_power - total_offset - mcs_5g_offset);

    /*
    printf("max_2g_power = %f\n", max_2g_power);
    printf("max_5g_power = %f\n", max_5g_power);
    printf("cck_offset = %f\n", cck_offset);
    printf("ofdm_2g_offset = %f\n", ofdm_2g_offset);
    printf("ofdm_5g_offset = %f\n", ofdm_5g_offset);
    printf("mcs_2g_offset = %f\n", mcs_2g_offset);
    printf("mcs_5g_offset = %f\n", mcs_5g_offset);
    */

    return 0;
}
