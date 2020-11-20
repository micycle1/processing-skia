package micycle.processingSkia;

import java.nio.IntBuffer;

import org.jetbrains.skija.BackendRenderTarget;
import org.jetbrains.skija.Canvas;
import org.jetbrains.skija.ColorSpace;
import org.jetbrains.skija.DirectContext;
import org.jetbrains.skija.FramebufferFormat;
import org.jetbrains.skija.Surface;
import org.jetbrains.skija.SurfaceColorFormat;
import org.jetbrains.skija.SurfaceOrigin;

import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.event.WindowListener;
import com.jogamp.newt.event.WindowUpdateEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL;

import processing.core.PApplet;
import processing.opengl.PSurfaceJOGL;

public class SkiaCanvas {

	private static SkiaCanvas wrapper;

	/**
	 * Call this to get a Skia Canvas whose render target is the sketch provided.
	 * Calling Skija drawing methods on this canvas will draw the output to your
	 * Processing sketch.
	 * 
	 * @param p Your Processing sketch
	 * @return
	 */
	public static Canvas getSkiaCanvas(PApplet p) {
		if (!(p.sketchRenderer() == "processing.opengl.PGraphics3D" || p.sketchRenderer() == "processing.opengl.PGraphics2D")) {
			System.err.println("SkiaRenderer requires processing using P2D or P3D (JOGL) renderer.");
			return null;
		}

		if (wrapper == null) { // singleton pattern
			wrapper = new SkiaCanvas(p);
		}

		return wrapper.canvas;
	}

	private Canvas canvas;

	private DirectContext context;
	private BackendRenderTarget renderTarget;
	private Surface surface;

	private GL gl;

	private PApplet p;

	private SkiaCanvas(PApplet p) {

		PSurfaceJOGL pSurface = (PSurfaceJOGL) p.getSurface();
		GLWindow window = (GLWindow) pSurface.getNative();
		gl = window.getGL();

		final IntBuffer intBuffer = IntBuffer.allocate(1);
		gl.glGetIntegerv(GL.GL_DRAW_FRAMEBUFFER_BINDING, intBuffer);
		final int fbId = intBuffer.get(0);

		renderTarget = BackendRenderTarget.makeGL(p.width, p.height, 0, 8, fbId, FramebufferFormat.GR_GL_RGBA8);

		context = DirectContext.makeGL();

		surface = Surface.makeFromBackendRenderTarget(context, renderTarget, SurfaceOrigin.BOTTOM_LEFT, SurfaceColorFormat.RGBA_8888,
				ColorSpace.getSRGB());

		canvas = surface.getCanvas();

		window.addWindowListener(new WindowListener() {

			@Override
			public void windowResized(WindowEvent e) {
				renderTarget = BackendRenderTarget.makeGL(window.getWidth(), window.getHeight(), 0, 8, fbId, FramebufferFormat.GR_GL_RGBA8);
				surface = Surface.makeFromBackendRenderTarget(context, renderTarget, SurfaceOrigin.BOTTOM_LEFT,
						SurfaceColorFormat.RGBA_8888, ColorSpace.getSRGB());
				canvas = surface.getCanvas();
			}

			// @formatter:off
			public void windowRepaint(WindowUpdateEvent e) {}
			public void windowMoved(WindowEvent e) {}
			public void windowLostFocus(WindowEvent e) {}
			public void windowGainedFocus(WindowEvent e) {}
			public void windowDestroyed(WindowEvent e) {}
			public void windowDestroyNotify(WindowEvent e) {}
			// @formatter:on
		});

		this.p = p;

		p.registerMethod("pre", this);
		p.registerMethod("post", this);
	}

	/**
	 * Calling Skija's {@link Canvas#clear(int) canvas.clear()} breaks it for some
	 * unknown reason; without calling clear() on the PApplet, skia doesn't update
	 * (again, for some unknown reason...).
	 */
	public void pre() {
		p.clear();
	}

	/**
	 * Flushes Skia graphics to the screen.
	 */
	public void post() {
		context.flush();
	}

}
