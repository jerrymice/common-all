package com.github.jerrymice.common.entity.stream;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author tumingjian
 * 创建时间: 2019-09-29 15:10
 * 功能说明:一个检测对象的流,类似于jdk的stream
 */
public interface Stream<T,U> {

    /**
     * 获取检查结果
     * @return
     */
    U result() ;

    /**
     * 判断是否有检查结果
     * @return
     */
    default boolean isResult(){
        return result()!=null;
    }

    /**
     * 获取检查对象
     * @return
     */
     T get();

    /**
     * 检查对象如果为空将返回t;
     * @param t
     * @return
     */
      T orElse(T t);

    /**
     * 同Stream的map函数
     * @param mapper
     * @param <E>
     * @return
     */
      <E> Stream<E, U> map(Function<? super T, ? extends E> mapper);

    /**
     * 如果满足条件,将抛出一个异常
     * @param predicate
     * @param f
     * @return
     */
     Stream<T, U> throwEx(Predicate<? super U> predicate, BiFunction<T, U, ? extends RuntimeException> f);
    /**
     * 直接抛出异常.只有Inspector实现了该接口,Select,AbstractStream将抛出固定的 @see NotImplementedException异常
     * @return
     */
     Stream<T, U> throwEx();
    /**
     * 同Stream的peek,可以接收两个参数:
     * t:要检查的对象
     * u:检查结果
     * @param c
     * @return
     */
     Stream<T, U> peek(BiConsumer<T, U> c);

    /**
     * 输入一个条件,如果条件不足满足,那么将设置检查结果为result
     * @param predicate
     * @param result
     * @return
     */
     Stream<T, U> condition(Predicate<? super T> predicate, U result);

    /**
     * 输入一个值,并且输入一个条件,如果条件不满足,那么将设置检查结果为result
     *
     * @param mapper
     * @param predicate
     * @param result
     * @param <E>
     * @return
     */
     <E> Stream<T, U> condition(Function<? super T, ? extends E> mapper, Predicate<? super E> predicate, U result);

    /**
     * 判断是否为空,如果是集合对象请使用isEmpty()
     * @return
     */
    default boolean isNull(){
        return get()==null;
    }

    /**
     * 判断是否为空,如果集合类型的size为0,那么也返回true.
     * @return
     */
    default boolean isEmpty(){
        if(isNull()){
            return true;
        }
        return get() != null && (get() instanceof Collection && ((Collection) get()).isEmpty());
    }

    /**
     * 检查一个集合对象是否为空,如果为空,将设置检查结果为result
     * @param result
     * @return
     */
    Stream<T, U> notEmpty(U result);
    /**
     * 检查一个对象是否为空,如果为空,将设置检查结果为result
     * @param result
     * @return
     */
    Stream<T, U> notNull(U result);

    /**
     * 检查一个集合对象是否为空,如果为空,将设置检查结果为result
     * @param mapper
     * @param result
     * @param <E>
     * @return
     */
    <E> Stream<T, U> notEmpty(Function<? super T, ? extends E> mapper, U result);
    /**
     * 检查一个对象是否为空,如果为空,将设置检查结果为result
     * @param mapper
     * @param result
     * @param <E>
     * @return
     */
    <E> Stream<T, U> notNull(Function<? super T, ? extends E> mapper, U result);
}
