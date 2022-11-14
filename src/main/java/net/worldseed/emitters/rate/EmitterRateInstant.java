package net.worldseed.emitters.rate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.worldseed.emitters.EmitterRate;
import net.worldseed.runtime.ParticleEmitterScript;
import net.worldseed.runtime.ParticleInterface;

import java.lang.reflect.InvocationTargetException;

public record EmitterRateInstant(ParticleEmitterScript particleNumber) implements EmitterRate {
    public static EmitterRate parse(JsonObject asJsonObject) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (asJsonObject == null) return new EmitterRateInstant(ParticleEmitterScript.fromDouble(10));
        JsonElement particle_number = asJsonObject.get("num_particles");
        var particleNumber = particle_number == null ? ParticleEmitterScript.fromDouble(10) : ParticleEmitterScript.fromString(particle_number.getAsString());
        return new EmitterRateInstant(particleNumber);
    }

    @Override
    public boolean canEmit(ParticleInterface emitter) {
        return particleNumber.evaluate(emitter) > emitter.particle_count();
    }
}