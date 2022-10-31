package emitters;

import runtime.ParticleInterface;

public interface EmitterLifetime {
    enum LifetimeState {
        ALIVE, INACTIVE, DEAD
    }

    LifetimeState getState(ParticleInterface i);
}
