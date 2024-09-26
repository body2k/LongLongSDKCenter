
package com.longlong.common.utils;

import com.longlong.common.support.FastStringWriter;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * 异常处理工具类
 */
public class Exceptions {

    /**
     * 将CheckedException转换为UncheckedException.
     *
     * @param e Throwable
     * @return {RuntimeException}
     */
    public static RuntimeException unchecked(Throwable e) {
        if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
                || e instanceof NoSuchMethodException) {
            return new IllegalArgumentException(e);
        } else if (e instanceof InvocationTargetException) {
            return new RuntimeException(((InvocationTargetException) e).getTargetException());
        } else if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        } else {
            return new RuntimeException(e);
        }
    }

    /**
     * 代理异常解包
     *
     * @param wrapped 包装过得异常
     * @return 解包后的异常
     */
    public static Throwable unwrap(Throwable wrapped) {
        Throwable unwrapped = wrapped;
        while (true) {
            if (unwrapped instanceof InvocationTargetException) {
                unwrapped = ((InvocationTargetException) unwrapped).getTargetException();
            } else if (unwrapped instanceof UndeclaredThrowableException) {
                unwrapped = ((UndeclaredThrowableException) unwrapped).getUndeclaredThrowable();
            } else {
                return unwrapped;
            }
        }
    }

    /**
     * 将ErrorStack转化为String.
     *
     * @param ex Throwable
     * @return {String}
     */
    public static String getStackTraceAsString(Throwable ex) {
        FastStringWriter stringWriter = new FastStringWriter();
        ex.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    /**
     * 返回异常行数
     *
     * @param error 异常
     */
    public static int getExceptionLineNumber(Exception error) {
        if (error == null) {
            return -1;
        }
        StackTraceElement[] stackTrace = error.getStackTrace();
        if (stackTrace == null || stackTrace.length == 0) {
            return -1;
        }
        return stackTrace[0].getLineNumber();
    }

    /**
     * 返回异常行数
     *
     * @param error 异常
     */
    public static int getExceptionLineNumber(Throwable error) {
        if (error == null) {
            return -1;
        }
        StackTraceElement[] stackTrace = error.getStackTrace();
        if (stackTrace == null || stackTrace.length == 0) {
            return -1;
        }
        return stackTrace[0].getLineNumber();
    }

    /**
     * 返回异常堆栈
     */
    public static String getExceptionStackTrace(Exception error) {
        error.printStackTrace();
        // 创建一个StringBuffer对象
        StringBuffer stringBuffer = new StringBuffer();
        // 获取异常的堆栈信息
        StackTraceElement[] stackTrace = error.getStackTrace();
        // 遍历堆栈信息
        for (StackTraceElement element : stackTrace) {
            // 将堆栈信息拼接到StringBuffer中
            stringBuffer.append(element.toString());
        }
        // 返回拼接完成的StringBuffer对象
        return stringBuffer.toString();

    }
    public static String getExceptionStackTrace(Throwable error) {
        // 创建一个StringBuffer对象
        StringBuffer stringBuffer = new StringBuffer();
        // 获取异常的堆栈信息
        StackTraceElement[] stackTrace = error.getStackTrace();
        // 遍历堆栈信息
        for (StackTraceElement element : stackTrace) {
            // 将堆栈信息拼接到StringBuffer中
            stringBuffer.append(element.toString());
        }
        // 返回拼接完成的StringBuffer对象
        return stringBuffer.toString();

    }
}

