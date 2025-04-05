package org.dromara.omind.userplat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.omind.userplat.api.domain.dto.OmindRechargePackageDto;
import org.dromara.omind.userplat.api.domain.entity.OmindRechargePackageEntity;
import org.dromara.omind.userplat.domain.service.OmindRechargePackageEntityIService;
import org.dromara.omind.userplat.service.OmindRechargePackageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OmindRechargePackageServiceImpl implements OmindRechargePackageService {

    private final OmindRechargePackageEntityIService rechargePackageEntityIService;

    @Override
    public List<OmindRechargePackageDto> getRechargePackageList() {
        LambdaQueryWrapper<OmindRechargePackageEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OmindRechargePackageEntity::getEnableState, 1)
                .eq(OmindRechargePackageEntity::getIsDel, 0)
                .orderByAsc(OmindRechargePackageEntity::getRechargeMoney);

        List<OmindRechargePackageEntity> list = rechargePackageEntityIService.list(queryWrapper);
        return BeanUtil.copyToList(list, OmindRechargePackageDto.class);
    }

    @Override
    public TableDataInfo<OmindRechargePackageDto> getRechargePackageListForAdmin(OmindRechargePackageDto packageDto, PageQuery pageQuery) {
        LambdaQueryWrapper<OmindRechargePackageEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(packageDto.getEnableState() != null, OmindRechargePackageEntity::getEnableState, packageDto.getEnableState())
                .eq(OmindRechargePackageEntity::getIsDel, 0)
                .like(StringUtils.isNotBlank(packageDto.getPackageName()), OmindRechargePackageEntity::getPackageName, packageDto.getPackageName())
                .orderByAsc(OmindRechargePackageEntity::getRechargeMoney);

        Page<OmindRechargePackageEntity> page = rechargePackageEntityIService.page(pageQuery.build(), queryWrapper);

        return TableDataInfo.build(BeanUtil.copyToList(page.getRecords(), OmindRechargePackageDto.class));
    }

    @Override
    public Boolean addRechargePackage(OmindRechargePackageEntity packageEntity) {
        packageEntity.setIsDel(0);
        packageEntity.setEnableState(1);
        return rechargePackageEntityIService.save(packageEntity);
    }

    @Override
    public Boolean updateRechargePackage(OmindRechargePackageEntity packageEntity) {
        if (packageEntity.getPackageId() == null) {
            return false;
        }
        return rechargePackageEntityIService.updateById(packageEntity);
    }

    @Override
    public Boolean deleteRechargePackage(Long packageId) {
        OmindRechargePackageEntity packageEntity = new OmindRechargePackageEntity();
        packageEntity.setPackageId(packageId);
        packageEntity.setIsDel(1);
        return rechargePackageEntityIService.updateById(packageEntity);
    }

    @Override
    public OmindRechargePackageDto getRechargePackageById(Long packageId) {
        OmindRechargePackageEntity entity = rechargePackageEntityIService.getById(packageId);
        return entity != null ? BeanUtil.copyProperties(entity, OmindRechargePackageDto.class) : null;
    }
} 
