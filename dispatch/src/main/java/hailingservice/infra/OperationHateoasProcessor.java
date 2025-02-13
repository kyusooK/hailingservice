package hailingservice.infra;

import hailingservice.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class OperationHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<Operation>> {

    @Override
    public EntityModel<Operation> process(EntityModel<Operation> model) {
        model.add(
            Link
                .of(model.getRequiredLink("self").getHref() + "/operate")
                .withRel("operate")
        );
        model.add(
            Link
                .of(
                    model.getRequiredLink("self").getHref() +
                    "/completeoperation"
                )
                .withRel("completeoperation")
        );

        return model;
    }
}
