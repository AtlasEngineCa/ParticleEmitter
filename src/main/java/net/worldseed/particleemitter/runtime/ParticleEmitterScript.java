package net.worldseed.particleemitter.runtime;

import net.hollowcube.mql.jit.MqlCompiler;
import net.hollowcube.mql.jit.MqlEnv;

import java.lang.reflect.InvocationTargetException;

public interface ParticleEmitterScript {
    double evaluate(@MqlEnv({"variable", "v"}) ParticleInterface particle);

    static ParticleEmitterScript fromDouble(double value) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return fromString(Double.toString(value));
    }

    static ParticleEmitterScript fromString(String s) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (s == null) return fromDouble(0);
        MqlCompiler<ParticleEmitterScript> compiler = new MqlCompiler<>(ParticleEmitterScript.class);
        Class<ParticleEmitterScript> scriptClass = compiler.compile(s.replace("Math", "math"));
        return scriptClass.getDeclaredConstructor().newInstance();
    }
}