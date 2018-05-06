package nl.shanelab.kwetter.dal.domain;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@EqualsAndHashCode
@NoArgsConstructor
@RequiredArgsConstructor
@NamedQueries({
        @NamedQuery(name = "HashTag.findByName", query = "SELECT DISTINCT h FROM HashTag h WHERE h.name = :name"),
//        @NamedQuery(name = "HashTag.findTrendingByWeek", query = "SELECT h FROM HashTag h WHERE h.id IN(SELECT k.hashTags FROM Kweet k WHERE k.createdAt BETWEEN :startDate AND :endDate)")
})
public class HashTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /**
     * The hashtag identifier
     */
    private long id;

//    @NotBlank(message = "The name may not be set as empty")
//    @Pattern(regexp = "/[^\\s+]/g", message = "The name may not contain any whitespaces")
    @Column(unique = true, nullable = false)
    @NonNull
    /**
     * The unique name of the hashtag
     */
    private String name;
}
