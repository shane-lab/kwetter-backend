package nl.shanelab.kwetter.api.routers.api.routes;

import nl.shanelab.kwetter.api.dto.HashTagDto;
import nl.shanelab.kwetter.api.routers.BaseRoute;
import nl.shanelab.kwetter.dal.domain.HashTag;
import nl.shanelab.kwetter.services.KweetingService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Set;

@Path("/hashtags")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class HashTagRoute extends BaseRoute {

    @Inject
    KweetingService kweetingService;

    @GET
    @Path("/")
    public Response getAllHashTags() {
        Set<HashTagDto> hashTagDtos = new HashSet<>();

        kweetingService.getAllHashTags().forEach(hashTag -> hashTagDtos.add(mapHashTag(hashTag)));

        return ok(hashTagDtos);
    }

    @GET
    @Path("/{id}")
    public Response getHashTagById(@Valid @PathParam("id") long id) {
        HashTag hashTag = kweetingService.getHashTagById(id);

        HashTagDto hashTagDto = mapHashTag(hashTag);

        return ok(hashTagDto);
    }

    private HashTagDto mapHashTag(HashTag hashTag) {
        HashTagDto hashTagDto = new HashTagDto();
        hashTagDto.setId(hashTag.getId());
        hashTagDto.setName(hashTag.getName());

        return hashTagDto;
    }

}
