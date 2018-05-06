package nl.shanelab.kwetter.services.util;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.shanelab.kwetter.dal.dao.UserDao;
import nl.shanelab.kwetter.dal.domain.User;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserFollowHelper {

    @NonNull
    private UserDao dao;

    public static UserFollowHelper with(UserDao dao) {
        return new UserFollowHelper(dao);
    }

    public SetFollowState make(User user) {
        return new SetFollowState(dao, user);
    }

    public CheckFollowState is(User user) {
        return new CheckFollowState(dao, user);
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class SetFollowState {

        @NonNull
        private UserDao dao;

        @NonNull
        private User user;

        public void follow(User b) {
            dao.createFollow(user, b);
        }

        public void unFollow(User b) {
            dao.unFollow(user, b);
        }
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CheckFollowState {

        @NonNull
        private UserDao dao;

        @NonNull
        private User user;

        public boolean following(User b) {
            return dao.isFollowing(user, b);
        }

        public boolean followedBy(User b) {
            return dao.isFollowedBy(user, b);
        }
    }

}
