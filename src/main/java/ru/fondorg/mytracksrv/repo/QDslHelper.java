package ru.fondorg.mytracksrv.repo;

import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
public class QDslHelper {
    private final EntityManager entityManager;

    private final JPAQueryFactory factory;

    public QDslHelper(EntityManager entityManager) {
        this.entityManager = entityManager;
        factory = new JPAQueryFactory(entityManager);
    }

    Querydsl getQueryDsl(Class<?> entityClass) {
        return new Querydsl(entityManager, new PathBuilderFactory().create(entityClass));
    }

    public JPQLQueryFactory getFactory() {
        return factory;
    }

}
