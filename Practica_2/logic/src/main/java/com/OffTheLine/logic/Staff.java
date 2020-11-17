//package com.OffTheLine.logic;
package com.OffTheLine.logic;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Staff implements Jsonable {

    private String name;
    private int age;
    private String[] position;
    private List<String> skills;
    private Map<String, BigDecimal> salary;

    //..getters setters

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
        /*json.put("name", this.getName());
        json.put("age", this.getAge());
        json.put("position", this.getPosition());
        json.put("skills", this.getSkills());
        json.put("salary", this.getSalary());
        json.toJson(writer);*/

    }
}
