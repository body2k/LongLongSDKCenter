package com.longlong.common.utils;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * @author 29027
 * 该类效率低，不过严格在计算金钱类或想要精确计算时使用，其他推荐double类型
 */
public class BigDecimalUtil {
    /**
     * 科学计数
     * 1 AU 单位千米
     */
    public final static BigDecimal AU = new BigDecimal("149597870.691");

    /**
     * 向上取整
     *
     * @param param  向上取整
     * @param param1 保留几位小数
     */
    public static BigDecimal UpBigDecimal(BigDecimal param, Integer param1) {
        return param.setScale(param1, RoundingMode.UP);
    }


    /**
     * 向下取整
     *
     * @param param  向下取整
     * @param param1 保留几位小数
     */
    public static BigDecimal DownBigDecimal(BigDecimal param, Integer param1) {
        return param.setScale(param1, RoundingMode.DOWN);
    }

    /**
     * 四舍五入
     *
     * @param param  保留几位小数
     * @param param1 保留几位小数
     */
    public static BigDecimal rounding(BigDecimal param, Integer param1) {

        return param.setScale(param1, RoundingMode.HALF_UP);
    }


    /**
     * 减
     *
     * @param param  被减数
     * @param param1 减数
     */
    public static BigDecimal reduce(Integer param, Integer param1) {
        BigDecimal bigDecimal = new BigDecimal(param);
        BigDecimal bigDecimal1 = new BigDecimal(param1);
        return bigDecimal.subtract(bigDecimal1);
    }

    /**
     * 减
     *
     * @param param  被减数
     * @param param1 减数
     */
    public static BigDecimal reduce(Long param, Long param1) {
        BigDecimal bigDecimal = new BigDecimal(param);
        BigDecimal bigDecimal1 = new BigDecimal(param1);
        return bigDecimal.subtract(bigDecimal1);
    }

    /**
     * 减
     *
     * @param param  被减数
     * @param param1 减数
     */
    public static BigDecimal reduce(Double param, Double param1) {
        BigDecimal bigDecimal = new BigDecimal(param);
        BigDecimal bigDecimal1 = new BigDecimal(param1);
        return bigDecimal.subtract(bigDecimal1);
    }

    /**
     * 减
     *
     * @param param  被减数
     * @param param1 减数
     */
    public static BigDecimal reduce(BigDecimal param, BigDecimal param1) {
        return param.subtract(param1);
    }

    /**
     * 加
     *
     * @param param  条件1
     * @param param1 条件2
     */
    public static BigDecimal add(Double param, Double param1) {
        BigDecimal bigDecimal = new BigDecimal(param);
        BigDecimal bigDecimal1 = new BigDecimal(param1);
        return bigDecimal.add(bigDecimal1);
    }

    /**
     * 加
     *
     * @param param  条件1
     * @param param1 条件2
     */
    public static BigDecimal add(Long param, Long param1) {
        BigDecimal bigDecimal = new BigDecimal(param);
        BigDecimal bigDecimal1 = new BigDecimal(param1);
        return bigDecimal.add(bigDecimal1);
    }

    /**
     * 加
     *
     * @param param  条件1
     * @param param1 条件2
     */
    public static BigDecimal add(Integer param, Integer param1) {
        BigDecimal bigDecimal = new BigDecimal(param);
        BigDecimal bigDecimal1 = new BigDecimal(param1);
        return bigDecimal.add(bigDecimal1);
    }

    /**
     * 加
     *
     * @param param  条件1
     * @param param1 条件2
     */
    public static BigDecimal add(BigDecimal param, BigDecimal param1) {
        return param.add(param1);
    }

    /**
     * 乘
     *
     * @param param  条件1
     * @param param1 条件2
     */
    public static BigDecimal multiply(BigDecimal param, BigDecimal param1) {
        return param.multiply(param1);
    }

    /**
     * 乘
     *
     * @param param  条件1
     * @param param1 条件2
     */
    public static BigDecimal multiply(Integer param, Integer param1) {
        BigDecimal bigDecimal = new BigDecimal(param);
        BigDecimal bigDecimal1 = new BigDecimal(param1);
        return bigDecimal.multiply(bigDecimal1);
    }

    /**
     * 乘
     *
     * @param param  条件1
     * @param param1 条件2
     */
    public static BigDecimal multiply(Long param, Long param1) {
        BigDecimal bigDecimal = new BigDecimal(param);
        BigDecimal bigDecimal1 = new BigDecimal(param1);
        return bigDecimal.multiply(bigDecimal1);
    }

