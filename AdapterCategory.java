package com.cuna.splashscreen;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterCategory extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<Category> items;

    public AdapterCategory(Activity activity, ArrayList<Category> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = view;

        if (view == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_category, null);
        }

        Category dir = items.get(i);

        TextView title = v.findViewById(R.id.tiendaId);
        title.setText(dir.getId()+"");

        TextView nombre = v.findViewById(R.id.tiendaNombre);
        nombre.setText(dir.getNombre());

        TextView direccion = v.findViewById(R.id.tiendaDireccion);
        direccion.setText(dir.getDireccion());

        TextView latitud = v.findViewById(R.id.tiendaLatitud);
        latitud.setText(dir.getLatitud()+"");

        TextView longitud = v.findViewById(R.id.tiendaLongitud);
        longitud.setText(dir.getLongitud()+"");

        TextView descripcion = v.findViewById(R.id.tiendaDescripcion);
        descripcion.setText(dir.getDescripcion());

        return v;

    }

}
