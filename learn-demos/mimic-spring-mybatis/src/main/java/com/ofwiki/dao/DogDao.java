package com.ofwiki.dao;

import com.ofwiki.mapper.Mapper;
import com.ofwiki.mapper.Param;
import com.ofwiki.mapper.Select;

/**
 * @author HuangJS
 * @date 2019-08-28 5:54 PM
 */
@Mapper
public interface DogDao {

    @Select("select * from table where age=#{age} and name = #{name}")
    String query(@Param("name") String name, @Param("age") int age);
}
