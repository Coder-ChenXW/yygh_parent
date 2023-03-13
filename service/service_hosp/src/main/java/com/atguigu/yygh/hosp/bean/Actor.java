package com.atguigu.yygh.hosp.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "Actor")
public class Actor {

    @Id
    private String id;
    @Field(value = "actor_name")
    private String actorName;
    private boolean gender;
    private Date birth;


}
