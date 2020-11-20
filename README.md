# processing-skia

**Skia** is an open source 2D graphics library which provides common APIs that work across a variety of hardware and software platforms.

**Skija** provides high-quality Java bindings for Skia.

**processing-skia** does the backend work of setting Skija's render target to Processing, so you can easily use any Skija [bindings](https://github.com/JetBrains/skija/blob/master/docs/Getting%20Started.md) to draw into a Processing sketch.

## Requirements
* Java 11+
* A Processing sketch using either the `P2D` or `P3D` renderer.

## Download
Download the processing-skia *jar* from [releases](https://github.com/micycle1/processing-skia/releases/).

## Example
### Code

```
import micycle.processingSkia.SkiaCanvas;
import org.jetbrains.skija.*;

Canvas skiaCanvas;

@Override
public void settings() {
    size(800, 800, P2D);
}

@Override
public void setup() {
    skiaCanvas = SkiaCanvas.getSkiaCanvas(this);
}

@Override
public void draw() {
    background(255);
	
    Paint fill = new Paint().setShader(Shader.makeLinearGradient(400, 300, 400, 500, new int[] {0xFFFFA500, 0xFF4CA387}));
	
    skiaCanvas.drawCircle(400, 400, 200, fill);
}
```
### Result
<p align="center">
<img src="resources/result_example.png" alt="Example" width="400"squares_examplesquares_example/></a><br></p>

## Further Work
Further work would wrap the Skija library itself, creating something like a `PSurfaceSkia` interface leading to a dedicated Skia-based renderer in Processing.

## Another Example

This is the [squares example](https://github.com/JetBrains/skija/blob/master/examples/lwjgl/src/main/java/org/jetbrains/skija/examples/lwjgl/SquaresScene.java) as rendered in a Processing sketch. Here, the gradient fill and square-circle morphing provide features that are not easily attained in vanilla Processing, but are easily attained with the Skia renderer.

<p align="center">
<img src="resources/squares_example.gif" alt="Example"/></a><br></p>
