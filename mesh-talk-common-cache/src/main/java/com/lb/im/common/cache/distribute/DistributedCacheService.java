package com.lb.im.common.cache.distribute;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.json.JSONUtil;
import com.lb.im.common.cache.distribute.conversion.TypeConversion;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 分布式缓存接口，通用型接口，在满足分布式缓存的需求时，解决了缓存击穿、穿透和雪崩的问题
 */
public interface DistributedCacheService {

    /**
     * 永久缓存
     *
     * @param key   缓存键名
     * @param value 缓存值对象
     */
    void set(String key, Object value);

    /**
     * 将数据缓存一段时间
     *
     * @param key     缓存键名
     * @param value   缓存值对象
     * @param timeout 物理缓存时长（单位由timeUnit指定）
     * @param unit    时间单位
     */
    void set(String key, Object value, Long timeout, TimeUnit unit);

    /**
     * 设置缓存过期时间
     *
     * @param key     缓存键名
     * @param timeout 过期时长
     * @param unit    时间单位
     * @return 设置是否成功
     */
    Boolean expire(String key, final long timeout, final TimeUnit unit);

    /**
     * 设置缓存时附加逻辑过期时间，用于缓存重建机制
     *
     * @param key     缓存键名
     * @param value   缓存值对象
     * @param timeout 逻辑过期时长
     * @param unit    时间单位
     */
    void setWithLogicalExpire(String key, Object value, Long timeout, TimeUnit unit);

    /**
     * 获取缓存中的字符串值
     *
     * @param key 缓存键名
     * @return 缓存值字符串
     */
    String get(String key);

    /**
     * 将缓存值反序列化为目标类型对象
     *
     * @param key         缓存键名
     * @param targetClass 目标对象类型
     * @param <T>         泛型类型
     * @return 反序列化后的对象
     */
    <T> T getObject(String key, Class<T> targetClass);

    /**
     * 批量获取多个键的缓存值
     *
     * @param keys 键名集合
     * @return 键值列表（顺序与输入一致）
     */
    List<String> multiGet(Collection<String> keys);

    /**
     * 通过正则表达式匹配所有符合条件的键
     *
     * @param pattern 正则表达式模式
     * @return 匹配到的键集合
     */
    Set<String> keys(String pattern);

    /**
     * 删除指定键的缓存
     *
     * @param key 要删除的键名
     * @return 删除是否成功
     */
    Boolean delete(String key);

    /**
     * 防止缓存穿透的带参数查询，先查缓存再查数据库，最后写回缓存
     *
     * @param keyPrefix  缓存键前缀
     * @param id         业务标识（如ID）
     * @param type       缓存对象类型
     * @param dbFallback 数据库查询函数（参数为id）
     * @param timeout    缓存时长
     * @param unit       时间单位
     * @param <R>        返回值类型
     * @param <ID>       业务标识类型
     * @return 查询结果（缓存或数据库数据）
     */
    <R, ID> R queryWithPassThrough(String keyPrefix, ID id, Class<R> type, Function<ID, R> dbFallback, Long timeout, TimeUnit unit);

    /**
     * 无参数版本的防穿透查询
     *
     * @param keyPrefix  缓存键前缀
     * @param type       缓存对象类型
     * @param dbFallback 数据库查询函数（无参数）
     * @param timeout    缓存时长
     * @param unit       时间单位
     * @param <R>        返回值类型
     * @return 查询结果
     */
    <R> R queryWithPassThroughWithoutArgs(String keyPrefix, Class<R> type, Supplier<R> dbFallback, Long timeout, TimeUnit unit);

    /**
     * 防止缓存穿透的集合数据查询
     *
     * @param keyPrefix  缓存键前缀
     * @param id         业务标识
     * @param type       集合元素类型
     * @param dbFallback 数据库查询函数（返回List）
     * @param timeout    缓存时长
     * @param unit       时间单位
     * @param <R>        集合元素类型
     * @param <ID>       业务标识类型
     * @return 查询结果列表
     */
    <R, ID> List<R> queryWithPassThroughList(String keyPrefix, ID id, Class<R> type, Function<ID, List<R>> dbFallback, Long timeout, TimeUnit unit);

    /**
     * 无参数集合数据防穿透查询
     *
     * @param keyPrefix  缓存键前缀
     * @param type       集合元素类型
     * @param dbFallback 数据库查询函数（返回List）
     * @param timeout    缓存时长
     * @param unit       时间单位
     * @param <R>        集合元素类型
     * @return 查询结果列表
     */
    <R> List<R> queryWithPassThroughListWithoutArgs(String keyPrefix, Class<R> type, Supplier<List<R>> dbFallback, Long timeout, TimeUnit unit);

    /**
     * 使用逻辑过期时间的带参数查询，允许旧数据继续服务同时重建缓存
     *
     * @param keyPrefix  缓存键前缀
     * @param id         业务标识
     * @param type       缓存对象类型
     * @param dbFallback 数据库查询函数
     * @param timeout    逻辑过期时长
     * @param unit       时间单位
     * @param <R>        返回类型
     * @param <ID>       业务标识类型
     * @return 查询结果（可能包含过期数据）
     */
    <R, ID> R queryWithLogicalExpire(String keyPrefix, ID id, Class<R> type, Function<ID, R> dbFallback, Long timeout, TimeUnit unit);

    /**
     * 无参数逻辑过期查询
     *
     * @param keyPrefix  缓存键前缀
     * @param type       缓存对象类型
     * @param dbFallback 数据库查询函数
     * @param timeout    逻辑过期时长
     * @param unit       时间单位
     * @param <R>        返回类型
     * @return 查询结果
     */
    <R> R queryWithLogicalExpireWithoutArgs(String keyPrefix, Class<R> type, Supplier<R> dbFallback, Long timeout, TimeUnit unit);

