import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.Test;
import runtime.Parser;

import java.io.*;

public class ParserTest {
    private final ClassLoader classLoader = getClass().getClassLoader();
    static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    @Test
    public void parseMagic() throws IOException {
        File file = new File(classLoader.getResource("magic.particle.json").getFile());
        FileInputStream fis = new FileInputStream(file);
        JsonReader reader = new JsonReader(new InputStreamReader(fis, "UTF-8"));
        JsonObject map = GSON.fromJson(reader, JsonObject.class);
        System.out.println(Parser.parse(map));
    }
}
