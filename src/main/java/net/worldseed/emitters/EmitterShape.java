package net.worldseed.emitters;

import net.minestom.server.coordinate.Vec;
import net.worldseed.runtime.ParticleEmitter;

public interface EmitterShape {
    Vec emitPosition(ParticleEmitter particleEmitter);
    Vec emitDirection(ParticleEmitter particleEmitter);
}
