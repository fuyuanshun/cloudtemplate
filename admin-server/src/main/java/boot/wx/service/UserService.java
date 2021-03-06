package boot.wx.service;

import boot.wx.service.fallback.UserServiceFallbackFactory;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// @Component注解为了去除掉注入的地方报红，不加也可以
@Component
@FeignClient(value = "user-server", fallbackFactory = UserServiceFallbackFactory.class)
public interface UserService {

    @GetMapping("/test/remote/{str}")
    String test(@PathVariable("str") String str);
}
