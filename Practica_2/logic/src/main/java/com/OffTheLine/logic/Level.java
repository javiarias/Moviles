package com.OffTheLine.logic;

import com.OffTheLine.common.Engine;
import com.OffTheLine.common.Graphics;
import com.OffTheLine.common.Input;
import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;

public class Level {

    /*Variables*/
    String _name;
    ArrayList<Path> _paths;
    ArrayList<Item> _items;
    ArrayList<Enemy> _enemies;
    int _time;
    String _path;
    Engine _e;

    /*Funciones*/

    /*Getters*/

    //Obtener nombre del nivel
    public String getName() { return _name; }

    //Obtener paths del nivel
    public ArrayList<Path> getPaths() { return _paths; }

    //Obtener items (monedas) del nivel
    public ArrayList<Item> getItems() { return _items; }

    //Obtener enemigos del nivel
    public ArrayList<Enemy> getEnemies() { return _enemies; }

    //Time al final no se usa, pero por si acaso
    public int getTime() { return _time; }

    //Constructoras
    public Level(String path, Engine e)
    {
        _path = path;
        _e = e;

        //Nuevas listas
        _paths = new ArrayList<Path>();
        _items = new ArrayList<Item>();
        _enemies = new ArrayList<Enemy>();
    }

    //Update
    public void update(double delta, ArrayList<Input.TouchEvent> inputList)
    {
        //Actualizacion de las monedas
        for (Item i : _items)
        {
            i.update(delta, inputList);
        }
        //Actualizacion de los enemigos
        for (Enemy e : _enemies)
        {
            e.update(delta, inputList);
        }
    }

    //Render (save antes y restore despues del render)
    public void render(Graphics g)
    {
        //Render de paths
        for (Path p : _paths)
        {
            g.save();
            p.render(g);
            g.restore();
        }
        //Render de items (monedas)
        for (Item i : _items)
        {
            g.save();
            i.render(g);
            g.restore();
        }
        //Render de enemigos
        for (Enemy e : _enemies)
        {
            g.save();
            e.render(g);
            g.restore();
        }
    }

    //Para cargar el nivel, devolviendo el Array de todos los objetos
    private JsonArray loadLevelFile(String path) throws Exception
    {
        try
        {
            InputStream is = _e.getFile(path);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String jsonString = new String(buffer, "UTF-8");
            JsonArray a = (JsonArray) Jsoner.deserialize(jsonString);
            return  a;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    //Carga de un nivel, dado el numero
    public void loadLevel(int lvl) throws Exception
    {
        JsonArray a = loadLevelFile(_path);
        JsonObject JsonLevel = (JsonObject) a.get(lvl);
        loadLevel(JsonLevel);
    }

    //Dado el objeto JSON, construcción del nivel
    private void loadLevel(JsonObject JsonLevel)
    {
        //Carga de name y time
        _name = (String) JsonLevel.get("name");
        _time = Integer.parseInt((String) JsonLevel.get("time"));

        //Carga de paths
        JsonArray pathsJson = (JsonArray) JsonLevel.get("paths");

        if (pathsJson != null) //Comprobación de que existe
        {
            int paths_size = pathsJson.size();

            for (int i = 0; i < paths_size; i++)
            {
                //Vertices
                JsonObject aux = (JsonObject) pathsJson.get(i);
                JsonArray vertices = (JsonArray) aux.get("vertices");
                int vertices_size = vertices.size();

                Path tempPath = new Path();

                for (int j = 0; j < vertices_size; j++)
                {
                    JsonObject vertice = (JsonObject) vertices.get(j);

                    BigDecimal x = (BigDecimal) vertice.get("x");
                    BigDecimal y = (BigDecimal) vertice.get("y");

                    float x_ = x.floatValue();
                    float y_ = y.floatValue();

                    tempPath.addVertice(x_, y_);
                }

                //Directions (Opcional)
                JsonArray directions = (JsonArray) aux.get("directions");

                if (directions != null) //Comprobación de que existe
                {
                    tempPath.useDirections();

                    int directions_size = directions.size();

                    for (int j = 0; j < directions_size; j++)
                    {
                        JsonObject direction = (JsonObject) directions.get(j);

                        BigDecimal x = (BigDecimal) direction.get("x");
                        BigDecimal y = (BigDecimal) direction.get("y");

                        float x_ = x.floatValue();
                        float y_ = y.floatValue();

                        tempPath.addDirection(x_, y_);
                    }
                }
                _paths.add(tempPath);
            }
        }

        //Carga de items
        JsonArray items = (JsonArray) JsonLevel.get("items");

        if (items != null) //Comprobación de que existe
        {
            int items_size = items.size();

            for (int i = 0; i < items_size; i++)
            {
                JsonObject aux = (JsonObject) items.get(i);

                BigDecimal x = (BigDecimal) aux.get("x");
                BigDecimal y = (BigDecimal) aux.get("y");

                float x_ = x.floatValue();
                float y_ = y.floatValue();

                Item tempItem = new Item(x_, y_);

                //Parte opcional

                if (aux.get("radius") != null) //Comprobación de que existe
                {
                    BigDecimal r = (BigDecimal) aux.get("radius");
                    float radius = r.floatValue();
                    tempItem.setRadius(radius);
                }

                if (aux.get("speed") != null) //Comprobación de que existe
                {
                    BigDecimal s = (BigDecimal) aux.get("speed");
                    float speed = s.floatValue();
                    tempItem.setSpeed(speed);
                }

                if (aux.get("angle") != null) //Comprobación de que existe
                {
                    BigDecimal a = (BigDecimal) aux.get("angle");
                    float angle = a.floatValue();
                    tempItem.setAngle(angle);
                }
                _items.add(tempItem);
            }
        }

        //Carga de enemies
        JsonArray enemies = (JsonArray) JsonLevel.get("enemies");

        if (enemies != null) //Comprobación de que existe
        {
            int enemies_size = enemies.size();

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

                Enemy tempEnemy = new Enemy(x_, y_, angle_, length_);

                //Parte opcional

                if (aux.get("offset") != null) //Comprobación de que existe
                {
                    JsonObject o = (JsonObject) aux.get("offset");

                    BigDecimal o_x = (BigDecimal) o.get("x");
                    BigDecimal o_y = (BigDecimal) o.get("y");

                    float offset_x = o_x.floatValue();
                    float offset_y = o_y.floatValue();

                    tempEnemy.setOffset(offset_x, offset_y);
                }

                if (aux.get("time1") != null) //Comprobación de que existe
                {
                    BigDecimal t1 = (BigDecimal) aux.get("time1");
                    float time1 = t1.floatValue();
                    tempEnemy.setTime1(time1);
                }

                if (aux.get("time2") != null) //Comprobación de que existe
                {
                    BigDecimal t2 = (BigDecimal) aux.get("time2");
                    float time2 = t2.floatValue();
                    tempEnemy.setTime2(time2);
                }

                if (aux.get("speed") != null) //Comprobación de que existe
                {
                    BigDecimal speed = (BigDecimal) aux.get("speed");
                    float speed_ = speed.floatValue();
                    tempEnemy.setSpeed(speed_);
                }
                _enemies.add(tempEnemy);
            }
        }
    }
}
