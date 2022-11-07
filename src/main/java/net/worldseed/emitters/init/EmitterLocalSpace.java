package net.worldseed.emitters.init;

import com.google.gson.JsonElement;

public record EmitterLocalSpace(boolean position, boolean velocity, boolean rotation) {
    public static EmitterLocalSpace parse(JsonElement emitterLocalSpace) {
        if (emitterLocalSpace == null) return new EmitterLocalSpace(false, false, false);

        JsonElement positionEl = emitterLocalSpace.getAsJsonObject().get("position");
        JsonElement velocityEl = emitterLocalSpace.getAsJsonObject().get("velocity");
        JsonElement rotationEl = emitterLocalSpace.getAsJsonObject().get("rotation");

        boolean position = positionEl != null && positionEl.getAsBoolean();
        boolean velocity = velocityEl != null && velocityEl.getAsBoolean();
        boolean rotation = rotationEl != null && rotationEl.getAsBoolean();

        return new EmitterLocalSpace(position, velocity, rotation);
    }
}
