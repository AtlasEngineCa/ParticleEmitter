package emitters;

import runtime.ParticleInterface;

public interface EmitterRate {
    boolean canEmit(ParticleInterface emitter);
}