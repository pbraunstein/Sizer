import java.util.Map;

ElementGrid g;

void setup() {
	size(1400, 800);

	// Create parser
	Parser p = new Parser("area_abbd.csv", "StateMatrix.csv");
	ArrayList<Element>els = p.readInData();
	HashMap<String, Point> h = p.readInMap();

	// Create Grid
	float offset = 0;
	Rect bounds = new Rect(new Point(offset, offset * 2),
		new Size(width - offset, height - offset * 2));
	Point dims = new Point(14, 6);
	g = new ElementGrid(els, h, dims, bounds);

}

void printTest(HashMap<String, Point> t) {
	for (Map.Entry me : t.entrySet()) {
		Point p = (Point)me.getValue();
		println(me.getKey() + ": (" + p.x  + ", " + p.y + ")");
	}
}

void printTest(ArrayList<Element> l) {
	for (Element e : l) {
		println("ID = " + e.id + " , Temp = " + e.data);
	}
}

void draw() {
	background(255, 255, 255);
	g.render();

}