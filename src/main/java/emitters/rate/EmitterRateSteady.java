package emitters.rate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import emitters.EmitterRate;
import runtime.Emitter;
import runtime.ParticleEmitterScript;

import java.lang.reflect.InvocationTargetException;

public record EmitterRateSteady(ParticleEmitterScript spawnRate, ParticleEmitterScript maxParticles) implements EmitterRate {
    public static EmitterRate parse(JsonObject asJsonObject) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (asJsonObject == null) return new EmitterRateSteady(ParticleEmitterScript.fromDouble(1), ParticleEmitterScript.fromDouble(50));
        JsonElement spawn_rate = asJsonObject.get("spawn_rate");
        JsonElement max_particles = asJsonObject.get("max_particles");
        var spawnRate = spawn_rate == null ? ParticleEmitterScript.fromDouble(1) : ParticleEmitterScript.fromString(spawn_rate.getAsString());
        var maxParticles = max_particles == null ? ParticleEmitterScript.fromDouble(50) : ParticleEmitterScript.fromString(max_particles.getAsString());

        return new EmitterRateSteady(spawnRate, maxParticles);
    }

    @Override
    public boolean canEmit(Emitter emitter) {
        return false;
    }
}
