package nl.shanelab.kwetter.api.tests;

import nl.shanelab.kwetter.api.dto.HashTagDto;
import nl.shanelab.kwetter.api.dto.KweetDto;
import nl.shanelab.kwetter.api.mappers.KweetMapper;
import nl.shanelab.kwetter.dal.domain.HashTag;
import nl.shanelab.kwetter.dal.domain.Kweet;
import nl.shanelab.kwetter.dal.domain.Role;
import nl.shanelab.kwetter.dal.domain.User;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class KweetMapperTest {

    private Kweet testSubject;

    @Before
    public void onBefore() {
        User user = new User("user", "user", Role.USER);
        user.setId(1);

        // create iterable user for conducted tests
        testSubject = new Kweet("Some Kweet", user);
        testSubject.setId(1);

        // check is MapStruct dependencies are loaded before tests run
        assertWithMessage("The KweetMapper interface was not invoked as MapStruct DTO mapper wrapper class")
                .that(KweetMapper.INSTANCE)
                .isNotNull();
    }

    @Test
    public void shouldMapKweetToDto() {

        // map Kweet to dto
        KweetDto dto = KweetMapper.INSTANCE.kweetToDto(testSubject);

        assertThat(dto).isNotNull();

        assertWithMessage("The expected Kweet message on the dto is not 'Some Kweet'")
                .that(dto.getMessage())
                .isEqualTo(testSubject.getMessage());

        assertWithMessage("The expected author name on the dto differs from the Kweet object")
                .that(dto.getAuthor())
                .isEqualTo(testSubject.getAuthor().getUsername());
    }

    @Test
    public void shouldMapKweetWithHashTagsToDto() {
        final Set<HashTag> hashTags = new HashSet<>();
        HashTag hashTag = new HashTag("abc");
        hashTag.setId(hashTags.size());
        hashTags.add(hashTag);
        HashTag hashTag1 = new HashTag("def");
        hashTag1.setId(hashTags.size());
        hashTags.add(hashTag1);
        HashTag hashTag2 = new HashTag("ghi");
        hashTag2.setId(hashTags.size());
        hashTags.add(hashTag2);

        // append some hashtags to @field testSubject
        testSubject.setHashTags(hashTags);

        KweetDto dto = KweetMapper.INSTANCE.kweetToDto(testSubject);

        assertWithMessage("No hashtags were transferred to the dto after mapping")
                .that(dto.getHashTags())
                .isNotEmpty();

        assertWithMessage(String.format("The expected amount of hashtags(%d) did not transfer to the dto after mapping", hashTags.size()))
                .that(dto.getHashTags().size())
                .isEqualTo(hashTags.size());

        Iterator<HashTag> hashTagIterator = hashTags.iterator();

        for (HashTagDto hashTagDto : dto.getHashTags()) {
            HashTag hashTagNext = hashTagIterator.next();

            assertWithMessage(String.format("The expected hashtag '%s' differs from the actual kweet", hashTagNext.getName()))
                    .that(hashTagDto.getName())
                    .isEqualTo(hashTagNext.getName());
        }

        testSubject.setHashTags(null);
    }

    @Test
    public void shouldMapKweetWithMentionsToDto() {
        final Set<User> users = new HashSet<>();
        User user = new User("user1", "password", Role.USER);
        user.setId(testSubject.getId()+1);
        users.add(user);
        User user1 = new User("user2", "password", Role.MODERATOR);
        user1.setId(user.getId()+1);
        users.add(user1);
        User user2 = new User("user3", "password", Role.ADMINISTRATOR);
        user2.setId(user1.getId()+1);
        users.add(user2);

        // append some mentions to the @field testSubject
        testSubject.setMentions(users);

        KweetDto dto = KweetMapper.INSTANCE.kweetToDto(testSubject);

        assertWithMessage("No mentions were transferred to the dto after mapping")
                .that(dto.getMentions())
                .isNotEmpty();

        assertWithMessage(String.format("The expected amount of mentions(%s) did not transfer to the dto after mapping", users.size()))
                .that(dto.getMentions().size())
                .isEqualTo(users.size());

        Iterator<User> userIterator = users.iterator();

        for (String mentioned : dto.getMentions()) {
            User userNext = userIterator.next();

            assertWithMessage(String.format("The expected username '%s' differs from the actual kweet", userNext.getUsername()))
                    .that(mentioned)
                    .isEqualTo(userNext.getUsername());
        }

        testSubject.setMentions(null);
    }
}
