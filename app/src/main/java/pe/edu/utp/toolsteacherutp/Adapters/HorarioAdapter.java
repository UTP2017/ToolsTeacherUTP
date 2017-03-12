package pe.edu.utp.toolsteacherutp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pe.edu.utp.toolsteacherutp.Models.DateHorarios;
import pe.edu.utp.toolsteacherutp.Models.Horario;
import pe.edu.utp.toolsteacherutp.R;

/**
 * Created by elbuenpixel on 11/03/17.
 */

public class HorarioAdapter extends RecyclerView.Adapter<HorarioAdapter.ViewHolder> {
    private List<Horario> horarios;
    private Context mContext;

    public HorarioAdapter(List<Horario> horarios, Context mContext) {
        this.horarios = horarios;
        this.mContext=mContext;
    }

    public void updateData(List<Horario> horarios) {
        horarios.clear();
        horarios.addAll(horarios);
        notifyDataSetChanged();
    }

    public Horario getItem(int position) {
        return horarios.get(position);
    }

    public void removeItem(int position) {
        horarios.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public HorarioAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horario_layout, parent, false);
        HorarioAdapter.ViewHolder viewHolder = new HorarioAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HorarioAdapter.ViewHolder holder, int position) {
        holder.hourBetweenTextView.setText( this.horarios.get(position).getHora_inicio() + " - " + this.horarios.get(position).getHora_fin()  );
        holder.courseNameTextView.setText( this.horarios.get(position).getCurso() );
        holder.labNameTextView.setText( this.horarios.get(position).getAula() );
        holder.sedeNameTextView.setText( this.horarios.get(position).getSede() );
        holder.seccionNameTextView.setText( this.horarios.get(position).getSeccion() );
    }

    @Override
    public int getItemCount() {
        return horarios.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView hourBetweenTextView;
        TextView courseNameTextView;
        TextView labNameTextView;
        TextView sedeNameTextView;
        TextView seccionNameTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            hourBetweenTextView = (TextView) itemView.findViewById(R.id.hourBetweenTextView);
            courseNameTextView = (TextView) itemView.findViewById(R.id.courseNameTextView);
            labNameTextView = (TextView) itemView.findViewById(R.id.labNameTextView);
            sedeNameTextView = (TextView) itemView.findViewById(R.id.sedeNameTextView);
            seccionNameTextView = (TextView) itemView.findViewById(R.id.seccionNameTextView);
        }
    }
}
