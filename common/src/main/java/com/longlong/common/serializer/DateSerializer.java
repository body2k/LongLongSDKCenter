package com.longlong.common.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.longlong.common.utils.DateUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateSerializer extends JsonSerializer<Date> {

    // 定义一个变量param，用于存储日期格式
    private String param = DateUtil.PATTERN_DATETIME;

    // 无参构造方法
    public DateSerializer() {

    }

    // 有参构造方法
    public DateSerializer(String param) {
        this.param = param;
    }

    // 重写serialize方法，用于将Date类型的数据转换为字符串
    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // 创建一个SimpleDateFormat对象，用于格式化日期
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(param);

        // 将Date类型的数据转换为字符串
        gen.writeString(simpleDateFormat.format(value));
    }
}

