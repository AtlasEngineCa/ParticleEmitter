package emitters;

import net.minestom.server.coordinate.Vec;
import runtime.ParticleEmitter;

public interface EmitterShape {
    Vec emitPosition(ParticleEmitter particleEmitter);
    Vec emitDirection(ParticleEmitter particleEmitter);
}
