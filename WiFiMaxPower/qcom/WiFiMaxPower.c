#define LOG_TAG "WiFiMaxPower"

#include "power_table.h"
#include "cutils/log.h"
#include "cutils/properties.h"

struct rate_pwr_map *get_power_table(char *model)
{
    if (strncmp(model, "P002", 4) == 0)
        return z370kl_rate_pwr_map;

    return NULL;
}

int get_table_size(char *model) {
    if (strncmp(model, "P002", 4) == 0)
        return 3;

    return 0;
}

int main(int argc, char *argv[])
{
    char model[PROPERTY_VALUE_MAX];
    struct rate_pwr_map *power_table;
    int size, i;
    property_get("ro.product.model", model, "");

    if (argc != 2) {
        printf("Unsupported foramt!\n");
        printf("Format: WiFiMaxPower <11a/11b/11g/11n/11ag/11an/11ac>\n");
        return -1;
    }

    power_table = get_power_table(model);
    if (power_table == NULL) {
        printf("Not supported model\n");
        return -1;
    }

    size = get_table_size(model);
    if (size == 0) {
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
