package com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.data.jpa.domain;

import com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client.message.ItemResources;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import javax.persistence.EntityManager;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = {ItemEntityDataJpaTest.Configuration_.class})
@Slf4j
class ItemEntityDataJpaTest {

    @EnableAutoConfiguration
    @Configuration
    static class Configuration_ {

    }

    static Stream<ItemEntity> items() {
        return ItemResources.items()
                .map(s -> {
                    final ItemEntity itemEntity = new ItemEntity();
                    BeanUtils.copyProperties(s, itemEntity);
                    return itemEntity;
                });
    }

    @ParameterizedTest
    @MethodSource({"items"})
    void persist_Equals_Find(final ItemEntity itemEntity) {
        entityManager.persist(itemEntity);
        entityManager.flush();
        entityManager.clear();
        final ItemId id = ItemId.builder()
                .locdate(itemEntity.getLocdate())
                .seq(itemEntity.getSeq())
                .build();
        final ItemEntity found = entityManager.find(ItemEntity.class, id);
        assertThat(found).isNotNull().isEqualTo(itemEntity);
    }

    @Autowired
    private EntityManager entityManager;
}