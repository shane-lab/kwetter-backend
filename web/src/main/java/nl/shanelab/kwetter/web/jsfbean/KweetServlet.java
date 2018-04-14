package nl.shanelab.kwetter.web.jsfbean;

import nl.shanelab.kwetter.dal.domain.Kweet;
import nl.shanelab.kwetter.services.KweetingService;
import nl.shanelab.kwetter.services.exceptions.KweetException;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class KweetServlet implements JsfBeanServlet {

    @Inject
    private KweetingService kweetingService;

    public long count() {
        return 0L;
    }

//    public KweetDto getKweetById(long id) {
//        return KweetMapper.INSTANCE.kweetToDto(kweetingService.getKweetById(id));
//    }
//
//    public Collection<KweetDto> getKweets(int page, int size) {
//        return kweetingService.getAllKweets().stream()
//                .map(KweetMapper.INSTANCE::kweetToDto)
//                .collect(Collectors.toSet());
//    }

    @RolesAllowed({"Administrator"})
    public boolean delete(long id) {
        Kweet kweet = kweetingService.getKweetById(id);

        if (kweet == null) {
            return false;
        }

        try {
            kweetingService.removeKweet(kweet);
        } catch (KweetException e) {
            return false;
        }

        return true;
    }
}
