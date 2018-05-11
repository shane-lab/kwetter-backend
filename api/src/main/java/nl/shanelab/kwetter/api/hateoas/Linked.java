package nl.shanelab.kwetter.api.hateoas;

import lombok.*;

import java.io.Serializable;

@Builder
@Data
//@NoArgsConstructor
public class Linked implements Serializable {

    String type;

    String title;

    String path;

    String href;

    String rel;

    boolean requiresAuth;
}
