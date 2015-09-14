package io.github.tomacla.client.app.json;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import javax.ws.rs.ext.Provider;

@Provider
public class ObjectMapperProvider extends JacksonJaxbJsonProvider {

    public ObjectMapperProvider() {
        super();

        SimpleModule module = new SimpleModule();

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Include.NON_NULL);
        mapper.registerModule(module);

        this.setMapper(mapper);

    }

}