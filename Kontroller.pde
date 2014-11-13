class Kontroller {
	private ElementGrid eg;
	private Label l;
	private ButtonManager bm;

	private String dataMode = DEFAULT_VAL;
	public final float MAP_PADDING_PCT = 0.9;

	// Dimensions of Sub Views
	private Rect mapDims;
	private Rect titleDims;
	private Rect buttonsDims;

	public final String[] VALID_DATA_MODES = {GDP, AREA, OBESITY_PCT, POPULATION, TEMP};

	public Kontroller(ArrayList<Element> elements, HashMap<String, Point> stateMap,
		Point dimensions, Rect bounds) {

		// Calculate bounds of children
		mapDims = new Rect(new Point(bounds.o.x, bounds.o.y + bounds.s.h * (1 - MAP_PADDING_PCT)), 
			new Size(bounds.s.w * MAP_PADDING_PCT, bounds.s.h * MAP_PADDING_PCT));
		titleDims = new Rect(new Point(bounds.o.x, bounds.o.y), 
			new Size(bounds.s.w, bounds.s.h - bounds.s.h * MAP_PADDING_PCT));
		buttonsDims = new Rect(new Point(mapDims.o.x + mapDims.s.w, mapDims.o.y),
			new Size(bounds.s.w - mapDims.s.w, bounds.s.h - titleDims.s.h));

		// Instantiate children
		eg = new ElementGrid(elements, stateMap, dimensions, mapDims);
		l = new Label(titleDims, "Fuck Off!");
		bm = new ButtonManager(VALID_DATA_MODES, buttonsDims);

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

	private void drawSeparators() {
		line(titleDims.o.x, titleDims.o.y + titleDims.s.h, titleDims.o.x + titleDims.s.w, titleDims.o.y + titleDims.s.h);
		line(mapDims.o.x + mapDims.s.w, mapDims.o.y, mapDims.o.x + mapDims.s.w, mapDims.o.y + mapDims.s.h);
	}

	// Highlights the button clicked on, checks to make sure clicked is
	// in the button managers jurisdiction
	public void clicked() {
	 if (buttonsDims.pointContained(mouseX, mouseY)) {
			ArrayList<Button> buttons = bm.getButtons();
			for (Button b : buttons) {
				if (b.pointContained(mouseX, mouseY)) {
					bm.setSelected(b);
					return;  // Get out in case of border
				}
			}
		}
	}

	public void render() {
		drawSeparators();
		eg.render();
		l.render();
		bm.render();
	}
}