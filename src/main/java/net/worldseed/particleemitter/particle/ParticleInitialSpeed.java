package net.worldseed.particleemitter.particle;

import com.google.gson.JsonElement;
import net.worldseed.particleemitter.runtime.ParticleEmitterScript;

import java.lang.reflect.InvocationTargetException;

public record ParticleInitialSpeed(ParticleEmitterScript speedX, ParticleEmitterScript speedY, ParticleEmitterScript speedZ) {
    public static ParticleInitialSpeed parse(JsonElement particleInitialSpeed) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (particleInitialSpeed == null) return new ParticleInitialSpeed(ParticleEmitterScript.fromDouble(0), ParticleEmitterScript.fromDouble(0), ParticleEmitterScript.fromDouble(0));

        if (particleInitialSpeed.isJsonArray()) {
            var asArray = particleInitialSpeed.getAsJsonArray();

            ParticleEmitterScript speedX = ParticleEmitterScript.fromString(asArray.get(0).getAsString());
            ParticleEmitterScript speedY = ParticleEmitterScript.fromString(asArray.get(1).getAsString());
            ParticleEmitterScript speedZ = ParticleEmitterScript.fromString(asArray.get(2).getAsString());

            return new ParticleInitialSpeed(speedX, speedY, speedZ);
        }

        ParticleEmitterScript speed = ParticleEmitterScript.fromString(particleInitialSpeed.getAsString());
        return new ParticleInitialSpeed(speed, speed, speed);
    }
}
