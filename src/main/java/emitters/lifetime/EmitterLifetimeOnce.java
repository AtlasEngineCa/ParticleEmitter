package emitters.lifetime;

import emitters.EmitterLifetime;

public record EmitterLifetimeOnce(String activeTime) implements EmitterLifetime {
    @Override
    public LifetimeState getState() {
        return null;
    }
}
