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

Element e;
ElementView v;
ElementGrid g;

public void setup() {
	size(600, 800);
	g = new Parser("AvgTemps.csv").readIn();
	printTest(g.elements);
	e = new Element("Colorado", 45.1f);
	v = new ElementView(e);
	v.setCenter(new Point(width / 2, height / 2));

}

public void printTest(ArrayList<Element> l) {
	for (Element e : l) {
		println("ID = " + e.id + " , Temp = " + e.data);
	}
}

public void draw() {
	background(255, 255, 255);
	v.draw();

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
	public ArrayList<Element> elements;
	public ElementGrid(ArrayList<Element> elements) {
		this.elements = elements;
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

	public void draw() {
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
	public Point origin;
	public Size size;
}
class Parser {
	public final String file;

	public final int ID_COLUMN = 0;
	public final int VAL_COLUMN = 1;

	public Parser(String file) {
		this.file = file;
	}

	public ElementGrid readIn() {
		String[] lines = loadStrings(file);
		ArrayList<Element> dieListe = new ArrayList<Element>();

		for (String l : lines) {
			if (l.startsWith("#")) {
				continue;  // It is the header
			}

			String[] listL = split(l, ',');

			dieListe.add(new Element(listL[ID_COLUMN], Float.parseFloat(listL[VAL_COLUMN])));

		}

		return new ElementGrid(dieListe);
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
