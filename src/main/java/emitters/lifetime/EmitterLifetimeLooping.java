package emitters.lifetime;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import emitters.EmitterLifetime;
import runtime.ParticleEmitterScript;
import runtime.ParticleInterface;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public final class EmitterLifetimeLooping implements EmitterLifetime {
    private final ParticleEmitterScript activeTime;
    private final ParticleEmitterScript sleepTime;
    private int waitTicks = 0;
    boolean active = true;

    public EmitterLifetimeLooping(ParticleEmitterScript activeTime, ParticleEmitterScript sleepTime) {
        this.activeTime = activeTime;
        this.sleepTime = sleepTime;
    }

    public static EmitterLifetime parse(JsonObject asJsonObject) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (asJsonObject == null)
            return new EmitterLifetimeLooping(ParticleEmitterScript.fromDouble(10), ParticleEmitterScript.fromDouble(0));
        JsonElement active_time = asJsonObject.get("active_time");
        JsonElement sleep_time = asJsonObject.get("sleep_time");
        var activeTime = active_time == null ? ParticleEmitterScript.fromDouble(10) : ParticleEmitterScript.fromString(active_time.getAsString());
        var sleepTime = sleep_time == null ? ParticleEmitterScript.fromDouble(0) : ParticleEmitterScript.fromString(sleep_time.getAsString());

        return new EmitterLifetimeLooping(activeTime, sleepTime);
    }

    @Override
    public LifetimeState getState(ParticleInterface i) {
        if (waitTicks <= 0) {
            if (active) {
                waitTicks = (int) (i.updatesPerSecond() * sleepTime.evaluate(i));
                active = false;
                return LifetimeState.INACTIVE;
            } else {
                waitTicks = (int) (i.updatesPerSecond() * activeTime.evaluate(i));
                active = true;
                i.reset();
                return LifetimeState.ALIVE;
            }
        } else {
            waitTicks--;
            return active ? LifetimeState.ALIVE : LifetimeState.INACTIVE;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (EmitterLifetimeLooping) obj;
        return Objects.equals(this.activeTime, that.activeTime) &&
                Objects.equals(this.sleepTime, that.sleepTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activeTime, sleepTime);
    }

    @Override
    public String toString() {
        return "EmitterLifetimeLooping[" +
                "activeTime=" + activeTime + ", " +
                "sleepTime=" + sleepTime + ']';
    }

}
