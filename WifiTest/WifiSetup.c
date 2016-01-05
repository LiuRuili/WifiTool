#include <stdio.h>
#include <sys/types.h>
#include <string.h>
#define LOG_TAG "WifiTest"

#include "cutils/log.h"
#include "cutils/properties.h"

#define NOWIFI        0
#define BGNSISO       1
#define BGNMIMO       2
#define ABGNSISO      3
#define ABGNMIMO      4
#define ACSISO        5
#define ACMIMO        6
#define IDACSISO      7
#define MTKACSISO     8
#define MTKIDACSISO   9

/* BCM */
#define BCM43362      1
#define BCM43340      2
#define BCM43241      3
#define BCM4343S      4
#define BCM4339       5
#define BCM4354       6

/* QCOM */
#define WCN3610      21
#define WCN3660      22

/* INTEL */
#define AG620        31
#define LNP          32

/* MTK */
#define MT6630       41


typedef struct model_chip_struct {
    char *model;
    int chip;
} model_chip_map;

const struct model_chip_struct asus_model_chip_map[] = {
	{ "K012", BCM43362 }, { "K017", BCM43362 },		// FE170CG & ME170C
	{ "K01A", BCM43362 }, { "K018", BCM43362 },		// ME70C & TF103CG
	{ "K019", BCM43362 }, { "mofd_v0", BCM43362 },		// FE375CG
	{ "K007", BCM43362 }, { "K00R", BCM43362 },		// ME572C & ME572CL
	{ "K013", BCM43362 }, { "K011", BCM43362 },		// ME176C & ME181C
	{ "K00C", BCM43340 }, { "K00G", BCM43340 },		// TF701T & ME560CG
	{ "K014", BCM43340 }, { "TF303CL", BCM43340 },		// TF303CL
	{ "K010", BCM43340 }, { "K010E", BCM43340 },		// TF103C & TF103CE
	{ "K013C", BCM43340 }, { "P1802-T", BCM43241 },		// ME176CE & P1802
	{ "K00X", BCM4343S }, { "K01Q", BCM4343S },		// ME375CL & FE375CL
	{ "K01R", BCM4343S }, { "P01T", BCM4343S },		// RD001 & Z300CL
	{ "mofd_v1", BCM4343S },
	{ "AST21", BCM4339 }, { "K015", BCM4339 },		// ME581CL
	{ "K01H", BCM4339 }, { "P025", BCM4339 },		// ME581C & Z581C & SANTA
	{ "KIWI", BCM4354 },					// TV500I
	{ "K01B", WCN3660 }, 					// TF303K
	{ "P002", WCN3610 },					// Z370KL
	{ "K01Z", AG620 }, { "P01Z", AG620 },			// Z170C
	{ "K01Y", AG620 }, { "P01Y", AG620 },			// Z170CG
	{ "K01W", AG620 }, { "P01W", AG620 },			// Z370C
	{ "K01V", AG620 }, { "P01V", AG620 },			// Z370CG
	{ "P01I", LNP }, { "SofiaLTE_AOSP EPAD", LNP },		// Z370CL
	{ "asus8173_tb_a8s_l1", MT6630 },			// Ariel
	{ "asus8176_tb_a8_m", MT6630 }, { "P026", MT6630 },	// Ariel
};

int getchipid(char *model)
{
	int i;
	int size = (sizeof(asus_model_chip_map)/sizeof(model_chip_map));
	for (i=0; i < size; i++) {
		if(strcmp(model, asus_model_chip_map[i].model) == 0){
			return asus_model_chip_map[i].chip;
		}
	}
	return NOWIFI;
}

int main( int argc, char **argv )
{
	int chip_spec = 0;
	char project_id[PROPERTY_VALUE_MAX];
	char country[PROPERTY_VALUE_MAX];
	property_get("ro.product.model", project_id, "");
	property_get("ro.config.versatility", country, "WW");

	switch (getchipid(project_id)) {
	case BCM43362:
		printf("%d\n",BGNSISO);
		break;
	case BCM43340:
		printf("%d\n",ABGNSISO);
		break;
	case BCM43241:
		printf("%d\n",ABGNMIMO);
		break;
	case BCM4343S:
		printf("%d\n",BGNSISO);
		break;
	case BCM4339:
		if (strcmp(country, "ID") == 0) {
			printf("%d\n",IDACSISO);
			break;
		} else {
			printf("%d\n",ACSISO);
			break;
		}
	case BCM4354:
		printf("%d\n",ACMIMO);
		break;
	case WCN3660:
		printf("%d\n",ABGNSISO);
		break;
	case WCN3610:
		printf("%d\n",BGNSISO);
		break;
	case AG620:
		printf("%d\n",BGNSISO);
		break;
	case LNP:
		if (strcmp(country, "ID") == 0) {
			printf("%d\n",IDACSISO);
		} else {
			printf("%d\n",ACSISO);
		}
		break;
	case MT6630:
		if (strcmp(country, "ID") == 0) {
			printf("%d\n",MTKACSISO);
		} else {
			printf("%d\n",MTKACSISO);
		}
		break;
	default:
		printf("%d\n",NOWIFI);
		break;
	}
	return 0;
}
