package emitters.lifetime;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import emitters.EmitterLifetime;
import runtime.ParticleEmitterScript;

import java.lang.reflect.InvocationTargetException;

public record EmitterLifetimeOnce(ParticleEmitterScript activeTime) implements EmitterLifetime {
    public static EmitterLifetime parse(JsonObject asJsonObject) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (asJsonObject == null) return new EmitterLifetimeOnce(ParticleEmitterScript.fromDouble(10));
        JsonElement active_time = asJsonObject.get("active_time");
        var activeTime = active_time == null ? ParticleEmitterScript.fromDouble(0) : ParticleEmitterScript.fromString(active_time.getAsString());

        return new EmitterLifetimeOnce(activeTime);
    }

    @Override
    public LifetimeState getState() {
        return null;
    }
}
