package emitters;

public interface EmitterLifetime {
    enum LifetimeState {
        ALIVE, INACTIVE, DEAD
    }

    LifetimeState getState();
}
