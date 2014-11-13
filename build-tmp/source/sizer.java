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
	Parser p = new Parser("combined_2.csv", "StateMatrix.csv");
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
	public int FONT_SIZE = 20;

	public Button(Point o, Size s, String label) {
		super(o, s);
		this.label = label;
	}


	// Inverts colors if a button is selected
	public void render(boolean selected) {
		line(o.x, o.y, o.x + s.w, o.y);
		line(o.x, o.y, o.x, o.y + s.h);
		line(o.x, o.y + s.h, o.x + s.w, o.y + s.h);
		line(o.x + s.w, o.y, o.x + s.w, o.y + s.h);
		if (selected) {
			fill(BLACK);
			rect(o.x, o.y, s.w, s.h);
			fill(WHITE);
			text(label, o.x + s.w / 2, o.y + s.h / 2);
			return;
		}	

		textAlign(CENTER, CENTER);
		textSize(FONT_SIZE);
		fill(BLACK);
		text(label, o.x + s.w / 2, o.y + s.h / 2);
		
		fill(BLACK);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String s) {
		label = s;
	}

	// Returns true if the x y coord submitted
	// is in the bounds of the button
	public boolean hit(float x, float y) {
		if (x <= (o.x + s.w) && x >= o.x ) {
			if (y <= (o.y + s.h) && y >= o.y) {
				return true;
			}
		}
		return false;
	}
}
class ButtonManager {
	private ArrayList<Button> buttons;
	private Rect bounds;
	private Button selected;


	// Makes first button selected by default
	public ButtonManager(String[] buttonsToMake, Rect bounds) {
		this.bounds = bounds;
		makeButtons(buttonsToMake);
		selected = buttons.get(0);
	}

	private void makeButtons(String[] buttonsToMake) {
		buttons = new ArrayList<Button>();

		Size buttonSize = getButtonSize(buttonsToMake.length);

		float xCoord = bounds.o.x;
		float yCoord = bounds.o.y;

		for (String s : buttonsToMake) {
			buttons.add(new Button(new Point(xCoord, yCoord),
				buttonSize, s));

			yCoord += buttonSize.h;
		}
	}

	public Button getSelected() {
		return selected;
	}

	public void setSelected(Button b) {
		selected = b;
	}

	public ArrayList<Button> getButtons() {
		return buttons;
	}

	private Size getButtonSize(int numButtons) {
		float w = bounds.s.w;
		float h = (bounds.s.h / numButtons);

		return new Size(w, h);
	}

	public void render() {
		bounds.fillColor(WHITE);  // To overlap out of bounds spheres
		for (Button b : buttons) {
			if (b == selected) {
				b.render(true);
			} else {
				b.render(false);
			}
		}
	}
}
public final String GDP = "GSP";
public final String AREA = "Area";
public final String OBESITY_PCT = "Obesity";
public final String POPULATION = "Population";
public final String TEMP = "Temp";
public final String GSP_P_CAP = "GSP_Cap";
public final String DEFAULT_VAL = AREA;
public final int BLACK = color(0, 0, 0);
public final int WHITE = color(255, 255, 255);


class Element {
	public final String id;
	public final float area;
	public final float obesityPct;
	public final float population;
	public final float temp;
	public final float gspPC;


	public Element(String id, float area, float obesityPct,
		float population, float temp, float gspPC) {
		this.id = id;
		this.area = area;
		this.obesityPct = obesityPct;
		this.population = population;
		this.temp = temp;
		this.gspPC = gspPC;
	}

	public float getData(String dataMode) {
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

		if (dataMode.equals(GSP_P_CAP)) {
			return gspPC;
		}

		println("ERROR: Problem getting data out of Element");

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

	public String toString() {
		return "Origin = (" + o.x + ", " + o.y + ")\n" + "Size = (" +
			s.w + ", " + s.h + ")";
	}

	public boolean pointContained(float x, float y) {
		if (x >= o.x && x <= (o.x + s.w)) {
			if (y >= o.y && y <= (o.y + s.h)) {
				return true;
			}
		}
		return false;
	}

	public void fillColor(int toFill) {
		fill(toFill);
		rect(o.x, o.y, s.w, s.h);
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
	private ButtonManager bm;

	private String dataMode = DEFAULT_VAL;
	public final float MAP_PADDING_PCT = 0.9f;

	// Dimensions of Sub Views
	private Rect mapDims;
	private Rect titleDims;
	private Rect buttonsDims;

	public final String[] VALID_DATA_MODES = {AREA, OBESITY_PCT, POPULATION, TEMP, GSP_P_CAP};

	public Kontroller(ArrayList<Element> elements, HashMap<String, Point> stateMap,
		Point dimensions, Rect bounds) {

		// Calculate bounds of children
		mapDims = new Rect(new Point(bounds.o.x, bounds.o.y + bounds.s.h * (1 - MAP_PADDING_PCT)), 
			new Size(bounds.s.w * MAP_PADDING_PCT, bounds.s.h * MAP_PADDING_PCT));
		titleDims = new Rect(new Point(bounds.o.x, bounds.o.y), 
			new Size(bounds.s.w, bounds.s.h - bounds.s.h * MAP_PADDING_PCT));
		buttonsDims = new Rect(new Point(mapDims.o.x + mapDims.s.w, mapDims.o.y),
			new Size(bounds.s.w - mapDims.s.w, bounds.s.h - titleDims.s.h));

		// Instantiate children
		eg = new ElementGrid(elements, stateMap, dimensions, mapDims);
		l = new Label(titleDims, "Fuck Off!");
		bm = new ButtonManager(VALID_DATA_MODES, buttonsDims);

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

	// Highlights the button clicked on, checks to make sure clicked is
	// in the button managers jurisdiction.
	// Sets the elementGrid mote to be whatever button was pushed
	public void clicked() {
	 if (buttonsDims.pointContained(mouseX, mouseY)) {
			ArrayList<Button> buttons = bm.getButtons();
			for (Button b : buttons) {
				if (b.pointContained(mouseX, mouseY)) {
					bm.setSelected(b);
					int modeIndex = buttons.indexOf(b);
					setDataMode(VALID_DATA_MODES[modeIndex]);
					return;
				}
			}

		}
	}

	public void render() {
		drawSeparators();
		eg.render();
		l.render();
		bm.render();
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
	public final int AREA_COL = 1;
	public final int OBESE_COL = 2;
	public final int POP_COL = 3;
	public final int TEMP_COL = 4;
	public final int GSP_P_CAP_COL = 5;

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

			dieListe.add(new Element(listL[ID_COL], Float.parseFloat(listL[AREA_COL]),
				Float.parseFloat(listL[OBESE_COL]), Float.parseFloat(listL[POP_COL]),
				Float.parseFloat(listL[TEMP_COL]), Float.parseFloat(listL[GSP_P_CAP_COL])));

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
public void mouseClicked() {
	k.clicked();
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
