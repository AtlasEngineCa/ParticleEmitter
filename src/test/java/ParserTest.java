import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import net.hollowcube.mql.foreign.Query;
import net.hollowcube.mql.jit.MqlCompiler;
import net.hollowcube.mql.jit.MqlEnv;
import org.junit.jupiter.api.Test;
import runtime.Parser;
import runtime.ParticleEmitter;

import java.io.*;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserTest {
    private final ClassLoader classLoader = getClass().getClassLoader();
    static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    @Test
    public void parseMagic() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        File file = new File(classLoader.getResource("magic.particle.json").getFile());
        FileInputStream fis = new FileInputStream(file);
        JsonReader reader = new JsonReader(new InputStreamReader(fis, "UTF-8"));
        JsonObject map = GSON.fromJson(reader, JsonObject.class);
        ParticleEmitter emitter = Parser.parse(map);
    }

    public class TestClass {
        @Query
        public double emitter_age() {
            return 0;
        }
    }

    @FunctionalInterface
    public interface TestScript {
        double evaluate(@MqlEnv({"variable", "v"}) TestClass emitter);
    }

    @Test
    public void compile() throws InstantiationException, IllegalAccessException {
        TestClass test = new TestClass();

        MqlCompiler<TestScript> compiler = new MqlCompiler<>(TestScript.class);
        Class<TestScript> scriptClass = compiler.compile("math.sin(variable.emitter_age*200)*6");
        assertEquals(0, scriptClass.newInstance().evaluate(test));

        compiler = new MqlCompiler<>(TestScript.class);
        scriptClass = compiler.compile("math.sin(variable.emitter_age*200)*6");
        assertEquals(0, scriptClass.newInstance().evaluate(test));
    }
}