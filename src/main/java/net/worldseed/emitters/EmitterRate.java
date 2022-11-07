package net.worldseed.emitters;

import net.worldseed.runtime.ParticleInterface;

public interface EmitterRate {
    boolean canEmit(ParticleInterface emitter);
}