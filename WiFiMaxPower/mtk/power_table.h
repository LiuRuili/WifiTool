typedef struct rate_pwr_map {
    char rate[5];
    int power;
} rate_pwr;

const struct rate_pwr_map ariel_rate_pwr_map[] = {
	{"11b", 1500},
	{"11g", 1300},
	{"11n", 1200},
	{"11a", 1500},
	{"11ag", 1500},
	{"11an", 1300},
	{"11ac", 1200},
};
