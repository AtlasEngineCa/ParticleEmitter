package emitters.lifetime;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import emitters.EmitterLifetime;

public record EmitterLifetimeLooping(String activeTime, String sleepTime) implements EmitterLifetime {
    public static EmitterLifetime parse(JsonObject asJsonObject) {
        if (asJsonObject == null) return new EmitterLifetimeLooping("0", "0");
        JsonElement active_time = asJsonObject.get("active_time");
        JsonElement sleep_time = asJsonObject.get("sleep_time");
        String activeTime = active_time == null ? "0" : active_time.getAsString();
        String sleepTime = sleep_time == null ? "0" : sleep_time.getAsString();

        return new EmitterLifetimeLooping(activeTime, sleepTime);
    }

    @Override
    public LifetimeState getState() {
        return null;
    }
}
