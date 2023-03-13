package com.atguigu.yygh.hosp;


import com.atguigu.yygh.hosp.bean.Actor;
import com.atguigu.yygh.hosp.repository.ActorRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class RepositoryTest {

    @Autowired
    private ActorRepository actorRepository;


    @Test
    public void testSelfDefinitionMethod() {
        List<Actor> actors = actorRepository.findByActorNameLikeAndGender("王",false);
        actors.forEach(System.out::println);
    }


    @Test
    public void testPage() {
        int pageNum = 1;
        int pageSize = 3;
        Actor actor = new Actor();
        actor.setGender(true);
        Example<Actor> example = Example.of(actor);
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by("id"));
        Page<Actor> page = actorRepository.findAll(example, pageable);
        System.out.println("总记录数：" + page.getTotalElements());
        System.out.println("总页数：" + page.getTotalPages());
        System.out.println("当前页：" + page.getNumber());
        System.out.println("当前页列表");
        for (Actor actor1 : page.getContent()) {
            System.out.println(actor1);
        }
    }

    @Test
    public void testQuery() {
//        Actor actor = actorRepository.findById("24").get();
//        System.out.println("actor = " + actor);

        Actor actor = new Actor();
//        actor.setActorName("岳不群");
        actor.setGender(true);

//        ExampleMatcher matcher = ExampleMatcher.matching()
//                .withMatcher(ExampleMatcher.StringMatcher.CONTAINING)
//                .withIgnoreCase(true);

        Example<Actor> example = Example.of(actor);

        List<Actor> all = actorRepository.findAll(example);
        all.forEach(System.out::println);

    }


    @Test
    public void testUpdate() {
        Actor actor = new Actor();
        actor.setId("14");
        actor.setActorName("张学友");
        actor.setGender(true);
        actor.setBirth(new Date());
        actorRepository.save(actor);
    }


    @Test
    public void testDelete() {
//        actorRepository.deleteById("17");
        Actor actor = new Actor();
//        actor.setActorName("谢霆锋");
        actor.setId("14");
        actorRepository.delete(actor);
    }


    @Test
    public void testInsert() {
//        actorRepository.insert(new Actor("18", "东方不败", true, new Date()));
//        actorRepository.save(new Actor("19", "任我行", true, new Date()));
        List<Actor> actorList = new ArrayList<>();
        actorList.add(new Actor("24", "岳不群", false, new Date()));
        actorList.add(new Actor("25", "左冷禅", false, new Date()));
        actorList.add(new Actor("26", "莫大", false, new Date()));

//        actorRepository.insert(actorList);

        actorRepository.saveAll(actorList);

    }

}
