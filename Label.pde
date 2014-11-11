class Label {
	public final Rect bounds;
	private String labelText;
	private Point center;

	public Label(Rect bounds, String labelText) {
		this.bounds = bounds;
		this.labelText = labelText;
		center = calculateCenter();
	}

	private Point calculateCenter() {
		return new Point(bounds.o.x + bounds.s.w / 2, bounds.o.y + bounds.s.h / 2);
	}

	public String getLabelText() {
		return labelText;
	}

	public void setLabelText(String s) {
		labelText = s;
	}

	public void render() {
		textAlign(CENTER, CENTER);
		fill(BLACK);
		textSize(26);
		text(labelText, center.x, center.y);
	}


}