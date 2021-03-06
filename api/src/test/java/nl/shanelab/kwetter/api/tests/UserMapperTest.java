package nl.shanelab.kwetter.api.tests;

import nl.shanelab.kwetter.api.dto.UserDto;
import nl.shanelab.kwetter.api.mappers.UserMapper;
import nl.shanelab.kwetter.dal.domain.HashTag;
import nl.shanelab.kwetter.dal.domain.Kweet;
import nl.shanelab.kwetter.dal.domain.Role;
import nl.shanelab.kwetter.dal.domain.User;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class UserMapperTest {

    private User testSubject;

    @Before
    public void onBefore() {
        // create iterable user for conducted tests
        testSubject = new User("user", "user", Role.USER);
        testSubject.setId(1);

        // check is MapStruct dependencies are loaded before tests run
        assertWithMessage("The UserMapper interface was not invoked as MapStruct DTO mapper wrapper class")
                .that(UserMapper.INSTANCE)
                .isNotNull();
    }

    @Test
    public void shouldMapUserToDto() {
        testSubject.setBio("MySpace first, Kwetter second");

        // map user to dto
        UserDto dto = UserMapper.INSTANCE.userAsDTO(testSubject);

        assertThat(dto).isNotNull();

        assertWithMessage("The expected username is not 'user'")
                .that(dto.getUsername())
                .isEqualTo(testSubject.getUsername());

        assertWithMessage(String.format("The expected roleId is not of type %s(%d)", Role.USER.toString(), Role.USER.getId()))
                .that(dto.getRoleId())
                .isEqualTo(Role.USER.getId());

        assertWithMessage("The expected biography was not correctly transferred to the dto")
                .that(dto.getBio())
                .isEqualTo(testSubject.getBio());
    }

    @Test
    public void shouldMapUserWithAlteringRolesToDto() {
        testSubject.setRole(Role.MODERATOR);

        UserDto dto = UserMapper.INSTANCE.userAsDTO(testSubject);

        assertWithMessage(String.format("The expected roleId is not of type %s(%d)", Role.MODERATOR.toString(), Role.MODERATOR.getId()))
                .that(dto.getRoleId())
                .isEqualTo(Role.MODERATOR.getId());

        testSubject.setRole(Role.ADMINISTRATOR);

        dto = UserMapper.INSTANCE.userAsDTO(testSubject);

        assertWithMessage(String.format("The expected roleId is not of type %s(%d)", Role.ADMINISTRATOR.toString(), Role.ADMINISTRATOR.getId()))
                .that(dto.getRoleId())
                .isEqualTo(Role.ADMINISTRATOR.getId());
    }

    @Test
    public void shouldMapUserWithKweetsToDto() {
        Kweet kweet = new Kweet("Everyone left @MySpace for this platform?", testSubject);
        kweet.setId(0);
        Kweet kweet1 = new Kweet("WHY IS EVERYONE LEAVING MYSPACE!?", testSubject);
        kweet.setId(1);
        Kweet kweet2 = new Kweet("I wish Kwetter would allow my visitors to hear my music through an automated playlist, like in MySpace. #ThoseGloryDays #MySpaceForever", testSubject);
        kweet.setId(2);

        // append a mention to @local kweet
        User mySpace = new User("MySpace", "myspace", Role.USER);

        final Set<User> mentions = new HashSet<>();
        mentions.add(mySpace);

        kweet.setMentions(mentions);

        // append some hashtags to @local kweet2
        final Set<HashTag> hashTags = new HashSet<>();
        hashTags.add(new HashTag("ThoseGloryDays"));
        hashTags.add(new HashTag("MySpaceForever"));

        kweet2.setHashTags(hashTags);

        // append some kweets to @field testSubject
        final Set<Kweet> kweets = new HashSet<>();
        kweets.add(kweet);
        kweets.add(kweet1);
        kweets.add(kweet2);

        testSubject.setKweets(kweets);

        UserDto dto = UserMapper.INSTANCE.userAsDTO(testSubject);

//        assertWithMessage("No kweets were transferred to the dto after mapping")
//                .that(dto.getKweets())
//                .isNotEmpty();

//        assertWithMessage(String.format("The expected amount of kweets(%d) did not transfer to the dto after mapping", kweets.size()))
//                .that(dto.getKweets())
//                .isEqualTo(kweets.size());
    }

}