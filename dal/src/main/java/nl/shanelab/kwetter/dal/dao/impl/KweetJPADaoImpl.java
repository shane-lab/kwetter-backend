package nl.shanelab.kwetter.dal.dao.impl;

import nl.shanelab.kwetter.dal.dao.KweetDao;
import nl.shanelab.kwetter.dal.domain.Kweet;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.dal.qualifiers.JPADao;

import javax.ejb.Stateless;
import java.util.Collection;

@JPADao
@Stateless
public class KweetJPADaoImpl implements KweetDao {

    public int count() {
        return 0;
    }

    public Kweet create(Kweet kweet) {
        return null;
    }

    public Kweet edit(Kweet kweet) {
        return null;
    }

    public Kweet find(Long id) {
        return null;
    }

    public Collection<Kweet> findAll() {
        return null;
    }

    public void remove(Kweet kweet) {

    }

    public Collection<Kweet> getByUserName(String name) {
        return null;
    }

    public Collection<Kweet> getByUserId(Long id) {
        return null;
    }

    public Collection<Kweet> getByHashTagName(String name) {
        return null;
    }

    public Collection<Kweet> getByHashTagId(Long id) {
        return null;
    }

    public Collection<Kweet> getByMention(String name) {
        return null;
    }

    public Collection<Kweet> getByFavoritedBy(String name) {
        return null;
    }

    public boolean isFavoritedBy(Kweet kweet, User user) {
        return false;
    }

    public boolean isMentionedIn(Kweet kweet, User user) {
        return false;
    }

    public void favourite(Kweet kweet, User user) {

    }

    public void unFavourite(Kweet kweet, User user) {

    }
}
