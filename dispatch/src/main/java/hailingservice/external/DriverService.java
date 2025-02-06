package hailingservice.external;

import java.util.Date;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "driver", url = "${api.url.driver}")
public interface DriverService {
    @GetMapping(path = "/drivers/search/getDriverLocation")
    public List<Driver> getDriverLocation(String driverLocation);
}
