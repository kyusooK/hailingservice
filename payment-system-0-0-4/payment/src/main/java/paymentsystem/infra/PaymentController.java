package paymentsystem.infra;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import paymentsystem.domain.*;

//<<< Clean Arch / Inbound Adaptor

@RestController
// @RequestMapping(value="/payments")
@Transactional
public class PaymentController {

    @Autowired
    PaymentRepository paymentRepository;

   
    @RequestMapping(
        value = "/payments/{id}/requestrefund",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public Payment requestRefund(
        @PathVariable(value = "id") Long id,
        @RequestBody RequestRefundCommand requestRefundCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /payment/requestRefund  called #####");
        Optional<Payment> optionalPayment = paymentRepository.findById(id);

        optionalPayment.orElseThrow(() -> new Exception("No Entity Found"));
        Payment payment = optionalPayment.get();
        payment.requestRefund(requestRefundCommand);

        paymentRepository.save(payment);
        return payment;
    }

    @RequestMapping(
        value = "/payments/{id}/receivepaymentstatus",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public Payment receivePaymentStatus(
        @PathVariable(value = "id") Long id,
        @RequestBody ReceivePaymentStatusCommand receivePaymentStatusCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /payment/receivePaymentStatus  called #####");
        Optional<Payment> optionalPayment = paymentRepository.findById(id);

        optionalPayment.orElseThrow(() -> new Exception("No Entity Found"));
        Payment payment = optionalPayment.get();
        payment.receivePaymentStatus(receivePaymentStatusCommand);
        paymentRepository.save(payment);
        return payment;
    }
}
//>>> Clean Arch / Inbound Adaptor
