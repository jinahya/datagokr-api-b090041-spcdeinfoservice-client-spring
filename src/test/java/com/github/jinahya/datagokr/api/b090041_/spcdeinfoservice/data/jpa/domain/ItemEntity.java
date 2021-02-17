package com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.data.jpa.domain;

import com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client.message.Item;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(ItemId.class)
@Table(name = "item")
@Slf4j
class ItemEntity extends Item {

}
