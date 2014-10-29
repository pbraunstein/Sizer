ElementGrid g;

void setup() {
	size(1400, 800);

	// Create parser
	Parser p = new Parser("AvgTemps.csv");
	ArrayList<Element>els = p.readIn();

	// Create Grid
	float offset = 0;
	g = new ElementGrid(els, new Rect(new Point(offset, offset * 2), 
		new Size(width - offset, height - offset * 2)));

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