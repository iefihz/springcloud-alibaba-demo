package com.iefihz.cloud.plugins.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;

/**
 * 这个配置主要为了解决默认情况下只支持RedisTemplate<String, String>
 *
 * opsForXXX和boundXXXOps的区别：
 * XXX为value的类型，前者获取一个operator，但是没有指定操作的对象（key），可以在一个连接（事务）内操作多个key以及对应的value；
 * 后者获取了一个指定操作对象（key）的operator，在一个连接（事务）内只能操作这个key对应的value。
 *
 * @author He Zhifei
 * @date 2020/9/27 11:55
 */
@Configuration
public class RedisConfig {

    /**
     * 值的序列化、反序列化使用{@link GenericJackson2JsonRedisSerializer}
     * @param connectionFactory
     * @return
     */
    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Serializable> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, Serializable> template = new RedisTemplate<String, Serializable>();
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer();
        template.setKeySerializer(keySerializer);
        template.setHashKeySerializer(keySerializer);
        template.setValueSerializer(valueSerializer);
        template.setHashValueSerializer(valueSerializer);
//        template.setDefaultSerializer(valueSerializer);
        template.setConnectionFactory(connectionFactory);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * key/value（也支持byte[]）都为较为复杂的Object类型时使用objectRedisTemplate
     * @param connectionFactory
     * @return
     */
    @Bean(name = "objectRedisTemplate")
    public RedisTemplate<Serializable, Serializable> objectRedisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<Serializable, Serializable> template = new RedisTemplate<Serializable, Serializable>();
        MyRedisValueSerializer valueSerializer = new MyRedisValueSerializer();
        template.setKeySerializer(valueSerializer);
        template.setHashKeySerializer(valueSerializer);
        template.setValueSerializer(valueSerializer);
        template.setHashValueSerializer(valueSerializer);
        template.setDefaultSerializer(valueSerializer);
        template.setConnectionFactory(connectionFactory);
        template.afterPropertiesSet();
        return template;
    }

    private class MyRedisValueSerializer implements RedisSerializer {
        @Override
        public byte[] serialize(Object o) throws SerializationException {
            return org.springframework.util.SerializationUtils.serialize(o);
        }

        @Override
        public Object deserialize(byte[] bytes) throws SerializationException {
            return org.springframework.util.SerializationUtils.deserialize(bytes);
        }
    }

//    /**
//     * 值的序列化、反序列化使用{@link Jackson2JsonRedisSerializer}
//     * @param connectionFactory
//     * @return
//     */
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory connectionFactory) {
//        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
//        template.setConnectionFactory(connectionFactory);
//
//        // 使用注解@Bean返回RedisTemplate时，同时配置hashKey与HashValue的序列化方式
//        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
//        Jackson2JsonRedisSerializer jacksonSerializer = new Jackson2JsonRedisSerializer(Object.class);
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jacksonSerializer.setObjectMapper(objectMapper);
//        // key采用String的序列化方式
//        template.setKeySerializer(stringRedisSerializer);
//        // value序列化方式采用Jackson
//        template.setValueSerializer(jacksonSerializer);
//        // hash的key也采用String的序列化方式
//        template.setHashKeySerializer(stringRedisSerializer);
//        // hash的vlaue序列化采用Jackson
//        template.setHashValueSerializer(jacksonSerializer);
//
//        template.afterPropertiesSet();
//        return template;
//    }

}
