package com.OffTheLine.logic;

import com.OffTheLine.logic.Path;
import com.OffTheLine.logic.Item;
import com.OffTheLine.logic.Enemy;
import com.OffTheLine.logic.Vertice;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import com.github.cliftonlabs.json_simple.Jsonable;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;

public class Level implements Jsonable {

    public String name;
    public Path paths[];
    public Item items[];
    public Enemy enemies[];
    public int time;

    public Level()
    {
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

    public JsonArray loadLevelFile() throws IOException, JsonException
    {
        //Test InputStream
        try {
            InputStream is = new FileInputStream("levels.json");

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String jsonString = new String(buffer, "UTF-8");

            JsonArray a = (JsonArray) Jsoner.deserialize(jsonString);

            return  a;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Level loadThisLevel (int this_) throws IOException, JsonException
    {
        Level l_ = new Level();

        JsonArray a = loadLevelFile();

        JsonObject JsonLevel = (JsonObject) a.get(this_);

        l_ = loadLevel(JsonLevel);

        return l_;
    }

    public Level loadLevel(JsonObject JsonLevel)
    {
        Level level = new Level();

        //Carga de name y time
        level.name = (String) JsonLevel.get("name");
        level.time = Integer.parseInt((String) JsonLevel.get("time"));

        //Carga de paths
        JsonArray paths = (JsonArray) JsonLevel.get("paths");

        if (paths != null)
        {
            int paths_size = paths.size();

            level.paths = new Path[paths_size];

            for (int i = 0; i < paths_size; i++)
            {
                //Vertices
                JsonObject aux = (JsonObject) paths.get(i);
                JsonArray vertices = (JsonArray) aux.get("vertices");
                int vertices_size = vertices.size();

                level.paths[i] = new Path(vertices_size);

                for (int j = 0; j < vertices_size; j++)
                {
                    JsonObject vertice = (JsonObject) vertices.get(j);

                    BigDecimal x = (BigDecimal) vertice.get("x");
                    BigDecimal y = (BigDecimal) vertice.get("y");

                    float x_ = x.floatValue();
                    float y_ = y.floatValue();

                    level.paths[i].vertices[j] = new Vertice(x_, y_);
                }

                //Directions (Opcional)

                JsonArray directions = (JsonArray) aux.get("directions");

                if (directions != null) {
                    int directions_size = directions.size();

                    level.paths[i].directions = new Direction[directions_size];

                    for (int j = 0; j < directions_size; j++) {
                        JsonObject direction = (JsonObject) directions.get(j);

                        BigDecimal x = (BigDecimal) direction.get("x");
                        BigDecimal y = (BigDecimal) direction.get("y");

                        float x_ = x.floatValue();
                        float y_ = y.floatValue();

                        level.paths[i].directions[j] = new Direction(x_, y_);
                    }
                }
            }
        }

        //Carga de items
        JsonArray items = (JsonArray) JsonLevel.get("items");

        if (items != null)
        {
            int items_size = items.size();

            level.items = new Item[items_size];

            for (int i = 0; i < items_size; i++)
            {
                JsonObject aux = (JsonObject) items.get(i);

                BigDecimal x = (BigDecimal) aux.get("x");
                BigDecimal y = (BigDecimal) aux.get("y");

                float x_ = x.floatValue();
                float y_ = y.floatValue();

                level.items[i] = new Item(x_, y_);

                //Parte opcional

                if (aux.get("radius") != null)
                {
                    BigDecimal r = (BigDecimal) aux.get("radius");
                    float radius = r.floatValue();
                    level.items[i].setRadius(radius);
                }

                if (aux.get("speed") != null)
                {
                    BigDecimal s = (BigDecimal) aux.get("speed");
                    float speed = s.floatValue();
                    level.items[i].setSpeed(speed);
                }

                if (aux.get("angle") != null)
                {
                    BigDecimal a = (BigDecimal) aux.get("angle");
                    float angle = a.floatValue();
                    level.items[i].setAngle(angle);
                }
            }
        }

        //Carga de enemies
        JsonArray enemies = (JsonArray) JsonLevel.get("enemies");

        if (enemies != null)
        {
            int enemies_size = enemies.size();

            level.enemies = new Enemy[enemies_size];

            for (int i = 0; i < enemies_size; i++)
            {
                JsonObject aux = (JsonObject) enemies.get(i);

                BigDecimal x = (BigDecimal) aux.get("x");
                BigDecimal y = (BigDecimal) aux.get("y");
                BigDecimal length = (BigDecimal) aux.get("length");
                BigDecimal angle = (BigDecimal) aux.get("angle");

                float x_ = x.floatValue();
                float y_ = y.floatValue();
                float length_ = length.floatValue();
                float angle_ = angle.floatValue();

                level.enemies[i] = new Enemy(x_, y_, angle_, length_);

                //Parte opcional

                if (aux.get("offset") != null)
                {
                    BigDecimal o = (BigDecimal) aux.get("offset");
                    float offset = o.floatValue();
                    level.enemies[i].setOffset(offset);
                }

                if (aux.get("time1") != null)
                {
                    BigDecimal t1 = (BigDecimal) aux.get("time1");
                    float time1 = t1.floatValue();
                    level.enemies[i].setTime1(time1);
                }

                if (aux.get("time2") != null)
                {
                    BigDecimal t2 = (BigDecimal) aux.get("time2");
                    float time2 = t2.floatValue();
                    level.enemies[i].setAngle(time2);
                }
            }
        }

     return  level;
    }
}
