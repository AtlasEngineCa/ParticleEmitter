package net.worldseed.particle;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.worldseed.misc.Colour;
import net.worldseed.runtime.ParticleEmitterScript;
import net.worldseed.runtime.Particle;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.TreeMap;

public record ParticleAppearanceTinting(Map<Double, Colour> color, ParticleEmitterScript interpolant) {
    public static ParticleAppearanceTinting parse(JsonObject particleAppearanceTinting) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        JsonElement colors = particleAppearanceTinting.get("color");

        ParticleEmitterScript interpolant = null;
        Map<Double, Colour> map = new TreeMap<>();

        if (colors.isJsonArray()) {
            String r = colors.getAsJsonArray().get(0).getAsString();
            String g = colors.getAsJsonArray().get(1).getAsString();
            String b = colors.getAsJsonArray().get(2).getAsString();

            map.put(0.0, new Colour(r, g, b));
        } else if (colors.isJsonPrimitive()) {
            String color = colors.getAsString();
            var c = Color.decode(color);
            map.put(0.0, new Colour(c.getRed(), c.getGreen(), c.getBlue()));
        } else if (colors.isJsonObject()) {
            interpolant = ParticleEmitterScript.fromString(colors.getAsJsonObject().get("interpolant").getAsString());

            if (colors.getAsJsonObject().has("gradient")) {
                JsonElement gradient = colors.getAsJsonObject().get("gradient");

                if (gradient.isJsonObject()) {
                    for (Map.Entry<String, JsonElement> entry : colors.getAsJsonObject().get("gradient").getAsJsonObject().entrySet()) {
                        var c = Colour.fromJson(entry.getValue());
                        map.put(Double.parseDouble(entry.getKey()), c);
                    }
                } else {
                    JsonArray gradientArray = gradient.getAsJsonArray();
                    for (int i = 0; i < gradientArray.size(); i++) {
                        Colour c = Colour.fromJson(gradientArray.get(i));
                        map.put((double) i / (gradientArray.size() - 1), c);
                    }
                }
            } else {
                for (Map.Entry<String, JsonElement> entry : colors.getAsJsonObject().entrySet()) {
                    String key = entry.getKey();
                    JsonElement value = entry.getValue();

                    var c = Colour.fromJson(value);
                    map.put(Double.parseDouble(key), c);
                }
            }
        }

        return new ParticleAppearanceTinting(map, interpolant);
    }

    public ColourEvaluated evaluate(Particle particle) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (interpolant == null && color.size() == 0) return ColourEvaluated.white();
        if (interpolant == null) return color.values().iterator().next().toColourEvaluated(particle);

        double t = interpolant.evaluate(particle);
        if (color.size() == 1) return color.values().iterator().next().toColourEvaluated(particle);

        Colour c1 = null;
        Colour c2 = null;
        double t1 = 0;
        double t2 = 0;

        for (Map.Entry<Double, Colour> entry : color.entrySet()) {
            if (entry.getKey() <= t) {
                c1 = entry.getValue();
                t1 = entry.getKey();
            } else {
                c2 = entry.getValue();
                t2 = entry.getKey();
                break;
            }
        }

        if (c1 == null) return c2.toColourEvaluated(particle);
        if (c2 == null) return c1.toColourEvaluated(particle);

        double t3 = (t - t1) / (t2 - t1);
        return interpolateColour(c1, c2, t3, particle);
    }

    public record ColourEvaluated(double r, double g, double b) {
        public static ColourEvaluated white() {
            return new ColourEvaluated(1, 1, 1);
        }
    }

    public static ColourEvaluated interpolateColour(Colour c1, Colour c2, double t, Particle particle) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return new ColourEvaluated(
                (c1.r().evaluate(particle) * (1 - t) + c2.r().evaluate(particle) * t),
                (c1.g().evaluate(particle) * (1 - t) + c2.g().evaluate(particle) * t),
                (c1.b().evaluate(particle) * (1 - t) + c2.b().evaluate(particle) * t)
        );
    }
}
