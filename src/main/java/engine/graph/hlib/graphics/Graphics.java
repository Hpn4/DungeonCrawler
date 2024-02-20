package engine.graph.hlib.graphics;

import static org.lwjgl.nanovg.NanoVG.nvgBeginFrame;
import static org.lwjgl.nanovg.NanoVG.nvgBeginPath;
import static org.lwjgl.nanovg.NanoVG.nvgCircle;
import static org.lwjgl.nanovg.NanoVG.nvgCreateFont;
import static org.lwjgl.nanovg.NanoVG.nvgEllipse;
import static org.lwjgl.nanovg.NanoVG.nvgEndFrame;
import static org.lwjgl.nanovg.NanoVG.nvgFill;
import static org.lwjgl.nanovg.NanoVG.nvgFillColor;
import static org.lwjgl.nanovg.NanoVG.nvgFillPaint;
import static org.lwjgl.nanovg.NanoVG.nvgFontBlur;
import static org.lwjgl.nanovg.NanoVG.nvgFontFace;
import static org.lwjgl.nanovg.NanoVG.nvgFontSize;
import static org.lwjgl.nanovg.NanoVG.nvgLineTo;
import static org.lwjgl.nanovg.NanoVG.nvgLinearGradient;
import static org.lwjgl.nanovg.NanoVG.nvgMoveTo;
import static org.lwjgl.nanovg.NanoVG.nvgRadialGradient;
import static org.lwjgl.nanovg.NanoVG.nvgRect;
import static org.lwjgl.nanovg.NanoVG.nvgRoundedRect;
import static org.lwjgl.nanovg.NanoVG.nvgRoundedRectVarying;
import static org.lwjgl.nanovg.NanoVG.nvgStroke;
import static org.lwjgl.nanovg.NanoVG.nvgStrokeColor;
import static org.lwjgl.nanovg.NanoVG.nvgStrokePaint;
import static org.lwjgl.nanovg.NanoVG.nvgStrokeWidth;
import static org.lwjgl.nanovg.NanoVG.nvgText;
import static org.lwjgl.nanovg.NanoVG.nvgTextAlign;
import static org.lwjgl.nanovg.NanoVG.nvgTextBounds;
import static org.lwjgl.nanovg.NanoVGGL3.NVG_ANTIALIAS;
import static org.lwjgl.nanovg.NanoVGGL3.NVG_STENCIL_STROKES;
import static org.lwjgl.nanovg.NanoVGGL3.nnvgDelete;
import static org.lwjgl.nanovg.NanoVGGL3.nvgCreate;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.FloatBuffer;

import org.joml.Vector2i;
import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.system.MemoryStack;

import engine.graph.hlib.component.HWindow;
import engine.graph.hlib.graphics.paint.Color;
import engine.graph.hlib.graphics.paint.Image;
import engine.graph.hlib.graphics.paint.Paint;
import engine.graph.hlib.graphics.paint.Paint2;
import engine.graph.hlib.utils.Rectangle;

public class Graphics {

	// Font
	private final static String FONT_NAME = "cyber";

	public final static int FONT_SIZE = 40;

	private float fontBlur;

	// NanoVG context
	private final long ctx;

	// Paint
	private Paint paint;

	private NVGPaint advancedPaint;

	private boolean useAdvancedPaint;

	private float strokeWidth;

	// Draw origin
	private final Vector2i origin;

	public Graphics(final HWindow window) throws Exception {
		origin = new Vector2i();

		// Create nanoVG context
		ctx = window.getOptions().antialiasing ? nvgCreate(NVG_ANTIALIAS | NVG_STENCIL_STROKES)
				: nvgCreate(NVG_STENCIL_STROKES);

		if (ctx == NULL)
			throw new Exception("Could not init nanovg");

		if (nvgCreateFont(ctx, FONT_NAME, "resources/font.ttf") == -1)
			throw new Exception("Could not add font");

		advancedPaint = NVGPaint.create();
	}

