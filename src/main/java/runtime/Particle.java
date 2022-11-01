package runtime;

import generator.ParticleGenerator;
import misc.Colour;
import net.hollowcube.mql.foreign.Query;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import particle.ParticleAppearanceTinting;
import particle.ParticleLifetime;

import java.lang.reflect.InvocationTargetException;

public class Particle extends ParticleInterface {
    private final ParticleEmitter emitter;
    private final ParticleAppearanceTinting particleColour;
    private final ParticleLifetime particleLifetime;
    private final ParticlePacket packet;

    double particle_age;

    final double particle_random_1;
    final double particle_random_2;
    final double particle_random_3;
    final double particle_random_4;

    @Query
    public double particle_age() {
        return particle_age;
    }

    @Query
    public double particle_random_1() {
        return particle_random_1;
    }

    @Query
    public double particle_random_2() {
        return particle_random_2;
    }

    @Query
    public double particle_random_3() {
        return particle_random_3;
    }

    @Query
    public double particle_random_4() {
        return particle_random_4;
    }

    @Override
    public int particle_count() {
        return emitter.particle_count();
    }

    @Query
    public double emitter_age() {
        return emitter.emitter_age();
    }

    @Query
    public double emitter_random_1() {
        return emitter.emitter_random_1();
    }

    @Query
    public double emitter_random_2() {
        return emitter.emitter_random_2();
    }

    @Query
    public double emitter_random_3() {
        return emitter.emitter_random_3();
    }

    @Query
    public double emitter_random_4() {
        return emitter.emitter_random_4();
    }

    public ParticlePacket getPacket() {
        return packet;
    }

    public Particle(Vec start, Vec direction, ParticleEmitter emitter, ParticleAppearanceTinting particleColour, ParticleLifetime particleLifetime) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        this.particle_age = 0;

        this.particle_random_1 = Math.random();
        this.particle_random_2 = Math.random();
        this.particle_random_3 = Math.random();
        this.particle_random_4 = Math.random();

        this.emitter = emitter;

        this.particleColour = particleColour;
        this.particleLifetime = particleLifetime;

        this.packet = draw(start, direction);
    }

    public ParticlePacket draw(Vec start, Vec direction) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Colour colour = particleColour.evaluate(this);

        return ParticleGenerator.buildParticle(net.minestom.server.particle.Particle.DUST,
                start.x(),
                start.y(),
                start.z(),
                1,
                direction.x(),
                direction.y(),
                direction.z(),
                colour.r().evaluate(this),
                colour.g().evaluate(this),
                colour.b().evaluate(this));
    }

    public void tick() {
        particle_age += 1 / 1000.0;
    }

    public boolean isAlive() {
        return particleLifetime.isAlive(this);
    }
}
