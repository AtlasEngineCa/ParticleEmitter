package emitters.rate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import emitters.EmitterRate;
import runtime.ParticleEmitterScript;
import runtime.ParticleInterface;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public final class EmitterRateSteady implements EmitterRate {
    private final ParticleEmitterScript spawnRate;
    private final ParticleEmitterScript maxParticles;
    private int waitTicks = 0;

    public EmitterRateSteady(ParticleEmitterScript spawnRate, ParticleEmitterScript maxParticles) {
        this.spawnRate = spawnRate;
        this.maxParticles = maxParticles;
    }

    public static EmitterRate parse(JsonObject asJsonObject) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (asJsonObject == null)
            return new EmitterRateSteady(ParticleEmitterScript.fromDouble(1), ParticleEmitterScript.fromDouble(50));
        JsonElement spawn_rate = asJsonObject.get("spawn_rate");
        JsonElement max_particles = asJsonObject.get("max_particles");
        var spawnRate = spawn_rate == null ? ParticleEmitterScript.fromDouble(1) : ParticleEmitterScript.fromString(spawn_rate.getAsString());
        var maxParticles = max_particles == null ? ParticleEmitterScript.fromDouble(50) : ParticleEmitterScript.fromString(max_particles.getAsString());

        return new EmitterRateSteady(spawnRate, maxParticles);
    }

    @Override
    public boolean canEmit(ParticleInterface emitter) {
        if (waitTicks <= 0) {
            waitTicks = (int) spawnRate.evaluate(emitter);
            return emitter.particle_count() < maxParticles.evaluate(emitter);
        } else {
            waitTicks--;
            return false;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (EmitterRateSteady) obj;
        return Objects.equals(this.spawnRate, that.spawnRate) &&
                Objects.equals(this.maxParticles, that.maxParticles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(spawnRate, maxParticles);
    }

    @Override
    public String toString() {
        return "EmitterRateSteady[" +
                "spawnRate=" + spawnRate + ", " +
                "maxParticles=" + maxParticles + ']';
    }

}
