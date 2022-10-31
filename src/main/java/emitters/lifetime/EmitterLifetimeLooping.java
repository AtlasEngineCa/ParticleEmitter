package emitters.lifetime;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import emitters.EmitterLifetime;
import runtime.ParticleEmitterScript;
import runtime.ParticleInterface;

import java.lang.reflect.InvocationTargetException;

public record EmitterLifetimeLooping(ParticleEmitterScript activeTime, ParticleEmitterScript sleepTime) implements EmitterLifetime {
    public static EmitterLifetime parse(JsonObject asJsonObject) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (asJsonObject == null) return new EmitterLifetimeLooping(ParticleEmitterScript.fromDouble(10), ParticleEmitterScript.fromDouble(0));
        JsonElement active_time = asJsonObject.get("active_time");
        JsonElement sleep_time = asJsonObject.get("sleep_time");
        var activeTime = active_time == null ? ParticleEmitterScript.fromDouble(10) : ParticleEmitterScript.fromString(active_time.getAsString());
        var sleepTime = sleep_time == null ? ParticleEmitterScript.fromDouble(0) : ParticleEmitterScript.fromString(sleep_time.getAsString());

        return new EmitterLifetimeLooping(activeTime, sleepTime);
    }

    @Override
    public LifetimeState getState(ParticleInterface i) {
        return null;
    }
}
