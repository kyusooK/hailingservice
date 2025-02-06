package hailingservice.infra;

import hailingservice.domain.*;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//<<< Clean Arch / Inbound Adaptor

@RestController
// @RequestMapping(value="/drivers")
@Transactional
public class DriverController {

    @Autowired
    DriverRepository driverRepository;

    @RequestMapping(
        value = "/drivers/{id}/confirmlicense",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public Driver confirmLicense(
        @PathVariable(value = "id") Long id,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /driver/confirmLicense  called #####");
        Optional<Driver> optionalDriver = driverRepository.findById(id);

        optionalDriver.orElseThrow(() -> new Exception("No Entity Found"));
        Driver driver = optionalDriver.get();
        driver.confirmLicense();

        driverRepository.save(driver);
        return driver;
    }

    @RequestMapping(
        value = "/drivers/{id}/acceptcarhailing",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public Driver acceptCarhailing(
        @PathVariable(value = "id") Long id,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /driver/acceptCarhailing  called #####");
        Optional<Driver> optionalDriver = driverRepository.findById(id);

        optionalDriver.orElseThrow(() -> new Exception("No Entity Found"));
        Driver driver = optionalDriver.get();
        driver.acceptCarhailing();

        driverRepository.save(driver);
        return driver;
    }

    @RequestMapping(
        value = "/drivers/{id}/changeoperationstatus",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public Driver changeOperationstatus(
        @PathVariable(value = "id") Long id,
        @RequestBody ChangeOperationstatusCommand changeOperationstatusCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /driver/changeOperationstatus  called #####");
        Optional<Driver> optionalDriver = driverRepository.findById(id);

        optionalDriver.orElseThrow(() -> new Exception("No Entity Found"));
        Driver driver = optionalDriver.get();
        driver.changeOperationstatus(changeOperationstatusCommand);

        driverRepository.save(driver);
        return driver;
    }
}
//>>> Clean Arch / Inbound Adaptor
