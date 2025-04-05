package org.dromara.omind.userplat.domain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.dromara.omind.userplat.api.domain.entity.OmindWalletEntity;
import org.dromara.omind.userplat.domain.mapper.OmindWalletEntityMapper;
import org.dromara.omind.userplat.domain.service.OmindWalletEntityIService;
import org.springframework.stereotype.Repository;

@Repository
public class OmindWalletEntityServiceImpl extends ServiceImpl<OmindWalletEntityMapper, OmindWalletEntity> implements OmindWalletEntityIService {
} 