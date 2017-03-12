package pe.edu.utp.toolsteacherutp.Models;

import java.util.Date;
import java.util.List;

/**
 * Created by elbuenpixel on 11/03/17.
 */

public class DateHorarios implements Comparable<DateHorarios> {
    private String name;
    private Date date;
    private long time;
    private List<Horario> horarios;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public List<Horario> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<Horario> horarios) {
        this.horarios = horarios;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(DateHorarios o) {
        return getDate().compareTo(o.getDate());
    }
}
