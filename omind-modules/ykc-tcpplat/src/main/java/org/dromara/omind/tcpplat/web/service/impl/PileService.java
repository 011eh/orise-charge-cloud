package org.dromara.omind.tcpplat.web.service.impl;

import org.dromara.omind.tcpplat.netty.service.IPileService;
import org.springframework.stereotype.Service;

@Service
public class PileService implements IPileService {

    @Override
    public boolean authenticate(String pileCode) {
        return true;
    }

    @Override
    public boolean isRuleConsistent(String pileCode, String priceRuleCode) {
        return true;
    }
}
