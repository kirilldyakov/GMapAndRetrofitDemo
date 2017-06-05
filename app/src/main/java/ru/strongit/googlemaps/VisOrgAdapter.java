package ru.strongit.googlemaps;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ru.strongit.googlemaps.model.OrganizationModel;
import ru.strongit.googlemaps.model.VisitModel;

/**
 * Created by user on 01.06.17.
 */

public class VisOrgAdapter extends RecyclerView.Adapter<VisOrgAdapter.ViewHolder> {

    public interface OnVisitSelectedListener {
        void onVisitSelected(String id);
    }

    public OnVisitSelectedListener onVisitSelectedListener;

    private List<VisitModel> mVisits;
    private List<OrganizationModel> mOrgs;

    //private LinearLayout myBackground;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    public VisOrgAdapter(List<VisitModel> visits, List<OrganizationModel> orgs) {
        this.mVisits = visits;
        this.mOrgs = orgs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.org_visit_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(VisOrgAdapter.ViewHolder holder, int position) {

        VisitModel visit = mVisits.get(position);
        OrganizationModel org = getOrgById(mOrgs, visit.getOrganizationId());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.mVisit.setText(Html.fromHtml(visit.getTitle(), Html.FROM_HTML_MODE_LEGACY));
            holder.mOrg.setText(Html.fromHtml(org != null ? org.getTitle() : "---", Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.mVisit.setText(Html.fromHtml(visit.getTitle()));
            holder.mOrg.setText(Html.fromHtml(org != null ? org.getTitle() : "---"));

        }
        try {
            holder.itemView.setTag(org.getOrganizationId());

            holder.mOrg.setTag(org.getOrganizationId());
            if (selectedItems.get(position, false))
                holder.itemView.setBackgroundColor(Color.GREEN);
            else
                holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private OrganizationModel getOrgById(List<OrganizationModel> orgs, String id) {
        for (int i = 0; i < orgs.size(); i++) {
            String oid = orgs.get(i).getOrganizationId();
            if (oid.equals(id)) {
                return orgs.get(i);
            }
        }
        return null;
    }

    void selectListItem(String id) {
        selectedItems.clear();
        for (int i = 0; i < mVisits.size(); i++) {
            if (mVisits.get(i).getOrganizationId().equals(id)) {
                selectedItems.put(i, true);
            }
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mVisits == null)
            return 0;
        return mVisits.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mVisit;
        TextView mOrg;
        LinearLayout myBackground;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mVisit = (TextView) itemView.findViewById(R.id.visitId);
            mOrg = (TextView) itemView.findViewById(R.id.orgId);
            myBackground = (LinearLayout) itemView.findViewById(R.id.myBackground);
        }

        @Override
        public void onClick(View v) {
            selectedItems.clear();
            if (selectedItems.get(getAdapterPosition(), false)) {
                selectedItems.delete(getAdapterPosition());
            } else {
                selectedItems.put(getAdapterPosition(), true);
            }

            notifyDataSetChanged();

            onVisitSelectedListener.onVisitSelected(v.getTag().toString());
        }

    }
}

