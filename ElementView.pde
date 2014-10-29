class ElementView {
	public final Element element;
	private Point center;
	public float radius;

	public final float RADIUS_SCALE = 1;
	public final color FILL_COLOR = color(50, 50, 50);
	public final color STROKE_COLOR = color(0, 0, 0);

	// Defaults center to left corner
	public ElementView(Element element) {
		this.element = element;
		this.radius = this.element.data * RADIUS_SCALE;
		this.center = new Point(0, 0);
	}

	public Point getCenter() {
		return center;
	}

	public void setCenter(Point p) {
		this.center = p;
	}

	public void render() {
		ellipseMode(CENTER);  // First 2 params center, second two width & height
		stroke(STROKE_COLOR);
		fill(FILL_COLOR);
		ellipse(center.x, center.y, radius, radius);
	}

}