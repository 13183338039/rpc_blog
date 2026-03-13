package com.markerhub.shiro.cache;

import com.markerhub.shiro.AccountProfile;
import com.markerhub.util.JwtUtils;
import com.markerhub.util.JedisUtil;
import com.markerhub.common.Constant;
import com.markerhub.util.PropertiesUtil;
import com.markerhub.util.SerializableUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.subject.SimplePrincipalCollection;

import java.util.*;

/**
 * 重写Shiro的Cache保存读取
 * @author dolyw.com
 * @date 2018/9/4 17:31
 */
@Slf4j
public class CustomCache<K,V> implements Cache<K,V> {

    /**
     * 缓存的key名称获取为shiro:cache:account
     * @param key
     * @return java.lang.String
     * @author dolyw.com
     * @date 2018/9/4 18:33
     */
    public String cachename;

    public  CustomCache(String s){
        this.cachename=s;
    }
    private String getKey(Object key) {
        //log.info("gen key:"+key);
        log.info("cachename:"+cachename);
        log.info("key类型："+key.getClass().getName());
        if(key instanceof SimplePrincipalCollection){
            Object principal = ((SimplePrincipalCollection) key).getPrimaryPrincipal();
            String a=Constant.PREFIX_SHIRO_CACHE +this.cachename+((AccountProfile)principal).getUsername();
            return a;
        }
        return Constant.PREFIX_SHIRO_CACHE +this.cachename+ JwtUtils.getClaim(key.toString(), Constant.ACCOUNT);
    }

    /**
     * 获取缓存
     */
    @Override
    public Object get(Object key) throws CacheException {
        String a=this.getKey(key);
//        if(key instanceof SimplePrincipalCollection){
//            Object principal = ((SimplePrincipalCollection) key).getPrimaryPrincipal();
//            a=Constant.PREFIX_SHIRO_CACHE +((AccountProfile)principal).getUsername();
//        }else{
//            System.out.println(key.getClass().getName());
//            a=this.getKey(key);
//        }

        //System.out.println("查找缓存:("+a+")");
        log.info("查找缓存:("+a+")");
        if(Boolean.FALSE.equals(JedisUtil.exists(a))){
            System.out.println("查找缓存fail");
            log.info("  fail!");
            return null;
        }
        //System.out.println("查找缓存-cunzai");
        log.info("  success!");
        return JedisUtil.getObject(this.getKey(key));
    }

    /**
     * 保存缓存
     */
    @Override
    public Object put(Object key, Object value) throws CacheException {
        // 读取配置文件，获取Redis的Shiro缓存过期时间
        //System.out.println("设置缓存:("+key+","+value+")");
        log.info("K 类型是：{}", key.getClass().getName());
        log.info("V 类型是：{}", value.getClass().getName());
        PropertiesUtil.readProperties("config.properties");
        String shiroCacheExpireTime = PropertiesUtil.getProperty("shiroCacheExpireTime");
        // 设置Redis的Shiro缓存
        String a=this.getKey(key);
        log.info("调用自定义缓存的put函数:");
        log.info("      key="+a);
        log.info("      value="+value);
        return JedisUtil.setObject(this.getKey(key), value, Integer.parseInt(shiroCacheExpireTime));
    }

    /**
     * 移除缓存
     */
    @Override
    public Object remove(Object key) throws CacheException {
        log.info("remove key 类型是：{}", key.getClass().getName());
        log.info("调用自定义缓存的remove函数 key= "+key);
        if (key instanceof SimplePrincipalCollection){
            return null;
        }
        AccountProfile a1=(AccountProfile)key;
        log.info(": "+a1.getUsername());
        if(Boolean.FALSE.equals(JedisUtil.exists(Constant.PREFIX_SHIRO_CACHE +this.cachename+((AccountProfile)key).getUsername()))){
            return null;
        }
        JedisUtil.delKey(Constant.PREFIX_SHIRO_CACHE +this.cachename+((AccountProfile)key).getUsername());
        log.info("      success!");
        return null;
    }

    /**
     * 清空所有缓存
     */
    @Override
    public void clear() throws CacheException {
        Objects.requireNonNull(JedisUtil.getJedis()).flushDB();
    }

    /**
     * 缓存的个数
     */
    @Override
    public int size() {
        Long size = Objects.requireNonNull(JedisUtil.getJedis()).dbSize();
        return size.intValue();
    }

    /**
     * 获取所有的key
     */
    @Override
    public Set keys() {
        Set<byte[]> keys = Objects.requireNonNull(JedisUtil.getJedis()).keys("*".getBytes());
        Set<Object> set = new HashSet<Object>();
        for (byte[] bs : keys) {
            set.add(SerializableUtil.unserializable(bs));
        }
        return set;
    }

    /**
     * 获取所有的value
     */
    @Override
    public Collection values() {
        Set keys = this.keys();
        List<Object> values = new ArrayList<Object>();
        for (Object key : keys) {
            values.add(JedisUtil.getObject(this.getKey(key)));
        }
        return values;
    }
}

