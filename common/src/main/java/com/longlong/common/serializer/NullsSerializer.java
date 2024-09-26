package com.longlong.common.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.longlong.common.utils.ObjectUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NullsSerializer extends JsonSerializer {
    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // 如果value不为空
        if (ObjectUtil.isNotBlank(value)) {
            // 如果value是Integer类型
            if (value instanceof Integer) {
                // 写入空字符串
                gen.writeString("");
            // 如果value是Double类型
            } else if (value instanceof Double) {
                // 写入空字符串
                gen.writeString("");
            // 如果value是Long类型
            } else if (value instanceof Long) {
                // 写入空字符串
                gen.writeString("");
            // 如果value是BigDecimal类型
            } else if (value instanceof BigDecimal) {
                // 写入空字符串
                gen.writeString("");
            // 如果value是BigInteger类型
            } else if (value instanceof BigInteger) {
                // 写入空字符串
                gen.writeString("");
            } else {
                // 写入空字符串
                gen.writeString("");
            }
        // 如果value为空
        } else {
            // 写入value的字符串形式
            gen.writeString(value.toString());
        }
    }
}
