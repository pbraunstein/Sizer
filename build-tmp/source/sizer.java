import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.Map; 
import java.util.Arrays; 

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
Kontroller k;
Button b;

public void setup() {
	size(1400, 800);

	// Create parser
	Parser p = new Parser("combined.csv", "StateMatrix.csv");
	ArrayList<Element>els = p.readInData();
	HashMap<String, Point> h = p.readInMap();

	k = new Kontroller(els, h, new Point(14, 6), new Rect(new Point(0, 0),
		new Size(width, height)));
	b = new Button(new Point(width / 2, 10), new Size(width / 2 - 10, height - 20), "plop");
}

public void draw() {
	background(255, 255, 255);
	k.render();
}
class Button extends Rect {
	private String label;
	public int FONT_SIZE = 30;

	public Button(Point o, Size s, String label) {
		super(o, s);
		this.label = label;
	}

	public void render() {
		line(o.x, o.y, o.x + s.w, o.y);
		line(o.x, o.y, o.x, o.y + s.h);
		line(o.x, o.y + s.h, o.x + s.w, o.y + s.h);
		line(o.x + s.w, o.y, o.x + s.w, o.y + s.h);
		textAlign(CENTER, CENTER);
		textSize(FONT_SIZE);
		fill(color(0, 0, 0));
		text(label, o.x + s.w / 2, o.y + s.h / 2);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String s) {
		label = s;
	}


}
public final String GDP = "GDP";
public final String AREA = "AREA";
public final String OBESITY_PCT = "OBESITY_PCT";
public final String POPULATION = "POPULATION";
public final String TEMP = "TEMP";
public final String DEFAULT_VAL = GDP;
public final int BLACK = color(0, 0, 0);

public final String[] VALID_DATA_MODES = {GDP, AREA, OBESITY_PCT, POPULATION, TEMP, DEFAULT_VAL};
class Element {
	public final String id;
	public final float gdp;
	public final float area;
	public final float obesityPct;
	public final float population;
	public final float temp;


	public Element(String id, float gdp, float area, float obesityPct,
		float population, float temp) {
		this.id = id;
		this.gdp = gdp;
		this.area = area;
		this.obesityPct = obesityPct;
		this.population = population;
		this.temp = temp;
	}

	public float getData(String dataMode) {
		if (dataMode.equals(GDP)) {
			return gdp;
		}

		if (dataMode.equals(AREA)) {
			return area;
		}

		if (dataMode.equals(OBESITY_PCT)) {
			return obesityPct;
		}

		if (dataMode.equals(POPULATION)) {
			return population;
		}

		if (dataMode.equals(TEMP)) {
			return temp;
		}

		return Float.NaN;
	}
}
class ElementGrid{
	public ArrayList<ElementView> elementViews;
	public Rect bounds;
	private Point dimensions;
	private float elemWidthPct;
	private float elemHeightPct;
	private String dataMode = DEFAULT_VAL;

	public final float PADDING_PCT = 0.02f;
	public final float BIGGEST_RAD = 64.0f;

	public ElementGrid(ArrayList<Element> elements, HashMap<String, Point>
		stateMap, Point dimensions, Rect bounds) {
		this.elementViews = new ArrayList<ElementView>();
		this.bounds = bounds;
		this.dimensions = dimensions;

		makeElementViews(elements, stateMap);
		calculateElemContainer();
		scaleRadii();

	}

	// Scales radii so bubbles fit on the screen
	private void scaleRadii() {
		float maxRad = getMaxInitRad();

		for (ElementView e : elementViews) {
			float currentRad = e.getRadius();
			e.setRadius((BIGGEST_RAD * currentRad) / maxRad);
		}

	}

	private float getMaxInitRad() {
		float toReturn = 0.0f;

		for (ElementView e : elementViews) {
			float currentRad = e.getRadius();

			if (currentRad > toReturn) {
				toReturn = currentRad;
			}
		}

		return toReturn;
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

	public void setDataMode(String s) {
		dataMode = s;
		for (ElementView ev : elementViews) {
			ev.setDataMode(s);
		}
		scaleRadii();  // OMG THIS WAS A GREAT DECISION
	}


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
 
}
class ElementView {
	public final Element element;
	private Point center;
	private float radius;
	public final Point index;
	private String dataMode = DEFAULT_VAL;

