package net.worldseed.particleemitter.emitters;

import net.worldseed.particleemitter.runtime.ParticleInterface;

public interface EmitterLifetime {
    enum LifetimeState {
        ALIVE, INACTIVE, DEAD
    }

    LifetimeState getState(ParticleInterface i);
}
