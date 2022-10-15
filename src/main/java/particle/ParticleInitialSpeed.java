package particle;

import com.google.gson.JsonElement;

public record ParticleInitialSpeed(String speedX, String speedY, String speedZ) {
    public static ParticleInitialSpeed parse(JsonElement particleInitialSpeed) {
        if (particleInitialSpeed == null) return new ParticleInitialSpeed("0", "0", "0");

        if (particleInitialSpeed.isJsonArray()) {
            var asArray = particleInitialSpeed.getAsJsonArray();
            return new ParticleInitialSpeed(asArray.get(0).getAsString(), asArray.get(1).getAsString(), asArray.get(2).getAsString());
        }

        return new ParticleInitialSpeed(particleInitialSpeed.getAsString(), particleInitialSpeed.getAsString(), particleInitialSpeed.getAsString());
    }
}
