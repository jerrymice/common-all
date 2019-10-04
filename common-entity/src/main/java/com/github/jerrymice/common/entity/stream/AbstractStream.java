package com.github.jerrymice.common.entity.stream;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.*;

/**
 * @author tumingjian
 * 创建时间: 2019-09-26 13:22
 * 功能说明: Stream的抽象实现.
 */
public abstract class AbstractStream<T, U> implements Stream<T, U> {

    private T value;
    private U result;

    protected AbstractStream() {
    }

    protected AbstractStream(T value, U result) {
        this.value = value;
        this.result = result;
    }

    @Override
    public U result() {
        return this.result;
    }

    @Override
    public T get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    @Override
    public T orElse(T t) {
        return value==null?t:value;
    }

    @Override
    public Stream<T, U> throwEx(Predicate<? super U> predicate, BiFunction<T, U, ? extends RuntimeException> f) {
        Objects.requireNonNull(f);
        if (predicate.test(result)) {
            RuntimeException apply = f.apply(value, result);
            if (apply == null) {
                return this;
            } else {
                throw apply;
            }
        } else {
            return this;
        }
    }

    @Override
    public Stream<T, U> peek(BiConsumer<T, U> c) {
        Objects.requireNonNull(c);
        c.accept(this.value, this.result);
        return this;
    }

    @Override
    public Stream<T, U> condition(Predicate<? super T> predicate, U result) {
        Objects.requireNonNull(predicate);
        if (!isResult()) {
            if (!predicate.test(value)) {
                this.result = result;
            }
        }
        return this;
    }

    @Override
    public <E> Stream<T, U> condition(Function<? super T, ? extends E> mapper, Predicate<? super E> predicate, U result) {
        Objects.requireNonNull(predicate);
        if (!isResult()) {
            if (!predicate.test(mapper.apply(value))) {
                this.result = result;
            }
        }
        return this;
    }

    @Override
    public boolean isNull() {
        return value == null;
    }

    private <E> boolean isNull(Function<? super T, ? extends E> mapper) {
        Objects.requireNonNull(mapper);
        E apply = mapper.apply(this.value);
        return apply == null;
    }

    @Override
    public boolean isEmpty() {
        return this.isEmpty(i -> value);
    }

    private <E> boolean isEmpty(Function<? super T, ? extends E> mapper) {
        Objects.requireNonNull(mapper);
        E apply = mapper.apply(this.value);
        return value == null || (apply instanceof Collection && ((Collection) apply).isEmpty());
    }

    @Override
    public Stream<T, U> notEmpty(U result) {
        if (isEmpty()) {
            this.result = result;
        }
        return this;
    }

    @Override
    public Stream<T, U> notNull(U result) {
        if (isNull() && !isResult()) {
            this.result = result;
        }
        return this;
    }

    @Override
    public <E> Stream<T, U> notEmpty(Function<? super T, ? extends E> mapper, U result) {
        Objects.requireNonNull(mapper);
        if (!isResult() && isEmpty(mapper)) {
            this.result = result;
        }
        return this;
    }

    @Override
    public <E> Stream<T, U> notNull(Function<? super T, ? extends E> mapper, U result) {
        if (!isResult() && isNull(mapper)) {
            this.result = result;
        }
        return this;
    }

    @Override
    public Stream<T, U> throwEx() {
        throw new NotImplementedException();
    }
}
