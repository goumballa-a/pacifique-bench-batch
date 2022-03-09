package fr.hm.pacifique.bench.config;

import fr.hm.fwk.batch.service.JobParameterService;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Date;

@Component
@Primary
public class JobParamsService implements JobParameterService<File> {

    @Override
    public void addJobParameters(String s, Message<File> message, JobParametersBuilder jobParametersBuilder) {
        jobParametersBuilder.addDate("timestamp", new Date());
    }
}