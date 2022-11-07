package net.worldseed.misc;

import net.worldseed.runtime.ParticleEmitterScript;

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
}
