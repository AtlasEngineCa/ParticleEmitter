package runtime;

import net.hollowcube.mql.foreign.Query;

public interface ParticleInterface {
    @Query
    double emitter_age();
    @Query
    double emitter_random_1();
    @Query
    double emitter_random_2();
    @Query
    double emitter_random_3();
    @Query
    double emitter_random_4();
    @Query
    double particle_age();
    @Query
    double particle_random_1();
    @Query
    double particle_random_2();
    @Query
    double particle_random_3();
    @Query
    double particle_random_4();
    @Query
    int particle_count();
}
