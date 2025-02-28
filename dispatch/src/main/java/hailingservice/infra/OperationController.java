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
// @RequestMapping(value="/operations")
@Transactional
public class OperationController {

    @Autowired
    OperationRepository operationRepository;

    @RequestMapping(
        value = "/operations/{id}/operate",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public Operation operate(
        @PathVariable(value = "id") Long id,
        @RequestBody OperateCommand operateCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /operation/operate  called #####");
        Optional<Operation> optionalOperation = operationRepository.findById(
            id
        );

        optionalOperation.orElseThrow(() -> new Exception("No Entity Found"));
        Operation operation = optionalOperation.get();
        operation.operate(operateCommand);

        operationRepository.save(operation);
        return operation;
    }

    @RequestMapping(
        value = "/operations/{id}/completeoperation",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public Operation completeOperation(
        @PathVariable(value = "id") Long id,
        @RequestBody CompleteOperationCommand completeOperationCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /operation/completeOperation  called #####");
        Optional<Operation> optionalOperation = operationRepository.findById(
            id
        );

        optionalOperation.orElseThrow(() -> new Exception("No Entity Found"));
        Operation operation = optionalOperation.get();
        operation.completeOperation(completeOperationCommand);

        operationRepository.save(operation);
        return operation;
    }
}
//>>> Clean Arch / Inbound Adaptor
