package com.demo.rabbitmq.config;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Primary
@Configuration
@EnableRedisRepositories
public class RabbitMQConfig {


    @Value("${rabbitmq.queue.name}")
    private String queue;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    @Value("${spring.data.redis.host}")
    private String databaseHostname;

    @Value("${spring.data.redis.port}")
    private int port;


    @Bean
    public Queue queue()
    {
        return new Queue(queue);
    }

    @Bean
    public TopicExchange exchange()
    {
        return  new TopicExchange(exchange);
    }

    @Bean
    public Binding binding()
    {
        return BindingBuilder.bind(queue()).to(exchange()).with(routingKey);
    }

    @Bean
    public JedisConnectionFactory connectionFactory()
  {
      System.out.println("Redis Hostname: "+databaseHostname);
      RedisStandaloneConfiguration configuration=new RedisStandaloneConfiguration();
      configuration.setUsername(databaseHostname);
      configuration.setPort(port);
      return new JedisConnectionFactory(configuration);

  }


    @Bean
    public RedisTemplate<String, Object> myRestTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new JdkSerializationRedisSerializer());
        template.setValueSerializer(new JdkSerializationRedisSerializer());
        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();
        System.out.println("Redis properties set");
        return template;
    }
/*
  @Bean
    JedisConnectionFactory jedisConnectionFactory() {

        return new JedisConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }

*/
}
