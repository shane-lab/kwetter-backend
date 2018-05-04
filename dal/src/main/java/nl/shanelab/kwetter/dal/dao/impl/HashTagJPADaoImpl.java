package nl.shanelab.kwetter.dal.dao.impl;

import nl.shanelab.kwetter.dal.dao.HashTagDao;
import nl.shanelab.kwetter.dal.domain.HashTag;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.dal.qualifiers.JPADao;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

@JPADao
@Stateless
public class HashTagJPADaoImpl extends BaseJPADao<HashTag, Long> implements HashTagDao {

    public HashTag create(HashTag entity) {
        return null;
    }

    @Override
    public HashTag edit(HashTag entity) {
        // dont edit the hashtag. (or maybe alter this hashtag in existing kweets)
        return null;
    }

    @Override
    public void remove(Long id) {
        // TODO dont remove if a kweet with this hashtag exists
//        super.remove(entity);
    }

    public HashTag getByName(String name) {
        Query query = manager.createNamedQuery("HashTag.findByName", User.class);
        query.setParameter("name", name);

        return JPAResult.getSingleResultOrNull(query);
    }

    @Override
    public Collection<HashTag> getTrending(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);

        while (calendar.get(Calendar.DAY_OF_WEEK) > calendar.getFirstDayOfWeek()) {
            calendar.add(Calendar.DATE, -1);
        }

        Date startDate = calendar.getTime();
        calendar.add(Calendar.DATE, 7);
        Date endDate = calendar.getTime();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Query query = manager.createNativeQuery(String.format("SELECT h.* FROM `HASHTAG` h, kweet_hashtag kh WHERE h.id = kh.hashtag_id AND kh.kweet_id IN(SELECT k.id FROM KWEET k WHERE k.createdAt BETWEEN \"%1s\" AND \"%2s\") GROUP BY kh.hashtag_id ORDER BY COUNT(*) DESC, kh.hashtag_id ASC", formatter.format(startDate), formatter.format(endDate)), HashTag.class);
        query.setMaxResults(10);

        return query.getResultList();
    }
}
