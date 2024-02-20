package engine.graph.hlib.component;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_CENTER_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_DONT_CARE;
import static org.lwjgl.glfw.GLFW.GLFW_MAXIMIZED;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_REFRESH_RATE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_SAMPLES;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateCursor;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyCursor;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetFramebufferSize;
import static org.lwjgl.glfw.GLFW.glfwGetKeyName;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwMaximizeWindow;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCharCallback;
import static org.lwjgl.glfw.GLFW.glfwSetCursor;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_STENCIL_TEST;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;
import java.util.Objects;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import engine.graph.hlib.event.KeyEvent;
import engine.graph.hlib.event.KeyManager;
import engine.graph.hlib.event.MouseEvent;
import engine.graph.hlib.event.MouseInput;
import engine.graph.hlib.graphics.Graphics;
import engine.graph.hlib.graphics.paint.Color;
import engine.graph.hlib.graphics.paint.ImageCache;
import engine.graph.hlib.utils.HLib;
import engine.graph.renderer.Options;
import loader.texture.Texture;

public class HWindow extends HContainer {

    public static int initWidth, initHeight, frameWidth, frameHeight;

    private final KeyManager keys;

    private MouseEvent mouse;

    private Graphics g;

    private WindowOptions opts;

    // Id of the window and of the cursor
    private long windowHandle;

    private long cursor;

    /**
     * The title of the Window
     */
    private String title;

    private Matrix4f orthoMatrix;

    public HWindow(final String title, final int width, final int height, final WindowOptions opts) {
        this.title = title;

        frameWidth = initWidth = width;
        frameHeight = initHeight = height;

        orthoMatrix = new Matrix4f();

        this.opts = opts;

        keys = new KeyManager();
    }

    public void init() throws Exception {
        orthoMatrix = new Matrix4f();
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwWindowHint(GLFW_REFRESH_RATE, GLFW_DONT_CARE);

        glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable

        // Version of OpenGL we want. In this project we use OpenGL 3.3
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

        if (System.getProperty("os.name").contains("Mac"))
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        glfwWindowHint(GLFW_CENTER_CURSOR, GL_TRUE);

        boolean maximized = false;

        if (frameWidth == 0 || frameHeight == 0) {
            // Set up a fixed width and height so window initialization does not fail
            frameWidth = 100;
            frameHeight = 100;
            glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
            maximized = true;
        }

        // Create the window
        windowHandle = glfwCreateWindow(frameWidth, frameHeight, title, NULL, NULL);
        if (windowHandle == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowHandle);

        // Setup resize callback
        glfwSetFramebufferSizeCallback(windowHandle, (window, width, height) -> {
            setSize(width, height);
        });

        // Setup a key callback. It will be called every time a key is pressed, repeated
        // or released.
        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            KeyEvent.action = action;
            KeyEvent.mods = mods;
            KeyEvent.key = key;
            if (glfwGetKeyName(key, scancode) == null)
                HLib.fireKeyEvent(new KeyEvent((char) -1));
        });

        glfwSetCursorPosCallback(windowHandle, (window, xPos, yPos) -> {
            mouse = new MouseEvent(xPos, yPos);
            fireEvent(mouse);
        });

        glfwSetMouseButtonCallback(windowHandle, (window, button, actions, mods) -> {
            mouse = new MouseEvent(button, actions, mods);
            fireEvent(mouse);
        });

        glfwSetScrollCallback(windowHandle, (window, xoffset, yoffset) -> {
            fireEvent(new MouseEvent(xoffset, yoffset, true));
        });

        glfwSetCharCallback(windowHandle, (window, codepoint) -> {
            HLib.fireKeyEvent(new KeyEvent((char) codepoint));
        });

        final GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if (maximized) {
            glfwMaximizeWindow(windowHandle);

            try (final MemoryStack stack = MemoryStack.stackPush()) {
                final IntBuffer w = stack.ints(1), h = stack.ints(1);
                glfwGetFramebufferSize(windowHandle, w, h);

                setSize(w.get(), h.get());
            }
        } else
            glfwSetWindowPos(windowHandle, (vidmode.width() - frameWidth) / 2, (vidmode.height() - frameHeight) / 2);

        initWidth = vidmode.width();
        initHeight = vidmode.height();

        if (System.getProperty("os.name").contains("Linux")) {
            frameWidth = initWidth;
            frameHeight = initHeight;
        }

        // glfwSetInputMode(windowHandle, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        // Enable v-sync
        if (Options.vSync)
            glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(windowHandle);

        GL.createCapabilities();

        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        glEnable(GL_STENCIL_TEST);

        // Antialiasing
        if (opts.antialiasing)
            glfwWindowHint(GLFW_SAMPLES, 4);

        g = new Graphics(this);

        SCALE = Math.min(frameHeight / 20f, frameWidth / 30f);

        // On scale pour voir qqc
        orthoMatrix.identity().ortho2D(-frameWidth / 2f, frameWidth / 2f, frameHeight / 2f, -frameHeight / 2f).scale(SCALE).translate(-15f, -10f, 0);
    }

    public static float SCALE;

    public void input(final MouseInput mouseInput) {
        mouseInput.input(mouse);
        keys.update(windowHandle);
    }

    public void update() {
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();
        recalcSize(g);

        // Remet a 0 les touches
        keys.reset();
    }

    public void setVisible(final boolean visible) {
        super.setVisible(visible);
        initSize(g);
    }

    public void render() {
        g.startRendering(this);
        g.moveOriginTo(0, 0);
        paint(g);
        g.moveOriginTo(0, 0);
        g.endRendering(this);
    }

    public Graphics getGraphics() {
        return g;
    }

    public boolean isKeyReleassed(final int keyCode) {
        return keys.isKeyReleassed(keyCode);
    }

    public boolean isKeyPressed(final int keyCode) {
        return keys.isKeyPressed(keyCode);
    }

    public boolean windowShouldClose() {
        return glfwWindowShouldClose(windowHandle);
    }

    public long getWindowHandle() {
        return windowHandle;
    }

    public Matrix4f getOrthoMatrix() {
        return orthoMatrix;
    }

    public WindowOptions getOptions() {
        return opts;
    }

    public String getTitle() {
        return title;
    }

    public void setWindowTitle(final String title) {
        this.title = title;
        glfwSetWindowTitle(windowHandle, title);
    }

    public int getWidth() {
        return frameWidth;
    }

    public int getHeight() {
        return frameHeight;
    }

    public void setPos(final int x, final int y) {
        glfwSetWindowPos(windowHandle, x, y);
    }

    public void setSize(final int w, final int h) {
        super.setSize(w, h);
        frameWidth = w;
        frameHeight = h;
    }


    public void setCursor(final Texture image) {
        cursor = glfwCreateCursor(GLFWImage.malloc().set(image.getWidth(), image.getHeight(), image.getImage()), 0, 0);

        if (cursor == NULL)
            throw new RuntimeException("Error creating cursor");

        // Set the cursor on a window
        glfwSetCursor(windowHandle, cursor);
    }

    public void destroy() {
        // Clean texture, font and image of each component
        final long ctx = g.getContext();
        cleanUp(ctx);

        Color.cleanUpStatic(ctx);
        ImageCache.cleanUp(ctx);

        g.cleanUp();

        if (cursor != 0)
            glfwDestroyCursor(cursor);

        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);
        glfwTerminate();

        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    public static class WindowOptions {

        public boolean antialiasing;
    }
}