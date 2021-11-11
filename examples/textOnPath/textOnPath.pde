import micycle.processingSkia.SkiaCanvas;
import org.jetbrains.skija.*;

Canvas canvas;
Font font;

public void setup() {
  size(1000, 1000, P2D);
  colorMode(HSB, 1, 1, 1);
  canvas = SkiaCanvas.getSkiaCanvas(this);
  Typeface face = FontMgr.getDefault().matchFamilyStyle("Tw Cen MT", FontStyle.NORMAL);
  font = new Font(face, 96);
}

public void draw() {
  background(1);

  Paint textFill = new Paint().setColor(color(0.6, 1, 1));
  Paint stroke = new Paint().setColor(color(0.3, 1, 1)).setMode(PaintMode.STROKE).setStrokeWidth(10);
  float radius = 300;

  canvas.drawCircle(width/2, height/2, radius-5, stroke);
  var text = textOnPath("processing-skia", new Path().addCircle(width/2, height/2, radius), font, frameCount/300f);
  canvas.drawTextBlob(text, 0, 0, textFill);
}

TextBlob textOnPath(String text, Path path, Font font, float phase) {
  short[] glyphs = font.getStringGlyphs(text);
  float[] widths = font.getWidths(glyphs);
  RSXform[] xforms = new RSXform[glyphs.length];
  PathMeasure measure = new PathMeasure(path);
  final float length = measure.getLength();
  final float relativeOffset = phase;
  float distance = relativeOffset * length;

  for (int i = 0; i < xforms.length; ++i) {
    float w = widths[i];
    distance += w / 2;
    while (distance > length) {
      distance -= length;
    }
    final Point p = measure.getPosition(distance);
    final Point t = measure.getTangent(distance);
    xforms[i] = new RSXform(t.getX(), t.getY(), p.getX() - w / 2f * t.getX(), p.getY() - w / 2f * t.getY());
    distance += w / 2;
  }
  measure.close();

  return TextBlob.makeFromRSXform(glyphs, xforms, font);
}
