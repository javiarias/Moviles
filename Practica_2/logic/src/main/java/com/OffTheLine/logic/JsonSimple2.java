package com.OffTheLine.logic;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.io.FileReader;
import java.io.IOException;

public class JsonSimple2 {

    public static void main(String[] args) throws IOException, JsonException {

        // The file `user3.json` is generated from above example 3.1
        try (FileReader fileReader = new FileReader(("C:\\projects\\user3.json"))) {

            JsonObject deserialize = (JsonObject) Jsoner.deserialize(fileReader);

            // need dozer to copy object to staff, json_simple no api for this?
            Mapper mapper = new DozerBeanMapper();

            // JSON to object
            Staff staff = mapper.map(deserialize, Staff.class);

            System.out.println(staff);
        }

    }

}
