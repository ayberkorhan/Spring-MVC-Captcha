package tr.edu.duzce.spring.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tr.edu.duzce.spring.model.Captcha;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;


/**
 * @author mahmutcandurak
 */

@Repository
public class CaptchaDao {

    private final SessionFactory sessionFactory;

    public CaptchaDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }


    public boolean saveOrUpdateObject(Object object) {
        boolean success = true;
        try {
            Serializable s = getCurrentSession().save(object);
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }

        return success;
    }

    public boolean removeObject(Object object) {
        boolean success = true;
        try {
            getCurrentSession().remove(object);
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }

        return success;
    }


    public List<Captcha> getAllCaptchaId() {
        Session currentSession = getCurrentSession();
        CriteriaBuilder criteriaBuilder = currentSession.getCriteriaBuilder();
        CriteriaQuery<Captcha> criteriaQuery = criteriaBuilder.createQuery(Captcha.class);
        Root<Captcha> root = criteriaQuery.from(Captcha.class);
        criteriaQuery.select(root);
        Query<Captcha> query = currentSession.createQuery(criteriaQuery);

        return query.getResultList();

    }

    public Captcha getCaptchaById(Long id){
        Session currentSession = getCurrentSession();
        CriteriaBuilder criteriaBuilder = currentSession.getCriteriaBuilder();
        CriteriaQuery<Captcha> criteriaQuery = criteriaBuilder.createQuery(Captcha.class);
        Root<Captcha> root = criteriaQuery.from(Captcha.class);
        Predicate getId = criteriaBuilder.equal(root.get("Id"),id);
        criteriaQuery.select(root).where(getId);

        Query<Captcha> query = currentSession.createQuery(criteriaQuery);
        Captcha captcha = query.getSingleResult();
        return captcha;
    }



}
