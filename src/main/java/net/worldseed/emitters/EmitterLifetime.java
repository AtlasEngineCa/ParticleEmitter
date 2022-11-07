package net.worldseed.emitters;

import net.worldseed.runtime.ParticleInterface;

public interface EmitterLifetime {
    enum LifetimeState {
        ALIVE, INACTIVE, DEAD
    }

    LifetimeState getState(ParticleInterface i);
}
