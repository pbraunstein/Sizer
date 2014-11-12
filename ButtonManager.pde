class ButtonManager {
	private ArrayList<Button> buttons;
	private Rect bounds;

	public final float PCT_X_PADDING = 0.01;
	public final float PCT_Y_PADDING = 0.01;


	public ButtonManager(String[] buttonsToMake, Rect bounds) {
		this.bounds = bounds;
		makeButtons(buttonsToMake);
	}

	private void makeButtons(String[] buttonsToMake) {
		buttons = new ArrayList<Button>();

		Size buttonSize = getButtonSize(buttonsToMake.length);

		float xCoord = bounds.o.x + bounds.o.x * PCT_X_PADDING;
		float yCoord = bounds.o.y + bounds.o.y * PCT_Y_PADDING;

		for (String s : buttonsToMake) {
			buttons.add(new Button(new Point(xCoord, yCoord),
				buttonSize, s));

			yCoord += buttonSize.h + buttonSize.h * PCT_Y_PADDING;
		}
	}

	private Size getButtonSize(int numButtons) {
		float w = bounds.s.w - (bounds.s.w * PCT_X_PADDING);
		float h = bounds.s.h / numButtons;

		return new Size(w, h);
	}

	public void render() {
		for (Button b : buttons) {
			b.render();
		}
	}
}