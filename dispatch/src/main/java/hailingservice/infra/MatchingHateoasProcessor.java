package hailingservice.infra;

import hailingservice.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class MatchingHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<Matching>> {

    @Override
    public EntityModel<Matching> process(EntityModel<Matching> model) {
        return model;
    }
}
