package mybaidu.admin.example.com.mybaidumapdemo;

import java.util.ArrayList;
import java.util.List;

public class AddressInfoLab {
    public static List<AddressInfo> generateDatas() {
        List<AddressInfo> addressInfos = new ArrayList<>();
        addressInfos.add(new AddressInfo(39.880761,116.483438,
                R.drawable.building, "隆聚祥旅馆", "距离313米"));
        addressInfos.add(new AddressInfo(39.879294,116.483326,
                R.drawable.building, "汉庭酒店", "距离520米"));
        return addressInfos;
    }
}
