package nl.shanelab.kwetter.web.jsfbean;

import nl.shanelab.kwetter.dal.domain.Role;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.services.UserService;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class UserServlet implements JsfBeanServlet {

    @Inject
    private UserService userService;

    public long count() {
        return 0L;
    }

//    public UserDto getUserById(long id) {
//        return UserMapper.INSTANCE.userAsDTO(userService.getById(id));
//    }
//
//    public Collection<UserDto> getUsers(int page, int size) {
//        return userService.getAllUsers().stream()
//                .map(UserMapper.INSTANCE::userAsDTO)
//                .collect(Collectors.toSet());
//    }

    @RolesAllowed({"Administrator"})
    public int setRole(long id, int current, int requested) {
        User user  = userService.getById(id);

        if (user == null) {
            return current;
        }

        try {
            user.setRole(Role.findById(requested));
        } catch (Exception e) {
            return user.getRole().getId();
        }

        return requested;
    }
}
