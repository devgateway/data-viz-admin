package org.devgateway.toolkit.persistence.excel.test;

import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author idobre
 * @since 26/03/2018
 *
 * Mock for {@link BaseJpaRepository} interface.
 */
public class TestAddressRepository implements BaseJpaRepository<TestAddress, Long> {

    @Override
    public Optional<TestAddress> findById(Long aLong) {
        return Optional.of(new TestAddress("Street 1", "Romania"));
    }

    @Override
    public <S extends TestAddress> S save(S entity) {
        return null;
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List findAll() {
        return null;
    }

    @Override
    public List findAll(Sort sort) {
        return null;
    }

    @Override
    public Page findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List findAllById(Iterable iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(TestAddress entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void flush() {

    }

    @Override
    public void deleteInBatch(Iterable entities) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public TestAddress getOne(Long aLong) {
        return null;
    }

    @Override
    public TestAddress getById(Long aLong) {
        return null;
    }

    @Override
    public TestAddress getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public List findAll(Example example, Sort sort) {
        return null;
    }

    @Override
    public List findAll(Example example) {
        return null;
    }

    @Override
    public <S extends TestAddress> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends TestAddress> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<TestAddress> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public List saveAll(Iterable entities) {
        return null;
    }

    @Override
    public Optional findOne(Specification spec) {
        return Optional.empty();
    }

    @Override
    public List findAll(Specification spec) {
        return null;
    }

    @Override
    public Page findAll(Specification spec, Pageable pageable) {
        return null;
    }

    @Override
    public List findAll(Specification spec, Sort sort) {
        return null;
    }

    @Override
    public long count(Specification spec) {
        return 0;
    }

    @Override
    public Optional findOne(Example example) {
        return Optional.empty();
    }

    @Override
    public Page findAll(Example example, Pageable pageable) {
        return null;
    }

    @Override
    public long count(Example example) {
        return 0;
    }

    @Override
    public boolean exists(Example example) {
        return false;
    }

    @Override
    public boolean exists(Specification<TestAddress> spec) {
        return false;
    }

    @Override
    public long delete(Specification<TestAddress> spec) {
        return 0;
    }

    @Override
    public <S extends TestAddress, R> R findBy(Specification<TestAddress> spec, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends TestAddress, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
