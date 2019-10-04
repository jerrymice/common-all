package com.github.jerrymice.common.entity.stream;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author tumingjian
 * 创建时间: 2019-09-29 17:12
 * 功能说明:用于检测对象
 */
public class Select<T, U> extends AbstractStream<T, U> {
    private static AbstractStream<?, ?> EMPTY = new Select<>();

    private Select(T value, U result) {
        super(value, result);
    }

    private Select() {
    }

    /**
     * 创建一个Select对象,用于检查对象
     * @param value  要检查的对象
     * @param u  要返回的值的类型
     * @param <T> value值的类型
     * @param <U> 返回值的类型
     * @return
     */
    public static <T, U> Stream<T, U> ofNullable(T value, Class<? extends U> u) {
        return value==null?empty():new Select<>(value, null);
    }

    /**
     * 创建一个空的select
     * @param <T>
     * @param <U>
     * @return
     */
    public static <T, U> Stream<T, U> empty() {
        AbstractStream<T, U> t = (AbstractStream<T, U>) EMPTY;
        return t;
    }

    /**
     * 创建一个空的Select,但是有result值
     * @param result
     * @param <T>
     * @param <U>
     * @return
     */
    private static <T, U> Stream<T, U> empty(U result) {
        AbstractStream<T, U> t = new Select<>(null, result);
        return t;
    }

    @Override
    public <E> Stream<E, U> map(Function<? super T, ? extends E> mapper) {
        Objects.requireNonNull(mapper);
        T t = orElse(null);
        return t!=null?new Select<>(mapper.apply(t), result()):new Select<>(null,result());
    }

}
