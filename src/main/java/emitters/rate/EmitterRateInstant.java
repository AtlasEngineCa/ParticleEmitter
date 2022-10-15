package emitters.rate;

import emitters.EmitterRate;
import runtime.Emitter;

public record EmitterRateInstant(String particleNumber) implements EmitterRate {
    @Override
    public boolean canEmit(Emitter emitter) {
        return false;
    }
}