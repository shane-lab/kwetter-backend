package nl.shanelab.kwetter.api.routers.api.routes;

import lombok.Getter;
import nl.shanelab.kwetter.api.BaseRoute;
import nl.shanelab.kwetter.api.dto.HashTagDto;
import nl.shanelab.kwetter.api.hateoas.routelinks.HashTagRouteLinks;
import nl.shanelab.kwetter.dal.dao.Pagination;
import nl.shanelab.kwetter.dal.domain.HashTag;
import nl.shanelab.kwetter.services.KweetingService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

@Path("/hashtags")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class HashTagRoute extends BaseRoute {

    @Inject
    KweetingService kweetingService;

    @GET
    @Path(HashTagRouteLinks.Constants.LIST_HASHTAGS)
    public Response getAllHashTags(@QueryParam("page") int page, @QueryParam("size") int size) {
        Pagination<HashTag> pagination = kweetingService.getAllHashTags(page, size);

        return paginated(
                pagination.getPage(),
                pagination.getRequestedSize(),
                pagination.pages(),
                pagination.hasPrevious(),
                pagination.hasNext(), pagination.getCollection().stream()
                .map(this::mapHashTagWithLinks)
                .collect(Collectors.toSet()));
    }

    @GET
    @Path(HashTagRouteLinks.Constants.FETCH_HASHTAG)
    public Response getHashTagById(@Valid @PathParam("id") long id) {
        HashTag hashTag = kweetingService.getHashTagById(id);

        HashTagDto hashTagDto = mapHashTag(hashTag);

        return ok(hashTagDto);
    }

    @GET
    @Path(HashTagRouteLinks.Constants.FETCH_TRENDING)
    public Response getTrendingHashTags(@Valid @PathParam("date") DateParam date) {

        return ok(kweetingService.getTrendingHashTags(date.getDate()).stream()
                .map(this::mapHashTagWithLinks)
                .collect(Collectors.toSet()));
    }

    private HashTagDto mapHashTag(HashTag hashTag) {
        HashTagDto hashTagDto = new HashTagDto();
        hashTagDto.setId(hashTag.getId());
        hashTagDto.setName(hashTag.getName());

        return hashTagDto;
    }

    private HashTagDto mapHashTagWithLinks(HashTag hashTag) {
        HashTagDto dto = mapHashTag(hashTag);
        dto.add(HashTagRouteLinks.FETCH_HASHTAG.asLinked(uriInfo.getBaseUri().toString(), hashTag.getId()));

        return dto;
    }

    @Getter
    public static class DateParam {
        private final Date date;

        public DateParam(@NotBlank(message = "No date time was given") String date) {
            final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                this.date = dateFormat.parse(date);
            } catch (ParseException e) {
                throw new WebApplicationException(String.format("The given date '%s' does not satisfy the expected date format", date));
            }
        }

    }

}
