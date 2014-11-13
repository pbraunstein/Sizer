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