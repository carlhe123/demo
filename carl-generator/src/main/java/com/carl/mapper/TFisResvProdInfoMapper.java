package com.carl.mapper;

import com.carl.modal.TFisResvProdInfo;

public interface TFisResvProdInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TFisResvProdInfo record);

    int insertSelective(TFisResvProdInfo record);

    TFisResvProdInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TFisResvProdInfo record);

    int updateByPrimaryKey(TFisResvProdInfo record);
}