package com.markerhub.shiro.cache;


import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * 重写Shiro缓存管理器
 * @author dolyw.com
 * @date 2018/9/4 17:41
 */
@Component
@Primary
public class CustomCacheManager implements CacheManager {
    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        System.out.println("getCache:"+s);
        return new CustomCache<K,V>(s);
    }
}

