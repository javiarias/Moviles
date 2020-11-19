package com.OffTheLine.logic;

import com.OffTheLine.logic.Path;
import com.OffTheLine.logic.Item;
import com.OffTheLine.logic.Enemy;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import com.github.cliftonlabs.json_simple.Jsonable;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Level implements Jsonable {

    public String name;
    //public Path paths[];
    //public Item items[];
    //public Enemy enemies[];

    public Level()
    {
    }

    public Level(String name) //, Path paths[], Item items[], Enemy enemies[])
    {
        this.name = name;
    }

    @Override
    public String toJson() {
        final StringWriter writable = new StringWriter();
        try {
            this.toJson(writable);
        } catch (final IOException e) {
        }
        return writable.toString();
    }

    @Override
    public void toJson(Writer writer) throws IOException {

        final JsonObject json = new JsonObject();
        //json.put("name", "Antonio con camiseta del hacktoberfest saliendo de tu pantalla");
        json.toJson(writer);
    }

    public void loadLevel() throws IOException, JsonException
    {
        try (FileReader fileReader = new FileReader("user3.json")) {

            JsonObject deserialize = (JsonObject) Jsoner.deserialize(fileReader);

            // need dozer to copy object to staff, json_simple no api for this?
            Mapper mapper = new DozerBeanMapper();

            // JSON to object
            Level level2 = mapper.map(deserialize, Level.class);

            System.out.println(level2.name);
        }
    }
}
