package com.seecen.waterinfo.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seecen.waterinfo.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRepository extends BaseMapper<User> {
}
