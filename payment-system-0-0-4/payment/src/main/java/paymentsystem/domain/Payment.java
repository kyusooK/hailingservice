package paymentsystem.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;
import paymentsystem.PaymentApplication;
import paymentsystem.domain.PaymentCancelled;
import paymentsystem.domain.PaymentCompleted;
import paymentsystem.domain.PaymentRefunded;
import paymentsystem.domain.RequestRefundCompleted;
import paymentsystem.domain.RequstPaymentCompleted;

@Entity
@Table(name = "Payment_table")
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // 주문 번호 
    private String paymentId; // 결제 번호
    private Integer price; // 결제 금액
    private String name; // 결제 상품 이름
    private String buyerId; // 구매자 아이디
    private String buyerName; // 구매자 이름
    private String buyerTel; // 구매자 전화번호
    private String buyerEmail; // 구매자 이메일
    private String status; // 결제 상태
    private String reason; // 결제 사유 (취소)

    @PostPersist
    public void onPostPersist() {
        RequstPaymentCompleted requstPaymentCompleted = new RequstPaymentCompleted(this);
        requstPaymentCompleted.publishAfterCommit();
    }

    public static PaymentRepository repository() {
        PaymentRepository paymentRepository = PaymentApplication.applicationContext.getBean(PaymentRepository.class);
        return paymentRepository;
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public void requestRefund(RequestRefundCommand requestRefundCommand) {
        //implement business logic here:

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public void receivePaymentStatus(ReceivePaymentStatusCommand receivePaymentStatusCommand) {

        if(receivePaymentStatusCommand.getStatus().equals("SUCESS")) {
            PaymentCompleted paymentCompleted = new PaymentCompleted(this);
            paymentCompleted.setId(receivePaymentStatusCommand.getId());
            paymentCompleted.setPaymentId(receivePaymentStatusCommand.getPaymentId());
            paymentCompleted.setStatus(receivePaymentStatusCommand.getStatus());
            paymentCompleted.setReason(receivePaymentStatusCommand.getReason());
            paymentCompleted.publishAfterCommit();
        } else if(receivePaymentStatusCommand.getStatus().equals("CANCELLED")) {
            PaymentCancelled paymentCancelled = new PaymentCancelled(this);
            paymentCancelled.setId(receivePaymentStatusCommand.getId());
            paymentCancelled.setPaymentId(receivePaymentStatusCommand.getPaymentId());
            paymentCancelled.setStatus(receivePaymentStatusCommand.getStatus());
            paymentCancelled.setReason(receivePaymentStatusCommand.getReason());
            paymentCancelled.publishAfterCommit();
        }
    }

}
//>>> DDD / Aggregate Root
