package com.datazuul.examples.jpa;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Test;

import com.datazuul.examples.jpa.domain.Address;
import com.datazuul.examples.jpa.domain.Company;
import com.datazuul.examples.jpa.domain.Person;

public class CompanyTest extends Base {
    @Test
    public void createCompany() {
        final Company c1 = new Company();
        c1.setName("The Company");
        c1.setAddress(new Address("D Rd.", "", "Paris", "TX", "77382"));

        em.getTransaction().begin();
        em.persist(c1);
        em.getTransaction().commit();

        final Company foundCompany = findCompanyNamed(em, "The Company");

        assertEquals("D Rd.", foundCompany.getAddress().getStreetAddress1());
        // Note, we do not need an assert. Why? the method getSingleResult()
        // will throw an exception if there is not exactly one
        // object found. We'll research that in the second JPA tutorial.
    }

    @SuppressWarnings("unchecked")
    @Test
    public void createCompanyAndHirePeople() {
        createCompanyWithTwoEmployees();

        final List<Person> list = em.createQuery("select p from Person p").getResultList();
        assertEquals(2, list.size());

        final Company foundCompany = (Company) em.createQuery("select c from Company c where c.name=?1").setParameter(
            1,
            "The Company").getSingleResult();
        assertEquals(2, foundCompany.getEmployees().size());
    }

    @Test
    public void hireAndFire() {
        final Company c1 = createCompanyWithTwoEmployees();
        final List<Person> people = PersonTest.generatePersonObjects();

        em.getTransaction().begin();
        for (final Person p : people) {
            c1.fire(p);
        }
        em.persist(c1);
        em.getTransaction().commit();

        final Company foundCompany = findCompanyNamed(em, "The Company");
        assertEquals(0, foundCompany.getEmployees().size());
    }

    private Company findCompanyNamed(final EntityManager em, final String name) {
        return (Company) em.createQuery("select c from Company c where c.name=?1").setParameter(1, name).getSingleResult();
    }

    private Company createCompanyWithTwoEmployees() {
        final Company c1 = new Company();
        c1.setName("The Company");
        c1.setAddress(new Address("D Rd.", "", "Paris", "TX", "77382"));

        final List<Person> people = PersonTest.generatePersonObjects();
        for (final Person p : people) {
            c1.hire(p);
        }

        em.getTransaction().begin();
        for (final Person p : people) {
            em.persist(p);
        }
        em.persist(c1);
        em.getTransaction().commit();

        return c1;
    }
}