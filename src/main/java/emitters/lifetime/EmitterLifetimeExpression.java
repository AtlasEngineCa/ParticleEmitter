package emitters.lifetime;

import emitters.EmitterLifetime;

public record EmitterLifetimeExpression(String activeExpression, String expirationExpression) implements EmitterLifetime {
    @Override
    public LifetimeState getState() {
        return null;
    }
}