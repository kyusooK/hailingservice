package hailingservice.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import hailingservice.config.kafka.KafkaProcessor;
import hailingservice.domain.*;
import javax.naming.NameParser;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    OperationRepository operationRepository;

    @Autowired
    MatchingRepository matchingRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='HailingAccepted'"
    )
    public void wheneverHailingAccepted_MatchDriver(
        @Payload HailingAccepted hailingAccepted
    ) {
        HailingAccepted event = hailingAccepted;
        System.out.println(
            "\n\n##### listener MatchDriver : " + hailingAccepted + "\n\n"
        );

        // Sample Logic //
        Matching.matchDriver(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='Operated'"
    )
    public void wheneverOperated_CalculateDestination(
        @Payload Operated operated
    ) {
        Operated event = operated;
        System.out.println(
            "\n\n##### listener CalculateDestination : " + operated + "\n\n"
        );

        // Sample Logic //
        Matching.calculateDestination(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='CarHailing'"
    )
    public void wheneverCarHailing_ConfirmGpsBasedLocation(
        @Payload CarHailing carHailing
    ) {
        CarHailing event = carHailing;
        System.out.println(
            "\n\n##### listener ConfirmGpsBasedLocation : " +
            carHailing +
            "\n\n"
        );

        // Sample Logic //
        Matching.confirmGpsBasedLocation(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='HailingAccepted'"
    )
    public void wheneverHailingAccepted_RegisterDriver(
        @Payload HailingAccepted hailingAccepted
    ) {
        HailingAccepted event = hailingAccepted;
        System.out.println(
            "\n\n##### listener RegisterDriver : " + hailingAccepted + "\n\n"
        );

        // Sample Logic //
        Operation.registerDriver(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='RequestPaymentCompleted'"
    )
    public void wheneverRequestPaymentCompleted_UpdatePaymentInfo(
        @Payload RequestPaymentCompleted requestPaymentCompleted
    ) {
        RequestPaymentCompleted event = requestPaymentCompleted;
        System.out.println(
            "\n\n##### listener UpdatePaymentInfo : " +
            requestPaymentCompleted +
            "\n\n"
        );

        // Sample Logic //
        Operation.updatePaymentInfo(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
