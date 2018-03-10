package nl.shanelab.kwetter.dal.dao.impl;

import nl.shanelab.kwetter.dal.dao.HashTagDao;
import nl.shanelab.kwetter.dal.domain.HashTag;
import nl.shanelab.kwetter.dal.qualifiers.JPADao;

import javax.ejb.Stateless;
import java.util.Collection;

@JPADao
@Stateless
public class HashTagJPADaoImpl implements HashTagDao {

    public int count() {
        return 0;
    }

    public HashTag create(HashTag hashTag) {
        return null;
    }

    public HashTag edit(HashTag hashTag) {
        return null;
    }

    public HashTag find(Long id) {
        return null;
    }

    public Collection<HashTag> findAll() {
        return null;
    }

    public void remove(HashTag hashTag) {

    }

    public HashTag getByName(String name) {
        return null;
    }
}
