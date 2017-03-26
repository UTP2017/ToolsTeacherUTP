package pe.edu.utp.toolsteacherutp.Models;

import android.util.Log;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.List;

/**
 * Created by elbuenpixel on 11/03/17.
 */

public class User  extends SugarRecord {
    private String name;
    private String correo;

    @Ignore
    private List<Seccion> secciones;
    private String avatar;
    private String rol;

    @Override
    public long save() {
        if ( getSecciones().size() > 0 ){
            for ( Seccion seccion : getSecciones()) {
                seccion.save();
            }
        }
        return super.save();
    }

    public User(){
    }

    public User(String name, String correo, String avatar, String rol, List<Seccion> secciones){
        this.name = name;
        this.correo = correo;
        this.avatar = avatar;
        this.rol = rol;
        this.secciones = secciones;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public List<Seccion> getSecciones() {
        return secciones;
    }

    public List<Seccion> getSeccionesList() {
        return Seccion.find(Seccion.class, "user = ?", getId() + "" );
    }

    public void setSecciones(List<Seccion> secciones) {
        this.secciones = secciones;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
