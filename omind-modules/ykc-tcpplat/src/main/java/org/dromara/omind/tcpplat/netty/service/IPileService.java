package org.dromara.omind.tcpplat.netty.service;

public interface IPileService {

    boolean authenticate(String pileCode);

    boolean isRuleConsistent(String pileCode, String priceRuleCode);
}
