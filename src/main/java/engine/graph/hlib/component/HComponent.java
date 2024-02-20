package engine.graph.hlib.component;

import org.joml.Vector2i;

import engine.graph.hlib.border.Border;
import engine.graph.hlib.event.EventMulticaster;
import engine.graph.hlib.event.KeyEvent;
import engine.graph.hlib.event.MouseEvent;
import engine.graph.hlib.event.listener.KeyListener;
import engine.graph.hlib.event.listener.MouseListener;
import engine.graph.hlib.graphics.Graphics;
import engine.graph.hlib.graphics.paint.Color;
import engine.graph.hlib.utils.Background;
import engine.graph.hlib.utils.HLib;
import engine.graph.hlib.utils.Insets;

/**
 * The base class of all the graphics elements, she contains data for position,
 * size, border, background, focusing, visibility, id. And all the key and mouse
 * listener like mouseClicked, mouseReleassed..
 * 
 * All the data can be obtained by getters and modified by setters.
 * 
 * @author Hpn4
 *
 */
public abstract class HComponent {

	/**
	 * Color often used
	 */
	protected final static Color colorPressed = Color.GRAY;

	protected final static Color colorEntered = new Color(243, 214, 23);

	protected final static Color colorDefault = new Color(204, 155, 82);

	protected final static Color colorDisabled = Color.DARKGRAY;

	/**
	 * Position and size
	 */
	private int x;

	private int y;

	private int width;

	private int height;

	/**
	 * ID of this component, it's used for remove
	 */
	private long id;

	/**
	 * Margin, margout, border and background
	 */
	private Insets margin;

	private Insets margout;

	private Border border;

	private Background bg;

	/**
	 * All the MouseListener
	 */
	private MouseListener mouseClicked;

	private MouseListener mousePressed;

	private MouseListener mouseReleased;

	private MouseListener mouseEnter;

	private MouseListener mouseExit;

	private MouseListener mouseMove;

	private MouseListener mouseWheel;

	/**
	 * All the KeyListener
	 */
	private KeyListener keyPressed;

	private KeyListener keyReleased;

	private KeyListener keyTyped;

	/**
	 * Boolean value for visibility, enabling, focusing...
	 */
	private boolean enabled;

	private boolean visible;

	private boolean focused;

	private boolean recalcSizeNeeded;

	private boolean enter;

	private boolean isMouseClicked;

	/**
	 * The default constructor of {@code HComponent}. He initialize the id of the
	 * component. Sets his visibility and his focusing to {@code false}. And his
	 * margin and margout are each equal to one;
	 */
	public HComponent() {
		setId(System.nanoTime());
		focused = false;
		enabled = true;
		noMarge();
	}

	/**
	 ******************************
	 ************ PAINT ***********
	 ******************************
	 */
	public void paint(final Graphics g) {
		final Vector2i origin = g.getOrigin();

		g.translateOrigin(margout.left, margout.top);
		if (bg != null)
			bg.paintBackground(this, g, 0, 0, width - margout.getWidth(), height - margout.getHeight());

		if (border != null) {
			border.paintBorder(this, g, 0, 0, width - margout.getWidth(), height - margout.getHeight());
			final Insets borderIn = border.getBorderInsets();
			g.translateOrigin(borderIn.left, borderIn.top);
		}

		g.translateOrigin(margin.left, margin.top);
		paintComponent(g);
		g.moveOriginTo(origin);
	}

	/**
	 * Cette méthode et uniquement cette méthode est à redéfinir pour peindre un
	 * composant
	 * 
	 * @param g L'objet {@code Graphics} à utilisé pour peindre
	 */
	protected abstract void paintComponent(final Graphics g);

	protected abstract void initSize(final Graphics g);

	/**
	 *********************************
	 ************ CLEAN UP ***********
	 *********************************
	 */
	public void cleanUp(final long ctx) {
		if (border != null)
			border.cleanUp(ctx);

		if (bg != null)
			bg.cleanUp(ctx);
	}

	/**
	 **********************************
	 *********** MOUSE EVENT **********
	 **********************************
	 */
	protected void mouseClicked(final MouseEvent event) {
		HLib.setFocusOn(this);
		if (getMouseClicked() != null)
			getMouseClicked().fire(event);
	}

