class ElementGrid{
	public ArrayList<ElementView> elementViews;
	public Rect bounds;
	private Point dimensions;
	private float elemWidthPct;
	private float elemHeightPct;

	public final float PADDING_PCT = 0.02;
	public final float BIGGEST_RAD = 64.0;

	public ElementGrid(ArrayList<Element> elements, HashMap<String, Point>
		stateMap, Point dimensions, Rect bounds) {
		this.elementViews = new ArrayList<ElementView>();
		this.bounds = bounds;
		this.dimensions = dimensions;

		makeElementViews(elements, stateMap);
		calculateElemContainer();
		scaleRadii();

	}

	// Scales radii so bubbles fit on the screen
	private void scaleRadii() {
		float maxRad = getMaxInitRad();

		for (ElementView e : elementViews) {
			float currentRad = e.getRadius();
			e.setRadius((BIGGEST_RAD * currentRad) / maxRad);
		}

	}

	private float getMaxInitRad() {
		float toReturn = 0.0;

		for (ElementView e : elementViews) {
			float currentRad = e.getRadius();

			if (currentRad > toReturn) {
				toReturn = currentRad;
			}
		}

		return toReturn;
	}

	// Wraps each element in an ElementView and adds it to
	// the class variable
	private void makeElementViews(ArrayList<Element> elements, 
		HashMap<String, Point> stateMap) {
		for (Element e : elements) {
			Point toAdd = stateMap.get(e.id);
			elementViews.add(new ElementView(e, toAdd));
		}
	}


	// Calculates the perctange of the w and h of the
	// bounds that each element can use
	public void calculateElemContainer() {
		elemWidthPct = 1.0 / dimensions.x;
		elemHeightPct = 1.0 / dimensions.y;
	}

	public void render() {
		for (ElementView e : elementViews) {
			Point index = e.index;
			// e.setRadius(50);
			float rad = e.getRadius();
			float xInd = index.x;
			float yInd = index.y;

			float xCoord = getXCoord(elemWidthPct * xInd + getXPct(rad));
			float yCoord = getYCoord(elemHeightPct * yInd + getYPct(rad));

			e.setCenter(new Point(xCoord, yCoord));

			e.render();
		}
	}


	// Coordinate system is from 0 to 1, these functions
	// convert back to the real coordinate system
	public float getXCoord(float pctX) {
		return (pctX * bounds.s.w) + bounds.o.x; 
	}

	public float getYCoord(float pctY) {
		return (pctY * bounds.s.h) + bounds.o.y;
	}

	// Convert from real coordinate system to the 0 - 1
	// coordinates specified here
	public float getXPct(float xVal) {
		return (xVal) / bounds.s.w;
	}

	public float getYPct(float yVal) {
		return (yVal) / bounds.s.h;
	}

	public void tPrint() {
		for (ElementView e : elementViews) {
			e.tPrint();
		}
	}
 
}