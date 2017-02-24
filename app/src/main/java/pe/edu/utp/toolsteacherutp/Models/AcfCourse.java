package pe.edu.utp.toolsteacherutp.Models;

import java.util.List;

/**
 * Created by elbuenpixel on 24/02/17.
 */

public class AcfCourse {
    private List<SingleCourse> requiere;
    private List<SingleCourse> equivalente;

    public List<SingleCourse> getEquivalente() {
        return equivalente;
    }

    public void setEquivalente(List<SingleCourse> equivalente) {
        this.equivalente = equivalente;
    }

    public List<SingleCourse> getRequiere() {

        return requiere;
    }

    public void setRequiere(List<SingleCourse> requiere) {
        this.requiere = requiere;
    }

    public AcfCourse() {
    }

    public AcfCourse(List<SingleCourse> requiere, List<SingleCourse> equivalente) {
        this.requiere = requiere;
        this.equivalente = equivalente;
    }
}
