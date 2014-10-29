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