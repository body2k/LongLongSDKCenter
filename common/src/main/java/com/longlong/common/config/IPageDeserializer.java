package com.longlong.common.config;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;


import java.io.IOException;

public class IPageDeserializer extends StdDeserializer<IPage> {
    protected IPageDeserializer(Class<?> vc) {
        super(vc);
    }


    @Override
    public IPage deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        String s  = node.toString();
        ObjectMapper om = new ObjectMapper();
        Page page = om.readValue(s,Page.class);
        return page;
    }
}