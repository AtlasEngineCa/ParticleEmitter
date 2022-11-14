package net.worldseed.misc;

import com.google.gson.JsonElement;
import net.worldseed.runtime.ParticleEmitterScript;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public record Colour(ParticleEmitterScript r, ParticleEmitterScript g, ParticleEmitterScript b) {
    public Colour(String r, String g, String b) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        this(ParticleEmitterScript.fromString(r), ParticleEmitterScript.fromString(g), ParticleEmitterScript.fromString(b));
    }

    public Colour(double v, double v1, double v2) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        this(ParticleEmitterScript.fromDouble(v), ParticleEmitterScript.fromDouble(v1), ParticleEmitterScript.fromDouble(v2));
    }

    public static Colour white() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return new Colour(ParticleEmitterScript.fromDouble(255), ParticleEmitterScript.fromDouble(255), ParticleEmitterScript.fromDouble(255));
    }

    public static Colour fromJson(JsonElement color) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (color.isJsonPrimitive()) {
            var s = color.getAsString();

            if (s.length() == 9 && s.startsWith("#")) s = s.substring(1);
            if (s.length() == 8) s = s.substring(2);
            if (s.length() == 6) s = "#" + s;

            var c = Color.decode(s);
            return new Colour(c.getRed() / 255.0, c.getGreen() / 255.0, c.getBlue() / 255.0);
        } else {
            String r = color.getAsJsonArray().get(0).getAsString();
            String g = color.getAsJsonArray().get(1).getAsString();
            String b = color.getAsJsonArray().get(2).getAsString();
            return new Colour(r, g, b);
        }
    }
}