	protected void mousePressed(final MouseEvent event) {
		HLib.setFocusOn(this);
		if (getMousePressed() != null)
			getMousePressed().fire(event);
	}

	protected void mouseReleased(final MouseEvent event) {
		if (getMouseReleased() != null)
			getMouseReleased().fire(event);
	}

	protected void mouseEntered(final MouseEvent event) {
		if (getMouseEnter() != null)
			getMouseEnter().fire(event);
	}

	protected void mouseExited(final MouseEvent event) {
		if (getMouseExit() != null)
			getMouseExit().fire(event);
	}

	protected void mouseMove(final MouseEvent event) {
		if (getMouseMove() != null)
			getMouseMove().fire(event);
	}

	protected void mouseWheel(final MouseEvent event) {
		if (getMouseWheel() != null)
			getMouseWheel().fire(event);
	}

	/**
	 **********************************
	 ************ KEY EVENT ***********
	 **********************************
	 */
	public void keyPressed(final KeyEvent event) {
		if (getKeyPressed() != null)
			getKeyPressed().fire(event);
	}

	public void keyReleased(final KeyEvent event) {
		if (getKeyReleased() != null)
			getKeyReleased().fire(event);
	}

	public void keyTyped(final KeyEvent event) {
		if (getKeyTyped() != null)
			getKeyTyped().fire(event);
	}

	/**
	 ******************************
	 ************ UTILS ***********
	 ******************************
	 */
	public void fireEvent(final MouseEvent event) {
		final int x = event.getX(), y = event.getY();

		if (isVisible() && isInAreaOfComponent(x, y)) {

			if (event.isScrolling())
				mouseWheel(event);

			// Lorsque la souris entre dans le composant
			if (!enter) {
				enter = true;
				mouseEntered(event);
			}

			// Lorqu'un bouton est presse
			if (event.isPressed()) {
				mousePressed(event);
				isMouseClicked = !isMouseClicked ? true : isMouseClicked;

			} else if (event.isReleassed()) {
				if (isMouseClicked) {
					isMouseClicked = false;
					mouseClicked(event);
					mouseReleased(event);
				}
			}

		} else if (enter) { // Si la souris n'est pas dans le composant
			enter = false;
			mouseExited(event);
		}
	}

	public boolean isInAreaOfComponent(final int x, final int y) {
		if (isVisible())
			return x >= getX() && y >= getY() && x <= (getX() + getWidth()) && y <= (getY() + getHeight());
		return false;
	}

	public void infoPos() {
		System.out.println("x:" + getX() + ", y:" + getY() + ", xPaint:" + getPaintX() + ", yPaint:" + getPaintY()
				+ ", width:" + getWidth() + ", height:" + getHeight() + ", widthPaint:" + getPaintWidth()
				+ ", heightPaint:" + getPaintHeight());
	}

	public boolean equals(final Object obj) {
		return ((HComponent) obj).getId() == getId();
	}

	public int hashCode() {
		return Long.hashCode(getId());
	}

	/**
	 *****************************
	 ************ SIZE ***********
	 *****************************
	 */
	public void setSize(final int width, final int height) {
		setWidth(width);
		setHeight(height);
	}

	public void setWidth(final int width) {
		this.width = width;
	}

	public int getAllInsetsWidth() {
		int width = 0;
		if (getMargout() != null)
			width += getMargout().getWidth();
		if (getBorder() != null)
			width += getBorder().getBorderInsets().getWidth();
		if (getMargin() != null)
			width += getMargin().getWidth();

		return width;
	}

	public int getPaintWidth() {
		return width - getAllInsetsWidth();
	}

	/**
	 * Return the total width of this component (margin + width + margout)
	 * 
	 * @return The total width
	 * @see #getWidth()
	 */
	public int getWidth() {
		return width;
	}

	public void setHeight(final int height) {
		this.height = height;
	}

	public int getAllInsetsHeight() {
		int height = 0;
		if (getMargout() != null)
			height += getMargout().getHeight();
		if (getBorder() != null)
			height += getBorder().getBorderInsets().getHeight();
		if (getMargin() != null)
			height += getMargin().getHeight();

		return height;
	}

	public int getPaintHeight() {
		return height - getAllInsetsHeight();
	}

