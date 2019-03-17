package mybaidu.admin.example.com.mybaidumapdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AddressView extends RelativeLayout {
    private ImageView ivIcon;
    private TextView tvName;
    private TextView tvDistance;

    public AddressView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setVisibility(View.GONE);
        LayoutInflater.from(context).inflate(R.layout.item_address_info, this);

        ivIcon = findViewById(R.id.iv_icon);
        tvName = findViewById(R.id.tv_name);
        tvDistance = findViewById(R.id.tv_distance);
    }

    public void setAddressInfo(AddressInfo addressInfo) {
        ivIcon.setImageResource(addressInfo.getImageId());
        tvName.setText(addressInfo.getName());
        tvDistance.setText(addressInfo.getDistance());
        setVisibility(View.VISIBLE);
    }
}
