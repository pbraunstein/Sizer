class ElementView {
	public final Element element;
	private Point center;
	private float radius;
	public final Point index;
	private String dataMode = DEFAULT_VAL;

	public final float RADIUS_SCALE = 2;
	public final int FONT_SIZE = 14;
	public final color FILL_COLOR = color(50, 50, 50);
	public final color STROKE_COLOR = color(0, 0, 0);
	public final color TEXT_COLOR = color(255, 0, 0);

	// Defaults center to left corner
	public ElementView(Element element, Point index) {
		this.element = element;
		this.radius = this.element.getData(dataMode) * RADIUS_SCALE;
		this.center = new Point(0, 0);
		this.index = index;
	}

	public String getDataMode() {
		return dataMode;
	}

	public void setDataMode(String s) {
		dataMode = s;
		radius = element.getData(dataMode) * RADIUS_SCALE;
	}

	public Point getCenter() {
		return center;
	}

	public void setCenter(Point p) {
		this.center = p;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float r) {
		this.radius = r;
	}

	public void render() {
		ellipseMode(CENTER);  // First 2 params center, second two width & height
		stroke(STROKE_COLOR);
		fill(FILL_COLOR);

		// Draw function takes in diameter, must scale radius
		ellipse(center.x, center.y, 2 * radius, 2 * radius);

		fill(TEXT_COLOR);
		textSize(FONT_SIZE);
		textAlign(CENTER, CENTER);
		text(element.id, center.x, center.y);

	}

}