class Kontroller {
	private ElementGrid eg;
	private String dataMode = DEFAULT_VAL;

	public Kontroller(ArrayList<Element> elements, HashMap<String, Point> stateMap,
		Point dimensions, Rect bounds) {
		Rect newBounds = new Rect(new Point(bounds.o.x, bounds.o.y / (bounds.s.h * 0.1)), 
			new Size(bounds.s.w * 0.9, bounds.s.h));
		eg = new ElementGrid(elements, stateMap, dimensions, newBounds);
		setDataMode(AREA);
	}

	public ElementGrid getElementGrid() {
		return eg;
	}

	public String getDataMode() {
		return dataMode;
	}

	public void setDataMode(String s) {
		this.dataMode = s;

		// Make sure it is a valid mode
		if (!Arrays.asList(VALID_DATA_MODES).contains(s)) {
			println("ERROR: Invalid Data Mode: " + s);
			System.exit(1);
		}

		eg.setDataMode(s);
	}

	public void render() {
		eg.render();
	}
}