	/**
	 * NE PAS oublier de liberer l'objet {@code #Paint.cleanUp()}
	 * 
	 * @param paint
	 */
	public void setPaint(final Paint paint) {
		this.paint = paint;
		useAdvancedPaint = false;
	}

	public Paint getPaint() {
		return paint;
	}

	public void linearGradient(final int x, final int y, final int width, final int height, final Color colStart,
			final Color colEnd) {
		nvgLinearGradient(ctx, x, y, width, height, colStart.getColor(), colEnd.getColor(), advancedPaint);
		useAdvancedPaint = true;
	}

	public void radialGradient(final int x, final int y, final float innerR, final float outerR, final Color colStart,
			final Color colEnd) {
		nvgRadialGradient(ctx, x, y, innerR, outerR, colStart.getColor(), colEnd.getColor(), advancedPaint);
		useAdvancedPaint = true;
	}

	public void resetAdvancedPaint() {
		useAdvancedPaint = false;
	}

	/**
	 *********************************
	 ********** STROKE WIDTH *********
	 *********************************
	 */
	public void setStrokeWidth(final float strokeWidth) {
		this.strokeWidth = strokeWidth;
		nvgStrokeWidth(ctx, strokeWidth);
	}

	public float getStrokeWidth() {
		return strokeWidth;
	}

	/**
	 *********************************
	 ************* ORIGIN ************
	 *********************************
	 */
	public void moveOriginTo(final Vector2i origin) {
		moveOriginTo(origin.x, origin.y);
	}

	public void moveOriginTo(final int xOrigin, final int yOrigin) {
		origin.set(xOrigin, yOrigin);
	}

	public void translateOrigin(final Vector2i origin) {
		translateOrigin(origin.x, origin.y);
	}

	public void translateOrigin(final int xOrigin, final int yOrigin) {
		origin.add(xOrigin, yOrigin);
	}

	public Vector2i getOrigin() {
		return origin;
	}

	/**
	 ********************************
	 ************ UTILITY ***********
	 ********************************
	 */
	public void startRendering(final HWindow window) {
		nvgBeginFrame(ctx, window.getWidth(), window.getHeight(), 1);
		nvgFontFace(ctx, FONT_NAME);

		setStrokeWidth(2f);
	}

	public void endRendering(final HWindow window) {
		nvgEndFrame(ctx);
	}

	private void beginPath() {
		nvgBeginPath(ctx);
	}

	private void render(final boolean filled) {
		if (filled) {
			if (useAdvancedPaint)
				nvgFillPaint(ctx, advancedPaint);
			else {
				if (paint.isColor())
					nvgFillColor(ctx, ((Color) paint).getColor());
				else if (paint instanceof Paint2) {
					final Paint2 paint2 = (Paint2) paint;
					paint2.init(ctx, 0, 0);
					nvgFillPaint(ctx, paint2.getNVGPaint());
				}
			}
			nvgFill(ctx);
		} else {
			if (useAdvancedPaint)
				nvgStrokePaint(ctx, advancedPaint);
			else {
				if (paint.isColor())
					nvgStrokeColor(ctx, ((Color) paint).getColor());
				else if (paint instanceof Paint2) {
					final Paint2 paint2 = (Paint2) paint;
					paint2.init(ctx, 0, 0);
					nvgStrokePaint(ctx, paint2.getNVGPaint());
				}
			}
			nvgStroke(ctx);
		}
	}

	public void cleanUp() {
		advancedPaint.free();
		nnvgDelete(ctx);
	}

	/**
	 **********************************
	 ************ RECTANGLE ***********
	 **********************************
	 */
	public void fillRect(final int x, final int y, final int width, final int height) {
		renderRect(x, y, width, height, true);
	}

	public void drawRect(final int x, final int y, final int width, final int height) {
		renderRect(x, y, width, height, false);
	}

	private void renderRect(final int x, final int y, final int width, final int height, final boolean filled) {
		beginPath();
		nvgRect(ctx, x + origin.x, y + origin.y, width, height);
		render(filled);
	}

