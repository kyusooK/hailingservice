package hailingservice.infra;

import hailingservice.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class DriverHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<Driver>> {

    @Override
    public EntityModel<Driver> process(EntityModel<Driver> model) {
        model.add(
            Link
                .of(model.getRequiredLink("self").getHref() + "/confirmlicense")
                .withRel("confirmlicense")
        );
        model.add(
            Link
                .of(
                    model.getRequiredLink("self").getHref() +
                    "/acceptcarhailing"
                )
                .withRel("acceptcarhailing")
        );
        model.add(
            Link
                .of(
                    model.getRequiredLink("self").getHref() +
                    "/changeoperationstatus"
                )
                .withRel("changeoperationstatus")
        );

        return model;
    }
}
