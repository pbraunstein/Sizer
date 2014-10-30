class ElementGrid{
	public ArrayList<ElementView> elementViews;
	public Rect bounds;
	private Point dimensions;
	private float elemWidthPct;
	private float elemHeightPct;

	public final float PADDING_PCT = 0.02;

	public ElementGrid(ArrayList<Element> elements, HashMap<String, Point>
		stateMap, Point dimensions, Rect bounds) {
		this.elementViews = new ArrayList<ElementView>();
		this.bounds = bounds;
		this.dimensions = dimensions;

		makeElementViews(elements, stateMap);
		calculateElemContainer();

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

	// public void render() {
	// 	float xPctUsed = 0.0;
	// 	float yPctUsed = 0.0;

	// 	// println("Bounds_X = " + bounds.s.w + ", Bounds_Y = " + bounds.s.h);

	// 	for (ElementView e : elementViews) {
	// 		e.setRadius(35);
	// 		float radius = e.getRadius();

	// 		float xPct = getXPct(radius) + PADDING_PCT;
	// 		float yPct = getYPct(radius) + PADDING_PCT;
	// 		float xCent = getXCoord(xPct + xPctUsed);
	// 		float yCent = getYCoord(yPct + yPctUsed);

	// 		// If the next element would fall off the screen -- recalculate
	// 		if (xPct + xPctUsed + getXPct(radius) + PADDING_PCT >= 1) { 
	// 			xPctUsed = 0.0;
	// 			yPctUsed += yPct * 2;
	// 			xCent = getXCoord(xPct + xPctUsed);
	// 			yCent = getYCoord(yPct + yPctUsed);
	// 		} else {
	// 			xPctUsed += xPct * 2; // Only update xPctUsed
	// 		}

	// 		// Bounds control for height just GTFO
	// 		if (yPct + yPctUsed + getYPct(radius) + PADDING_PCT >= 1) {
	// 			println("ERROR: Not enough room to display all elements in view");
	// 			System.exit(1);
	// 		}




	// 		e.setCenter(new Point(xCent, yCent));
	// 		e.render();	

	// 	}


	// }

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