    /**
     * 乘
     *
     * @param param  条件1
     * @param param1 条件2
     */
    public static BigDecimal multiply(Double param, Double param1) {
        BigDecimal bigDecimal = new BigDecimal(param);
        BigDecimal bigDecimal1 = new BigDecimal(param1);
        return bigDecimal.multiply(bigDecimal1);
    }

    /**
     * 除 不建议使用 必须可以除近 如果无法除进则会抛出异常 java.lang.ArithmeticException
     *
     * @param param  条件1
     * @param param1 条件2
     */
    public static BigDecimal except(BigDecimal param, BigDecimal param1) {
        return param.divide(param1);
    }


    /**
     * 除 不建议使用 必须可以除近 如果无法除进则会抛出异常 java.lang.ArithmeticException
     *
     * @param param  条件1
     * @param param1 条件2
     */
    public static BigDecimal except(Double param, Double param1) {
        BigDecimal bigDecimal = new BigDecimal(param);
        BigDecimal bigDecimal1 = new BigDecimal(param1);
        return bigDecimal.divide(bigDecimal1);
    }

    /**
     * 除 不建议使用 必须可以除近 如果无法除进则会抛出异常 java.lang.ArithmeticException
     *
     * @param param  条件1
     * @param param1 条件2
     */
    public static BigDecimal except(Long param, Long param1) {
        BigDecimal bigDecimal = new BigDecimal(param);
        BigDecimal bigDecimal1 = new BigDecimal(param1);
        return bigDecimal.divide(bigDecimal1);
    }

    /**
     * 除 不建议使用 必须可以除近 如果无法除进则会抛出异常 java.lang.ArithmeticException
     *
     * @param param  条件1
     * @param param1 条件2
     */
    public static BigDecimal except(Integer param, Integer param1) {
        BigDecimal bigDecimal = new BigDecimal(param);
        BigDecimal bigDecimal1 = new BigDecimal(param1);
        return bigDecimal.divide(bigDecimal1);
    }

    /**
     * 余
     *
     * @param param  被除数
     * @param param1 除数
     * @return 结果
     */
    public static BigDecimal remain(BigDecimal param, BigDecimal param1) {
        return param.remainder(param1);
    }



    /**
     * 余
     *
     * @param param  被除数
     * @param param1 除数
     * @return 结果
     */
    public static BigDecimal remain(Integer param, Integer param1) {
        BigDecimal bigDecimal = new BigDecimal(param);
        BigDecimal bigDecimal1 = new BigDecimal(param1);

        return bigDecimal.remainder(bigDecimal1);
    }

    /**
     * 余
     *
     * @param param  被除数
     * @param param1 除数
     * @return 结果
     */
    public static BigDecimal remain(Double param, Double param1) {
        BigDecimal bigDecimal = new BigDecimal(param);
        BigDecimal bigDecimal1 = new BigDecimal(param1);

        return bigDecimal.remainder(bigDecimal1);
    }

    /**
     * 余
     *
     * @param param  被除数
     * @param param1 除数
     * @return 结果
     */
    public static BigDecimal remain(Long param, Long param1) {
        BigDecimal bigDecimal = new BigDecimal(param);
        BigDecimal bigDecimal1 = new BigDecimal(param1);
        return bigDecimal.remainder(bigDecimal1);
    }


    /**
     * 是否等于
     *
     * @param param  条件1
     * @param param1 条件2
     * @return true 满足 false 不满足
     */
    public static Boolean amount(BigDecimal param, BigDecimal param1) {
        return param.compareTo(param1) == 0;
    }

    /**
     * @param param  条件1
     * @param param1 条件2
     * @return true 满足 false 不满足
     */
    public static Boolean moreThanThe(BigDecimal param, BigDecimal param1) {
        return param.compareTo(param1) == 1;
    }

    /**
     * 是否小于
     *
     * @param param  条件1
     * @param param1 条件2
     * @return true 满足 false 不满足
     */
    public static Boolean lessThan(BigDecimal param, BigDecimal param1) {
        return param.compareTo(param1) == -1;
    }

    /**
     * 判断
     *
     * @param param  条件1
     * @param param1 条件2
     * @return 1 大于 0等于 -1小于
     */
    public static Integer judge(BigDecimal param, BigDecimal param1) {
        return param.compareTo(param1);
    }

    /**
     * @param param 集合
     * @return 返回double
     */
    public static BigDecimal addBigDecimal(List<BigDecimal> param) {
        return param.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
