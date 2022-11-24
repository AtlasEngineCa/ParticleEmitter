package net.worldseed.particleemitter.emitters;

import net.worldseed.particleemitter.runtime.ParticleInterface;

public interface EmitterRate {
    boolean canEmit(ParticleInterface emitter);
}