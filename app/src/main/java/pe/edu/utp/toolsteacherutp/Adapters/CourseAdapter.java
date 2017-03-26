package pe.edu.utp.toolsteacherutp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pe.edu.utp.toolsteacherutp.Models.Seccion;
import pe.edu.utp.toolsteacherutp.R;

/**
 * Created by elbuenpixel on 23/03/17.
 */

public class CourseAdapter  extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {
    private List<Seccion> secciones;
    private Context mContext;

    public CourseAdapter(List<Seccion> secciones, Context mContext) {
        this.secciones = secciones;
        this.mContext=mContext;
    }

    public void updateData(List<Seccion> _secciones) {
        secciones.clear();
        secciones.addAll(_secciones);
        notifyDataSetChanged();
    }

    public Seccion getItem(int position) {
        return secciones.get(position);
    }

    public void removeItem(int position) {
        secciones.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public CourseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.seccion_layout, parent, false);
        return new CourseAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CourseAdapter.ViewHolder holder, int position) {
        holder.courseNameTextView.setText( this.secciones.get(position).getCurso() );
        holder.labNameTextView.setText( this.secciones.get(position).getAula() );
        holder.sedeNameTextView.setText( this.secciones.get(position).getSede() );
        holder.seccionNameTextView.setText( this.secciones.get(position).getPost_title() );
    }

    @Override
    public int getItemCount() {
        return secciones.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView courseNameTextView;
        TextView labNameTextView;
        TextView sedeNameTextView;
        TextView seccionNameTextView;
        ViewHolder(View itemView) {
            super(itemView);
            courseNameTextView = (TextView) itemView.findViewById(R.id.courseNameTextView);
            labNameTextView = (TextView) itemView.findViewById(R.id.labNameTextView);
            sedeNameTextView = (TextView) itemView.findViewById(R.id.sedeNameTextView);
            seccionNameTextView = (TextView) itemView.findViewById(R.id.seccionNameTextView);
        }
    }
}