	/**
	 * Return the total height of this component (margin + height + margout) <br>
	 * <ul>
	 * <li>In yellow the size of the component</li>
	 * <li>In red the size of the margout</li>
	 * <li>In Blue the size of the border</li>
	 * <li>In green the margin</li>
	 * <li>In white the painted component</li>
	 * </ul>
	 * <div style="border:2px solid green;box-shadow: 0 0 0 2px blue, 0 0 0 4px red,
	 * 0 0 0 5px
	 * yellow;width:40px;height:20px;background-color:white;float:right;margin: 0
	 * 10px 0 0"></div>
	 * 
	 * @return The total height
	 * @see #getHeight()
	 *
	 */
	public int getHeight() {
		return height;
	}

	/**
	 *********************************
	 ************ POSITION ***********
	 *********************************
	 */
	public void setPos(final int x, final int y) {
		setX(x);
		setY(y);
	}

	public void setX(final int x) {
		this.x = x;
	}

	public void setY(final int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getPaintX() {
		int x = this.x;
		if (getMargout() != null)
			x += getMargout().left;
		if (getBorder() != null)
			x += getBorder().getBorderInsets().left;
		if (getMargin() != null)
			x += getMargin().left;
		return x;
	}

	public int getAllInsetsXMax() {
		int xMax = 0;
		if (getMargout() != null)
			xMax += getMargout().right;
		if (getBorder() != null)
			xMax += getBorder().getBorderInsets().right;
		if (getMargin() != null)
			xMax += getMargin().right;

		return xMax;
	}

	public int getY() {
		return y;
	}

	public int getPaintY() {
		int y = this.y;
		if (getMargout() != null)
			y += getMargout().top;
		if (getBorder() != null)
			y += getBorder().getBorderInsets().top;
		if (getMargin() != null)
			y += getMargin().top;
		return y;
	}

	public int getAllInsetsYMax() {
		int yMax = 0;
		if (getMargout() != null)
			yMax += getMargout().bot;
		if (getBorder() != null)
			yMax += getBorder().getBorderInsets().bot;
		if (getMargin() != null)
			yMax += getMargin().bot;

		return yMax;
	}

	public void setBounds(final int x, final int y, final int width, final int height) {
		setPos(x, y);
		setSize(width, height);
	}

	/**
	 ********************************
	 ************** ID **************
	 ********************************
	 */
	public void setId(final long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	/**
	 ********************************
	 ************ ENABLED ***********
	 ********************************
	 */
	public void setEnabled(final boolean enable) {
		this.enabled = enable;
	}

	public boolean isEnabled() {
		return enabled;
	}

	/**
	 ********************************
	 ************ VISIBLE ***********
	 ********************************
	 */
	public void setVisible(final boolean visible) {
		this.visible = visible;
	}

	public boolean isVisible() {
		return visible;
	}

	/**
	 ******************************
	 ************ FOCUS ***********
	 ******************************
	 */
	public boolean isFocused() {
		return focused;
	}

	public void setFocus(final boolean focused) {
		this.focused = focused;
	}

	/**
	 ************************************
	 ************ RECALC SIZE ***********
	 ************************************
	 */
	public boolean isRecalcSizeNeeded() {
		return recalcSizeNeeded;
	}

	public void setRecalcSizeNeeded(final boolean recalcSizeNeeded) {
		this.recalcSizeNeeded = recalcSizeNeeded;
	}

	/**
	 *****************************************
	 ************ MARGIN & MARGOUT ***********
	 *****************************************
	 */
	public void setMarge(final Insets margin, final Insets margout) {
		setMargin(margin);
		setMargout(margout);
	}

	public void noMarge() {
		final Insets zero = new Insets(0);
		setMargin(zero);
		setMargout(zero);
	}

	public void setMargin(final Insets margin) {
		this.margin = margin;
	}

	public void setMargout(final Insets margout) {
		this.margout = margout;
	}

	public Insets getMargin() {
		return margin;
	}

	public Insets getMargout() {
		return margout;
	}

	/**
	 ********************************************
	 ************ BORDER & BACKGROUND ***********
	 ********************************************
	 */
	public void setBorder(final Border border) {
		this.border = border;
	}

	public void setBackground(final Background bg) {
		this.bg = bg;
	}

	public Border getBorder() {
		return border;
	}

	public Background getBackground() {
		return bg;
	}

	/**
	 ***************************************
	 ************ MOUSE LISTENER ***********
	 ***************************************
	 */
	public void addMouseEnter(final MouseListener mouseEnter) {
		this.mouseEnter = EventMulticaster.add(this.mouseEnter, mouseEnter);
	}

	public void addMouseExit(final MouseListener mouseExit) {
		this.mouseExit = EventMulticaster.add(this.mouseExit, mouseExit);
	}

	public void addMouseMove(final MouseListener mouseMove) {
		this.mouseMove = EventMulticaster.add(this.mouseMove, mouseMove);
	}

	public void addMouseWheel(final MouseListener mouseWheel) {
		this.mouseWheel = EventMulticaster.add(this.mouseWheel, mouseWheel);
	}

	public void addMouseClicked(final MouseListener mouseClicked) {
		this.mouseClicked = EventMulticaster.add(this.mouseClicked, mouseClicked);
	}

	public void addMousePressed(final MouseListener mousePressed) {
		this.mousePressed = EventMulticaster.add(this.mousePressed, mousePressed);
	}

	public void addMouseReleased(final MouseListener mouseReleased) {
		this.mouseReleased = EventMulticaster.add(this.mouseReleased, mouseReleased);
	}

	public void removeMouseEnter(final MouseListener mouseEnter) {
		this.mouseEnter = EventMulticaster.remove(this.mouseEnter, mouseEnter);
	}

	public void removeMouseExit(final MouseListener mouseExit) {
		this.mouseExit = EventMulticaster.remove(this.mouseExit, mouseExit);
	}

	public void removeMouseMove(final MouseListener mouseMove) {
		this.mouseMove = EventMulticaster.remove(this.mouseMove, mouseMove);
	}

	public void removeMouseWheel(final MouseListener mouseWheel) {
		this.mouseWheel = EventMulticaster.remove(this.mouseWheel, mouseWheel);
	}

	public void removeMouseClicked(final MouseListener mouseClicked) {
		this.mouseClicked = EventMulticaster.remove(this.mouseClicked, mouseClicked);
	}

	public void removeMousePressed(final MouseListener mousePressed) {
		this.mousePressed = EventMulticaster.remove(this.mousePressed, mousePressed);
	}

	public void removeMouseReleased(final MouseListener mouseReleased) {
		this.mouseReleased = EventMulticaster.remove(this.mouseReleased, mouseReleased);
	}

	public MouseListener getMouseEnter() {
		return mouseEnter;
	}

	public MouseListener getMouseExit() {
		return mouseExit;
	}

	public MouseListener getMouseMove() {
		return mouseMove;
	}

	public MouseListener getMouseWheel() {
		return mouseWheel;
	}

	public MouseListener getMouseClicked() {
		return mouseClicked;
	}

	public MouseListener getMousePressed() {
		return mousePressed;
	}

	public MouseListener getMouseReleased() {
		return mouseReleased;
	}

	/**
	 *************************************
	 ************ KEY LISTENER ***********
	 *************************************
	 */
	public void addKeyPressed(final KeyListener keyPressed) {
		this.keyPressed = EventMulticaster.add(this.keyPressed, keyPressed);
	}

	public void addKeyReleased(final KeyListener keyReleased) {
		this.keyReleased = EventMulticaster.add(this.keyReleased, keyReleased);
	}

	public void addKeyTyped(final KeyListener keyTyped) {
		this.keyTyped = EventMulticaster.add(this.keyTyped, keyTyped);
	}

	public void removeKeyPressed(final KeyListener keyPressed) {
		this.keyPressed = EventMulticaster.remove(this.keyPressed, keyPressed);
	}

	public void removeKeyReleased(final KeyListener keyReleased) {
		this.keyReleased = EventMulticaster.remove(this.keyReleased, keyReleased);
	}

	public void removeKeyTyped(final KeyListener keyTyped) {
		this.keyTyped = EventMulticaster.remove(this.keyTyped, keyTyped);
	}

	public KeyListener getKeyPressed() {
		return keyPressed;
	}

	public KeyListener getKeyReleased() {
		return keyReleased;
	}

	public KeyListener getKeyTyped() {
		return keyTyped;
	}
}