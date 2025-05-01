package br.com.postech.techchallange.adapter.out.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service("redisQrcodeService")
public class RedisQrcodeService extends RedisAbstractService<String, String> {

	public RedisQrcodeService(@Qualifier("stringRedisTemplate") RedisTemplate<String, String> redisTemplate) {
		super(redisTemplate, Duration.ofMinutes(10));
	}
}