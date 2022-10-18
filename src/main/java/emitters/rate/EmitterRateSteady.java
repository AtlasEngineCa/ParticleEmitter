package emitters.rate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import emitters.EmitterRate;
import runtime.Emitter;

public record EmitterRateSteady(String spawnRate, String maxParticles) implements EmitterRate {
    public static EmitterRate parse(JsonObject asJsonObject) {
        if (asJsonObject == null) return new EmitterRateSteady("0", "0");
        JsonElement spawn_rate = asJsonObject.get("spawn_rate");
        JsonElement max_particles = asJsonObject.get("max_particles");
        String spawnRate = spawn_rate == null ? "0" : spawn_rate.getAsString();
        String maxParticles = max_particles == null ? "0" : max_particles.getAsString();

        return new EmitterRateSteady(spawnRate, maxParticles);
    }

    @Override
    public boolean canEmit(Emitter emitter) {
        return false;
    }
}
