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
    DriverRepository driverRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='DriverMatched'"
    )
    public void wheneverDriverMatched_ServePassengerBoardingLocation(
        @Payload DriverMatched driverMatched
    ) {
        DriverMatched event = driverMatched;
        System.out.println(
            "\n\n##### listener ServePassengerBoardingLocation : " +
            driverMatched +
            "\n\n"
        );

        // Sample Logic //
        Driver.servePassengerBoardingLocation(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='GpsBasedLocationConfirmed'"
    )
    public void wheneverGpsBasedLocationConfirmed_HailDriver(
        @Payload GpsBasedLocationConfirmed gpsBasedLocationConfirmed
    ) {
        GpsBasedLocationConfirmed event = gpsBasedLocationConfirmed;
        System.out.println(
            "\n\n##### listener HailDriver : " +
            gpsBasedLocationConfirmed +
            "\n\n"
        );

        // Sample Logic //
        Driver.hailDriver(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='DestinationCalculated'"
    )
    public void wheneverDestinationCalculated_ServeDestination(
        @Payload DestinationCalculated destinationCalculated
    ) {
        DestinationCalculated event = destinationCalculated;
        System.out.println(
            "\n\n##### listener ServeDestination : " +
            destinationCalculated +
            "\n\n"
        );

        // Sample Logic //
        Driver.serveDestination(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
