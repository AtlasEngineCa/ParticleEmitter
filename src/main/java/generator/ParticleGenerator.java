package generator;

import net.minestom.server.coordinate.Vec;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;

public class ParticleGenerator {
    public static ParticlePacket buildParticle(Particle particleType, double x, double y, double z, double size, double velocityX, double velocityY, double velocityZ, double r, double g, double b) {
        if (particleType == Particle.DUST) return buildDust(x, y, z, size, r, g, b);

        if (particleType == Particle.FLAME || particleType == Particle.SMOKE || particleType == Particle.FIREWORK || particleType == Particle.SOUL_FIRE_FLAME) return buildDirectional(x, y, z, size, velocityX, velocityY, velocityZ);
        return null;
    }

    private static ParticlePacket buildDirectional(double x, double y, double z, double size, double velocityX, double velocityY, double velocityZ) {
        return null;
    }

    private static ParticlePacket buildDust(double x, double y, double z, double size, double r, double g, double b) {
        Vec vec = new Vec(x, y, z).normalize();

        return null;
    }
}
