package com.plobin.sandbox.Config

import com.plobin.sandbox.util.DataSeeding.SeedTemplates.Util as SeedTemplatesUtil
import com.plobin.sandbox.util.DataSeeding.SeedSandboxes.Util as SeedSandboxesUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class DataSeeder(
    private val seedTemplatesUtil: SeedTemplatesUtil,
    private val seedSandboxesUtil: SeedSandboxesUtil
) : CommandLineRunner {

    @Value("\${app.data-seeding.enabled:true}")
    private var seedingEnabled: Boolean = true

    override fun run(vararg args: String?) {
        if (!seedingEnabled) {
            println("Data seeding is disabled")
            return
        }

        println("Starting data seeding...")

        // 1. 템플릿 시딩
        seedTemplatesUtil()

        // 2. 샌드박스 시딩
        seedSandboxesUtil()

        println("Data seeding completed")
    }
}