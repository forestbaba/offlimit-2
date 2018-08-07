package com.forestsoftware.offlimit.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.forestsoftware.offlimit.R;
import com.forestsoftware.offlimit.models.keymodel.Key;
import com.forestsoftware.offlimit.models.keymodel.LockkeyModel;

import java.util.List;

public class LockCombinationAdapter extends RecyclerView.Adapter<LockCombinationAdapter.ViewHolder>
{
    private List<LockkeyModel> lockkeyModel;
    Context context;

    public LockCombinationAdapter(Context context, List<LockkeyModel>data) {
        this.context = context;
        this.lockkeyModel = data;
    }


    @NonNull
    @Override
    public LockCombinationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lock_combination_custom, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LockCombinationAdapter.ViewHolder viewHolder, int i)
    {
        LockkeyModel lm = lockkeyModel.get(i);
        final List<Key> combinations = lm.getKeyCombinations();

        if (combinations == null) {
            TextView view = new TextView(context);
            view.setText("No HotKey set");
            view.setTextColor(ContextCompat.getColor(context, R.color.hint_color));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(20, 0, 10, 0);
            view.setLayoutParams(lp);
            view.setTypeface(view.getTypeface(), Typeface.ITALIC);

            viewHolder.t4.addView(view);
            return;
        }

        for (Key k : combinations) {
            TextView view = (TextView) View.inflate(context, R.layout.textview_pill, null);
            view.setHeight(50);
            view.setTextSize(12);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(10, 0, 10, 0);
            view.setLayoutParams(lp);

            view.setText(k.getName());
            viewHolder.t4.addView(view);
        }

    }

    @Override
    public int getItemCount() {
        return lockkeyModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout t4;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            t4 = (LinearLayout) itemView.findViewById(R.id.lin);

        }
    }
}
