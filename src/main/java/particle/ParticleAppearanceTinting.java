package particle;

import misc.Colour;
import java.util.Map;

public record ParticleAppearanceTinting(Map<Double, Colour> color, String interpolant) {
}
