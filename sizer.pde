ElementGrid g;

void setup() {
	size(600, 800);

	// Create parser
	Parser p = new Parser("AvgTemps.csv");
	ArrayList<Element>els = p.readIn();

	// Create Grid
	g = new ElementGrid(els, new Rect(new Point(width / 2, height / 2), 
		new Size(width, height)));

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