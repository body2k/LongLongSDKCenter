package com.longlong.common.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public class BigDecimalSerializer extends JsonSerializer<BigDecimal> {
    RoundingMode roundingMode = RoundingMode.DOWN;

    Integer newScale = 2;

    // 定义一个BigDecimalSerializer的构造方法，用于初始化RoundingMode和newScale
    public BigDecimalSerializer() {

    }

    // 定义一个BigDecimalSerializer的构造方法，用于初始化RoundingMode
    public BigDecimalSerializer(RoundingMode roundingMode) {
        this.roundingMode = roundingMode;
    }

    // 定义一个BigDecimalSerializer的构造方法，用于初始化newScale
    public BigDecimalSerializer(Integer newScale) {
        this.newScale = newScale;

    }

    // 定义一个BigDecimalSerializer的构造方法，用于初始化RoundingMode和newScale
    public BigDecimalSerializer(Integer newScale, RoundingMode roundingMode) {
        this.roundingMode = roundingMode;
        this.newScale=newScale;
    }

    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // 将BigDecimal的值设置为newScale，并使用RoundingMode.DOWN方式进行四舍五入
        value.setScale(newScale, RoundingMode.DOWN);
        gen.writeString(value.toString());
    }
}
