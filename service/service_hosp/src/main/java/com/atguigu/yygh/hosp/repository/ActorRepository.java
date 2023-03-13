package com.atguigu.yygh.hosp.repository;


import com.atguigu.yygh.hosp.bean.Actor;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface ActorRepository extends MongoRepository<Actor, String> {

    public List<Actor> findByActorNameLikeAndGender(String name,boolean gender);

}
