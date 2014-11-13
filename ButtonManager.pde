class ButtonManager {
	private ArrayList<Button> buttons;
	private Rect bounds;
	private Button selected;

	public final float PCT_X_PADDING = 0;
	public final float PCT_Y_PADDING = 0;


	public ButtonManager(String[] buttonsToMake, Rect bounds) {
		this.bounds = bounds;
		makeButtons(buttonsToMake);
		selected = buttons.get(2);
	}

	private void makeButtons(String[] buttonsToMake) {
		buttons = new ArrayList<Button>();

		Size buttonSize = getButtonSize(buttonsToMake.length);

		float xCoord = bounds.o.x + bounds.o.x * PCT_X_PADDING;
		float yCoord = bounds.o.y;

		for (String s : buttonsToMake) {
			buttons.add(new Button(new Point(xCoord, yCoord),
				buttonSize, s));

			yCoord += buttonSize.h + buttonSize.h * PCT_Y_PADDING;
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
		float w = bounds.s.w - (bounds.s.w * PCT_X_PADDING);
		float h = (bounds.s.h / numButtons) - (bounds.s.h / numButtons) * PCT_Y_PADDING;

		return new Size(w, h);
	}

	public void render() {
		for (Button b : buttons) {
			if (b == selected) {
				b.render(true);
			} else {
				b.render(false);
			}
		}
	}
}