class ElementGrid{
	public ArrayList<ElementView> elementViews;
	public Rect bounds;

	public final float PADDING_PCT = 0;

	public ElementGrid(ArrayList<Element> elements, Rect bounds) {
		this.elementViews = new ArrayList<ElementView>();
		makeElementViews(elements);
		this.bounds = bounds;
	}


	// Wraps each element in an ElementView and adds it to
	// the class variable
	private void makeElementViews(ArrayList<Element> elements) {
		for (Element e : elements) {
			elementViews.add(new ElementView(e));
		}
	}

	public void render() {
		float xPctUsed = 0.0;
		float yPctUsed = 0.0;

		// println("Bounds_X = " + bounds.s.w + ", Bounds_Y = " + bounds.s.h);

		for (ElementView e : elementViews) {
			e.setRadius(20);
			float radius = e.getRadius();

			float xPct = getXPct(radius) + PADDING_PCT;
			float yPct = getYPct(radius) + PADDING_PCT;
			float xCent = getXCoord(xPct + xPctUsed);
			float yCent = getYCoord(yPct + yPctUsed);

			// If the next element would fall off the screen -- recalculate
			if (xPct + xPctUsed + getXPct(radius) >= 1) { 
				xPctUsed = 0.0;
				yPctUsed += yPct * 2;
				xCent = getXCoord(xPct + xPctUsed);
				yCent = getYCoord(yPct + yPctUsed);
			} else {
				xPctUsed += xPct * 2; // Only update xPctUsed
			}




			e.setCenter(new Point(xCent, yCent));
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
 
}