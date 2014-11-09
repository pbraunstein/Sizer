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