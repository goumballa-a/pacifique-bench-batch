#!/usr/bin/groovy

mavenNode(['node_qualifier': 'SpringBoot']) {
    email = 'ab0f81e6.harmoniemutuelle.onmicrosoft.com@emea.teams.ms'
    mavenBoot {
        gcl_folder = "/DEV_DPA/cycledev/tags"
        deployment = true
        deployment_target = 'sba37int1'
        service_name = 'int1_pacifique-bench_21009.service'
    }
}