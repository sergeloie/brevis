package ru.anseranser.brevis.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "brevis")
@EntityListeners(AuditingEntityListener.class)
public class Brevis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "source_url")
    private String sourceURL;

    @Column(name = "short_url", unique = true)
    private String shortURL;

    @Basic(fetch = FetchType.LAZY)
    @CreatedDate
    @Column(name = "created_date")
    private Instant createdDate;

}
