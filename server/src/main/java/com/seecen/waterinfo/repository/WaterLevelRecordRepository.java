package com.seecen.waterinfo.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seecen.waterinfo.domain.entity.WaterLevelRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WaterLevelRecordRepository extends BaseMapper<WaterLevelRecord> {
}
