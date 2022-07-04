package com.test.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.store.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
