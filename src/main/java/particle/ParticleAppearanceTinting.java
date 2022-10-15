package particle;

import com.google.gson.JsonElement;
import misc.Colour;
import java.util.Map;

public record ParticleAppearanceTinting(Map<Double, Colour> color, String interpolant) {
    public static ParticleAppearanceTinting parse(JsonElement particleAppearanceTinting) {
    }
}
