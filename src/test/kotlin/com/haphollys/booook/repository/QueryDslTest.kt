package com.haphollys.booook.repository

import com.haphollys.booook.domains.movie.MovieEntity
import com.haphollys.booook.domains.movie.QMovieEntity
import com.haphollys.booook.domains.movie.QMovieEntity.movieEntity
import com.querydsl.jpa.impl.JPAQueryFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@SpringBootTest
@Transactional
class QueryDslTest {
    @Autowired
    lateinit var em: EntityManager

    @Autowired
    lateinit var query: JPAQueryFactory

    val testMovie = MovieEntity(name = "TEST_MOVIE")

    @BeforeEach
    fun setup() {
        em.persist(testMovie)
    }

    @Test
    fun `movie 조회`() {
        // when
        val result = query.select(movieEntity)
            .from(movieEntity)
            .fetch()

        // then
        assertEquals(testMovie.name, result[0].name)
    }

    @Test
    fun `movie 튜플 조회`() {
        val result = query.select(movieEntity.id, movieEntity.name)
            .from(movieEntity)
            .fetch();

        for (r in result) {
            println("id : ${r.get(movieEntity.id)}")
            println("name : ${r.get(movieEntity.name)}")
        }
    }


}