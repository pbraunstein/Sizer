Element e;
ElementView v;
ElementGrid g;

void setup() {
	size(600, 800);
	g = new Parser("AvgTemps.csv").readIn();
	printTest(g.elements);
	e = new Element("Colorado", 45.1);
	v = new ElementView(e);
	v.setCenter(new Point(width / 2, height / 2));

}

void printTest(ArrayList<Element> l) {
	for (Element e : l) {
		println("ID = " + e.id + " , Temp = " + e.data);
	}
}

void draw() {
	background(255, 255, 255);
	v.draw();

}