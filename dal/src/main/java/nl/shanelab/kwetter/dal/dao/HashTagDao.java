package nl.shanelab.kwetter.dal.dao;

import nl.shanelab.kwetter.dal.domain.HashTag;

import java.util.Collection;
import java.util.Date;

public interface HashTagDao extends GenericDao<HashTag, Long> {

    HashTag getByName(String name);

    Collection<HashTag> getTrending(Date date);
}
