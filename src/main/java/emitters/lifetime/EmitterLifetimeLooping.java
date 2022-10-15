package emitters.lifetime;

import emitters.EmitterLifetime;

public record EmitterLifetimeLooping(String activeTime, String sleepTime) implements EmitterLifetime {
    @Override
    public LifetimeState getState() {
        return null;
    }
}
