package fr.hm.pacifique.bench.projection.common;

import fr.hm.pacifique.bench.constant.BatchConstant;
import fr.hm.pacifique.bench.enums.ContextPeriod;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Context {
    private ContextPeriod contextPeriod;
    private int year;

    public Context() {
        this.year = BatchConstant.START_YEAR;
        this.contextPeriod = ContextPeriod.parse(BatchConstant.START_PERIOD);
    }

    public Context(LocalDateTime localDateTime, String period) {
        this.year = localDateTime.getYear();
        this.contextPeriod = ContextPeriod.parse(period);
    }

    public Context(int year, String period) {
        this.year = year;
        this.contextPeriod = ContextPeriod.parse(period);
    }

    public Context(String context) {
        this.contextPeriod = ContextPeriod.parse(context.substring(4, 6));
        this.year = Integer.parseInt(context.substring(0, 4));
    }

    public Context increase() {
        Context result = new Context(year, contextPeriod.getLabel());
        switch (contextPeriod) {
            case Q1:
                result.setContextPeriod(ContextPeriod.Q2);
                break;
            case Q2:
                result.setContextPeriod(ContextPeriod.Q3);
                break;
            case Q3:
                result.setContextPeriod(ContextPeriod.Q4);
                break;
            case Q4:
                result.setContextPeriod(ContextPeriod.Q1);
                result.setYear(year + 1);
                break;
        }
        return result;
    }

    public String getDirectory() {
        return String.valueOf(year).concat(contextPeriod.getLabel());
    }

    public LocalDateTime getDate() {
        return LocalDateTime.now().withYear(year).withMonth(Integer.parseInt(contextPeriod.getMonth()));
    }

    @Override
    public String toString() {
        return String.valueOf(year).concat(String.valueOf(contextPeriod.getMonth())).concat("01");
    }
}