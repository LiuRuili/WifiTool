struct rate_pwr_map {
    char rate[5];
    int power;
};

const struct rate_pwr_map z370kl_rate_pwr_map[] = {
	{"11b", 1600},
	{"11g", 1200},
	{"11n", 1100},
};
