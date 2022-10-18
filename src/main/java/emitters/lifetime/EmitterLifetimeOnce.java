package emitters.lifetime;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import emitters.EmitterLifetime;

public record EmitterLifetimeOnce(String activeTime) implements EmitterLifetime {
    public static EmitterLifetime parse(JsonObject asJsonObject) {
        if (asJsonObject == null) return new EmitterLifetimeOnce("10");
        JsonElement active_time = asJsonObject.get("active_time");
        String activeTime = active_time == null ? "10" : active_time.getAsString();

        return new EmitterLifetimeOnce(activeTime);
    }

    @Override
    public LifetimeState getState() {
        return null;
    }
}
