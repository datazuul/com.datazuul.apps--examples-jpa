package com.datazuul.examples.jpa.domain;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * A Company that has a 1 to many relationship with People,
 * who are its employees.
 */
@Entity
public class Company {
    @Id
    @GeneratedValue
    int id;

    private String name;
    @Embedded
    private Address address;

    /**
     * Adding mappedBy lets the entity manager know you mean for this
     * relationship to be bi-directional rather that two unidirectional
     * relationships.
     */
    @OneToMany(mappedBy = "job")
    private Collection<Person> employees;

    public Company() {
    }

    public Company(final String name, final Address address, final Collection<Person> employees) {
        setName(name);
        setAddress(address);
        setEmployees(employees);
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(final Address address) {
        this.address = address;
    }

    public Collection<Person> getEmployees() {
        if (employees == null) {
            employees = new ArrayList<Person>();
        }
        return employees;
    }

    public void setEmployees(final Collection<Person> newStaff) {
        // fire everybody
        final Collection<Person> clone = new ArrayList<Person>(employees);

        for (final Person p : clone) {
            fire(p);
        }

        for (final Person p : newStaff) {
            hire(p);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void hire(final Person p) {
        getEmployees().add(p);
        p.setJob(this);
    }

    public void fire(final Person p) {
        getEmployees().remove(p);
        p.setJob(null);
    }
}
