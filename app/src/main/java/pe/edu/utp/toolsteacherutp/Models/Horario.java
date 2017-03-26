package pe.edu.utp.toolsteacherutp.Models;

import com.orm.SugarRecord;

/**
 * Created by elbuenpixel on 11/03/17.
 */
public class Horario extends SugarRecord {
    private String post_content;
    private String post_title;
    private String hora_inicio;
    private String hora_fin;
    private String aula;
    private String sede;
    private String curso;
    private String seccion;
    private boolean lunes;
    private boolean martes;
    private boolean miercoles;
    private boolean jueves;
    private boolean viernes;
    private boolean sabado;
    private boolean domingo;
    private Long seccion_id;
    private Long horario_id;

    public Horario() {
    }

    public Horario(String post_content, String post_title, String hora_inicio, String hora_fin, String aula, String sede, String curso, String seccion, boolean lunes, boolean martes, boolean miercoles, boolean jueves, boolean viernes, boolean sabado, boolean domingo, Long seccion_id, Long horario_id) {
        this.post_content = post_content;
        this.post_title = post_title;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
        this.aula = aula;
        this.sede = sede;
        this.curso = curso;
        this.seccion = seccion;
        this.lunes = lunes;
        this.martes = martes;
        this.miercoles = miercoles;
        this.jueves = jueves;
        this.viernes = viernes;
        this.sabado = sabado;
        this.domingo = domingo;
        this.seccion_id = seccion_id;
        this.horario_id = horario_id;
    }

    public Long getSeccion_id() {
        return seccion_id;
    }

    public void setSeccion_id(Long seccion_id) {
        this.seccion_id = seccion_id;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(String hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public String getHora_fin() {
        return hora_fin;
    }

    public void setHora_fin(String hora_fin) {
        this.hora_fin = hora_fin;
    }

    public boolean isLunes() {
        return lunes;
    }

    public void setLunes(boolean lunes) {
        this.lunes = lunes;
    }

    public boolean isMartes() {
        return martes;
    }

    public void setMartes(boolean martes) {
        this.martes = martes;
    }

    public boolean isMiercoles() {
        return miercoles;
    }

    public void setMiercoles(boolean miercoles) {
        this.miercoles = miercoles;
    }

    public boolean isJueves() {
        return jueves;
    }

    public void setJueves(boolean jueves) {
        this.jueves = jueves;
    }

    public boolean isViernes() {
        return viernes;
    }

    public void setViernes(boolean viernes) {
        this.viernes = viernes;
    }

    public boolean isSabado() {
        return sabado;
    }

    public void setSabado(boolean sabado) {
        this.sabado = sabado;
    }

    public boolean isDomingo() {
        return domingo;
    }

    public void setDomingo(boolean domingo) {
        this.domingo = domingo;
    }
}
