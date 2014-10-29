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
		ElementView e = elementViews.get(0);
		e.setRadius(100);
		float radius = e.getRadius();

		float xPct = getXPct(radius) + PADDING_PCT;
		float yPct = getYPct(radius) + PADDING_PCT;
		float xCent = getXCoord(xPct);
		float yCent = getYCoord(yPct);

		println("radius = " + radius);
		println("radius_Xpct = " + xPct);
		println("radius_Ypct = " + yPct);
		println("center_X = " + xCent);
		println("center_Y = " + yCent);

		e.setCenter(new Point(xCent, yCent));
		e.render();	
		//System.exit(1);
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
		return (xVal - bounds.o.x) / bounds.s.w;
	}

	public float getYPct(float yVal) {
		return (yVal - bounds.o.y) / bounds.s.h;
	}
 
}