	/**
	 **********************************
	 ************ DRAW LINE ***********
	 **********************************
	 */
	public void drawLine(final float x, final float y, final float x2, final float y2) {
		beginPath();
		nvgMoveTo(ctx, x + origin.x, y + origin.y);
		nvgLineTo(ctx, x2 + origin.x, y2 + origin.y);
		render(false);
	}

	/**
	 *********************************
	 ************ TRIANGLE ***********
	 *********************************
	 */
	public void fillTriangle(final int x, final int y, final int width, final int height) {
		renderTriangle(x, y, width, height, true);
	}

	public void drawTriangle(final int x, final int y, final int width, final int height) {
		renderTriangle(x, y, width, height, false);
	}

	private void renderTriangle(final int x, final int y, final int width, final int height, final boolean filled) {
		final float xO = x + origin.x;
		final float yO = y + origin.y;

		beginPath();

		nvgMoveTo(ctx, xO, yO);
		nvgLineTo(ctx, xO + width, yO); // Top left corner
		nvgLineTo(ctx, xO, yO + height); // Top right corner
		nvgLineTo(ctx, xO, yO); // Bottom Left corner

		render(filled);
	}

	/**
	 *******************************
	 ************ CIRCLE ***********
	 *******************************
	 */
	public void fillCircle(final int centerX, final int centerY, final int radius) {
		renderCircle(centerX, centerY, radius, true);
	}

	public void drawCircle(final int centerX, final int centerY, final int radius) {
		renderCircle(centerX, centerY, radius, false);
	}

	private void renderCircle(final int centerX, final int centerY, final int radius, final boolean filled) {
		beginPath();
		nvgCircle(ctx, centerX + origin.x, centerY + origin.y, radius);
		render(filled);
	}

	/**
	 ***********************************
	 ************ ROUND RECT ***********
	 ***********************************
	 */
	public void fillRoundRect(final int x, final int y, final int width, final int height, final float cornerRadius) {
		renderRoundRect(x, y, width, height, cornerRadius, true);
	}

	public void drawRoundRect(final int x, final int y, final int width, final int height, final float cornerRadius) {
		renderRoundRect(x, y, width, height, cornerRadius, false);
	}

	private void renderRoundRect(final int x, final int y, final int width, final int height, final float cornerRadius,
			final boolean filled) {
		beginPath();
		nvgRoundedRect(ctx, x + origin.x, y + origin.y, width, height, cornerRadius);
		render(filled);
	}

	// With varying radius
	public void fillRoundRect(final int x, final int y, final int width, final int height, final float rX,
			final float rY, final float rW, final float rH) {
		renderRoundRect(x, y, width, height, rX, rY, rW, rH, true);
	}

	public void drawRoundRect(final int x, final int y, final int width, final int height, final float rX,
			final float rY, final float rW, final float rH) {
		renderRoundRect(x, y, width, height, rX, rY, rW, rH, false);
	}

	private void renderRoundRect(final int x, final int y, final int width, final int height, final float rX,
			final float rY, final float rW, final float rH, final boolean filled) {
		beginPath();
		nvgRoundedRectVarying(ctx, x, y, width, height, rX, rY, rW, rH);
		render(filled);
	}

	/**
	 ********************************
	 ************ ELLIPSE ***********
	 ********************************
	 */
	public void fillEllipse(final int centerX, final int centerY, final int xRadius, final int yRadius) {
		renderEllipse(centerX, centerY, xRadius, yRadius, true);
	}

	public void drawEllipse(final int centerX, final int centerY, final int xRadius, final int yRadius) {
		renderEllipse(centerX, centerY, xRadius, yRadius, false);
	}

	private void renderEllipse(final int centerX, final int centerY, final int xRadius, final int yRadius,
			final boolean filled) {
		beginPath();
		nvgEllipse(ctx, centerX + origin.x, centerY + origin.y, xRadius, yRadius);
		render(filled);
	}

