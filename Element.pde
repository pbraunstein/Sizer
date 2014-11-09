class Element {
	public final String id;
	public final float gdp;
	public final float area;
	public final float obesityPct;
	public final float population;
	public final float temp;


	public Element(String id, float gdp, float area, float obesityPct,
		float population, float temp) {
		this.id = id;
		this.gdp = gdp;
		this.area = area;
		this.obesityPct = obesityPct;
		this.population = population;
		this.temp = temp;
	}

	public float getData(String dataMode) {
		if (dataMode.equals(GDP)) {
			return gdp;
		}

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

		return Float.NaN;
	}
}