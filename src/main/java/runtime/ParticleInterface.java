package runtime;

import net.hollowcube.mql.foreign.Query;

public abstract class ParticleInterface {
    @Query
    abstract public double emitter_age();
    @Query
    abstract public double emitter_random_1();
    @Query
    abstract public double emitter_random_2();
    @Query
    abstract public double emitter_random_3();
    @Query
    abstract public double emitter_random_4();
    @Query
    abstract public double particle_age();
    @Query
    abstract public double particle_random_1();
    @Query
    abstract public double particle_random_2();
    @Query
    abstract public double particle_random_3();
    @Query
    abstract public double particle_random_4();
    @Query
    abstract public int particle_count();
}
