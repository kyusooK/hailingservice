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
    UserRepository userRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='DriverMatched'"
    )
    public void wheneverDriverMatched_ServeDriverInfo(
        @Payload DriverMatched driverMatched
    ) {
        DriverMatched event = driverMatched;
        System.out.println(
            "\n\n##### listener ServeDriverInfo : " + driverMatched + "\n\n"
        );

        // Sample Logic //
        User.serveDriverInfo(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
