package com.github.jerrymice.common.id.generator;



import com.github.jerrymice.common.id.utils.IDGeneratorUtils;

import java.util.List;

/**
 * @author tumingjian
 * 说明:ID生成器接口
 */
public interface IDGenerator {
    /**
     * 获取一个ID
     * @return 返回一个long类型的ID值
     */
    default Long id(){
        return IDGeneratorUtils.id();
    }

    /**
     * 一次性批量获取多个ID
     * @param count  要获取的ID数量
     * @return  返回一个size为count的ID列表.
     */
    default List<Long> batchId(int count){
        return IDGeneratorUtils.batchId(count);
    }

}
