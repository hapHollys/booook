package com.haphollys.booook.batch

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile


@Profile("deploy")
@Configuration
@EnableBatchProcessing
class BatchConfig {
}