package com.haphollys.booook.batch

import com.haphollys.booook.domains.book.BookEntity
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.database.JpaCursorItemReader
import org.springframework.batch.item.database.JpaItemWriter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.time.LocalDateTime
import javax.persistence.EntityManagerFactory


@Profile("deploy")
@Configuration
class RemoveUnPaidBookJob(
    private val jobBuilder: JobBuilderFactory,
    private val stepBuilder: StepBuilderFactory,
    private val emf: EntityManagerFactory,
) {

    @Bean
    fun testJob(): Job {
        return jobBuilder["testJob"]
            .start(testStep())
            .build()
    }

    @Bean
    fun testStep(): Step {
        return stepBuilder["testStep"]
            .chunk<BookEntity, BookEntity>(CHUNK_SIZE)
            .reader(jpaReader())
            .processor(processor())
            .writer(writer())
            .build()
    }

    @Bean
    @StepScope
    fun jpaReader(): JpaCursorItemReader<BookEntity> {
        val reader = JpaCursorItemReader<BookEntity>()
        reader.setQueryString("select b from BookEntity b where status = :status and bookedAt < :deadline")
        reader.setParameterValues(getQueryParams())
        reader.setEntityManagerFactory(emf)

        return reader
    }

    private fun getQueryParams(): MutableMap<String, Any> {
        val paramMap: MutableMap<String, Any> = HashMap()
        paramMap["status"] = BookEntity.BookStatus.BOOKED
        paramMap["deadline"] = LocalDateTime.now().minusMinutes(5)
        return paramMap
    }

    @Bean
    fun processor(): ItemProcessor<BookEntity, BookEntity> {
        // 예약한지 5분지났는지 검증 후 CANCEL 상태로
        return ItemProcessor<BookEntity, BookEntity> {
            println("PROCESSOR : ${it}")
            it.status = BookEntity.BookStatus.CANCEL
            it
        }
    }

    @Bean
    fun writer(): JpaItemWriter<BookEntity> {
        val itemWriter = JpaItemWriter<BookEntity>()
        itemWriter.setEntityManagerFactory(emf)
        return itemWriter
    }

    companion object {
        const val CHUNK_SIZE = 2
    }
}