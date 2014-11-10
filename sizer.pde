import java.util.Map;
import java.util.Arrays;

ElementGrid g;
Kontroller k;
Button b;

void setup() {
	size(1400, 800);

	// Create parser
	Parser p = new Parser("combined.csv", "StateMatrix.csv");
	ArrayList<Element>els = p.readInData();
	HashMap<String, Point> h = p.readInMap();

	k = new Kontroller(els, h, new Point(14, 6), new Rect(new Point(0, 0),
		new Size(width, height)));
	b = new Button(new Point(width / 2, 10), new Size(width / 2 - 10, height - 20), "plop");
}

void draw() {
	background(255, 255, 255);
	k.render();
	b.render();
}