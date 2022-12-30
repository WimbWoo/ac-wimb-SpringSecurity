package com.wim.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wim.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
