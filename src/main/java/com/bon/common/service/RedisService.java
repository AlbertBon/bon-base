package com.bon.common.service;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

import java.util.List;
import java.util.Set;

/**
 * @program: bon基础项目
 * @description: redis接口
 * @author: Bon
 * @create: 2018-05-16 17:04
 **/
public interface RedisService extends CacheManager {
    public boolean set(String key, String value);

    public void create(String key, String value);

    public void createAndExpire(String key, String value, long expire);

    public String get(String key);

    public Set<String> keys(String pattern);

    public String findKey(String pattern);

    public void removeByPattern(String pattern);

    public boolean expire(String key, long expire);

    public <T> boolean setList(String key, List<T> list);

    public <T> List<T> getList(String key, Class<T> clz);

    public long lpush(String key, Object obj);

    public long rpush(String key, Object obj);

    public String lpop(String key);

    public void del(String key);

    boolean check(String pattern);

    @Override
    <K, V> Cache<K, V> getCache(String s) throws CacheException;
}
