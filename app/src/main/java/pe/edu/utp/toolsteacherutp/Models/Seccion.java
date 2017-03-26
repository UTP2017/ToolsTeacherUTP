package pe.edu.utp.toolsteacherutp.Models;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.List;

/**
 * Created by elbuenpixel on 11/03/17.
 */
public class Seccion extends SugarRecord {
    private String post_content;
    private String post_title;
    private String curso;
    private String aula;
    private String sede;
    private String periodo;
    private String inicio;
    private String fin;
    @Ignore
    private List<Horario> horarios;
    private Long user;


    @Override
    public long save() {
        if ( getHorarios().size() > 0 ){
            for ( Horario horario: getHorarios()) {
                horario.save();
            }
        }
        return super.save();
    }


    public Seccion() {
    }

    public Seccion(String post_content, String post_title, String curso, String periodo, String inicio, String fin, List<Horario> horarios, Long user, String aula, String sede) {
        this.post_content = post_content;
        this.post_title = post_title;
        this.curso = curso;
        this.periodo = periodo;
        this.inicio = inicio;
        this.fin = fin;
        this.user = user;
        this.aula = aula;
        this.sede= sede;
        this.horarios = horarios;
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

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getFin() {
        return fin;
    }

    public void setFin(String fin) {
        this.fin = fin;
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

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public List<Horario> getHorarios() {
        return horarios;
    }

    public List<Horario> getHorariosList() {
        return Horario.find(Horario.class, "seccionID = ?", getId() + "" );
    }

    public void setHorarios(List<Horario> horarios) {
        this.horarios = horarios;
    }
}
