package emitters.rate;

import emitters.EmitterRate;
import runtime.Emitter;

public record EmitterRateSteady(String spawnRate, String maxParticles) implements EmitterRate {
    @Override
    public boolean canEmit(Emitter emitter) {
        return false;
    }
}
