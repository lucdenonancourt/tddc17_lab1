package tddc17;

public final class Coordinates {
	private int xpos;
	private int ypos;
	/**
	 * @param xpos
	 * @param ypos
	 */
	public Coordinates(int xpos, int ypos) {
		super();
		this.xpos = xpos;
		this.ypos = ypos;
	}
	
	public int getXpos() {
		return xpos;
	}

	public int getYpos() {
		return ypos;
	}
	
	public int distance(Coordinates c2) {
		return c2.getXpos() - xpos + c2.getYpos() - ypos;
	}

}
