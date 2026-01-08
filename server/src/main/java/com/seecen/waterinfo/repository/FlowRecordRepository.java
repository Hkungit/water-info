package com.seecen.waterinfo.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seecen.waterinfo.domain.entity.FlowRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FlowRecordRepository extends BaseMapper<FlowRecord> {
}
