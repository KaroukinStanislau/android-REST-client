package by.korovkin.restClient.rest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.List;

import by.korovkin.restClient.entity.Reference;

public class ReferenceSpinnerAdaper extends BaseAdapter {

    private List<Reference> references;
    private final LayoutInflater layoutInflater;

    public ReferenceSpinnerAdaper(Context context, List<Reference> references) {
        this.references = references;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.references != null ? references.size() : 0;
    }

    @Override
    public Reference getItem(int i) {
        return this.references.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(android.R.layout.simple_spinner_dropdown_item, viewGroup, false);
        }
        Reference reference = getItem(i);
        if (reference != null) {
            TextView t = (TextView) convertView;
            t.setText(reference.getValue());
        }

        return convertView;
    }
}