    /**
     * 逻辑过期时间的集合数据查询
     *
     * @param keyPrefix  缓存键前缀
     * @param id         业务标识
     * @param type       集合元素类型
     * @param dbFallback 数据库查询函数（返回List）
     * @param timeout    逻辑过期时长
     * @param unit       时间单位
     * @param <R>        集合元素类型
     * @param <ID>       业务标识类型
     * @return 查询结果列表
     */
    <R, ID> List<R> queryWithLogicalExpireList(String keyPrefix, ID id, Class<R> type, Function<ID, List<R>> dbFallback, Long timeout, TimeUnit unit);

    /**
     * 无参数逻辑过期集合查询
     *
     * @param keyPrefix  缓存键前缀
     * @param type       集合元素类型
     * @param dbFallback 数据库查询函数（返回List）
     * @param timeout    逻辑过期时长
     * @param unit       时间单位
     * @param <R>        集合元素类型
     * @return 查询结果列表
     */
    <R> List<R> queryWithLogicalExpireListWithoutArgs(String keyPrefix, Class<R> type, Supplier<List<R>> dbFallback, Long timeout, TimeUnit unit);

    /**
     * 使用互斥锁防止缓存击穿的带参数查询
     *
     * @param keyPrefix  缓存键前缀
     * @param id         业务标识
     * @param type       缓存对象类型
     * @param dbFallback 数据库查询函数
     * @param timeout    缓存时长
     * @param unit       时间单位
     * @param <R>        返回类型
     * @param <ID>       业务标识类型
     * @return 查询结果
     */
    <R, ID> R queryWithMutex(String keyPrefix, ID id, Class<R> type, Function<ID, R> dbFallback, Long timeout, TimeUnit unit);

    /**
     * 无参数互斥锁查询
     *
     * @param keyPrefix  缓存键前缀
     * @param type       缓存对象类型
     * @param dbFallback 数据库查询函数
     * @param timeout    缓存时长
     * @param unit       时间单位
     * @param <R>        返回类型
     * @return 查询结果
     */
    <R> R queryWithMutexWithoutArgs(String keyPrefix, Class<R> type, Supplier<R> dbFallback, Long timeout, TimeUnit unit);

    /**
     * 互斥锁机制的集合数据查询
     *
     * @param keyPrefix  缓存键前缀
     * @param id         业务标识
     * @param type       集合元素类型
     * @param dbFallback 数据库查询函数（返回List）
     * @param timeout    缓存时长
     * @param unit       时间单位
     * @param <R>        集合元素类型
     * @param <ID>       业务标识类型
     * @return 查询结果列表
     */
    <R, ID> List<R> queryWithMutexList(String keyPrefix, ID id, Class<R> type, Function<ID, List<R>> dbFallback, Long timeout, TimeUnit unit);

    /**
     * 无参数互斥锁集合查询
     *
     * @param keyPrefix  缓存键前缀
     * @param type       集合元素类型
     * @param dbFallback 数据库查询函数（返回List）
     * @param timeout    缓存时长
     * @param unit       时间单位
     * @param <R>        集合元素类型
     * @return 查询结果列表
     */
    <R> List<R> queryWithMutexListWithoutArgs(String keyPrefix, Class<R> type, Supplier<List<R>> dbFallback, Long timeout, TimeUnit unit);

    /**
     * 将缓存值转换为目标泛型类型
     *
     * @param obj  缓存值对象（可能为简单类型或JSON字符串）
     * @param type 目标类型
     * @param <R>  泛型类型
     * @return 转换后的对象
     */
    default <R> R getResult(Object obj, Class<R> type) {
        if (obj == null) {
            return null;
        }
        // 简单类型直接转换，复杂类型通过JSON反序列化
        if (TypeConversion.isSimpleType(obj)) {
            return Convert.convert(type, obj);
        }
        return JSONUtil.toBean(JSONUtil.toJsonStr(obj), type);
    }

    /**
     * 将JSON字符串转换为泛型列表
     *
     * @param str  JSON数组字符串
     * @param type 集合元素类型
     * @param <R>  泛型类型
     * @return 泛型列表
     */
    default <R> List<R> getResultList(String str, Class<R> type) {
        if (StrUtil.isEmpty(str)) {
            return null;
        }
        // 直接使用JSON工具转换列表
        return JSONUtil.toList(JSONUtil.parseArray(str), type);
    }

    /**
     * 生成基础缓存键（无参数版本）
     *
     * @param key 基础键名
     * @return 最终键名
     */
    default String getKey(String key) {
        return getKey(key, null);
    }

    /**
     * 生成带参数的缓存键，支持复杂类型参数的MD5处理
     *
     * @param keyPrefix 缓存键前缀
     * @param id        业务标识（可为复杂对象）
     * @param <ID>      业务标识类型
     * @return 唯一缓存键
     */
    default <ID> String getKey(String keyPrefix, ID id) {
        if (id == null) {
            return keyPrefix;
        }
        String key = "";
        // 简单类型直接转换，复杂类型转JSON后MD5
        if (TypeConversion.isSimpleType(id)) {
            key = StrUtil.toString(id);
        } else {
            key = MD5.create().digestHex(JSONUtil.toJsonStr(id));
        }
        if (StrUtil.isEmpty(key)) {
            key = "";
        }
        return keyPrefix.concat(key);
    }

    /**
     * 将缓存值转换为存储格式
     *
     * @param value 缓存值对象
     * @return 存储字符串（简单类型直接转换，复杂类型转JSON）
     */
    default String getValue(Object value) {
        return TypeConversion.isSimpleType(value) ? String.valueOf(value) : JSONUtil.toJsonStr(value);
    }

}
