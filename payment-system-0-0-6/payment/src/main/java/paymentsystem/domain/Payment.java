package paymentsystem.domain;

import javax.persistence.*;
import lombok.Data;
import paymentsystem.PaymentApplication;

@Entity
@Table(name = "Payment_table")
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // 결제 번호(고유번호) 
    private Long itemId; // 다른 서비스의 고유 번호
    private String paymentId; // PG 결제 번호
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

    public void requestCancelled(RequestCancelledCommand requestCancelledCommand) {
        //implement business logic here:
        RequestCancelledCompleted requestCancelledCompleted = new RequestCancelledCompleted(this);
        requestCancelledCompleted.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method
    public void receivePaymentCompleted(ReceivePaymentCompletedCommand receivePaymentCompletedCommand) {
        //implement business logic here:
        PaymentCompleted paymentCompleted = new PaymentCompleted(this);
        paymentCompleted.setStatus(receivePaymentCompletedCommand.getStatus());
        paymentCompleted.setReason(receivePaymentCompletedCommand.getReason());
        paymentCompleted.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method
    public void receiveCancelledCompleted(ReceiveCancelledCompletedCommand receiveCancelledCompletedCommand) {
        //implement business logic here:
        CancelledCompleted paymentCancelled = new CancelledCompleted(this);
        paymentCancelled.setStatus(receiveCancelledCompletedCommand.getStatus());
        paymentCancelled.setReason(receiveCancelledCompletedCommand.getReason());
        paymentCancelled.publishAfterCommit();
    }
}
//>>> DDD / Aggregate Root
