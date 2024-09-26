package com.longlong.common.mybatis;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Join {
    //    String LEFT = "left join";
//    String RIGHT = "";
//    String INNER = "inner join";
    LEFT_JOIN("left join"),
    RIGHT_JOIN("right join"),
    INNER_JOIN("inner join");


    String value;


}
