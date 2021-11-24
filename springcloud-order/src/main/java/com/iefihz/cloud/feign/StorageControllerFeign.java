package com.iefihz.cloud.feign;

import com.iefihz.cloud.plugins.exception.Res;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "springcloud-storage")
@RequestMapping("/storage")
public interface StorageControllerFeign {

    @GetMapping("/port")
    public Res port();
}
