package paymentsystem.infra;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import paymentsystem.domain.*;

//<<< Clean Arch / Inbound Adaptor

@RestController
// @RequestMapping(value="/payments")
@Transactional
public class PaymentController {

    @Autowired
    PaymentRepository paymentRepository;

   
    @RequestMapping(
        value = "/payments/{id}/requestcancelled",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public Payment requestCancelled(
        @PathVariable(value = "id") Long id,
        @RequestBody RequestCancelledCommand requestCancelledCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /payment/requestRefund  called #####");
        Optional<Payment> optionalPayment = paymentRepository.findById(id);

        optionalPayment.orElseThrow(() -> new Exception("No Entity Found"));
        Payment payment = optionalPayment.get();
        payment.requestCancelled(requestCancelledCommand);
        payment.setStatus(requestCancelledCommand.getStatus());
        payment.setReason(requestCancelledCommand.getReason());
        paymentRepository.save(payment);
        return payment;
    }

    @RequestMapping(
        value = "/payments/{id}/receivepaymentcompleted",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public Payment receivePaymentCompleted(
        @PathVariable(value = "id") Long id,
        @RequestBody ReceivePaymentCompletedCommand receivePaymentCompletedCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println(
            "##### /payment/receivePaymentCompleted  called #####"
        );
        Optional<Payment> optionalPayment = paymentRepository.findById(id);

        optionalPayment.orElseThrow(() -> new Exception("No Entity Found"));
        Payment payment = optionalPayment.get();
        payment.receivePaymentCompleted(receivePaymentCompletedCommand);
        payment.setStatus(receivePaymentCompletedCommand.getStatus());
        payment.setReason(receivePaymentCompletedCommand.getReason());

        paymentRepository.save(payment);
        return payment;
    }

    @RequestMapping(
        value = "/payments/{id}/receivecancelledcompleted",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public Payment receiveCancelledCompleted(
        @PathVariable(value = "id") Long id,
        @RequestBody ReceiveCancelledCompletedCommand receiveCancelledCompletedCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println(
            "##### /payment/receiveCancelledCompleted  called #####"
        );
        Optional<Payment> optionalPayment = paymentRepository.findById(id);

        optionalPayment.orElseThrow(() -> new Exception("No Entity Found"));
        Payment payment = optionalPayment.get();
        payment.receiveCancelledCompleted(receiveCancelledCompletedCommand);
        payment.setStatus(receiveCancelledCompletedCommand.getStatus());
        payment.setReason(receiveCancelledCompletedCommand.getReason());

        paymentRepository.save(payment);
        return payment;
    }
}
//>>> Clean Arch / Inbound Adaptor
