package paymentsystem.infra;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import paymentsystem.config.kafka.KafkaProcessor;
import paymentsystem.domain.*;

@Service
public class PaymentReceiptViewHandler {

    //<<< DDD / CQRS
    @Autowired
    private PaymentReceiptRepository paymentReceiptRepository;
    //>>> DDD / CQRS
}
