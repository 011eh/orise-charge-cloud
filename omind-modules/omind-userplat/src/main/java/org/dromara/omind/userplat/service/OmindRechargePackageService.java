package org.dromara.omind.userplat.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.omind.userplat.api.domain.dto.OmindRechargePackageDto;
import org.dromara.omind.userplat.api.domain.entity.OmindRechargePackageEntity;

import java.util.List;

/**
 * 充值套餐服务接口
 */
public interface OmindRechargePackageService {

    /**
     * 获取充值套餐列表
     *
     * @return 套餐列表
     */
    List<OmindRechargePackageDto> getRechargePackageList();

    /**
     * 获取充值套餐（后台管理）
     *
     * @param packageDto 查询条件
     * @param pageQuery  分页查询
     * @return 套餐列表
     */
    TableDataInfo<OmindRechargePackageDto> getRechargePackageListForAdmin(OmindRechargePackageDto packageDto, PageQuery pageQuery);

    /**
     * 添加充值套餐
     *
     * @param packageEntity 套餐信息
     * @return 操作结果
     */
    Boolean addRechargePackage(OmindRechargePackageEntity packageEntity);

    /**
     * 更新充值套餐
     *
     * @param packageEntity 套餐信息
     * @return 操作结果
     */
    Boolean updateRechargePackage(OmindRechargePackageEntity packageEntity);

    /**
     * 删除充值套餐
     *
     * @param packageId 套餐ID
     * @return 操作结果
     */
    Boolean deleteRechargePackage(Long packageId);

    /**
     * 根据ID获取充值套餐
     *
     * @param packageId 套餐ID
     * @return 充值套餐
     */
    OmindRechargePackageDto getRechargePackageById(Long packageId);
} 