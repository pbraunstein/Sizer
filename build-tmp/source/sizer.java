import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.Map; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class sizer extends PApplet {



ElementGrid g;

public void setup() {
	size(1400, 800);

	// Create parser
	Parser p = new Parser("Obesity.csv", "StateMatrix.csv");
	ArrayList<Element>els = p.readInData();
	HashMap<String, Point> h = p.readInMap();

	// Create Grid
	float offset = 0;
	Rect bounds = new Rect(new Point(offset, offset * 2), 
		new Size(width - offset, height - offset * 2));
	Point dims = new Point(14, 6);
	g = new ElementGrid(els, h, dims, bounds);

}

public void printTest(HashMap<String, Point> t) {
	for (Map.Entry me : t.entrySet()) {
		Point p = (Point)me.getValue();
		println(me.getKey() + ": (" + p.x  + ", " + p.y + ")");
	}
}

public void printTest(ArrayList<Element> l) {
	for (Element e : l) {
		println("ID = " + e.id + " , Temp = " + e.data);
	}
}

public void draw() {
	background(255, 255, 255);
	g.render();

}
class Element {
	public final String id;
	public final float data;

	public Element(String id, float data) {
		this.id = id;
		this.data = data;
	}
}
class ElementGrid{
	public ArrayList<ElementView> elementViews;
	public Rect bounds;
	private Point dimensions;
	private float elemWidthPct;
	private float elemHeightPct;

	public final float PADDING_PCT = 0.02f;

	public ElementGrid(ArrayList<Element> elements, HashMap<String, Point>
		stateMap, Point dimensions, Rect bounds) {
		this.elementViews = new ArrayList<ElementView>();
		this.bounds = bounds;
		this.dimensions = dimensions;

		makeElementViews(elements, stateMap);
		calculateElemContainer();

	}

	// Wraps each element in an ElementView and adds it to
	// the class variable
	private void makeElementViews(ArrayList<Element> elements, 
		HashMap<String, Point> stateMap) {
		for (Element e : elements) {
			Point toAdd = stateMap.get(e.id);
			elementViews.add(new ElementView(e, toAdd));
		}
	}


	// Calculates the perctange of the w and h of the
	// bounds that each element can use
	public void calculateElemContainer() {
		elemWidthPct = 1.0f / dimensions.x;
		elemHeightPct = 1.0f / dimensions.y;
	}

	public void render() {
		for (ElementView e : elementViews) {
			Point index = e.index;
			// e.setRadius(50);
			float rad = e.getRadius();
			float xInd = index.x;
			float yInd = index.y;

			float xCoord = getXCoord(elemWidthPct * xInd + getXPct(rad));
			float yCoord = getYCoord(elemHeightPct * yInd + getYPct(rad));

			e.setCenter(new Point(xCoord, yCoord));

			e.render();
		}
	}

	// public void render() {
	// 	float xPctUsed = 0.0;
	// 	float yPctUsed = 0.0;

	// 	// println("Bounds_X = " + bounds.s.w + ", Bounds_Y = " + bounds.s.h);

	// 	for (ElementView e : elementViews) {
	// 		e.setRadius(35);
	// 		float radius = e.getRadius();

	// 		float xPct = getXPct(radius) + PADDING_PCT;
	// 		float yPct = getYPct(radius) + PADDING_PCT;
	// 		float xCent = getXCoord(xPct + xPctUsed);
	// 		float yCent = getYCoord(yPct + yPctUsed);

	// 		// If the next element would fall off the screen -- recalculate
	// 		if (xPct + xPctUsed + getXPct(radius) + PADDING_PCT >= 1) { 
	// 			xPctUsed = 0.0;
	// 			yPctUsed += yPct * 2;
	// 			xCent = getXCoord(xPct + xPctUsed);
	// 			yCent = getYCoord(yPct + yPctUsed);
	// 		} else {
	// 			xPctUsed += xPct * 2; // Only update xPctUsed
	// 		}

	// 		// Bounds control for height just GTFO
	// 		if (yPct + yPctUsed + getYPct(radius) + PADDING_PCT >= 1) {
	// 			println("ERROR: Not enough room to display all elements in view");
	// 			System.exit(1);
	// 		}




	// 		e.setCenter(new Point(xCent, yCent));
	// 		e.render();	

	// 	}


	// }

	// Coordinate system is from 0 to 1, these functions
	// convert back to the real coordinate system
	public float getXCoord(float pctX) {
		return (pctX * bounds.s.w) + bounds.o.x; 
	}

	public float getYCoord(float pctY) {
		return (pctY * bounds.s.h) + bounds.o.y;
	}

	// Convert from real coordinate system to the 0 - 1
	// coordinates specified here
	public float getXPct(float xVal) {
		return (xVal) / bounds.s.w;
	}

	public float getYPct(float yVal) {
		return (yVal) / bounds.s.h;
	}

	public void tPrint() {
		for (ElementView e : elementViews) {
			e.tPrint();
		}
	}
 
}
class ElementView {
	public final Element element;
	private Point center;
	private float radius;
	public final Point index;

	public final float RADIUS_SCALE = 2;
	public final int FILL_COLOR = color(50, 50, 50);
	public final int STROKE_COLOR = color(0, 0, 0);
	public final int TEXT_COLOR = color(255, 0, 0);

	// Defaults center to left corner
	public ElementView(Element element, Point index) {
		this.element = element;
		this.radius = this.element.data * RADIUS_SCALE;
		this.center = new Point(0, 0);
		this.index = index;
	}

	public Point getCenter() {
		return center;
	}

	public void setCenter(Point p) {
		this.center = p;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float r) {
		this.radius = r;
	}

	public void render() {
		ellipseMode(CENTER);  // First 2 params center, second two width & height
		stroke(STROKE_COLOR);
		fill(FILL_COLOR);

		// Draw function takes in diameter, must scale radius
		ellipse(center.x, center.y, 2 * radius, 2 * radius);

		fill(TEXT_COLOR);
		textAlign(CENTER, CENTER);
		text(element.id, center.x, center.y);

	}

	public void tPrint() {
		println("id = " + element.id);
		println("data = " + element.data);
		println("Index = (" + index.x + ", " + index.y + ")");
		println();
	}

}
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
}
class Parser {
	public final String dataFile;
	public final String mapFile;

	// Data File Columns
	public final int ID_COL = 0;
	public final int VAL_COL = 1;

	// Map File Columns
	public final int STATE_COL = 0;
	public final int X_COL = 1;
	public final int Y_COL = 2;


	public Parser(String dataFile, String mapFile) {
		this.dataFile = dataFile;
		this.mapFile = mapFile;
	}

	public ArrayList<Element> readInData() {
		String[] lines = loadStrings(dataFile);
		ArrayList<Element> dieListe = new ArrayList<Element>();

		for (String l : lines) {
			if (l.startsWith("#")) {
				continue;  // It is the header
			}

			String[] listL = split(l, ',');

			dieListe.add(new Element(listL[ID_COL], Float.parseFloat(listL[VAL_COL])));

		}

		return dieListe;
	}

	public HashMap<String, Point> readInMap() {
		String[] lines = loadStrings(mapFile);
		
		HashMap<String, Point> derTisch = new HashMap<String, Point>();

		for (String l : lines) {
			if (l.startsWith("#")) {
				continue;
			}

			String[] listL = split(l, ',');

			derTisch.put(listL[STATE_COL], new Point(
				Integer.parseInt(listL[X_COL]), Integer.parseInt(listL[Y_COL])));

		}

		return derTisch;

	}
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "sizer" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
