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

    private ContextPeriod contextDeb;
    private ContextPeriod contextFin;
    private Integer yearDeb;
    private Integer yearFin;
    private String societe;

    public Context(Integer yearDeb,Integer yearFin,String contextDeb,String contextFin,String societe) {
        this.yearDeb = BatchConstant.START_YEAR;
        this.yearFin= BatchConstant.START_YEAR;
        this.contextDeb = ContextPeriod.parse(BatchConstant.START_PERIOD);
        this.contextFin = ContextPeriod.parse(BatchConstant.START_PERIOD);
        this.societe = societe;
    }

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

    public Context increaseTrim() {
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
                result.setContextPeriod(ContextPeriod.AN);
                break;
            case AN:
                result.setContextPeriod(ContextPeriod.Q1);
                result.setYear(year + 1);
                break;
        }
        return result;
    }

    public Context increase() {
        Context result = new Context(year, contextPeriod.getLabel());
        switch (contextPeriod) {
            case M1:
                result.setContextPeriod(ContextPeriod.M2);
                break;
            case M2:
                result.setContextPeriod(ContextPeriod.M3);
                break;
            case M3:
                result.setContextPeriod(ContextPeriod.M4);
                break;
            case M4:
                result.setContextPeriod(ContextPeriod.M5);
                break;
            case M5:
                result.setContextPeriod(ContextPeriod.M6);
                break;
            case M6:
                result.setContextPeriod(ContextPeriod.M7);
                break;
            case M7:
                result.setContextPeriod(ContextPeriod.M8);
                break;
            case M8:
                result.setContextPeriod(ContextPeriod.M9);
                break;
            case M9:
                result.setContextPeriod(ContextPeriod.M10);
                break;
            case M10:
                result.setContextPeriod(ContextPeriod.M11);
                break;
            case M11:
                result.setContextPeriod(ContextPeriod.M12);
                break;
            case M12:
                result.setContextPeriod(ContextPeriod.M1);
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