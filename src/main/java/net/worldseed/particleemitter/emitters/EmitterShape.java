package net.worldseed.particleemitter.emitters;

import net.minestom.server.coordinate.Vec;
import net.worldseed.particleemitter.runtime.ParticleInterface;

public interface EmitterShape {
    Vec emitPosition(ParticleInterface particleEmitter);
    Vec emitDirection(Vec origin, ParticleInterface particleEmitter);
    boolean canRotate();
}