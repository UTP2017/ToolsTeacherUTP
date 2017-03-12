package pe.edu.utp.toolsteacherutp.Adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import pe.edu.utp.toolsteacherutp.Models.DateHorarios;
import pe.edu.utp.toolsteacherutp.R;

/**
 * Created by elbuenpixel on 11/03/17.
 */

public class DateHorariosAdapter extends RecyclerView.Adapter<DateHorariosAdapter.ViewHolder> {
    private List<DateHorarios> dateHorarioses;
    private Context mContext;

    public DateHorariosAdapter(List<DateHorarios> dateHorarioses, Context mContext) {
        this.dateHorarioses = dateHorarioses;
        this.mContext=mContext;
    }

    public void updateData(List<DateHorarios> newHorarios) {
        dateHorarioses.clear();
        dateHorarioses.addAll(newHorarios);
        notifyDataSetChanged();
    }

    public DateHorarios getItem(int position) {
        return dateHorarioses.get(position);
    }

    public void removeItem(int position) {
        dateHorarioses.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_horarios_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DateHorariosAdapter.ViewHolder holder, int position) {
        SimpleDateFormat formatDateNumber = new SimpleDateFormat("d", Locale.getDefault());
        SimpleDateFormat formatDate= new SimpleDateFormat("EEE\nMMM\nyyyy", Locale.getDefault());


        Log.e( "Calendar",this.dateHorarioses.get(position).getDate().toString() );
        String dateNumber = formatDateNumber.format( this.dateHorarioses.get(position).getDate() );
        String date = formatDate.format( this.dateHorarioses.get(position).getDate() );

        date = date.substring(0, 1).toUpperCase() + date.substring(1);

        holder.dateNumberTextView.setText( dateNumber );
        holder.dateDescTextView.setText( date );

        holder.horariosRecyclerView.setHasFixedSize(true);
        holder.mHorariosLayoutManager = new LinearLayoutManager( mContext ) ;
        holder.horariosRecyclerView.setLayoutManager(holder.mHorariosLayoutManager );

        holder.horarioAdapter = new HorarioAdapter( this.dateHorarioses.get(position).getHorarios(), mContext );
        holder.horariosRecyclerView.setAdapter( holder.horarioAdapter );
    }

    @Override
    public int getItemCount() {
        return dateHorarioses.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateNumberTextView;
        TextView dateDescTextView;
        RecyclerView horariosRecyclerView;
        RecyclerView.LayoutManager mHorariosLayoutManager;
        HorarioAdapter horarioAdapter;

        public ViewHolder(View itemView) {
            super(itemView);
            dateNumberTextView = (TextView) itemView.findViewById(R.id.dateNumberTextView);
            dateDescTextView = (TextView) itemView.findViewById(R.id.dateDescTextView);
            horariosRecyclerView = (RecyclerView) itemView.findViewById(R.id.horariosRecyclerView);
        }
    }
}
