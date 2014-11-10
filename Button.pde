class Button extends Rect {
	private String label;
	public int FONT_SIZE = 30;

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
		fill(color(0, 0, 0));
		text(label, o.x + s.w / 2, o.y + s.h / 2);
	}


}