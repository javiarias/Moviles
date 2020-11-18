package com.OffTheLine.logic;

import com.github.cliftonlabs.json_simple.Jsonable;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.io.FileWriter;

public class Staff implements Jsonable {

    private String name;
    private int age;

    public int getAge()
    {
        return age;
    }

    public String getName()
    {
        return  name;
    }

    public  void setName(String s_)
    {
        name = s_;
    }

    public  void setAge(int a_)
    {
        age = a_;
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
        json.put("name", this.getName());
        json.put("age", this.getAge());
        json.toJson(writer);

    }

    public void dameJsonPrimo() throws IOException {

        Staff staff = createStaff();

        // Java objects to JSON String
        String json = Jsoner.serialize(staff);

        // pretty print
        json = Jsoner.prettyPrint(json);

        System.out.println(json);

        // Java objects to JSON file
        try (FileWriter fileWriter = new FileWriter("user3.json")) {
            Jsoner.serialize(staff, fileWriter);
        }
        catch (Exception E)
        {
            System.err.println(E);
        }
    }

    private static Staff createStaff() {

        Staff staff = new Staff();

        staff.setName("mkyong");
        staff.setAge(38);

        return staff;
    }
}
