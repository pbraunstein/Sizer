class Button extends Rect {
	private String label;
	public int FONT_SIZE = 20;

	public Button(Point o, Size s, String label) {
		super(o, s);
		this.label = label;
	}

	public void render() {
		line(o.x, o.y, o.x + s.w, o.y);
		line(o.x, o.y, o.x, o.y + s.h);
		line(o.x, o.y + s.h, o.x + s.w, o.y + s.h);
		line(o.x + s.w, o.y, o.x + s.w, o.y + s.h);
		textAlign(CENTER, CENTER);
		textSize(FONT_SIZE);
		fill(BLACK);
		text(label, o.x + s.w / 2, o.y + s.h / 2);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String s) {
		label = s;
	}

	// Returns true if the x y coord submitted
	// is in the bounds of the button
	public boolean hit(float x, float y) {
		if (x <= (o.x + s.w) && x >= o.x ) {
			if (y <= (o.y + s.h) && y >= o.y) {
				return true;
			}
		}
		return false;
	}
}