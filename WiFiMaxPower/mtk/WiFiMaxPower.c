#define LOG_TAG "WiFiMaxPower"
#include <stdio.h>
#include <string.h>
#include "power_table.h"
#include "cutils/log.h"
#include "cutils/properties.h"

struct rate_pwr_map *get_power_table(char *model, int *size)
{
    if (strncmp(model, "asus8173_tb_a8s_l1", 18)==0 ||
        strncmp(model, "asus8176_tb_a8_m", 16)==0 ||
        strncmp(model, "P026", 4)==0 ){
        *size = (sizeof(ariel_rate_pwr_map)/sizeof(rate_pwr));
        return ariel_rate_pwr_map;
    } else { // default device: ariel
        *size = (sizeof(ariel_rate_pwr_map)/sizeof(rate_pwr));
        return ariel_rate_pwr_map;
    }
}

int main(int argc, char *argv[])
{
    char model[PROPERTY_VALUE_MAX];
    struct rate_pwr_map *power_table;
    int size=0, i=0;
    property_get("ro.product.model", model, "");

    if (argc != 2) {
        printf("Unsupported foramt!\n");
        printf("Format: WiFiMaxPower <11a/11b/11g/11n/11ag/11an/11ac>\n");
        return -1;
    }

    power_table = get_power_table(model, &size);
    if (power_table == NULL || size == 0 ) {
        printf("Not supported model\n");
        return -1;
    }

    for (i = 0; i < size; i++) {
        if (strcmp(argv[1], power_table[i].rate) == 0) {
            printf("%.1f\n", (float) power_table[i].power/100);
            return 0;
        }
    }

    printf("Unsupported rate!\n");
    return -1;
}
