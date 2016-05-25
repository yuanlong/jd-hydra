package com.jd.bdp.hydra.agent.support;


import com.jd.bdp.hydra.Span;
import com.jd.bdp.hydra.agent.CollectorService;
import com.jd.bdp.hydra.dubbomonitor.HydraService;
import com.jd.bdp.hydra.dubbomonitor.RegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Date: 13-3-27
 * Time: 上午10:57
 */
public class TraceService implements com.jd.bdp.hydra.agent.RegisterService, CollectorService {

    private static final Logger logger = LoggerFactory.getLogger(TraceService.class);

    private RegisterService registerService;
    private HydraService hydraService;
    private Map<String, String> registerInfo;
    public static final String SEED = "seed";
    private boolean isRegister = false;

    public boolean isRegister() {
        return isRegister;
    }

    @Override
    public void sendSpan(List<Span> spanList) {
        //fixme try-catch性能影响？
        try {
            hydraService.push(spanList);
        } catch (Exception e) {
            logger.warn("Trace data push failure~");
        }
    }


    @Override
    public boolean registerService(String serviceName) {
        logger.info("*****" + serviceName);
        String serviceId = null;
        try {
            serviceId = registerService.registerClient(serviceName);
        } catch (Exception e) {
            logger.warn("[Hydra] client cannot regist service <" + serviceName + "> into the hydra system");
        }
        if (serviceId != null) {
            logger.info("[Hydra] Registry ["+serviceName+"] option is ok!");
            //更新本地注册信息
            registerInfo.put(serviceName, serviceId);
            return true;
        } else{
            return false;
        }
    }

    @Override
    public boolean registerService(List<String> services) {
        // logger.info(name + " " + services);
        try {
            this.registerInfo = registerService.registerClient(services);
        } catch (Exception e) {
            logger.warn("[Hydra] Client global config-info cannot regist into the hydra system");
        }
        if (registerInfo != null) {
            logger.info("[Hydra] Global registry option is ok!");
            isRegister = true;
        }
        return isRegister;
    }

    public RegisterService getRegisterService() {
        return registerService;
    }

    public void setRegisterService(RegisterService registerService) {
        this.registerService = registerService;
    }

    public HydraService getHydraService() {
        return hydraService;
    }

    public void setHydraService(HydraService hydraService) {
        this.hydraService = hydraService;
    }

    public String getServiceId(String service) {
        if (isRegister && registerInfo.containsKey(service))
            return registerInfo.get(service);
        else
            return null;
    }

    public Long getSeed() {
        String s = null;
        if (isRegister) {
            s = registerInfo.get(SEED);
            return Long.valueOf(s);
        }
        return null;
    }
}
