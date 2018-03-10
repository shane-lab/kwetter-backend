package nl.shanelab.kwetter.dal.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Entity
@EqualsAndHashCode
@NoArgsConstructor
@RequiredArgsConstructor
public class HashTag {

    @Id
    @GeneratedValue
    /**
     * The hashtag identifier
     */
    private long id;

    @NotBlank(message = "The name may not be set as empty")
    @Pattern(regexp = "/[^\\s+]/g", message = "The name may not contain any whitespaces")
    @Column(unique = true)
    @NonNull
    /**
     * The unique name of the hashtag
     */
    private String name;
}
