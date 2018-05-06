package nl.shanelab.kwetter.dal.qualifiers;


import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER})
/**
 * This Qualifier annotation should be used to annotate Dao implementations which make use of a JPA (e.g. hibernate, eclipselink) data access implementation.
 */
public @interface JPADao {
}