	/**
	 *******************************
	 ************* TEXT ************
	 *******************************
	 */
	public void drawText(final int x, final int y, final String text) {
		beginPath();

		if (useAdvancedPaint)
			nvgFillPaint(ctx, advancedPaint);
		else {
			if (paint.isColor())
				nvgFillColor(ctx, ((Color) paint).getColor());
			else if (paint instanceof Paint2) {
				final Paint2 paint2 = (Paint2) paint;
				paint2.init(ctx, x, y);
				nvgFillPaint(ctx, paint2.getNVGPaint());
			}
		}

		nvgText(ctx, x + origin.x, y + origin.y, text);
	}

	public void drawText(final int x, final int y, final String text, final Paint paint, final int fontHeight) {
		setPaint(paint);
		setFontSize(fontHeight);
		drawText(x, y, text);
	}

	public void setFontSize(final float fontSize) {
		nvgFontSize(ctx, fontSize);
	}

	/**
	 ********************************
	 *********** FONT BLUR **********
	 ********************************
	 */
	public void setFontBLur(final float blur) {
		fontBlur = blur;
		nvgFontBlur(ctx, blur);
	}

	public float getFontBlur() {
		return fontBlur;
	}

	public Rectangle getFontBounds(final int x, final int y, final String text) {
		Rectangle rect = null;

		if (text != null && !text.equals("")) {
			try (final MemoryStack stack = MemoryStack.stackPush()) {
				final FloatBuffer bounds = stack.callocFloat(4);

				nvgTextBounds(ctx, x, y, text, bounds);
				rect = new Rectangle(Math.round(bounds.get()), Math.round(bounds.get()), Math.round(bounds.get()),
						Math.round(bounds.get()));
			}
		}

		return rect;
	}

	public void alignText(final TextAlign... textAligns) {
		int align = 0;

		for (int i = 0, c = textAligns.length; i < c; i++)
			align = align | textAligns[i].getAlign();

		setTextAlign(align);
	}

	/**
	 * Définie la position du texte
	 *
	 * @param align Le parametre peut être l'un de ceux:<br>
	 *              <table>
	 *              <tr>
	 *              <td>{@link NanoVG#NVG_ALIGN_LEFT ALIGN_LEFT}</td>
	 *              <td>{@link NanoVG#NVG_ALIGN_CENTER ALIGN_CENTER}</td>
	 *              <td>{@link NanoVG#NVG_ALIGN_RIGHT ALIGN_RIGHT}</td>
	 *              <td>{@link NanoVG#NVG_ALIGN_TOP ALIGN_TOP}</td>
	 *              <td>{@link NanoVG#NVG_ALIGN_MIDDLE ALIGN_MIDDLE}</td>
	 *              <td>{@link NanoVG#NVG_ALIGN_BOTTOM ALIGN_BOTTOM}</td>
	 *              <td>{@link NanoVG#NVG_ALIGN_BASELINE ALIGN_BASELINE}</td>
	 *              </tr>
	 *              </table>
	 */
	public void setTextAlign(final int align) {
		nvgTextAlign(ctx, align);
	}

	/**
	 ********************************
	 ************* IMAGE ************
	 ********************************
	 */
	public void drawImage(final Image img, final int x, final int y) {
		beginPath();
		img.init(ctx, x + origin.x, y + origin.y);
		nvgRect(ctx, x + origin.x, y + origin.y, img.getWidth(), img.getHeight());
		nvgFillPaint(ctx, img.getNVGPaint());
		nvgFill(ctx);
	}

	/**
	 * Cette méthode ne devrait normalement jamais a être utilisé. Sert pour pouvoir
	 * faire des manipulations graphiques plus avancé en apellant directement les
	 * fonctions de NanoVG. Elle est aussi utilisé pour clean les resources
	 * 
	 * @return Le context utilisé de NanoVG
	 */
	public long getContext() {
		return ctx;
	}
}
