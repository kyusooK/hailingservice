package hailingservice.infra;

import hailingservice.config.kafka.KafkaProcessor;
import hailingservice.domain.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class OperationRecordViewHandler {

    //<<< DDD / CQRS
    @Autowired
    private OperationRecordRepository operationRecordRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenOperated_then_CREATE_1(@Payload Operated operated) {
        try {
            if (!operated.validate()) return;

            // view 객체 생성
            OperationRecord operationRecord = new OperationRecord();
            // view 객체에 이벤트의 Value 를 set 함
            operationRecord.setDriverId(String.valueOf(operated.getDriverId()));
            operationRecord.setStartingPoint(operated.getPassengerLocation());
            operationRecord.setUserId(String.valueOf(operated.getUserId()));
            // view 레파지 토리에 save
            operationRecordRepository.save(operationRecord);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenOperationCompleted_then_UPDATE_1(
        @Payload OperationCompleted operationCompleted
    ) {
        try {
            if (!operationCompleted.validate()) return;
            // view 객체 조회
            Optional<OperationRecord> operationRecordOptional = operationRecordRepository.findById(
                operationCompleted.getId()
            );

            if (operationRecordOptional.isPresent()) {
                OperationRecord operationRecord = operationRecordOptional.get();
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                operationRecord.setDestination(
                    operationCompleted.getDestination()
                );
                // view 레파지 토리에 save
                operationRecordRepository.save(operationRecord);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //>>> DDD / CQRS
}
