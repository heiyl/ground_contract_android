package com.diyuewang.m.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.Marker;
import com.diyuewang.m.R;
import com.diyuewang.m.model.MarkerInfoUtil;
import com.diyuewang.m.tools.rvhelper.adapter.CommonAdapter;
import com.diyuewang.m.tools.rvhelper.base.ViewHolder;

public class MarketAdapter  extends CommonAdapter<MarkerInfoUtil> {
    public int selectedPosition;

    public MarketAdapter(Context context) {
        super(context, R.layout.adapter_item_market);
    }

    @Override
    protected void convert(ViewHolder holder, final MarkerInfoUtil markerInfoUtil, int position) {
        double lon = markerInfoUtil.getLongitude();
        double lan = markerInfoUtil.getLatitude();
        final Marker marker = markerInfoUtil.getMarker();
        holder.setText(R.id.tv_lon,"经度：" + lon);
        holder.setText(R.id.tv_lat,"纬度：" + lan);

        TextView tvDel = (TextView) holder.getView(R.id.tv_del);
        tvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(removeMarketListener != null){
                    removeMarketListener.removeMarekt(markerInfoUtil,marker);
                }
            }
        });
    }

    private RemoveMarketListener removeMarketListener;

    public void setRemoveMarketListener(RemoveMarketListener removeMarketListener){
        this.removeMarketListener = removeMarketListener;
    }
    public interface RemoveMarketListener{
        void removeMarekt(MarkerInfoUtil markerInfoUtil,Marker marker);
    }
}