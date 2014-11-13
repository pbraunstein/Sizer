class Element {
	public final String id;
	public final float area;
	public final float obesityPct;
	public final float population;
	public final float temp;
	public final float gspPC;


	public Element(String id, float area, float obesityPct,
		float population, float temp, float gspPC) {
		this.id = id;
		this.area = area;
		this.obesityPct = obesityPct;
		this.population = population;
		this.temp = temp;
		this.gspPC = gspPC;
	}

	public float getData(String dataMode) {
		if (dataMode.equals(AREA)) {
			return area;
		}

		if (dataMode.equals(OBESITY_PCT)) {
			return obesityPct;
		}

		if (dataMode.equals(POPULATION)) {
			return population;
		}

		if (dataMode.equals(TEMP)) {
			return temp;
		}

		if (dataMode.equals(GSP_P_CAP)) {
			return gspPC;
		}

		println("ERROR: Problem getting data out of Element");

		return Float.NaN;
	}
}