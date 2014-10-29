import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

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
	size(600, 800);

	// Create parser
	Parser p = new Parser("AvgTemps.csv");
	ArrayList<Element>els = p.readIn();

	// Create Grid
	g = new ElementGrid(els, new Rect(new Point(width / 2, height / 2), 
		new Size(width, height)));

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

	public ElementGrid(ArrayList<Element> elements, Rect bounds) {
		this.elementViews = new ArrayList<ElementView>();
		makeElementViews(elements);
		this.bounds = bounds;
	}


	// Wraps each element in an ElementView and adds it to
	// the class variable
	private void makeElementViews(ArrayList<Element> elements) {
		for (Element e : elements) {
			elementViews.add(new ElementView(e));
		}
	}

	public void render() {
		ElementView e = elementViews.get(0);
		e.setCenter(new Point(getXCoord(0), getYCoord(0)));
		e.render();	
	}


	// Coordinate system is from 0 to 1, these functions
	// convert back to the real coordinate system
	public float getXCoord(float pctX) {
		return (pctX * bounds.s.w) + bounds.o.x; 
	}

	public float getYCoord(float pctY) {
		return (pctY * bounds.s.h) + bounds.o.y;
	}

}
class ElementView {
	public final Element element;
	private Point center;
	public float radius;

	public final float RADIUS_SCALE = 1;
	public final int FILL_COLOR = color(50, 50, 50);
	public final int STROKE_COLOR = color(0, 0, 0);

	// Defaults center to left corner
	public ElementView(Element element) {
		this.element = element;
		this.radius = this.element.data * RADIUS_SCALE;
		this.center = new Point(0, 0);
	}

	public Point getCenter() {
		return center;
	}

	public void setCenter(Point p) {
		this.center = p;
	}

	public void render() {
		ellipseMode(CENTER);  // First 2 params center, second two width & height
		stroke(STROKE_COLOR);
		fill(FILL_COLOR);
		ellipse(center.x, center.y, radius, radius);
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
	public final String file;

	public final int ID_COLUMN = 0;
	public final int VAL_COLUMN = 1;

	public Parser(String file) {
		this.file = file;
	}

	public ArrayList<Element> readIn() {
		String[] lines = loadStrings(file);
		ArrayList<Element> dieListe = new ArrayList<Element>();

		for (String l : lines) {
			if (l.startsWith("#")) {
				continue;  // It is the header
			}

			String[] listL = split(l, ',');

			dieListe.add(new Element(listL[ID_COLUMN], Float.parseFloat(listL[VAL_COLUMN])));

		}

		return dieListe;
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
