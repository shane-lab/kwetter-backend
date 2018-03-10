package nl.shanelab.kwetter.dal.dao;

import nl.shanelab.kwetter.dal.domain.HashTag;

public interface HashTagDao extends GenericDao<HashTag, Long> {

    HashTag getByName(String name);
}
