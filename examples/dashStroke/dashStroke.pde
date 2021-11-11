import micycle.processingSkia.SkiaCanvas;
import org.jetbrains.skija.*;

Canvas canvas;
Font font;

public void setup() {
  size(1000, 1000, P2D);
  colorMode(HSB, 1, 1, 1);
  canvas = SkiaCanvas.getSkiaCanvas(this);
  Typeface face = FontMgr.getDefault().matchFamilyStyle("Arial", FontStyle.NORMAL);
  font = new Font(face, 800);
}

public void draw() {
  background(1);

  float dashLength = 110 + sin(frameCount/100f)*50;
  float dashDistance = 20; // distance between endpoints
  float phase = frameCount;
  PathEffect dash = PathEffect.makeDash(new float[] { dashLength, dashDistance }, phase);

  Paint dashes = new Paint().setMode(PaintMode.STROKE).setStrokeWidth(5f).setStrokeCap(PaintStrokeCap.ROUND).setPathEffect(dash);
  var gradient = Shader.makeSweepGradient(new Point(780,180), new int[]{color(0), color(1), color(0)});
  Paint fill = new Paint().setMode(PaintMode.FILL).setShader(gradient);

  var bounds = font.getBounds(font.getStringGlyphs("P"))[0];

  canvas.drawString("P", width / 2 - bounds.getWidth()/2, height / 2 +  bounds.getHeight()/2, font, fill); // character
  canvas.drawString("P", width / 2 - bounds.getWidth()/2, height / 2 +  bounds.getHeight()/2, font, dashes); // stroke
}
