package com.fh.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool {

    private RedisPool(){

    }

    private static JedisPool jedisPool;

    private static void initPool(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(1000);
        jedisPoolConfig.setMinIdle(500);
        jedisPoolConfig.setMaxIdle(500);
        jedisPool = new JedisPool(jedisPoolConfig, "192.168.35.131", 6379);
    }

    //只会在jvm虚拟机加载类，执行一次 【保证只能创建一个redis连接池】
    //static静态块只能调用静态的static方法
    static {
        initPool();
    }

    public static Jedis getResource(){
        return jedisPool.getResource();
    }

}