	public final float RADIUS_SCALE = 2;
	public final int FONT_SIZE = 14;
	public final int FILL_COLOR = color(50, 50, 50);
	public final int STROKE_COLOR = color(0, 0, 0);
	public final int TEXT_COLOR = color(255, 0, 0);

	// Defaults center to left corner
	public ElementView(Element element, Point index) {
		this.element = element;
		this.radius = this.element.getData(dataMode) * RADIUS_SCALE;
		this.center = new Point(0, 0);
		this.index = index;
	}

	public String getDataMode() {
		return dataMode;
	}

	public void setDataMode(String s) {
		dataMode = s;
		radius = element.getData(dataMode) * RADIUS_SCALE;
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
		textSize(FONT_SIZE);
		textAlign(CENTER, CENTER);
		text(element.id, center.x, center.y);

	}

	// public void tPrint() {
	// 	println("id = " + element.id);
	// 	println("data = " + element.data);
	// 	println("Index = (" + index.x + ", " + index.y + ")");
	// 	println();
	// }

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


	// Mostly used for debugging and silliness
	public final int PURPLE = color(128, 0, 128);
	public void fillPurple() {
		fill(PURPLE);
		rect(o.x, o.y, s.w, s.h);
	}
}
class Kontroller {
	private ElementGrid eg;
	private Label l;
	private String dataMode = DEFAULT_VAL;
	public final float MAP_PADDING_PCT = 0.9f;

	// Dimensions of Sub Views
	private Rect mapDims;
	private Rect titleDims;
	private Rect buttonsDims;

	public Kontroller(ArrayList<Element> elements, HashMap<String, Point> stateMap,
		Point dimensions, Rect bounds) {

		// Calculate bounds of children
		mapDims = new Rect(new Point(bounds.o.x, bounds.o.y + bounds.s.h * (1 - MAP_PADDING_PCT)), 
			new Size(bounds.s.w * MAP_PADDING_PCT, bounds.s.h * MAP_PADDING_PCT));
		titleDims = new Rect(new Point(bounds.o.x, bounds.o.y), 
			new Size(bounds.s.w, bounds.s.h - bounds.s.h * MAP_PADDING_PCT));

		// Instantiate children
		eg = new ElementGrid(elements, stateMap, dimensions, mapDims);
		l = new Label(titleDims, "Fuck Off!");

		setDataMode(AREA);
	}

	public ElementGrid getElementGrid() {
		return eg;
	}

	public String getDataMode() {
		return dataMode;
	}

	public void setDataMode(String s) {
		this.dataMode = s;

		// Make sure it is a valid mode
		if (!Arrays.asList(VALID_DATA_MODES).contains(s)) {
			println("ERROR: Invalid Data Mode: " + s);
			System.exit(1);
		}

		eg.setDataMode(s);
	}

	private void drawSeparators() {
		line(titleDims.o.x, titleDims.o.y + titleDims.s.h, titleDims.o.x + titleDims.s.w, titleDims.o.y + titleDims.s.h);
		line(mapDims.o.x + mapDims.s.w, mapDims.o.y, mapDims.o.x + mapDims.s.w, mapDims.o.y + mapDims.s.h);
	}

	public void render() {
		drawSeparators();
		eg.render();
		l.render();
	}
}
class Label {
	public final Rect bounds;
	private String labelText;
	private Point center;

	public Label(Rect bounds, String labelText) {
		this.bounds = bounds;
		this.labelText = labelText;
		center = calculateCenter();
	}

	private Point calculateCenter() {
		return new Point(bounds.o.x + bounds.s.w / 2, bounds.o.y + bounds.s.h / 2);
	}

	public String getLabelText() {
		return labelText;
	}

	public void setLabelText(String s) {
		labelText = s;
	}

	public void render() {
		textAlign(CENTER, CENTER);
		fill(BLACK);
		textSize(26);
		text(labelText, center.x, center.y);
	}


}
class Parser {
	public final String dataFile;
	public final String mapFile;

	// Data File Columns
	public final int ID_COL = 0;
	public final int GDP_COL = 1;
	public final int AREA_COL = 2;
	public final int OBESE_COL = 3;
	public final int POP_COL = 4;
	public final int TEMP_COL = 5;

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

			dieListe.add(new Element(listL[ID_COL], Float.parseFloat(listL[GDP_COL]),
				Float.parseFloat(listL[AREA_COL]), Float.parseFloat(listL[OBESE_COL]),
				Float.parseFloat(listL[POP_COL]), Float.parseFloat(listL[TEMP_COL])));

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
