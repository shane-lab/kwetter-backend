package nl.shanelab.kwetter.dal.dao;

import nl.shanelab.kwetter.dal.domain.Kweet;

import java.util.Collection;

public interface KweetDao extends GenericDao<Kweet, Long> {

    Collection<Kweet> getByUserName(String name);

    Collection<Kweet> getByUserId(Long id);

    // shouldn't the below be in the service layer?

    Collection<Kweet> getByHashTagName(String name);

    Collection<Kweet> getByHashTagId(Long id);

    Collection<Kweet> getByMention(String name);
}
