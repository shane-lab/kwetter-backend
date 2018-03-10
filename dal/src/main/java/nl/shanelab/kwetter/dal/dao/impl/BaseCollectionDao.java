package nl.shanelab.kwetter.dal.dao.impl;

import nl.shanelab.kwetter.dal.ejb.DummyData;

import javax.ejb.EJB;

public abstract class BaseCollectionDao {

    @EJB
    protected DummyData data;
}
