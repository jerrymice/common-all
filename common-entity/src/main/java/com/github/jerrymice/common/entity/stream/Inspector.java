package com.github.jerrymice.common.entity.stream;

import com.github.jerrymice.common.entity.entity.Status;
import com.github.jerrymice.common.entity.ex.ResultException;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author tumingjian
 * 创建时间: 2019-09-29 16:28
 * 功能说明: 用于检测对象,如果不满足某种条件,可以抛出特定的ResultException异常.
 */
public class Inspector<T> extends AbstractStream<T, Status> {
    private static AbstractStream<?, Status> EMPTY = new Inspector<>();

    private Inspector(T value, Status result) {
        super(value, result);
    }

    public Inspector() {
    }

    public static <T> Stream<T, Status> ofNullable(T value) {
        return value==null?empty():new Inspector<>(value, null);
    }

    public static <T> Stream<T, Status> empty() {
        AbstractStream<T, Status> t = (AbstractStream<T, Status>) EMPTY;
        return t;
    }

    @Override
    public <E> Stream<E, Status> map(Function<? super T, ? extends E> mapper) {
        Objects.requireNonNull(mapper);
        T t = orElse(null);
        return t!=null?new Inspector<>(mapper.apply(t), result()):new Inspector<>(null,result());
    }

    @Override
    public Inspector<T> throwEx() {
        return (Inspector<T>) super.throwEx(r -> r != null, (v, r) -> new ResultException(r));
    }

}
