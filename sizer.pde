ElementGrid g;

void setup() {
	size(600, 800);

	// Create parser
	Parser p = new Parser("AvgTemps.csv");
	ArrayList<Element>els = p.readIn();

	// Create Grid
	float offset = 100;
	g = new ElementGrid(els, new Rect(new Point(offset, offset * 2), 
		new Size(width - offset, height - offset * 2)));
	// println("Width = " + (width - offset) + ", Height = " + (height - offset));

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