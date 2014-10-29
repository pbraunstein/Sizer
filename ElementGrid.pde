class ElementGrid{
	public ArrayList<ElementView> elementViews;
	public Rect bounds;

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
		e.setCenter(new Point(getXCoord(0), getYCoord(0)));
		e.render();	
	}


	// Coordinate system is from 0 to 1, these functions
	// convert back to the real coordinate system
	public float getXCoord(float pctX) {
		return (pctX * bounds.s.w) + bounds.o.x; 
	}

	public float getYCoord(float pctY) {
		return (pctY * bounds.s.h) + bounds.o.y;
	}

}