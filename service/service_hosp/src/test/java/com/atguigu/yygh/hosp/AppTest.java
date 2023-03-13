package com.atguigu.yygh.hosp;


import com.atguigu.yygh.hosp.ServiceHospMainStarter;
import com.atguigu.yygh.hosp.bean.Actor;

import com.mongodb.client.result.DeleteResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;


import java.util.*;
import java.util.regex.Pattern;

@SpringBootTest(classes = ServiceHospMainStarter.class)
public class AppTest {


    @Autowired
    private MongoTemplate mongoTemplate;

    // 分页查询
    @Test
    public void testQueryPage() {

        int pageNum = 1;
        int size = 3;

        Query query = new Query(Criteria.where("gender").is(false));

        long total = mongoTemplate.count(query, Actor.class);

        List<Actor> actors = mongoTemplate.find(query.skip((pageNum - 1) * size).limit(size), Actor.class);

        actors.forEach(System.out::println);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", total);
        map.put("rows", actors);
//        return map;
        System.out.println(total);
    }

    // 查询
    @Test
    public void testQuery() {
//        Actor actor = mongoTemplate.findById("112", Actor.class);
//        System.out.println(actor);

//        List<Actor> all = mongoTemplate.findAll(Actor.class);
//        all.forEach(System.out::println);

//        Query query = new Query(Criteria.where("age").is(19));
//        List<Actor> actors = mongoTemplate.find(query, Actor.class);
//        actors.forEach(System.out::println);

        String result = String.format("%s%s%s", ".*", "之", ".*");

        Pattern pattern = Pattern.compile(result, Pattern.CASE_INSENSITIVE);
        Query query = new Query(Criteria.where("actorName").regex(pattern));
        List<Actor> actors = mongoTemplate.find(query, Actor.class);
        actors.forEach(System.out::println);


    }


    // 修改
    @Test
    public void testUpdate() {
        Query query = new Query(Criteria.where("gender").is(true));
        Update update = new Update();
//        update.set("gender", true);
//        update.set("birth", new Date());

        update.set("age", 18);

        mongoTemplate.updateFirst(query, update, Actor.class);
    }


    // 删除
    @Test
    public void testDelete() {
        Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where("id").is("1"), Criteria.where("actorName").is("关之琳"));
        Query query = new Query(criteria);

        DeleteResult remove = mongoTemplate.remove(query, Actor.class);
        System.out.println(remove.getDeletedCount());
    }


    @Test
    public void testBatchInsert() {
        List<Actor> actors = new ArrayList<>();
        actors.add(new Actor("14", "谢霆锋", true, new Date()));
        actors.add(new Actor("15", "张柏芝", false, new Date()));
        actors.add(new Actor("16", "王菲", false, new Date()));
        actors.add(new Actor("17", "李亚鹏", true, new Date()));
        mongoTemplate.insert(actors, Actor.class);
    }

    @Test
    public void testModify() {
        Actor actor = mongoTemplate.findById("1", Actor.class);
        actor.setActorName("朱丽叶");
        mongoTemplate.save(actor);
    }

    @Test
    public void testInsert() {

//        mongoTemplate.insert(new Actor("112","梁朝伟",true,new Date()));
        Actor actor = new Actor();
        actor.setId("2");
        actor.setActorName("郭富城");
        mongoTemplate.save(actor);

//        mongoTemplate.save(new Actor("1", "刘德华", true, new Date()));

    }

}
