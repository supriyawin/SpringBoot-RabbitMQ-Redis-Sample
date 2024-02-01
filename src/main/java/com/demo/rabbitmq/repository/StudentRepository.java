package com.demo.rabbitmq.repository;

import com.demo.rabbitmq.dto.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentRepository {


    @Qualifier("myRestTemplate")
    @Autowired
    private  RedisTemplate<String,Object> template;

    private static final String KEY = "Student";

    public boolean saveStudent(Student s)
    {
        System.out.println("Coming inside Repository");

            template.opsForHash().put(KEY, s.getId(), s);

        return true;
    }

    public Student getStudent(String id) {
        return (Student) template.opsForHash().get(KEY,id);
    }
}
