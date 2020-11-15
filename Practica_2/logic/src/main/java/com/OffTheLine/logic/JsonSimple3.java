package com.OffTheLine.logic;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.Jsoner;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JsonSimple3 {

    public static void main(String[] args) throws IOException, JsonException {

        // The file `user4.json` is generated from above example 3.2
        try (FileReader fileReader = new FileReader(("C:\\projects\\user4.json"))) {

            JsonArray objects = Jsoner.deserializeMany(fileReader);

            Mapper mapper = new DozerBeanMapper();

            JsonArray o = (JsonArray) objects.get(0);
            //List<Staff> collect = o.stream().map(x -> mapper.map(x, Staff.class)).collect(Collectors.toList()); //Dice que da error por tema de versiones
            //collect.forEach(x->System.out.println(x)); //Sale como error

        }

    }

}