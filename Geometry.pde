class Point {
	public final float x;
	public final float y;

	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}
}

class Size {
	public float w;
	public float h;

	public Size(float w, float h) {
		this.w = w;
		this.h = h;
	}

}


class Rect {
	public Point o;
	public Size s;

	public Rect(Point o, Size s) {
		this.o = o;
		this.s = s;
	}

	public String toString() {
		return "Origin = (" + o.x + ", " + o.y + ")\n" + "Size = (" +
			s.w + ", " + s.h + ")";
	}

	public boolean pointContained(float x, float y) {
		if (x >= o.x && x <= (o.x + s.w)) {
			if (y >= o.y && y <= (o.y + s.h)) {
				return true;
			}
		}
		return false;
	}


	// Mostly used for debugging and silliness
	public final color PURPLE = color(128, 0, 128);
	public void fillPurple() {
		fill(PURPLE);
		rect(o.x, o.y, s.w, s.h);
	}
}