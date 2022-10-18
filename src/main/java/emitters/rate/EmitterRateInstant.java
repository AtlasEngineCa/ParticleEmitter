package emitters.rate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import emitters.EmitterRate;
import runtime.Emitter;

public record EmitterRateInstant(String particleNumber) implements EmitterRate {
    public static EmitterRate parse(JsonObject asJsonObject) {
        if (asJsonObject == null) return new EmitterRateInstant("10");
        JsonElement particle_number = asJsonObject.get("num_particles");
        String particleNumber = particle_number == null ? "10" : particle_number.getAsString();

        return new EmitterRateInstant(particleNumber);
    }

    @Override
    public boolean canEmit(Emitter emitter) {
        return false;
    }
}